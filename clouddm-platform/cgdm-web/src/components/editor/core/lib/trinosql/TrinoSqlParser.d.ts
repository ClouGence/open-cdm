import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { RuleContext } from "antlr4ts/RuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { TrinoSqlListener } from "./TrinoSqlListener";
import { TrinoSqlVisitor } from "./TrinoSqlVisitor";
export declare class TrinoSqlParser extends Parser {
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
    static readonly T__11 = 12;
    static readonly T__12 = 13;
    static readonly T__13 = 14;
    static readonly T__14 = 15;
    static readonly T__15 = 16;
    static readonly KW_ADD = 17;
    static readonly KW_ADMIN = 18;
    static readonly KW_AFTER = 19;
    static readonly KW_ALL = 20;
    static readonly KW_ALTER = 21;
    static readonly KW_ANALYZE = 22;
    static readonly KW_AND = 23;
    static readonly KW_ANY = 24;
    static readonly KW_ARRAY = 25;
    static readonly KW_AS = 26;
    static readonly KW_ASC = 27;
    static readonly KW_AT = 28;
    static readonly KW_AUTHORIZATION = 29;
    static readonly KW_BERNOULLI = 30;
    static readonly KW_BETWEEN = 31;
    static readonly KW_BY = 32;
    static readonly KW_CALL = 33;
    static readonly KW_CASCADE = 34;
    static readonly KW_CASE = 35;
    static readonly KW_CAST = 36;
    static readonly KW_CATALOGS = 37;
    static readonly KW_COLUMN = 38;
    static readonly KW_COLUMNS = 39;
    static readonly KW_COMMENT = 40;
    static readonly KW_COMMIT = 41;
    static readonly KW_COMMITTED = 42;
    static readonly KW_CONSTRAINT = 43;
    static readonly KW_CREATE = 44;
    static readonly KW_CROSS = 45;
    static readonly KW_CUBE = 46;
    static readonly KW_CURRENT = 47;
    static readonly KW_CURRENT_CATALOG = 48;
    static readonly KW_CURRENT_DATE = 49;
    static readonly KW_CURRENT_PATH = 50;
    static readonly KW_CURRENT_ROLE = 51;
    static readonly KW_CURRENT_SCHEMA = 52;
    static readonly KW_CURRENT_TIME = 53;
    static readonly KW_CURRENT_TIMESTAMP = 54;
    static readonly KW_CURRENT_USER = 55;
    static readonly KW_DATA = 56;
    static readonly KW_DATE = 57;
    static readonly KW_DAY = 58;
    static readonly KW_DEFAULT = 59;
    static readonly KW_DEALLOCATE = 60;
    static readonly KW_DEFINER = 61;
    static readonly KW_DELETE = 62;
    static readonly KW_DESC = 63;
    static readonly KW_DESCRIBE = 64;
    static readonly KW_DEFINE = 65;
    static readonly KW_DISTINCT = 66;
    static readonly KW_DISTRIBUTED = 67;
    static readonly KW_DOUBLE = 68;
    static readonly KW_DROP = 69;
    static readonly KW_ELSE = 70;
    static readonly KW_EMPTY = 71;
    static readonly KW_END = 72;
    static readonly KW_ESCAPE = 73;
    static readonly KW_EXCEPT = 74;
    static readonly KW_EXCLUDING = 75;
    static readonly KW_EXECUTE = 76;
    static readonly KW_EXISTS = 77;
    static readonly KW_EXPLAIN = 78;
    static readonly KW_EXTRACT = 79;
    static readonly KW_FALSE = 80;
    static readonly KW_FETCH = 81;
    static readonly KW_FILTER = 82;
    static readonly KW_FINAL = 83;
    static readonly KW_FIRST = 84;
    static readonly KW_FOLLOWING = 85;
    static readonly KW_FOR = 86;
    static readonly KW_FORMAT = 87;
    static readonly KW_FROM = 88;
    static readonly KW_FULL = 89;
    static readonly KW_FUNCTIONS = 90;
    static readonly KW_GRANT = 91;
    static readonly KW_GRANTED = 92;
    static readonly KW_GRANTS = 93;
    static readonly KW_DENY = 94;
    static readonly KW_GRAPHVIZ = 95;
    static readonly KW_GROUP = 96;
    static readonly KW_GROUPING = 97;
    static readonly KW_GROUPS = 98;
    static readonly KW_HAVING = 99;
    static readonly KW_HOUR = 100;
    static readonly KW_IF = 101;
    static readonly KW_IGNORE = 102;
    static readonly KW_IN = 103;
    static readonly KW_INCLUDING = 104;
    static readonly KW_INITIAL = 105;
    static readonly KW_INNER = 106;
    static readonly KW_INPUT = 107;
    static readonly KW_INSERT = 108;
    static readonly KW_INTERSECT = 109;
    static readonly KW_INTERVAL = 110;
    static readonly KW_INTO = 111;
    static readonly KW_INVOKER = 112;
    static readonly KW_IO = 113;
    static readonly KW_IS = 114;
    static readonly KW_ISOLATION = 115;
    static readonly KW_JOIN = 116;
    static readonly KW_JSON = 117;
    static readonly KW_LAST = 118;
    static readonly KW_LATERAL = 119;
    static readonly KW_LEFT = 120;
    static readonly KW_LEVEL = 121;
    static readonly KW_LIKE = 122;
    static readonly KW_LIMIT = 123;
    static readonly KW_LOCAL = 124;
    static readonly KW_LOCALTIME = 125;
    static readonly KW_LOCALTIMESTAMP = 126;
    static readonly KW_LOGICAL = 127;
    static readonly KW_MAP = 128;
    static readonly KW_MATCH = 129;
    static readonly KW_MATCHED = 130;
    static readonly KW_MATCHES = 131;
    static readonly KW_MATCH_RECOGNIZE = 132;
    static readonly KW_MATERIALIZED = 133;
    static readonly KW_MEASURES = 134;
    static readonly KW_MERGE = 135;
    static readonly KW_MINUTE = 136;
    static readonly KW_MONTH = 137;
    static readonly KW_NATURAL = 138;
    static readonly KW_NEXT = 139;
    static readonly KW_NFC = 140;
    static readonly KW_NFD = 141;
    static readonly KW_NFKC = 142;
    static readonly KW_NFKD = 143;
    static readonly KW_NO = 144;
    static readonly KW_NONE = 145;
    static readonly KW_NORMALIZE = 146;
    static readonly KW_NOT = 147;
    static readonly KW_NULL = 148;
    static readonly KW_NULLIF = 149;
    static readonly KW_NULLS = 150;
    static readonly KW_OFFSET = 151;
    static readonly KW_OMIT = 152;
    static readonly KW_ON = 153;
    static readonly KW_ONE = 154;
    static readonly KW_ONLY = 155;
    static readonly KW_OPTION = 156;
    static readonly KW_OR = 157;
    static readonly KW_ORDER = 158;
    static readonly KW_ORDINALITY = 159;
    static readonly KW_OUTER = 160;
    static readonly KW_OUTPUT = 161;
    static readonly KW_OVER = 162;
    static readonly KW_PARTITION = 163;
    static readonly KW_PARTITIONS = 164;
    static readonly KW_PAST = 165;
    static readonly KW_PATH = 166;
    static readonly KW_PATTERN = 167;
    static readonly KW_PER = 168;
    static readonly KW_PERMUTE = 169;
    static readonly KW_POSITION = 170;
    static readonly KW_PRECEDING = 171;
    static readonly KW_PRECISION = 172;
    static readonly KW_PREPARE = 173;
    static readonly KW_PRIVILEGES = 174;
    static readonly KW_PROPERTIES = 175;
    static readonly KW_RANGE = 176;
    static readonly KW_READ = 177;
    static readonly KW_RECURSIVE = 178;
    static readonly KW_REFRESH = 179;
    static readonly KW_RENAME = 180;
    static readonly KW_REPEATABLE = 181;
    static readonly KW_REPLACE = 182;
    static readonly KW_RESET = 183;
    static readonly KW_RESPECT = 184;
    static readonly KW_RESTRICT = 185;
    static readonly KW_REVOKE = 186;
    static readonly KW_RIGHT = 187;
    static readonly KW_ROLE = 188;
    static readonly KW_ROLES = 189;
    static readonly KW_ROLLBACK = 190;
    static readonly KW_ROLLUP = 191;
    static readonly KW_ROW = 192;
    static readonly KW_ROWS = 193;
    static readonly KW_RUNNING = 194;
    static readonly KW_SCHEMA = 195;
    static readonly KW_SCHEMAS = 196;
    static readonly KW_SECOND = 197;
    static readonly KW_SECURITY = 198;
    static readonly KW_SEEK = 199;
    static readonly KW_SELECT = 200;
    static readonly KW_SERIALIZABLE = 201;
    static readonly KW_SESSION = 202;
    static readonly KW_SET = 203;
    static readonly KW_SETS = 204;
    static readonly KW_SHOW = 205;
    static readonly KW_SOME = 206;
    static readonly KW_START = 207;
    static readonly KW_STATS = 208;
    static readonly KW_SUBSET = 209;
    static readonly KW_SUBSTRING = 210;
    static readonly KW_SYSTEM = 211;
    static readonly KW_TABLE = 212;
    static readonly KW_TABLES = 213;
    static readonly KW_TABLESAMPLE = 214;
    static readonly KW_TEXT = 215;
    static readonly KW_THEN = 216;
    static readonly KW_TIES = 217;
    static readonly KW_TIME = 218;
    static readonly KW_TIMESTAMP = 219;
    static readonly KW_TO = 220;
    static readonly KW_TRANSACTION = 221;
    static readonly KW_TRUNCATE = 222;
    static readonly KW_TRUE = 223;
    static readonly KW_TRY_CAST = 224;
    static readonly KW_TYPE = 225;
    static readonly KW_UESCAPE = 226;
    static readonly KW_UNBOUNDED = 227;
    static readonly KW_UNCOMMITTED = 228;
    static readonly KW_UNION = 229;
    static readonly KW_UNMATCHED = 230;
    static readonly KW_UNNEST = 231;
    static readonly KW_UPDATE = 232;
    static readonly KW_USE = 233;
    static readonly KW_USER = 234;
    static readonly KW_USING = 235;
    static readonly KW_VALIDATE = 236;
    static readonly KW_VALUES = 237;
    static readonly KW_VERBOSE = 238;
    static readonly KW_VIEW = 239;
    static readonly KW_WHEN = 240;
    static readonly KW_WHERE = 241;
    static readonly KW_WINDOW = 242;
    static readonly KW_WITH = 243;
    static readonly KW_WITHOUT = 244;
    static readonly KW_WORK = 245;
    static readonly KW_WRITE = 246;
    static readonly KW_YEAR = 247;
    static readonly KW_ZONE = 248;
    static readonly EQ = 249;
    static readonly NEQ = 250;
    static readonly LT = 251;
    static readonly LTE = 252;
    static readonly GT = 253;
    static readonly GTE = 254;
    static readonly PLUS = 255;
    static readonly MINUS = 256;
    static readonly ASTERISK = 257;
    static readonly SLASH = 258;
    static readonly PERCENT = 259;
    static readonly CONCAT = 260;
    static readonly QUESTION_MARK = 261;
    static readonly STRING = 262;
    static readonly UNICODE_STRING = 263;
    static readonly BINARY_LITERAL = 264;
    static readonly INTEGER_VALUE = 265;
    static readonly DECIMAL_VALUE = 266;
    static readonly DOUBLE_VALUE = 267;
    static readonly IDENTIFIER = 268;
    static readonly DIGIT_IDENTIFIER = 269;
    static readonly QUOTED_IDENTIFIER = 270;
    static readonly BACKQUOTED_IDENTIFIER = 271;
    static readonly SEMICOLON = 272;
    static readonly SIMPLE_COMMENT = 273;
    static readonly BRACKETED_COMMENT = 274;
    static readonly WS = 275;
    static readonly UNRECOGNIZED = 276;
    static readonly DELIMITER = 277;
    static readonly RULE_program = 0;
    static readonly RULE_statements = 1;
    static readonly RULE_singleStatement = 2;
    static readonly RULE_standaloneExpression = 3;
    static readonly RULE_standalonePathSpecification = 4;
    static readonly RULE_standaloneType = 5;
    static readonly RULE_standaloneRowPattern = 6;
    static readonly RULE_statement = 7;
    static readonly RULE_query = 8;
    static readonly RULE_with = 9;
    static readonly RULE_tableElement = 10;
    static readonly RULE_columnDefinition = 11;
    static readonly RULE_likeClause = 12;
    static readonly RULE_properties = 13;
    static readonly RULE_propertyAssignments = 14;
    static readonly RULE_property = 15;
    static readonly RULE_propertyValue = 16;
    static readonly RULE_queryNoWith = 17;
    static readonly RULE_limitRowCount = 18;
    static readonly RULE_rowCount = 19;
    static readonly RULE_queryTerm = 20;
    static readonly RULE_queryPrimary = 21;
    static readonly RULE_sortItem = 22;
    static readonly RULE_querySpecification = 23;
    static readonly RULE_groupBy = 24;
    static readonly RULE_groupingElement = 25;
    static readonly RULE_groupingSet = 26;
    static readonly RULE_groupingTerm = 27;
    static readonly RULE_windowDefinition = 28;
    static readonly RULE_windowSpecification = 29;
    static readonly RULE_namedQuery = 30;
    static readonly RULE_setQuantifier = 31;
    static readonly RULE_selectItem = 32;
    static readonly RULE_relation = 33;
    static readonly RULE_joinType = 34;
    static readonly RULE_joinCriteria = 35;
    static readonly RULE_sampledRelation = 36;
    static readonly RULE_sampleType = 37;
    static readonly RULE_patternRecognition = 38;
    static readonly RULE_measureDefinition = 39;
    static readonly RULE_rowsPerMatch = 40;
    static readonly RULE_emptyMatchHandling = 41;
    static readonly RULE_skipTo = 42;
    static readonly RULE_subsetDefinition = 43;
    static readonly RULE_variableDefinition = 44;
    static readonly RULE_aliasedRelation = 45;
    static readonly RULE_columnListCreate = 46;
    static readonly RULE_columnList = 47;
    static readonly RULE_columnAliases = 48;
    static readonly RULE_relationPrimary = 49;
    static readonly RULE_expression = 50;
    static readonly RULE_booleanExpression = 51;
    static readonly RULE_predicate = 52;
    static readonly RULE_valueExpression = 53;
    static readonly RULE_primaryExpression = 54;
    static readonly RULE_processingMode = 55;
    static readonly RULE_nullTreatment = 56;
    static readonly RULE_string = 57;
    static readonly RULE_timeZoneSpecifier = 58;
    static readonly RULE_comparisonOperator = 59;
    static readonly RULE_comparisonQuantifier = 60;
    static readonly RULE_booleanValue = 61;
    static readonly RULE_interval = 62;
    static readonly RULE_intervalField = 63;
    static readonly RULE_normalForm = 64;
    static readonly RULE_type = 65;
    static readonly RULE_rowField = 66;
    static readonly RULE_typeParameter = 67;
    static readonly RULE_whenClause = 68;
    static readonly RULE_filter = 69;
    static readonly RULE_mergeCase = 70;
    static readonly RULE_over = 71;
    static readonly RULE_windowFrame = 72;
    static readonly RULE_frameExtent = 73;
    static readonly RULE_frameBound = 74;
    static readonly RULE_rowPattern = 75;
    static readonly RULE_patternPrimary = 76;
    static readonly RULE_patternQuantifier = 77;
    static readonly RULE_updateAssignment = 78;
    static readonly RULE_explainOption = 79;
    static readonly RULE_transactionMode = 80;
    static readonly RULE_levelOfIsolation = 81;
    static readonly RULE_callArgument = 82;
    static readonly RULE_pathElement = 83;
    static readonly RULE_pathSpecification = 84;
    static readonly RULE_privilege = 85;
    static readonly RULE_tableOrViewName = 86;
    static readonly RULE_tableName = 87;
    static readonly RULE_tableNameCreate = 88;
    static readonly RULE_viewName = 89;
    static readonly RULE_viewNameCreate = 90;
    static readonly RULE_tablePath = 91;
    static readonly RULE_viewPath = 92;
    static readonly RULE_schemaName = 93;
    static readonly RULE_schemaNameCreate = 94;
    static readonly RULE_schemaPath = 95;
    static readonly RULE_catalogName = 96;
    static readonly RULE_catalogNameCreate = 97;
    static readonly RULE_functionName = 98;
    static readonly RULE_columnName = 99;
    static readonly RULE_columnNameCreate = 100;
    static readonly RULE_qualifiedName = 101;
    static readonly RULE_grantor = 102;
    static readonly RULE_principal = 103;
    static readonly RULE_roles = 104;
    static readonly RULE_identifier = 105;
    static readonly RULE_number = 106;
    static readonly RULE_nonReserved = 107;
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
    statements(): StatementsContext;
    singleStatement(): SingleStatementContext;
    standaloneExpression(): StandaloneExpressionContext;
    standalonePathSpecification(): StandalonePathSpecificationContext;
    standaloneType(): StandaloneTypeContext;
    standaloneRowPattern(): StandaloneRowPatternContext;
    statement(): StatementContext;
    query(): QueryContext;
    with(): WithContext;
    tableElement(): TableElementContext;
    columnDefinition(): ColumnDefinitionContext;
    likeClause(): LikeClauseContext;
    properties(): PropertiesContext;
    propertyAssignments(): PropertyAssignmentsContext;
    property(): PropertyContext;
    propertyValue(): PropertyValueContext;
    queryNoWith(): QueryNoWithContext;
    limitRowCount(): LimitRowCountContext;
    rowCount(): RowCountContext;
    queryTerm(): QueryTermContext;
    queryTerm(_p: number): QueryTermContext;
    queryPrimary(): QueryPrimaryContext;
    sortItem(): SortItemContext;
    querySpecification(): QuerySpecificationContext;
    groupBy(): GroupByContext;
    groupingElement(): GroupingElementContext;
    groupingSet(): GroupingSetContext;
    groupingTerm(): GroupingTermContext;
    windowDefinition(): WindowDefinitionContext;
    windowSpecification(): WindowSpecificationContext;
    namedQuery(): NamedQueryContext;
    setQuantifier(): SetQuantifierContext;
    selectItem(): SelectItemContext;
    relation(): RelationContext;
    relation(_p: number): RelationContext;
    joinType(): JoinTypeContext;
    joinCriteria(): JoinCriteriaContext;
    sampledRelation(): SampledRelationContext;
    sampleType(): SampleTypeContext;
    patternRecognition(): PatternRecognitionContext;
    measureDefinition(): MeasureDefinitionContext;
    rowsPerMatch(): RowsPerMatchContext;
    emptyMatchHandling(): EmptyMatchHandlingContext;
    skipTo(): SkipToContext;
    subsetDefinition(): SubsetDefinitionContext;
    variableDefinition(): VariableDefinitionContext;
    aliasedRelation(): AliasedRelationContext;
    columnListCreate(): ColumnListCreateContext;
    columnList(): ColumnListContext;
    columnAliases(): ColumnAliasesContext;
    relationPrimary(): RelationPrimaryContext;
    expression(): ExpressionContext;
    booleanExpression(): BooleanExpressionContext;
    booleanExpression(_p: number): BooleanExpressionContext;
    predicate(value: ParserRuleContext): PredicateContext;
    valueExpression(): ValueExpressionContext;
    valueExpression(_p: number): ValueExpressionContext;
    primaryExpression(): PrimaryExpressionContext;
    primaryExpression(_p: number): PrimaryExpressionContext;
    processingMode(): ProcessingModeContext;
    nullTreatment(): NullTreatmentContext;
    string(): StringContext;
    timeZoneSpecifier(): TimeZoneSpecifierContext;
    comparisonOperator(): ComparisonOperatorContext;
    comparisonQuantifier(): ComparisonQuantifierContext;
    booleanValue(): BooleanValueContext;
    interval(): IntervalContext;
    intervalField(): IntervalFieldContext;
    normalForm(): NormalFormContext;
    type(): TypeContext;
    type(_p: number): TypeContext;
    rowField(): RowFieldContext;
    typeParameter(): TypeParameterContext;
    whenClause(): WhenClauseContext;
    filter(): FilterContext;
    mergeCase(): MergeCaseContext;
    over(): OverContext;
    windowFrame(): WindowFrameContext;
    frameExtent(): FrameExtentContext;
    frameBound(): FrameBoundContext;
    rowPattern(): RowPatternContext;
    rowPattern(_p: number): RowPatternContext;
    patternPrimary(): PatternPrimaryContext;
    patternQuantifier(): PatternQuantifierContext;
    updateAssignment(): UpdateAssignmentContext;
    explainOption(): ExplainOptionContext;
    transactionMode(): TransactionModeContext;
    levelOfIsolation(): LevelOfIsolationContext;
    callArgument(): CallArgumentContext;
    pathElement(): PathElementContext;
    pathSpecification(): PathSpecificationContext;
    privilege(): PrivilegeContext;
    tableOrViewName(): TableOrViewNameContext;
    tableName(): TableNameContext;
    tableNameCreate(): TableNameCreateContext;
    viewName(): ViewNameContext;
    viewNameCreate(): ViewNameCreateContext;
    tablePath(): TablePathContext;
    viewPath(): ViewPathContext;
    schemaName(): SchemaNameContext;
    schemaNameCreate(): SchemaNameCreateContext;
    schemaPath(): SchemaPathContext;
    catalogName(): CatalogNameContext;
    catalogNameCreate(): CatalogNameCreateContext;
    functionName(): FunctionNameContext;
    columnName(): ColumnNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    qualifiedName(): QualifiedNameContext;
    grantor(): GrantorContext;
    principal(): PrincipalContext;
    roles(): RolesContext;
    identifier(): IdentifierContext;
    number(): NumberContext;
    nonReserved(): NonReservedContext;
    sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean;
    private queryTerm_sempred;
    private relation_sempred;
    private booleanExpression_sempred;
    private valueExpression_sempred;
    private primaryExpression_sempred;
    private type_sempred;
    private rowPattern_sempred;
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
    statements(): StatementsContext[];
    statements(i: number): StatementsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StatementsContext extends ParserRuleContext {
    singleStatement(): SingleStatementContext | undefined;
    standaloneExpression(): StandaloneExpressionContext | undefined;
    standalonePathSpecification(): StandalonePathSpecificationContext | undefined;
    standaloneType(): StandaloneTypeContext | undefined;
    standaloneRowPattern(): StandaloneRowPatternContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SingleStatementContext extends ParserRuleContext {
    statement(): StatementContext;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StandaloneExpressionContext extends ParserRuleContext {
    expression(): ExpressionContext;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StandalonePathSpecificationContext extends ParserRuleContext {
    pathSpecification(): PathSpecificationContext;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StandaloneTypeContext extends ParserRuleContext {
    type(): TypeContext;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StandaloneRowPatternContext extends ParserRuleContext {
    rowPattern(): RowPatternContext;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: StatementContext): void;
}
export declare class StatementDefaultContext extends StatementContext {
    query(): QueryContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UseContext extends StatementContext {
    KW_USE(): TerminalNode;
    schemaName(): SchemaNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CreateSchemaContext extends StatementContext {
    KW_CREATE(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schemaNameCreate(): SchemaNameCreateContext;
    KW_IF(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    principal(): PrincipalContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DropSchemaContext extends StatementContext {
    KW_DROP(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schemaName(): SchemaNameContext;
    KW_IF(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RenameSchemaContext extends StatementContext {
    KW_ALTER(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schemaName(): SchemaNameContext;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    schemaNameCreate(): SchemaNameCreateContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetSchemaAuthorizationContext extends StatementContext {
    KW_ALTER(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schemaName(): SchemaNameContext;
    KW_SET(): TerminalNode;
    KW_AUTHORIZATION(): TerminalNode;
    principal(): PrincipalContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CreateTableAsSelectContext extends StatementContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    KW_AS(): TerminalNode;
    query(): QueryContext | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    columnListCreate(): ColumnListCreateContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    string(): StringContext | undefined;
    KW_WITH(): TerminalNode[];
    KW_WITH(i: number): TerminalNode;
    properties(): PropertiesContext | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CreateTableContext extends StatementContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    tableElement(): TableElementContext[];
    tableElement(i: number): TableElementContext;
    KW_IF(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    string(): StringContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DropTableContext extends StatementContext {
    KW_DROP(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_IF(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class InsertIntoContext extends StatementContext {
    KW_INSERT(): TerminalNode;
    KW_INTO(): TerminalNode;
    tableName(): TableNameContext;
    query(): QueryContext;
    columnList(): ColumnListContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DeleteContext extends StatementContext {
    KW_DELETE(): TerminalNode;
    KW_FROM(): TerminalNode;
    tableName(): TableNameContext;
    KW_WHERE(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TruncateTableContext extends StatementContext {
    KW_TRUNCATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RenameTableContext extends StatementContext {
    _from: TableNameContext;
    _to: TableNameCreateContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    tableName(): TableNameContext;
    tableNameCreate(): TableNameCreateContext;
    KW_IF(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CommentTableContext extends StatementContext {
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_IS(): TerminalNode;
    string(): StringContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CommentColumnContext extends StatementContext {
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    columnName(): ColumnNameContext;
    KW_IS(): TerminalNode;
    string(): StringContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RenameColumnContext extends StatementContext {
    _from: ColumnNameContext;
    _to: ColumnNameCreateContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_RENAME(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    KW_TO(): TerminalNode;
    columnName(): ColumnNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    KW_IF(): TerminalNode[];
    KW_IF(i: number): TerminalNode;
    KW_EXISTS(): TerminalNode[];
    KW_EXISTS(i: number): TerminalNode;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DropColumnContext extends StatementContext {
    _column: ColumnNameContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_DROP(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    columnName(): ColumnNameContext;
    KW_IF(): TerminalNode[];
    KW_IF(i: number): TerminalNode;
    KW_EXISTS(): TerminalNode[];
    KW_EXISTS(i: number): TerminalNode;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class AddColumnContext extends StatementContext {
    _column: ColumnDefinitionContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_ADD(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    columnDefinition(): ColumnDefinitionContext;
    KW_IF(): TerminalNode[];
    KW_IF(i: number): TerminalNode;
    KW_EXISTS(): TerminalNode[];
    KW_EXISTS(i: number): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetTableAuthorizationContext extends StatementContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_SET(): TerminalNode;
    KW_AUTHORIZATION(): TerminalNode;
    principal(): PrincipalContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetTablePropertiesContext extends StatementContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_SET(): TerminalNode;
    KW_PROPERTIES(): TerminalNode;
    propertyAssignments(): PropertyAssignmentsContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TableExecuteContext extends StatementContext {
    _procedureName: IdentifierContext;
    _where: BooleanExpressionContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_EXECUTE(): TerminalNode;
    identifier(): IdentifierContext;
    KW_WHERE(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    callArgument(): CallArgumentContext[];
    callArgument(i: number): CallArgumentContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class AnalyzeContext extends StatementContext {
    KW_ANALYZE(): TerminalNode;
    tableName(): TableNameContext;
    KW_WITH(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CreateMaterializedViewContext extends StatementContext {
    KW_CREATE(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNameCreate(): ViewNameCreateContext;
    KW_AS(): TerminalNode;
    query(): QueryContext;
    KW_OR(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    string(): StringContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CreateViewContext extends StatementContext {
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNameCreate(): ViewNameCreateContext;
    KW_AS(): TerminalNode;
    query(): QueryContext;
    KW_OR(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    string(): StringContext | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RefreshMaterializedViewContext extends StatementContext {
    KW_REFRESH(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DropMaterializedViewContext extends StatementContext {
    KW_DROP(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    KW_IF(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RenameMaterializedViewContext extends StatementContext {
    _from: ViewNameContext;
    _to: ViewNameCreateContext;
    KW_ALTER(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    viewName(): ViewNameContext;
    viewNameCreate(): ViewNameCreateContext;
    KW_IF(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetMaterializedViewPropertiesContext extends StatementContext {
    KW_ALTER(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    KW_SET(): TerminalNode;
    KW_PROPERTIES(): TerminalNode;
    propertyAssignments(): PropertyAssignmentsContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DropViewContext extends StatementContext {
    KW_DROP(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    KW_IF(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RenameViewContext extends StatementContext {
    _from: ViewNameContext;
    _to: ViewNameCreateContext;
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    viewName(): ViewNameContext;
    viewNameCreate(): ViewNameCreateContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetViewAuthorizationContext extends StatementContext {
    _from: ViewNameContext;
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    KW_SET(): TerminalNode;
    KW_AUTHORIZATION(): TerminalNode;
    principal(): PrincipalContext;
    viewName(): ViewNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CallContext extends StatementContext {
    KW_CALL(): TerminalNode;
    functionName(): FunctionNameContext;
    callArgument(): CallArgumentContext[];
    callArgument(i: number): CallArgumentContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CreateRoleContext extends StatementContext {
    _name: IdentifierContext;
    KW_CREATE(): TerminalNode;
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext;
    KW_WITH(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    grantor(): GrantorContext | undefined;
    KW_IN(): TerminalNode | undefined;
    catalogName(): CatalogNameContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DropRoleContext extends StatementContext {
    _name: IdentifierContext;
    KW_DROP(): TerminalNode;
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GrantRolesContext extends StatementContext {
    KW_GRANT(): TerminalNode;
    roles(): RolesContext;
    KW_TO(): TerminalNode;
    principal(): PrincipalContext[];
    principal(i: number): PrincipalContext;
    KW_WITH(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_GRANTED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    grantor(): GrantorContext | undefined;
    KW_IN(): TerminalNode | undefined;
    catalogName(): CatalogNameContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RevokeRolesContext extends StatementContext {
    KW_REVOKE(): TerminalNode;
    roles(): RolesContext;
    KW_FROM(): TerminalNode;
    principal(): PrincipalContext[];
    principal(i: number): PrincipalContext;
    KW_ADMIN(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_GRANTED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    grantor(): GrantorContext | undefined;
    KW_IN(): TerminalNode | undefined;
    catalogName(): CatalogNameContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetRoleContext extends StatementContext {
    _role: IdentifierContext;
    KW_SET(): TerminalNode;
    KW_ROLE(): TerminalNode;
    KW_ALL(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    KW_IN(): TerminalNode | undefined;
    catalogName(): CatalogNameContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GrantContext extends StatementContext {
    _grantee: PrincipalContext;
    KW_GRANT(): TerminalNode[];
    KW_GRANT(i: number): TerminalNode;
    KW_ON(): TerminalNode;
    KW_TO(): TerminalNode;
    principal(): PrincipalContext;
    privilege(): PrivilegeContext[];
    privilege(i: number): PrivilegeContext;
    KW_ALL(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    schemaName(): SchemaNameContext | undefined;
    tableName(): TableNameContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DenyContext extends StatementContext {
    _grantee: PrincipalContext;
    KW_DENY(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_TO(): TerminalNode;
    principal(): PrincipalContext;
    privilege(): PrivilegeContext[];
    privilege(i: number): PrivilegeContext;
    KW_ALL(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    schemaName(): SchemaNameContext | undefined;
    tableName(): TableNameContext | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RevokeContext extends StatementContext {
    _grantee: PrincipalContext;
    KW_REVOKE(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_FROM(): TerminalNode;
    principal(): PrincipalContext;
    privilege(): PrivilegeContext[];
    privilege(i: number): PrivilegeContext;
    KW_ALL(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_GRANT(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    schemaName(): SchemaNameContext | undefined;
    tableName(): TableNameContext | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowGrantsContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_GRANTS(): TerminalNode;
    KW_ON(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExplainContext extends StatementContext {
    KW_EXPLAIN(): TerminalNode;
    statement(): StatementContext;
    KW_ANALYZE(): TerminalNode | undefined;
    KW_VERBOSE(): TerminalNode | undefined;
    explainOption(): ExplainOptionContext[];
    explainOption(i: number): ExplainOptionContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowCreateTableContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowCreateSchemaContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schemaName(): SchemaNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowCreateViewContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowCreateMaterializedViewContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowTablesContext extends StatementContext {
    _pattern: StringContext;
    _escape: StringContext;
    KW_SHOW(): TerminalNode;
    KW_TABLES(): TerminalNode;
    schemaName(): SchemaNameContext | undefined;
    KW_LIKE(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowSchemasContext extends StatementContext {
    _pattern: StringContext;
    _escape: StringContext;
    KW_SHOW(): TerminalNode;
    KW_SCHEMAS(): TerminalNode;
    catalogName(): CatalogNameContext | undefined;
    KW_LIKE(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowCatalogsContext extends StatementContext {
    _pattern: StringContext;
    _escape: StringContext;
    KW_SHOW(): TerminalNode;
    KW_CATALOGS(): TerminalNode;
    KW_LIKE(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowColumnsContext extends StatementContext {
    _pattern: StringContext;
    _escape: StringContext;
    KW_SHOW(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    tableOrViewName(): TableOrViewNameContext | undefined;
    KW_LIKE(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    KW_ESCAPE(): TerminalNode | undefined;
    KW_DESCRIBE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowStatsContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_STATS(): TerminalNode;
    KW_FOR(): TerminalNode;
    tableName(): TableNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowStatsForQueryContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_STATS(): TerminalNode;
    KW_FOR(): TerminalNode;
    query(): QueryContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowRolesContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_ROLES(): TerminalNode;
    KW_CURRENT(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowRoleGrantsContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_ROLE(): TerminalNode;
    KW_GRANTS(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowFunctionsContext extends StatementContext {
    _pattern: StringContext;
    _escape: StringContext;
    KW_SHOW(): TerminalNode;
    KW_FUNCTIONS(): TerminalNode;
    KW_LIKE(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowSessionContext extends StatementContext {
    _pattern: StringContext;
    _escape: StringContext;
    KW_SHOW(): TerminalNode;
    KW_SESSION(): TerminalNode;
    KW_LIKE(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetSessionContext extends StatementContext {
    KW_SET(): TerminalNode;
    KW_SESSION(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    EQ(): TerminalNode;
    expression(): ExpressionContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ResetSessionContext extends StatementContext {
    KW_RESET(): TerminalNode;
    KW_SESSION(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StartTransactionContext extends StatementContext {
    KW_START(): TerminalNode;
    KW_TRANSACTION(): TerminalNode;
    transactionMode(): TransactionModeContext[];
    transactionMode(i: number): TransactionModeContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CommitContext extends StatementContext {
    KW_COMMIT(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RollbackContext extends StatementContext {
    KW_ROLLBACK(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PrepareContext extends StatementContext {
    KW_PREPARE(): TerminalNode;
    identifier(): IdentifierContext;
    KW_FROM(): TerminalNode;
    statement(): StatementContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DeallocateContext extends StatementContext {
    KW_DEALLOCATE(): TerminalNode;
    KW_PREPARE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExecuteContext extends StatementContext {
    KW_EXECUTE(): TerminalNode;
    identifier(): IdentifierContext;
    KW_USING(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DescribeInputContext extends StatementContext {
    KW_DESCRIBE(): TerminalNode;
    KW_INPUT(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DescribeOutputContext extends StatementContext {
    KW_DESCRIBE(): TerminalNode;
    KW_OUTPUT(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetPathContext extends StatementContext {
    KW_SET(): TerminalNode;
    KW_PATH(): TerminalNode;
    pathSpecification(): PathSpecificationContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetTimeZoneContext extends StatementContext {
    KW_SET(): TerminalNode;
    KW_TIME(): TerminalNode;
    KW_ZONE(): TerminalNode;
    KW_LOCAL(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UpdateContext extends StatementContext {
    _where: BooleanExpressionContext;
    KW_UPDATE(): TerminalNode;
    tableName(): TableNameContext;
    KW_SET(): TerminalNode;
    updateAssignment(): UpdateAssignmentContext[];
    updateAssignment(i: number): UpdateAssignmentContext;
    KW_WHERE(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MergeContext extends StatementContext {
    KW_MERGE(): TerminalNode;
    KW_INTO(): TerminalNode;
    tableName(): TableNameContext;
    KW_USING(): TerminalNode;
    relation(): RelationContext;
    KW_ON(): TerminalNode;
    expression(): ExpressionContext;
    identifier(): IdentifierContext | undefined;
    mergeCase(): MergeCaseContext[];
    mergeCase(i: number): MergeCaseContext;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowTableCommentContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ShowColumnCommentContext extends StatementContext {
    KW_SHOW(): TerminalNode;
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    columnName(): ColumnNameContext;
    constructor(ctx: StatementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QueryContext extends ParserRuleContext {
    queryNoWith(): QueryNoWithContext;
    with(): WithContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class WithContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    namedQuery(): NamedQueryContext[];
    namedQuery(i: number): NamedQueryContext;
    KW_RECURSIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TableElementContext extends ParserRuleContext {
    columnDefinition(): ColumnDefinitionContext | undefined;
    likeClause(): LikeClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate(): ColumnNameCreateContext;
    type(): TypeContext;
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    string(): StringContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LikeClauseContext extends ParserRuleContext {
    _optionType: Token;
    KW_LIKE(): TerminalNode;
    tableName(): TableNameContext;
    KW_PROPERTIES(): TerminalNode | undefined;
    KW_INCLUDING(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PropertiesContext extends ParserRuleContext {
    propertyAssignments(): PropertyAssignmentsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PropertyAssignmentsContext extends ParserRuleContext {
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PropertyContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    EQ(): TerminalNode;
    propertyValue(): PropertyValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PropertyValueContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PropertyValueContext): void;
}
export declare class DefaultPropertyValueContext extends PropertyValueContext {
    KW_DEFAULT(): TerminalNode;
    constructor(ctx: PropertyValueContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NonDefaultPropertyValueContext extends PropertyValueContext {
    expression(): ExpressionContext;
    constructor(ctx: PropertyValueContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QueryNoWithContext extends ParserRuleContext {
    _offset: RowCountContext;
    _limit: LimitRowCountContext;
    _fetchFirst: RowCountContext;
    queryTerm(): QueryTermContext;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    KW_OFFSET(): TerminalNode | undefined;
    rowCount(): RowCountContext[];
    rowCount(i: number): RowCountContext;
    KW_LIMIT(): TerminalNode | undefined;
    KW_FETCH(): TerminalNode | undefined;
    limitRowCount(): LimitRowCountContext | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode[];
    KW_ROW(i: number): TerminalNode;
    KW_ROWS(): TerminalNode[];
    KW_ROWS(i: number): TerminalNode;
    KW_ONLY(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_TIES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LimitRowCountContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    rowCount(): RowCountContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RowCountContext extends ParserRuleContext {
    INTEGER_VALUE(): TerminalNode | undefined;
    QUESTION_MARK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QueryTermContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: QueryTermContext): void;
}
export declare class QueryTermDefaultContext extends QueryTermContext {
    queryPrimary(): QueryPrimaryContext;
    constructor(ctx: QueryTermContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QueryPrimaryContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: QueryPrimaryContext): void;
}
export declare class QueryPrimaryDefaultContext extends QueryPrimaryContext {
    querySpecification(): QuerySpecificationContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TableContext extends QueryPrimaryContext {
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class InlineTableContext extends QueryPrimaryContext {
    KW_VALUES(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SubqueryContext extends QueryPrimaryContext {
    queryNoWith(): QueryNoWithContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SortItemContext extends ParserRuleContext {
    _ordering: Token;
    _nullOrdering: Token;
    columnName(): ColumnNameContext | undefined;
    expression(): ExpressionContext | undefined;
    KW_NULLS(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QuerySpecificationContext extends ParserRuleContext {
    _where: BooleanExpressionContext;
    _having: BooleanExpressionContext;
    KW_SELECT(): TerminalNode;
    selectItem(): SelectItemContext[];
    selectItem(i: number): SelectItemContext;
    setQuantifier(): SetQuantifierContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    relation(): RelationContext[];
    relation(i: number): RelationContext;
    KW_WHERE(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    groupBy(): GroupByContext | undefined;
    KW_HAVING(): TerminalNode | undefined;
    KW_WINDOW(): TerminalNode | undefined;
    windowDefinition(): WindowDefinitionContext[];
    windowDefinition(i: number): WindowDefinitionContext;
    booleanExpression(): BooleanExpressionContext[];
    booleanExpression(i: number): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GroupByContext extends ParserRuleContext {
    groupingElement(): GroupingElementContext[];
    groupingElement(i: number): GroupingElementContext;
    setQuantifier(): SetQuantifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GroupingElementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: GroupingElementContext): void;
}
export declare class SingleGroupingSetContext extends GroupingElementContext {
    groupingSet(): GroupingSetContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RollupContext extends GroupingElementContext {
    KW_ROLLUP(): TerminalNode;
    groupingTerm(): GroupingTermContext[];
    groupingTerm(i: number): GroupingTermContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CubeContext extends GroupingElementContext {
    KW_CUBE(): TerminalNode;
    groupingTerm(): GroupingTermContext[];
    groupingTerm(i: number): GroupingTermContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MultipleGroupingSetsContext extends GroupingElementContext {
    KW_GROUPING(): TerminalNode;
    KW_SETS(): TerminalNode;
    groupingSet(): GroupingSetContext[];
    groupingSet(i: number): GroupingSetContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GroupingSetContext extends ParserRuleContext {
    groupingTerm(): GroupingTermContext[];
    groupingTerm(i: number): GroupingTermContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GroupingTermContext extends ParserRuleContext {
    columnName(): ColumnNameContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class WindowDefinitionContext extends ParserRuleContext {
    _name: IdentifierContext;
    KW_AS(): TerminalNode;
    windowSpecification(): WindowSpecificationContext;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class WindowSpecificationContext extends ParserRuleContext {
    _existingWindowName: IdentifierContext;
    _expression: ExpressionContext;
    _partition: ExpressionContext[];
    KW_PARTITION(): TerminalNode | undefined;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    KW_ORDER(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    windowFrame(): WindowFrameContext | undefined;
    identifier(): IdentifierContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NamedQueryContext extends ParserRuleContext {
    _name: IdentifierContext;
    KW_AS(): TerminalNode;
    query(): QueryContext;
    identifier(): IdentifierContext;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SetQuantifierContext extends ParserRuleContext {
    KW_DISTINCT(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SelectItemContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SelectItemContext): void;
}
export declare class SelectSingleContext extends SelectItemContext {
    columnName(): ColumnNameContext | undefined;
    expression(): ExpressionContext | undefined;
    identifier(): IdentifierContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: SelectItemContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SelectAllContext extends SelectItemContext {
    primaryExpression(): PrimaryExpressionContext | undefined;
    ASTERISK(): TerminalNode;
    KW_AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(ctx: SelectItemContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    KW_NATURAL(): TerminalNode | undefined;
    sampledRelation(): SampledRelationContext | undefined;
    constructor(ctx: RelationContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RelationDefaultContext extends RelationContext {
    sampledRelation(): SampledRelationContext;
    constructor(ctx: RelationContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class JoinTypeContext extends ParserRuleContext {
    KW_INNER(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class JoinCriteriaContext extends ParserRuleContext {
    KW_ON(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    KW_USING(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SampledRelationContext extends ParserRuleContext {
    _percentage: ExpressionContext;
    patternRecognition(): PatternRecognitionContext;
    KW_TABLESAMPLE(): TerminalNode | undefined;
    sampleType(): SampleTypeContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SampleTypeContext extends ParserRuleContext {
    KW_BERNOULLI(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PatternRecognitionContext extends ParserRuleContext {
    _expression: ExpressionContext;
    _partition: ExpressionContext[];
    aliasedRelation(): AliasedRelationContext;
    KW_MATCH_RECOGNIZE(): TerminalNode | undefined;
    KW_PATTERN(): TerminalNode | undefined;
    rowPattern(): RowPatternContext | undefined;
    KW_DEFINE(): TerminalNode | undefined;
    variableDefinition(): VariableDefinitionContext[];
    variableDefinition(i: number): VariableDefinitionContext;
    KW_PARTITION(): TerminalNode | undefined;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    KW_ORDER(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    KW_MEASURES(): TerminalNode | undefined;
    measureDefinition(): MeasureDefinitionContext[];
    measureDefinition(i: number): MeasureDefinitionContext;
    rowsPerMatch(): RowsPerMatchContext | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_MATCH(): TerminalNode | undefined;
    skipTo(): SkipToContext | undefined;
    KW_SUBSET(): TerminalNode | undefined;
    subsetDefinition(): SubsetDefinitionContext[];
    subsetDefinition(i: number): SubsetDefinitionContext;
    identifier(): IdentifierContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_INITIAL(): TerminalNode | undefined;
    KW_SEEK(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MeasureDefinitionContext extends ParserRuleContext {
    expression(): ExpressionContext;
    KW_AS(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RowsPerMatchContext extends ParserRuleContext {
    KW_ONE(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_PER(): TerminalNode;
    KW_MATCH(): TerminalNode;
    KW_ALL(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    emptyMatchHandling(): EmptyMatchHandlingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class EmptyMatchHandlingContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode | undefined;
    KW_EMPTY(): TerminalNode | undefined;
    KW_MATCHES(): TerminalNode | undefined;
    KW_OMIT(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_UNMATCHED(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SkipToContext extends ParserRuleContext {
    KW_TO(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_PAST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SubsetDefinitionContext extends ParserRuleContext {
    _name: IdentifierContext;
    _identifier: IdentifierContext;
    _union: IdentifierContext[];
    EQ(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class VariableDefinitionContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    KW_AS(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class AliasedRelationContext extends ParserRuleContext {
    relationPrimary(): RelationPrimaryContext;
    identifier(): IdentifierContext | undefined;
    KW_AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnListCreateContext extends ParserRuleContext {
    columnNameCreate(): ColumnNameCreateContext[];
    columnNameCreate(i: number): ColumnNameCreateContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnListContext extends ParserRuleContext {
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnAliasesContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RelationPrimaryContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RelationPrimaryContext): void;
}
export declare class TableOrViewRelationContext extends RelationPrimaryContext {
    tableOrViewName(): TableOrViewNameContext;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SubqueryRelationContext extends RelationPrimaryContext {
    query(): QueryContext;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UnnestContext extends RelationPrimaryContext {
    KW_UNNEST(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_WITH(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LateralContext extends RelationPrimaryContext {
    KW_LATERAL(): TerminalNode;
    query(): QueryContext;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ParenthesizedRelationContext extends RelationPrimaryContext {
    relation(): RelationContext;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExpressionContext extends ParserRuleContext {
    booleanExpression(): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LogicalNotContext extends BooleanExpressionContext {
    KW_NOT(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QuantifiedComparisonContext extends PredicateContext {
    comparisonOperator(): ComparisonOperatorContext;
    comparisonQuantifier(): ComparisonQuantifierContext;
    query(): QueryContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class InListContext extends PredicateContext {
    KW_IN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class InSubqueryContext extends PredicateContext {
    KW_IN(): TerminalNode;
    query(): QueryContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LikeContext extends PredicateContext {
    _pattern: ValueExpressionContext;
    _escape: ValueExpressionContext;
    KW_LIKE(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NullPredicateContext extends PredicateContext {
    KW_IS(): TerminalNode;
    KW_NULL(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DistinctFromContext extends PredicateContext {
    _right: ValueExpressionContext;
    KW_IS(): TerminalNode;
    KW_DISTINCT(): TerminalNode;
    KW_FROM(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ValueExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ValueExpressionContext): void;
}
export declare class ValueExpressionDefaultContext extends ValueExpressionContext {
    primaryExpression(): PrimaryExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class AtTimeZoneContext extends ValueExpressionContext {
    valueExpression(): ValueExpressionContext;
    KW_AT(): TerminalNode;
    timeZoneSpecifier(): TimeZoneSpecifierContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ArithmeticUnaryContext extends ValueExpressionContext {
    _operator: Token;
    valueExpression(): ValueExpressionContext;
    MINUS(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ConcatenationContext extends ValueExpressionContext {
    _left: ValueExpressionContext;
    _right: ValueExpressionContext;
    CONCAT(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PrimaryExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrimaryExpressionContext): void;
}
export declare class NullLiteralContext extends PrimaryExpressionContext {
    KW_NULL(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class IntervalLiteralContext extends PrimaryExpressionContext {
    interval(): IntervalContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TypeConstructorContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext | undefined;
    string(): StringContext;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_PRECISION(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NumericLiteralContext extends PrimaryExpressionContext {
    number(): NumberContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class BooleanLiteralContext extends PrimaryExpressionContext {
    booleanValue(): BooleanValueContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StringLiteralContext extends PrimaryExpressionContext {
    string(): StringContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class BinaryLiteralContext extends PrimaryExpressionContext {
    BINARY_LITERAL(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ParameterContext extends PrimaryExpressionContext {
    QUESTION_MARK(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PositionContext extends PrimaryExpressionContext {
    KW_POSITION(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_IN(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RowConstructorContext extends PrimaryExpressionContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_ROW(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class FunctionCallContext extends PrimaryExpressionContext {
    functionName(): FunctionNameContext;
    ASTERISK(): TerminalNode | undefined;
    filter(): FilterContext | undefined;
    over(): OverContext | undefined;
    processingMode(): ProcessingModeContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    setQuantifier(): SetQuantifierContext | undefined;
    nullTreatment(): NullTreatmentContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MeasureContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext;
    over(): OverContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LambdaContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    expression(): ExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SubqueryExpressionContext extends PrimaryExpressionContext {
    query(): QueryContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExistsContext extends PrimaryExpressionContext {
    KW_EXISTS(): TerminalNode;
    query(): QueryContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SimpleCaseContext extends PrimaryExpressionContext {
    _operand: ExpressionContext;
    _elseExpression: ExpressionContext;
    KW_CASE(): TerminalNode;
    KW_END(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    whenClause(): WhenClauseContext[];
    whenClause(i: number): WhenClauseContext;
    KW_ELSE(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CastContext extends PrimaryExpressionContext {
    KW_CAST(): TerminalNode | undefined;
    expression(): ExpressionContext;
    KW_AS(): TerminalNode;
    type(): TypeContext;
    KW_TRY_CAST(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ArrayConstructorContext extends PrimaryExpressionContext {
    KW_ARRAY(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SubscriptContext extends PrimaryExpressionContext {
    _value: PrimaryExpressionContext;
    _index: ValueExpressionContext;
    primaryExpression(): PrimaryExpressionContext;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnReferenceContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DereferenceContext extends PrimaryExpressionContext {
    _base: PrimaryExpressionContext;
    _fieldName: IdentifierContext;
    primaryExpression(): PrimaryExpressionContext;
    identifier(): IdentifierContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SpecialDateTimeFunctionContext extends PrimaryExpressionContext {
    _name: Token;
    _precision: Token;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentUserContext extends PrimaryExpressionContext {
    _name: Token;
    KW_CURRENT_USER(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentCatalogContext extends PrimaryExpressionContext {
    _name: Token;
    KW_CURRENT_CATALOG(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentSchemaContext extends PrimaryExpressionContext {
    _name: Token;
    KW_CURRENT_SCHEMA(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentPathContext extends PrimaryExpressionContext {
    _name: Token;
    KW_CURRENT_PATH(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SubstringContext extends PrimaryExpressionContext {
    KW_SUBSTRING(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_FROM(): TerminalNode;
    KW_FOR(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NormalizeContext extends PrimaryExpressionContext {
    KW_NORMALIZE(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    normalForm(): NormalFormContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExtractContext extends PrimaryExpressionContext {
    KW_EXTRACT(): TerminalNode;
    identifier(): IdentifierContext;
    KW_FROM(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ParenthesizedExpressionContext extends PrimaryExpressionContext {
    expression(): ExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GroupingOperationContext extends PrimaryExpressionContext {
    KW_GROUPING(): TerminalNode;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ProcessingModeContext extends ParserRuleContext {
    KW_RUNNING(): TerminalNode | undefined;
    KW_FINAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NullTreatmentContext extends ParserRuleContext {
    KW_IGNORE(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode;
    KW_RESPECT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class StringContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: StringContext): void;
}
export declare class BasicStringLiteralContext extends StringContext {
    STRING(): TerminalNode;
    constructor(ctx: StringContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UnicodeStringLiteralContext extends StringContext {
    UNICODE_STRING(): TerminalNode;
    KW_UESCAPE(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    constructor(ctx: StringContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TimeZoneSpecifierContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TimeZoneSpecifierContext): void;
}
export declare class TimeZoneIntervalContext extends TimeZoneSpecifierContext {
    KW_TIME(): TerminalNode;
    KW_ZONE(): TerminalNode;
    interval(): IntervalContext;
    constructor(ctx: TimeZoneSpecifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TimeZoneStringContext extends TimeZoneSpecifierContext {
    KW_TIME(): TerminalNode;
    KW_ZONE(): TerminalNode;
    string(): StringContext;
    constructor(ctx: TimeZoneSpecifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ComparisonQuantifierContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class BooleanValueContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class IntervalContext extends ParserRuleContext {
    _sign: Token;
    _from: IntervalFieldContext;
    _to: IntervalFieldContext;
    KW_INTERVAL(): TerminalNode;
    string(): StringContext;
    intervalField(): IntervalFieldContext[];
    intervalField(i: number): IntervalFieldContext;
    KW_TO(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class IntervalFieldContext extends ParserRuleContext {
    KW_YEAR(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NormalFormContext extends ParserRuleContext {
    KW_NFD(): TerminalNode | undefined;
    KW_NFC(): TerminalNode | undefined;
    KW_NFKD(): TerminalNode | undefined;
    KW_NFKC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TypeContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TypeContext): void;
}
export declare class RowTypeContext extends TypeContext {
    KW_ROW(): TerminalNode;
    rowField(): RowFieldContext[];
    rowField(i: number): RowFieldContext;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class IntervalTypeContext extends TypeContext {
    _from: IntervalFieldContext;
    _to: IntervalFieldContext;
    KW_INTERVAL(): TerminalNode;
    intervalField(): IntervalFieldContext[];
    intervalField(i: number): IntervalFieldContext;
    KW_TO(): TerminalNode | undefined;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DateTimeTypeContext extends TypeContext {
    _base: Token;
    _precision: TypeParameterContext;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_TIME(): TerminalNode[];
    KW_TIME(i: number): TerminalNode;
    KW_ZONE(): TerminalNode | undefined;
    typeParameter(): TypeParameterContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DoublePrecisionTypeContext extends TypeContext {
    KW_DOUBLE(): TerminalNode;
    KW_PRECISION(): TerminalNode;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LegacyArrayTypeContext extends TypeContext {
    KW_ARRAY(): TerminalNode;
    LT(): TerminalNode;
    type(): TypeContext;
    GT(): TerminalNode;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LegacyMapTypeContext extends TypeContext {
    _keyType: TypeContext;
    _valueType: TypeContext;
    KW_MAP(): TerminalNode;
    LT(): TerminalNode;
    GT(): TerminalNode;
    type(): TypeContext[];
    type(i: number): TypeContext;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ArrayTypeContext extends TypeContext {
    type(): TypeContext;
    KW_ARRAY(): TerminalNode;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GenericTypeContext extends TypeContext {
    identifier(): IdentifierContext;
    typeParameter(): TypeParameterContext[];
    typeParameter(i: number): TypeParameterContext;
    constructor(ctx: TypeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RowFieldContext extends ParserRuleContext {
    type(): TypeContext;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TypeParameterContext extends ParserRuleContext {
    INTEGER_VALUE(): TerminalNode | undefined;
    type(): TypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class FilterContext extends ParserRuleContext {
    KW_FILTER(): TerminalNode;
    KW_WHERE(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MergeCaseContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: MergeCaseContext): void;
}
export declare class MergeUpdateContext extends MergeCaseContext {
    _condition: ExpressionContext;
    _identifier: IdentifierContext;
    _targets: IdentifierContext[];
    _expression: ExpressionContext;
    _values: ExpressionContext[];
    KW_WHEN(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_THEN(): TerminalNode;
    KW_UPDATE(): TerminalNode;
    KW_SET(): TerminalNode;
    EQ(): TerminalNode[];
    EQ(i: number): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_AND(): TerminalNode | undefined;
    constructor(ctx: MergeCaseContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MergeDeleteContext extends MergeCaseContext {
    _condition: ExpressionContext;
    KW_WHEN(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_THEN(): TerminalNode;
    KW_DELETE(): TerminalNode;
    KW_AND(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: MergeCaseContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class MergeInsertContext extends MergeCaseContext {
    _condition: ExpressionContext;
    _identifier: IdentifierContext;
    _targets: IdentifierContext[];
    _expression: ExpressionContext;
    _values: ExpressionContext[];
    KW_WHEN(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_THEN(): TerminalNode;
    KW_INSERT(): TerminalNode;
    KW_VALUES(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_AND(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(ctx: MergeCaseContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class OverContext extends ParserRuleContext {
    _windowName: IdentifierContext;
    KW_OVER(): TerminalNode;
    windowSpecification(): WindowSpecificationContext | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class WindowFrameContext extends ParserRuleContext {
    frameExtent(): FrameExtentContext;
    KW_MEASURES(): TerminalNode | undefined;
    measureDefinition(): MeasureDefinitionContext[];
    measureDefinition(i: number): MeasureDefinitionContext;
    KW_AFTER(): TerminalNode | undefined;
    KW_MATCH(): TerminalNode | undefined;
    skipTo(): SkipToContext | undefined;
    KW_PATTERN(): TerminalNode | undefined;
    rowPattern(): RowPatternContext | undefined;
    KW_SUBSET(): TerminalNode | undefined;
    subsetDefinition(): SubsetDefinitionContext[];
    subsetDefinition(i: number): SubsetDefinitionContext;
    KW_DEFINE(): TerminalNode | undefined;
    variableDefinition(): VariableDefinitionContext[];
    variableDefinition(i: number): VariableDefinitionContext;
    KW_INITIAL(): TerminalNode | undefined;
    KW_SEEK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class FrameExtentContext extends ParserRuleContext {
    _frameType: Token;
    _frameStart: FrameBoundContext;
    _end: FrameBoundContext;
    KW_RANGE(): TerminalNode | undefined;
    frameBound(): FrameBoundContext[];
    frameBound(i: number): FrameBoundContext;
    KW_ROWS(): TerminalNode | undefined;
    KW_GROUPS(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentRowBoundContext extends FrameBoundContext {
    KW_CURRENT(): TerminalNode;
    KW_ROW(): TerminalNode;
    constructor(ctx: FrameBoundContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class BoundedFrameContext extends FrameBoundContext {
    _boundType: Token;
    expression(): ExpressionContext;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    constructor(ctx: FrameBoundContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RowPatternContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RowPatternContext): void;
}
export declare class QuantifiedPrimaryContext extends RowPatternContext {
    patternPrimary(): PatternPrimaryContext;
    patternQuantifier(): PatternQuantifierContext | undefined;
    constructor(ctx: RowPatternContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PatternConcatenationContext extends RowPatternContext {
    rowPattern(): RowPatternContext[];
    rowPattern(i: number): RowPatternContext;
    constructor(ctx: RowPatternContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PatternAlternationContext extends RowPatternContext {
    rowPattern(): RowPatternContext[];
    rowPattern(i: number): RowPatternContext;
    constructor(ctx: RowPatternContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PatternPrimaryContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PatternPrimaryContext): void;
}
export declare class PatternVariableContext extends PatternPrimaryContext {
    identifier(): IdentifierContext;
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class EmptyPatternContext extends PatternPrimaryContext {
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PatternPermutationContext extends PatternPrimaryContext {
    KW_PERMUTE(): TerminalNode;
    rowPattern(): RowPatternContext[];
    rowPattern(i: number): RowPatternContext;
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GroupedPatternContext extends PatternPrimaryContext {
    rowPattern(): RowPatternContext;
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PartitionStartAnchorContext extends PatternPrimaryContext {
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PartitionEndAnchorContext extends PatternPrimaryContext {
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExcludedPatternContext extends PatternPrimaryContext {
    rowPattern(): RowPatternContext;
    constructor(ctx: PatternPrimaryContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PatternQuantifierContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PatternQuantifierContext): void;
}
export declare class ZeroOrMoreQuantifierContext extends PatternQuantifierContext {
    _reluctant: Token;
    ASTERISK(): TerminalNode;
    QUESTION_MARK(): TerminalNode | undefined;
    constructor(ctx: PatternQuantifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class OneOrMoreQuantifierContext extends PatternQuantifierContext {
    _reluctant: Token;
    PLUS(): TerminalNode;
    QUESTION_MARK(): TerminalNode | undefined;
    constructor(ctx: PatternQuantifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ZeroOrOneQuantifierContext extends PatternQuantifierContext {
    _reluctant: Token;
    QUESTION_MARK(): TerminalNode[];
    QUESTION_MARK(i: number): TerminalNode;
    constructor(ctx: PatternQuantifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RangeQuantifierContext extends PatternQuantifierContext {
    _exactly: Token;
    _reluctant: Token;
    _atLeast: Token;
    _atMost: Token;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    QUESTION_MARK(): TerminalNode | undefined;
    constructor(ctx: PatternQuantifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UpdateAssignmentContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    EQ(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExplainOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ExplainOptionContext): void;
}
export declare class ExplainFormatContext extends ExplainOptionContext {
    _value: Token;
    KW_FORMAT(): TerminalNode;
    KW_TEXT(): TerminalNode | undefined;
    KW_GRAPHVIZ(): TerminalNode | undefined;
    KW_JSON(): TerminalNode | undefined;
    constructor(ctx: ExplainOptionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ExplainTypeContext extends ExplainOptionContext {
    _value: Token;
    KW_TYPE(): TerminalNode;
    KW_LOGICAL(): TerminalNode | undefined;
    KW_DISTRIBUTED(): TerminalNode | undefined;
    KW_VALIDATE(): TerminalNode | undefined;
    KW_IO(): TerminalNode | undefined;
    constructor(ctx: ExplainOptionContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TransactionModeContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TransactionModeContext): void;
}
export declare class IsolationLevelContext extends TransactionModeContext {
    KW_ISOLATION(): TerminalNode;
    KW_LEVEL(): TerminalNode;
    levelOfIsolation(): LevelOfIsolationContext;
    constructor(ctx: TransactionModeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TransactionAccessModeContext extends TransactionModeContext {
    _accessMode: Token;
    KW_READ(): TerminalNode;
    KW_ONLY(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    constructor(ctx: TransactionModeContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class LevelOfIsolationContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: LevelOfIsolationContext): void;
}
export declare class ReadUncommittedContext extends LevelOfIsolationContext {
    KW_READ(): TerminalNode;
    KW_UNCOMMITTED(): TerminalNode;
    constructor(ctx: LevelOfIsolationContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ReadCommittedContext extends LevelOfIsolationContext {
    KW_READ(): TerminalNode;
    KW_COMMITTED(): TerminalNode;
    constructor(ctx: LevelOfIsolationContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RepeatableReadContext extends LevelOfIsolationContext {
    KW_REPEATABLE(): TerminalNode;
    KW_READ(): TerminalNode;
    constructor(ctx: LevelOfIsolationContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SerializableContext extends LevelOfIsolationContext {
    KW_SERIALIZABLE(): TerminalNode;
    constructor(ctx: LevelOfIsolationContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CallArgumentContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CallArgumentContext): void;
}
export declare class PositionalArgumentContext extends CallArgumentContext {
    expression(): ExpressionContext;
    constructor(ctx: CallArgumentContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NamedArgumentContext extends CallArgumentContext {
    identifier(): IdentifierContext;
    expression(): ExpressionContext;
    constructor(ctx: CallArgumentContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PathElementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PathElementContext): void;
}
export declare class QualifiedArgumentContext extends PathElementContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(ctx: PathElementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UnqualifiedArgumentContext extends PathElementContext {
    identifier(): IdentifierContext;
    constructor(ctx: PathElementContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PathSpecificationContext extends ParserRuleContext {
    pathElement(): PathElementContext[];
    pathElement(i: number): PathElementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PrivilegeContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TableOrViewNameContext extends ParserRuleContext {
    tableName(): TableNameContext | undefined;
    viewName(): ViewNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TableNameContext extends ParserRuleContext {
    tablePath(): TablePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TableNameCreateContext extends ParserRuleContext {
    tablePath(): TablePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ViewNameContext extends ParserRuleContext {
    viewPath(): ViewPathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ViewNameCreateContext extends ParserRuleContext {
    viewPath(): ViewPathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class TablePathContext extends ParserRuleContext {
    _table: IdentifierContext;
    _schema: IdentifierContext;
    _catalog: IdentifierContext;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ViewPathContext extends ParserRuleContext {
    _view: IdentifierContext;
    _schema: IdentifierContext;
    _catalog: IdentifierContext;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SchemaNameContext extends ParserRuleContext {
    schemaPath(): SchemaPathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SchemaNameCreateContext extends ParserRuleContext {
    schemaPath(): SchemaPathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class SchemaPathContext extends ParserRuleContext {
    _schema: IdentifierContext;
    _catalog: IdentifierContext;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CatalogNameContext extends ParserRuleContext {
    _catalog: IdentifierContext;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CatalogNameCreateContext extends ParserRuleContext {
    _catalog: IdentifierContext;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class FunctionNameContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnNameContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class ColumnNameCreateContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QualifiedNameContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class GrantorContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: GrantorContext): void;
}
export declare class SpecifiedPrincipalContext extends GrantorContext {
    principal(): PrincipalContext;
    constructor(ctx: GrantorContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentUserGrantorContext extends GrantorContext {
    KW_CURRENT_USER(): TerminalNode;
    constructor(ctx: GrantorContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class CurrentRoleGrantorContext extends GrantorContext {
    KW_CURRENT_ROLE(): TerminalNode;
    constructor(ctx: GrantorContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class PrincipalContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrincipalContext): void;
}
export declare class UnspecifiedPrincipalContext extends PrincipalContext {
    identifier(): IdentifierContext;
    constructor(ctx: PrincipalContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class UserPrincipalContext extends PrincipalContext {
    KW_USER(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: PrincipalContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RolePrincipalContext extends PrincipalContext {
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: PrincipalContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class RolesContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class QuotedIdentifierContext extends IdentifierContext {
    QUOTED_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class BackQuotedIdentifierContext extends IdentifierContext {
    BACKQUOTED_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DigitIdentifierContext extends IdentifierContext {
    DIGIT_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
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
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class DoubleLiteralContext extends NumberContext {
    DOUBLE_VALUE(): TerminalNode;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: NumberContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class IntegerLiteralContext extends NumberContext {
    INTEGER_VALUE(): TerminalNode;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: NumberContext);
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
export declare class NonReservedContext extends ParserRuleContext {
    KW_ADD(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_ANALYZE(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_AT(): TerminalNode | undefined;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    KW_BERNOULLI(): TerminalNode | undefined;
    KW_CALL(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_CATALOGS(): TerminalNode | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_COMMIT(): TerminalNode | undefined;
    KW_COMMITTED(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_DATE(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_DEFINE(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_DISTRIBUTED(): TerminalNode | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_EMPTY(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    KW_EXPLAIN(): TerminalNode | undefined;
    KW_FETCH(): TerminalNode | undefined;
    KW_FILTER(): TerminalNode | undefined;
    KW_FINAL(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_GRANT(): TerminalNode | undefined;
    KW_GRANTED(): TerminalNode | undefined;
    KW_GRANTS(): TerminalNode | undefined;
    KW_DENY(): TerminalNode | undefined;
    KW_GRAPHVIZ(): TerminalNode | undefined;
    KW_GROUPS(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_INCLUDING(): TerminalNode | undefined;
    KW_INITIAL(): TerminalNode | undefined;
    KW_INPUT(): TerminalNode | undefined;
    KW_INTERVAL(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_IO(): TerminalNode | undefined;
    KW_ISOLATION(): TerminalNode | undefined;
    KW_JSON(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_LOGICAL(): TerminalNode | undefined;
    KW_MAP(): TerminalNode | undefined;
    KW_MATCH(): TerminalNode | undefined;
    KW_MATCHED(): TerminalNode | undefined;
    KW_MATCHES(): TerminalNode | undefined;
    KW_MATCH_RECOGNIZE(): TerminalNode | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_MEASURES(): TerminalNode | undefined;
    KW_MERGE(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_NFC(): TerminalNode | undefined;
    KW_NFD(): TerminalNode | undefined;
    KW_NFKC(): TerminalNode | undefined;
    KW_NFKD(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    KW_NULLIF(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode | undefined;
    KW_OFFSET(): TerminalNode | undefined;
    KW_OMIT(): TerminalNode | undefined;
    KW_ONE(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    KW_OUTPUT(): TerminalNode | undefined;
    KW_OVER(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_PAST(): TerminalNode | undefined;
    KW_PATH(): TerminalNode | undefined;
    KW_PATTERN(): TerminalNode | undefined;
    KW_PER(): TerminalNode | undefined;
    KW_PERMUTE(): TerminalNode | undefined;
    KW_POSITION(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_PRECISION(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_PROPERTIES(): TerminalNode | undefined;
    KW_RANGE(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_REFRESH(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_REPEATABLE(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_RESPECT(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_REVOKE(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_ROLES(): TerminalNode | undefined;
    KW_ROLLBACK(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_RUNNING(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_SCHEMAS(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_SEEK(): TerminalNode | undefined;
    KW_SERIALIZABLE(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_SETS(): TerminalNode | undefined;
    KW_SHOW(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_START(): TerminalNode | undefined;
    KW_STATS(): TerminalNode | undefined;
    KW_SUBSET(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_TABLESAMPLE(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_TIES(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_TRY_CAST(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_UNBOUNDED(): TerminalNode | undefined;
    KW_UNCOMMITTED(): TerminalNode | undefined;
    KW_UNMATCHED(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_USE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_VALIDATE(): TerminalNode | undefined;
    KW_VERBOSE(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_WINDOW(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_WORK(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: TrinoSqlListener): void;
    exitRule(listener: TrinoSqlListener): void;
    accept<Result>(visitor: TrinoSqlVisitor<Result>): Result;
}
