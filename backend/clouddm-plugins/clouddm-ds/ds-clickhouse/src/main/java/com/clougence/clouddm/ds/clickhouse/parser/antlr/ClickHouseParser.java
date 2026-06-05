// Generated from ClickHouseParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.clickhouse.parser.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ClickHouseParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ACCESS=1, ADD=2, AFTER=3, ALIAS=4, ALL=5, ALTER=6, AND=7, ANTI=8, ANY=9, 
		ARRAY=10, AS=11, ASCENDING=12, ASOF=13, AST=14, ASYNC=15, ATTACH=16, BETWEEN=17, 
		BOTH=18, BY=19, CACHES=20, CASE=21, CAST=22, CHECK=23, CLEAR=24, CLUSTER=25, 
		CLUSTERS=26, CODEC=27, COLLATE=28, COLUMN=29, COLUMNS=30, COMMENT=31, 
		CONSTRAINT=32, CREATE=33, CROSS=34, CUBE=35, CURRENT=36, CURRENT_USER=37, 
		CHANGED=38, DATABASE=39, DATABASES=40, DATE=41, DAY=42, DEDUPLICATE=43, 
		DEFAULT=44, DELAY=45, DELETE=46, DESC=47, DESCENDING=48, DESCRIBE=49, 
		DETACH=50, DICTIONARIES=51, DICTIONARY=52, DISK=53, DISTINCT=54, DISTRIBUTED=55, 
		DROP=56, ELSE=57, ENABLED=58, END=59, ENGINE=60, ENGINES=61, ESTIMATE=62, 
		EVENTS=63, EXCEPT=64, INTERSECT=65, EXISTS=66, EXPLAIN=67, EXPRESSION=68, 
		EXTENDED=69, EXTRACT=70, FETCHES=71, FIELDS=72, FILESYSTEM=73, FILL=74, 
		FINAL=75, FIRST=76, FLUSH=77, FOLLOWING=78, FOR=79, FORMAT=80, FREEZE=81, 
		FROM=82, FULL=83, FUNCTION=84, FUNCTIONS=85, GLOBAL=86, GRANULARITY=87, 
		GRANTS=88, GROUP=89, GROUPING=90, HAVING=91, HIERARCHICAL=92, HOUR=93, 
		ID=94, IF=95, ILIKE=96, IMPLICIT=97, IN=98, INDEX=99, INDEXES=100, INDICES=101, 
		INF=102, INJECTIVE=103, INNER=104, INSERT=105, INTERPOLATE=106, INTERVAL=107, 
		INTO=108, IS=109, IS_OBJECT_ID=110, JOIN=111, KEY=112, KEYS=113, KILL=114, 
		LAST=115, LAYOUT=116, LEADING=117, LEFT=118, LIFETIME=119, LIKE=120, LIMIT=121, 
		LIVE=122, LOCAL=123, LOGS=124, MATERIALIZE=125, MATERIALIZED=126, MAX=127, 
		MERGES=128, MICROSECOND=129, MILLISECOND=130, MIN=131, MINUTE=132, MODIFY=133, 
		MONTH=134, MOVE=135, MUTATION=136, NAN_SQL=137, NANOSECOND=138, NO=139, 
		NOT=140, NULL_SQL=141, NULLABLE=142, NULLS=143, OFFSET=144, ON=145, OPTIMIZE=146, 
		OR=147, ORDER=148, OUTER=149, OUTFILE=150, OVER=151, OVERRIDE=152, PARTITION=153, 
		PIPELINE=154, PLAN=155, POLICY=156, POLICIES=157, POPULATE=158, PRECEDING=159, 
		PREWHERE=160, QUALIFY=161, PRIMARY=162, PRIVILEGES=163, PROCESSLIST=164, 
		PROFILE=165, PROFILES=166, PROJECTION=167, QUARTER=168, QUOTA=169, QUOTAS=170, 
		RANGE=171, RECURSIVE=172, RELOAD=173, REMOVE=174, RENAME=175, REPLACE=176, 
		REPLICA=177, REPLICATED=178, RIGHT=179, ROLE=180, ROLES=181, ROLLUP=182, 
		ROW=183, ROWS=184, SAMPLE=185, SECOND=186, SELECT=187, SEMI=188, SENDS=189, 
		SET=190, SETS=191, SETTING=192, SETTINGS=193, SHOW=194, SOURCE=195, START=196, 
		STOP=197, SUBSTRING=198, SYNC=199, SYNTAX=200, SYSTEM=201, STEP=202, TABLE=203, 
		TABLES=204, TEMPORARY=205, TEST=206, THEN=207, TIES=208, TIMEOUT=209, 
		TIMESTAMP=210, TO=211, TOP=212, TOTALS=213, TRAILING=214, TREE=215, TRIM=216, 
		TRUNCATE=217, TTL=218, TYPE=219, UNBOUNDED=220, UNION=221, UPDATE=222, 
		USE=223, USER=224, USERS=225, USING=226, UUID=227, VALUES=228, VIEW=229, 
		VOLUME=230, WATCH=231, WEEK=232, WHEN=233, WHERE=234, WINDOW=235, WITH=236, 
		YEAR=237, JSON_FALSE=238, JSON_TRUE=239, HEXADECIMAL_STRING_LITERAL=240, 
		BINARY_STRING_LITERAL=241, IDENTIFIER=242, FLOATING_LITERAL=243, OCTAL_LITERAL=244, 
		DECIMAL_LITERAL=245, HEXADECIMAL_NUMERIC_LITERAL=246, BINARY_NUMERIC_LITERAL=247, 
		STRING_LITERAL=248, ARROW=249, ASTERISK=250, BACKQUOTE=251, BACKSLASH=252, 
		DOUBLE_COLON=253, COLON=254, COMMA=255, CONCAT=256, DASH=257, DOT=258, 
		EQ_DOUBLE=259, EQ_SINGLE=260, GE=261, GT=262, LBRACE=263, LBRACKET=264, 
		LE=265, LPAREN=266, LT=267, NOT_EQ=268, PERCENT=269, PLUS=270, QUERY=271, 
		QUOTE_DOUBLE=272, QUOTE_SINGLE=273, RBRACE=274, RBRACKET=275, RPAREN=276, 
		SEMICOLON=277, SLASH=278, UNDERSCORE=279, MULTI_LINE_COMMENT=280, SINGLE_LINE_COMMENT=281, 
		WHITESPACE=282;
	public static final int
		RULE_root = 0, RULE_queryStmt = 1, RULE_query = 2, RULE_alterStmt = 3, 
		RULE_alterTableClause = 4, RULE_assignmentExprList = 5, RULE_assignmentExpr = 6, 
		RULE_tableColumnPropertyType = 7, RULE_partitionClause = 8, RULE_attachStmt = 9, 
		RULE_checkStmt = 10, RULE_createStmt = 11, RULE_dictionarySchemaClause = 12, 
		RULE_dictionaryAttrDfnt = 13, RULE_dictionaryEngineClause = 14, RULE_dictionaryPrimaryKeyClause = 15, 
		RULE_dictionaryArgExpr = 16, RULE_sourceClause = 17, RULE_lifetimeClause = 18, 
		RULE_layoutClause = 19, RULE_rangeClause = 20, RULE_dictionarySettingsClause = 21, 
		RULE_clusterClause = 22, RULE_uuidClause = 23, RULE_destinationClause = 24, 
		RULE_subqueryClause = 25, RULE_tableSchemaClause = 26, RULE_engineClause = 27, 
		RULE_partitionByClause = 28, RULE_primaryKeyClause = 29, RULE_sampleByClause = 30, 
		RULE_ttlClause = 31, RULE_commentClause = 32, RULE_engineExpr = 33, RULE_tableElementExpr = 34, 
		RULE_tableColumnDfnt = 35, RULE_tableColumnPropertyExpr = 36, RULE_tableIndexDfnt = 37, 
		RULE_tableProjectionDfnt = 38, RULE_codecExpr = 39, RULE_codecArgExpr = 40, 
		RULE_ttlExpr = 41, RULE_ttlSetExpr = 42, RULE_describeStmt = 43, RULE_dropStmt = 44, 
		RULE_existsStmt = 45, RULE_explainStmt = 46, RULE_insertStmt = 47, RULE_columnsClause = 48, 
		RULE_dataClause = 49, RULE_assignmentValues = 50, RULE_assignmentValue = 51, 
		RULE_deleteStmt = 52, RULE_inPartitionClause = 53, RULE_updateStmt = 54, 
		RULE_killStmt = 55, RULE_optimizeStmt = 56, RULE_renameStmt = 57, RULE_renameEntityClause = 58, 
		RULE_projectionSelectStmt = 59, RULE_selectUnionStmt = 60, RULE_selectStmtWithParens = 61, 
		RULE_selectStmt = 62, RULE_withClause = 63, RULE_withExprList = 64, RULE_withExpr = 65, 
		RULE_topClause = 66, RULE_fromClause = 67, RULE_arrayJoinClause = 68, 
		RULE_windowClause = 69, RULE_prewhereClause = 70, RULE_qualifyClause = 71, 
		RULE_whereClause = 72, RULE_groupByClause = 73, RULE_havingClause = 74, 
		RULE_orderByClause = 75, RULE_interpolateClause = 76, RULE_projectionOrderByClause = 77, 
		RULE_limitByClause = 78, RULE_limitClause = 79, RULE_settingsClause = 80, 
		RULE_joinExpr = 81, RULE_joinOp = 82, RULE_joinOpCross = 83, RULE_joinConstraintClause = 84, 
		RULE_sampleClause = 85, RULE_limitExpr = 86, RULE_orderExprList = 87, 
		RULE_orderExpr = 88, RULE_ratioExpr = 89, RULE_settingExprList = 90, RULE_settingExpr = 91, 
		RULE_windowExpr = 92, RULE_winPartitionByClause = 93, RULE_winOrderByClause = 94, 
		RULE_winFrameClause = 95, RULE_winFrameExtend = 96, RULE_winFrameBound = 97, 
		RULE_setStmt = 98, RULE_showStmt = 99, RULE_systemStmt = 100, RULE_truncateStmt = 101, 
		RULE_useStmt = 102, RULE_watchStmt = 103, RULE_columnTypeExpr = 104, RULE_columnExprList = 105, 
		RULE_columnsExpr = 106, RULE_columnExpr = 107, RULE_columnArgList = 108, 
		RULE_columnArgExpr = 109, RULE_columnLambdaExpr = 110, RULE_columnIdentifier = 111, 
		RULE_nestedIdentifier = 112, RULE_columnExceptExpr = 113, RULE_tableExpr = 114, 
		RULE_tableFunctionExpr = 115, RULE_tableIdentifier = 116, RULE_tableArgList = 117, 
		RULE_tableArgExpr = 118, RULE_databaseIdentifier = 119, RULE_dictionaryIdentifier = 120, 
		RULE_floatingLiteral = 121, RULE_numberLiteral = 122, RULE_stringLiteral = 123, 
		RULE_literal = 124, RULE_interval = 125, RULE_keyword = 126, RULE_keywordForAlias = 127, 
		RULE_alias = 128, RULE_identifier = 129, RULE_identifierOrNull = 130, 
		RULE_enumValue = 131;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "queryStmt", "query", "alterStmt", "alterTableClause", "assignmentExprList", 
			"assignmentExpr", "tableColumnPropertyType", "partitionClause", "attachStmt", 
			"checkStmt", "createStmt", "dictionarySchemaClause", "dictionaryAttrDfnt", 
			"dictionaryEngineClause", "dictionaryPrimaryKeyClause", "dictionaryArgExpr", 
			"sourceClause", "lifetimeClause", "layoutClause", "rangeClause", "dictionarySettingsClause", 
			"clusterClause", "uuidClause", "destinationClause", "subqueryClause", 
			"tableSchemaClause", "engineClause", "partitionByClause", "primaryKeyClause", 
			"sampleByClause", "ttlClause", "commentClause", "engineExpr", "tableElementExpr", 
			"tableColumnDfnt", "tableColumnPropertyExpr", "tableIndexDfnt", "tableProjectionDfnt", 
			"codecExpr", "codecArgExpr", "ttlExpr", "ttlSetExpr", "describeStmt", 
			"dropStmt", "existsStmt", "explainStmt", "insertStmt", "columnsClause", 
			"dataClause", "assignmentValues", "assignmentValue", "deleteStmt", "inPartitionClause", 
			"updateStmt", "killStmt", "optimizeStmt", "renameStmt", "renameEntityClause", 
			"projectionSelectStmt", "selectUnionStmt", "selectStmtWithParens", "selectStmt", 
			"withClause", "withExprList", "withExpr", "topClause", "fromClause", 
			"arrayJoinClause", "windowClause", "prewhereClause", "qualifyClause", 
			"whereClause", "groupByClause", "havingClause", "orderByClause", "interpolateClause", 
			"projectionOrderByClause", "limitByClause", "limitClause", "settingsClause", 
			"joinExpr", "joinOp", "joinOpCross", "joinConstraintClause", "sampleClause", 
			"limitExpr", "orderExprList", "orderExpr", "ratioExpr", "settingExprList", 
			"settingExpr", "windowExpr", "winPartitionByClause", "winOrderByClause", 
			"winFrameClause", "winFrameExtend", "winFrameBound", "setStmt", "showStmt", 
			"systemStmt", "truncateStmt", "useStmt", "watchStmt", "columnTypeExpr", 
			"columnExprList", "columnsExpr", "columnExpr", "columnArgList", "columnArgExpr", 
			"columnLambdaExpr", "columnIdentifier", "nestedIdentifier", "columnExceptExpr", 
			"tableExpr", "tableFunctionExpr", "tableIdentifier", "tableArgList", 
			"tableArgExpr", "databaseIdentifier", "dictionaryIdentifier", "floatingLiteral", 
			"numberLiteral", "stringLiteral", "literal", "interval", "keyword", "keywordForAlias", 
			"alias", "identifier", "identifierOrNull", "enumValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "'->'", "'*'", 
			"'`'", "'\\'", "'::'", "':'", "','", "'||'", "'-'", "'.'", "'=='", "'='", 
			"'>='", "'>'", "'{'", "'['", "'<='", "'('", "'<'", null, "'%'", "'+'", 
			"'?'", "'\"'", "'''", "'}'", "']'", "')'", "';'", "'/'", "'_'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ACCESS", "ADD", "AFTER", "ALIAS", "ALL", "ALTER", "AND", "ANTI", 
			"ANY", "ARRAY", "AS", "ASCENDING", "ASOF", "AST", "ASYNC", "ATTACH", 
			"BETWEEN", "BOTH", "BY", "CACHES", "CASE", "CAST", "CHECK", "CLEAR", 
			"CLUSTER", "CLUSTERS", "CODEC", "COLLATE", "COLUMN", "COLUMNS", "COMMENT", 
			"CONSTRAINT", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_USER", "CHANGED", 
			"DATABASE", "DATABASES", "DATE", "DAY", "DEDUPLICATE", "DEFAULT", "DELAY", 
			"DELETE", "DESC", "DESCENDING", "DESCRIBE", "DETACH", "DICTIONARIES", 
			"DICTIONARY", "DISK", "DISTINCT", "DISTRIBUTED", "DROP", "ELSE", "ENABLED", 
			"END", "ENGINE", "ENGINES", "ESTIMATE", "EVENTS", "EXCEPT", "INTERSECT", 
			"EXISTS", "EXPLAIN", "EXPRESSION", "EXTENDED", "EXTRACT", "FETCHES", 
			"FIELDS", "FILESYSTEM", "FILL", "FINAL", "FIRST", "FLUSH", "FOLLOWING", 
			"FOR", "FORMAT", "FREEZE", "FROM", "FULL", "FUNCTION", "FUNCTIONS", "GLOBAL", 
			"GRANULARITY", "GRANTS", "GROUP", "GROUPING", "HAVING", "HIERARCHICAL", 
			"HOUR", "ID", "IF", "ILIKE", "IMPLICIT", "IN", "INDEX", "INDEXES", "INDICES", 
			"INF", "INJECTIVE", "INNER", "INSERT", "INTERPOLATE", "INTERVAL", "INTO", 
			"IS", "IS_OBJECT_ID", "JOIN", "KEY", "KEYS", "KILL", "LAST", "LAYOUT", 
			"LEADING", "LEFT", "LIFETIME", "LIKE", "LIMIT", "LIVE", "LOCAL", "LOGS", 
			"MATERIALIZE", "MATERIALIZED", "MAX", "MERGES", "MICROSECOND", "MILLISECOND", 
			"MIN", "MINUTE", "MODIFY", "MONTH", "MOVE", "MUTATION", "NAN_SQL", "NANOSECOND", 
			"NO", "NOT", "NULL_SQL", "NULLABLE", "NULLS", "OFFSET", "ON", "OPTIMIZE", 
			"OR", "ORDER", "OUTER", "OUTFILE", "OVER", "OVERRIDE", "PARTITION", "PIPELINE", 
			"PLAN", "POLICY", "POLICIES", "POPULATE", "PRECEDING", "PREWHERE", "QUALIFY", 
			"PRIMARY", "PRIVILEGES", "PROCESSLIST", "PROFILE", "PROFILES", "PROJECTION", 
			"QUARTER", "QUOTA", "QUOTAS", "RANGE", "RECURSIVE", "RELOAD", "REMOVE", 
			"RENAME", "REPLACE", "REPLICA", "REPLICATED", "RIGHT", "ROLE", "ROLES", 
			"ROLLUP", "ROW", "ROWS", "SAMPLE", "SECOND", "SELECT", "SEMI", "SENDS", 
			"SET", "SETS", "SETTING", "SETTINGS", "SHOW", "SOURCE", "START", "STOP", 
			"SUBSTRING", "SYNC", "SYNTAX", "SYSTEM", "STEP", "TABLE", "TABLES", "TEMPORARY", 
			"TEST", "THEN", "TIES", "TIMEOUT", "TIMESTAMP", "TO", "TOP", "TOTALS", 
			"TRAILING", "TREE", "TRIM", "TRUNCATE", "TTL", "TYPE", "UNBOUNDED", "UNION", 
			"UPDATE", "USE", "USER", "USERS", "USING", "UUID", "VALUES", "VIEW", 
			"VOLUME", "WATCH", "WEEK", "WHEN", "WHERE", "WINDOW", "WITH", "YEAR", 
			"JSON_FALSE", "JSON_TRUE", "HEXADECIMAL_STRING_LITERAL", "BINARY_STRING_LITERAL", 
			"IDENTIFIER", "FLOATING_LITERAL", "OCTAL_LITERAL", "DECIMAL_LITERAL", 
			"HEXADECIMAL_NUMERIC_LITERAL", "BINARY_NUMERIC_LITERAL", "STRING_LITERAL", 
			"ARROW", "ASTERISK", "BACKQUOTE", "BACKSLASH", "DOUBLE_COLON", "COLON", 
			"COMMA", "CONCAT", "DASH", "DOT", "EQ_DOUBLE", "EQ_SINGLE", "GE", "GT", 
			"LBRACE", "LBRACKET", "LE", "LPAREN", "LT", "NOT_EQ", "PERCENT", "PLUS", 
			"QUERY", "QUOTE_DOUBLE", "QUOTE_SINGLE", "RBRACE", "RBRACKET", "RPAREN", 
			"SEMICOLON", "SLASH", "UNDERSCORE", "MULTI_LINE_COMMENT", "SINGLE_LINE_COMMENT", 
			"WHITESPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ClickHouseParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ClickHouseParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(ClickHouseParser.EOF, 0); }
		public List<TerminalNode> SEMICOLON() { return getTokens(ClickHouseParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(ClickHouseParser.SEMICOLON, i);
		}
		public List<QueryStmtContext> queryStmt() {
			return getRuleContexts(QueryStmtContext.class);
		}
		public QueryStmtContext queryStmt(int i) {
			return getRuleContext(QueryStmtContext.class,i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(264);
					match(SEMICOLON);
					}
					} 
				}
				setState(269);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALTER) | (1L << ATTACH) | (1L << CHECK) | (1L << CREATE) | (1L << DELETE) | (1L << DESC) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DROP))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (EXISTS - 66)) | (1L << (EXPLAIN - 66)) | (1L << (INSERT - 66)) | (1L << (KILL - 66)))) != 0) || ((((_la - 146)) & ~0x3f) == 0 && ((1L << (_la - 146)) & ((1L << (OPTIMIZE - 146)) | (1L << (RENAME - 146)) | (1L << (REPLACE - 146)) | (1L << (SELECT - 146)) | (1L << (SET - 146)) | (1L << (SHOW - 146)) | (1L << (SYSTEM - 146)))) != 0) || ((((_la - 217)) & ~0x3f) == 0 && ((1L << (_la - 217)) & ((1L << (TRUNCATE - 217)) | (1L << (UPDATE - 217)) | (1L << (USE - 217)) | (1L << (WATCH - 217)) | (1L << (WITH - 217)) | (1L << (LPAREN - 217)))) != 0)) {
				{
				setState(270);
				queryStmt();
				}
			}

			setState(281);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(274); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(273);
						match(SEMICOLON);
						}
						}
						setState(276); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SEMICOLON );
					setState(278);
					queryStmt();
					}
					} 
				}
				setState(283);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMICOLON) {
				{
				{
				setState(284);
				match(SEMICOLON);
				}
				}
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(290);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryStmtContext extends ParserRuleContext {
		public QueryStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryStmt; }
	 
		public QueryStmtContext() { }
		public void copyFrom(QueryStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class QueryStmtUpdateContext extends QueryStmtContext {
		public UpdateStmtContext updateStmt() {
			return getRuleContext(UpdateStmtContext.class,0);
		}
		public QueryStmtUpdateContext(QueryStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterQueryStmtUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitQueryStmtUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitQueryStmtUpdate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class QueryStmtInsertContext extends QueryStmtContext {
		public InsertStmtContext insertStmt() {
			return getRuleContext(InsertStmtContext.class,0);
		}
		public QueryStmtInsertContext(QueryStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterQueryStmtInsert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitQueryStmtInsert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitQueryStmtInsert(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class QueryStmtDeleteContext extends QueryStmtContext {
		public DeleteStmtContext deleteStmt() {
			return getRuleContext(DeleteStmtContext.class,0);
		}
		public QueryStmtDeleteContext(QueryStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterQueryStmtDelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitQueryStmtDelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitQueryStmtDelete(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class QueryStmtQueryContext extends QueryStmtContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode INTO() { return getToken(ClickHouseParser.INTO, 0); }
		public TerminalNode OUTFILE() { return getToken(ClickHouseParser.OUTFILE, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode FORMAT() { return getToken(ClickHouseParser.FORMAT, 0); }
		public IdentifierOrNullContext identifierOrNull() {
			return getRuleContext(IdentifierOrNullContext.class,0);
		}
		public QueryStmtQueryContext(QueryStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterQueryStmtQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitQueryStmtQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitQueryStmtQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryStmtContext queryStmt() throws RecognitionException {
		QueryStmtContext _localctx = new QueryStmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_queryStmt);
		int _la;
		try {
			setState(305);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ALTER:
			case ATTACH:
			case CHECK:
			case CREATE:
			case DESC:
			case DESCRIBE:
			case DETACH:
			case DROP:
			case EXISTS:
			case EXPLAIN:
			case KILL:
			case OPTIMIZE:
			case RENAME:
			case REPLACE:
			case SELECT:
			case SET:
			case SHOW:
			case SYSTEM:
			case TRUNCATE:
			case USE:
			case WATCH:
			case WITH:
			case LPAREN:
				_localctx = new QueryStmtQueryContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(292);
				query();
				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INTO) {
					{
					setState(293);
					match(INTO);
					setState(294);
					match(OUTFILE);
					setState(295);
					stringLiteral();
					}
				}

				setState(300);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FORMAT) {
					{
					setState(298);
					match(FORMAT);
					setState(299);
					identifierOrNull();
					}
				}

				}
				break;
			case INSERT:
				_localctx = new QueryStmtInsertContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				insertStmt();
				}
				break;
			case DELETE:
				_localctx = new QueryStmtDeleteContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(303);
				deleteStmt();
				}
				break;
			case UPDATE:
				_localctx = new QueryStmtUpdateContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(304);
				updateStmt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryContext extends ParserRuleContext {
		public AlterStmtContext alterStmt() {
			return getRuleContext(AlterStmtContext.class,0);
		}
		public AttachStmtContext attachStmt() {
			return getRuleContext(AttachStmtContext.class,0);
		}
		public CheckStmtContext checkStmt() {
			return getRuleContext(CheckStmtContext.class,0);
		}
		public CreateStmtContext createStmt() {
			return getRuleContext(CreateStmtContext.class,0);
		}
		public DescribeStmtContext describeStmt() {
			return getRuleContext(DescribeStmtContext.class,0);
		}
		public DropStmtContext dropStmt() {
			return getRuleContext(DropStmtContext.class,0);
		}
		public ExistsStmtContext existsStmt() {
			return getRuleContext(ExistsStmtContext.class,0);
		}
		public ExplainStmtContext explainStmt() {
			return getRuleContext(ExplainStmtContext.class,0);
		}
		public KillStmtContext killStmt() {
			return getRuleContext(KillStmtContext.class,0);
		}
		public OptimizeStmtContext optimizeStmt() {
			return getRuleContext(OptimizeStmtContext.class,0);
		}
		public RenameStmtContext renameStmt() {
			return getRuleContext(RenameStmtContext.class,0);
		}
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public SetStmtContext setStmt() {
			return getRuleContext(SetStmtContext.class,0);
		}
		public ShowStmtContext showStmt() {
			return getRuleContext(ShowStmtContext.class,0);
		}
		public SystemStmtContext systemStmt() {
			return getRuleContext(SystemStmtContext.class,0);
		}
		public TruncateStmtContext truncateStmt() {
			return getRuleContext(TruncateStmtContext.class,0);
		}
		public UseStmtContext useStmt() {
			return getRuleContext(UseStmtContext.class,0);
		}
		public WatchStmtContext watchStmt() {
			return getRuleContext(WatchStmtContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_query);
		try {
			setState(325);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(307);
				alterStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(308);
				attachStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(309);
				checkStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(310);
				createStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(311);
				describeStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(312);
				dropStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(313);
				existsStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(314);
				explainStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(315);
				killStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(316);
				optimizeStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(317);
				renameStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(318);
				selectUnionStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(319);
				setStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(320);
				showStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(321);
				systemStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(322);
				truncateStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(323);
				useStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(324);
				watchStmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AlterStmtContext extends ParserRuleContext {
		public AlterStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alterStmt; }
	 
		public AlterStmtContext() { }
		public void copyFrom(AlterStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AlterTableStmtContext extends AlterStmtContext {
		public TerminalNode ALTER() { return getToken(ClickHouseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public List<AlterTableClauseContext> alterTableClause() {
			return getRuleContexts(AlterTableClauseContext.class);
		}
		public AlterTableClauseContext alterTableClause(int i) {
			return getRuleContext(AlterTableClauseContext.class,i);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public AlterTableStmtContext(AlterStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlterStmtContext alterStmt() throws RecognitionException {
		AlterStmtContext _localctx = new AlterStmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_alterStmt);
		int _la;
		try {
			_localctx = new AlterTableStmtContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			match(ALTER);
			setState(328);
			match(TABLE);
			setState(329);
			tableIdentifier();
			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(330);
				clusterClause();
				}
			}

			setState(333);
			alterTableClause();
			setState(338);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(334);
				match(COMMA);
				setState(335);
				alterTableClause();
				}
				}
				setState(340);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AlterTableClauseContext extends ParserRuleContext {
		public AlterTableClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alterTableClause; }
	 
		public AlterTableClauseContext() { }
		public void copyFrom(AlterTableClauseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AlterTableClauseReplaceContext extends AlterTableClauseContext {
		public TerminalNode REPLACE() { return getToken(ClickHouseParser.REPLACE, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public AlterTableClauseReplaceContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseReplace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseReplace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseReplace(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableModifyColumnContext extends AlterTableClauseContext {
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public TableColumnPropertyExprContext tableColumnPropertyExpr() {
			return getRuleContext(TableColumnPropertyExprContext.class,0);
		}
		public CommentClauseContext commentClause() {
			return getRuleContext(CommentClauseContext.class,0);
		}
		public CodecExprContext codecExpr() {
			return getRuleContext(CodecExprContext.class,0);
		}
		public TtlClauseContext ttlClause() {
			return getRuleContext(TtlClauseContext.class,0);
		}
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode FIRST() { return getToken(ClickHouseParser.FIRST, 0); }
		public SettingExprListContext settingExprList() {
			return getRuleContext(SettingExprListContext.class,0);
		}
		public AlterTableModifyColumnContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableModifyColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableModifyColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableModifyColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseModifyOrderByContext extends AlterTableClauseContext {
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public TerminalNode ORDER() { return getToken(ClickHouseParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public AlterTableClauseModifyOrderByContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseModifyOrderBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseModifyOrderBy(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseModifyOrderBy(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseUpdateContext extends AlterTableClauseContext {
		public TerminalNode UPDATE() { return getToken(ClickHouseParser.UPDATE, 0); }
		public AssignmentExprListContext assignmentExprList() {
			return getRuleContext(AssignmentExprListContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public AlterTableClauseUpdateContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseUpdate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableModifyCommentContext extends AlterTableClauseContext {
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public CommentClauseContext commentClause() {
			return getRuleContext(CommentClauseContext.class,0);
		}
		public AlterTableModifyCommentContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableModifyComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableModifyComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableModifyComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseClearProjectionContext extends AlterTableClauseContext {
		public TerminalNode CLEAR() { return getToken(ClickHouseParser.CLEAR, 0); }
		public TerminalNode PROJECTION() { return getToken(ClickHouseParser.PROJECTION, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseClearProjectionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseClearProjection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseClearProjection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseClearProjection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableModifyColumnDefaultContext extends AlterTableClauseContext {
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(ClickHouseParser.DEFAULT, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public AlterTableModifyColumnDefaultContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableModifyColumnDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableModifyColumnDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableModifyColumnDefault(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseDeleteContext extends AlterTableClauseContext {
		public TerminalNode DELETE() { return getToken(ClickHouseParser.DELETE, 0); }
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public AlterTableClauseDeleteContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseDelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseDelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseDelete(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseCommentContext extends AlterTableClauseContext {
		public TerminalNode COMMENT() { return getToken(ClickHouseParser.COMMENT, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public AlterTableClauseCommentContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseDropColumnContext extends AlterTableClauseContext {
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public AlterTableClauseDropColumnContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseDropColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseDropColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseDropColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseDetachContext extends AlterTableClauseContext {
		public TerminalNode DETACH() { return getToken(ClickHouseParser.DETACH, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseDetachContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseDetach(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseDetach(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseDetach(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseAddIndexContext extends AlterTableClauseContext {
		public TerminalNode ADD() { return getToken(ClickHouseParser.ADD, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public TableIndexDfntContext tableIndexDfnt() {
			return getRuleContext(TableIndexDfntContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public AlterTableClauseAddIndexContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseAddIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseAddIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseAddIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseDropPartitionContext extends AlterTableClauseContext {
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseDropPartitionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseDropPartition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseDropPartition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseDropPartition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseMaterializeIndexContext extends AlterTableClauseContext {
		public TerminalNode MATERIALIZE() { return getToken(ClickHouseParser.MATERIALIZE, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseMaterializeIndexContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseMaterializeIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseMaterializeIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseMaterializeIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseMaterializeProjectionContext extends AlterTableClauseContext {
		public TerminalNode MATERIALIZE() { return getToken(ClickHouseParser.MATERIALIZE, 0); }
		public TerminalNode PROJECTION() { return getToken(ClickHouseParser.PROJECTION, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseMaterializeProjectionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseMaterializeProjection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseMaterializeProjection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseMaterializeProjection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseMovePartitionContext extends AlterTableClauseContext {
		public TerminalNode MOVE() { return getToken(ClickHouseParser.MOVE, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public TerminalNode TO() { return getToken(ClickHouseParser.TO, 0); }
		public TerminalNode DISK() { return getToken(ClickHouseParser.DISK, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode VOLUME() { return getToken(ClickHouseParser.VOLUME, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public AlterTableClauseMovePartitionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseMovePartition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseMovePartition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseMovePartition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseFreezePartitionContext extends AlterTableClauseContext {
		public TerminalNode FREEZE() { return getToken(ClickHouseParser.FREEZE, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseFreezePartitionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseFreezePartition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseFreezePartition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseFreezePartition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseClearColumnContext extends AlterTableClauseContext {
		public TerminalNode CLEAR() { return getToken(ClickHouseParser.CLEAR, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseClearColumnContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseClearColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseClearColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseClearColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseClearIndexContext extends AlterTableClauseContext {
		public TerminalNode CLEAR() { return getToken(ClickHouseParser.CLEAR, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public AlterTableClauseClearIndexContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseClearIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseClearIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseClearIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseRemoveTTLContext extends AlterTableClauseContext {
		public TerminalNode REMOVE() { return getToken(ClickHouseParser.REMOVE, 0); }
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public AlterTableClauseRemoveTTLContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseRemoveTTL(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseRemoveTTL(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseRemoveTTL(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableAlterColumnContext extends AlterTableClauseContext {
		public TerminalNode ALTER() { return getToken(ClickHouseParser.ALTER, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode TYPE() { return getToken(ClickHouseParser.TYPE, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public TableColumnPropertyExprContext tableColumnPropertyExpr() {
			return getRuleContext(TableColumnPropertyExprContext.class,0);
		}
		public CommentClauseContext commentClause() {
			return getRuleContext(CommentClauseContext.class,0);
		}
		public CodecExprContext codecExpr() {
			return getRuleContext(CodecExprContext.class,0);
		}
		public TtlClauseContext ttlClause() {
			return getRuleContext(TtlClauseContext.class,0);
		}
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode FIRST() { return getToken(ClickHouseParser.FIRST, 0); }
		public SettingExprListContext settingExprList() {
			return getRuleContext(SettingExprListContext.class,0);
		}
		public AlterTableAlterColumnContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableAlterColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableAlterColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableAlterColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseAttachContext extends AlterTableClauseContext {
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public AlterTableClauseAttachContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseAttach(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseAttach(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseAttach(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseDropProjectionContext extends AlterTableClauseContext {
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode PROJECTION() { return getToken(ClickHouseParser.PROJECTION, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public AlterTableClauseDropProjectionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseDropProjection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseDropProjection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseDropProjection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseRenameColumnContext extends AlterTableClauseContext {
		public TerminalNode RENAME() { return getToken(ClickHouseParser.RENAME, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode TO() { return getToken(ClickHouseParser.TO, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public AlterTableClauseRenameColumnContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseRenameColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseRenameColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseRenameColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseDropIndexContext extends AlterTableClauseContext {
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public AlterTableClauseDropIndexContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseDropIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseDropIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseDropIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseModifyTTLContext extends AlterTableClauseContext {
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public TtlClauseContext ttlClause() {
			return getRuleContext(TtlClauseContext.class,0);
		}
		public AlterTableClauseModifyTTLContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseModifyTTL(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseModifyTTL(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseModifyTTL(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseAddProjectionContext extends AlterTableClauseContext {
		public TerminalNode ADD() { return getToken(ClickHouseParser.ADD, 0); }
		public TerminalNode PROJECTION() { return getToken(ClickHouseParser.PROJECTION, 0); }
		public TableProjectionDfntContext tableProjectionDfnt() {
			return getRuleContext(TableProjectionDfntContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public AlterTableClauseAddProjectionContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseAddProjection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseAddProjection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseAddProjection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlterTableClauseAddColumnContext extends AlterTableClauseContext {
		public TerminalNode ADD() { return getToken(ClickHouseParser.ADD, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public TableColumnDfntContext tableColumnDfnt() {
			return getRuleContext(TableColumnDfntContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode FIRST() { return getToken(ClickHouseParser.FIRST, 0); }
		public AlterTableClauseAddColumnContext(AlterTableClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlterTableClauseAddColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlterTableClauseAddColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlterTableClauseAddColumn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlterTableClauseContext alterTableClause() throws RecognitionException {
		AlterTableClauseContext _localctx = new AlterTableClauseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_alterTableClause);
		int _la;
		try {
			setState(589);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				_localctx = new AlterTableClauseAddColumnContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(341);
				match(ADD);
				setState(342);
				match(COLUMN);
				setState(346);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(343);
					match(IF);
					setState(344);
					match(NOT);
					setState(345);
					match(EXISTS);
					}
					break;
				}
				setState(348);
				tableColumnDfnt();
				setState(352);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case AFTER:
					{
					setState(349);
					match(AFTER);
					setState(350);
					nestedIdentifier();
					}
					break;
				case FIRST:
					{
					setState(351);
					match(FIRST);
					}
					break;
				case EOF:
				case FORMAT:
				case INTO:
				case COMMA:
				case SEMICOLON:
					break;
				default:
					break;
				}
				}
				break;
			case 2:
				_localctx = new AlterTableClauseAddIndexContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(354);
				match(ADD);
				setState(355);
				match(INDEX);
				setState(359);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(356);
					match(IF);
					setState(357);
					match(NOT);
					setState(358);
					match(EXISTS);
					}
					break;
				}
				setState(361);
				tableIndexDfnt();
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AFTER) {
					{
					setState(362);
					match(AFTER);
					setState(363);
					nestedIdentifier();
					}
				}

				}
				break;
			case 3:
				_localctx = new AlterTableClauseAddProjectionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(366);
				match(ADD);
				setState(367);
				match(PROJECTION);
				setState(371);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(368);
					match(IF);
					setState(369);
					match(NOT);
					setState(370);
					match(EXISTS);
					}
					break;
				}
				setState(373);
				tableProjectionDfnt();
				setState(376);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AFTER) {
					{
					setState(374);
					match(AFTER);
					setState(375);
					nestedIdentifier();
					}
				}

				}
				break;
			case 4:
				_localctx = new AlterTableClauseAttachContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(378);
				match(ATTACH);
				setState(379);
				partitionClause();
				setState(382);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FROM) {
					{
					setState(380);
					match(FROM);
					setState(381);
					tableIdentifier();
					}
				}

				}
				break;
			case 5:
				_localctx = new AlterTableClauseClearColumnContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(384);
				match(CLEAR);
				setState(385);
				match(COLUMN);
				setState(388);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
				case 1:
					{
					setState(386);
					match(IF);
					setState(387);
					match(EXISTS);
					}
					break;
				}
				setState(390);
				nestedIdentifier();
				setState(393);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IN) {
					{
					setState(391);
					match(IN);
					setState(392);
					partitionClause();
					}
				}

				}
				break;
			case 6:
				_localctx = new AlterTableClauseClearIndexContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(395);
				match(CLEAR);
				setState(396);
				match(INDEX);
				setState(399);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(397);
					match(IF);
					setState(398);
					match(EXISTS);
					}
					break;
				}
				setState(401);
				nestedIdentifier();
				setState(404);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IN) {
					{
					setState(402);
					match(IN);
					setState(403);
					partitionClause();
					}
				}

				}
				break;
			case 7:
				_localctx = new AlterTableClauseClearProjectionContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(406);
				match(CLEAR);
				setState(407);
				match(PROJECTION);
				setState(410);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(408);
					match(IF);
					setState(409);
					match(EXISTS);
					}
					break;
				}
				setState(412);
				nestedIdentifier();
				setState(415);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IN) {
					{
					setState(413);
					match(IN);
					setState(414);
					partitionClause();
					}
				}

				}
				break;
			case 8:
				_localctx = new AlterTableClauseCommentContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(417);
				match(COMMENT);
				setState(418);
				match(COLUMN);
				setState(421);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(419);
					match(IF);
					setState(420);
					match(EXISTS);
					}
					break;
				}
				setState(423);
				identifier();
				setState(424);
				stringLiteral();
				}
				break;
			case 9:
				_localctx = new AlterTableClauseDeleteContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(426);
				match(DELETE);
				setState(427);
				match(WHERE);
				setState(428);
				columnExpr(0);
				}
				break;
			case 10:
				_localctx = new AlterTableClauseDetachContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(429);
				match(DETACH);
				setState(430);
				partitionClause();
				}
				break;
			case 11:
				_localctx = new AlterTableClauseDropColumnContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(431);
				match(DROP);
				setState(432);
				match(COLUMN);
				setState(435);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
				case 1:
					{
					setState(433);
					match(IF);
					setState(434);
					match(EXISTS);
					}
					break;
				}
				setState(437);
				identifier();
				}
				break;
			case 12:
				_localctx = new AlterTableClauseDropIndexContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(438);
				match(DROP);
				setState(439);
				match(INDEX);
				setState(442);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(440);
					match(IF);
					setState(441);
					match(EXISTS);
					}
					break;
				}
				setState(444);
				nestedIdentifier();
				}
				break;
			case 13:
				_localctx = new AlterTableClauseDropProjectionContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(445);
				match(DROP);
				setState(446);
				match(PROJECTION);
				setState(449);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(447);
					match(IF);
					setState(448);
					match(EXISTS);
					}
					break;
				}
				setState(451);
				nestedIdentifier();
				}
				break;
			case 14:
				_localctx = new AlterTableClauseDropPartitionContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(452);
				match(DROP);
				setState(453);
				partitionClause();
				}
				break;
			case 15:
				_localctx = new AlterTableClauseFreezePartitionContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(454);
				match(FREEZE);
				setState(456);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PARTITION) {
					{
					setState(455);
					partitionClause();
					}
				}

				}
				break;
			case 16:
				_localctx = new AlterTableClauseMaterializeIndexContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(458);
				match(MATERIALIZE);
				setState(459);
				match(INDEX);
				setState(462);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(460);
					match(IF);
					setState(461);
					match(EXISTS);
					}
					break;
				}
				setState(464);
				nestedIdentifier();
				setState(467);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IN) {
					{
					setState(465);
					match(IN);
					setState(466);
					partitionClause();
					}
				}

				}
				break;
			case 17:
				_localctx = new AlterTableClauseMaterializeProjectionContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(469);
				match(MATERIALIZE);
				setState(470);
				match(PROJECTION);
				setState(473);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(471);
					match(IF);
					setState(472);
					match(EXISTS);
					}
					break;
				}
				setState(475);
				nestedIdentifier();
				setState(478);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IN) {
					{
					setState(476);
					match(IN);
					setState(477);
					partitionClause();
					}
				}

				}
				break;
			case 18:
				_localctx = new AlterTableClauseModifyOrderByContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(480);
				match(MODIFY);
				setState(481);
				match(ORDER);
				setState(482);
				match(BY);
				setState(483);
				columnExpr(0);
				}
				break;
			case 19:
				_localctx = new AlterTableClauseModifyTTLContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(484);
				match(MODIFY);
				setState(485);
				ttlClause();
				}
				break;
			case 20:
				_localctx = new AlterTableClauseMovePartitionContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(486);
				match(MOVE);
				setState(487);
				partitionClause();
				setState(497);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
				case 1:
					{
					setState(488);
					match(TO);
					setState(489);
					match(DISK);
					setState(490);
					stringLiteral();
					}
					break;
				case 2:
					{
					setState(491);
					match(TO);
					setState(492);
					match(VOLUME);
					setState(493);
					stringLiteral();
					}
					break;
				case 3:
					{
					setState(494);
					match(TO);
					setState(495);
					match(TABLE);
					setState(496);
					tableIdentifier();
					}
					break;
				}
				}
				break;
			case 21:
				_localctx = new AlterTableClauseRemoveTTLContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(499);
				match(REMOVE);
				setState(500);
				match(TTL);
				}
				break;
			case 22:
				_localctx = new AlterTableClauseRenameColumnContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(501);
				match(RENAME);
				setState(502);
				match(COLUMN);
				setState(505);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(503);
					match(IF);
					setState(504);
					match(EXISTS);
					}
					break;
				}
				setState(507);
				identifier();
				setState(508);
				match(TO);
				setState(509);
				identifier();
				}
				break;
			case 23:
				_localctx = new AlterTableClauseReplaceContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(511);
				match(REPLACE);
				setState(512);
				partitionClause();
				setState(513);
				match(FROM);
				setState(514);
				tableIdentifier();
				}
				break;
			case 24:
				_localctx = new AlterTableClauseUpdateContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(516);
				match(UPDATE);
				setState(517);
				assignmentExprList();
				setState(518);
				whereClause();
				}
				break;
			case 25:
				_localctx = new AlterTableModifyCommentContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(520);
				match(MODIFY);
				setState(521);
				commentClause();
				}
				break;
			case 26:
				_localctx = new AlterTableModifyColumnDefaultContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(522);
				match(MODIFY);
				setState(523);
				match(COLUMN);
				setState(524);
				identifier();
				setState(525);
				match(DEFAULT);
				setState(526);
				literal();
				}
				break;
			case 27:
				_localctx = new AlterTableAlterColumnContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(528);
				match(ALTER);
				setState(529);
				match(COLUMN);
				setState(532);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
				case 1:
					{
					setState(530);
					match(IF);
					setState(531);
					match(EXISTS);
					}
					break;
				}
				setState(534);
				identifier();
				setState(535);
				match(TYPE);
				setState(537);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(536);
					columnTypeExpr();
					}
					break;
				}
				setState(540);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(539);
					tableColumnPropertyExpr();
					}
					break;
				}
				setState(543);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
				case 1:
					{
					setState(542);
					commentClause();
					}
					break;
				}
				setState(546);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
				case 1:
					{
					setState(545);
					codecExpr();
					}
					break;
				}
				setState(549);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(548);
					ttlClause();
					}
					break;
				}
				setState(554);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
				case 1:
					{
					setState(551);
					match(AFTER);
					setState(552);
					nestedIdentifier();
					}
					break;
				case 2:
					{
					setState(553);
					match(FIRST);
					}
					break;
				}
				setState(557);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(556);
					settingExprList();
					}
					break;
				}
				}
				break;
			case 28:
				_localctx = new AlterTableModifyColumnContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(559);
				match(MODIFY);
				setState(560);
				match(COLUMN);
				setState(563);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
				case 1:
					{
					setState(561);
					match(IF);
					setState(562);
					match(EXISTS);
					}
					break;
				}
				setState(565);
				identifier();
				setState(567);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
				case 1:
					{
					setState(566);
					columnTypeExpr();
					}
					break;
				}
				setState(570);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(569);
					tableColumnPropertyExpr();
					}
					break;
				}
				setState(573);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
				case 1:
					{
					setState(572);
					commentClause();
					}
					break;
				}
				setState(576);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
				case 1:
					{
					setState(575);
					codecExpr();
					}
					break;
				}
				setState(579);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(578);
					ttlClause();
					}
					break;
				}
				setState(584);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
				case 1:
					{
					setState(581);
					match(AFTER);
					setState(582);
					nestedIdentifier();
					}
					break;
				case 2:
					{
					setState(583);
					match(FIRST);
					}
					break;
				}
				setState(587);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
				case 1:
					{
					setState(586);
					settingExprList();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentExprListContext extends ParserRuleContext {
		public List<AssignmentExprContext> assignmentExpr() {
			return getRuleContexts(AssignmentExprContext.class);
		}
		public AssignmentExprContext assignmentExpr(int i) {
			return getRuleContext(AssignmentExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public AssignmentExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAssignmentExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAssignmentExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAssignmentExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentExprListContext assignmentExprList() throws RecognitionException {
		AssignmentExprListContext _localctx = new AssignmentExprListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assignmentExprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(591);
			assignmentExpr();
			setState(596);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(592);
				match(COMMA);
				setState(593);
				assignmentExpr();
				}
				}
				setState(598);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentExprContext extends ParserRuleContext {
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode EQ_SINGLE() { return getToken(ClickHouseParser.EQ_SINGLE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public AssignmentExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAssignmentExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAssignmentExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAssignmentExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentExprContext assignmentExpr() throws RecognitionException {
		AssignmentExprContext _localctx = new AssignmentExprContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_assignmentExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(599);
			nestedIdentifier();
			setState(600);
			match(EQ_SINGLE);
			setState(601);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableColumnPropertyTypeContext extends ParserRuleContext {
		public TerminalNode ALIAS() { return getToken(ClickHouseParser.ALIAS, 0); }
		public TerminalNode CODEC() { return getToken(ClickHouseParser.CODEC, 0); }
		public TerminalNode COMMENT() { return getToken(ClickHouseParser.COMMENT, 0); }
		public TerminalNode DEFAULT() { return getToken(ClickHouseParser.DEFAULT, 0); }
		public TerminalNode MATERIALIZED() { return getToken(ClickHouseParser.MATERIALIZED, 0); }
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public TableColumnPropertyTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableColumnPropertyType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableColumnPropertyType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableColumnPropertyType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableColumnPropertyType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableColumnPropertyTypeContext tableColumnPropertyType() throws RecognitionException {
		TableColumnPropertyTypeContext _localctx = new TableColumnPropertyTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_tableColumnPropertyType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(603);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALIAS) | (1L << CODEC) | (1L << COMMENT) | (1L << DEFAULT))) != 0) || _la==MATERIALIZED || _la==TTL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartitionClauseContext extends ParserRuleContext {
		public TerminalNode PARTITION() { return getToken(ClickHouseParser.PARTITION, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode ID() { return getToken(ClickHouseParser.ID, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public PartitionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partitionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterPartitionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitPartitionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitPartitionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PartitionClauseContext partitionClause() throws RecognitionException {
		PartitionClauseContext _localctx = new PartitionClauseContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_partitionClause);
		try {
			setState(610);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(605);
				match(PARTITION);
				setState(606);
				columnExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(607);
				match(PARTITION);
				setState(608);
				match(ID);
				setState(609);
				stringLiteral();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttachStmtContext extends ParserRuleContext {
		public AttachStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attachStmt; }
	 
		public AttachStmtContext() { }
		public void copyFrom(AttachStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AttachDictionaryStmtContext extends AttachStmtContext {
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public AttachDictionaryStmtContext(AttachStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAttachDictionaryStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAttachDictionaryStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAttachDictionaryStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttachStmtContext attachStmt() throws RecognitionException {
		AttachStmtContext _localctx = new AttachStmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_attachStmt);
		int _la;
		try {
			_localctx = new AttachDictionaryStmtContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			match(ATTACH);
			setState(613);
			match(DICTIONARY);
			setState(614);
			tableIdentifier();
			setState(616);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(615);
				clusterClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CheckStmtContext extends ParserRuleContext {
		public TerminalNode CHECK() { return getToken(ClickHouseParser.CHECK, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public CheckStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checkStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCheckStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCheckStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCheckStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CheckStmtContext checkStmt() throws RecognitionException {
		CheckStmtContext _localctx = new CheckStmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_checkStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(618);
			match(CHECK);
			setState(619);
			match(TABLE);
			setState(620);
			tableIdentifier();
			setState(622);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PARTITION) {
				{
				setState(621);
				partitionClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreateStmtContext extends ParserRuleContext {
		public CreateStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createStmt; }
	 
		public CreateStmtContext() { }
		public void copyFrom(CreateStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CreateViewStmtContext extends CreateStmtContext {
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public SubqueryClauseContext subqueryClause() {
			return getRuleContext(SubqueryClauseContext.class,0);
		}
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode OR() { return getToken(ClickHouseParser.OR, 0); }
		public TerminalNode REPLACE() { return getToken(ClickHouseParser.REPLACE, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public UuidClauseContext uuidClause() {
			return getRuleContext(UuidClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TableSchemaClauseContext tableSchemaClause() {
			return getRuleContext(TableSchemaClauseContext.class,0);
		}
		public CreateViewStmtContext(CreateStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCreateViewStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCreateViewStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCreateViewStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateDictionaryStmtContext extends CreateStmtContext {
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public DictionarySchemaClauseContext dictionarySchemaClause() {
			return getRuleContext(DictionarySchemaClauseContext.class,0);
		}
		public DictionaryEngineClauseContext dictionaryEngineClause() {
			return getRuleContext(DictionaryEngineClauseContext.class,0);
		}
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode REPLACE() { return getToken(ClickHouseParser.REPLACE, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public UuidClauseContext uuidClause() {
			return getRuleContext(UuidClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TerminalNode OR() { return getToken(ClickHouseParser.OR, 0); }
		public CreateDictionaryStmtContext(CreateStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCreateDictionaryStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCreateDictionaryStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCreateDictionaryStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateDatabaseStmtContext extends CreateStmtContext {
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public EngineExprContext engineExpr() {
			return getRuleContext(EngineExprContext.class,0);
		}
		public CommentClauseContext commentClause() {
			return getRuleContext(CommentClauseContext.class,0);
		}
		public CreateDatabaseStmtContext(CreateStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCreateDatabaseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCreateDatabaseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCreateDatabaseStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateMaterializedViewStmtContext extends CreateStmtContext {
		public TerminalNode MATERIALIZED() { return getToken(ClickHouseParser.MATERIALIZED, 0); }
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public SubqueryClauseContext subqueryClause() {
			return getRuleContext(SubqueryClauseContext.class,0);
		}
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public DestinationClauseContext destinationClause() {
			return getRuleContext(DestinationClauseContext.class,0);
		}
		public EngineClauseContext engineClause() {
			return getRuleContext(EngineClauseContext.class,0);
		}
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public UuidClauseContext uuidClause() {
			return getRuleContext(UuidClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TableSchemaClauseContext tableSchemaClause() {
			return getRuleContext(TableSchemaClauseContext.class,0);
		}
		public TerminalNode POPULATE() { return getToken(ClickHouseParser.POPULATE, 0); }
		public CreateMaterializedViewStmtContext(CreateStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCreateMaterializedViewStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCreateMaterializedViewStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCreateMaterializedViewStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateTableStmtContext extends CreateStmtContext {
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode REPLACE() { return getToken(ClickHouseParser.REPLACE, 0); }
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public UuidClauseContext uuidClause() {
			return getRuleContext(UuidClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TableSchemaClauseContext tableSchemaClause() {
			return getRuleContext(TableSchemaClauseContext.class,0);
		}
		public EngineClauseContext engineClause() {
			return getRuleContext(EngineClauseContext.class,0);
		}
		public SubqueryClauseContext subqueryClause() {
			return getRuleContext(SubqueryClauseContext.class,0);
		}
		public TerminalNode OR() { return getToken(ClickHouseParser.OR, 0); }
		public CreateTableStmtContext(CreateStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCreateTableStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCreateTableStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCreateTableStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateStmtContext createStmt() throws RecognitionException {
		CreateStmtContext _localctx = new CreateStmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_createStmt);
		int _la;
		try {
			setState(750);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				_localctx = new CreateDatabaseStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(624);
				_la = _input.LA(1);
				if ( !(_la==ATTACH || _la==CREATE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(625);
				match(DATABASE);
				setState(629);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
				case 1:
					{
					setState(626);
					match(IF);
					setState(627);
					match(NOT);
					setState(628);
					match(EXISTS);
					}
					break;
				}
				setState(631);
				databaseIdentifier();
				setState(633);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(632);
					clusterClause();
					}
				}

				setState(636);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ENGINE) {
					{
					setState(635);
					engineExpr();
					}
				}

				setState(639);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(638);
					commentClause();
					}
				}

				}
				break;
			case 2:
				_localctx = new CreateDictionaryStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(648);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ATTACH:
					{
					setState(641);
					match(ATTACH);
					}
					break;
				case CREATE:
					{
					setState(642);
					match(CREATE);
					setState(645);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OR) {
						{
						setState(643);
						match(OR);
						setState(644);
						match(REPLACE);
						}
					}

					}
					break;
				case REPLACE:
					{
					setState(647);
					match(REPLACE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(650);
				match(DICTIONARY);
				setState(654);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
				case 1:
					{
					setState(651);
					match(IF);
					setState(652);
					match(NOT);
					setState(653);
					match(EXISTS);
					}
					break;
				}
				setState(656);
				tableIdentifier();
				setState(658);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==UUID) {
					{
					setState(657);
					uuidClause();
					}
				}

				setState(661);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(660);
					clusterClause();
					}
				}

				setState(663);
				dictionarySchemaClause();
				setState(664);
				dictionaryEngineClause();
				}
				break;
			case 3:
				_localctx = new CreateMaterializedViewStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(666);
				_la = _input.LA(1);
				if ( !(_la==ATTACH || _la==CREATE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(667);
				match(MATERIALIZED);
				setState(668);
				match(VIEW);
				setState(672);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
				case 1:
					{
					setState(669);
					match(IF);
					setState(670);
					match(NOT);
					setState(671);
					match(EXISTS);
					}
					break;
				}
				setState(674);
				tableIdentifier();
				setState(676);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==UUID) {
					{
					setState(675);
					uuidClause();
					}
				}

				setState(679);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(678);
					clusterClause();
					}
				}

				setState(682);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AS || _la==LPAREN) {
					{
					setState(681);
					tableSchemaClause();
					}
				}

				setState(689);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case TO:
					{
					setState(684);
					destinationClause();
					}
					break;
				case ENGINE:
					{
					setState(685);
					engineClause();
					setState(687);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==POPULATE) {
						{
						setState(686);
						match(POPULATE);
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(691);
				subqueryClause();
				}
				break;
			case 4:
				_localctx = new CreateTableStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(700);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ATTACH:
					{
					setState(693);
					match(ATTACH);
					}
					break;
				case CREATE:
					{
					setState(694);
					match(CREATE);
					setState(697);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OR) {
						{
						setState(695);
						match(OR);
						setState(696);
						match(REPLACE);
						}
					}

					}
					break;
				case REPLACE:
					{
					setState(699);
					match(REPLACE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(703);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TEMPORARY) {
					{
					setState(702);
					match(TEMPORARY);
					}
				}

				setState(705);
				match(TABLE);
				setState(709);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
				case 1:
					{
					setState(706);
					match(IF);
					setState(707);
					match(NOT);
					setState(708);
					match(EXISTS);
					}
					break;
				}
				setState(711);
				tableIdentifier();
				setState(713);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==UUID) {
					{
					setState(712);
					uuidClause();
					}
				}

				setState(716);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(715);
					clusterClause();
					}
				}

				setState(719);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
				case 1:
					{
					setState(718);
					tableSchemaClause();
					}
					break;
				}
				setState(722);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ENGINE) {
					{
					setState(721);
					engineClause();
					}
				}

				setState(725);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AS) {
					{
					setState(724);
					subqueryClause();
					}
				}

				}
				break;
			case 5:
				_localctx = new CreateViewStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(727);
				_la = _input.LA(1);
				if ( !(_la==ATTACH || _la==CREATE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(730);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OR) {
					{
					setState(728);
					match(OR);
					setState(729);
					match(REPLACE);
					}
				}

				setState(732);
				match(VIEW);
				setState(736);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(733);
					match(IF);
					setState(734);
					match(NOT);
					setState(735);
					match(EXISTS);
					}
					break;
				}
				setState(738);
				tableIdentifier();
				setState(740);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==UUID) {
					{
					setState(739);
					uuidClause();
					}
				}

				setState(743);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(742);
					clusterClause();
					}
				}

				setState(746);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(745);
					tableSchemaClause();
					}
					break;
				}
				setState(748);
				subqueryClause();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionarySchemaClauseContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<DictionaryAttrDfntContext> dictionaryAttrDfnt() {
			return getRuleContexts(DictionaryAttrDfntContext.class);
		}
		public DictionaryAttrDfntContext dictionaryAttrDfnt(int i) {
			return getRuleContext(DictionaryAttrDfntContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public DictionarySchemaClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionarySchemaClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionarySchemaClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionarySchemaClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionarySchemaClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionarySchemaClauseContext dictionarySchemaClause() throws RecognitionException {
		DictionarySchemaClauseContext _localctx = new DictionarySchemaClauseContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_dictionarySchemaClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(752);
			match(LPAREN);
			setState(753);
			dictionaryAttrDfnt();
			setState(758);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(754);
				match(COMMA);
				setState(755);
				dictionaryAttrDfnt();
				}
				}
				setState(760);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(761);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionaryAttrDfntContext extends ParserRuleContext {
		public java.util.Set<String> attrs = new java.util.HashSet<String>();;
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public List<TerminalNode> DEFAULT() { return getTokens(ClickHouseParser.DEFAULT); }
		public TerminalNode DEFAULT(int i) {
			return getToken(ClickHouseParser.DEFAULT, i);
		}
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<TerminalNode> EXPRESSION() { return getTokens(ClickHouseParser.EXPRESSION); }
		public TerminalNode EXPRESSION(int i) {
			return getToken(ClickHouseParser.EXPRESSION, i);
		}
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public List<TerminalNode> HIERARCHICAL() { return getTokens(ClickHouseParser.HIERARCHICAL); }
		public TerminalNode HIERARCHICAL(int i) {
			return getToken(ClickHouseParser.HIERARCHICAL, i);
		}
		public List<TerminalNode> INJECTIVE() { return getTokens(ClickHouseParser.INJECTIVE); }
		public TerminalNode INJECTIVE(int i) {
			return getToken(ClickHouseParser.INJECTIVE, i);
		}
		public List<TerminalNode> IS_OBJECT_ID() { return getTokens(ClickHouseParser.IS_OBJECT_ID); }
		public TerminalNode IS_OBJECT_ID(int i) {
			return getToken(ClickHouseParser.IS_OBJECT_ID, i);
		}
		public DictionaryAttrDfntContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionaryAttrDfnt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionaryAttrDfnt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionaryAttrDfnt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionaryAttrDfnt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionaryAttrDfntContext dictionaryAttrDfnt() throws RecognitionException {
		DictionaryAttrDfntContext _localctx = new DictionaryAttrDfntContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_dictionaryAttrDfnt);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(763);
			identifier();
			setState(764);
			columnTypeExpr();
			setState(786);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(784);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
					case 1:
						{
						setState(765);
						if (!(!_localctx.attrs.contains("default"))) throw new FailedPredicateException(this, "!$attrs.contains(\"default\")");
						setState(766);
						match(DEFAULT);
						setState(767);
						literal();
						_localctx.attrs.add("default");
						}
						break;
					case 2:
						{
						setState(770);
						if (!(!_localctx.attrs.contains("expression"))) throw new FailedPredicateException(this, "!$attrs.contains(\"expression\")");
						setState(771);
						match(EXPRESSION);
						setState(772);
						columnExpr(0);
						_localctx.attrs.add("expression");
						}
						break;
					case 3:
						{
						setState(775);
						if (!(!_localctx.attrs.contains("hierarchical"))) throw new FailedPredicateException(this, "!$attrs.contains(\"hierarchical\")");
						setState(776);
						match(HIERARCHICAL);
						_localctx.attrs.add("hierarchical");
						}
						break;
					case 4:
						{
						setState(778);
						if (!(!_localctx.attrs.contains("injective"))) throw new FailedPredicateException(this, "!$attrs.contains(\"injective\")");
						setState(779);
						match(INJECTIVE);
						_localctx.attrs.add("injective");
						}
						break;
					case 5:
						{
						setState(781);
						if (!(!_localctx.attrs.contains("is_object_id"))) throw new FailedPredicateException(this, "!$attrs.contains(\"is_object_id\")");
						setState(782);
						match(IS_OBJECT_ID);
						_localctx.attrs.add("is_object_id");
						}
						break;
					}
					} 
				}
				setState(788);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionaryEngineClauseContext extends ParserRuleContext {
		public java.util.Set<String> clauses = new java.util.HashSet<String>();;
		public DictionaryPrimaryKeyClauseContext dictionaryPrimaryKeyClause() {
			return getRuleContext(DictionaryPrimaryKeyClauseContext.class,0);
		}
		public List<SourceClauseContext> sourceClause() {
			return getRuleContexts(SourceClauseContext.class);
		}
		public SourceClauseContext sourceClause(int i) {
			return getRuleContext(SourceClauseContext.class,i);
		}
		public List<LifetimeClauseContext> lifetimeClause() {
			return getRuleContexts(LifetimeClauseContext.class);
		}
		public LifetimeClauseContext lifetimeClause(int i) {
			return getRuleContext(LifetimeClauseContext.class,i);
		}
		public List<LayoutClauseContext> layoutClause() {
			return getRuleContexts(LayoutClauseContext.class);
		}
		public LayoutClauseContext layoutClause(int i) {
			return getRuleContext(LayoutClauseContext.class,i);
		}
		public List<RangeClauseContext> rangeClause() {
			return getRuleContexts(RangeClauseContext.class);
		}
		public RangeClauseContext rangeClause(int i) {
			return getRuleContext(RangeClauseContext.class,i);
		}
		public List<DictionarySettingsClauseContext> dictionarySettingsClause() {
			return getRuleContexts(DictionarySettingsClauseContext.class);
		}
		public DictionarySettingsClauseContext dictionarySettingsClause(int i) {
			return getRuleContext(DictionarySettingsClauseContext.class,i);
		}
		public DictionaryEngineClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionaryEngineClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionaryEngineClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionaryEngineClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionaryEngineClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionaryEngineClauseContext dictionaryEngineClause() throws RecognitionException {
		DictionaryEngineClauseContext _localctx = new DictionaryEngineClauseContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_dictionaryEngineClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				{
				setState(789);
				dictionaryPrimaryKeyClause();
				}
				break;
			}
			setState(814);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(812);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
					case 1:
						{
						setState(792);
						if (!(!_localctx.clauses.contains("source"))) throw new FailedPredicateException(this, "!$clauses.contains(\"source\")");
						setState(793);
						sourceClause();
						_localctx.clauses.add("source");
						}
						break;
					case 2:
						{
						setState(796);
						if (!(!_localctx.clauses.contains("lifetime"))) throw new FailedPredicateException(this, "!$clauses.contains(\"lifetime\")");
						setState(797);
						lifetimeClause();
						_localctx.clauses.add("lifetime");
						}
						break;
					case 3:
						{
						setState(800);
						if (!(!_localctx.clauses.contains("layout"))) throw new FailedPredicateException(this, "!$clauses.contains(\"layout\")");
						setState(801);
						layoutClause();
						_localctx.clauses.add("layout");
						}
						break;
					case 4:
						{
						setState(804);
						if (!(!_localctx.clauses.contains("range"))) throw new FailedPredicateException(this, "!$clauses.contains(\"range\")");
						setState(805);
						rangeClause();
						_localctx.clauses.add("range");
						}
						break;
					case 5:
						{
						setState(808);
						if (!(!_localctx.clauses.contains("settings"))) throw new FailedPredicateException(this, "!$clauses.contains(\"settings\")");
						setState(809);
						dictionarySettingsClause();
						_localctx.clauses.add("settings");
						}
						break;
					}
					} 
				}
				setState(816);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionaryPrimaryKeyClauseContext extends ParserRuleContext {
		public TerminalNode PRIMARY() { return getToken(ClickHouseParser.PRIMARY, 0); }
		public TerminalNode KEY() { return getToken(ClickHouseParser.KEY, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public DictionaryPrimaryKeyClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionaryPrimaryKeyClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionaryPrimaryKeyClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionaryPrimaryKeyClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionaryPrimaryKeyClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionaryPrimaryKeyClauseContext dictionaryPrimaryKeyClause() throws RecognitionException {
		DictionaryPrimaryKeyClauseContext _localctx = new DictionaryPrimaryKeyClauseContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_dictionaryPrimaryKeyClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817);
			match(PRIMARY);
			setState(818);
			match(KEY);
			setState(819);
			columnExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionaryArgExprContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public DictionaryArgExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionaryArgExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionaryArgExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionaryArgExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionaryArgExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionaryArgExprContext dictionaryArgExpr() throws RecognitionException {
		DictionaryArgExprContext _localctx = new DictionaryArgExprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_dictionaryArgExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			identifier();
			setState(828);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				{
				setState(822);
				identifier();
				setState(825);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAREN) {
					{
					setState(823);
					match(LPAREN);
					setState(824);
					match(RPAREN);
					}
				}

				}
				break;
			case 2:
				{
				setState(827);
				literal();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SourceClauseContext extends ParserRuleContext {
		public TerminalNode SOURCE() { return getToken(ClickHouseParser.SOURCE, 0); }
		public List<TerminalNode> LPAREN() { return getTokens(ClickHouseParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(ClickHouseParser.LPAREN, i);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TerminalNode> RPAREN() { return getTokens(ClickHouseParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(ClickHouseParser.RPAREN, i);
		}
		public List<DictionaryArgExprContext> dictionaryArgExpr() {
			return getRuleContexts(DictionaryArgExprContext.class);
		}
		public DictionaryArgExprContext dictionaryArgExpr(int i) {
			return getRuleContext(DictionaryArgExprContext.class,i);
		}
		public SourceClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSourceClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSourceClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSourceClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceClauseContext sourceClause() throws RecognitionException {
		SourceClauseContext _localctx = new SourceClauseContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_sourceClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(830);
			match(SOURCE);
			setState(831);
			match(LPAREN);
			setState(832);
			identifier();
			setState(833);
			match(LPAREN);
			setState(837);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (IDENTIFIER - 192)))) != 0)) {
				{
				{
				setState(834);
				dictionaryArgExpr();
				}
				}
				setState(839);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(840);
			match(RPAREN);
			setState(841);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LifetimeClauseContext extends ParserRuleContext {
		public TerminalNode LIFETIME() { return getToken(ClickHouseParser.LIFETIME, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> DECIMAL_LITERAL() { return getTokens(ClickHouseParser.DECIMAL_LITERAL); }
		public TerminalNode DECIMAL_LITERAL(int i) {
			return getToken(ClickHouseParser.DECIMAL_LITERAL, i);
		}
		public TerminalNode MIN() { return getToken(ClickHouseParser.MIN, 0); }
		public TerminalNode MAX() { return getToken(ClickHouseParser.MAX, 0); }
		public LifetimeClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lifetimeClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterLifetimeClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitLifetimeClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitLifetimeClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LifetimeClauseContext lifetimeClause() throws RecognitionException {
		LifetimeClauseContext _localctx = new LifetimeClauseContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_lifetimeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(843);
			match(LIFETIME);
			setState(844);
			match(LPAREN);
			setState(854);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DECIMAL_LITERAL:
				{
				setState(845);
				match(DECIMAL_LITERAL);
				}
				break;
			case MIN:
				{
				setState(846);
				match(MIN);
				setState(847);
				match(DECIMAL_LITERAL);
				setState(848);
				match(MAX);
				setState(849);
				match(DECIMAL_LITERAL);
				}
				break;
			case MAX:
				{
				setState(850);
				match(MAX);
				setState(851);
				match(DECIMAL_LITERAL);
				setState(852);
				match(MIN);
				setState(853);
				match(DECIMAL_LITERAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(856);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LayoutClauseContext extends ParserRuleContext {
		public TerminalNode LAYOUT() { return getToken(ClickHouseParser.LAYOUT, 0); }
		public List<TerminalNode> LPAREN() { return getTokens(ClickHouseParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(ClickHouseParser.LPAREN, i);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TerminalNode> RPAREN() { return getTokens(ClickHouseParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(ClickHouseParser.RPAREN, i);
		}
		public List<DictionaryArgExprContext> dictionaryArgExpr() {
			return getRuleContexts(DictionaryArgExprContext.class);
		}
		public DictionaryArgExprContext dictionaryArgExpr(int i) {
			return getRuleContext(DictionaryArgExprContext.class,i);
		}
		public LayoutClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_layoutClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterLayoutClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitLayoutClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitLayoutClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LayoutClauseContext layoutClause() throws RecognitionException {
		LayoutClauseContext _localctx = new LayoutClauseContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_layoutClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			match(LAYOUT);
			setState(859);
			match(LPAREN);
			setState(860);
			identifier();
			setState(861);
			match(LPAREN);
			setState(865);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (IDENTIFIER - 192)))) != 0)) {
				{
				{
				setState(862);
				dictionaryArgExpr();
				}
				}
				setState(867);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(868);
			match(RPAREN);
			setState(869);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeClauseContext extends ParserRuleContext {
		public TerminalNode RANGE() { return getToken(ClickHouseParser.RANGE, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TerminalNode MIN() { return getToken(ClickHouseParser.MIN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode MAX() { return getToken(ClickHouseParser.MAX, 0); }
		public RangeClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterRangeClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitRangeClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitRangeClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeClauseContext rangeClause() throws RecognitionException {
		RangeClauseContext _localctx = new RangeClauseContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(871);
			match(RANGE);
			setState(872);
			match(LPAREN);
			setState(883);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MIN:
				{
				setState(873);
				match(MIN);
				setState(874);
				identifier();
				setState(875);
				match(MAX);
				setState(876);
				identifier();
				}
				break;
			case MAX:
				{
				setState(878);
				match(MAX);
				setState(879);
				identifier();
				setState(880);
				match(MIN);
				setState(881);
				identifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(885);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionarySettingsClauseContext extends ParserRuleContext {
		public TerminalNode SETTINGS() { return getToken(ClickHouseParser.SETTINGS, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public SettingExprListContext settingExprList() {
			return getRuleContext(SettingExprListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public DictionarySettingsClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionarySettingsClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionarySettingsClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionarySettingsClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionarySettingsClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionarySettingsClauseContext dictionarySettingsClause() throws RecognitionException {
		DictionarySettingsClauseContext _localctx = new DictionarySettingsClauseContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_dictionarySettingsClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(887);
			match(SETTINGS);
			setState(888);
			match(LPAREN);
			setState(889);
			settingExprList();
			setState(890);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClusterClauseContext extends ParserRuleContext {
		public TerminalNode ON() { return getToken(ClickHouseParser.ON, 0); }
		public TerminalNode CLUSTER() { return getToken(ClickHouseParser.CLUSTER, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public ClusterClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clusterClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterClusterClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitClusterClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitClusterClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClusterClauseContext clusterClause() throws RecognitionException {
		ClusterClauseContext _localctx = new ClusterClauseContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_clusterClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(892);
			match(ON);
			setState(893);
			match(CLUSTER);
			setState(896);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ACCESS:
			case ADD:
			case AFTER:
			case ALIAS:
			case ALL:
			case ALTER:
			case AND:
			case ANTI:
			case ANY:
			case ARRAY:
			case AS:
			case ASCENDING:
			case ASOF:
			case AST:
			case ASYNC:
			case ATTACH:
			case BETWEEN:
			case BOTH:
			case BY:
			case CACHES:
			case CASE:
			case CAST:
			case CHECK:
			case CLEAR:
			case CLUSTER:
			case CLUSTERS:
			case CODEC:
			case COLLATE:
			case COLUMN:
			case COLUMNS:
			case COMMENT:
			case CONSTRAINT:
			case CREATE:
			case CROSS:
			case CUBE:
			case CURRENT:
			case CURRENT_USER:
			case CHANGED:
			case DATABASE:
			case DATABASES:
			case DATE:
			case DAY:
			case DEDUPLICATE:
			case DEFAULT:
			case DELAY:
			case DELETE:
			case DESC:
			case DESCENDING:
			case DESCRIBE:
			case DETACH:
			case DICTIONARIES:
			case DICTIONARY:
			case DISK:
			case DISTINCT:
			case DISTRIBUTED:
			case DROP:
			case ELSE:
			case ENABLED:
			case END:
			case ENGINE:
			case ENGINES:
			case ESTIMATE:
			case EVENTS:
			case EXCEPT:
			case EXISTS:
			case EXPLAIN:
			case EXPRESSION:
			case EXTENDED:
			case EXTRACT:
			case FETCHES:
			case FIELDS:
			case FILESYSTEM:
			case FILL:
			case FINAL:
			case FIRST:
			case FLUSH:
			case FOLLOWING:
			case FOR:
			case FORMAT:
			case FREEZE:
			case FROM:
			case FULL:
			case FUNCTION:
			case FUNCTIONS:
			case GLOBAL:
			case GRANULARITY:
			case GRANTS:
			case GROUP:
			case GROUPING:
			case HAVING:
			case HIERARCHICAL:
			case HOUR:
			case ID:
			case IF:
			case ILIKE:
			case IMPLICIT:
			case IN:
			case INDEX:
			case INDEXES:
			case INDICES:
			case INJECTIVE:
			case INNER:
			case INSERT:
			case INTERPOLATE:
			case INTERVAL:
			case INTO:
			case IS:
			case IS_OBJECT_ID:
			case JOIN:
			case KEY:
			case KEYS:
			case KILL:
			case LAST:
			case LAYOUT:
			case LEADING:
			case LEFT:
			case LIFETIME:
			case LIKE:
			case LIMIT:
			case LIVE:
			case LOCAL:
			case LOGS:
			case MATERIALIZE:
			case MATERIALIZED:
			case MAX:
			case MERGES:
			case MICROSECOND:
			case MILLISECOND:
			case MIN:
			case MINUTE:
			case MODIFY:
			case MONTH:
			case MOVE:
			case MUTATION:
			case NANOSECOND:
			case NO:
			case NOT:
			case NULLS:
			case OFFSET:
			case ON:
			case OPTIMIZE:
			case OR:
			case ORDER:
			case OUTER:
			case OUTFILE:
			case OVER:
			case OVERRIDE:
			case PARTITION:
			case PIPELINE:
			case PLAN:
			case POLICY:
			case POLICIES:
			case POPULATE:
			case PRECEDING:
			case PREWHERE:
			case PRIMARY:
			case PRIVILEGES:
			case PROCESSLIST:
			case PROFILE:
			case PROFILES:
			case PROJECTION:
			case QUARTER:
			case QUOTA:
			case QUOTAS:
			case RANGE:
			case RECURSIVE:
			case RELOAD:
			case REMOVE:
			case RENAME:
			case REPLACE:
			case REPLICA:
			case REPLICATED:
			case RIGHT:
			case ROLE:
			case ROLES:
			case ROLLUP:
			case ROW:
			case ROWS:
			case SAMPLE:
			case SECOND:
			case SELECT:
			case SEMI:
			case SENDS:
			case SET:
			case SETTING:
			case SETTINGS:
			case SHOW:
			case SOURCE:
			case START:
			case STOP:
			case SUBSTRING:
			case SYNC:
			case SYNTAX:
			case SYSTEM:
			case STEP:
			case TABLE:
			case TABLES:
			case TEMPORARY:
			case TEST:
			case THEN:
			case TIES:
			case TIMEOUT:
			case TIMESTAMP:
			case TO:
			case TOP:
			case TOTALS:
			case TRAILING:
			case TREE:
			case TRIM:
			case TRUNCATE:
			case TTL:
			case TYPE:
			case UNBOUNDED:
			case UNION:
			case UPDATE:
			case USE:
			case USER:
			case USERS:
			case USING:
			case UUID:
			case VALUES:
			case VIEW:
			case VOLUME:
			case WATCH:
			case WEEK:
			case WHEN:
			case WHERE:
			case WINDOW:
			case WITH:
			case YEAR:
			case JSON_FALSE:
			case JSON_TRUE:
			case IDENTIFIER:
				{
				setState(894);
				identifier();
				}
				break;
			case HEXADECIMAL_STRING_LITERAL:
			case BINARY_STRING_LITERAL:
			case STRING_LITERAL:
				{
				setState(895);
				stringLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UuidClauseContext extends ParserRuleContext {
		public TerminalNode UUID() { return getToken(ClickHouseParser.UUID, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public UuidClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uuidClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterUuidClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitUuidClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitUuidClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UuidClauseContext uuidClause() throws RecognitionException {
		UuidClauseContext _localctx = new UuidClauseContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_uuidClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(898);
			match(UUID);
			setState(899);
			stringLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DestinationClauseContext extends ParserRuleContext {
		public TerminalNode TO() { return getToken(ClickHouseParser.TO, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public DestinationClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_destinationClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDestinationClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDestinationClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDestinationClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DestinationClauseContext destinationClause() throws RecognitionException {
		DestinationClauseContext _localctx = new DestinationClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_destinationClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(901);
			match(TO);
			setState(902);
			tableIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubqueryClauseContext extends ParserRuleContext {
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public SubqueryClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subqueryClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSubqueryClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSubqueryClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSubqueryClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubqueryClauseContext subqueryClause() throws RecognitionException {
		SubqueryClauseContext _localctx = new SubqueryClauseContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_subqueryClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(904);
			match(AS);
			setState(905);
			selectUnionStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableSchemaClauseContext extends ParserRuleContext {
		public TableSchemaClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableSchemaClause; }
	 
		public TableSchemaClauseContext() { }
		public void copyFrom(TableSchemaClauseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SchemaAsTableClauseContext extends TableSchemaClauseContext {
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public SchemaAsTableClauseContext(TableSchemaClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSchemaAsTableClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSchemaAsTableClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSchemaAsTableClause(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SchemaAsFunctionClauseContext extends TableSchemaClauseContext {
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public TableFunctionExprContext tableFunctionExpr() {
			return getRuleContext(TableFunctionExprContext.class,0);
		}
		public SchemaAsFunctionClauseContext(TableSchemaClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSchemaAsFunctionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSchemaAsFunctionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSchemaAsFunctionClause(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SchemaDescriptionClauseContext extends TableSchemaClauseContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<TableElementExprContext> tableElementExpr() {
			return getRuleContexts(TableElementExprContext.class);
		}
		public TableElementExprContext tableElementExpr(int i) {
			return getRuleContext(TableElementExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public SchemaDescriptionClauseContext(TableSchemaClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSchemaDescriptionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSchemaDescriptionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSchemaDescriptionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableSchemaClauseContext tableSchemaClause() throws RecognitionException {
		TableSchemaClauseContext _localctx = new TableSchemaClauseContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_tableSchemaClause);
		int _la;
		try {
			setState(922);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				_localctx = new SchemaDescriptionClauseContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(907);
				match(LPAREN);
				setState(908);
				tableElementExpr();
				setState(913);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(909);
					match(COMMA);
					setState(910);
					tableElementExpr();
					}
					}
					setState(915);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(916);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new SchemaAsTableClauseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(918);
				match(AS);
				setState(919);
				tableIdentifier();
				}
				break;
			case 3:
				_localctx = new SchemaAsFunctionClauseContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(920);
				match(AS);
				setState(921);
				tableFunctionExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EngineClauseContext extends ParserRuleContext {
		public java.util.Set<String> clauses = new java.util.HashSet<String>();;
		public EngineExprContext engineExpr() {
			return getRuleContext(EngineExprContext.class,0);
		}
		public List<OrderByClauseContext> orderByClause() {
			return getRuleContexts(OrderByClauseContext.class);
		}
		public OrderByClauseContext orderByClause(int i) {
			return getRuleContext(OrderByClauseContext.class,i);
		}
		public List<PartitionByClauseContext> partitionByClause() {
			return getRuleContexts(PartitionByClauseContext.class);
		}
		public PartitionByClauseContext partitionByClause(int i) {
			return getRuleContext(PartitionByClauseContext.class,i);
		}
		public List<PrimaryKeyClauseContext> primaryKeyClause() {
			return getRuleContexts(PrimaryKeyClauseContext.class);
		}
		public PrimaryKeyClauseContext primaryKeyClause(int i) {
			return getRuleContext(PrimaryKeyClauseContext.class,i);
		}
		public List<SampleByClauseContext> sampleByClause() {
			return getRuleContexts(SampleByClauseContext.class);
		}
		public SampleByClauseContext sampleByClause(int i) {
			return getRuleContext(SampleByClauseContext.class,i);
		}
		public List<TtlClauseContext> ttlClause() {
			return getRuleContexts(TtlClauseContext.class);
		}
		public TtlClauseContext ttlClause(int i) {
			return getRuleContext(TtlClauseContext.class,i);
		}
		public List<SettingsClauseContext> settingsClause() {
			return getRuleContexts(SettingsClauseContext.class);
		}
		public SettingsClauseContext settingsClause(int i) {
			return getRuleContext(SettingsClauseContext.class,i);
		}
		public List<CommentClauseContext> commentClause() {
			return getRuleContexts(CommentClauseContext.class);
		}
		public CommentClauseContext commentClause(int i) {
			return getRuleContext(CommentClauseContext.class,i);
		}
		public EngineClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_engineClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterEngineClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitEngineClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitEngineClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EngineClauseContext engineClause() throws RecognitionException {
		EngineClauseContext _localctx = new EngineClauseContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_engineClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(924);
			engineExpr();
			setState(955);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(953);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
					case 1:
						{
						setState(925);
						if (!(!_localctx.clauses.contains("orderByClause"))) throw new FailedPredicateException(this, "!$clauses.contains(\"orderByClause\")");
						setState(926);
						orderByClause();
						_localctx.clauses.add("orderByClause");
						}
						break;
					case 2:
						{
						setState(929);
						if (!(!_localctx.clauses.contains("partitionByClause"))) throw new FailedPredicateException(this, "!$clauses.contains(\"partitionByClause\")");
						setState(930);
						partitionByClause();
						_localctx.clauses.add("partitionByClause");
						}
						break;
					case 3:
						{
						setState(933);
						if (!(!_localctx.clauses.contains("primaryKeyClause"))) throw new FailedPredicateException(this, "!$clauses.contains(\"primaryKeyClause\")");
						setState(934);
						primaryKeyClause();
						_localctx.clauses.add("primaryKeyClause");
						}
						break;
					case 4:
						{
						setState(937);
						if (!(!_localctx.clauses.contains("sampleByClause"))) throw new FailedPredicateException(this, "!$clauses.contains(\"sampleByClause\")");
						setState(938);
						sampleByClause();
						_localctx.clauses.add("sampleByClause");
						}
						break;
					case 5:
						{
						setState(941);
						if (!(!_localctx.clauses.contains("ttlClause"))) throw new FailedPredicateException(this, "!$clauses.contains(\"ttlClause\")");
						setState(942);
						ttlClause();
						_localctx.clauses.add("ttlClause");
						}
						break;
					case 6:
						{
						setState(945);
						if (!(!_localctx.clauses.contains("settingsClause"))) throw new FailedPredicateException(this, "!$clauses.contains(\"settingsClause\")");
						setState(946);
						settingsClause();
						_localctx.clauses.add("settingsClause");
						}
						break;
					case 7:
						{
						setState(949);
						if (!(!_localctx.clauses.contains("comment"))) throw new FailedPredicateException(this, "!$clauses.contains(\"comment\")");
						setState(950);
						commentClause();
						_localctx.clauses.add("comment");
						}
						break;
					}
					} 
				}
				setState(957);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartitionByClauseContext extends ParserRuleContext {
		public TerminalNode PARTITION() { return getToken(ClickHouseParser.PARTITION, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public PartitionByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partitionByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterPartitionByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitPartitionByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitPartitionByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PartitionByClauseContext partitionByClause() throws RecognitionException {
		PartitionByClauseContext _localctx = new PartitionByClauseContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_partitionByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(958);
			match(PARTITION);
			setState(959);
			match(BY);
			setState(960);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryKeyClauseContext extends ParserRuleContext {
		public TerminalNode PRIMARY() { return getToken(ClickHouseParser.PRIMARY, 0); }
		public TerminalNode KEY() { return getToken(ClickHouseParser.KEY, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public PrimaryKeyClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryKeyClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterPrimaryKeyClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitPrimaryKeyClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitPrimaryKeyClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryKeyClauseContext primaryKeyClause() throws RecognitionException {
		PrimaryKeyClauseContext _localctx = new PrimaryKeyClauseContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_primaryKeyClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			match(PRIMARY);
			setState(963);
			match(KEY);
			setState(964);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SampleByClauseContext extends ParserRuleContext {
		public TerminalNode SAMPLE() { return getToken(ClickHouseParser.SAMPLE, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public SampleByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sampleByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSampleByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSampleByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSampleByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SampleByClauseContext sampleByClause() throws RecognitionException {
		SampleByClauseContext _localctx = new SampleByClauseContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_sampleByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
			match(SAMPLE);
			setState(967);
			match(BY);
			setState(968);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TtlClauseContext extends ParserRuleContext {
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public List<TtlExprContext> ttlExpr() {
			return getRuleContexts(TtlExprContext.class);
		}
		public TtlExprContext ttlExpr(int i) {
			return getRuleContext(TtlExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public TtlClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ttlClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTtlClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTtlClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTtlClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TtlClauseContext ttlClause() throws RecognitionException {
		TtlClauseContext _localctx = new TtlClauseContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_ttlClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
			match(TTL);
			setState(971);
			ttlExpr();
			setState(976);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,103,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(972);
					match(COMMA);
					setState(973);
					ttlExpr();
					}
					} 
				}
				setState(978);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,103,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentClauseContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(ClickHouseParser.COMMENT, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(ClickHouseParser.STRING_LITERAL, 0); }
		public CommentClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCommentClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCommentClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCommentClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentClauseContext commentClause() throws RecognitionException {
		CommentClauseContext _localctx = new CommentClauseContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_commentClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(979);
			match(COMMENT);
			setState(980);
			match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EngineExprContext extends ParserRuleContext {
		public TerminalNode ENGINE() { return getToken(ClickHouseParser.ENGINE, 0); }
		public IdentifierOrNullContext identifierOrNull() {
			return getRuleContext(IdentifierOrNullContext.class,0);
		}
		public TerminalNode EQ_SINGLE() { return getToken(ClickHouseParser.EQ_SINGLE, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public EngineExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_engineExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterEngineExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitEngineExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitEngineExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EngineExprContext engineExpr() throws RecognitionException {
		EngineExprContext _localctx = new EngineExprContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_engineExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(982);
			match(ENGINE);
			setState(984);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQ_SINGLE) {
				{
				setState(983);
				match(EQ_SINGLE);
				}
			}

			setState(986);
			identifierOrNull();
			setState(992);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(987);
				match(LPAREN);
				setState(989);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(988);
					columnExprList();
					}
				}

				setState(991);
				match(RPAREN);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableElementExprContext extends ParserRuleContext {
		public TableElementExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableElementExpr; }
	 
		public TableElementExprContext() { }
		public void copyFrom(TableElementExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TableElementExprProjectionContext extends TableElementExprContext {
		public TerminalNode PROJECTION() { return getToken(ClickHouseParser.PROJECTION, 0); }
		public TableProjectionDfntContext tableProjectionDfnt() {
			return getRuleContext(TableProjectionDfntContext.class,0);
		}
		public TableElementExprProjectionContext(TableElementExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableElementExprProjection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableElementExprProjection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableElementExprProjection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableElementExprConstraintContext extends TableElementExprContext {
		public TerminalNode CONSTRAINT() { return getToken(ClickHouseParser.CONSTRAINT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode CHECK() { return getToken(ClickHouseParser.CHECK, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TableElementExprConstraintContext(TableElementExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableElementExprConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableElementExprConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableElementExprConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableElementExprPriContext extends TableElementExprContext {
		public TerminalNode PRIMARY() { return getToken(ClickHouseParser.PRIMARY, 0); }
		public TerminalNode KEY() { return getToken(ClickHouseParser.KEY, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TableElementExprPriContext(TableElementExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableElementExprPri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableElementExprPri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableElementExprPri(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableElementExprColumnContext extends TableElementExprContext {
		public TableColumnDfntContext tableColumnDfnt() {
			return getRuleContext(TableColumnDfntContext.class,0);
		}
		public TableElementExprColumnContext(TableElementExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableElementExprColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableElementExprColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableElementExprColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableElementExprIndexContext extends TableElementExprContext {
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public TableIndexDfntContext tableIndexDfnt() {
			return getRuleContext(TableIndexDfntContext.class,0);
		}
		public TableElementExprIndexContext(TableElementExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableElementExprIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableElementExprIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableElementExprIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableElementExprContext tableElementExpr() throws RecognitionException {
		TableElementExprContext _localctx = new TableElementExprContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_tableElementExpr);
		try {
			setState(1007);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				_localctx = new TableElementExprPriContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				match(PRIMARY);
				setState(995);
				match(KEY);
				setState(996);
				columnExpr(0);
				}
				break;
			case 2:
				_localctx = new TableElementExprColumnContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(997);
				tableColumnDfnt();
				}
				break;
			case 3:
				_localctx = new TableElementExprConstraintContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(998);
				match(CONSTRAINT);
				setState(999);
				identifier();
				setState(1000);
				match(CHECK);
				setState(1001);
				columnExpr(0);
				}
				break;
			case 4:
				_localctx = new TableElementExprIndexContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1003);
				match(INDEX);
				setState(1004);
				tableIndexDfnt();
				}
				break;
			case 5:
				_localctx = new TableElementExprProjectionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1005);
				match(PROJECTION);
				setState(1006);
				tableProjectionDfnt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableColumnDfntContext extends ParserRuleContext {
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode NULLABLE() { return getToken(ClickHouseParser.NULLABLE, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TableColumnPropertyExprContext tableColumnPropertyExpr() {
			return getRuleContext(TableColumnPropertyExprContext.class,0);
		}
		public TerminalNode COMMENT() { return getToken(ClickHouseParser.COMMENT, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public CodecExprContext codecExpr() {
			return getRuleContext(CodecExprContext.class,0);
		}
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TableColumnDfntContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableColumnDfnt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableColumnDfnt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableColumnDfnt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableColumnDfnt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableColumnDfntContext tableColumnDfnt() throws RecognitionException {
		TableColumnDfntContext _localctx = new TableColumnDfntContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_tableColumnDfnt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1009);
			nestedIdentifier();
			setState(1016);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULLABLE:
				{
				setState(1010);
				match(NULLABLE);
				setState(1011);
				match(LPAREN);
				setState(1012);
				columnTypeExpr();
				setState(1013);
				match(RPAREN);
				}
				break;
			case ACCESS:
			case ADD:
			case AFTER:
			case ALIAS:
			case ALL:
			case ALTER:
			case AND:
			case ANTI:
			case ANY:
			case ARRAY:
			case AS:
			case ASCENDING:
			case ASOF:
			case AST:
			case ASYNC:
			case ATTACH:
			case BETWEEN:
			case BOTH:
			case BY:
			case CACHES:
			case CASE:
			case CAST:
			case CHECK:
			case CLEAR:
			case CLUSTER:
			case CLUSTERS:
			case CODEC:
			case COLLATE:
			case COLUMN:
			case COLUMNS:
			case COMMENT:
			case CONSTRAINT:
			case CREATE:
			case CROSS:
			case CUBE:
			case CURRENT:
			case CURRENT_USER:
			case CHANGED:
			case DATABASE:
			case DATABASES:
			case DATE:
			case DAY:
			case DEDUPLICATE:
			case DEFAULT:
			case DELAY:
			case DELETE:
			case DESC:
			case DESCENDING:
			case DESCRIBE:
			case DETACH:
			case DICTIONARIES:
			case DICTIONARY:
			case DISK:
			case DISTINCT:
			case DISTRIBUTED:
			case DROP:
			case ELSE:
			case ENABLED:
			case END:
			case ENGINE:
			case ENGINES:
			case ESTIMATE:
			case EVENTS:
			case EXCEPT:
			case EXISTS:
			case EXPLAIN:
			case EXPRESSION:
			case EXTENDED:
			case EXTRACT:
			case FETCHES:
			case FIELDS:
			case FILESYSTEM:
			case FILL:
			case FINAL:
			case FIRST:
			case FLUSH:
			case FOLLOWING:
			case FOR:
			case FORMAT:
			case FREEZE:
			case FROM:
			case FULL:
			case FUNCTION:
			case FUNCTIONS:
			case GLOBAL:
			case GRANULARITY:
			case GRANTS:
			case GROUP:
			case GROUPING:
			case HAVING:
			case HIERARCHICAL:
			case HOUR:
			case ID:
			case IF:
			case ILIKE:
			case IMPLICIT:
			case IN:
			case INDEX:
			case INDEXES:
			case INDICES:
			case INJECTIVE:
			case INNER:
			case INSERT:
			case INTERPOLATE:
			case INTERVAL:
			case INTO:
			case IS:
			case IS_OBJECT_ID:
			case JOIN:
			case KEY:
			case KEYS:
			case KILL:
			case LAST:
			case LAYOUT:
			case LEADING:
			case LEFT:
			case LIFETIME:
			case LIKE:
			case LIMIT:
			case LIVE:
			case LOCAL:
			case LOGS:
			case MATERIALIZE:
			case MATERIALIZED:
			case MAX:
			case MERGES:
			case MICROSECOND:
			case MILLISECOND:
			case MIN:
			case MINUTE:
			case MODIFY:
			case MONTH:
			case MOVE:
			case MUTATION:
			case NANOSECOND:
			case NO:
			case NOT:
			case NULLS:
			case OFFSET:
			case ON:
			case OPTIMIZE:
			case OR:
			case ORDER:
			case OUTER:
			case OUTFILE:
			case OVER:
			case OVERRIDE:
			case PARTITION:
			case PIPELINE:
			case PLAN:
			case POLICY:
			case POLICIES:
			case POPULATE:
			case PRECEDING:
			case PREWHERE:
			case PRIMARY:
			case PRIVILEGES:
			case PROCESSLIST:
			case PROFILE:
			case PROFILES:
			case PROJECTION:
			case QUARTER:
			case QUOTA:
			case QUOTAS:
			case RANGE:
			case RECURSIVE:
			case RELOAD:
			case REMOVE:
			case RENAME:
			case REPLACE:
			case REPLICA:
			case REPLICATED:
			case RIGHT:
			case ROLE:
			case ROLES:
			case ROLLUP:
			case ROW:
			case ROWS:
			case SAMPLE:
			case SECOND:
			case SELECT:
			case SEMI:
			case SENDS:
			case SET:
			case SETTING:
			case SETTINGS:
			case SHOW:
			case SOURCE:
			case START:
			case STOP:
			case SUBSTRING:
			case SYNC:
			case SYNTAX:
			case SYSTEM:
			case STEP:
			case TABLE:
			case TABLES:
			case TEMPORARY:
			case TEST:
			case THEN:
			case TIES:
			case TIMEOUT:
			case TIMESTAMP:
			case TO:
			case TOP:
			case TOTALS:
			case TRAILING:
			case TREE:
			case TRIM:
			case TRUNCATE:
			case TTL:
			case TYPE:
			case UNBOUNDED:
			case UNION:
			case UPDATE:
			case USE:
			case USER:
			case USERS:
			case USING:
			case UUID:
			case VALUES:
			case VIEW:
			case VOLUME:
			case WATCH:
			case WEEK:
			case WHEN:
			case WHERE:
			case WINDOW:
			case WITH:
			case YEAR:
			case JSON_FALSE:
			case JSON_TRUE:
			case IDENTIFIER:
				{
				setState(1015);
				columnTypeExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1019);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS || _la==DEFAULT || _la==MATERIALIZED) {
				{
				setState(1018);
				tableColumnPropertyExpr();
				}
			}

			setState(1023);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(1021);
				match(COMMENT);
				setState(1022);
				stringLiteral();
				}
			}

			setState(1026);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CODEC) {
				{
				setState(1025);
				codecExpr();
				}
			}

			setState(1030);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TTL) {
				{
				setState(1028);
				match(TTL);
				setState(1029);
				columnExpr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableColumnPropertyExprContext extends ParserRuleContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(ClickHouseParser.DEFAULT, 0); }
		public TerminalNode MATERIALIZED() { return getToken(ClickHouseParser.MATERIALIZED, 0); }
		public TerminalNode ALIAS() { return getToken(ClickHouseParser.ALIAS, 0); }
		public TableColumnPropertyExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableColumnPropertyExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableColumnPropertyExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableColumnPropertyExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableColumnPropertyExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableColumnPropertyExprContext tableColumnPropertyExpr() throws RecognitionException {
		TableColumnPropertyExprContext _localctx = new TableColumnPropertyExprContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_tableColumnPropertyExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1032);
			_la = _input.LA(1);
			if ( !(_la==ALIAS || _la==DEFAULT || _la==MATERIALIZED) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1033);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableIndexDfntContext extends ParserRuleContext {
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode TYPE() { return getToken(ClickHouseParser.TYPE, 0); }
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public TerminalNode GRANULARITY() { return getToken(ClickHouseParser.GRANULARITY, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public TableIndexDfntContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableIndexDfnt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableIndexDfnt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableIndexDfnt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableIndexDfnt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableIndexDfntContext tableIndexDfnt() throws RecognitionException {
		TableIndexDfntContext _localctx = new TableIndexDfntContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_tableIndexDfnt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1035);
			nestedIdentifier();
			setState(1036);
			columnExpr(0);
			setState(1037);
			match(TYPE);
			setState(1038);
			columnTypeExpr();
			setState(1039);
			match(GRANULARITY);
			setState(1040);
			match(DECIMAL_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableProjectionDfntContext extends ParserRuleContext {
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public ProjectionSelectStmtContext projectionSelectStmt() {
			return getRuleContext(ProjectionSelectStmtContext.class,0);
		}
		public TableProjectionDfntContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableProjectionDfnt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableProjectionDfnt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableProjectionDfnt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableProjectionDfnt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableProjectionDfntContext tableProjectionDfnt() throws RecognitionException {
		TableProjectionDfntContext _localctx = new TableProjectionDfntContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_tableProjectionDfnt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1042);
			nestedIdentifier();
			setState(1043);
			projectionSelectStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodecExprContext extends ParserRuleContext {
		public TerminalNode CODEC() { return getToken(ClickHouseParser.CODEC, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<CodecArgExprContext> codecArgExpr() {
			return getRuleContexts(CodecArgExprContext.class);
		}
		public CodecArgExprContext codecArgExpr(int i) {
			return getRuleContext(CodecArgExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public CodecExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codecExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCodecExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCodecExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCodecExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodecExprContext codecExpr() throws RecognitionException {
		CodecExprContext _localctx = new CodecExprContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_codecExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			match(CODEC);
			setState(1046);
			match(LPAREN);
			setState(1047);
			codecArgExpr();
			setState(1052);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1048);
				match(COMMA);
				setState(1049);
				codecArgExpr();
				}
				}
				setState(1054);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1055);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodecArgExprContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public CodecArgExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codecArgExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterCodecArgExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitCodecArgExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitCodecArgExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodecArgExprContext codecArgExpr() throws RecognitionException {
		CodecArgExprContext _localctx = new CodecArgExprContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_codecArgExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
			identifier();
			setState(1063);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(1058);
				match(LPAREN);
				setState(1060);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(1059);
					columnExprList();
					}
				}

				setState(1062);
				match(RPAREN);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TtlExprContext extends ParserRuleContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode DELETE() { return getToken(ClickHouseParser.DELETE, 0); }
		public TerminalNode TO() { return getToken(ClickHouseParser.TO, 0); }
		public TerminalNode DISK() { return getToken(ClickHouseParser.DISK, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode VOLUME() { return getToken(ClickHouseParser.VOLUME, 0); }
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public GroupByClauseContext groupByClause() {
			return getRuleContext(GroupByClauseContext.class,0);
		}
		public TerminalNode SET() { return getToken(ClickHouseParser.SET, 0); }
		public List<TtlSetExprContext> ttlSetExpr() {
			return getRuleContexts(TtlSetExprContext.class);
		}
		public TtlSetExprContext ttlSetExpr(int i) {
			return getRuleContext(TtlSetExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public TtlExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ttlExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTtlExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTtlExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTtlExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TtlExprContext ttlExpr() throws RecognitionException {
		TtlExprContext _localctx = new TtlExprContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_ttlExpr);
		try {
			int _alt;
			setState(1089);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1065);
				columnExpr(0);
				setState(1076);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
				case 1:
					{
					setState(1066);
					match(DELETE);
					setState(1068);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
					case 1:
						{
						setState(1067);
						whereClause();
						}
						break;
					}
					}
					break;
				case 2:
					{
					setState(1070);
					match(TO);
					setState(1071);
					match(DISK);
					setState(1072);
					stringLiteral();
					}
					break;
				case 3:
					{
					setState(1073);
					match(TO);
					setState(1074);
					match(VOLUME);
					setState(1075);
					stringLiteral();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1078);
				columnExpr(0);
				setState(1079);
				groupByClause();
				setState(1080);
				match(SET);
				setState(1081);
				ttlSetExpr();
				setState(1086);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,118,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1082);
						match(COMMA);
						setState(1083);
						ttlSetExpr();
						}
						} 
					}
					setState(1088);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,118,_ctx);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TtlSetExprContext extends ParserRuleContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode EQ_SINGLE() { return getToken(ClickHouseParser.EQ_SINGLE, 0); }
		public TtlSetExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ttlSetExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTtlSetExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTtlSetExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTtlSetExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TtlSetExprContext ttlSetExpr() throws RecognitionException {
		TtlSetExprContext _localctx = new TtlSetExprContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_ttlSetExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1091);
			columnExpr(0);
			setState(1092);
			match(EQ_SINGLE);
			setState(1093);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DescribeStmtContext extends ParserRuleContext {
		public TableExprContext tableExpr() {
			return getRuleContext(TableExprContext.class,0);
		}
		public TerminalNode DESCRIBE() { return getToken(ClickHouseParser.DESCRIBE, 0); }
		public TerminalNode DESC() { return getToken(ClickHouseParser.DESC, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public DescribeStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_describeStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDescribeStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDescribeStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDescribeStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescribeStmtContext describeStmt() throws RecognitionException {
		DescribeStmtContext _localctx = new DescribeStmtContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_describeStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
			_la = _input.LA(1);
			if ( !(_la==DESC || _la==DESCRIBE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1097);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1096);
				match(TABLE);
				}
				break;
			}
			setState(1099);
			tableExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DropStmtContext extends ParserRuleContext {
		public DropStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dropStmt; }
	 
		public DropStmtContext() { }
		public void copyFrom(DropStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DropDatabaseStmtContext extends DropStmtContext {
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public TerminalNode DETACH() { return getToken(ClickHouseParser.DETACH, 0); }
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public DropDatabaseStmtContext(DropStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDropDatabaseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDropDatabaseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDropDatabaseStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropTableStmtContext extends DropStmtContext {
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DETACH() { return getToken(ClickHouseParser.DETACH, 0); }
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TerminalNode NO() { return getToken(ClickHouseParser.NO, 0); }
		public TerminalNode DELAY() { return getToken(ClickHouseParser.DELAY, 0); }
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public DropTableStmtContext(DropStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDropTableStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDropTableStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDropTableStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DropStmtContext dropStmt() throws RecognitionException {
		DropStmtContext _localctx = new DropStmtContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_dropStmt);
		int _la;
		try {
			setState(1132);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				_localctx = new DropDatabaseStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1101);
				_la = _input.LA(1);
				if ( !(_la==DETACH || _la==DROP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1102);
				match(DATABASE);
				setState(1105);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
				case 1:
					{
					setState(1103);
					match(IF);
					setState(1104);
					match(EXISTS);
					}
					break;
				}
				setState(1107);
				databaseIdentifier();
				setState(1109);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(1108);
					clusterClause();
					}
				}

				}
				break;
			case 2:
				_localctx = new DropTableStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1111);
				_la = _input.LA(1);
				if ( !(_la==DETACH || _la==DROP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1118);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DICTIONARY:
					{
					setState(1112);
					match(DICTIONARY);
					}
					break;
				case TABLE:
				case TEMPORARY:
					{
					setState(1114);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==TEMPORARY) {
						{
						setState(1113);
						match(TEMPORARY);
						}
					}

					setState(1116);
					match(TABLE);
					}
					break;
				case VIEW:
					{
					setState(1117);
					match(VIEW);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1122);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
				case 1:
					{
					setState(1120);
					match(IF);
					setState(1121);
					match(EXISTS);
					}
					break;
				}
				setState(1124);
				tableIdentifier();
				setState(1126);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(1125);
					clusterClause();
					}
				}

				setState(1130);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NO) {
					{
					setState(1128);
					match(NO);
					setState(1129);
					match(DELAY);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExistsStmtContext extends ParserRuleContext {
		public ExistsStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existsStmt; }
	 
		public ExistsStmtContext() { }
		public void copyFrom(ExistsStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExistsTableStmtContext extends ExistsStmtContext {
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public ExistsTableStmtContext(ExistsStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterExistsTableStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitExistsTableStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitExistsTableStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExistsDatabaseStmtContext extends ExistsStmtContext {
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public ExistsDatabaseStmtContext(ExistsStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterExistsDatabaseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitExistsDatabaseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitExistsDatabaseStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistsStmtContext existsStmt() throws RecognitionException {
		ExistsStmtContext _localctx = new ExistsStmtContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_existsStmt);
		int _la;
		try {
			setState(1147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				_localctx = new ExistsDatabaseStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1134);
				match(EXISTS);
				setState(1135);
				match(DATABASE);
				setState(1136);
				databaseIdentifier();
				}
				break;
			case 2:
				_localctx = new ExistsTableStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1137);
				match(EXISTS);
				setState(1144);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
				case 1:
					{
					setState(1138);
					match(DICTIONARY);
					}
					break;
				case 2:
					{
					setState(1140);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==TEMPORARY) {
						{
						setState(1139);
						match(TEMPORARY);
						}
					}

					setState(1142);
					match(TABLE);
					}
					break;
				case 3:
					{
					setState(1143);
					match(VIEW);
					}
					break;
				}
				setState(1146);
				tableIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplainStmtContext extends ParserRuleContext {
		public TerminalNode EXPLAIN() { return getToken(ClickHouseParser.EXPLAIN, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public TerminalNode AST() { return getToken(ClickHouseParser.AST, 0); }
		public TerminalNode SYNTAX() { return getToken(ClickHouseParser.SYNTAX, 0); }
		public TerminalNode QUERY() { return getToken(ClickHouseParser.QUERY, 0); }
		public TerminalNode TREE() { return getToken(ClickHouseParser.TREE, 0); }
		public TerminalNode PLAN() { return getToken(ClickHouseParser.PLAN, 0); }
		public TerminalNode PIPELINE() { return getToken(ClickHouseParser.PIPELINE, 0); }
		public TerminalNode ESTIMATE() { return getToken(ClickHouseParser.ESTIMATE, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TerminalNode OVERRIDE() { return getToken(ClickHouseParser.OVERRIDE, 0); }
		public SettingExprListContext settingExprList() {
			return getRuleContext(SettingExprListContext.class,0);
		}
		public ExplainStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explainStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterExplainStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitExplainStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitExplainStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplainStmtContext explainStmt() throws RecognitionException {
		ExplainStmtContext _localctx = new ExplainStmtContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_explainStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			match(EXPLAIN);
			setState(1159);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1150);
				match(AST);
				}
				break;
			case 2:
				{
				setState(1151);
				match(SYNTAX);
				}
				break;
			case 3:
				{
				setState(1152);
				match(QUERY);
				setState(1153);
				match(TREE);
				}
				break;
			case 4:
				{
				setState(1154);
				match(PLAN);
				}
				break;
			case 5:
				{
				setState(1155);
				match(PIPELINE);
				}
				break;
			case 6:
				{
				setState(1156);
				match(ESTIMATE);
				}
				break;
			case 7:
				{
				setState(1157);
				match(TABLE);
				setState(1158);
				match(OVERRIDE);
				}
				break;
			}
			setState(1162);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1161);
				settingExprList();
				}
				break;
			}
			setState(1164);
			selectUnionStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InsertStmtContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(ClickHouseParser.INSERT, 0); }
		public TerminalNode INTO() { return getToken(ClickHouseParser.INTO, 0); }
		public DataClauseContext dataClause() {
			return getRuleContext(DataClauseContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode FUNCTION() { return getToken(ClickHouseParser.FUNCTION, 0); }
		public TableFunctionExprContext tableFunctionExpr() {
			return getRuleContext(TableFunctionExprContext.class,0);
		}
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public ColumnsClauseContext columnsClause() {
			return getRuleContext(ColumnsClauseContext.class,0);
		}
		public InsertStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterInsertStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitInsertStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitInsertStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertStmtContext insertStmt() throws RecognitionException {
		InsertStmtContext _localctx = new InsertStmtContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_insertStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1166);
			match(INSERT);
			setState(1167);
			match(INTO);
			setState(1169);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1168);
				match(TABLE);
				}
				break;
			}
			setState(1174);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				{
				setState(1171);
				tableIdentifier();
				}
				break;
			case 2:
				{
				setState(1172);
				match(FUNCTION);
				setState(1173);
				tableFunctionExpr();
				}
				break;
			}
			setState(1177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1176);
				columnsClause();
				}
				break;
			}
			setState(1179);
			dataClause();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnsClauseContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<NestedIdentifierContext> nestedIdentifier() {
			return getRuleContexts(NestedIdentifierContext.class);
		}
		public NestedIdentifierContext nestedIdentifier(int i) {
			return getRuleContext(NestedIdentifierContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnsClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnsClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnsClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnsClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnsClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnsClauseContext columnsClause() throws RecognitionException {
		ColumnsClauseContext _localctx = new ColumnsClauseContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_columnsClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1181);
			match(LPAREN);
			setState(1182);
			nestedIdentifier();
			setState(1187);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1183);
				match(COMMA);
				setState(1184);
				nestedIdentifier();
				}
				}
				setState(1189);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1190);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataClauseContext extends ParserRuleContext {
		public DataClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataClause; }
	 
		public DataClauseContext() { }
		public void copyFrom(DataClauseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DataClauseValuesContext extends DataClauseContext {
		public TerminalNode VALUES() { return getToken(ClickHouseParser.VALUES, 0); }
		public List<AssignmentValuesContext> assignmentValues() {
			return getRuleContexts(AssignmentValuesContext.class);
		}
		public AssignmentValuesContext assignmentValues(int i) {
			return getRuleContext(AssignmentValuesContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public DataClauseValuesContext(DataClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDataClauseValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDataClauseValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDataClauseValues(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DataClauseFormatContext extends DataClauseContext {
		public TerminalNode FORMAT() { return getToken(ClickHouseParser.FORMAT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DataClauseFormatContext(DataClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDataClauseFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDataClauseFormat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDataClauseFormat(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DataClauseSelectContext extends DataClauseContext {
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public DataClauseSelectContext(DataClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDataClauseSelect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDataClauseSelect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDataClauseSelect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataClauseContext dataClause() throws RecognitionException {
		DataClauseContext _localctx = new DataClauseContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_dataClause);
		int _la;
		try {
			setState(1204);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORMAT:
				_localctx = new DataClauseFormatContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1192);
				match(FORMAT);
				setState(1193);
				identifier();
				}
				break;
			case VALUES:
				_localctx = new DataClauseValuesContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1194);
				match(VALUES);
				setState(1195);
				assignmentValues();
				setState(1200);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1196);
					match(COMMA);
					setState(1197);
					assignmentValues();
					}
					}
					setState(1202);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case SELECT:
			case WITH:
			case LPAREN:
				_localctx = new DataClauseSelectContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1203);
				selectUnionStmt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentValuesContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<AssignmentValueContext> assignmentValue() {
			return getRuleContexts(AssignmentValueContext.class);
		}
		public AssignmentValueContext assignmentValue(int i) {
			return getRuleContext(AssignmentValueContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public AssignmentValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAssignmentValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAssignmentValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAssignmentValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentValuesContext assignmentValues() throws RecognitionException {
		AssignmentValuesContext _localctx = new AssignmentValuesContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_assignmentValues);
		int _la;
		try {
			setState(1219);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1206);
				match(LPAREN);
				setState(1207);
				assignmentValue();
				setState(1212);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1208);
					match(COMMA);
					setState(1209);
					assignmentValue();
					}
					}
					setState(1214);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1215);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1217);
				match(LPAREN);
				setState(1218);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentValueContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public AssignmentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAssignmentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAssignmentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAssignmentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentValueContext assignmentValue() throws RecognitionException {
		AssignmentValueContext _localctx = new AssignmentValueContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_assignmentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1221);
			literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeleteStmtContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(ClickHouseParser.DELETE, 0); }
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public InPartitionClauseContext inPartitionClause() {
			return getRuleContext(InPartitionClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public DeleteStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDeleteStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDeleteStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDeleteStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteStmtContext deleteStmt() throws RecognitionException {
		DeleteStmtContext _localctx = new DeleteStmtContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_deleteStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1223);
			match(DELETE);
			setState(1224);
			match(FROM);
			setState(1225);
			nestedIdentifier();
			setState(1227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(1226);
				clusterClause();
				}
			}

			setState(1230);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IN) {
				{
				setState(1229);
				inPartitionClause();
				}
			}

			setState(1233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(1232);
				whereClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InPartitionClauseContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public TerminalNode PARTITION() { return getToken(ClickHouseParser.PARTITION, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public InPartitionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inPartitionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterInPartitionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitInPartitionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitInPartitionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InPartitionClauseContext inPartitionClause() throws RecognitionException {
		InPartitionClauseContext _localctx = new InPartitionClauseContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_inPartitionClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			match(IN);
			setState(1236);
			match(PARTITION);
			setState(1237);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UpdateStmtContext extends ParserRuleContext {
		public TerminalNode UPDATE() { return getToken(ClickHouseParser.UPDATE, 0); }
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TerminalNode SET() { return getToken(ClickHouseParser.SET, 0); }
		public AssignmentExprListContext assignmentExprList() {
			return getRuleContext(AssignmentExprListContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public InPartitionClauseContext inPartitionClause() {
			return getRuleContext(InPartitionClauseContext.class,0);
		}
		public UpdateStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterUpdateStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitUpdateStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitUpdateStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateStmtContext updateStmt() throws RecognitionException {
		UpdateStmtContext _localctx = new UpdateStmtContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_updateStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1239);
			match(UPDATE);
			setState(1240);
			nestedIdentifier();
			setState(1241);
			match(SET);
			setState(1242);
			assignmentExprList();
			setState(1244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(1243);
				clusterClause();
				}
			}

			setState(1247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IN) {
				{
				setState(1246);
				inPartitionClause();
				}
			}

			setState(1249);
			whereClause();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KillStmtContext extends ParserRuleContext {
		public KillStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_killStmt; }
	 
		public KillStmtContext() { }
		public void copyFrom(KillStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class KillMutationStmtContext extends KillStmtContext {
		public TerminalNode KILL() { return getToken(ClickHouseParser.KILL, 0); }
		public TerminalNode MUTATION() { return getToken(ClickHouseParser.MUTATION, 0); }
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TerminalNode SYNC() { return getToken(ClickHouseParser.SYNC, 0); }
		public TerminalNode ASYNC() { return getToken(ClickHouseParser.ASYNC, 0); }
		public TerminalNode TEST() { return getToken(ClickHouseParser.TEST, 0); }
		public KillMutationStmtContext(KillStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterKillMutationStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitKillMutationStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitKillMutationStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KillStmtContext killStmt() throws RecognitionException {
		KillStmtContext _localctx = new KillStmtContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_killStmt);
		int _la;
		try {
			_localctx = new KillMutationStmtContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			match(KILL);
			setState(1252);
			match(MUTATION);
			setState(1254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(1253);
				clusterClause();
				}
			}

			setState(1256);
			whereClause();
			setState(1258);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASYNC || _la==SYNC || _la==TEST) {
				{
				setState(1257);
				_la = _input.LA(1);
				if ( !(_la==ASYNC || _la==SYNC || _la==TEST) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OptimizeStmtContext extends ParserRuleContext {
		public TerminalNode OPTIMIZE() { return getToken(ClickHouseParser.OPTIMIZE, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public PartitionClauseContext partitionClause() {
			return getRuleContext(PartitionClauseContext.class,0);
		}
		public TerminalNode FINAL() { return getToken(ClickHouseParser.FINAL, 0); }
		public TerminalNode DEDUPLICATE() { return getToken(ClickHouseParser.DEDUPLICATE, 0); }
		public OptimizeStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optimizeStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterOptimizeStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitOptimizeStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitOptimizeStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptimizeStmtContext optimizeStmt() throws RecognitionException {
		OptimizeStmtContext _localctx = new OptimizeStmtContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_optimizeStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1260);
			match(OPTIMIZE);
			setState(1261);
			match(TABLE);
			setState(1262);
			tableIdentifier();
			setState(1264);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(1263);
				clusterClause();
				}
			}

			setState(1267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PARTITION) {
				{
				setState(1266);
				partitionClause();
				}
			}

			setState(1270);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FINAL) {
				{
				setState(1269);
				match(FINAL);
				}
			}

			setState(1273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEDUPLICATE) {
				{
				setState(1272);
				match(DEDUPLICATE);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RenameStmtContext extends ParserRuleContext {
		public TerminalNode RENAME() { return getToken(ClickHouseParser.RENAME, 0); }
		public RenameEntityClauseContext renameEntityClause() {
			return getRuleContext(RenameEntityClauseContext.class,0);
		}
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public RenameStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_renameStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterRenameStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitRenameStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitRenameStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenameStmtContext renameStmt() throws RecognitionException {
		RenameStmtContext _localctx = new RenameStmtContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_renameStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1275);
			match(RENAME);
			setState(1276);
			renameEntityClause();
			setState(1278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(1277);
				clusterClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RenameEntityClauseContext extends ParserRuleContext {
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public List<TableIdentifierContext> tableIdentifier() {
			return getRuleContexts(TableIdentifierContext.class);
		}
		public TableIdentifierContext tableIdentifier(int i) {
			return getRuleContext(TableIdentifierContext.class,i);
		}
		public List<TerminalNode> TO() { return getTokens(ClickHouseParser.TO); }
		public TerminalNode TO(int i) {
			return getToken(ClickHouseParser.TO, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public List<DatabaseIdentifierContext> databaseIdentifier() {
			return getRuleContexts(DatabaseIdentifierContext.class);
		}
		public DatabaseIdentifierContext databaseIdentifier(int i) {
			return getRuleContext(DatabaseIdentifierContext.class,i);
		}
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public List<DictionaryIdentifierContext> dictionaryIdentifier() {
			return getRuleContexts(DictionaryIdentifierContext.class);
		}
		public DictionaryIdentifierContext dictionaryIdentifier(int i) {
			return getRuleContext(DictionaryIdentifierContext.class,i);
		}
		public RenameEntityClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_renameEntityClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterRenameEntityClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitRenameEntityClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitRenameEntityClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenameEntityClauseContext renameEntityClause() throws RecognitionException {
		RenameEntityClauseContext _localctx = new RenameEntityClauseContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_renameEntityClause);
		int _la;
		try {
			setState(1322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TABLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1280);
				match(TABLE);
				setState(1281);
				tableIdentifier();
				setState(1282);
				match(TO);
				setState(1283);
				tableIdentifier();
				setState(1291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1284);
					match(COMMA);
					setState(1285);
					tableIdentifier();
					setState(1286);
					match(TO);
					setState(1287);
					tableIdentifier();
					}
					}
					setState(1293);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case DATABASE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1294);
				match(DATABASE);
				setState(1295);
				databaseIdentifier();
				setState(1296);
				match(TO);
				setState(1297);
				databaseIdentifier();
				setState(1305);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1298);
					match(COMMA);
					setState(1299);
					databaseIdentifier();
					setState(1300);
					match(TO);
					setState(1301);
					databaseIdentifier();
					}
					}
					setState(1307);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case DICTIONARY:
				enterOuterAlt(_localctx, 3);
				{
				setState(1308);
				match(DICTIONARY);
				setState(1309);
				dictionaryIdentifier();
				setState(1310);
				match(TO);
				setState(1311);
				dictionaryIdentifier();
				setState(1319);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1312);
					match(COMMA);
					setState(1313);
					dictionaryIdentifier();
					setState(1314);
					match(TO);
					setState(1315);
					dictionaryIdentifier();
					}
					}
					setState(1321);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProjectionSelectStmtContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode SELECT() { return getToken(ClickHouseParser.SELECT, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public WithClauseContext withClause() {
			return getRuleContext(WithClauseContext.class,0);
		}
		public GroupByClauseContext groupByClause() {
			return getRuleContext(GroupByClauseContext.class,0);
		}
		public ProjectionOrderByClauseContext projectionOrderByClause() {
			return getRuleContext(ProjectionOrderByClauseContext.class,0);
		}
		public ProjectionSelectStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_projectionSelectStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterProjectionSelectStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitProjectionSelectStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitProjectionSelectStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProjectionSelectStmtContext projectionSelectStmt() throws RecognitionException {
		ProjectionSelectStmtContext _localctx = new ProjectionSelectStmtContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_projectionSelectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1324);
			match(LPAREN);
			setState(1326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(1325);
				withClause();
				}
			}

			setState(1328);
			match(SELECT);
			setState(1329);
			columnExprList();
			setState(1331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GROUP) {
				{
				setState(1330);
				groupByClause();
				}
			}

			setState(1334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(1333);
				projectionOrderByClause();
				}
			}

			setState(1336);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectUnionStmtContext extends ParserRuleContext {
		public List<SelectStmtWithParensContext> selectStmtWithParens() {
			return getRuleContexts(SelectStmtWithParensContext.class);
		}
		public SelectStmtWithParensContext selectStmtWithParens(int i) {
			return getRuleContext(SelectStmtWithParensContext.class,i);
		}
		public WithClauseContext withClause() {
			return getRuleContext(WithClauseContext.class,0);
		}
		public List<TerminalNode> UNION() { return getTokens(ClickHouseParser.UNION); }
		public TerminalNode UNION(int i) {
			return getToken(ClickHouseParser.UNION, i);
		}
		public List<TerminalNode> EXCEPT() { return getTokens(ClickHouseParser.EXCEPT); }
		public TerminalNode EXCEPT(int i) {
			return getToken(ClickHouseParser.EXCEPT, i);
		}
		public List<TerminalNode> INTERSECT() { return getTokens(ClickHouseParser.INTERSECT); }
		public TerminalNode INTERSECT(int i) {
			return getToken(ClickHouseParser.INTERSECT, i);
		}
		public List<TerminalNode> ALL() { return getTokens(ClickHouseParser.ALL); }
		public TerminalNode ALL(int i) {
			return getToken(ClickHouseParser.ALL, i);
		}
		public List<TerminalNode> DISTINCT() { return getTokens(ClickHouseParser.DISTINCT); }
		public TerminalNode DISTINCT(int i) {
			return getToken(ClickHouseParser.DISTINCT, i);
		}
		public SelectUnionStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectUnionStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSelectUnionStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSelectUnionStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSelectUnionStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectUnionStmtContext selectUnionStmt() throws RecognitionException {
		SelectUnionStmtContext _localctx = new SelectUnionStmtContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_selectUnionStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1339);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(1338);
				withClause();
				}
			}

			setState(1341);
			selectStmtWithParens();
			setState(1353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EXCEPT || _la==INTERSECT || _la==UNION) {
				{
				{
				setState(1348);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case UNION:
					{
					setState(1342);
					match(UNION);
					setState(1344);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ALL || _la==DISTINCT) {
						{
						setState(1343);
						_la = _input.LA(1);
						if ( !(_la==ALL || _la==DISTINCT) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					}
					break;
				case EXCEPT:
					{
					setState(1346);
					match(EXCEPT);
					}
					break;
				case INTERSECT:
					{
					setState(1347);
					match(INTERSECT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1350);
				selectStmtWithParens();
				}
				}
				setState(1355);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectStmtWithParensContext extends ParserRuleContext {
		public SelectStmtContext selectStmt() {
			return getRuleContext(SelectStmtContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public SelectStmtWithParensContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStmtWithParens; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSelectStmtWithParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSelectStmtWithParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSelectStmtWithParens(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStmtWithParensContext selectStmtWithParens() throws RecognitionException {
		SelectStmtWithParensContext _localctx = new SelectStmtWithParensContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_selectStmtWithParens);
		try {
			setState(1361);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1356);
				selectStmt();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1357);
				match(LPAREN);
				setState(1358);
				selectUnionStmt();
				setState(1359);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectStmtContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(ClickHouseParser.SELECT, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode DISTINCT() { return getToken(ClickHouseParser.DISTINCT, 0); }
		public TopClauseContext topClause() {
			return getRuleContext(TopClauseContext.class,0);
		}
		public FromClauseContext fromClause() {
			return getRuleContext(FromClauseContext.class,0);
		}
		public ArrayJoinClauseContext arrayJoinClause() {
			return getRuleContext(ArrayJoinClauseContext.class,0);
		}
		public WindowClauseContext windowClause() {
			return getRuleContext(WindowClauseContext.class,0);
		}
		public PrewhereClauseContext prewhereClause() {
			return getRuleContext(PrewhereClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public QualifyClauseContext qualifyClause() {
			return getRuleContext(QualifyClauseContext.class,0);
		}
		public GroupByClauseContext groupByClause() {
			return getRuleContext(GroupByClauseContext.class,0);
		}
		public List<TerminalNode> WITH() { return getTokens(ClickHouseParser.WITH); }
		public TerminalNode WITH(int i) {
			return getToken(ClickHouseParser.WITH, i);
		}
		public TerminalNode TOTALS() { return getToken(ClickHouseParser.TOTALS, 0); }
		public HavingClauseContext havingClause() {
			return getRuleContext(HavingClauseContext.class,0);
		}
		public OrderByClauseContext orderByClause() {
			return getRuleContext(OrderByClauseContext.class,0);
		}
		public InterpolateClauseContext interpolateClause() {
			return getRuleContext(InterpolateClauseContext.class,0);
		}
		public LimitByClauseContext limitByClause() {
			return getRuleContext(LimitByClauseContext.class,0);
		}
		public LimitClauseContext limitClause() {
			return getRuleContext(LimitClauseContext.class,0);
		}
		public SettingsClauseContext settingsClause() {
			return getRuleContext(SettingsClauseContext.class,0);
		}
		public TerminalNode CUBE() { return getToken(ClickHouseParser.CUBE, 0); }
		public TerminalNode ROLLUP() { return getToken(ClickHouseParser.ROLLUP, 0); }
		public SelectStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSelectStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSelectStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSelectStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStmtContext selectStmt() throws RecognitionException {
		SelectStmtContext _localctx = new SelectStmtContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1363);
			match(SELECT);
			setState(1365);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				{
				setState(1364);
				match(DISTINCT);
				}
				break;
			}
			setState(1368);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
			case 1:
				{
				setState(1367);
				topClause();
				}
				break;
			}
			setState(1370);
			columnExprList();
			setState(1372);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(1371);
				fromClause();
				}
			}

			setState(1375);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARRAY || _la==INNER || _la==LEFT) {
				{
				setState(1374);
				arrayJoinClause();
				}
			}

			setState(1378);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WINDOW) {
				{
				setState(1377);
				windowClause();
				}
			}

			setState(1381);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PREWHERE) {
				{
				setState(1380);
				prewhereClause();
				}
			}

			setState(1384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(1383);
				whereClause();
				}
			}

			setState(1387);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QUALIFY) {
				{
				setState(1386);
				qualifyClause();
				}
			}

			setState(1390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GROUP) {
				{
				setState(1389);
				groupByClause();
				}
			}

			setState(1394);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,175,_ctx) ) {
			case 1:
				{
				setState(1392);
				match(WITH);
				setState(1393);
				_la = _input.LA(1);
				if ( !(_la==CUBE || _la==ROLLUP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(1396);
				match(WITH);
				setState(1397);
				match(TOTALS);
				}
			}

			setState(1401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==HAVING) {
				{
				setState(1400);
				havingClause();
				}
			}

			setState(1404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(1403);
				orderByClause();
				}
			}

			setState(1407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INTERPOLATE) {
				{
				setState(1406);
				interpolateClause();
				}
			}

			setState(1410);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,180,_ctx) ) {
			case 1:
				{
				setState(1409);
				limitByClause();
				}
				break;
			}
			setState(1413);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(1412);
				limitClause();
				}
			}

			setState(1416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SETTINGS) {
				{
				setState(1415);
				settingsClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WithClauseContext extends ParserRuleContext {
		public TerminalNode WITH() { return getToken(ClickHouseParser.WITH, 0); }
		public WithExprListContext withExprList() {
			return getRuleContext(WithExprListContext.class,0);
		}
		public WithClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWithClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWithClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWithClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithClauseContext withClause() throws RecognitionException {
		WithClauseContext _localctx = new WithClauseContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_withClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
			match(WITH);
			setState(1419);
			withExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WithExprListContext extends ParserRuleContext {
		public List<WithExprContext> withExpr() {
			return getRuleContexts(WithExprContext.class);
		}
		public WithExprContext withExpr(int i) {
			return getRuleContext(WithExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public WithExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWithExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWithExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWithExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithExprListContext withExprList() throws RecognitionException {
		WithExprListContext _localctx = new WithExprListContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_withExprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1421);
			withExpr();
			setState(1426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1422);
				match(COMMA);
				setState(1423);
				withExpr();
				}
				}
				setState(1428);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WithExprContext extends ParserRuleContext {
		public WithExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withExpr; }
	 
		public WithExprContext() { }
		public void copyFrom(WithExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class WithExprExpressionContext extends WithExprContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public WithExprExpressionContext(WithExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWithExprExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWithExprExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWithExprExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WithExprSubqueryContext extends WithExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TerminalNode RECURSIVE() { return getToken(ClickHouseParser.RECURSIVE, 0); }
		public WithExprSubqueryContext(WithExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWithExprSubquery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWithExprSubquery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWithExprSubquery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithExprContext withExpr() throws RecognitionException {
		WithExprContext _localctx = new WithExprContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_withExpr);
		try {
			setState(1442);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,185,_ctx) ) {
			case 1:
				_localctx = new WithExprSubqueryContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1430);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,184,_ctx) ) {
				case 1:
					{
					setState(1429);
					match(RECURSIVE);
					}
					break;
				}
				setState(1432);
				identifier();
				setState(1433);
				match(AS);
				setState(1434);
				match(LPAREN);
				setState(1435);
				selectUnionStmt();
				setState(1436);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new WithExprExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1438);
				columnExpr(0);
				setState(1439);
				match(AS);
				setState(1440);
				identifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TopClauseContext extends ParserRuleContext {
		public TerminalNode TOP() { return getToken(ClickHouseParser.TOP, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public TerminalNode WITH() { return getToken(ClickHouseParser.WITH, 0); }
		public TerminalNode TIES() { return getToken(ClickHouseParser.TIES, 0); }
		public TopClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_topClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTopClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTopClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTopClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TopClauseContext topClause() throws RecognitionException {
		TopClauseContext _localctx = new TopClauseContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_topClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1444);
			match(TOP);
			setState(1445);
			match(DECIMAL_LITERAL);
			setState(1448);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,186,_ctx) ) {
			case 1:
				{
				setState(1446);
				match(WITH);
				setState(1447);
				match(TIES);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FromClauseContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public JoinExprContext joinExpr() {
			return getRuleContext(JoinExprContext.class,0);
		}
		public FromClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterFromClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitFromClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitFromClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromClauseContext fromClause() throws RecognitionException {
		FromClauseContext _localctx = new FromClauseContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_fromClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1450);
			match(FROM);
			setState(1451);
			joinExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayJoinClauseContext extends ParserRuleContext {
		public TerminalNode ARRAY() { return getToken(ClickHouseParser.ARRAY, 0); }
		public TerminalNode JOIN() { return getToken(ClickHouseParser.JOIN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode LEFT() { return getToken(ClickHouseParser.LEFT, 0); }
		public TerminalNode INNER() { return getToken(ClickHouseParser.INNER, 0); }
		public ArrayJoinClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayJoinClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterArrayJoinClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitArrayJoinClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitArrayJoinClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayJoinClauseContext arrayJoinClause() throws RecognitionException {
		ArrayJoinClauseContext _localctx = new ArrayJoinClauseContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_arrayJoinClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1454);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INNER || _la==LEFT) {
				{
				setState(1453);
				_la = _input.LA(1);
				if ( !(_la==INNER || _la==LEFT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1456);
			match(ARRAY);
			setState(1457);
			match(JOIN);
			setState(1458);
			columnExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WindowClauseContext extends ParserRuleContext {
		public TerminalNode WINDOW() { return getToken(ClickHouseParser.WINDOW, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public WindowExprContext windowExpr() {
			return getRuleContext(WindowExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public WindowClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWindowClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWindowClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowClauseContext windowClause() throws RecognitionException {
		WindowClauseContext _localctx = new WindowClauseContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_windowClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1460);
			match(WINDOW);
			setState(1461);
			identifier();
			setState(1462);
			match(AS);
			setState(1463);
			match(LPAREN);
			setState(1464);
			windowExpr();
			setState(1465);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrewhereClauseContext extends ParserRuleContext {
		public TerminalNode PREWHERE() { return getToken(ClickHouseParser.PREWHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public PrewhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prewhereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterPrewhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitPrewhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitPrewhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrewhereClauseContext prewhereClause() throws RecognitionException {
		PrewhereClauseContext _localctx = new PrewhereClauseContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_prewhereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1467);
			match(PREWHERE);
			setState(1468);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifyClauseContext extends ParserRuleContext {
		public TerminalNode QUALIFY() { return getToken(ClickHouseParser.QUALIFY, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public QualifyClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifyClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterQualifyClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitQualifyClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitQualifyClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifyClauseContext qualifyClause() throws RecognitionException {
		QualifyClauseContext _localctx = new QualifyClauseContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_qualifyClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1470);
			match(QUALIFY);
			setState(1471);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhereClauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1473);
			match(WHERE);
			setState(1474);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupByClauseContext extends ParserRuleContext {
		public GroupByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByClause; }
	 
		public GroupByClauseContext() { }
		public void copyFrom(GroupByClauseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class GroupByClauseAllContext extends GroupByClauseContext {
		public TerminalNode GROUP() { return getToken(ClickHouseParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public TerminalNode ALL() { return getToken(ClickHouseParser.ALL, 0); }
		public GroupByClauseAllContext(GroupByClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterGroupByClauseAll(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitGroupByClauseAll(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitGroupByClauseAll(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GroupByClauseSimpleContext extends GroupByClauseContext {
		public TerminalNode GROUP() { return getToken(ClickHouseParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public GroupByClauseSimpleContext(GroupByClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterGroupByClauseSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitGroupByClauseSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitGroupByClauseSimple(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GroupByClauseGroupingSetsContext extends GroupByClauseContext {
		public TerminalNode GROUP() { return getToken(ClickHouseParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public TerminalNode GROUPING() { return getToken(ClickHouseParser.GROUPING, 0); }
		public TerminalNode SETS() { return getToken(ClickHouseParser.SETS, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public GroupByClauseGroupingSetsContext(GroupByClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterGroupByClauseGroupingSets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitGroupByClauseGroupingSets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitGroupByClauseGroupingSets(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GroupByClauseCubeOrRollupContext extends GroupByClauseContext {
		public TerminalNode GROUP() { return getToken(ClickHouseParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TerminalNode CUBE() { return getToken(ClickHouseParser.CUBE, 0); }
		public TerminalNode ROLLUP() { return getToken(ClickHouseParser.ROLLUP, 0); }
		public GroupByClauseCubeOrRollupContext(GroupByClauseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterGroupByClauseCubeOrRollup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitGroupByClauseCubeOrRollup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitGroupByClauseCubeOrRollup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByClauseContext groupByClause() throws RecognitionException {
		GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_groupByClause);
		int _la;
		try {
			setState(1497);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,188,_ctx) ) {
			case 1:
				_localctx = new GroupByClauseAllContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1476);
				match(GROUP);
				setState(1477);
				match(BY);
				setState(1478);
				match(ALL);
				}
				break;
			case 2:
				_localctx = new GroupByClauseCubeOrRollupContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1479);
				match(GROUP);
				setState(1480);
				match(BY);
				setState(1481);
				_la = _input.LA(1);
				if ( !(_la==CUBE || _la==ROLLUP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1482);
				match(LPAREN);
				setState(1483);
				columnExprList();
				setState(1484);
				match(RPAREN);
				}
				break;
			case 3:
				_localctx = new GroupByClauseGroupingSetsContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1486);
				match(GROUP);
				setState(1487);
				match(BY);
				setState(1488);
				match(GROUPING);
				setState(1489);
				match(SETS);
				setState(1490);
				match(LPAREN);
				setState(1491);
				columnExprList();
				setState(1492);
				match(RPAREN);
				}
				break;
			case 4:
				_localctx = new GroupByClauseSimpleContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1494);
				match(GROUP);
				setState(1495);
				match(BY);
				setState(1496);
				columnExprList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HavingClauseContext extends ParserRuleContext {
		public TerminalNode HAVING() { return getToken(ClickHouseParser.HAVING, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public HavingClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_havingClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterHavingClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitHavingClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitHavingClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HavingClauseContext havingClause() throws RecognitionException {
		HavingClauseContext _localctx = new HavingClauseContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_havingClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1499);
			match(HAVING);
			setState(1500);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrderByClauseContext extends ParserRuleContext {
		public TerminalNode ORDER() { return getToken(ClickHouseParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public OrderExprListContext orderExprList() {
			return getRuleContext(OrderExprListContext.class,0);
		}
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitOrderByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_orderByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1502);
			match(ORDER);
			setState(1503);
			match(BY);
			setState(1504);
			orderExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterpolateClauseContext extends ParserRuleContext {
		public TerminalNode INTERPOLATE() { return getToken(ClickHouseParser.INTERPOLATE, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public InterpolateClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interpolateClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterInterpolateClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitInterpolateClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitInterpolateClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterpolateClauseContext interpolateClause() throws RecognitionException {
		InterpolateClauseContext _localctx = new InterpolateClauseContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_interpolateClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1506);
			match(INTERPOLATE);
			setState(1507);
			match(LPAREN);
			setState(1508);
			columnExprList();
			setState(1509);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProjectionOrderByClauseContext extends ParserRuleContext {
		public TerminalNode ORDER() { return getToken(ClickHouseParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ProjectionOrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_projectionOrderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterProjectionOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitProjectionOrderByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitProjectionOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProjectionOrderByClauseContext projectionOrderByClause() throws RecognitionException {
		ProjectionOrderByClauseContext _localctx = new ProjectionOrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_projectionOrderByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1511);
			match(ORDER);
			setState(1512);
			match(BY);
			setState(1513);
			columnExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LimitByClauseContext extends ParserRuleContext {
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public LimitExprContext limitExpr() {
			return getRuleContext(LimitExprContext.class,0);
		}
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public LimitByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterLimitByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitLimitByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitLimitByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LimitByClauseContext limitByClause() throws RecognitionException {
		LimitByClauseContext _localctx = new LimitByClauseContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_limitByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1515);
			match(LIMIT);
			setState(1516);
			limitExpr();
			setState(1517);
			match(BY);
			setState(1518);
			columnExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LimitClauseContext extends ParserRuleContext {
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public LimitExprContext limitExpr() {
			return getRuleContext(LimitExprContext.class,0);
		}
		public TerminalNode WITH() { return getToken(ClickHouseParser.WITH, 0); }
		public TerminalNode TIES() { return getToken(ClickHouseParser.TIES, 0); }
		public LimitClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterLimitClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitLimitClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitLimitClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LimitClauseContext limitClause() throws RecognitionException {
		LimitClauseContext _localctx = new LimitClauseContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_limitClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1520);
			match(LIMIT);
			setState(1521);
			limitExpr();
			setState(1524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(1522);
				match(WITH);
				setState(1523);
				match(TIES);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SettingsClauseContext extends ParserRuleContext {
		public TerminalNode SETTINGS() { return getToken(ClickHouseParser.SETTINGS, 0); }
		public SettingExprListContext settingExprList() {
			return getRuleContext(SettingExprListContext.class,0);
		}
		public SettingsClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_settingsClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSettingsClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSettingsClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSettingsClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SettingsClauseContext settingsClause() throws RecognitionException {
		SettingsClauseContext _localctx = new SettingsClauseContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_settingsClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1526);
			match(SETTINGS);
			setState(1527);
			settingExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinExprContext extends ParserRuleContext {
		public JoinExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinExpr; }
	 
		public JoinExprContext() { }
		public void copyFrom(JoinExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class JoinExprOpContext extends JoinExprContext {
		public List<JoinExprContext> joinExpr() {
			return getRuleContexts(JoinExprContext.class);
		}
		public JoinExprContext joinExpr(int i) {
			return getRuleContext(JoinExprContext.class,i);
		}
		public TerminalNode JOIN() { return getToken(ClickHouseParser.JOIN, 0); }
		public JoinConstraintClauseContext joinConstraintClause() {
			return getRuleContext(JoinConstraintClauseContext.class,0);
		}
		public JoinOpContext joinOp() {
			return getRuleContext(JoinOpContext.class,0);
		}
		public TerminalNode GLOBAL() { return getToken(ClickHouseParser.GLOBAL, 0); }
		public TerminalNode LOCAL() { return getToken(ClickHouseParser.LOCAL, 0); }
		public JoinExprOpContext(JoinExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinExprOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinExprOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinExprOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class JoinExprTableContext extends JoinExprContext {
		public TableExprContext tableExpr() {
			return getRuleContext(TableExprContext.class,0);
		}
		public TerminalNode FINAL() { return getToken(ClickHouseParser.FINAL, 0); }
		public SampleClauseContext sampleClause() {
			return getRuleContext(SampleClauseContext.class,0);
		}
		public JoinExprTableContext(JoinExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinExprTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinExprTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinExprTable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class JoinExprParensContext extends JoinExprContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public JoinExprContext joinExpr() {
			return getRuleContext(JoinExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public JoinExprParensContext(JoinExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinExprParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinExprParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinExprParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class JoinExprCrossOpContext extends JoinExprContext {
		public List<JoinExprContext> joinExpr() {
			return getRuleContexts(JoinExprContext.class);
		}
		public JoinExprContext joinExpr(int i) {
			return getRuleContext(JoinExprContext.class,i);
		}
		public JoinOpCrossContext joinOpCross() {
			return getRuleContext(JoinOpCrossContext.class,0);
		}
		public JoinExprCrossOpContext(JoinExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinExprCrossOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinExprCrossOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinExprCrossOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinExprContext joinExpr() throws RecognitionException {
		return joinExpr(0);
	}

	private JoinExprContext joinExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		JoinExprContext _localctx = new JoinExprContext(_ctx, _parentState);
		JoinExprContext _prevctx = _localctx;
		int _startState = 162;
		enterRecursionRule(_localctx, 162, RULE_joinExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1541);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				{
				_localctx = new JoinExprTableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1530);
				tableExpr(0);
				setState(1532);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,190,_ctx) ) {
				case 1:
					{
					setState(1531);
					match(FINAL);
					}
					break;
				}
				setState(1535);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,191,_ctx) ) {
				case 1:
					{
					setState(1534);
					sampleClause();
					}
					break;
				}
				}
				break;
			case 2:
				{
				_localctx = new JoinExprParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1537);
				match(LPAREN);
				setState(1538);
				joinExpr(0);
				setState(1539);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1560);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,196,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1558);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,195,_ctx) ) {
					case 1:
						{
						_localctx = new JoinExprCrossOpContext(new JoinExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_joinExpr);
						setState(1543);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1544);
						joinOpCross();
						setState(1545);
						joinExpr(4);
						}
						break;
					case 2:
						{
						_localctx = new JoinExprOpContext(new JoinExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_joinExpr);
						setState(1547);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1549);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==GLOBAL || _la==LOCAL) {
							{
							setState(1548);
							_la = _input.LA(1);
							if ( !(_la==GLOBAL || _la==LOCAL) ) {
							_errHandler.recoverInline(this);
							}
							else {
								if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
								_errHandler.reportMatch(this);
								consume();
							}
							}
						}

						setState(1552);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANTI) | (1L << ANY) | (1L << ASOF))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (FULL - 83)) | (1L << (INNER - 83)) | (1L << (LEFT - 83)))) != 0) || _la==RIGHT || _la==SEMI) {
							{
							setState(1551);
							joinOp();
							}
						}

						setState(1554);
						match(JOIN);
						setState(1555);
						joinExpr(0);
						setState(1556);
						joinConstraintClause();
						}
						break;
					}
					} 
				}
				setState(1562);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,196,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class JoinOpContext extends ParserRuleContext {
		public JoinOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinOp; }
	 
		public JoinOpContext() { }
		public void copyFrom(JoinOpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class JoinOpFullContext extends JoinOpContext {
		public TerminalNode FULL() { return getToken(ClickHouseParser.FULL, 0); }
		public TerminalNode OUTER() { return getToken(ClickHouseParser.OUTER, 0); }
		public TerminalNode ALL() { return getToken(ClickHouseParser.ALL, 0); }
		public TerminalNode ANY() { return getToken(ClickHouseParser.ANY, 0); }
		public JoinOpFullContext(JoinOpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinOpFull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinOpFull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinOpFull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class JoinOpInnerContext extends JoinOpContext {
		public TerminalNode INNER() { return getToken(ClickHouseParser.INNER, 0); }
		public TerminalNode ALL() { return getToken(ClickHouseParser.ALL, 0); }
		public TerminalNode ANY() { return getToken(ClickHouseParser.ANY, 0); }
		public TerminalNode ASOF() { return getToken(ClickHouseParser.ASOF, 0); }
		public JoinOpInnerContext(JoinOpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinOpInner(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinOpInner(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinOpInner(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class JoinOpLeftRightContext extends JoinOpContext {
		public TerminalNode LEFT() { return getToken(ClickHouseParser.LEFT, 0); }
		public TerminalNode RIGHT() { return getToken(ClickHouseParser.RIGHT, 0); }
		public TerminalNode OUTER() { return getToken(ClickHouseParser.OUTER, 0); }
		public TerminalNode SEMI() { return getToken(ClickHouseParser.SEMI, 0); }
		public TerminalNode ALL() { return getToken(ClickHouseParser.ALL, 0); }
		public TerminalNode ANTI() { return getToken(ClickHouseParser.ANTI, 0); }
		public TerminalNode ANY() { return getToken(ClickHouseParser.ANY, 0); }
		public TerminalNode ASOF() { return getToken(ClickHouseParser.ASOF, 0); }
		public JoinOpLeftRightContext(JoinOpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinOpLeftRight(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinOpLeftRight(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinOpLeftRight(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinOpContext joinOp() throws RecognitionException {
		JoinOpContext _localctx = new JoinOpContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_joinOp);
		int _la;
		try {
			setState(1606);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,210,_ctx) ) {
			case 1:
				_localctx = new JoinOpInnerContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1572);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,199,_ctx) ) {
				case 1:
					{
					setState(1564);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANY) | (1L << ASOF))) != 0)) {
						{
						setState(1563);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANY) | (1L << ASOF))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					setState(1566);
					match(INNER);
					}
					break;
				case 2:
					{
					setState(1567);
					match(INNER);
					setState(1569);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANY) | (1L << ASOF))) != 0)) {
						{
						setState(1568);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANY) | (1L << ASOF))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					}
					break;
				case 3:
					{
					setState(1571);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANY) | (1L << ASOF))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				}
				break;
			case 2:
				_localctx = new JoinOpLeftRightContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1588);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,204,_ctx) ) {
				case 1:
					{
					setState(1575);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANTI) | (1L << ANY) | (1L << ASOF))) != 0) || _la==SEMI) {
						{
						setState(1574);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANTI) | (1L << ANY) | (1L << ASOF))) != 0) || _la==SEMI) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					setState(1577);
					_la = _input.LA(1);
					if ( !(_la==LEFT || _la==RIGHT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1579);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(1578);
						match(OUTER);
						}
					}

					}
					break;
				case 2:
					{
					setState(1581);
					_la = _input.LA(1);
					if ( !(_la==LEFT || _la==RIGHT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1583);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(1582);
						match(OUTER);
						}
					}

					setState(1586);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANTI) | (1L << ANY) | (1L << ASOF))) != 0) || _la==SEMI) {
						{
						setState(1585);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << ANTI) | (1L << ANY) | (1L << ASOF))) != 0) || _la==SEMI) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					}
					break;
				}
				}
				break;
			case 3:
				_localctx = new JoinOpFullContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1604);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,209,_ctx) ) {
				case 1:
					{
					setState(1591);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ALL || _la==ANY) {
						{
						setState(1590);
						_la = _input.LA(1);
						if ( !(_la==ALL || _la==ANY) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					setState(1593);
					match(FULL);
					setState(1595);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(1594);
						match(OUTER);
						}
					}

					}
					break;
				case 2:
					{
					setState(1597);
					match(FULL);
					setState(1599);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(1598);
						match(OUTER);
						}
					}

					setState(1602);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ALL || _la==ANY) {
						{
						setState(1601);
						_la = _input.LA(1);
						if ( !(_la==ALL || _la==ANY) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
					}

					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinOpCrossContext extends ParserRuleContext {
		public TerminalNode CROSS() { return getToken(ClickHouseParser.CROSS, 0); }
		public TerminalNode JOIN() { return getToken(ClickHouseParser.JOIN, 0); }
		public TerminalNode GLOBAL() { return getToken(ClickHouseParser.GLOBAL, 0); }
		public TerminalNode LOCAL() { return getToken(ClickHouseParser.LOCAL, 0); }
		public TerminalNode COMMA() { return getToken(ClickHouseParser.COMMA, 0); }
		public JoinOpCrossContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinOpCross; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinOpCross(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinOpCross(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinOpCross(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinOpCrossContext joinOpCross() throws RecognitionException {
		JoinOpCrossContext _localctx = new JoinOpCrossContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_joinOpCross);
		int _la;
		try {
			setState(1614);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CROSS:
			case GLOBAL:
			case LOCAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(1609);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GLOBAL || _la==LOCAL) {
					{
					setState(1608);
					_la = _input.LA(1);
					if ( !(_la==GLOBAL || _la==LOCAL) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(1611);
				match(CROSS);
				setState(1612);
				match(JOIN);
				}
				break;
			case COMMA:
				enterOuterAlt(_localctx, 2);
				{
				setState(1613);
				match(COMMA);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinConstraintClauseContext extends ParserRuleContext {
		public TerminalNode ON() { return getToken(ClickHouseParser.ON, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public TerminalNode USING() { return getToken(ClickHouseParser.USING, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public JoinConstraintClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinConstraintClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterJoinConstraintClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitJoinConstraintClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitJoinConstraintClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinConstraintClauseContext joinConstraintClause() throws RecognitionException {
		JoinConstraintClauseContext _localctx = new JoinConstraintClauseContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_joinConstraintClause);
		try {
			setState(1625);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1616);
				match(ON);
				setState(1617);
				columnExprList();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1618);
				match(USING);
				setState(1619);
				match(LPAREN);
				setState(1620);
				columnExprList();
				setState(1621);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1623);
				match(USING);
				setState(1624);
				columnExprList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SampleClauseContext extends ParserRuleContext {
		public TerminalNode SAMPLE() { return getToken(ClickHouseParser.SAMPLE, 0); }
		public List<RatioExprContext> ratioExpr() {
			return getRuleContexts(RatioExprContext.class);
		}
		public RatioExprContext ratioExpr(int i) {
			return getRuleContext(RatioExprContext.class,i);
		}
		public TerminalNode OFFSET() { return getToken(ClickHouseParser.OFFSET, 0); }
		public SampleClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sampleClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSampleClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSampleClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSampleClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SampleClauseContext sampleClause() throws RecognitionException {
		SampleClauseContext _localctx = new SampleClauseContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_sampleClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1627);
			match(SAMPLE);
			setState(1628);
			ratioExpr();
			setState(1631);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,214,_ctx) ) {
			case 1:
				{
				setState(1629);
				match(OFFSET);
				setState(1630);
				ratioExpr();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LimitExprContext extends ParserRuleContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(ClickHouseParser.COMMA, 0); }
		public TerminalNode OFFSET() { return getToken(ClickHouseParser.OFFSET, 0); }
		public LimitExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterLimitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitLimitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitLimitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LimitExprContext limitExpr() throws RecognitionException {
		LimitExprContext _localctx = new LimitExprContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_limitExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1633);
			columnExpr(0);
			setState(1636);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OFFSET || _la==COMMA) {
				{
				setState(1634);
				_la = _input.LA(1);
				if ( !(_la==OFFSET || _la==COMMA) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1635);
				columnExpr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrderExprListContext extends ParserRuleContext {
		public List<OrderExprContext> orderExpr() {
			return getRuleContexts(OrderExprContext.class);
		}
		public OrderExprContext orderExpr(int i) {
			return getRuleContext(OrderExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public OrderExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterOrderExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitOrderExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitOrderExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderExprListContext orderExprList() throws RecognitionException {
		OrderExprListContext _localctx = new OrderExprListContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_orderExprList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1638);
			orderExpr();
			setState(1643);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,216,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1639);
					match(COMMA);
					setState(1640);
					orderExpr();
					}
					} 
				}
				setState(1645);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,216,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrderExprContext extends ParserRuleContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode NULLS() { return getToken(ClickHouseParser.NULLS, 0); }
		public TerminalNode COLLATE() { return getToken(ClickHouseParser.COLLATE, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode WITH() { return getToken(ClickHouseParser.WITH, 0); }
		public TerminalNode FILL() { return getToken(ClickHouseParser.FILL, 0); }
		public TerminalNode ASCENDING() { return getToken(ClickHouseParser.ASCENDING, 0); }
		public TerminalNode DESCENDING() { return getToken(ClickHouseParser.DESCENDING, 0); }
		public TerminalNode DESC() { return getToken(ClickHouseParser.DESC, 0); }
		public TerminalNode FIRST() { return getToken(ClickHouseParser.FIRST, 0); }
		public TerminalNode LAST() { return getToken(ClickHouseParser.LAST, 0); }
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public TerminalNode TO() { return getToken(ClickHouseParser.TO, 0); }
		public TerminalNode STEP() { return getToken(ClickHouseParser.STEP, 0); }
		public OrderExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterOrderExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitOrderExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitOrderExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderExprContext orderExpr() throws RecognitionException {
		OrderExprContext _localctx = new OrderExprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_orderExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1646);
			columnExpr(0);
			setState(1648);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,217,_ctx) ) {
			case 1:
				{
				setState(1647);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASCENDING) | (1L << DESC) | (1L << DESCENDING))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1652);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,218,_ctx) ) {
			case 1:
				{
				setState(1650);
				match(NULLS);
				setState(1651);
				_la = _input.LA(1);
				if ( !(_la==FIRST || _la==LAST) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1656);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,219,_ctx) ) {
			case 1:
				{
				setState(1654);
				match(COLLATE);
				setState(1655);
				stringLiteral();
				}
				break;
			}
			setState(1672);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,223,_ctx) ) {
			case 1:
				{
				setState(1658);
				match(WITH);
				setState(1659);
				match(FILL);
				setState(1662);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,220,_ctx) ) {
				case 1:
					{
					setState(1660);
					match(FROM);
					setState(1661);
					columnExpr(0);
					}
					break;
				}
				setState(1666);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,221,_ctx) ) {
				case 1:
					{
					setState(1664);
					match(TO);
					setState(1665);
					columnExpr(0);
					}
					break;
				}
				setState(1670);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,222,_ctx) ) {
				case 1:
					{
					setState(1668);
					match(STEP);
					setState(1669);
					columnExpr(0);
					}
					break;
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RatioExprContext extends ParserRuleContext {
		public List<NumberLiteralContext> numberLiteral() {
			return getRuleContexts(NumberLiteralContext.class);
		}
		public NumberLiteralContext numberLiteral(int i) {
			return getRuleContext(NumberLiteralContext.class,i);
		}
		public TerminalNode SLASH() { return getToken(ClickHouseParser.SLASH, 0); }
		public RatioExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ratioExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterRatioExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitRatioExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitRatioExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RatioExprContext ratioExpr() throws RecognitionException {
		RatioExprContext _localctx = new RatioExprContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_ratioExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1674);
			numberLiteral();
			setState(1677);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,224,_ctx) ) {
			case 1:
				{
				setState(1675);
				match(SLASH);
				setState(1676);
				numberLiteral();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SettingExprListContext extends ParserRuleContext {
		public List<SettingExprContext> settingExpr() {
			return getRuleContexts(SettingExprContext.class);
		}
		public SettingExprContext settingExpr(int i) {
			return getRuleContext(SettingExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public SettingExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_settingExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSettingExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSettingExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSettingExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SettingExprListContext settingExprList() throws RecognitionException {
		SettingExprListContext _localctx = new SettingExprListContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_settingExprList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1679);
			settingExpr();
			setState(1684);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1680);
					match(COMMA);
					setState(1681);
					settingExpr();
					}
					} 
				}
				setState(1686);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SettingExprContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode EQ_SINGLE() { return getToken(ClickHouseParser.EQ_SINGLE, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public SettingExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_settingExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSettingExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSettingExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSettingExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SettingExprContext settingExpr() throws RecognitionException {
		SettingExprContext _localctx = new SettingExprContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_settingExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1687);
			identifier();
			setState(1688);
			match(EQ_SINGLE);
			setState(1689);
			literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WindowExprContext extends ParserRuleContext {
		public WinPartitionByClauseContext winPartitionByClause() {
			return getRuleContext(WinPartitionByClauseContext.class,0);
		}
		public WinOrderByClauseContext winOrderByClause() {
			return getRuleContext(WinOrderByClauseContext.class,0);
		}
		public WinFrameClauseContext winFrameClause() {
			return getRuleContext(WinFrameClauseContext.class,0);
		}
		public WindowExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWindowExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWindowExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWindowExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowExprContext windowExpr() throws RecognitionException {
		WindowExprContext _localctx = new WindowExprContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_windowExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1692);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PARTITION) {
				{
				setState(1691);
				winPartitionByClause();
				}
			}

			setState(1695);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(1694);
				winOrderByClause();
				}
			}

			setState(1698);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RANGE || _la==ROWS) {
				{
				setState(1697);
				winFrameClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WinPartitionByClauseContext extends ParserRuleContext {
		public TerminalNode PARTITION() { return getToken(ClickHouseParser.PARTITION, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public WinPartitionByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_winPartitionByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWinPartitionByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWinPartitionByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWinPartitionByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WinPartitionByClauseContext winPartitionByClause() throws RecognitionException {
		WinPartitionByClauseContext _localctx = new WinPartitionByClauseContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_winPartitionByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1700);
			match(PARTITION);
			setState(1701);
			match(BY);
			setState(1702);
			columnExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WinOrderByClauseContext extends ParserRuleContext {
		public TerminalNode ORDER() { return getToken(ClickHouseParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public OrderExprListContext orderExprList() {
			return getRuleContext(OrderExprListContext.class,0);
		}
		public WinOrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_winOrderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWinOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWinOrderByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWinOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WinOrderByClauseContext winOrderByClause() throws RecognitionException {
		WinOrderByClauseContext _localctx = new WinOrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_winOrderByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1704);
			match(ORDER);
			setState(1705);
			match(BY);
			setState(1706);
			orderExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WinFrameClauseContext extends ParserRuleContext {
		public WinFrameExtendContext winFrameExtend() {
			return getRuleContext(WinFrameExtendContext.class,0);
		}
		public TerminalNode ROWS() { return getToken(ClickHouseParser.ROWS, 0); }
		public TerminalNode RANGE() { return getToken(ClickHouseParser.RANGE, 0); }
		public WinFrameClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_winFrameClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWinFrameClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWinFrameClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWinFrameClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WinFrameClauseContext winFrameClause() throws RecognitionException {
		WinFrameClauseContext _localctx = new WinFrameClauseContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_winFrameClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1708);
			_la = _input.LA(1);
			if ( !(_la==RANGE || _la==ROWS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1709);
			winFrameExtend();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WinFrameExtendContext extends ParserRuleContext {
		public WinFrameExtendContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_winFrameExtend; }
	 
		public WinFrameExtendContext() { }
		public void copyFrom(WinFrameExtendContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FrameStartContext extends WinFrameExtendContext {
		public WinFrameBoundContext winFrameBound() {
			return getRuleContext(WinFrameBoundContext.class,0);
		}
		public FrameStartContext(WinFrameExtendContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterFrameStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitFrameStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitFrameStart(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FrameBetweenContext extends WinFrameExtendContext {
		public TerminalNode BETWEEN() { return getToken(ClickHouseParser.BETWEEN, 0); }
		public List<WinFrameBoundContext> winFrameBound() {
			return getRuleContexts(WinFrameBoundContext.class);
		}
		public WinFrameBoundContext winFrameBound(int i) {
			return getRuleContext(WinFrameBoundContext.class,i);
		}
		public TerminalNode AND() { return getToken(ClickHouseParser.AND, 0); }
		public FrameBetweenContext(WinFrameExtendContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterFrameBetween(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitFrameBetween(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitFrameBetween(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WinFrameExtendContext winFrameExtend() throws RecognitionException {
		WinFrameExtendContext _localctx = new WinFrameExtendContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_winFrameExtend);
		try {
			setState(1717);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CURRENT:
			case INF:
			case NAN_SQL:
			case UNBOUNDED:
			case FLOATING_LITERAL:
			case OCTAL_LITERAL:
			case DECIMAL_LITERAL:
			case HEXADECIMAL_NUMERIC_LITERAL:
			case BINARY_NUMERIC_LITERAL:
			case DASH:
			case DOT:
			case PLUS:
				_localctx = new FrameStartContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1711);
				winFrameBound();
				}
				break;
			case BETWEEN:
				_localctx = new FrameBetweenContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1712);
				match(BETWEEN);
				setState(1713);
				winFrameBound();
				setState(1714);
				match(AND);
				setState(1715);
				winFrameBound();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WinFrameBoundContext extends ParserRuleContext {
		public TerminalNode CURRENT() { return getToken(ClickHouseParser.CURRENT, 0); }
		public TerminalNode ROW() { return getToken(ClickHouseParser.ROW, 0); }
		public TerminalNode UNBOUNDED() { return getToken(ClickHouseParser.UNBOUNDED, 0); }
		public TerminalNode PRECEDING() { return getToken(ClickHouseParser.PRECEDING, 0); }
		public TerminalNode FOLLOWING() { return getToken(ClickHouseParser.FOLLOWING, 0); }
		public NumberLiteralContext numberLiteral() {
			return getRuleContext(NumberLiteralContext.class,0);
		}
		public WinFrameBoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_winFrameBound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWinFrameBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWinFrameBound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWinFrameBound(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WinFrameBoundContext winFrameBound() throws RecognitionException {
		WinFrameBoundContext _localctx = new WinFrameBoundContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_winFrameBound);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1731);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,230,_ctx) ) {
			case 1:
				{
				setState(1719);
				match(CURRENT);
				setState(1720);
				match(ROW);
				}
				break;
			case 2:
				{
				setState(1721);
				match(UNBOUNDED);
				setState(1722);
				match(PRECEDING);
				}
				break;
			case 3:
				{
				setState(1723);
				match(UNBOUNDED);
				setState(1724);
				match(FOLLOWING);
				}
				break;
			case 4:
				{
				setState(1725);
				numberLiteral();
				setState(1726);
				match(PRECEDING);
				}
				break;
			case 5:
				{
				setState(1728);
				numberLiteral();
				setState(1729);
				match(FOLLOWING);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetStmtContext extends ParserRuleContext {
		public TerminalNode SET() { return getToken(ClickHouseParser.SET, 0); }
		public SettingExprListContext settingExprList() {
			return getRuleContext(SettingExprListContext.class,0);
		}
		public SetStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSetStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSetStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSetStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetStmtContext setStmt() throws RecognitionException {
		SetStmtContext _localctx = new SetStmtContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_setStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1733);
			match(SET);
			setState(1734);
			settingExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ShowStmtContext extends ParserRuleContext {
		public ShowStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_showStmt; }
	 
		public ShowStmtContext() { }
		public void copyFrom(ShowStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ShowCreateUserStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode USER() { return getToken(ClickHouseParser.USER, 0); }
		public TerminalNode CURRENT_USER() { return getToken(ClickHouseParser.CURRENT_USER, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ShowCreateUserStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateUserStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateUserStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateUserStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowMergesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode MERGES() { return getToken(ClickHouseParser.MERGES, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ShowMergesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowMergesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowMergesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowMergesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowTablesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode TABLES() { return getToken(ClickHouseParser.TABLES, 0); }
		public TerminalNode FULL() { return getToken(ClickHouseParser.FULL, 0); }
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ShowTablesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowTablesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowTablesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowTablesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowColumnsStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode COLUMNS() { return getToken(ClickHouseParser.COLUMNS, 0); }
		public TerminalNode FIELDS() { return getToken(ClickHouseParser.FIELDS, 0); }
		public List<TerminalNode> FROM() { return getTokens(ClickHouseParser.FROM); }
		public TerminalNode FROM(int i) {
			return getToken(ClickHouseParser.FROM, i);
		}
		public List<TerminalNode> IN() { return getTokens(ClickHouseParser.IN); }
		public TerminalNode IN(int i) {
			return getToken(ClickHouseParser.IN, i);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode EXTENDED() { return getToken(ClickHouseParser.EXTENDED, 0); }
		public TerminalNode FULL() { return getToken(ClickHouseParser.FULL, 0); }
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ShowColumnsStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowColumnsStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowColumnsStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowColumnsStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowClusterStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CLUSTER() { return getToken(ClickHouseParser.CLUSTER, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public ShowClusterStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowClusterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowClusterStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowClusterStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowSettingsStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode SETTINGS() { return getToken(ClickHouseParser.SETTINGS, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode CHANGED() { return getToken(ClickHouseParser.CHANGED, 0); }
		public ShowSettingsStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowSettingsStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowSettingsStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowSettingsStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowQuotaStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode QUOTA() { return getToken(ClickHouseParser.QUOTA, 0); }
		public TerminalNode CURRENT() { return getToken(ClickHouseParser.CURRENT, 0); }
		public ShowQuotaStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowQuotaStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowQuotaStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowQuotaStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateViewStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public ShowCreateViewStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateViewStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateViewStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateViewStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowSettingStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode SETTING() { return getToken(ClickHouseParser.SETTING, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public ShowSettingStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowSettingStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowSettingStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowSettingStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowAccessStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode ACCESS() { return getToken(ClickHouseParser.ACCESS, 0); }
		public ShowAccessStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowAccessStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowAccessStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowAccessStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowClustersStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CLUSTERS() { return getToken(ClickHouseParser.CLUSTERS, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ShowClustersStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowClustersStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowClustersStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowClustersStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowIndexStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public TerminalNode INDEXES() { return getToken(ClickHouseParser.INDEXES, 0); }
		public TerminalNode INDICES() { return getToken(ClickHouseParser.INDICES, 0); }
		public TerminalNode KEYS() { return getToken(ClickHouseParser.KEYS, 0); }
		public List<TerminalNode> FROM() { return getTokens(ClickHouseParser.FROM); }
		public TerminalNode FROM(int i) {
			return getToken(ClickHouseParser.FROM, i);
		}
		public List<TerminalNode> IN() { return getTokens(ClickHouseParser.IN); }
		public TerminalNode IN(int i) {
			return getToken(ClickHouseParser.IN, i);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode EXTENDED() { return getToken(ClickHouseParser.EXTENDED, 0); }
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public ShowIndexStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowIndexStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowIndexStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowIndexStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowFunctionsStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode FUNCTIONS() { return getToken(ClickHouseParser.FUNCTIONS, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public ShowFunctionsStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowFunctionsStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowFunctionsStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowFunctionsStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowPoliciesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode POLICIES() { return getToken(ClickHouseParser.POLICIES, 0); }
		public TerminalNode ROW() { return getToken(ClickHouseParser.ROW, 0); }
		public TerminalNode ON() { return getToken(ClickHouseParser.ON, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public ShowPoliciesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowPoliciesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowPoliciesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowPoliciesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreatePolicyStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode POLICY() { return getToken(ClickHouseParser.POLICY, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ON() { return getToken(ClickHouseParser.ON, 0); }
		public List<TableIdentifierContext> tableIdentifier() {
			return getRuleContexts(TableIdentifierContext.class);
		}
		public TableIdentifierContext tableIdentifier(int i) {
			return getRuleContext(TableIdentifierContext.class,i);
		}
		public TerminalNode ROW() { return getToken(ClickHouseParser.ROW, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ShowCreatePolicyStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreatePolicyStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreatePolicyStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreatePolicyStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateQuotaStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode QUOTA() { return getToken(ClickHouseParser.QUOTA, 0); }
		public TerminalNode CURRENT() { return getToken(ClickHouseParser.CURRENT, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ShowCreateQuotaStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateQuotaStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateQuotaStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateQuotaStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowPrivilegesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode PRIVILEGES() { return getToken(ClickHouseParser.PRIVILEGES, 0); }
		public ShowPrivilegesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowPrivilegesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowPrivilegesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowPrivilegesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateDatabaseStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public ShowCreateDatabaseStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateDatabaseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateDatabaseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateDatabaseStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowDatabasesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode DATABASES() { return getToken(ClickHouseParser.DATABASES, 0); }
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ShowDatabasesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowDatabasesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowDatabasesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowDatabasesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateProfileStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode PROFILE() { return getToken(ClickHouseParser.PROFILE, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode SETTINGS() { return getToken(ClickHouseParser.SETTINGS, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ShowCreateProfileStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateProfileStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateProfileStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateProfileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateTableStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public ShowCreateTableStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateTableStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateTableStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateTableStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowDictionariesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode DICTIONARIES() { return getToken(ClickHouseParser.DICTIONARIES, 0); }
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ShowDictionariesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowDictionariesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowDictionariesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowDictionariesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowProfilesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode PROFILES() { return getToken(ClickHouseParser.PROFILES, 0); }
		public TerminalNode SETTINGS() { return getToken(ClickHouseParser.SETTINGS, 0); }
		public ShowProfilesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowProfilesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowProfilesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowProfilesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateDictionaryStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public ShowCreateDictionaryStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateDictionaryStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateDictionaryStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateDictionaryStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowGrantsStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode GRANTS() { return getToken(ClickHouseParser.GRANTS, 0); }
		public TerminalNode FOR() { return getToken(ClickHouseParser.FOR, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode WITH() { return getToken(ClickHouseParser.WITH, 0); }
		public TerminalNode IMPLICIT() { return getToken(ClickHouseParser.IMPLICIT, 0); }
		public TerminalNode FINAL() { return getToken(ClickHouseParser.FINAL, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ShowGrantsStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowGrantsStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowGrantsStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowGrantsStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowQuotasStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode QUOTAS() { return getToken(ClickHouseParser.QUOTAS, 0); }
		public ShowQuotasStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowQuotasStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowQuotasStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowQuotasStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowRolesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode ROLES() { return getToken(ClickHouseParser.ROLES, 0); }
		public TerminalNode CURRENT() { return getToken(ClickHouseParser.CURRENT, 0); }
		public TerminalNode ENABLED() { return getToken(ClickHouseParser.ENABLED, 0); }
		public ShowRolesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowRolesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowRolesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowRolesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowEnginesStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode ENGINES() { return getToken(ClickHouseParser.ENGINES, 0); }
		public ShowEnginesStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowEnginesStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowEnginesStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowEnginesStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowProcessListStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode PROCESSLIST() { return getToken(ClickHouseParser.PROCESSLIST, 0); }
		public ShowProcessListStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowProcessListStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowProcessListStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowProcessListStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowUsersStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode USERS() { return getToken(ClickHouseParser.USERS, 0); }
		public ShowUsersStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowUsersStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowUsersStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowUsersStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowFilesystemCachesContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode FILESYSTEM() { return getToken(ClickHouseParser.FILESYSTEM, 0); }
		public TerminalNode CACHES() { return getToken(ClickHouseParser.CACHES, 0); }
		public ShowFilesystemCachesContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowFilesystemCaches(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowFilesystemCaches(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowFilesystemCaches(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowCreateRoleStmtContext extends ShowStmtContext {
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode ROLE() { return getToken(ClickHouseParser.ROLE, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ShowCreateRoleStmtContext(ShowStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterShowCreateRoleStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitShowCreateRoleStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitShowCreateRoleStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShowStmtContext showStmt() throws RecognitionException {
		ShowStmtContext _localctx = new ShowStmtContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_showStmt);
		int _la;
		try {
			setState(2035);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,281,_ctx) ) {
			case 1:
				_localctx = new ShowCreateDatabaseStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1736);
				match(SHOW);
				setState(1737);
				match(CREATE);
				setState(1738);
				match(DATABASE);
				setState(1739);
				databaseIdentifier();
				}
				break;
			case 2:
				_localctx = new ShowCreateDictionaryStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1740);
				match(SHOW);
				setState(1741);
				match(CREATE);
				setState(1742);
				match(DICTIONARY);
				setState(1743);
				tableIdentifier();
				}
				break;
			case 3:
				_localctx = new ShowCreateViewStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1744);
				match(SHOW);
				setState(1746);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CREATE) {
					{
					setState(1745);
					match(CREATE);
					}
				}

				setState(1748);
				match(VIEW);
				setState(1749);
				tableIdentifier();
				}
				break;
			case 4:
				_localctx = new ShowDatabasesStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1750);
				match(SHOW);
				setState(1751);
				match(DATABASES);
				setState(1759);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ILIKE:
				case LIKE:
				case NOT:
					{
					{
					setState(1753);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(1752);
						match(NOT);
						}
					}

					setState(1755);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1756);
					stringLiteral();
					}
					}
					break;
				case WHERE:
					{
					setState(1757);
					match(WHERE);
					setState(1758);
					columnExpr(0);
					}
					break;
				case EOF:
				case FORMAT:
				case INTO:
				case LIMIT:
				case SEMICOLON:
					break;
				default:
					break;
				}
				setState(1763);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LIMIT) {
					{
					setState(1761);
					match(LIMIT);
					setState(1762);
					match(DECIMAL_LITERAL);
					}
				}

				}
				break;
			case 5:
				_localctx = new ShowDictionariesStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1765);
				match(SHOW);
				setState(1766);
				match(DICTIONARIES);
				setState(1769);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FROM) {
					{
					setState(1767);
					match(FROM);
					setState(1768);
					databaseIdentifier();
					}
				}

				setState(1778);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ILIKE:
				case LIKE:
				case NOT:
					{
					{
					setState(1772);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(1771);
						match(NOT);
						}
					}

					setState(1774);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1775);
					stringLiteral();
					}
					}
					break;
				case WHERE:
					{
					setState(1776);
					match(WHERE);
					setState(1777);
					columnExpr(0);
					}
					break;
				case EOF:
				case FORMAT:
				case INTO:
				case LIMIT:
				case SEMICOLON:
					break;
				default:
					break;
				}
				setState(1782);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LIMIT) {
					{
					setState(1780);
					match(LIMIT);
					setState(1781);
					match(DECIMAL_LITERAL);
					}
				}

				}
				break;
			case 6:
				_localctx = new ShowTablesStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1784);
				match(SHOW);
				setState(1786);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FULL) {
					{
					setState(1785);
					match(FULL);
					}
				}

				setState(1789);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TEMPORARY) {
					{
					setState(1788);
					match(TEMPORARY);
					}
				}

				setState(1791);
				match(TABLES);
				setState(1794);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(1792);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1793);
					databaseIdentifier();
					}
				}

				setState(1803);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ILIKE:
				case LIKE:
				case NOT:
					{
					{
					setState(1797);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(1796);
						match(NOT);
						}
					}

					setState(1799);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1800);
					stringLiteral();
					}
					}
					break;
				case WHERE:
					{
					setState(1801);
					match(WHERE);
					setState(1802);
					columnExpr(0);
					}
					break;
				case EOF:
				case FORMAT:
				case INTO:
				case LIMIT:
				case SEMICOLON:
					break;
				default:
					break;
				}
				setState(1807);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LIMIT) {
					{
					setState(1805);
					match(LIMIT);
					setState(1806);
					match(DECIMAL_LITERAL);
					}
				}

				}
				break;
			case 7:
				_localctx = new ShowColumnsStmtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1809);
				match(SHOW);
				setState(1811);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(1810);
					match(EXTENDED);
					}
				}

				setState(1814);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FULL) {
					{
					setState(1813);
					match(FULL);
					}
				}

				setState(1816);
				_la = _input.LA(1);
				if ( !(_la==COLUMNS || _la==FIELDS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1817);
				_la = _input.LA(1);
				if ( !(_la==FROM || _la==IN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1823);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,247,_ctx) ) {
				case 1:
					{
					setState(1818);
					tableIdentifier();
					}
					break;
				case 2:
					{
					{
					setState(1819);
					identifier();
					setState(1820);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1821);
					identifier();
					}
					}
					break;
				}
				setState(1832);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ILIKE:
				case LIKE:
				case NOT:
					{
					{
					setState(1826);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(1825);
						match(NOT);
						}
					}

					setState(1828);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1829);
					stringLiteral();
					}
					}
					break;
				case WHERE:
					{
					setState(1830);
					match(WHERE);
					setState(1831);
					columnExpr(0);
					}
					break;
				case EOF:
				case FORMAT:
				case INTO:
				case LIMIT:
				case SEMICOLON:
					break;
				default:
					break;
				}
				setState(1836);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LIMIT) {
					{
					setState(1834);
					match(LIMIT);
					setState(1835);
					match(DECIMAL_LITERAL);
					}
				}

				}
				break;
			case 8:
				_localctx = new ShowIndexStmtContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(1838);
				match(SHOW);
				setState(1840);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(1839);
					match(EXTENDED);
					}
				}

				setState(1842);
				_la = _input.LA(1);
				if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & ((1L << (INDEX - 99)) | (1L << (INDEXES - 99)) | (1L << (INDICES - 99)) | (1L << (KEYS - 99)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1843);
				_la = _input.LA(1);
				if ( !(_la==FROM || _la==IN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1849);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,252,_ctx) ) {
				case 1:
					{
					setState(1844);
					tableIdentifier();
					}
					break;
				case 2:
					{
					{
					setState(1845);
					identifier();
					setState(1846);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1847);
					identifier();
					}
					}
					break;
				}
				setState(1853);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WHERE) {
					{
					setState(1851);
					match(WHERE);
					setState(1852);
					columnExpr(0);
					}
				}

				}
				break;
			case 9:
				_localctx = new ShowProcessListStmtContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(1855);
				match(SHOW);
				setState(1856);
				match(PROCESSLIST);
				}
				break;
			case 10:
				_localctx = new ShowGrantsStmtContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(1857);
				match(SHOW);
				setState(1858);
				match(GRANTS);
				setState(1868);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FOR) {
					{
					setState(1859);
					match(FOR);
					setState(1860);
					identifier();
					setState(1865);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1861);
						match(COMMA);
						setState(1862);
						identifier();
						}
						}
						setState(1867);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1872);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WITH) {
					{
					setState(1870);
					match(WITH);
					setState(1871);
					match(IMPLICIT);
					}
				}

				setState(1875);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FINAL) {
					{
					setState(1874);
					match(FINAL);
					}
				}

				}
				break;
			case 11:
				_localctx = new ShowCreateUserStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(1877);
				match(SHOW);
				setState(1878);
				match(CREATE);
				setState(1879);
				match(USER);
				setState(1889);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,259,_ctx) ) {
				case 1:
					{
					{
					setState(1880);
					identifier();
					setState(1885);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1881);
						match(COMMA);
						setState(1882);
						identifier();
						}
						}
						setState(1887);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					break;
				case 2:
					{
					setState(1888);
					match(CURRENT_USER);
					}
					break;
				}
				}
				break;
			case 12:
				_localctx = new ShowCreateRoleStmtContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(1891);
				match(SHOW);
				setState(1892);
				match(CREATE);
				setState(1893);
				match(ROLE);
				setState(1894);
				identifier();
				setState(1899);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1895);
					match(COMMA);
					setState(1896);
					identifier();
					}
					}
					setState(1901);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 13:
				_localctx = new ShowCreatePolicyStmtContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(1902);
				match(SHOW);
				setState(1903);
				match(CREATE);
				setState(1905);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ROW) {
					{
					setState(1904);
					match(ROW);
					}
				}

				setState(1907);
				match(POLICY);
				setState(1908);
				identifier();
				setState(1909);
				match(ON);
				setState(1910);
				tableIdentifier();
				setState(1915);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1911);
					match(COMMA);
					setState(1912);
					tableIdentifier();
					}
					}
					setState(1917);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 14:
				_localctx = new ShowCreateQuotaStmtContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(1918);
				match(SHOW);
				setState(1919);
				match(CREATE);
				setState(1920);
				match(QUOTA);
				setState(1930);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,264,_ctx) ) {
				case 1:
					{
					{
					setState(1921);
					identifier();
					setState(1926);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1922);
						match(COMMA);
						setState(1923);
						identifier();
						}
						}
						setState(1928);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					break;
				case 2:
					{
					setState(1929);
					match(CURRENT);
					}
					break;
				}
				}
				break;
			case 15:
				_localctx = new ShowCreateProfileStmtContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(1932);
				match(SHOW);
				setState(1933);
				match(CREATE);
				setState(1935);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SETTINGS) {
					{
					setState(1934);
					match(SETTINGS);
					}
				}

				setState(1937);
				match(PROFILE);
				setState(1938);
				identifier();
				setState(1943);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1939);
					match(COMMA);
					setState(1940);
					identifier();
					}
					}
					setState(1945);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 16:
				_localctx = new ShowUsersStmtContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(1946);
				match(SHOW);
				setState(1947);
				match(USERS);
				}
				break;
			case 17:
				_localctx = new ShowRolesStmtContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(1948);
				match(SHOW);
				setState(1950);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CURRENT || _la==ENABLED) {
					{
					setState(1949);
					_la = _input.LA(1);
					if ( !(_la==CURRENT || _la==ENABLED) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(1952);
				match(ROLES);
				}
				break;
			case 18:
				_localctx = new ShowProfilesStmtContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(1953);
				match(SHOW);
				setState(1955);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SETTINGS) {
					{
					setState(1954);
					match(SETTINGS);
					}
				}

				setState(1957);
				match(PROFILES);
				}
				break;
			case 19:
				_localctx = new ShowPoliciesStmtContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(1958);
				match(SHOW);
				setState(1960);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ROW) {
					{
					setState(1959);
					match(ROW);
					}
				}

				setState(1962);
				match(POLICIES);
				setState(1965);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ON) {
					{
					setState(1963);
					match(ON);
					setState(1964);
					tableIdentifier();
					}
				}

				}
				break;
			case 20:
				_localctx = new ShowQuotasStmtContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(1967);
				match(SHOW);
				setState(1968);
				match(QUOTAS);
				}
				break;
			case 21:
				_localctx = new ShowQuotaStmtContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(1969);
				match(SHOW);
				setState(1971);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CURRENT) {
					{
					setState(1970);
					match(CURRENT);
					}
				}

				setState(1973);
				match(QUOTA);
				}
				break;
			case 22:
				_localctx = new ShowAccessStmtContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(1974);
				match(SHOW);
				setState(1975);
				match(ACCESS);
				}
				break;
			case 23:
				_localctx = new ShowClusterStmtContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(1976);
				match(SHOW);
				setState(1977);
				match(CLUSTER);
				setState(1978);
				stringLiteral();
				}
				break;
			case 24:
				_localctx = new ShowClustersStmtContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(1979);
				match(SHOW);
				setState(1980);
				match(CLUSTERS);
				setState(1986);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (ILIKE - 96)) | (1L << (LIKE - 96)) | (1L << (NOT - 96)))) != 0)) {
					{
					setState(1982);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(1981);
						match(NOT);
						}
					}

					setState(1984);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1985);
					stringLiteral();
					}
				}

				setState(1990);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LIMIT) {
					{
					setState(1988);
					match(LIMIT);
					setState(1989);
					match(DECIMAL_LITERAL);
					}
				}

				}
				break;
			case 25:
				_localctx = new ShowSettingsStmtContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(1992);
				match(SHOW);
				setState(1994);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CHANGED) {
					{
					setState(1993);
					match(CHANGED);
					}
				}

				setState(1996);
				match(SETTINGS);
				setState(1997);
				_la = _input.LA(1);
				if ( !(_la==ILIKE || _la==LIKE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1998);
				stringLiteral();
				}
				break;
			case 26:
				_localctx = new ShowSettingStmtContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(1999);
				match(SHOW);
				setState(2000);
				match(SETTING);
				setState(2001);
				stringLiteral();
				}
				break;
			case 27:
				_localctx = new ShowFilesystemCachesContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(2002);
				match(SHOW);
				setState(2003);
				match(FILESYSTEM);
				setState(2004);
				match(CACHES);
				}
				break;
			case 28:
				_localctx = new ShowEnginesStmtContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(2005);
				match(SHOW);
				setState(2006);
				match(ENGINES);
				}
				break;
			case 29:
				_localctx = new ShowFunctionsStmtContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(2007);
				match(SHOW);
				setState(2008);
				match(FUNCTIONS);
				setState(2011);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ILIKE || _la==LIKE) {
					{
					setState(2009);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(2010);
					stringLiteral();
					}
				}

				}
				break;
			case 30:
				_localctx = new ShowMergesStmtContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(2013);
				match(SHOW);
				setState(2014);
				match(MERGES);
				setState(2020);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (ILIKE - 96)) | (1L << (LIKE - 96)) | (1L << (NOT - 96)))) != 0)) {
					{
					setState(2016);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(2015);
						match(NOT);
						}
					}

					setState(2018);
					_la = _input.LA(1);
					if ( !(_la==ILIKE || _la==LIKE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(2019);
					stringLiteral();
					}
				}

				setState(2024);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LIMIT) {
					{
					setState(2022);
					match(LIMIT);
					setState(2023);
					match(DECIMAL_LITERAL);
					}
				}

				}
				break;
			case 31:
				_localctx = new ShowPrivilegesStmtContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(2026);
				match(SHOW);
				setState(2027);
				match(PRIVILEGES);
				}
				break;
			case 32:
				_localctx = new ShowCreateTableStmtContext(_localctx);
				enterOuterAlt(_localctx, 32);
				{
				setState(2028);
				match(SHOW);
				setState(2029);
				match(CREATE);
				setState(2031);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TEMPORARY) {
					{
					setState(2030);
					match(TEMPORARY);
					}
				}

				setState(2033);
				match(TABLE);
				setState(2034);
				tableIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SystemStmtContext extends ParserRuleContext {
		public TerminalNode SYSTEM() { return getToken(ClickHouseParser.SYSTEM, 0); }
		public TerminalNode FLUSH() { return getToken(ClickHouseParser.FLUSH, 0); }
		public TerminalNode DISTRIBUTED() { return getToken(ClickHouseParser.DISTRIBUTED, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode LOGS() { return getToken(ClickHouseParser.LOGS, 0); }
		public TerminalNode RELOAD() { return getToken(ClickHouseParser.RELOAD, 0); }
		public TerminalNode DICTIONARIES() { return getToken(ClickHouseParser.DICTIONARIES, 0); }
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TerminalNode START() { return getToken(ClickHouseParser.START, 0); }
		public TerminalNode STOP() { return getToken(ClickHouseParser.STOP, 0); }
		public TerminalNode SENDS() { return getToken(ClickHouseParser.SENDS, 0); }
		public TerminalNode FETCHES() { return getToken(ClickHouseParser.FETCHES, 0); }
		public TerminalNode MERGES() { return getToken(ClickHouseParser.MERGES, 0); }
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public TerminalNode REPLICATED() { return getToken(ClickHouseParser.REPLICATED, 0); }
		public TerminalNode SYNC() { return getToken(ClickHouseParser.SYNC, 0); }
		public TerminalNode REPLICA() { return getToken(ClickHouseParser.REPLICA, 0); }
		public SystemStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_systemStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterSystemStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitSystemStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitSystemStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SystemStmtContext systemStmt() throws RecognitionException {
		SystemStmtContext _localctx = new SystemStmtContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_systemStmt);
		int _la;
		try {
			setState(2071);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,284,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2037);
				match(SYSTEM);
				setState(2038);
				match(FLUSH);
				setState(2039);
				match(DISTRIBUTED);
				setState(2040);
				tableIdentifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2041);
				match(SYSTEM);
				setState(2042);
				match(FLUSH);
				setState(2043);
				match(LOGS);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2044);
				match(SYSTEM);
				setState(2045);
				match(RELOAD);
				setState(2046);
				match(DICTIONARIES);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2047);
				match(SYSTEM);
				setState(2048);
				match(RELOAD);
				setState(2049);
				match(DICTIONARY);
				setState(2050);
				tableIdentifier();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2051);
				match(SYSTEM);
				setState(2052);
				_la = _input.LA(1);
				if ( !(_la==START || _la==STOP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(2060);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DISTRIBUTED:
					{
					setState(2053);
					match(DISTRIBUTED);
					setState(2054);
					match(SENDS);
					}
					break;
				case FETCHES:
					{
					setState(2055);
					match(FETCHES);
					}
					break;
				case MERGES:
				case TTL:
					{
					setState(2057);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==TTL) {
						{
						setState(2056);
						match(TTL);
						}
					}

					setState(2059);
					match(MERGES);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(2062);
				tableIdentifier();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2063);
				match(SYSTEM);
				setState(2064);
				_la = _input.LA(1);
				if ( !(_la==START || _la==STOP) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(2065);
				match(REPLICATED);
				setState(2066);
				match(SENDS);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2067);
				match(SYSTEM);
				setState(2068);
				match(SYNC);
				setState(2069);
				match(REPLICA);
				setState(2070);
				tableIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TruncateStmtContext extends ParserRuleContext {
		public TerminalNode TRUNCATE() { return getToken(ClickHouseParser.TRUNCATE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public ClusterClauseContext clusterClause() {
			return getRuleContext(ClusterClauseContext.class,0);
		}
		public TruncateStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_truncateStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTruncateStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTruncateStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTruncateStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TruncateStmtContext truncateStmt() throws RecognitionException {
		TruncateStmtContext _localctx = new TruncateStmtContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_truncateStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2073);
			match(TRUNCATE);
			setState(2075);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,285,_ctx) ) {
			case 1:
				{
				setState(2074);
				match(TEMPORARY);
				}
				break;
			}
			setState(2078);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,286,_ctx) ) {
			case 1:
				{
				setState(2077);
				match(TABLE);
				}
				break;
			}
			setState(2082);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,287,_ctx) ) {
			case 1:
				{
				setState(2080);
				match(IF);
				setState(2081);
				match(EXISTS);
				}
				break;
			}
			setState(2084);
			tableIdentifier();
			setState(2086);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ON) {
				{
				setState(2085);
				clusterClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UseStmtContext extends ParserRuleContext {
		public TerminalNode USE() { return getToken(ClickHouseParser.USE, 0); }
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public UseStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_useStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterUseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitUseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitUseStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseStmtContext useStmt() throws RecognitionException {
		UseStmtContext _localctx = new UseStmtContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_useStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2088);
			match(USE);
			setState(2089);
			databaseIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WatchStmtContext extends ParserRuleContext {
		public TerminalNode WATCH() { return getToken(ClickHouseParser.WATCH, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode EVENTS() { return getToken(ClickHouseParser.EVENTS, 0); }
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public WatchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_watchStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterWatchStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitWatchStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitWatchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WatchStmtContext watchStmt() throws RecognitionException {
		WatchStmtContext _localctx = new WatchStmtContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_watchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2091);
			match(WATCH);
			setState(2092);
			tableIdentifier();
			setState(2094);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EVENTS) {
				{
				setState(2093);
				match(EVENTS);
				}
			}

			setState(2098);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(2096);
				match(LIMIT);
				setState(2097);
				match(DECIMAL_LITERAL);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnTypeExprContext extends ParserRuleContext {
		public ColumnTypeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnTypeExpr; }
	 
		public ColumnTypeExprContext() { }
		public void copyFrom(ColumnTypeExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ColumnTypeExprNestedContext extends ColumnTypeExprContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<ColumnTypeExprContext> columnTypeExpr() {
			return getRuleContexts(ColumnTypeExprContext.class);
		}
		public ColumnTypeExprContext columnTypeExpr(int i) {
			return getRuleContext(ColumnTypeExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnTypeExprNestedContext(ColumnTypeExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnTypeExprNested(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnTypeExprNested(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnTypeExprNested(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnTypeExprParamContext extends ColumnTypeExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ColumnTypeExprParamContext(ColumnTypeExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnTypeExprParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnTypeExprParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnTypeExprParam(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnTypeExprSimpleContext extends ColumnTypeExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColumnTypeExprSimpleContext(ColumnTypeExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnTypeExprSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnTypeExprSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnTypeExprSimple(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnTypeExprComplexContext extends ColumnTypeExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<ColumnTypeExprContext> columnTypeExpr() {
			return getRuleContexts(ColumnTypeExprContext.class);
		}
		public ColumnTypeExprContext columnTypeExpr(int i) {
			return getRuleContext(ColumnTypeExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnTypeExprComplexContext(ColumnTypeExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnTypeExprComplex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnTypeExprComplex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnTypeExprComplex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnTypeExprEnumContext extends ColumnTypeExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<EnumValueContext> enumValue() {
			return getRuleContexts(EnumValueContext.class);
		}
		public EnumValueContext enumValue(int i) {
			return getRuleContext(EnumValueContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnTypeExprEnumContext(ColumnTypeExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnTypeExprEnum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnTypeExprEnum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnTypeExprEnum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnTypeExprContext columnTypeExpr() throws RecognitionException {
		ColumnTypeExprContext _localctx = new ColumnTypeExprContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_columnTypeExpr);
		int _la;
		try {
			setState(2147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,295,_ctx) ) {
			case 1:
				_localctx = new ColumnTypeExprSimpleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(2100);
				identifier();
				}
				break;
			case 2:
				_localctx = new ColumnTypeExprNestedContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(2101);
				identifier();
				setState(2102);
				match(LPAREN);
				setState(2103);
				identifier();
				setState(2104);
				columnTypeExpr();
				setState(2111);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2105);
					match(COMMA);
					setState(2106);
					identifier();
					setState(2107);
					columnTypeExpr();
					}
					}
					setState(2113);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2114);
				match(RPAREN);
				}
				break;
			case 3:
				_localctx = new ColumnTypeExprEnumContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(2116);
				identifier();
				setState(2117);
				match(LPAREN);
				setState(2118);
				enumValue();
				setState(2123);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2119);
					match(COMMA);
					setState(2120);
					enumValue();
					}
					}
					setState(2125);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2126);
				match(RPAREN);
				}
				break;
			case 4:
				_localctx = new ColumnTypeExprComplexContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(2128);
				identifier();
				setState(2129);
				match(LPAREN);
				setState(2130);
				columnTypeExpr();
				setState(2135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2131);
					match(COMMA);
					setState(2132);
					columnTypeExpr();
					}
					}
					setState(2137);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2138);
				match(RPAREN);
				}
				break;
			case 5:
				_localctx = new ColumnTypeExprParamContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(2140);
				identifier();
				setState(2141);
				match(LPAREN);
				setState(2143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(2142);
					columnExprList();
					}
				}

				setState(2145);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnExprListContext extends ParserRuleContext {
		public List<ColumnsExprContext> columnsExpr() {
			return getRuleContexts(ColumnsExprContext.class);
		}
		public ColumnsExprContext columnsExpr(int i) {
			return getRuleContext(ColumnsExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnExprListContext columnExprList() throws RecognitionException {
		ColumnExprListContext _localctx = new ColumnExprListContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_columnExprList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2149);
			columnsExpr();
			setState(2154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,296,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(2150);
					match(COMMA);
					setState(2151);
					columnsExpr();
					}
					} 
				}
				setState(2156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,296,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnsExprContext extends ParserRuleContext {
		public ColumnsExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnsExpr; }
	 
		public ColumnsExprContext() { }
		public void copyFrom(ColumnsExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ColumnsExprColumnContext extends ColumnsExprContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public ColumnsExprColumnContext(ColumnsExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnsExprColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnsExprColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnsExprColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnsExprAsteriskContext extends ColumnsExprContext {
		public TerminalNode ASTERISK() { return getToken(ClickHouseParser.ASTERISK, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public ColumnExceptExprContext columnExceptExpr() {
			return getRuleContext(ColumnExceptExprContext.class,0);
		}
		public ColumnsExprAsteriskContext(ColumnsExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnsExprAsterisk(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnsExprAsterisk(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnsExprAsterisk(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnsExprSubqueryContext extends ColumnsExprContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnsExprSubqueryContext(ColumnsExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnsExprSubquery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnsExprSubquery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnsExprSubquery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnsExprContext columnsExpr() throws RecognitionException {
		ColumnsExprContext _localctx = new ColumnsExprContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_columnsExpr);
		int _la;
		try {
			setState(2171);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,299,_ctx) ) {
			case 1:
				_localctx = new ColumnsExprAsteriskContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(2160);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (IDENTIFIER - 192)))) != 0)) {
					{
					setState(2157);
					tableIdentifier();
					setState(2158);
					match(DOT);
					}
				}

				setState(2162);
				match(ASTERISK);
				setState(2164);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,298,_ctx) ) {
				case 1:
					{
					setState(2163);
					columnExceptExpr();
					}
					break;
				}
				}
				break;
			case 2:
				_localctx = new ColumnsExprSubqueryContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(2166);
				match(LPAREN);
				setState(2167);
				selectUnionStmt();
				setState(2168);
				match(RPAREN);
				}
				break;
			case 3:
				_localctx = new ColumnsExprColumnContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(2170);
				columnExpr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnExprContext extends ParserRuleContext {
		public ColumnExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnExpr; }
	 
		public ColumnExprContext() { }
		public void copyFrom(ColumnExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ColumnExprTernaryOpContext extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode QUERY() { return getToken(ClickHouseParser.QUERY, 0); }
		public TerminalNode COLON() { return getToken(ClickHouseParser.COLON, 0); }
		public ColumnExprTernaryOpContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprTernaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprTernaryOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprTernaryOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprAliasContext extends ColumnExprContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColumnExprAliasContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprAlias(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprExtractContext extends ColumnExprContext {
		public TerminalNode EXTRACT() { return getToken(ClickHouseParser.EXTRACT, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public IntervalContext interval() {
			return getRuleContext(IntervalContext.class,0);
		}
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprExtractContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprExtract(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprExtract(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprExtract(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprNegateContext extends ColumnExprContext {
		public TerminalNode DASH() { return getToken(ClickHouseParser.DASH, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public ColumnExprNegateContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprNegate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprNegate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprNegate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprSubqueryContext extends ColumnExprContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprSubqueryContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprSubquery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprSubquery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprSubquery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprLiteralContext extends ColumnExprContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ColumnExprLiteralContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprArrayContext extends ColumnExprContext {
		public TerminalNode LBRACKET() { return getToken(ClickHouseParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(ClickHouseParser.RBRACKET, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ColumnExprArrayContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprArray(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprSubstringContext extends ColumnExprContext {
		public TerminalNode SUBSTRING() { return getToken(ClickHouseParser.SUBSTRING, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TerminalNode FOR() { return getToken(ClickHouseParser.FOR, 0); }
		public ColumnExprSubstringContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprSubstring(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprSubstring(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprSubstring(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprCastContext extends ColumnExprContext {
		public TerminalNode CAST() { return getToken(ClickHouseParser.CAST, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprCastContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprCast(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprCast(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprCast(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprOrContext extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode OR() { return getToken(ClickHouseParser.OR, 0); }
		public ColumnExprOrContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprCastSymbolContext extends ColumnExprContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode DOUBLE_COLON() { return getToken(ClickHouseParser.DOUBLE_COLON, 0); }
		public ColumnTypeExprContext columnTypeExpr() {
			return getRuleContext(ColumnTypeExprContext.class,0);
		}
		public ColumnExprCastSymbolContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprCastSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprCastSymbol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprCastSymbol(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprPrecedence1Context extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode ASTERISK() { return getToken(ClickHouseParser.ASTERISK, 0); }
		public TerminalNode SLASH() { return getToken(ClickHouseParser.SLASH, 0); }
		public TerminalNode PERCENT() { return getToken(ClickHouseParser.PERCENT, 0); }
		public ColumnExprPrecedence1Context(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprPrecedence1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprPrecedence1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprPrecedence1(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprPrecedence2Context extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(ClickHouseParser.PLUS, 0); }
		public TerminalNode DASH() { return getToken(ClickHouseParser.DASH, 0); }
		public TerminalNode CONCAT() { return getToken(ClickHouseParser.CONCAT, 0); }
		public ColumnExprPrecedence2Context(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprPrecedence2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprPrecedence2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprPrecedence2(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprPrecedence3Context extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode EQ_DOUBLE() { return getToken(ClickHouseParser.EQ_DOUBLE, 0); }
		public TerminalNode EQ_SINGLE() { return getToken(ClickHouseParser.EQ_SINGLE, 0); }
		public TerminalNode NOT_EQ() { return getToken(ClickHouseParser.NOT_EQ, 0); }
		public TerminalNode LE() { return getToken(ClickHouseParser.LE, 0); }
		public TerminalNode GE() { return getToken(ClickHouseParser.GE, 0); }
		public TerminalNode LT() { return getToken(ClickHouseParser.LT, 0); }
		public TerminalNode GT() { return getToken(ClickHouseParser.GT, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode GLOBAL() { return getToken(ClickHouseParser.GLOBAL, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ColumnExprPrecedence3Context(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprPrecedence3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprPrecedence3(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprPrecedence3(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprIntervalContext extends ColumnExprContext {
		public TerminalNode INTERVAL() { return getToken(ClickHouseParser.INTERVAL, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public IntervalContext interval() {
			return getRuleContext(IntervalContext.class,0);
		}
		public ColumnExprIntervalContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprInterval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprInterval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprInterval(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprIsNullContext extends ColumnExprContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode IS() { return getToken(ClickHouseParser.IS, 0); }
		public TerminalNode NULL_SQL() { return getToken(ClickHouseParser.NULL_SQL, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ColumnExprIsNullContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprIsNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprIsNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprIsNull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprWinFunctionTargetContext extends ColumnExprContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode OVER() { return getToken(ClickHouseParser.OVER, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ColumnExprWinFunctionTargetContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprWinFunctionTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprWinFunctionTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprWinFunctionTarget(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprTrimContext extends ColumnExprContext {
		public TerminalNode TRIM() { return getToken(ClickHouseParser.TRIM, 0); }
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TerminalNode BOTH() { return getToken(ClickHouseParser.BOTH, 0); }
		public TerminalNode LEADING() { return getToken(ClickHouseParser.LEADING, 0); }
		public TerminalNode TRAILING() { return getToken(ClickHouseParser.TRAILING, 0); }
		public ColumnExprTrimContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprTrim(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprTrim(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprTrim(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprTupleContext extends ColumnExprContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ColumnExprTupleContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprTuple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprTuple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprTuple(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprArrayAccessContext extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode LBRACKET() { return getToken(ClickHouseParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(ClickHouseParser.RBRACKET, 0); }
		public ColumnExprArrayAccessContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprArrayAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprArrayAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprArrayAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprBetweenContext extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode BETWEEN() { return getToken(ClickHouseParser.BETWEEN, 0); }
		public TerminalNode AND() { return getToken(ClickHouseParser.AND, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ColumnExprBetweenContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprBetween(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprBetween(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprBetween(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprParensContext extends ColumnExprContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExprParensContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprTimestampContext extends ColumnExprContext {
		public TerminalNode TIMESTAMP() { return getToken(ClickHouseParser.TIMESTAMP, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public ColumnExprTimestampContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprTimestamp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprTimestamp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprAndContext extends ColumnExprContext {
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(ClickHouseParser.AND, 0); }
		public ColumnExprAndContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprTupleAccessContext extends ColumnExprContext {
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColumnExprTupleAccessContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprTupleAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprTupleAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprTupleAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprCaseContext extends ColumnExprContext {
		public TerminalNode CASE() { return getToken(ClickHouseParser.CASE, 0); }
		public TerminalNode END() { return getToken(ClickHouseParser.END, 0); }
		public List<ColumnExprContext> columnExpr() {
			return getRuleContexts(ColumnExprContext.class);
		}
		public ColumnExprContext columnExpr(int i) {
			return getRuleContext(ColumnExprContext.class,i);
		}
		public List<TerminalNode> WHEN() { return getTokens(ClickHouseParser.WHEN); }
		public TerminalNode WHEN(int i) {
			return getToken(ClickHouseParser.WHEN, i);
		}
		public List<TerminalNode> THEN() { return getTokens(ClickHouseParser.THEN); }
		public TerminalNode THEN(int i) {
			return getToken(ClickHouseParser.THEN, i);
		}
		public TerminalNode ELSE() { return getToken(ClickHouseParser.ELSE, 0); }
		public ColumnExprCaseContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprCase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprDateContext extends ColumnExprContext {
		public TerminalNode DATE() { return getToken(ClickHouseParser.DATE, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public ColumnExprDateContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprDate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprDate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprDate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprNotContext extends ColumnExprContext {
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public ColumnExprNotContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprWinFunctionContext extends ColumnExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode OVER() { return getToken(ClickHouseParser.OVER, 0); }
		public List<TerminalNode> LPAREN() { return getTokens(ClickHouseParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(ClickHouseParser.LPAREN, i);
		}
		public WindowExprContext windowExpr() {
			return getRuleContext(WindowExprContext.class,0);
		}
		public List<TerminalNode> RPAREN() { return getTokens(ClickHouseParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(ClickHouseParser.RPAREN, i);
		}
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ColumnExprWinFunctionContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprWinFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprWinFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprWinFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprIdentifierContext extends ColumnExprContext {
		public ColumnIdentifierContext columnIdentifier() {
			return getRuleContext(ColumnIdentifierContext.class,0);
		}
		public ColumnExprIdentifierContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprFunctionContext extends ColumnExprContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TerminalNode> LPAREN() { return getTokens(ClickHouseParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(ClickHouseParser.LPAREN, i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(ClickHouseParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(ClickHouseParser.RPAREN, i);
		}
		public TerminalNode DISTINCT() { return getToken(ClickHouseParser.DISTINCT, 0); }
		public ColumnArgListContext columnArgList() {
			return getRuleContext(ColumnArgListContext.class,0);
		}
		public ColumnExprListContext columnExprList() {
			return getRuleContext(ColumnExprListContext.class,0);
		}
		public ColumnExprFunctionContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExprAsteriskContext extends ColumnExprContext {
		public TerminalNode ASTERISK() { return getToken(ClickHouseParser.ASTERISK, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public ColumnExprAsteriskContext(ColumnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExprAsterisk(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExprAsterisk(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExprAsterisk(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnExprContext columnExpr() throws RecognitionException {
		return columnExpr(0);
	}

	private ColumnExprContext columnExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ColumnExprContext _localctx = new ColumnExprContext(_ctx, _parentState);
		ColumnExprContext _prevctx = _localctx;
		int _startState = 214;
		enterRecursionRule(_localctx, 214, RULE_columnExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,313,_ctx) ) {
			case 1:
				{
				_localctx = new ColumnExprCaseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(2174);
				match(CASE);
				setState(2176);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,300,_ctx) ) {
				case 1:
					{
					setState(2175);
					columnExpr(0);
					}
					break;
				}
				setState(2183); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(2178);
					match(WHEN);
					setState(2179);
					columnExpr(0);
					setState(2180);
					match(THEN);
					setState(2181);
					columnExpr(0);
					}
					}
					setState(2185); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==WHEN );
				setState(2189);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(2187);
					match(ELSE);
					setState(2188);
					columnExpr(0);
					}
				}

				setState(2191);
				match(END);
				}
				break;
			case 2:
				{
				_localctx = new ColumnExprCastContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2193);
				match(CAST);
				setState(2194);
				match(LPAREN);
				setState(2195);
				columnExpr(0);
				setState(2196);
				match(AS);
				setState(2197);
				columnTypeExpr();
				setState(2198);
				match(RPAREN);
				}
				break;
			case 3:
				{
				_localctx = new ColumnExprDateContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2200);
				match(DATE);
				setState(2201);
				stringLiteral();
				}
				break;
			case 4:
				{
				_localctx = new ColumnExprExtractContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2202);
				match(EXTRACT);
				setState(2203);
				match(LPAREN);
				setState(2204);
				interval();
				setState(2205);
				match(FROM);
				setState(2206);
				columnExpr(0);
				setState(2207);
				match(RPAREN);
				}
				break;
			case 5:
				{
				_localctx = new ColumnExprIntervalContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2209);
				match(INTERVAL);
				setState(2210);
				columnExpr(0);
				setState(2211);
				interval();
				}
				break;
			case 6:
				{
				_localctx = new ColumnExprSubstringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2213);
				match(SUBSTRING);
				setState(2214);
				match(LPAREN);
				setState(2215);
				columnExpr(0);
				setState(2216);
				match(FROM);
				setState(2217);
				columnExpr(0);
				setState(2220);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FOR) {
					{
					setState(2218);
					match(FOR);
					setState(2219);
					columnExpr(0);
					}
				}

				setState(2222);
				match(RPAREN);
				}
				break;
			case 7:
				{
				_localctx = new ColumnExprTimestampContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2224);
				match(TIMESTAMP);
				setState(2225);
				stringLiteral();
				}
				break;
			case 8:
				{
				_localctx = new ColumnExprTrimContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2226);
				match(TRIM);
				setState(2227);
				match(LPAREN);
				setState(2228);
				_la = _input.LA(1);
				if ( !(_la==BOTH || _la==LEADING || _la==TRAILING) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(2229);
				stringLiteral();
				setState(2230);
				match(FROM);
				setState(2231);
				columnExpr(0);
				setState(2232);
				match(RPAREN);
				}
				break;
			case 9:
				{
				_localctx = new ColumnExprNotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2234);
				match(NOT);
				setState(2235);
				columnExpr(22);
				}
				break;
			case 10:
				{
				_localctx = new ColumnExprWinFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2236);
				identifier();
				{
				setState(2237);
				match(LPAREN);
				setState(2239);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(2238);
					columnExprList();
					}
				}

				setState(2241);
				match(RPAREN);
				}
				setState(2243);
				match(OVER);
				setState(2244);
				match(LPAREN);
				setState(2245);
				windowExpr();
				setState(2246);
				match(RPAREN);
				}
				break;
			case 11:
				{
				_localctx = new ColumnExprWinFunctionTargetContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2248);
				identifier();
				{
				setState(2249);
				match(LPAREN);
				setState(2251);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(2250);
					columnExprList();
					}
				}

				setState(2253);
				match(RPAREN);
				}
				setState(2255);
				match(OVER);
				setState(2256);
				identifier();
				}
				break;
			case 12:
				{
				_localctx = new ColumnExprFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2258);
				identifier();
				setState(2264);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,307,_ctx) ) {
				case 1:
					{
					setState(2259);
					match(LPAREN);
					setState(2261);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
						{
						setState(2260);
						columnExprList();
						}
					}

					setState(2263);
					match(RPAREN);
					}
					break;
				}
				setState(2266);
				match(LPAREN);
				setState(2268);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,308,_ctx) ) {
				case 1:
					{
					setState(2267);
					match(DISTINCT);
					}
					break;
				}
				setState(2271);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(2270);
					columnArgList();
					}
				}

				setState(2273);
				match(RPAREN);
				}
				break;
			case 13:
				{
				_localctx = new ColumnExprLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2275);
				literal();
				}
				break;
			case 14:
				{
				_localctx = new ColumnExprNegateContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2276);
				match(DASH);
				setState(2277);
				columnExpr(15);
				}
				break;
			case 15:
				{
				_localctx = new ColumnExprAsteriskContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2281);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (IDENTIFIER - 192)))) != 0)) {
					{
					setState(2278);
					tableIdentifier();
					setState(2279);
					match(DOT);
					}
				}

				setState(2283);
				match(ASTERISK);
				}
				break;
			case 16:
				{
				_localctx = new ColumnExprSubqueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2284);
				match(LPAREN);
				setState(2285);
				selectUnionStmt();
				setState(2286);
				match(RPAREN);
				}
				break;
			case 17:
				{
				_localctx = new ColumnExprParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2288);
				match(LPAREN);
				setState(2289);
				columnExpr(0);
				setState(2290);
				match(RPAREN);
				}
				break;
			case 18:
				{
				_localctx = new ColumnExprTupleContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2292);
				match(LPAREN);
				setState(2294);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(2293);
					columnExprList();
					}
				}

				setState(2296);
				match(RPAREN);
				}
				break;
			case 19:
				{
				_localctx = new ColumnExprArrayContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2297);
				match(LBRACKET);
				setState(2299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)) | (1L << (ASTERISK - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (LBRACKET - 257)) | (1L << (LPAREN - 257)) | (1L << (PLUS - 257)))) != 0)) {
					{
					setState(2298);
					columnExprList();
					}
				}

				setState(2301);
				match(RBRACKET);
				}
				break;
			case 20:
				{
				_localctx = new ColumnExprIdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2302);
				columnIdentifier();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(2383);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,323,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(2381);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,322,_ctx) ) {
					case 1:
						{
						_localctx = new ColumnExprBetweenContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2305);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(2307);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==NOT) {
							{
							setState(2306);
							match(NOT);
							}
						}

						setState(2309);
						match(BETWEEN);
						setState(2310);
						columnExpr(0);
						setState(2311);
						match(AND);
						setState(2312);
						columnExpr(24);
						}
						break;
					case 2:
						{
						_localctx = new ColumnExprPrecedence1Context(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2314);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(2315);
						_la = _input.LA(1);
						if ( !(((((_la - 250)) & ~0x3f) == 0 && ((1L << (_la - 250)) & ((1L << (ASTERISK - 250)) | (1L << (PERCENT - 250)) | (1L << (SLASH - 250)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(2316);
						columnExpr(15);
						}
						break;
					case 3:
						{
						_localctx = new ColumnExprPrecedence2Context(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2317);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(2318);
						_la = _input.LA(1);
						if ( !(((((_la - 256)) & ~0x3f) == 0 && ((1L << (_la - 256)) & ((1L << (CONCAT - 256)) | (1L << (DASH - 256)) | (1L << (PLUS - 256)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(2319);
						columnExpr(14);
						}
						break;
					case 4:
						{
						_localctx = new ColumnExprPrecedence3Context(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2320);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(2339);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,318,_ctx) ) {
						case 1:
							{
							setState(2321);
							match(EQ_DOUBLE);
							}
							break;
						case 2:
							{
							setState(2322);
							match(EQ_SINGLE);
							}
							break;
						case 3:
							{
							setState(2323);
							match(NOT_EQ);
							}
							break;
						case 4:
							{
							setState(2324);
							match(LE);
							}
							break;
						case 5:
							{
							setState(2325);
							match(GE);
							}
							break;
						case 6:
							{
							setState(2326);
							match(LT);
							}
							break;
						case 7:
							{
							setState(2327);
							match(GT);
							}
							break;
						case 8:
							{
							setState(2329);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==GLOBAL) {
								{
								setState(2328);
								match(GLOBAL);
								}
							}

							setState(2332);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==NOT) {
								{
								setState(2331);
								match(NOT);
								}
							}

							setState(2334);
							match(IN);
							}
							break;
						case 9:
							{
							setState(2336);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==NOT) {
								{
								setState(2335);
								match(NOT);
								}
							}

							setState(2338);
							_la = _input.LA(1);
							if ( !(_la==ILIKE || _la==LIKE) ) {
							_errHandler.recoverInline(this);
							}
							else {
								if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
								_errHandler.reportMatch(this);
								consume();
							}
							}
							break;
						}
						setState(2341);
						columnExpr(13);
						}
						break;
					case 5:
						{
						_localctx = new ColumnExprAndContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2342);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(2343);
						match(AND);
						setState(2344);
						columnExpr(11);
						}
						break;
					case 6:
						{
						_localctx = new ColumnExprOrContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2345);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(2346);
						match(OR);
						setState(2347);
						columnExpr(10);
						}
						break;
					case 7:
						{
						_localctx = new ColumnExprTernaryOpContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2348);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(2349);
						match(QUERY);
						setState(2350);
						columnExpr(0);
						setState(2351);
						match(COLON);
						setState(2352);
						columnExpr(8);
						}
						break;
					case 8:
						{
						_localctx = new ColumnExprCastSymbolContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2354);
						if (!(precpred(_ctx, 30))) throw new FailedPredicateException(this, "precpred(_ctx, 30)");
						setState(2355);
						match(DOUBLE_COLON);
						setState(2356);
						columnTypeExpr();
						}
						break;
					case 9:
						{
						_localctx = new ColumnExprArrayAccessContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2357);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(2358);
						match(LBRACKET);
						setState(2359);
						columnExpr(0);
						setState(2360);
						match(RBRACKET);
						}
						break;
					case 10:
						{
						_localctx = new ColumnExprTupleAccessContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2362);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(2363);
						match(DOT);
						setState(2367);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case DECIMAL_LITERAL:
							{
							setState(2364);
							match(DECIMAL_LITERAL);
							}
							break;
						case HEXADECIMAL_STRING_LITERAL:
						case BINARY_STRING_LITERAL:
						case STRING_LITERAL:
							{
							setState(2365);
							stringLiteral();
							}
							break;
						case ACCESS:
						case ADD:
						case AFTER:
						case ALIAS:
						case ALL:
						case ALTER:
						case AND:
						case ANTI:
						case ANY:
						case ARRAY:
						case AS:
						case ASCENDING:
						case ASOF:
						case AST:
						case ASYNC:
						case ATTACH:
						case BETWEEN:
						case BOTH:
						case BY:
						case CACHES:
						case CASE:
						case CAST:
						case CHECK:
						case CLEAR:
						case CLUSTER:
						case CLUSTERS:
						case CODEC:
						case COLLATE:
						case COLUMN:
						case COLUMNS:
						case COMMENT:
						case CONSTRAINT:
						case CREATE:
						case CROSS:
						case CUBE:
						case CURRENT:
						case CURRENT_USER:
						case CHANGED:
						case DATABASE:
						case DATABASES:
						case DATE:
						case DAY:
						case DEDUPLICATE:
						case DEFAULT:
						case DELAY:
						case DELETE:
						case DESC:
						case DESCENDING:
						case DESCRIBE:
						case DETACH:
						case DICTIONARIES:
						case DICTIONARY:
						case DISK:
						case DISTINCT:
						case DISTRIBUTED:
						case DROP:
						case ELSE:
						case ENABLED:
						case END:
						case ENGINE:
						case ENGINES:
						case ESTIMATE:
						case EVENTS:
						case EXCEPT:
						case EXISTS:
						case EXPLAIN:
						case EXPRESSION:
						case EXTENDED:
						case EXTRACT:
						case FETCHES:
						case FIELDS:
						case FILESYSTEM:
						case FILL:
						case FINAL:
						case FIRST:
						case FLUSH:
						case FOLLOWING:
						case FOR:
						case FORMAT:
						case FREEZE:
						case FROM:
						case FULL:
						case FUNCTION:
						case FUNCTIONS:
						case GLOBAL:
						case GRANULARITY:
						case GRANTS:
						case GROUP:
						case GROUPING:
						case HAVING:
						case HIERARCHICAL:
						case HOUR:
						case ID:
						case IF:
						case ILIKE:
						case IMPLICIT:
						case IN:
						case INDEX:
						case INDEXES:
						case INDICES:
						case INJECTIVE:
						case INNER:
						case INSERT:
						case INTERPOLATE:
						case INTERVAL:
						case INTO:
						case IS:
						case IS_OBJECT_ID:
						case JOIN:
						case KEY:
						case KEYS:
						case KILL:
						case LAST:
						case LAYOUT:
						case LEADING:
						case LEFT:
						case LIFETIME:
						case LIKE:
						case LIMIT:
						case LIVE:
						case LOCAL:
						case LOGS:
						case MATERIALIZE:
						case MATERIALIZED:
						case MAX:
						case MERGES:
						case MICROSECOND:
						case MILLISECOND:
						case MIN:
						case MINUTE:
						case MODIFY:
						case MONTH:
						case MOVE:
						case MUTATION:
						case NANOSECOND:
						case NO:
						case NOT:
						case NULLS:
						case OFFSET:
						case ON:
						case OPTIMIZE:
						case OR:
						case ORDER:
						case OUTER:
						case OUTFILE:
						case OVER:
						case OVERRIDE:
						case PARTITION:
						case PIPELINE:
						case PLAN:
						case POLICY:
						case POLICIES:
						case POPULATE:
						case PRECEDING:
						case PREWHERE:
						case PRIMARY:
						case PRIVILEGES:
						case PROCESSLIST:
						case PROFILE:
						case PROFILES:
						case PROJECTION:
						case QUARTER:
						case QUOTA:
						case QUOTAS:
						case RANGE:
						case RECURSIVE:
						case RELOAD:
						case REMOVE:
						case RENAME:
						case REPLACE:
						case REPLICA:
						case REPLICATED:
						case RIGHT:
						case ROLE:
						case ROLES:
						case ROLLUP:
						case ROW:
						case ROWS:
						case SAMPLE:
						case SECOND:
						case SELECT:
						case SEMI:
						case SENDS:
						case SET:
						case SETTING:
						case SETTINGS:
						case SHOW:
						case SOURCE:
						case START:
						case STOP:
						case SUBSTRING:
						case SYNC:
						case SYNTAX:
						case SYSTEM:
						case STEP:
						case TABLE:
						case TABLES:
						case TEMPORARY:
						case TEST:
						case THEN:
						case TIES:
						case TIMEOUT:
						case TIMESTAMP:
						case TO:
						case TOP:
						case TOTALS:
						case TRAILING:
						case TREE:
						case TRIM:
						case TRUNCATE:
						case TTL:
						case TYPE:
						case UNBOUNDED:
						case UNION:
						case UPDATE:
						case USE:
						case USER:
						case USERS:
						case USING:
						case UUID:
						case VALUES:
						case VIEW:
						case VOLUME:
						case WATCH:
						case WEEK:
						case WHEN:
						case WHERE:
						case WINDOW:
						case WITH:
						case YEAR:
						case JSON_FALSE:
						case JSON_TRUE:
						case IDENTIFIER:
							{
							setState(2366);
							identifier();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					case 11:
						{
						_localctx = new ColumnExprIsNullContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2369);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(2370);
						match(IS);
						setState(2372);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==NOT) {
							{
							setState(2371);
							match(NOT);
							}
						}

						setState(2374);
						match(NULL_SQL);
						}
						break;
					case 12:
						{
						_localctx = new ColumnExprAliasContext(new ColumnExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_columnExpr);
						setState(2375);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(2379);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case AFTER:
						case ALIAS:
						case ALTER:
						case AST:
						case ASYNC:
						case ATTACH:
						case BOTH:
						case CASE:
						case CAST:
						case CHECK:
						case CLEAR:
						case CLUSTER:
						case CODEC:
						case COLUMN:
						case COMMENT:
						case CONSTRAINT:
						case CREATE:
						case CUBE:
						case CURRENT:
						case DATABASE:
						case DATABASES:
						case DATE:
						case DEDUPLICATE:
						case DEFAULT:
						case DELAY:
						case DESCRIBE:
						case DETACH:
						case DICTIONARIES:
						case DICTIONARY:
						case DISK:
						case DISTRIBUTED:
						case DROP:
						case ENGINE:
						case EVENTS:
						case EXISTS:
						case EXPLAIN:
						case EXPRESSION:
						case EXTRACT:
						case FETCHES:
						case FLUSH:
						case FOLLOWING:
						case FREEZE:
						case FUNCTION:
						case GRANULARITY:
						case HIERARCHICAL:
						case ID:
						case IF:
						case INDEX:
						case INJECTIVE:
						case INSERT:
						case IS_OBJECT_ID:
						case KEY:
						case KILL:
						case LAYOUT:
						case LEADING:
						case LIFETIME:
						case LIVE:
						case LOGS:
						case MATERIALIZE:
						case MATERIALIZED:
						case MAX:
						case MERGES:
						case MIN:
						case MODIFY:
						case MOVE:
						case MUTATION:
						case NO:
						case OPTIMIZE:
						case OUTFILE:
						case OVER:
						case PARTITION:
						case POPULATE:
						case PRECEDING:
						case PRIMARY:
						case RANGE:
						case RELOAD:
						case REMOVE:
						case RENAME:
						case REPLACE:
						case REPLICA:
						case REPLICATED:
						case ROLLUP:
						case ROW:
						case SELECT:
						case SENDS:
						case SET:
						case SHOW:
						case SOURCE:
						case START:
						case STOP:
						case SUBSTRING:
						case SYNC:
						case SYNTAX:
						case SYSTEM:
						case TABLE:
						case TABLES:
						case TEMPORARY:
						case TEST:
						case TIES:
						case TIMEOUT:
						case TIMESTAMP:
						case TOTALS:
						case TRAILING:
						case TRIM:
						case TRUNCATE:
						case TTL:
						case TYPE:
						case UNBOUNDED:
						case UPDATE:
						case USE:
						case UUID:
						case VALUES:
						case VIEW:
						case VOLUME:
						case WATCH:
						case IDENTIFIER:
							{
							setState(2376);
							alias();
							}
							break;
						case AS:
							{
							setState(2377);
							match(AS);
							setState(2378);
							identifier();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					}
					} 
				}
				setState(2385);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,323,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ColumnArgListContext extends ParserRuleContext {
		public List<ColumnArgExprContext> columnArgExpr() {
			return getRuleContexts(ColumnArgExprContext.class);
		}
		public ColumnArgExprContext columnArgExpr(int i) {
			return getRuleContext(ColumnArgExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnArgListContext columnArgList() throws RecognitionException {
		ColumnArgListContext _localctx = new ColumnArgListContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_columnArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2386);
			columnArgExpr();
			setState(2391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(2387);
				match(COMMA);
				setState(2388);
				columnArgExpr();
				}
				}
				setState(2393);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnArgExprContext extends ParserRuleContext {
		public ColumnLambdaExprContext columnLambdaExpr() {
			return getRuleContext(ColumnLambdaExprContext.class,0);
		}
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public ColumnArgExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnArgExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnArgExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnArgExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnArgExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnArgExprContext columnArgExpr() throws RecognitionException {
		ColumnArgExprContext _localctx = new ColumnArgExprContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_columnArgExpr);
		try {
			setState(2396);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,325,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2394);
				columnLambdaExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2395);
				columnExpr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnLambdaExprContext extends ParserRuleContext {
		public TerminalNode ARROW() { return getToken(ClickHouseParser.ARROW, 0); }
		public ColumnExprContext columnExpr() {
			return getRuleContext(ColumnExprContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnLambdaExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnLambdaExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnLambdaExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnLambdaExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnLambdaExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnLambdaExprContext columnLambdaExpr() throws RecognitionException {
		ColumnLambdaExprContext _localctx = new ColumnLambdaExprContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_columnLambdaExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2417);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				{
				setState(2398);
				match(LPAREN);
				setState(2399);
				identifier();
				setState(2404);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2400);
					match(COMMA);
					setState(2401);
					identifier();
					}
					}
					setState(2406);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2407);
				match(RPAREN);
				}
				break;
			case ACCESS:
			case ADD:
			case AFTER:
			case ALIAS:
			case ALL:
			case ALTER:
			case AND:
			case ANTI:
			case ANY:
			case ARRAY:
			case AS:
			case ASCENDING:
			case ASOF:
			case AST:
			case ASYNC:
			case ATTACH:
			case BETWEEN:
			case BOTH:
			case BY:
			case CACHES:
			case CASE:
			case CAST:
			case CHECK:
			case CLEAR:
			case CLUSTER:
			case CLUSTERS:
			case CODEC:
			case COLLATE:
			case COLUMN:
			case COLUMNS:
			case COMMENT:
			case CONSTRAINT:
			case CREATE:
			case CROSS:
			case CUBE:
			case CURRENT:
			case CURRENT_USER:
			case CHANGED:
			case DATABASE:
			case DATABASES:
			case DATE:
			case DAY:
			case DEDUPLICATE:
			case DEFAULT:
			case DELAY:
			case DELETE:
			case DESC:
			case DESCENDING:
			case DESCRIBE:
			case DETACH:
			case DICTIONARIES:
			case DICTIONARY:
			case DISK:
			case DISTINCT:
			case DISTRIBUTED:
			case DROP:
			case ELSE:
			case ENABLED:
			case END:
			case ENGINE:
			case ENGINES:
			case ESTIMATE:
			case EVENTS:
			case EXCEPT:
			case EXISTS:
			case EXPLAIN:
			case EXPRESSION:
			case EXTENDED:
			case EXTRACT:
			case FETCHES:
			case FIELDS:
			case FILESYSTEM:
			case FILL:
			case FINAL:
			case FIRST:
			case FLUSH:
			case FOLLOWING:
			case FOR:
			case FORMAT:
			case FREEZE:
			case FROM:
			case FULL:
			case FUNCTION:
			case FUNCTIONS:
			case GLOBAL:
			case GRANULARITY:
			case GRANTS:
			case GROUP:
			case GROUPING:
			case HAVING:
			case HIERARCHICAL:
			case HOUR:
			case ID:
			case IF:
			case ILIKE:
			case IMPLICIT:
			case IN:
			case INDEX:
			case INDEXES:
			case INDICES:
			case INJECTIVE:
			case INNER:
			case INSERT:
			case INTERPOLATE:
			case INTERVAL:
			case INTO:
			case IS:
			case IS_OBJECT_ID:
			case JOIN:
			case KEY:
			case KEYS:
			case KILL:
			case LAST:
			case LAYOUT:
			case LEADING:
			case LEFT:
			case LIFETIME:
			case LIKE:
			case LIMIT:
			case LIVE:
			case LOCAL:
			case LOGS:
			case MATERIALIZE:
			case MATERIALIZED:
			case MAX:
			case MERGES:
			case MICROSECOND:
			case MILLISECOND:
			case MIN:
			case MINUTE:
			case MODIFY:
			case MONTH:
			case MOVE:
			case MUTATION:
			case NANOSECOND:
			case NO:
			case NOT:
			case NULLS:
			case OFFSET:
			case ON:
			case OPTIMIZE:
			case OR:
			case ORDER:
			case OUTER:
			case OUTFILE:
			case OVER:
			case OVERRIDE:
			case PARTITION:
			case PIPELINE:
			case PLAN:
			case POLICY:
			case POLICIES:
			case POPULATE:
			case PRECEDING:
			case PREWHERE:
			case PRIMARY:
			case PRIVILEGES:
			case PROCESSLIST:
			case PROFILE:
			case PROFILES:
			case PROJECTION:
			case QUARTER:
			case QUOTA:
			case QUOTAS:
			case RANGE:
			case RECURSIVE:
			case RELOAD:
			case REMOVE:
			case RENAME:
			case REPLACE:
			case REPLICA:
			case REPLICATED:
			case RIGHT:
			case ROLE:
			case ROLES:
			case ROLLUP:
			case ROW:
			case ROWS:
			case SAMPLE:
			case SECOND:
			case SELECT:
			case SEMI:
			case SENDS:
			case SET:
			case SETTING:
			case SETTINGS:
			case SHOW:
			case SOURCE:
			case START:
			case STOP:
			case SUBSTRING:
			case SYNC:
			case SYNTAX:
			case SYSTEM:
			case STEP:
			case TABLE:
			case TABLES:
			case TEMPORARY:
			case TEST:
			case THEN:
			case TIES:
			case TIMEOUT:
			case TIMESTAMP:
			case TO:
			case TOP:
			case TOTALS:
			case TRAILING:
			case TREE:
			case TRIM:
			case TRUNCATE:
			case TTL:
			case TYPE:
			case UNBOUNDED:
			case UNION:
			case UPDATE:
			case USE:
			case USER:
			case USERS:
			case USING:
			case UUID:
			case VALUES:
			case VIEW:
			case VOLUME:
			case WATCH:
			case WEEK:
			case WHEN:
			case WHERE:
			case WINDOW:
			case WITH:
			case YEAR:
			case JSON_FALSE:
			case JSON_TRUE:
			case IDENTIFIER:
				{
				setState(2409);
				identifier();
				setState(2414);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2410);
					match(COMMA);
					setState(2411);
					identifier();
					}
					}
					setState(2416);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(2419);
			match(ARROW);
			setState(2420);
			columnExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnIdentifierContext extends ParserRuleContext {
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public ColumnIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnIdentifierContext columnIdentifier() throws RecognitionException {
		ColumnIdentifierContext _localctx = new ColumnIdentifierContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_columnIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2425);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,329,_ctx) ) {
			case 1:
				{
				setState(2422);
				tableIdentifier();
				setState(2423);
				match(DOT);
				}
				break;
			}
			setState(2427);
			nestedIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NestedIdentifierContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(ClickHouseParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(ClickHouseParser.DOT, i);
		}
		public NestedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterNestedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitNestedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitNestedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedIdentifierContext nestedIdentifier() throws RecognitionException {
		NestedIdentifierContext _localctx = new NestedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_nestedIdentifier);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2429);
			identifier();
			setState(2434);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,330,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(2430);
					match(DOT);
					setState(2431);
					identifier();
					}
					} 
				}
				setState(2436);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,330,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnExceptExprContext extends ParserRuleContext {
		public ColumnExceptExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnExceptExpr; }
	 
		public ColumnExceptExprContext() { }
		public void copyFrom(ColumnExceptExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ColumnExceptExprRegexpContext extends ColumnExceptExprContext {
		public TerminalNode EXCEPT() { return getToken(ClickHouseParser.EXCEPT, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public ColumnExceptExprRegexpContext(ColumnExceptExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExceptExprRegexp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExceptExprRegexp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExceptExprRegexp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ColumnExceptExprIdentifiersContext extends ColumnExceptExprContext {
		public TerminalNode EXCEPT() { return getToken(ClickHouseParser.EXCEPT, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public ColumnExceptExprIdentifiersContext(ColumnExceptExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterColumnExceptExprIdentifiers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitColumnExceptExprIdentifiers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitColumnExceptExprIdentifiers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnExceptExprContext columnExceptExpr() throws RecognitionException {
		ColumnExceptExprContext _localctx = new ColumnExceptExprContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_columnExceptExpr);
		int _la;
		try {
			setState(2460);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,334,_ctx) ) {
			case 1:
				_localctx = new ColumnExceptExprRegexpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(2437);
				match(EXCEPT);
				setState(2443);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case HEXADECIMAL_STRING_LITERAL:
				case BINARY_STRING_LITERAL:
				case STRING_LITERAL:
					{
					setState(2438);
					stringLiteral();
					}
					break;
				case LPAREN:
					{
					{
					setState(2439);
					match(LPAREN);
					setState(2440);
					stringLiteral();
					setState(2441);
					match(RPAREN);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 2:
				_localctx = new ColumnExceptExprIdentifiersContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(2445);
				match(EXCEPT);
				setState(2458);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ACCESS:
				case ADD:
				case AFTER:
				case ALIAS:
				case ALL:
				case ALTER:
				case AND:
				case ANTI:
				case ANY:
				case ARRAY:
				case AS:
				case ASCENDING:
				case ASOF:
				case AST:
				case ASYNC:
				case ATTACH:
				case BETWEEN:
				case BOTH:
				case BY:
				case CACHES:
				case CASE:
				case CAST:
				case CHECK:
				case CLEAR:
				case CLUSTER:
				case CLUSTERS:
				case CODEC:
				case COLLATE:
				case COLUMN:
				case COLUMNS:
				case COMMENT:
				case CONSTRAINT:
				case CREATE:
				case CROSS:
				case CUBE:
				case CURRENT:
				case CURRENT_USER:
				case CHANGED:
				case DATABASE:
				case DATABASES:
				case DATE:
				case DAY:
				case DEDUPLICATE:
				case DEFAULT:
				case DELAY:
				case DELETE:
				case DESC:
				case DESCENDING:
				case DESCRIBE:
				case DETACH:
				case DICTIONARIES:
				case DICTIONARY:
				case DISK:
				case DISTINCT:
				case DISTRIBUTED:
				case DROP:
				case ELSE:
				case ENABLED:
				case END:
				case ENGINE:
				case ENGINES:
				case ESTIMATE:
				case EVENTS:
				case EXCEPT:
				case EXISTS:
				case EXPLAIN:
				case EXPRESSION:
				case EXTENDED:
				case EXTRACT:
				case FETCHES:
				case FIELDS:
				case FILESYSTEM:
				case FILL:
				case FINAL:
				case FIRST:
				case FLUSH:
				case FOLLOWING:
				case FOR:
				case FORMAT:
				case FREEZE:
				case FROM:
				case FULL:
				case FUNCTION:
				case FUNCTIONS:
				case GLOBAL:
				case GRANULARITY:
				case GRANTS:
				case GROUP:
				case GROUPING:
				case HAVING:
				case HIERARCHICAL:
				case HOUR:
				case ID:
				case IF:
				case ILIKE:
				case IMPLICIT:
				case IN:
				case INDEX:
				case INDEXES:
				case INDICES:
				case INJECTIVE:
				case INNER:
				case INSERT:
				case INTERPOLATE:
				case INTERVAL:
				case INTO:
				case IS:
				case IS_OBJECT_ID:
				case JOIN:
				case KEY:
				case KEYS:
				case KILL:
				case LAST:
				case LAYOUT:
				case LEADING:
				case LEFT:
				case LIFETIME:
				case LIKE:
				case LIMIT:
				case LIVE:
				case LOCAL:
				case LOGS:
				case MATERIALIZE:
				case MATERIALIZED:
				case MAX:
				case MERGES:
				case MICROSECOND:
				case MILLISECOND:
				case MIN:
				case MINUTE:
				case MODIFY:
				case MONTH:
				case MOVE:
				case MUTATION:
				case NANOSECOND:
				case NO:
				case NOT:
				case NULLS:
				case OFFSET:
				case ON:
				case OPTIMIZE:
				case OR:
				case ORDER:
				case OUTER:
				case OUTFILE:
				case OVER:
				case OVERRIDE:
				case PARTITION:
				case PIPELINE:
				case PLAN:
				case POLICY:
				case POLICIES:
				case POPULATE:
				case PRECEDING:
				case PREWHERE:
				case PRIMARY:
				case PRIVILEGES:
				case PROCESSLIST:
				case PROFILE:
				case PROFILES:
				case PROJECTION:
				case QUARTER:
				case QUOTA:
				case QUOTAS:
				case RANGE:
				case RECURSIVE:
				case RELOAD:
				case REMOVE:
				case RENAME:
				case REPLACE:
				case REPLICA:
				case REPLICATED:
				case RIGHT:
				case ROLE:
				case ROLES:
				case ROLLUP:
				case ROW:
				case ROWS:
				case SAMPLE:
				case SECOND:
				case SELECT:
				case SEMI:
				case SENDS:
				case SET:
				case SETTING:
				case SETTINGS:
				case SHOW:
				case SOURCE:
				case START:
				case STOP:
				case SUBSTRING:
				case SYNC:
				case SYNTAX:
				case SYSTEM:
				case STEP:
				case TABLE:
				case TABLES:
				case TEMPORARY:
				case TEST:
				case THEN:
				case TIES:
				case TIMEOUT:
				case TIMESTAMP:
				case TO:
				case TOP:
				case TOTALS:
				case TRAILING:
				case TREE:
				case TRIM:
				case TRUNCATE:
				case TTL:
				case TYPE:
				case UNBOUNDED:
				case UNION:
				case UPDATE:
				case USE:
				case USER:
				case USERS:
				case USING:
				case UUID:
				case VALUES:
				case VIEW:
				case VOLUME:
				case WATCH:
				case WEEK:
				case WHEN:
				case WHERE:
				case WINDOW:
				case WITH:
				case YEAR:
				case JSON_FALSE:
				case JSON_TRUE:
				case IDENTIFIER:
					{
					setState(2446);
					identifier();
					}
					break;
				case LPAREN:
					{
					{
					setState(2447);
					match(LPAREN);
					setState(2448);
					identifier();
					setState(2453);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(2449);
						match(COMMA);
						setState(2450);
						identifier();
						}
						}
						setState(2455);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(2456);
					match(RPAREN);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableExprContext extends ParserRuleContext {
		public TableExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableExpr; }
	 
		public TableExprContext() { }
		public void copyFrom(TableExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TableExprIdentifierContext extends TableExprContext {
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TableExprIdentifierContext(TableExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableExprIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableExprIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableExprIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableExprSubqueryContext extends TableExprContext {
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public SelectUnionStmtContext selectUnionStmt() {
			return getRuleContext(SelectUnionStmtContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TableExprSubqueryContext(TableExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableExprSubquery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableExprSubquery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableExprSubquery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableExprAliasContext extends TableExprContext {
		public TableExprContext tableExpr() {
			return getRuleContext(TableExprContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TableExprAliasContext(TableExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableExprAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableExprAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableExprAlias(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TableExprFunctionContext extends TableExprContext {
		public TableFunctionExprContext tableFunctionExpr() {
			return getRuleContext(TableFunctionExprContext.class,0);
		}
		public TableExprFunctionContext(TableExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableExprFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableExprFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableExprFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableExprContext tableExpr() throws RecognitionException {
		return tableExpr(0);
	}

	private TableExprContext tableExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TableExprContext _localctx = new TableExprContext(_ctx, _parentState);
		TableExprContext _prevctx = _localctx;
		int _startState = 228;
		enterRecursionRule(_localctx, 228, RULE_tableExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2469);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,335,_ctx) ) {
			case 1:
				{
				_localctx = new TableExprIdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(2463);
				tableIdentifier();
				}
				break;
			case 2:
				{
				_localctx = new TableExprFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2464);
				tableFunctionExpr();
				}
				break;
			case 3:
				{
				_localctx = new TableExprSubqueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(2465);
				match(LPAREN);
				setState(2466);
				selectUnionStmt();
				setState(2467);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(2479);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,337,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TableExprAliasContext(new TableExprContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_tableExpr);
					setState(2471);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(2475);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case AFTER:
					case ALIAS:
					case ALTER:
					case AST:
					case ASYNC:
					case ATTACH:
					case BOTH:
					case CASE:
					case CAST:
					case CHECK:
					case CLEAR:
					case CLUSTER:
					case CODEC:
					case COLUMN:
					case COMMENT:
					case CONSTRAINT:
					case CREATE:
					case CUBE:
					case CURRENT:
					case DATABASE:
					case DATABASES:
					case DATE:
					case DEDUPLICATE:
					case DEFAULT:
					case DELAY:
					case DESCRIBE:
					case DETACH:
					case DICTIONARIES:
					case DICTIONARY:
					case DISK:
					case DISTRIBUTED:
					case DROP:
					case ENGINE:
					case EVENTS:
					case EXISTS:
					case EXPLAIN:
					case EXPRESSION:
					case EXTRACT:
					case FETCHES:
					case FLUSH:
					case FOLLOWING:
					case FREEZE:
					case FUNCTION:
					case GRANULARITY:
					case HIERARCHICAL:
					case ID:
					case IF:
					case INDEX:
					case INJECTIVE:
					case INSERT:
					case IS_OBJECT_ID:
					case KEY:
					case KILL:
					case LAYOUT:
					case LEADING:
					case LIFETIME:
					case LIVE:
					case LOGS:
					case MATERIALIZE:
					case MATERIALIZED:
					case MAX:
					case MERGES:
					case MIN:
					case MODIFY:
					case MOVE:
					case MUTATION:
					case NO:
					case OPTIMIZE:
					case OUTFILE:
					case OVER:
					case PARTITION:
					case POPULATE:
					case PRECEDING:
					case PRIMARY:
					case RANGE:
					case RELOAD:
					case REMOVE:
					case RENAME:
					case REPLACE:
					case REPLICA:
					case REPLICATED:
					case ROLLUP:
					case ROW:
					case SELECT:
					case SENDS:
					case SET:
					case SHOW:
					case SOURCE:
					case START:
					case STOP:
					case SUBSTRING:
					case SYNC:
					case SYNTAX:
					case SYSTEM:
					case TABLE:
					case TABLES:
					case TEMPORARY:
					case TEST:
					case TIES:
					case TIMEOUT:
					case TIMESTAMP:
					case TOTALS:
					case TRAILING:
					case TRIM:
					case TRUNCATE:
					case TTL:
					case TYPE:
					case UNBOUNDED:
					case UPDATE:
					case USE:
					case UUID:
					case VALUES:
					case VIEW:
					case VOLUME:
					case WATCH:
					case IDENTIFIER:
						{
						setState(2472);
						alias();
						}
						break;
					case AS:
						{
						setState(2473);
						match(AS);
						setState(2474);
						identifier();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(2481);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,337,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TableFunctionExprContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ClickHouseParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(ClickHouseParser.RPAREN, 0); }
		public TableArgListContext tableArgList() {
			return getRuleContext(TableArgListContext.class,0);
		}
		public TableFunctionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableFunctionExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableFunctionExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableFunctionExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableFunctionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableFunctionExprContext tableFunctionExpr() throws RecognitionException {
		TableFunctionExprContext _localctx = new TableFunctionExprContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_tableFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2482);
			identifier();
			setState(2483);
			match(LPAREN);
			setState(2485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INF - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MONTH - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NAN_SQL - 128)) | (1L << (NANOSECOND - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULL_SQL - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)) | (1L << (HEXADECIMAL_STRING_LITERAL - 192)) | (1L << (BINARY_STRING_LITERAL - 192)) | (1L << (IDENTIFIER - 192)) | (1L << (FLOATING_LITERAL - 192)) | (1L << (OCTAL_LITERAL - 192)) | (1L << (DECIMAL_LITERAL - 192)) | (1L << (HEXADECIMAL_NUMERIC_LITERAL - 192)) | (1L << (BINARY_NUMERIC_LITERAL - 192)) | (1L << (STRING_LITERAL - 192)))) != 0) || ((((_la - 257)) & ~0x3f) == 0 && ((1L << (_la - 257)) & ((1L << (DASH - 257)) | (1L << (DOT - 257)) | (1L << (PLUS - 257)))) != 0)) {
				{
				setState(2484);
				tableArgList();
				}
			}

			setState(2487);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public TableIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableIdentifierContext tableIdentifier() throws RecognitionException {
		TableIdentifierContext _localctx = new TableIdentifierContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_tableIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2492);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,339,_ctx) ) {
			case 1:
				{
				setState(2489);
				databaseIdentifier();
				setState(2490);
				match(DOT);
				}
				break;
			}
			setState(2494);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableArgListContext extends ParserRuleContext {
		public List<TableArgExprContext> tableArgExpr() {
			return getRuleContexts(TableArgExprContext.class);
		}
		public TableArgExprContext tableArgExpr(int i) {
			return getRuleContext(TableArgExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ClickHouseParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ClickHouseParser.COMMA, i);
		}
		public TableArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableArgListContext tableArgList() throws RecognitionException {
		TableArgListContext _localctx = new TableArgListContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_tableArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2496);
			tableArgExpr();
			setState(2501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(2497);
				match(COMMA);
				setState(2498);
				tableArgExpr();
				}
				}
				setState(2503);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableArgExprContext extends ParserRuleContext {
		public TableFunctionExprContext tableFunctionExpr() {
			return getRuleContext(TableFunctionExprContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public NestedIdentifierContext nestedIdentifier() {
			return getRuleContext(NestedIdentifierContext.class,0);
		}
		public TableArgExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableArgExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterTableArgExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitTableArgExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitTableArgExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableArgExprContext tableArgExpr() throws RecognitionException {
		TableArgExprContext _localctx = new TableArgExprContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_tableArgExpr);
		try {
			setState(2507);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,341,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2504);
				tableFunctionExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2505);
				literal();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2506);
				nestedIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DatabaseIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DatabaseIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_databaseIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDatabaseIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDatabaseIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDatabaseIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatabaseIdentifierContext databaseIdentifier() throws RecognitionException {
		DatabaseIdentifierContext _localctx = new DatabaseIdentifierContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_databaseIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2509);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DictionaryIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DatabaseIdentifierContext databaseIdentifier() {
			return getRuleContext(DatabaseIdentifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public DictionaryIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictionaryIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterDictionaryIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitDictionaryIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitDictionaryIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictionaryIdentifierContext dictionaryIdentifier() throws RecognitionException {
		DictionaryIdentifierContext _localctx = new DictionaryIdentifierContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_dictionaryIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2514);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,342,_ctx) ) {
			case 1:
				{
				setState(2511);
				databaseIdentifier();
				setState(2512);
				match(DOT);
				}
				break;
			}
			setState(2516);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FloatingLiteralContext extends ParserRuleContext {
		public TerminalNode FLOATING_LITERAL() { return getToken(ClickHouseParser.FLOATING_LITERAL, 0); }
		public TerminalNode DOT() { return getToken(ClickHouseParser.DOT, 0); }
		public List<TerminalNode> DECIMAL_LITERAL() { return getTokens(ClickHouseParser.DECIMAL_LITERAL); }
		public TerminalNode DECIMAL_LITERAL(int i) {
			return getToken(ClickHouseParser.DECIMAL_LITERAL, i);
		}
		public TerminalNode OCTAL_LITERAL() { return getToken(ClickHouseParser.OCTAL_LITERAL, 0); }
		public FloatingLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatingLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterFloatingLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitFloatingLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitFloatingLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FloatingLiteralContext floatingLiteral() throws RecognitionException {
		FloatingLiteralContext _localctx = new FloatingLiteralContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_floatingLiteral);
		int _la;
		try {
			setState(2526);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOATING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(2518);
				match(FLOATING_LITERAL);
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(2519);
				match(DOT);
				setState(2520);
				_la = _input.LA(1);
				if ( !(_la==OCTAL_LITERAL || _la==DECIMAL_LITERAL) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case DECIMAL_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(2521);
				match(DECIMAL_LITERAL);
				setState(2522);
				match(DOT);
				setState(2524);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,343,_ctx) ) {
				case 1:
					{
					setState(2523);
					_la = _input.LA(1);
					if ( !(_la==OCTAL_LITERAL || _la==DECIMAL_LITERAL) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberLiteralContext extends ParserRuleContext {
		public FloatingLiteralContext floatingLiteral() {
			return getRuleContext(FloatingLiteralContext.class,0);
		}
		public TerminalNode OCTAL_LITERAL() { return getToken(ClickHouseParser.OCTAL_LITERAL, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(ClickHouseParser.DECIMAL_LITERAL, 0); }
		public TerminalNode HEXADECIMAL_NUMERIC_LITERAL() { return getToken(ClickHouseParser.HEXADECIMAL_NUMERIC_LITERAL, 0); }
		public TerminalNode BINARY_NUMERIC_LITERAL() { return getToken(ClickHouseParser.BINARY_NUMERIC_LITERAL, 0); }
		public TerminalNode INF() { return getToken(ClickHouseParser.INF, 0); }
		public TerminalNode NAN_SQL() { return getToken(ClickHouseParser.NAN_SQL, 0); }
		public TerminalNode PLUS() { return getToken(ClickHouseParser.PLUS, 0); }
		public TerminalNode DASH() { return getToken(ClickHouseParser.DASH, 0); }
		public NumberLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberLiteralContext numberLiteral() throws RecognitionException {
		NumberLiteralContext _localctx = new NumberLiteralContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_numberLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2529);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DASH || _la==PLUS) {
				{
				setState(2528);
				_la = _input.LA(1);
				if ( !(_la==DASH || _la==PLUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(2538);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,346,_ctx) ) {
			case 1:
				{
				setState(2531);
				floatingLiteral();
				}
				break;
			case 2:
				{
				setState(2532);
				match(OCTAL_LITERAL);
				}
				break;
			case 3:
				{
				setState(2533);
				match(DECIMAL_LITERAL);
				}
				break;
			case 4:
				{
				setState(2534);
				match(HEXADECIMAL_NUMERIC_LITERAL);
				}
				break;
			case 5:
				{
				setState(2535);
				match(BINARY_NUMERIC_LITERAL);
				}
				break;
			case 6:
				{
				setState(2536);
				match(INF);
				}
				break;
			case 7:
				{
				setState(2537);
				match(NAN_SQL);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode HEXADECIMAL_STRING_LITERAL() { return getToken(ClickHouseParser.HEXADECIMAL_STRING_LITERAL, 0); }
		public TerminalNode BINARY_STRING_LITERAL() { return getToken(ClickHouseParser.BINARY_STRING_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(ClickHouseParser.STRING_LITERAL, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_stringLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2540);
			_la = _input.LA(1);
			if ( !(((((_la - 240)) & ~0x3f) == 0 && ((1L << (_la - 240)) & ((1L << (HEXADECIMAL_STRING_LITERAL - 240)) | (1L << (BINARY_STRING_LITERAL - 240)) | (1L << (STRING_LITERAL - 240)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public NumberLiteralContext numberLiteral() {
			return getRuleContext(NumberLiteralContext.class,0);
		}
		public TerminalNode JSON_FALSE() { return getToken(ClickHouseParser.JSON_FALSE, 0); }
		public TerminalNode JSON_TRUE() { return getToken(ClickHouseParser.JSON_TRUE, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode NULL_SQL() { return getToken(ClickHouseParser.NULL_SQL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_literal);
		try {
			setState(2547);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INF:
			case NAN_SQL:
			case FLOATING_LITERAL:
			case OCTAL_LITERAL:
			case DECIMAL_LITERAL:
			case HEXADECIMAL_NUMERIC_LITERAL:
			case BINARY_NUMERIC_LITERAL:
			case DASH:
			case DOT:
			case PLUS:
				enterOuterAlt(_localctx, 1);
				{
				setState(2542);
				numberLiteral();
				}
				break;
			case JSON_FALSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(2543);
				match(JSON_FALSE);
				}
				break;
			case JSON_TRUE:
				enterOuterAlt(_localctx, 3);
				{
				setState(2544);
				match(JSON_TRUE);
				}
				break;
			case HEXADECIMAL_STRING_LITERAL:
			case BINARY_STRING_LITERAL:
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 4);
				{
				setState(2545);
				stringLiteral();
				}
				break;
			case NULL_SQL:
				enterOuterAlt(_localctx, 5);
				{
				setState(2546);
				match(NULL_SQL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntervalContext extends ParserRuleContext {
		public TerminalNode NANOSECOND() { return getToken(ClickHouseParser.NANOSECOND, 0); }
		public TerminalNode MICROSECOND() { return getToken(ClickHouseParser.MICROSECOND, 0); }
		public TerminalNode MILLISECOND() { return getToken(ClickHouseParser.MILLISECOND, 0); }
		public TerminalNode SECOND() { return getToken(ClickHouseParser.SECOND, 0); }
		public TerminalNode MINUTE() { return getToken(ClickHouseParser.MINUTE, 0); }
		public TerminalNode HOUR() { return getToken(ClickHouseParser.HOUR, 0); }
		public TerminalNode DAY() { return getToken(ClickHouseParser.DAY, 0); }
		public TerminalNode WEEK() { return getToken(ClickHouseParser.WEEK, 0); }
		public TerminalNode MONTH() { return getToken(ClickHouseParser.MONTH, 0); }
		public TerminalNode QUARTER() { return getToken(ClickHouseParser.QUARTER, 0); }
		public TerminalNode YEAR() { return getToken(ClickHouseParser.YEAR, 0); }
		public IntervalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterInterval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitInterval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitInterval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalContext interval() throws RecognitionException {
		IntervalContext _localctx = new IntervalContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_interval);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2549);
			_la = _input.LA(1);
			if ( !(_la==DAY || _la==HOUR || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (MICROSECOND - 129)) | (1L << (MILLISECOND - 129)) | (1L << (MINUTE - 129)) | (1L << (MONTH - 129)) | (1L << (NANOSECOND - 129)) | (1L << (QUARTER - 129)) | (1L << (SECOND - 129)))) != 0) || _la==WEEK || _la==YEAR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode ACCESS() { return getToken(ClickHouseParser.ACCESS, 0); }
		public TerminalNode ADD() { return getToken(ClickHouseParser.ADD, 0); }
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public TerminalNode ALIAS() { return getToken(ClickHouseParser.ALIAS, 0); }
		public TerminalNode ALL() { return getToken(ClickHouseParser.ALL, 0); }
		public TerminalNode ALTER() { return getToken(ClickHouseParser.ALTER, 0); }
		public TerminalNode AND() { return getToken(ClickHouseParser.AND, 0); }
		public TerminalNode ANTI() { return getToken(ClickHouseParser.ANTI, 0); }
		public TerminalNode ANY() { return getToken(ClickHouseParser.ANY, 0); }
		public TerminalNode ARRAY() { return getToken(ClickHouseParser.ARRAY, 0); }
		public TerminalNode AS() { return getToken(ClickHouseParser.AS, 0); }
		public TerminalNode ASCENDING() { return getToken(ClickHouseParser.ASCENDING, 0); }
		public TerminalNode ASOF() { return getToken(ClickHouseParser.ASOF, 0); }
		public TerminalNode AST() { return getToken(ClickHouseParser.AST, 0); }
		public TerminalNode ASYNC() { return getToken(ClickHouseParser.ASYNC, 0); }
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode BETWEEN() { return getToken(ClickHouseParser.BETWEEN, 0); }
		public TerminalNode BOTH() { return getToken(ClickHouseParser.BOTH, 0); }
		public TerminalNode BY() { return getToken(ClickHouseParser.BY, 0); }
		public TerminalNode CACHES() { return getToken(ClickHouseParser.CACHES, 0); }
		public TerminalNode CASE() { return getToken(ClickHouseParser.CASE, 0); }
		public TerminalNode CAST() { return getToken(ClickHouseParser.CAST, 0); }
		public TerminalNode CHECK() { return getToken(ClickHouseParser.CHECK, 0); }
		public TerminalNode CLEAR() { return getToken(ClickHouseParser.CLEAR, 0); }
		public TerminalNode CLUSTER() { return getToken(ClickHouseParser.CLUSTER, 0); }
		public TerminalNode CLUSTERS() { return getToken(ClickHouseParser.CLUSTERS, 0); }
		public TerminalNode CODEC() { return getToken(ClickHouseParser.CODEC, 0); }
		public TerminalNode COLLATE() { return getToken(ClickHouseParser.COLLATE, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public TerminalNode COLUMNS() { return getToken(ClickHouseParser.COLUMNS, 0); }
		public TerminalNode COMMENT() { return getToken(ClickHouseParser.COMMENT, 0); }
		public TerminalNode CONSTRAINT() { return getToken(ClickHouseParser.CONSTRAINT, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode CROSS() { return getToken(ClickHouseParser.CROSS, 0); }
		public TerminalNode CUBE() { return getToken(ClickHouseParser.CUBE, 0); }
		public TerminalNode CURRENT() { return getToken(ClickHouseParser.CURRENT, 0); }
		public TerminalNode CURRENT_USER() { return getToken(ClickHouseParser.CURRENT_USER, 0); }
		public TerminalNode CHANGED() { return getToken(ClickHouseParser.CHANGED, 0); }
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public TerminalNode DATABASES() { return getToken(ClickHouseParser.DATABASES, 0); }
		public TerminalNode DATE() { return getToken(ClickHouseParser.DATE, 0); }
		public TerminalNode DAY() { return getToken(ClickHouseParser.DAY, 0); }
		public TerminalNode DEDUPLICATE() { return getToken(ClickHouseParser.DEDUPLICATE, 0); }
		public TerminalNode DEFAULT() { return getToken(ClickHouseParser.DEFAULT, 0); }
		public TerminalNode DELAY() { return getToken(ClickHouseParser.DELAY, 0); }
		public TerminalNode DELETE() { return getToken(ClickHouseParser.DELETE, 0); }
		public TerminalNode DESC() { return getToken(ClickHouseParser.DESC, 0); }
		public TerminalNode DESCENDING() { return getToken(ClickHouseParser.DESCENDING, 0); }
		public TerminalNode DESCRIBE() { return getToken(ClickHouseParser.DESCRIBE, 0); }
		public TerminalNode DETACH() { return getToken(ClickHouseParser.DETACH, 0); }
		public TerminalNode DICTIONARIES() { return getToken(ClickHouseParser.DICTIONARIES, 0); }
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TerminalNode DISK() { return getToken(ClickHouseParser.DISK, 0); }
		public TerminalNode DISTINCT() { return getToken(ClickHouseParser.DISTINCT, 0); }
		public TerminalNode DISTRIBUTED() { return getToken(ClickHouseParser.DISTRIBUTED, 0); }
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode ELSE() { return getToken(ClickHouseParser.ELSE, 0); }
		public TerminalNode ENABLED() { return getToken(ClickHouseParser.ENABLED, 0); }
		public TerminalNode END() { return getToken(ClickHouseParser.END, 0); }
		public TerminalNode ENGINE() { return getToken(ClickHouseParser.ENGINE, 0); }
		public TerminalNode ENGINES() { return getToken(ClickHouseParser.ENGINES, 0); }
		public TerminalNode ESTIMATE() { return getToken(ClickHouseParser.ESTIMATE, 0); }
		public TerminalNode EVENTS() { return getToken(ClickHouseParser.EVENTS, 0); }
		public TerminalNode EXCEPT() { return getToken(ClickHouseParser.EXCEPT, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode EXPLAIN() { return getToken(ClickHouseParser.EXPLAIN, 0); }
		public TerminalNode EXPRESSION() { return getToken(ClickHouseParser.EXPRESSION, 0); }
		public TerminalNode EXTENDED() { return getToken(ClickHouseParser.EXTENDED, 0); }
		public TerminalNode EXTRACT() { return getToken(ClickHouseParser.EXTRACT, 0); }
		public TerminalNode FETCHES() { return getToken(ClickHouseParser.FETCHES, 0); }
		public TerminalNode FIELDS() { return getToken(ClickHouseParser.FIELDS, 0); }
		public TerminalNode FILESYSTEM() { return getToken(ClickHouseParser.FILESYSTEM, 0); }
		public TerminalNode FILL() { return getToken(ClickHouseParser.FILL, 0); }
		public TerminalNode FINAL() { return getToken(ClickHouseParser.FINAL, 0); }
		public TerminalNode FIRST() { return getToken(ClickHouseParser.FIRST, 0); }
		public TerminalNode FLUSH() { return getToken(ClickHouseParser.FLUSH, 0); }
		public TerminalNode FOLLOWING() { return getToken(ClickHouseParser.FOLLOWING, 0); }
		public TerminalNode FOR() { return getToken(ClickHouseParser.FOR, 0); }
		public TerminalNode FORMAT() { return getToken(ClickHouseParser.FORMAT, 0); }
		public TerminalNode FREEZE() { return getToken(ClickHouseParser.FREEZE, 0); }
		public TerminalNode FROM() { return getToken(ClickHouseParser.FROM, 0); }
		public TerminalNode FULL() { return getToken(ClickHouseParser.FULL, 0); }
		public TerminalNode FUNCTION() { return getToken(ClickHouseParser.FUNCTION, 0); }
		public TerminalNode FUNCTIONS() { return getToken(ClickHouseParser.FUNCTIONS, 0); }
		public TerminalNode GLOBAL() { return getToken(ClickHouseParser.GLOBAL, 0); }
		public TerminalNode GRANULARITY() { return getToken(ClickHouseParser.GRANULARITY, 0); }
		public TerminalNode GRANTS() { return getToken(ClickHouseParser.GRANTS, 0); }
		public TerminalNode GROUP() { return getToken(ClickHouseParser.GROUP, 0); }
		public TerminalNode GROUPING() { return getToken(ClickHouseParser.GROUPING, 0); }
		public TerminalNode HAVING() { return getToken(ClickHouseParser.HAVING, 0); }
		public TerminalNode HIERARCHICAL() { return getToken(ClickHouseParser.HIERARCHICAL, 0); }
		public TerminalNode HOUR() { return getToken(ClickHouseParser.HOUR, 0); }
		public TerminalNode ID() { return getToken(ClickHouseParser.ID, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode ILIKE() { return getToken(ClickHouseParser.ILIKE, 0); }
		public TerminalNode IMPLICIT() { return getToken(ClickHouseParser.IMPLICIT, 0); }
		public TerminalNode IN() { return getToken(ClickHouseParser.IN, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public TerminalNode INDEXES() { return getToken(ClickHouseParser.INDEXES, 0); }
		public TerminalNode INDICES() { return getToken(ClickHouseParser.INDICES, 0); }
		public TerminalNode INJECTIVE() { return getToken(ClickHouseParser.INJECTIVE, 0); }
		public TerminalNode INNER() { return getToken(ClickHouseParser.INNER, 0); }
		public TerminalNode INSERT() { return getToken(ClickHouseParser.INSERT, 0); }
		public TerminalNode INTERPOLATE() { return getToken(ClickHouseParser.INTERPOLATE, 0); }
		public TerminalNode INTERVAL() { return getToken(ClickHouseParser.INTERVAL, 0); }
		public TerminalNode INTO() { return getToken(ClickHouseParser.INTO, 0); }
		public TerminalNode IS() { return getToken(ClickHouseParser.IS, 0); }
		public TerminalNode IS_OBJECT_ID() { return getToken(ClickHouseParser.IS_OBJECT_ID, 0); }
		public TerminalNode JOIN() { return getToken(ClickHouseParser.JOIN, 0); }
		public TerminalNode JSON_FALSE() { return getToken(ClickHouseParser.JSON_FALSE, 0); }
		public TerminalNode JSON_TRUE() { return getToken(ClickHouseParser.JSON_TRUE, 0); }
		public TerminalNode KEY() { return getToken(ClickHouseParser.KEY, 0); }
		public TerminalNode KEYS() { return getToken(ClickHouseParser.KEYS, 0); }
		public TerminalNode KILL() { return getToken(ClickHouseParser.KILL, 0); }
		public TerminalNode LAST() { return getToken(ClickHouseParser.LAST, 0); }
		public TerminalNode LAYOUT() { return getToken(ClickHouseParser.LAYOUT, 0); }
		public TerminalNode LEADING() { return getToken(ClickHouseParser.LEADING, 0); }
		public TerminalNode LEFT() { return getToken(ClickHouseParser.LEFT, 0); }
		public TerminalNode LIFETIME() { return getToken(ClickHouseParser.LIFETIME, 0); }
		public TerminalNode LIKE() { return getToken(ClickHouseParser.LIKE, 0); }
		public TerminalNode LIMIT() { return getToken(ClickHouseParser.LIMIT, 0); }
		public TerminalNode LIVE() { return getToken(ClickHouseParser.LIVE, 0); }
		public TerminalNode LOCAL() { return getToken(ClickHouseParser.LOCAL, 0); }
		public TerminalNode LOGS() { return getToken(ClickHouseParser.LOGS, 0); }
		public TerminalNode MATERIALIZE() { return getToken(ClickHouseParser.MATERIALIZE, 0); }
		public TerminalNode MATERIALIZED() { return getToken(ClickHouseParser.MATERIALIZED, 0); }
		public TerminalNode MAX() { return getToken(ClickHouseParser.MAX, 0); }
		public TerminalNode MERGES() { return getToken(ClickHouseParser.MERGES, 0); }
		public TerminalNode MICROSECOND() { return getToken(ClickHouseParser.MICROSECOND, 0); }
		public TerminalNode MILLISECOND() { return getToken(ClickHouseParser.MILLISECOND, 0); }
		public TerminalNode MIN() { return getToken(ClickHouseParser.MIN, 0); }
		public TerminalNode MINUTE() { return getToken(ClickHouseParser.MINUTE, 0); }
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public TerminalNode MOVE() { return getToken(ClickHouseParser.MOVE, 0); }
		public TerminalNode MUTATION() { return getToken(ClickHouseParser.MUTATION, 0); }
		public TerminalNode NO() { return getToken(ClickHouseParser.NO, 0); }
		public TerminalNode NOT() { return getToken(ClickHouseParser.NOT, 0); }
		public TerminalNode NULLS() { return getToken(ClickHouseParser.NULLS, 0); }
		public TerminalNode OFFSET() { return getToken(ClickHouseParser.OFFSET, 0); }
		public TerminalNode ON() { return getToken(ClickHouseParser.ON, 0); }
		public TerminalNode OPTIMIZE() { return getToken(ClickHouseParser.OPTIMIZE, 0); }
		public TerminalNode OR() { return getToken(ClickHouseParser.OR, 0); }
		public TerminalNode ORDER() { return getToken(ClickHouseParser.ORDER, 0); }
		public TerminalNode OUTER() { return getToken(ClickHouseParser.OUTER, 0); }
		public TerminalNode OUTFILE() { return getToken(ClickHouseParser.OUTFILE, 0); }
		public TerminalNode OVER() { return getToken(ClickHouseParser.OVER, 0); }
		public TerminalNode OVERRIDE() { return getToken(ClickHouseParser.OVERRIDE, 0); }
		public TerminalNode PARTITION() { return getToken(ClickHouseParser.PARTITION, 0); }
		public TerminalNode PIPELINE() { return getToken(ClickHouseParser.PIPELINE, 0); }
		public TerminalNode PLAN() { return getToken(ClickHouseParser.PLAN, 0); }
		public TerminalNode POLICY() { return getToken(ClickHouseParser.POLICY, 0); }
		public TerminalNode POLICIES() { return getToken(ClickHouseParser.POLICIES, 0); }
		public TerminalNode POPULATE() { return getToken(ClickHouseParser.POPULATE, 0); }
		public TerminalNode PRECEDING() { return getToken(ClickHouseParser.PRECEDING, 0); }
		public TerminalNode PREWHERE() { return getToken(ClickHouseParser.PREWHERE, 0); }
		public TerminalNode PRIMARY() { return getToken(ClickHouseParser.PRIMARY, 0); }
		public TerminalNode PRIVILEGES() { return getToken(ClickHouseParser.PRIVILEGES, 0); }
		public TerminalNode PROCESSLIST() { return getToken(ClickHouseParser.PROCESSLIST, 0); }
		public TerminalNode PROFILE() { return getToken(ClickHouseParser.PROFILE, 0); }
		public TerminalNode PROFILES() { return getToken(ClickHouseParser.PROFILES, 0); }
		public TerminalNode PROJECTION() { return getToken(ClickHouseParser.PROJECTION, 0); }
		public TerminalNode QUARTER() { return getToken(ClickHouseParser.QUARTER, 0); }
		public TerminalNode QUOTA() { return getToken(ClickHouseParser.QUOTA, 0); }
		public TerminalNode QUOTAS() { return getToken(ClickHouseParser.QUOTAS, 0); }
		public TerminalNode RANGE() { return getToken(ClickHouseParser.RANGE, 0); }
		public TerminalNode RECURSIVE() { return getToken(ClickHouseParser.RECURSIVE, 0); }
		public TerminalNode RELOAD() { return getToken(ClickHouseParser.RELOAD, 0); }
		public TerminalNode REMOVE() { return getToken(ClickHouseParser.REMOVE, 0); }
		public TerminalNode RENAME() { return getToken(ClickHouseParser.RENAME, 0); }
		public TerminalNode REPLACE() { return getToken(ClickHouseParser.REPLACE, 0); }
		public TerminalNode REPLICA() { return getToken(ClickHouseParser.REPLICA, 0); }
		public TerminalNode REPLICATED() { return getToken(ClickHouseParser.REPLICATED, 0); }
		public TerminalNode RIGHT() { return getToken(ClickHouseParser.RIGHT, 0); }
		public TerminalNode ROLE() { return getToken(ClickHouseParser.ROLE, 0); }
		public TerminalNode ROLES() { return getToken(ClickHouseParser.ROLES, 0); }
		public TerminalNode ROLLUP() { return getToken(ClickHouseParser.ROLLUP, 0); }
		public TerminalNode ROW() { return getToken(ClickHouseParser.ROW, 0); }
		public TerminalNode ROWS() { return getToken(ClickHouseParser.ROWS, 0); }
		public TerminalNode SAMPLE() { return getToken(ClickHouseParser.SAMPLE, 0); }
		public TerminalNode SECOND() { return getToken(ClickHouseParser.SECOND, 0); }
		public TerminalNode SELECT() { return getToken(ClickHouseParser.SELECT, 0); }
		public TerminalNode SEMI() { return getToken(ClickHouseParser.SEMI, 0); }
		public TerminalNode SENDS() { return getToken(ClickHouseParser.SENDS, 0); }
		public TerminalNode SET() { return getToken(ClickHouseParser.SET, 0); }
		public TerminalNode SETTING() { return getToken(ClickHouseParser.SETTING, 0); }
		public TerminalNode SETTINGS() { return getToken(ClickHouseParser.SETTINGS, 0); }
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode SOURCE() { return getToken(ClickHouseParser.SOURCE, 0); }
		public TerminalNode START() { return getToken(ClickHouseParser.START, 0); }
		public TerminalNode STOP() { return getToken(ClickHouseParser.STOP, 0); }
		public TerminalNode SUBSTRING() { return getToken(ClickHouseParser.SUBSTRING, 0); }
		public TerminalNode SYNC() { return getToken(ClickHouseParser.SYNC, 0); }
		public TerminalNode SYNTAX() { return getToken(ClickHouseParser.SYNTAX, 0); }
		public TerminalNode SYSTEM() { return getToken(ClickHouseParser.SYSTEM, 0); }
		public TerminalNode STEP() { return getToken(ClickHouseParser.STEP, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TerminalNode TABLES() { return getToken(ClickHouseParser.TABLES, 0); }
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public TerminalNode TEST() { return getToken(ClickHouseParser.TEST, 0); }
		public TerminalNode THEN() { return getToken(ClickHouseParser.THEN, 0); }
		public TerminalNode TIES() { return getToken(ClickHouseParser.TIES, 0); }
		public TerminalNode TIMEOUT() { return getToken(ClickHouseParser.TIMEOUT, 0); }
		public TerminalNode TIMESTAMP() { return getToken(ClickHouseParser.TIMESTAMP, 0); }
		public TerminalNode TO() { return getToken(ClickHouseParser.TO, 0); }
		public TerminalNode TOP() { return getToken(ClickHouseParser.TOP, 0); }
		public TerminalNode TOTALS() { return getToken(ClickHouseParser.TOTALS, 0); }
		public TerminalNode TRAILING() { return getToken(ClickHouseParser.TRAILING, 0); }
		public TerminalNode TREE() { return getToken(ClickHouseParser.TREE, 0); }
		public TerminalNode TRIM() { return getToken(ClickHouseParser.TRIM, 0); }
		public TerminalNode TRUNCATE() { return getToken(ClickHouseParser.TRUNCATE, 0); }
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public TerminalNode TYPE() { return getToken(ClickHouseParser.TYPE, 0); }
		public TerminalNode UNBOUNDED() { return getToken(ClickHouseParser.UNBOUNDED, 0); }
		public TerminalNode UNION() { return getToken(ClickHouseParser.UNION, 0); }
		public TerminalNode UPDATE() { return getToken(ClickHouseParser.UPDATE, 0); }
		public TerminalNode USE() { return getToken(ClickHouseParser.USE, 0); }
		public TerminalNode USER() { return getToken(ClickHouseParser.USER, 0); }
		public TerminalNode USERS() { return getToken(ClickHouseParser.USERS, 0); }
		public TerminalNode USING() { return getToken(ClickHouseParser.USING, 0); }
		public TerminalNode UUID() { return getToken(ClickHouseParser.UUID, 0); }
		public TerminalNode VALUES() { return getToken(ClickHouseParser.VALUES, 0); }
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TerminalNode VOLUME() { return getToken(ClickHouseParser.VOLUME, 0); }
		public TerminalNode WATCH() { return getToken(ClickHouseParser.WATCH, 0); }
		public TerminalNode WEEK() { return getToken(ClickHouseParser.WEEK, 0); }
		public TerminalNode WHEN() { return getToken(ClickHouseParser.WHEN, 0); }
		public TerminalNode WHERE() { return getToken(ClickHouseParser.WHERE, 0); }
		public TerminalNode WINDOW() { return getToken(ClickHouseParser.WINDOW, 0); }
		public TerminalNode WITH() { return getToken(ClickHouseParser.WITH, 0); }
		public TerminalNode YEAR() { return getToken(ClickHouseParser.YEAR, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_keyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2551);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCESS) | (1L << ADD) | (1L << AFTER) | (1L << ALIAS) | (1L << ALL) | (1L << ALTER) | (1L << AND) | (1L << ANTI) | (1L << ANY) | (1L << ARRAY) | (1L << AS) | (1L << ASCENDING) | (1L << ASOF) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BETWEEN) | (1L << BOTH) | (1L << BY) | (1L << CACHES) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CLUSTERS) | (1L << CODEC) | (1L << COLLATE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CROSS) | (1L << CUBE) | (1L << CURRENT) | (1L << CURRENT_USER) | (1L << CHANGED) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DAY) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DELETE) | (1L << DESC) | (1L << DESCENDING) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTINCT) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ELSE) | (1L << ENABLED) | (1L << END) | (1L << ENGINE) | (1L << ENGINES) | (1L << ESTIMATE) | (1L << EVENTS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EXCEPT - 64)) | (1L << (EXISTS - 64)) | (1L << (EXPLAIN - 64)) | (1L << (EXPRESSION - 64)) | (1L << (EXTENDED - 64)) | (1L << (EXTRACT - 64)) | (1L << (FETCHES - 64)) | (1L << (FIELDS - 64)) | (1L << (FILESYSTEM - 64)) | (1L << (FILL - 64)) | (1L << (FINAL - 64)) | (1L << (FIRST - 64)) | (1L << (FLUSH - 64)) | (1L << (FOLLOWING - 64)) | (1L << (FOR - 64)) | (1L << (FORMAT - 64)) | (1L << (FREEZE - 64)) | (1L << (FROM - 64)) | (1L << (FULL - 64)) | (1L << (FUNCTION - 64)) | (1L << (FUNCTIONS - 64)) | (1L << (GLOBAL - 64)) | (1L << (GRANULARITY - 64)) | (1L << (GRANTS - 64)) | (1L << (GROUP - 64)) | (1L << (GROUPING - 64)) | (1L << (HAVING - 64)) | (1L << (HIERARCHICAL - 64)) | (1L << (HOUR - 64)) | (1L << (ID - 64)) | (1L << (IF - 64)) | (1L << (ILIKE - 64)) | (1L << (IMPLICIT - 64)) | (1L << (IN - 64)) | (1L << (INDEX - 64)) | (1L << (INDEXES - 64)) | (1L << (INDICES - 64)) | (1L << (INJECTIVE - 64)) | (1L << (INNER - 64)) | (1L << (INSERT - 64)) | (1L << (INTERPOLATE - 64)) | (1L << (INTERVAL - 64)) | (1L << (INTO - 64)) | (1L << (IS - 64)) | (1L << (IS_OBJECT_ID - 64)) | (1L << (JOIN - 64)) | (1L << (KEY - 64)) | (1L << (KEYS - 64)) | (1L << (KILL - 64)) | (1L << (LAST - 64)) | (1L << (LAYOUT - 64)) | (1L << (LEADING - 64)) | (1L << (LEFT - 64)) | (1L << (LIFETIME - 64)) | (1L << (LIKE - 64)) | (1L << (LIMIT - 64)) | (1L << (LIVE - 64)) | (1L << (LOCAL - 64)) | (1L << (LOGS - 64)) | (1L << (MATERIALIZE - 64)) | (1L << (MATERIALIZED - 64)) | (1L << (MAX - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (MERGES - 128)) | (1L << (MICROSECOND - 128)) | (1L << (MILLISECOND - 128)) | (1L << (MIN - 128)) | (1L << (MINUTE - 128)) | (1L << (MODIFY - 128)) | (1L << (MOVE - 128)) | (1L << (MUTATION - 128)) | (1L << (NO - 128)) | (1L << (NOT - 128)) | (1L << (NULLS - 128)) | (1L << (OFFSET - 128)) | (1L << (ON - 128)) | (1L << (OPTIMIZE - 128)) | (1L << (OR - 128)) | (1L << (ORDER - 128)) | (1L << (OUTER - 128)) | (1L << (OUTFILE - 128)) | (1L << (OVER - 128)) | (1L << (OVERRIDE - 128)) | (1L << (PARTITION - 128)) | (1L << (PIPELINE - 128)) | (1L << (PLAN - 128)) | (1L << (POLICY - 128)) | (1L << (POLICIES - 128)) | (1L << (POPULATE - 128)) | (1L << (PRECEDING - 128)) | (1L << (PREWHERE - 128)) | (1L << (PRIMARY - 128)) | (1L << (PRIVILEGES - 128)) | (1L << (PROCESSLIST - 128)) | (1L << (PROFILE - 128)) | (1L << (PROFILES - 128)) | (1L << (PROJECTION - 128)) | (1L << (QUARTER - 128)) | (1L << (QUOTA - 128)) | (1L << (QUOTAS - 128)) | (1L << (RANGE - 128)) | (1L << (RECURSIVE - 128)) | (1L << (RELOAD - 128)) | (1L << (REMOVE - 128)) | (1L << (RENAME - 128)) | (1L << (REPLACE - 128)) | (1L << (REPLICA - 128)) | (1L << (REPLICATED - 128)) | (1L << (RIGHT - 128)) | (1L << (ROLE - 128)) | (1L << (ROLES - 128)) | (1L << (ROLLUP - 128)) | (1L << (ROW - 128)) | (1L << (ROWS - 128)) | (1L << (SAMPLE - 128)) | (1L << (SECOND - 128)) | (1L << (SELECT - 128)) | (1L << (SEMI - 128)) | (1L << (SENDS - 128)) | (1L << (SET - 128)))) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & ((1L << (SETTING - 192)) | (1L << (SETTINGS - 192)) | (1L << (SHOW - 192)) | (1L << (SOURCE - 192)) | (1L << (START - 192)) | (1L << (STOP - 192)) | (1L << (SUBSTRING - 192)) | (1L << (SYNC - 192)) | (1L << (SYNTAX - 192)) | (1L << (SYSTEM - 192)) | (1L << (STEP - 192)) | (1L << (TABLE - 192)) | (1L << (TABLES - 192)) | (1L << (TEMPORARY - 192)) | (1L << (TEST - 192)) | (1L << (THEN - 192)) | (1L << (TIES - 192)) | (1L << (TIMEOUT - 192)) | (1L << (TIMESTAMP - 192)) | (1L << (TO - 192)) | (1L << (TOP - 192)) | (1L << (TOTALS - 192)) | (1L << (TRAILING - 192)) | (1L << (TREE - 192)) | (1L << (TRIM - 192)) | (1L << (TRUNCATE - 192)) | (1L << (TTL - 192)) | (1L << (TYPE - 192)) | (1L << (UNBOUNDED - 192)) | (1L << (UNION - 192)) | (1L << (UPDATE - 192)) | (1L << (USE - 192)) | (1L << (USER - 192)) | (1L << (USERS - 192)) | (1L << (USING - 192)) | (1L << (UUID - 192)) | (1L << (VALUES - 192)) | (1L << (VIEW - 192)) | (1L << (VOLUME - 192)) | (1L << (WATCH - 192)) | (1L << (WEEK - 192)) | (1L << (WHEN - 192)) | (1L << (WHERE - 192)) | (1L << (WINDOW - 192)) | (1L << (WITH - 192)) | (1L << (YEAR - 192)) | (1L << (JSON_FALSE - 192)) | (1L << (JSON_TRUE - 192)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeywordForAliasContext extends ParserRuleContext {
		public TerminalNode AFTER() { return getToken(ClickHouseParser.AFTER, 0); }
		public TerminalNode ALIAS() { return getToken(ClickHouseParser.ALIAS, 0); }
		public TerminalNode ALTER() { return getToken(ClickHouseParser.ALTER, 0); }
		public TerminalNode AST() { return getToken(ClickHouseParser.AST, 0); }
		public TerminalNode ASYNC() { return getToken(ClickHouseParser.ASYNC, 0); }
		public TerminalNode ATTACH() { return getToken(ClickHouseParser.ATTACH, 0); }
		public TerminalNode BOTH() { return getToken(ClickHouseParser.BOTH, 0); }
		public TerminalNode CASE() { return getToken(ClickHouseParser.CASE, 0); }
		public TerminalNode CAST() { return getToken(ClickHouseParser.CAST, 0); }
		public TerminalNode CHECK() { return getToken(ClickHouseParser.CHECK, 0); }
		public TerminalNode CLEAR() { return getToken(ClickHouseParser.CLEAR, 0); }
		public TerminalNode CLUSTER() { return getToken(ClickHouseParser.CLUSTER, 0); }
		public TerminalNode CODEC() { return getToken(ClickHouseParser.CODEC, 0); }
		public TerminalNode COLUMN() { return getToken(ClickHouseParser.COLUMN, 0); }
		public TerminalNode COMMENT() { return getToken(ClickHouseParser.COMMENT, 0); }
		public TerminalNode CONSTRAINT() { return getToken(ClickHouseParser.CONSTRAINT, 0); }
		public TerminalNode CREATE() { return getToken(ClickHouseParser.CREATE, 0); }
		public TerminalNode CUBE() { return getToken(ClickHouseParser.CUBE, 0); }
		public TerminalNode CURRENT() { return getToken(ClickHouseParser.CURRENT, 0); }
		public TerminalNode DATABASE() { return getToken(ClickHouseParser.DATABASE, 0); }
		public TerminalNode DATABASES() { return getToken(ClickHouseParser.DATABASES, 0); }
		public TerminalNode DATE() { return getToken(ClickHouseParser.DATE, 0); }
		public TerminalNode DEDUPLICATE() { return getToken(ClickHouseParser.DEDUPLICATE, 0); }
		public TerminalNode DEFAULT() { return getToken(ClickHouseParser.DEFAULT, 0); }
		public TerminalNode DELAY() { return getToken(ClickHouseParser.DELAY, 0); }
		public TerminalNode DESCRIBE() { return getToken(ClickHouseParser.DESCRIBE, 0); }
		public TerminalNode DETACH() { return getToken(ClickHouseParser.DETACH, 0); }
		public TerminalNode DICTIONARIES() { return getToken(ClickHouseParser.DICTIONARIES, 0); }
		public TerminalNode DICTIONARY() { return getToken(ClickHouseParser.DICTIONARY, 0); }
		public TerminalNode DISK() { return getToken(ClickHouseParser.DISK, 0); }
		public TerminalNode DISTRIBUTED() { return getToken(ClickHouseParser.DISTRIBUTED, 0); }
		public TerminalNode DROP() { return getToken(ClickHouseParser.DROP, 0); }
		public TerminalNode ENGINE() { return getToken(ClickHouseParser.ENGINE, 0); }
		public TerminalNode EVENTS() { return getToken(ClickHouseParser.EVENTS, 0); }
		public TerminalNode EXISTS() { return getToken(ClickHouseParser.EXISTS, 0); }
		public TerminalNode EXPLAIN() { return getToken(ClickHouseParser.EXPLAIN, 0); }
		public TerminalNode EXPRESSION() { return getToken(ClickHouseParser.EXPRESSION, 0); }
		public TerminalNode EXTRACT() { return getToken(ClickHouseParser.EXTRACT, 0); }
		public TerminalNode FETCHES() { return getToken(ClickHouseParser.FETCHES, 0); }
		public TerminalNode FLUSH() { return getToken(ClickHouseParser.FLUSH, 0); }
		public TerminalNode FOLLOWING() { return getToken(ClickHouseParser.FOLLOWING, 0); }
		public TerminalNode FREEZE() { return getToken(ClickHouseParser.FREEZE, 0); }
		public TerminalNode FUNCTION() { return getToken(ClickHouseParser.FUNCTION, 0); }
		public TerminalNode GRANULARITY() { return getToken(ClickHouseParser.GRANULARITY, 0); }
		public TerminalNode HIERARCHICAL() { return getToken(ClickHouseParser.HIERARCHICAL, 0); }
		public TerminalNode ID() { return getToken(ClickHouseParser.ID, 0); }
		public TerminalNode IF() { return getToken(ClickHouseParser.IF, 0); }
		public TerminalNode INDEX() { return getToken(ClickHouseParser.INDEX, 0); }
		public TerminalNode INJECTIVE() { return getToken(ClickHouseParser.INJECTIVE, 0); }
		public TerminalNode INSERT() { return getToken(ClickHouseParser.INSERT, 0); }
		public TerminalNode IS_OBJECT_ID() { return getToken(ClickHouseParser.IS_OBJECT_ID, 0); }
		public TerminalNode KEY() { return getToken(ClickHouseParser.KEY, 0); }
		public TerminalNode KILL() { return getToken(ClickHouseParser.KILL, 0); }
		public TerminalNode LAYOUT() { return getToken(ClickHouseParser.LAYOUT, 0); }
		public TerminalNode LEADING() { return getToken(ClickHouseParser.LEADING, 0); }
		public TerminalNode LIFETIME() { return getToken(ClickHouseParser.LIFETIME, 0); }
		public TerminalNode LIVE() { return getToken(ClickHouseParser.LIVE, 0); }
		public TerminalNode LOGS() { return getToken(ClickHouseParser.LOGS, 0); }
		public TerminalNode MATERIALIZE() { return getToken(ClickHouseParser.MATERIALIZE, 0); }
		public TerminalNode MATERIALIZED() { return getToken(ClickHouseParser.MATERIALIZED, 0); }
		public TerminalNode MAX() { return getToken(ClickHouseParser.MAX, 0); }
		public TerminalNode MERGES() { return getToken(ClickHouseParser.MERGES, 0); }
		public TerminalNode MIN() { return getToken(ClickHouseParser.MIN, 0); }
		public TerminalNode MODIFY() { return getToken(ClickHouseParser.MODIFY, 0); }
		public TerminalNode MOVE() { return getToken(ClickHouseParser.MOVE, 0); }
		public TerminalNode MUTATION() { return getToken(ClickHouseParser.MUTATION, 0); }
		public TerminalNode NO() { return getToken(ClickHouseParser.NO, 0); }
		public TerminalNode OPTIMIZE() { return getToken(ClickHouseParser.OPTIMIZE, 0); }
		public TerminalNode OUTFILE() { return getToken(ClickHouseParser.OUTFILE, 0); }
		public TerminalNode OVER() { return getToken(ClickHouseParser.OVER, 0); }
		public TerminalNode PARTITION() { return getToken(ClickHouseParser.PARTITION, 0); }
		public TerminalNode POPULATE() { return getToken(ClickHouseParser.POPULATE, 0); }
		public TerminalNode PRECEDING() { return getToken(ClickHouseParser.PRECEDING, 0); }
		public TerminalNode PRIMARY() { return getToken(ClickHouseParser.PRIMARY, 0); }
		public TerminalNode RANGE() { return getToken(ClickHouseParser.RANGE, 0); }
		public TerminalNode RELOAD() { return getToken(ClickHouseParser.RELOAD, 0); }
		public TerminalNode REMOVE() { return getToken(ClickHouseParser.REMOVE, 0); }
		public TerminalNode RENAME() { return getToken(ClickHouseParser.RENAME, 0); }
		public TerminalNode REPLACE() { return getToken(ClickHouseParser.REPLACE, 0); }
		public TerminalNode REPLICA() { return getToken(ClickHouseParser.REPLICA, 0); }
		public TerminalNode REPLICATED() { return getToken(ClickHouseParser.REPLICATED, 0); }
		public TerminalNode ROLLUP() { return getToken(ClickHouseParser.ROLLUP, 0); }
		public TerminalNode ROW() { return getToken(ClickHouseParser.ROW, 0); }
		public TerminalNode SELECT() { return getToken(ClickHouseParser.SELECT, 0); }
		public TerminalNode SENDS() { return getToken(ClickHouseParser.SENDS, 0); }
		public TerminalNode SET() { return getToken(ClickHouseParser.SET, 0); }
		public TerminalNode SHOW() { return getToken(ClickHouseParser.SHOW, 0); }
		public TerminalNode SOURCE() { return getToken(ClickHouseParser.SOURCE, 0); }
		public TerminalNode START() { return getToken(ClickHouseParser.START, 0); }
		public TerminalNode STOP() { return getToken(ClickHouseParser.STOP, 0); }
		public TerminalNode SUBSTRING() { return getToken(ClickHouseParser.SUBSTRING, 0); }
		public TerminalNode SYNC() { return getToken(ClickHouseParser.SYNC, 0); }
		public TerminalNode SYNTAX() { return getToken(ClickHouseParser.SYNTAX, 0); }
		public TerminalNode SYSTEM() { return getToken(ClickHouseParser.SYSTEM, 0); }
		public TerminalNode TABLE() { return getToken(ClickHouseParser.TABLE, 0); }
		public TerminalNode TABLES() { return getToken(ClickHouseParser.TABLES, 0); }
		public TerminalNode TEMPORARY() { return getToken(ClickHouseParser.TEMPORARY, 0); }
		public TerminalNode TEST() { return getToken(ClickHouseParser.TEST, 0); }
		public TerminalNode TIES() { return getToken(ClickHouseParser.TIES, 0); }
		public TerminalNode TIMEOUT() { return getToken(ClickHouseParser.TIMEOUT, 0); }
		public TerminalNode TIMESTAMP() { return getToken(ClickHouseParser.TIMESTAMP, 0); }
		public TerminalNode TOTALS() { return getToken(ClickHouseParser.TOTALS, 0); }
		public TerminalNode TRAILING() { return getToken(ClickHouseParser.TRAILING, 0); }
		public TerminalNode TRIM() { return getToken(ClickHouseParser.TRIM, 0); }
		public TerminalNode TRUNCATE() { return getToken(ClickHouseParser.TRUNCATE, 0); }
		public TerminalNode TTL() { return getToken(ClickHouseParser.TTL, 0); }
		public TerminalNode TYPE() { return getToken(ClickHouseParser.TYPE, 0); }
		public TerminalNode UNBOUNDED() { return getToken(ClickHouseParser.UNBOUNDED, 0); }
		public TerminalNode UPDATE() { return getToken(ClickHouseParser.UPDATE, 0); }
		public TerminalNode USE() { return getToken(ClickHouseParser.USE, 0); }
		public TerminalNode UUID() { return getToken(ClickHouseParser.UUID, 0); }
		public TerminalNode VALUES() { return getToken(ClickHouseParser.VALUES, 0); }
		public TerminalNode VIEW() { return getToken(ClickHouseParser.VIEW, 0); }
		public TerminalNode VOLUME() { return getToken(ClickHouseParser.VOLUME, 0); }
		public TerminalNode WATCH() { return getToken(ClickHouseParser.WATCH, 0); }
		public KeywordForAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordForAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterKeywordForAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitKeywordForAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitKeywordForAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordForAliasContext keywordForAlias() throws RecognitionException {
		KeywordForAliasContext _localctx = new KeywordForAliasContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_keywordForAlias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2553);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AFTER) | (1L << ALIAS) | (1L << ALTER) | (1L << AST) | (1L << ASYNC) | (1L << ATTACH) | (1L << BOTH) | (1L << CASE) | (1L << CAST) | (1L << CHECK) | (1L << CLEAR) | (1L << CLUSTER) | (1L << CODEC) | (1L << COLUMN) | (1L << COMMENT) | (1L << CONSTRAINT) | (1L << CREATE) | (1L << CUBE) | (1L << CURRENT) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATE) | (1L << DEDUPLICATE) | (1L << DEFAULT) | (1L << DELAY) | (1L << DESCRIBE) | (1L << DETACH) | (1L << DICTIONARIES) | (1L << DICTIONARY) | (1L << DISK) | (1L << DISTRIBUTED) | (1L << DROP) | (1L << ENGINE) | (1L << EVENTS))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (EXISTS - 66)) | (1L << (EXPLAIN - 66)) | (1L << (EXPRESSION - 66)) | (1L << (EXTRACT - 66)) | (1L << (FETCHES - 66)) | (1L << (FLUSH - 66)) | (1L << (FOLLOWING - 66)) | (1L << (FREEZE - 66)) | (1L << (FUNCTION - 66)) | (1L << (GRANULARITY - 66)) | (1L << (HIERARCHICAL - 66)) | (1L << (ID - 66)) | (1L << (IF - 66)) | (1L << (INDEX - 66)) | (1L << (INJECTIVE - 66)) | (1L << (INSERT - 66)) | (1L << (IS_OBJECT_ID - 66)) | (1L << (KEY - 66)) | (1L << (KILL - 66)) | (1L << (LAYOUT - 66)) | (1L << (LEADING - 66)) | (1L << (LIFETIME - 66)) | (1L << (LIVE - 66)) | (1L << (LOGS - 66)) | (1L << (MATERIALIZE - 66)) | (1L << (MATERIALIZED - 66)) | (1L << (MAX - 66)) | (1L << (MERGES - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (MIN - 131)) | (1L << (MODIFY - 131)) | (1L << (MOVE - 131)) | (1L << (MUTATION - 131)) | (1L << (NO - 131)) | (1L << (OPTIMIZE - 131)) | (1L << (OUTFILE - 131)) | (1L << (OVER - 131)) | (1L << (PARTITION - 131)) | (1L << (POPULATE - 131)) | (1L << (PRECEDING - 131)) | (1L << (PRIMARY - 131)) | (1L << (RANGE - 131)) | (1L << (RELOAD - 131)) | (1L << (REMOVE - 131)) | (1L << (RENAME - 131)) | (1L << (REPLACE - 131)) | (1L << (REPLICA - 131)) | (1L << (REPLICATED - 131)) | (1L << (ROLLUP - 131)) | (1L << (ROW - 131)) | (1L << (SELECT - 131)) | (1L << (SENDS - 131)) | (1L << (SET - 131)) | (1L << (SHOW - 131)))) != 0) || ((((_la - 195)) & ~0x3f) == 0 && ((1L << (_la - 195)) & ((1L << (SOURCE - 195)) | (1L << (START - 195)) | (1L << (STOP - 195)) | (1L << (SUBSTRING - 195)) | (1L << (SYNC - 195)) | (1L << (SYNTAX - 195)) | (1L << (SYSTEM - 195)) | (1L << (TABLE - 195)) | (1L << (TABLES - 195)) | (1L << (TEMPORARY - 195)) | (1L << (TEST - 195)) | (1L << (TIES - 195)) | (1L << (TIMEOUT - 195)) | (1L << (TIMESTAMP - 195)) | (1L << (TOTALS - 195)) | (1L << (TRAILING - 195)) | (1L << (TRIM - 195)) | (1L << (TRUNCATE - 195)) | (1L << (TTL - 195)) | (1L << (TYPE - 195)) | (1L << (UNBOUNDED - 195)) | (1L << (UPDATE - 195)) | (1L << (USE - 195)) | (1L << (UUID - 195)) | (1L << (VALUES - 195)) | (1L << (VIEW - 195)) | (1L << (VOLUME - 195)) | (1L << (WATCH - 195)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ClickHouseParser.IDENTIFIER, 0); }
		public KeywordForAliasContext keywordForAlias() {
			return getRuleContext(KeywordForAliasContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_alias);
		try {
			setState(2557);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(2555);
				match(IDENTIFIER);
				}
				break;
			case AFTER:
			case ALIAS:
			case ALTER:
			case AST:
			case ASYNC:
			case ATTACH:
			case BOTH:
			case CASE:
			case CAST:
			case CHECK:
			case CLEAR:
			case CLUSTER:
			case CODEC:
			case COLUMN:
			case COMMENT:
			case CONSTRAINT:
			case CREATE:
			case CUBE:
			case CURRENT:
			case DATABASE:
			case DATABASES:
			case DATE:
			case DEDUPLICATE:
			case DEFAULT:
			case DELAY:
			case DESCRIBE:
			case DETACH:
			case DICTIONARIES:
			case DICTIONARY:
			case DISK:
			case DISTRIBUTED:
			case DROP:
			case ENGINE:
			case EVENTS:
			case EXISTS:
			case EXPLAIN:
			case EXPRESSION:
			case EXTRACT:
			case FETCHES:
			case FLUSH:
			case FOLLOWING:
			case FREEZE:
			case FUNCTION:
			case GRANULARITY:
			case HIERARCHICAL:
			case ID:
			case IF:
			case INDEX:
			case INJECTIVE:
			case INSERT:
			case IS_OBJECT_ID:
			case KEY:
			case KILL:
			case LAYOUT:
			case LEADING:
			case LIFETIME:
			case LIVE:
			case LOGS:
			case MATERIALIZE:
			case MATERIALIZED:
			case MAX:
			case MERGES:
			case MIN:
			case MODIFY:
			case MOVE:
			case MUTATION:
			case NO:
			case OPTIMIZE:
			case OUTFILE:
			case OVER:
			case PARTITION:
			case POPULATE:
			case PRECEDING:
			case PRIMARY:
			case RANGE:
			case RELOAD:
			case REMOVE:
			case RENAME:
			case REPLACE:
			case REPLICA:
			case REPLICATED:
			case ROLLUP:
			case ROW:
			case SELECT:
			case SENDS:
			case SET:
			case SHOW:
			case SOURCE:
			case START:
			case STOP:
			case SUBSTRING:
			case SYNC:
			case SYNTAX:
			case SYSTEM:
			case TABLE:
			case TABLES:
			case TEMPORARY:
			case TEST:
			case TIES:
			case TIMEOUT:
			case TIMESTAMP:
			case TOTALS:
			case TRAILING:
			case TRIM:
			case TRUNCATE:
			case TTL:
			case TYPE:
			case UNBOUNDED:
			case UPDATE:
			case USE:
			case UUID:
			case VALUES:
			case VIEW:
			case VOLUME:
			case WATCH:
				enterOuterAlt(_localctx, 2);
				{
				setState(2556);
				keywordForAlias();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ClickHouseParser.IDENTIFIER, 0); }
		public IntervalContext interval() {
			return getRuleContext(IntervalContext.class,0);
		}
		public KeywordContext keyword() {
			return getRuleContext(KeywordContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_identifier);
		try {
			setState(2562);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,349,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2559);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2560);
				interval();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2561);
				keyword();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierOrNullContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode NULL_SQL() { return getToken(ClickHouseParser.NULL_SQL, 0); }
		public IdentifierOrNullContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierOrNull; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterIdentifierOrNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitIdentifierOrNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitIdentifierOrNull(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierOrNullContext identifierOrNull() throws RecognitionException {
		IdentifierOrNullContext _localctx = new IdentifierOrNullContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_identifierOrNull);
		try {
			setState(2566);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ACCESS:
			case ADD:
			case AFTER:
			case ALIAS:
			case ALL:
			case ALTER:
			case AND:
			case ANTI:
			case ANY:
			case ARRAY:
			case AS:
			case ASCENDING:
			case ASOF:
			case AST:
			case ASYNC:
			case ATTACH:
			case BETWEEN:
			case BOTH:
			case BY:
			case CACHES:
			case CASE:
			case CAST:
			case CHECK:
			case CLEAR:
			case CLUSTER:
			case CLUSTERS:
			case CODEC:
			case COLLATE:
			case COLUMN:
			case COLUMNS:
			case COMMENT:
			case CONSTRAINT:
			case CREATE:
			case CROSS:
			case CUBE:
			case CURRENT:
			case CURRENT_USER:
			case CHANGED:
			case DATABASE:
			case DATABASES:
			case DATE:
			case DAY:
			case DEDUPLICATE:
			case DEFAULT:
			case DELAY:
			case DELETE:
			case DESC:
			case DESCENDING:
			case DESCRIBE:
			case DETACH:
			case DICTIONARIES:
			case DICTIONARY:
			case DISK:
			case DISTINCT:
			case DISTRIBUTED:
			case DROP:
			case ELSE:
			case ENABLED:
			case END:
			case ENGINE:
			case ENGINES:
			case ESTIMATE:
			case EVENTS:
			case EXCEPT:
			case EXISTS:
			case EXPLAIN:
			case EXPRESSION:
			case EXTENDED:
			case EXTRACT:
			case FETCHES:
			case FIELDS:
			case FILESYSTEM:
			case FILL:
			case FINAL:
			case FIRST:
			case FLUSH:
			case FOLLOWING:
			case FOR:
			case FORMAT:
			case FREEZE:
			case FROM:
			case FULL:
			case FUNCTION:
			case FUNCTIONS:
			case GLOBAL:
			case GRANULARITY:
			case GRANTS:
			case GROUP:
			case GROUPING:
			case HAVING:
			case HIERARCHICAL:
			case HOUR:
			case ID:
			case IF:
			case ILIKE:
			case IMPLICIT:
			case IN:
			case INDEX:
			case INDEXES:
			case INDICES:
			case INJECTIVE:
			case INNER:
			case INSERT:
			case INTERPOLATE:
			case INTERVAL:
			case INTO:
			case IS:
			case IS_OBJECT_ID:
			case JOIN:
			case KEY:
			case KEYS:
			case KILL:
			case LAST:
			case LAYOUT:
			case LEADING:
			case LEFT:
			case LIFETIME:
			case LIKE:
			case LIMIT:
			case LIVE:
			case LOCAL:
			case LOGS:
			case MATERIALIZE:
			case MATERIALIZED:
			case MAX:
			case MERGES:
			case MICROSECOND:
			case MILLISECOND:
			case MIN:
			case MINUTE:
			case MODIFY:
			case MONTH:
			case MOVE:
			case MUTATION:
			case NANOSECOND:
			case NO:
			case NOT:
			case NULLS:
			case OFFSET:
			case ON:
			case OPTIMIZE:
			case OR:
			case ORDER:
			case OUTER:
			case OUTFILE:
			case OVER:
			case OVERRIDE:
			case PARTITION:
			case PIPELINE:
			case PLAN:
			case POLICY:
			case POLICIES:
			case POPULATE:
			case PRECEDING:
			case PREWHERE:
			case PRIMARY:
			case PRIVILEGES:
			case PROCESSLIST:
			case PROFILE:
			case PROFILES:
			case PROJECTION:
			case QUARTER:
			case QUOTA:
			case QUOTAS:
			case RANGE:
			case RECURSIVE:
			case RELOAD:
			case REMOVE:
			case RENAME:
			case REPLACE:
			case REPLICA:
			case REPLICATED:
			case RIGHT:
			case ROLE:
			case ROLES:
			case ROLLUP:
			case ROW:
			case ROWS:
			case SAMPLE:
			case SECOND:
			case SELECT:
			case SEMI:
			case SENDS:
			case SET:
			case SETTING:
			case SETTINGS:
			case SHOW:
			case SOURCE:
			case START:
			case STOP:
			case SUBSTRING:
			case SYNC:
			case SYNTAX:
			case SYSTEM:
			case STEP:
			case TABLE:
			case TABLES:
			case TEMPORARY:
			case TEST:
			case THEN:
			case TIES:
			case TIMEOUT:
			case TIMESTAMP:
			case TO:
			case TOP:
			case TOTALS:
			case TRAILING:
			case TREE:
			case TRIM:
			case TRUNCATE:
			case TTL:
			case TYPE:
			case UNBOUNDED:
			case UNION:
			case UPDATE:
			case USE:
			case USER:
			case USERS:
			case USING:
			case UUID:
			case VALUES:
			case VIEW:
			case VOLUME:
			case WATCH:
			case WEEK:
			case WHEN:
			case WHERE:
			case WINDOW:
			case WITH:
			case YEAR:
			case JSON_FALSE:
			case JSON_TRUE:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(2564);
				identifier();
				}
				break;
			case NULL_SQL:
				enterOuterAlt(_localctx, 2);
				{
				setState(2565);
				match(NULL_SQL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumValueContext extends ParserRuleContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode EQ_SINGLE() { return getToken(ClickHouseParser.EQ_SINGLE, 0); }
		public NumberLiteralContext numberLiteral() {
			return getRuleContext(NumberLiteralContext.class,0);
		}
		public EnumValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).enterEnumValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ClickHouseParserListener ) ((ClickHouseParserListener)listener).exitEnumValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ClickHouseParserVisitor ) return ((ClickHouseParserVisitor<? extends T>)visitor).visitEnumValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueContext enumValue() throws RecognitionException {
		EnumValueContext _localctx = new EnumValueContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_enumValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2568);
			stringLiteral();
			setState(2569);
			match(EQ_SINGLE);
			setState(2570);
			numberLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 13:
			return dictionaryAttrDfnt_sempred((DictionaryAttrDfntContext)_localctx, predIndex);
		case 14:
			return dictionaryEngineClause_sempred((DictionaryEngineClauseContext)_localctx, predIndex);
		case 27:
			return engineClause_sempred((EngineClauseContext)_localctx, predIndex);
		case 81:
			return joinExpr_sempred((JoinExprContext)_localctx, predIndex);
		case 107:
			return columnExpr_sempred((ColumnExprContext)_localctx, predIndex);
		case 114:
			return tableExpr_sempred((TableExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean dictionaryAttrDfnt_sempred(DictionaryAttrDfntContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return !_localctx.attrs.contains("default");
		case 1:
			return !_localctx.attrs.contains("expression");
		case 2:
			return !_localctx.attrs.contains("hierarchical");
		case 3:
			return !_localctx.attrs.contains("injective");
		case 4:
			return !_localctx.attrs.contains("is_object_id");
		}
		return true;
	}
	private boolean dictionaryEngineClause_sempred(DictionaryEngineClauseContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return !_localctx.clauses.contains("source");
		case 6:
			return !_localctx.clauses.contains("lifetime");
		case 7:
			return !_localctx.clauses.contains("layout");
		case 8:
			return !_localctx.clauses.contains("range");
		case 9:
			return !_localctx.clauses.contains("settings");
		}
		return true;
	}
	private boolean engineClause_sempred(EngineClauseContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return !_localctx.clauses.contains("orderByClause");
		case 11:
			return !_localctx.clauses.contains("partitionByClause");
		case 12:
			return !_localctx.clauses.contains("primaryKeyClause");
		case 13:
			return !_localctx.clauses.contains("sampleByClause");
		case 14:
			return !_localctx.clauses.contains("ttlClause");
		case 15:
			return !_localctx.clauses.contains("settingsClause");
		case 16:
			return !_localctx.clauses.contains("comment");
		}
		return true;
	}
	private boolean joinExpr_sempred(JoinExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 17:
			return precpred(_ctx, 3);
		case 18:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean columnExpr_sempred(ColumnExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 19:
			return precpred(_ctx, 23);
		case 20:
			return precpred(_ctx, 14);
		case 21:
			return precpred(_ctx, 13);
		case 22:
			return precpred(_ctx, 12);
		case 23:
			return precpred(_ctx, 10);
		case 24:
			return precpred(_ctx, 9);
		case 25:
			return precpred(_ctx, 8);
		case 26:
			return precpred(_ctx, 30);
		case 27:
			return precpred(_ctx, 17);
		case 28:
			return precpred(_ctx, 16);
		case 29:
			return precpred(_ctx, 11);
		case 30:
			return precpred(_ctx, 7);
		}
		return true;
	}
	private boolean tableExpr_sempred(TableExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 31:
			return precpred(_ctx, 1);
		}
		return true;
	}

	private static final int _serializedATNSegments = 2;
	private static final String _serializedATNSegment0 =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u011c\u0a0f\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\3\2\7\2\u010c\n\2\f\2\16\2\u010f\13\2\3\2\5\2\u0112\n\2\3\2\6"+
		"\2\u0115\n\2\r\2\16\2\u0116\3\2\7\2\u011a\n\2\f\2\16\2\u011d\13\2\3\2"+
		"\7\2\u0120\n\2\f\2\16\2\u0123\13\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3\u012b\n"+
		"\3\3\3\3\3\5\3\u012f\n\3\3\3\3\3\3\3\5\3\u0134\n\3\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0148\n\4\3\5"+
		"\3\5\3\5\3\5\5\5\u014e\n\5\3\5\3\5\3\5\7\5\u0153\n\5\f\5\16\5\u0156\13"+
		"\5\3\6\3\6\3\6\3\6\3\6\5\6\u015d\n\6\3\6\3\6\3\6\3\6\5\6\u0163\n\6\3\6"+
		"\3\6\3\6\3\6\3\6\5\6\u016a\n\6\3\6\3\6\3\6\5\6\u016f\n\6\3\6\3\6\3\6\3"+
		"\6\3\6\5\6\u0176\n\6\3\6\3\6\3\6\5\6\u017b\n\6\3\6\3\6\3\6\3\6\5\6\u0181"+
		"\n\6\3\6\3\6\3\6\3\6\5\6\u0187\n\6\3\6\3\6\3\6\5\6\u018c\n\6\3\6\3\6\3"+
		"\6\3\6\5\6\u0192\n\6\3\6\3\6\3\6\5\6\u0197\n\6\3\6\3\6\3\6\3\6\5\6\u019d"+
		"\n\6\3\6\3\6\3\6\5\6\u01a2\n\6\3\6\3\6\3\6\3\6\5\6\u01a8\n\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u01b6\n\6\3\6\3\6\3\6\3\6\3"+
		"\6\5\6\u01bd\n\6\3\6\3\6\3\6\3\6\3\6\5\6\u01c4\n\6\3\6\3\6\3\6\3\6\3\6"+
		"\5\6\u01cb\n\6\3\6\3\6\3\6\3\6\5\6\u01d1\n\6\3\6\3\6\3\6\5\6\u01d6\n\6"+
		"\3\6\3\6\3\6\3\6\5\6\u01dc\n\6\3\6\3\6\3\6\5\6\u01e1\n\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u01f4\n\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\5\6\u01fc\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6"+
		"\u0217\n\6\3\6\3\6\3\6\5\6\u021c\n\6\3\6\5\6\u021f\n\6\3\6\5\6\u0222\n"+
		"\6\3\6\5\6\u0225\n\6\3\6\5\6\u0228\n\6\3\6\3\6\3\6\5\6\u022d\n\6\3\6\5"+
		"\6\u0230\n\6\3\6\3\6\3\6\3\6\5\6\u0236\n\6\3\6\3\6\5\6\u023a\n\6\3\6\5"+
		"\6\u023d\n\6\3\6\5\6\u0240\n\6\3\6\5\6\u0243\n\6\3\6\5\6\u0246\n\6\3\6"+
		"\3\6\3\6\5\6\u024b\n\6\3\6\5\6\u024e\n\6\5\6\u0250\n\6\3\7\3\7\3\7\7\7"+
		"\u0255\n\7\f\7\16\7\u0258\13\7\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\n\5\n\u0265\n\n\3\13\3\13\3\13\3\13\5\13\u026b\n\13\3\f\3\f\3\f\3\f"+
		"\5\f\u0271\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u0278\n\r\3\r\3\r\5\r\u027c\n\r"+
		"\3\r\5\r\u027f\n\r\3\r\5\r\u0282\n\r\3\r\3\r\3\r\3\r\5\r\u0288\n\r\3\r"+
		"\5\r\u028b\n\r\3\r\3\r\3\r\3\r\5\r\u0291\n\r\3\r\3\r\5\r\u0295\n\r\3\r"+
		"\5\r\u0298\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u02a3\n\r\3\r\3"+
		"\r\5\r\u02a7\n\r\3\r\5\r\u02aa\n\r\3\r\5\r\u02ad\n\r\3\r\3\r\3\r\5\r\u02b2"+
		"\n\r\5\r\u02b4\n\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u02bc\n\r\3\r\5\r\u02bf"+
		"\n\r\3\r\5\r\u02c2\n\r\3\r\3\r\3\r\3\r\5\r\u02c8\n\r\3\r\3\r\5\r\u02cc"+
		"\n\r\3\r\5\r\u02cf\n\r\3\r\5\r\u02d2\n\r\3\r\5\r\u02d5\n\r\3\r\5\r\u02d8"+
		"\n\r\3\r\3\r\3\r\5\r\u02dd\n\r\3\r\3\r\3\r\3\r\5\r\u02e3\n\r\3\r\3\r\5"+
		"\r\u02e7\n\r\3\r\5\r\u02ea\n\r\3\r\5\r\u02ed\n\r\3\r\3\r\5\r\u02f1\n\r"+
		"\3\16\3\16\3\16\3\16\7\16\u02f7\n\16\f\16\16\16\u02fa\13\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\7\17\u0313\n\17\f\17\16\17\u0316\13"+
		"\17\3\20\5\20\u0319\n\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\7\20\u032f\n\20"+
		"\f\20\16\20\u0332\13\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\5\22\u033c"+
		"\n\22\3\22\5\22\u033f\n\22\3\23\3\23\3\23\3\23\3\23\7\23\u0346\n\23\f"+
		"\23\16\23\u0349\13\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\5\24\u0359\n\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25"+
		"\7\25\u0362\n\25\f\25\16\25\u0365\13\25\3\25\3\25\3\25\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u0376\n\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\5\30\u0383\n\30\3\31\3\31"+
		"\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u0392\n\34"+
		"\f\34\16\34\u0395\13\34\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u039d\n\34"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\7\35\u03bc\n\35\f\35\16\35\u03bf\13\35\3\36\3\36\3\36\3\36\3\37"+
		"\3\37\3\37\3\37\3 \3 \3 \3 \3!\3!\3!\3!\7!\u03d1\n!\f!\16!\u03d4\13!\3"+
		"\"\3\"\3\"\3#\3#\5#\u03db\n#\3#\3#\3#\5#\u03e0\n#\3#\5#\u03e3\n#\3$\3"+
		"$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\5$\u03f2\n$\3%\3%\3%\3%\3%\3%\3%\5"+
		"%\u03fb\n%\3%\5%\u03fe\n%\3%\3%\5%\u0402\n%\3%\5%\u0405\n%\3%\3%\5%\u0409"+
		"\n%\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3)\3)\7)\u041d"+
		"\n)\f)\16)\u0420\13)\3)\3)\3*\3*\3*\5*\u0427\n*\3*\5*\u042a\n*\3+\3+\3"+
		"+\5+\u042f\n+\3+\3+\3+\3+\3+\3+\5+\u0437\n+\3+\3+\3+\3+\3+\3+\7+\u043f"+
		"\n+\f+\16+\u0442\13+\5+\u0444\n+\3,\3,\3,\3,\3-\3-\5-\u044c\n-\3-\3-\3"+
		".\3.\3.\3.\5.\u0454\n.\3.\3.\5.\u0458\n.\3.\3.\3.\5.\u045d\n.\3.\3.\5"+
		".\u0461\n.\3.\3.\5.\u0465\n.\3.\3.\5.\u0469\n.\3.\3.\5.\u046d\n.\5.\u046f"+
		"\n.\3/\3/\3/\3/\3/\3/\5/\u0477\n/\3/\3/\5/\u047b\n/\3/\5/\u047e\n/\3\60"+
		"\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\5\60\u048a\n\60\3\60\5\60"+
		"\u048d\n\60\3\60\3\60\3\61\3\61\3\61\5\61\u0494\n\61\3\61\3\61\3\61\5"+
		"\61\u0499\n\61\3\61\5\61\u049c\n\61\3\61\3\61\3\62\3\62\3\62\3\62\7\62"+
		"\u04a4\n\62\f\62\16\62\u04a7\13\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\7\63\u04b1\n\63\f\63\16\63\u04b4\13\63\3\63\5\63\u04b7\n\63\3\64"+
		"\3\64\3\64\3\64\7\64\u04bd\n\64\f\64\16\64\u04c0\13\64\3\64\3\64\3\64"+
		"\3\64\5\64\u04c6\n\64\3\65\3\65\3\66\3\66\3\66\3\66\5\66\u04ce\n\66\3"+
		"\66\5\66\u04d1\n\66\3\66\5\66\u04d4\n\66\3\67\3\67\3\67\3\67\38\38\38"+
		"\38\38\58\u04df\n8\38\58\u04e2\n8\38\38\39\39\39\59\u04e9\n9\39\39\59"+
		"\u04ed\n9\3:\3:\3:\3:\5:\u04f3\n:\3:\5:\u04f6\n:\3:\5:\u04f9\n:\3:\5:"+
		"\u04fc\n:\3;\3;\3;\5;\u0501\n;\3<\3<\3<\3<\3<\3<\3<\3<\3<\7<\u050c\n<"+
		"\f<\16<\u050f\13<\3<\3<\3<\3<\3<\3<\3<\3<\3<\7<\u051a\n<\f<\16<\u051d"+
		"\13<\3<\3<\3<\3<\3<\3<\3<\3<\3<\7<\u0528\n<\f<\16<\u052b\13<\5<\u052d"+
		"\n<\3=\3=\5=\u0531\n=\3=\3=\3=\5=\u0536\n=\3=\5=\u0539\n=\3=\3=\3>\5>"+
		"\u053e\n>\3>\3>\3>\5>\u0543\n>\3>\3>\5>\u0547\n>\3>\7>\u054a\n>\f>\16"+
		">\u054d\13>\3?\3?\3?\3?\3?\5?\u0554\n?\3@\3@\5@\u0558\n@\3@\5@\u055b\n"+
		"@\3@\3@\5@\u055f\n@\3@\5@\u0562\n@\3@\5@\u0565\n@\3@\5@\u0568\n@\3@\5"+
		"@\u056b\n@\3@\5@\u056e\n@\3@\5@\u0571\n@\3@\3@\5@\u0575\n@\3@\3@\5@\u0579"+
		"\n@\3@\5@\u057c\n@\3@\5@\u057f\n@\3@\5@\u0582\n@\3@\5@\u0585\n@\3@\5@"+
		"\u0588\n@\3@\5@\u058b\n@\3A\3A\3A\3B\3B\3B\7B\u0593\nB\fB\16B\u0596\13"+
		"B\3C\5C\u0599\nC\3C\3C\3C\3C\3C\3C\3C\3C\3C\3C\5C\u05a5\nC\3D\3D\3D\3"+
		"D\5D\u05ab\nD\3E\3E\3E\3F\5F\u05b1\nF\3F\3F\3F\3F\3G\3G\3G\3G\3G\3G\3"+
		"G\3H\3H\3H\3I\3I\3I\3J\3J\3J\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3"+
		"K\3K\3K\3K\3K\3K\3K\3K\5K\u05dc\nK\3L\3L\3L\3M\3M\3M\3M\3N\3N\3N\3N\3"+
		"N\3O\3O\3O\3O\3P\3P\3P\3P\3P\3Q\3Q\3Q\3Q\5Q\u05f7\nQ\3R\3R\3R\3S\3S\3"+
		"S\5S\u05ff\nS\3S\5S\u0602\nS\3S\3S\3S\3S\5S\u0608\nS\3S\3S\3S\3S\3S\3"+
		"S\5S\u0610\nS\3S\5S\u0613\nS\3S\3S\3S\3S\7S\u0619\nS\fS\16S\u061c\13S"+
		"\3T\5T\u061f\nT\3T\3T\3T\5T\u0624\nT\3T\5T\u0627\nT\3T\5T\u062a\nT\3T"+
		"\3T\5T\u062e\nT\3T\3T\5T\u0632\nT\3T\5T\u0635\nT\5T\u0637\nT\3T\5T\u063a"+
		"\nT\3T\3T\5T\u063e\nT\3T\3T\5T\u0642\nT\3T\5T\u0645\nT\5T\u0647\nT\5T"+
		"\u0649\nT\3U\5U\u064c\nU\3U\3U\3U\5U\u0651\nU\3V\3V\3V\3V\3V\3V\3V\3V"+
		"\3V\5V\u065c\nV\3W\3W\3W\3W\5W\u0662\nW\3X\3X\3X\5X\u0667\nX\3Y\3Y\3Y"+
		"\7Y\u066c\nY\fY\16Y\u066f\13Y\3Z\3Z\5Z\u0673\nZ\3Z\3Z\5Z\u0677\nZ\3Z\3"+
		"Z\5Z\u067b\nZ\3Z\3Z\3Z\3Z\5Z\u0681\nZ\3Z\3Z\5Z\u0685\nZ\3Z\3Z\5Z\u0689"+
		"\nZ\5Z\u068b\nZ\3[\3[\3[\5[\u0690\n[\3\\\3\\\3\\\7\\\u0695\n\\\f\\\16"+
		"\\\u0698\13\\\3]\3]\3]\3]\3^\5^\u069f\n^\3^\5^\u06a2\n^\3^\5^\u06a5\n"+
		"^\3_\3_\3_\3_\3`\3`\3`\3`\3a\3a\3a\3b\3b\3b\3b\3b\3b\5b\u06b8\nb\3c\3"+
		"c\3c\3c\3c\3c\3c\3c\3c\3c\3c\3c\5c\u06c6\nc\3d\3d\3d\3e\3e\3e\3e\3e\3"+
		"e\3e\3e\3e\3e\5e\u06d5\ne\3e\3e\3e\3e\3e\5e\u06dc\ne\3e\3e\3e\3e\5e\u06e2"+
		"\ne\3e\3e\5e\u06e6\ne\3e\3e\3e\3e\5e\u06ec\ne\3e\5e\u06ef\ne\3e\3e\3e"+
		"\3e\5e\u06f5\ne\3e\3e\5e\u06f9\ne\3e\3e\5e\u06fd\ne\3e\5e\u0700\ne\3e"+
		"\3e\3e\5e\u0705\ne\3e\5e\u0708\ne\3e\3e\3e\3e\5e\u070e\ne\3e\3e\5e\u0712"+
		"\ne\3e\3e\5e\u0716\ne\3e\5e\u0719\ne\3e\3e\3e\3e\3e\3e\3e\5e\u0722\ne"+
		"\3e\5e\u0725\ne\3e\3e\3e\3e\5e\u072b\ne\3e\3e\5e\u072f\ne\3e\3e\5e\u0733"+
		"\ne\3e\3e\3e\3e\3e\3e\3e\5e\u073c\ne\3e\3e\5e\u0740\ne\3e\3e\3e\3e\3e"+
		"\3e\3e\3e\7e\u074a\ne\fe\16e\u074d\13e\5e\u074f\ne\3e\3e\5e\u0753\ne\3"+
		"e\5e\u0756\ne\3e\3e\3e\3e\3e\3e\7e\u075e\ne\fe\16e\u0761\13e\3e\5e\u0764"+
		"\ne\3e\3e\3e\3e\3e\3e\7e\u076c\ne\fe\16e\u076f\13e\3e\3e\3e\5e\u0774\n"+
		"e\3e\3e\3e\3e\3e\3e\7e\u077c\ne\fe\16e\u077f\13e\3e\3e\3e\3e\3e\3e\7e"+
		"\u0787\ne\fe\16e\u078a\13e\3e\5e\u078d\ne\3e\3e\3e\5e\u0792\ne\3e\3e\3"+
		"e\3e\7e\u0798\ne\fe\16e\u079b\13e\3e\3e\3e\3e\5e\u07a1\ne\3e\3e\3e\5e"+
		"\u07a6\ne\3e\3e\3e\5e\u07ab\ne\3e\3e\3e\5e\u07b0\ne\3e\3e\3e\3e\5e\u07b6"+
		"\ne\3e\3e\3e\3e\3e\3e\3e\3e\3e\5e\u07c1\ne\3e\3e\5e\u07c5\ne\3e\3e\5e"+
		"\u07c9\ne\3e\3e\5e\u07cd\ne\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e"+
		"\3e\5e\u07de\ne\3e\3e\3e\5e\u07e3\ne\3e\3e\5e\u07e7\ne\3e\3e\5e\u07eb"+
		"\ne\3e\3e\3e\3e\3e\5e\u07f2\ne\3e\3e\5e\u07f6\ne\3f\3f\3f\3f\3f\3f\3f"+
		"\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\5f\u080c\nf\3f\5f\u080f\nf\3f"+
		"\3f\3f\3f\3f\3f\3f\3f\3f\5f\u081a\nf\3g\3g\5g\u081e\ng\3g\5g\u0821\ng"+
		"\3g\3g\5g\u0825\ng\3g\3g\5g\u0829\ng\3h\3h\3h\3i\3i\3i\5i\u0831\ni\3i"+
		"\3i\5i\u0835\ni\3j\3j\3j\3j\3j\3j\3j\3j\3j\7j\u0840\nj\fj\16j\u0843\13"+
		"j\3j\3j\3j\3j\3j\3j\3j\7j\u084c\nj\fj\16j\u084f\13j\3j\3j\3j\3j\3j\3j"+
		"\3j\7j\u0858\nj\fj\16j\u085b\13j\3j\3j\3j\3j\3j\5j\u0862\nj\3j\3j\5j\u0866"+
		"\nj\3k\3k\3k\7k\u086b\nk\fk\16k\u086e\13k\3l\3l\3l\5l\u0873\nl\3l\3l\5"+
		"l\u0877\nl\3l\3l\3l\3l\3l\5l\u087e\nl\3m\3m\3m\5m\u0883\nm\3m\3m\3m\3"+
		"m\3m\6m\u088a\nm\rm\16m\u088b\3m\3m\5m\u0890\nm\3m\3m\3m\3m\3m\3m\3m\3"+
		"m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\5m\u08af"+
		"\nm\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\5m\u08c2\nm\3m"+
		"\3m\3m\3m\3m\3m\3m\3m\3m\3m\5m\u08ce\nm\3m\3m\3m\3m\3m\3m\3m\3m\5m\u08d8"+
		"\nm\3m\5m\u08db\nm\3m\3m\5m\u08df\nm\3m\5m\u08e2\nm\3m\3m\3m\3m\3m\3m"+
		"\3m\3m\5m\u08ec\nm\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\5m\u08f9\nm\3m\3m"+
		"\3m\5m\u08fe\nm\3m\3m\5m\u0902\nm\3m\3m\5m\u0906\nm\3m\3m\3m\3m\3m\3m"+
		"\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\5m\u091c\nm\3m\5m\u091f\nm"+
		"\3m\3m\5m\u0923\nm\3m\5m\u0926\nm\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m"+
		"\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\5m\u0942\nm\3m\3m\3m\5m\u0947"+
		"\nm\3m\3m\3m\3m\3m\5m\u094e\nm\7m\u0950\nm\fm\16m\u0953\13m\3n\3n\3n\7"+
		"n\u0958\nn\fn\16n\u095b\13n\3o\3o\5o\u095f\no\3p\3p\3p\3p\7p\u0965\np"+
		"\fp\16p\u0968\13p\3p\3p\3p\3p\3p\7p\u096f\np\fp\16p\u0972\13p\5p\u0974"+
		"\np\3p\3p\3p\3q\3q\3q\5q\u097c\nq\3q\3q\3r\3r\3r\7r\u0983\nr\fr\16r\u0986"+
		"\13r\3s\3s\3s\3s\3s\3s\5s\u098e\ns\3s\3s\3s\3s\3s\3s\7s\u0996\ns\fs\16"+
		"s\u0999\13s\3s\3s\5s\u099d\ns\5s\u099f\ns\3t\3t\3t\3t\3t\3t\3t\5t\u09a8"+
		"\nt\3t\3t\3t\3t\5t\u09ae\nt\7t\u09b0\nt\ft\16t\u09b3\13t\3u\3u\3u\5u\u09b8"+
		"\nu\3u\3u\3v\3v\3v\5v\u09bf\nv\3v\3v\3w\3w\3w\7w\u09c6\nw\fw\16w\u09c9"+
		"\13w\3x\3x\3x\5x\u09ce\nx\3y\3y\3z\3z\3z\5z\u09d5\nz\3z\3z\3{\3{\3{\3"+
		"{\3{\3{\5{\u09df\n{\5{\u09e1\n{\3|\5|\u09e4\n|\3|\3|\3|\3|\3|\3|\3|\5"+
		"|\u09ed\n|\3}\3}\3~\3~\3~\3~\3~\5~\u09f6\n~\3\177\3\177\3\u0080\3\u0080"+
		"\3\u0081\3\u0081\3\u0082\3\u0082\5\u0082\u0a00\n\u0082\3\u0083\3\u0083"+
		"\3\u0083\5\u0083\u0a05\n\u0083\3\u0084\3\u0084\5\u0084\u0a09\n\u0084\3"+
		"\u0085\3\u0085\3\u0085\3\u0085\3\u0085\2\5\u00a4\u00d8\u00e6\u0086\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP"+
		"RTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e"+
		"\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6"+
		"\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be"+
		"\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6"+
		"\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee"+
		"\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106"+
		"\u0108\2#\b\2\6\6\35\35!!..\u0080\u0080\u00dc\u00dc\4\2\22\22##\5\2\6"+
		"\6..\u0080\u0080\4\2\61\61\63\63\4\2\64\64::\5\2\21\21\u00c9\u00c9\u00d0"+
		"\u00d0\4\2\7\788\4\2%%\u00b8\u00b8\4\2jjxx\4\2XX}}\5\2\7\7\13\13\17\17"+
		"\6\2\7\7\n\13\17\17\u00be\u00be\4\2xx\u00b5\u00b5\4\2\7\7\13\13\4\2\u0092"+
		"\u0092\u0101\u0101\4\2\16\16\61\62\4\2NNuu\4\2\u00ad\u00ad\u00ba\u00ba"+
		"\4\2bbzz\4\2TTdd\4\2  JJ\4\2egss\4\2&&<<\3\2\u00c6\u00c7\5\2\24\24ww\u00d8"+
		"\u00d8\5\2\u00fc\u00fc\u010f\u010f\u0118\u0118\4\2\u0102\u0103\u0110\u0110"+
		"\3\2\u00f6\u00f7\4\2\u0103\u0103\u0110\u0110\4\2\u00f2\u00f3\u00fa\u00fa"+
		"\f\2,,__\u0083\u0084\u0086\u0086\u0088\u0088\u008c\u008c\u00aa\u00aa\u00bc"+
		"\u00bc\u00ea\u00ea\u00ef\u00ef\n\2\3BDgi\u0087\u0089\u008a\u008d\u008e"+
		"\u0091\u00a2\u00a4\u00c0\u00c2\u00f18\2\5\6\b\b\20\22\24\24\27\33\35\35"+
		"\37\37!#%&)+-/\63\679:>>AADFHIOPSSVVYY^^`aeeiikkpprrttvwyy||~\u0082\u0085"+
		"\u0085\u0087\u0087\u0089\u008a\u008d\u008d\u0094\u0094\u0098\u0099\u009b"+
		"\u009b\u00a0\u00a1\u00a4\u00a4\u00ad\u00ad\u00af\u00b4\u00b8\u00b9\u00bd"+
		"\u00bd\u00bf\u00c0\u00c4\u00cb\u00cd\u00d0\u00d2\u00d4\u00d7\u00d8\u00da"+
		"\u00de\u00e0\u00e1\u00e5\u00e9\2\u0ba1\2\u010d\3\2\2\2\4\u0133\3\2\2\2"+
		"\6\u0147\3\2\2\2\b\u0149\3\2\2\2\n\u024f\3\2\2\2\f\u0251\3\2\2\2\16\u0259"+
		"\3\2\2\2\20\u025d\3\2\2\2\22\u0264\3\2\2\2\24\u0266\3\2\2\2\26\u026c\3"+
		"\2\2\2\30\u02f0\3\2\2\2\32\u02f2\3\2\2\2\34\u02fd\3\2\2\2\36\u0318\3\2"+
		"\2\2 \u0333\3\2\2\2\"\u0337\3\2\2\2$\u0340\3\2\2\2&\u034d\3\2\2\2(\u035c"+
		"\3\2\2\2*\u0369\3\2\2\2,\u0379\3\2\2\2.\u037e\3\2\2\2\60\u0384\3\2\2\2"+
		"\62\u0387\3\2\2\2\64\u038a\3\2\2\2\66\u039c\3\2\2\28\u039e\3\2\2\2:\u03c0"+
		"\3\2\2\2<\u03c4\3\2\2\2>\u03c8\3\2\2\2@\u03cc\3\2\2\2B\u03d5\3\2\2\2D"+
		"\u03d8\3\2\2\2F\u03f1\3\2\2\2H\u03f3\3\2\2\2J\u040a\3\2\2\2L\u040d\3\2"+
		"\2\2N\u0414\3\2\2\2P\u0417\3\2\2\2R\u0423\3\2\2\2T\u0443\3\2\2\2V\u0445"+
		"\3\2\2\2X\u0449\3\2\2\2Z\u046e\3\2\2\2\\\u047d\3\2\2\2^\u047f\3\2\2\2"+
		"`\u0490\3\2\2\2b\u049f\3\2\2\2d\u04b6\3\2\2\2f\u04c5\3\2\2\2h\u04c7\3"+
		"\2\2\2j\u04c9\3\2\2\2l\u04d5\3\2\2\2n\u04d9\3\2\2\2p\u04e5\3\2\2\2r\u04ee"+
		"\3\2\2\2t\u04fd\3\2\2\2v\u052c\3\2\2\2x\u052e\3\2\2\2z\u053d\3\2\2\2|"+
		"\u0553\3\2\2\2~\u0555\3\2\2\2\u0080\u058c\3\2\2\2\u0082\u058f\3\2\2\2"+
		"\u0084\u05a4\3\2\2\2\u0086\u05a6\3\2\2\2\u0088\u05ac\3\2\2\2\u008a\u05b0"+
		"\3\2\2\2\u008c\u05b6\3\2\2\2\u008e\u05bd\3\2\2\2\u0090\u05c0\3\2\2\2\u0092"+
		"\u05c3\3\2\2\2\u0094\u05db\3\2\2\2\u0096\u05dd\3\2\2\2\u0098\u05e0\3\2"+
		"\2\2\u009a\u05e4\3\2\2\2\u009c\u05e9\3\2\2\2\u009e\u05ed\3\2\2\2\u00a0"+
		"\u05f2\3\2\2\2\u00a2\u05f8\3\2\2\2\u00a4\u0607\3\2\2\2\u00a6\u0648\3\2"+
		"\2\2\u00a8\u0650\3\2\2\2\u00aa\u065b\3\2\2\2\u00ac\u065d\3\2\2\2\u00ae"+
		"\u0663\3\2\2\2\u00b0\u0668\3\2\2\2\u00b2\u0670\3\2\2\2\u00b4\u068c\3\2"+
		"\2\2\u00b6\u0691\3\2\2\2\u00b8\u0699\3\2\2\2\u00ba\u069e\3\2\2\2\u00bc"+
		"\u06a6\3\2\2\2\u00be\u06aa\3\2\2\2\u00c0\u06ae\3\2\2\2\u00c2\u06b7\3\2"+
		"\2\2\u00c4\u06c5\3\2\2\2\u00c6\u06c7\3\2\2\2\u00c8\u07f5\3\2\2\2\u00ca"+
		"\u0819\3\2\2\2\u00cc\u081b\3\2\2\2\u00ce\u082a\3\2\2\2\u00d0\u082d\3\2"+
		"\2\2\u00d2\u0865\3\2\2\2\u00d4\u0867\3\2\2\2\u00d6\u087d\3\2\2\2\u00d8"+
		"\u0901\3\2\2\2\u00da\u0954\3\2\2\2\u00dc\u095e\3\2\2\2\u00de\u0973\3\2"+
		"\2\2\u00e0\u097b\3\2\2\2\u00e2\u097f\3\2\2\2\u00e4\u099e\3\2\2\2\u00e6"+
		"\u09a7\3\2\2\2\u00e8\u09b4\3\2\2\2\u00ea\u09be\3\2\2\2\u00ec\u09c2\3\2"+
		"\2\2\u00ee\u09cd\3\2\2\2\u00f0\u09cf\3\2\2\2\u00f2\u09d4\3\2\2\2\u00f4"+
		"\u09e0\3\2\2\2\u00f6\u09e3\3\2\2\2\u00f8\u09ee\3\2\2\2\u00fa\u09f5\3\2"+
		"\2\2\u00fc\u09f7\3\2\2\2\u00fe\u09f9\3\2\2\2\u0100\u09fb\3\2\2\2\u0102"+
		"\u09ff\3\2\2\2\u0104\u0a04\3\2\2\2\u0106\u0a08\3\2\2\2\u0108\u0a0a\3\2"+
		"\2\2\u010a\u010c\7\u0117\2\2\u010b\u010a\3\2\2\2\u010c\u010f\3\2\2\2\u010d"+
		"\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u0111\3\2\2\2\u010f\u010d\3\2"+
		"\2\2\u0110\u0112\5\4\3\2\u0111\u0110\3\2\2\2\u0111\u0112\3\2\2\2\u0112"+
		"\u011b\3\2\2\2\u0113\u0115\7\u0117\2\2\u0114\u0113\3\2\2\2\u0115\u0116"+
		"\3\2\2\2\u0116\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u0118\3\2\2\2\u0118"+
		"\u011a\5\4\3\2\u0119\u0114\3\2\2\2\u011a\u011d\3\2\2\2\u011b\u0119\3\2"+
		"\2\2\u011b\u011c\3\2\2\2\u011c\u0121\3\2\2\2\u011d\u011b\3\2\2\2\u011e"+
		"\u0120\7\u0117\2\2\u011f\u011e\3\2\2\2\u0120\u0123\3\2\2\2\u0121\u011f"+
		"\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0124\3\2\2\2\u0123\u0121\3\2\2\2\u0124"+
		"\u0125\7\2\2\3\u0125\3\3\2\2\2\u0126\u012a\5\6\4\2\u0127\u0128\7n\2\2"+
		"\u0128\u0129\7\u0098\2\2\u0129\u012b\5\u00f8}\2\u012a\u0127\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012e\3\2\2\2\u012c\u012d\7R\2\2\u012d\u012f\5\u0106"+
		"\u0084\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u0134\3\2\2\2\u0130"+
		"\u0134\5`\61\2\u0131\u0134\5j\66\2\u0132\u0134\5n8\2\u0133\u0126\3\2\2"+
		"\2\u0133\u0130\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0132\3\2\2\2\u0134\5"+
		"\3\2\2\2\u0135\u0148\5\b\5\2\u0136\u0148\5\24\13\2\u0137\u0148\5\26\f"+
		"\2\u0138\u0148\5\30\r\2\u0139\u0148\5X-\2\u013a\u0148\5Z.\2\u013b\u0148"+
		"\5\\/\2\u013c\u0148\5^\60\2\u013d\u0148\5p9\2\u013e\u0148\5r:\2\u013f"+
		"\u0148\5t;\2\u0140\u0148\5z>\2\u0141\u0148\5\u00c6d\2\u0142\u0148\5\u00c8"+
		"e\2\u0143\u0148\5\u00caf\2\u0144\u0148\5\u00ccg\2\u0145\u0148\5\u00ce"+
		"h\2\u0146\u0148\5\u00d0i\2\u0147\u0135\3\2\2\2\u0147\u0136\3\2\2\2\u0147"+
		"\u0137\3\2\2\2\u0147\u0138\3\2\2\2\u0147\u0139\3\2\2\2\u0147\u013a\3\2"+
		"\2\2\u0147\u013b\3\2\2\2\u0147\u013c\3\2\2\2\u0147\u013d\3\2\2\2\u0147"+
		"\u013e\3\2\2\2\u0147\u013f\3\2\2\2\u0147\u0140\3\2\2\2\u0147\u0141\3\2"+
		"\2\2\u0147\u0142\3\2\2\2\u0147\u0143\3\2\2\2\u0147\u0144\3\2\2\2\u0147"+
		"\u0145\3\2\2\2\u0147\u0146\3\2\2\2\u0148\7\3\2\2\2\u0149\u014a\7\b\2\2"+
		"\u014a\u014b\7\u00cd\2\2\u014b\u014d\5\u00eav\2\u014c\u014e\5.\30\2\u014d"+
		"\u014c\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u014f\3\2\2\2\u014f\u0154\5\n"+
		"\6\2\u0150\u0151\7\u0101\2\2\u0151\u0153\5\n\6\2\u0152\u0150\3\2\2\2\u0153"+
		"\u0156\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155\t\3\2\2\2"+
		"\u0156\u0154\3\2\2\2\u0157\u0158\7\4\2\2\u0158\u015c\7\37\2\2\u0159\u015a"+
		"\7a\2\2\u015a\u015b\7\u008e\2\2\u015b\u015d\7D\2\2\u015c\u0159\3\2\2\2"+
		"\u015c\u015d\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u0162\5H%\2\u015f\u0160"+
		"\7\5\2\2\u0160\u0163\5\u00e2r\2\u0161\u0163\7N\2\2\u0162\u015f\3\2\2\2"+
		"\u0162\u0161\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0250\3\2\2\2\u0164\u0165"+
		"\7\4\2\2\u0165\u0169\7e\2\2\u0166\u0167\7a\2\2\u0167\u0168\7\u008e\2\2"+
		"\u0168\u016a\7D\2\2\u0169\u0166\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b"+
		"\3\2\2\2\u016b\u016e\5L\'\2\u016c\u016d\7\5\2\2\u016d\u016f\5\u00e2r\2"+
		"\u016e\u016c\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0250\3\2\2\2\u0170\u0171"+
		"\7\4\2\2\u0171\u0175\7\u00a9\2\2\u0172\u0173\7a\2\2\u0173\u0174\7\u008e"+
		"\2\2\u0174\u0176\7D\2\2\u0175\u0172\3\2\2\2\u0175\u0176\3\2\2\2\u0176"+
		"\u0177\3\2\2\2\u0177\u017a\5N(\2\u0178\u0179\7\5\2\2\u0179\u017b\5\u00e2"+
		"r\2\u017a\u0178\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u0250\3\2\2\2\u017c"+
		"\u017d\7\22\2\2\u017d\u0180\5\22\n\2\u017e\u017f\7T\2\2\u017f\u0181\5"+
		"\u00eav\2\u0180\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0250\3\2\2\2"+
		"\u0182\u0183\7\32\2\2\u0183\u0186\7\37\2\2\u0184\u0185\7a\2\2\u0185\u0187"+
		"\7D\2\2\u0186\u0184\3\2\2\2\u0186\u0187\3\2\2\2\u0187\u0188\3\2\2\2\u0188"+
		"\u018b\5\u00e2r\2\u0189\u018a\7d\2\2\u018a\u018c\5\22\n\2\u018b\u0189"+
		"\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u0250\3\2\2\2\u018d\u018e\7\32\2\2"+
		"\u018e\u0191\7e\2\2\u018f\u0190\7a\2\2\u0190\u0192\7D\2\2\u0191\u018f"+
		"\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0196\5\u00e2r"+
		"\2\u0194\u0195\7d\2\2\u0195\u0197\5\22\n\2\u0196\u0194\3\2\2\2\u0196\u0197"+
		"\3\2\2\2\u0197\u0250\3\2\2\2\u0198\u0199\7\32\2\2\u0199\u019c\7\u00a9"+
		"\2\2\u019a\u019b\7a\2\2\u019b\u019d\7D\2\2\u019c\u019a\3\2\2\2\u019c\u019d"+
		"\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u01a1\5\u00e2r\2\u019f\u01a0\7d\2\2"+
		"\u01a0\u01a2\5\22\n\2\u01a1\u019f\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u0250"+
		"\3\2\2\2\u01a3\u01a4\7!\2\2\u01a4\u01a7\7\37\2\2\u01a5\u01a6\7a\2\2\u01a6"+
		"\u01a8\7D\2\2\u01a7\u01a5\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\3\2"+
		"\2\2\u01a9\u01aa\5\u0104\u0083\2\u01aa\u01ab\5\u00f8}\2\u01ab\u0250\3"+
		"\2\2\2\u01ac\u01ad\7\60\2\2\u01ad\u01ae\7\u00ec\2\2\u01ae\u0250\5\u00d8"+
		"m\2\u01af\u01b0\7\64\2\2\u01b0\u0250\5\22\n\2\u01b1\u01b2\7:\2\2\u01b2"+
		"\u01b5\7\37\2\2\u01b3\u01b4\7a\2\2\u01b4\u01b6\7D\2\2\u01b5\u01b3\3\2"+
		"\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7\u0250\5\u0104\u0083"+
		"\2\u01b8\u01b9\7:\2\2\u01b9\u01bc\7e\2\2\u01ba\u01bb\7a\2\2\u01bb\u01bd"+
		"\7D\2\2\u01bc\u01ba\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2\2\2\u01be"+
		"\u0250\5\u00e2r\2\u01bf\u01c0\7:\2\2\u01c0\u01c3\7\u00a9\2\2\u01c1\u01c2"+
		"\7a\2\2\u01c2\u01c4\7D\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4"+
		"\u01c5\3\2\2\2\u01c5\u0250\5\u00e2r\2\u01c6\u01c7\7:\2\2\u01c7\u0250\5"+
		"\22\n\2\u01c8\u01ca\7S\2\2\u01c9\u01cb\5\22\n\2\u01ca\u01c9\3\2\2\2\u01ca"+
		"\u01cb\3\2\2\2\u01cb\u0250\3\2\2\2\u01cc\u01cd\7\177\2\2\u01cd\u01d0\7"+
		"e\2\2\u01ce\u01cf\7a\2\2\u01cf\u01d1\7D\2\2\u01d0\u01ce\3\2\2\2\u01d0"+
		"\u01d1\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d5\5\u00e2r\2\u01d3\u01d4"+
		"\7d\2\2\u01d4\u01d6\5\22\n\2\u01d5\u01d3\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6"+
		"\u0250\3\2\2\2\u01d7\u01d8\7\177\2\2\u01d8\u01db\7\u00a9\2\2\u01d9\u01da"+
		"\7a\2\2\u01da\u01dc\7D\2\2\u01db\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc"+
		"\u01dd\3\2\2\2\u01dd\u01e0\5\u00e2r\2\u01de\u01df\7d\2\2\u01df\u01e1\5"+
		"\22\n\2\u01e0\u01de\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u0250\3\2\2\2\u01e2"+
		"\u01e3\7\u0087\2\2\u01e3\u01e4\7\u0096\2\2\u01e4\u01e5\7\25\2\2\u01e5"+
		"\u0250\5\u00d8m\2\u01e6\u01e7\7\u0087\2\2\u01e7\u0250\5@!\2\u01e8\u01e9"+
		"\7\u0089\2\2\u01e9\u01f3\5\22\n\2\u01ea\u01eb\7\u00d5\2\2\u01eb\u01ec"+
		"\7\67\2\2\u01ec\u01f4\5\u00f8}\2\u01ed\u01ee\7\u00d5\2\2\u01ee\u01ef\7"+
		"\u00e8\2\2\u01ef\u01f4\5\u00f8}\2\u01f0\u01f1\7\u00d5\2\2\u01f1\u01f2"+
		"\7\u00cd\2\2\u01f2\u01f4\5\u00eav\2\u01f3\u01ea\3\2\2\2\u01f3\u01ed\3"+
		"\2\2\2\u01f3\u01f0\3\2\2\2\u01f4\u0250\3\2\2\2\u01f5\u01f6\7\u00b0\2\2"+
		"\u01f6\u0250\7\u00dc\2\2\u01f7\u01f8\7\u00b1\2\2\u01f8\u01fb\7\37\2\2"+
		"\u01f9\u01fa\7a\2\2\u01fa\u01fc\7D\2\2\u01fb\u01f9\3\2\2\2\u01fb\u01fc"+
		"\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01fe\5\u0104\u0083\2\u01fe\u01ff\7"+
		"\u00d5\2\2\u01ff\u0200\5\u0104\u0083\2\u0200\u0250\3\2\2\2\u0201\u0202"+
		"\7\u00b2\2\2\u0202\u0203\5\22\n\2\u0203\u0204\7T\2\2\u0204\u0205\5\u00ea"+
		"v\2\u0205\u0250\3\2\2\2\u0206\u0207\7\u00e0\2\2\u0207\u0208\5\f\7\2\u0208"+
		"\u0209\5\u0092J\2\u0209\u0250\3\2\2\2\u020a\u020b\7\u0087\2\2\u020b\u0250"+
		"\5B\"\2\u020c\u020d\7\u0087\2\2\u020d\u020e\7\37\2\2\u020e\u020f\5\u0104"+
		"\u0083\2\u020f\u0210\7.\2\2\u0210\u0211\5\u00fa~\2\u0211\u0250\3\2\2\2"+
		"\u0212\u0213\7\b\2\2\u0213\u0216\7\37\2\2\u0214\u0215\7a\2\2\u0215\u0217"+
		"\7D\2\2\u0216\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0218\3\2\2\2\u0218"+
		"\u0219\5\u0104\u0083\2\u0219\u021b\7\u00dd\2\2\u021a\u021c\5\u00d2j\2"+
		"\u021b\u021a\3\2\2\2\u021b\u021c\3\2\2\2\u021c\u021e\3\2\2\2\u021d\u021f"+
		"\5J&\2\u021e\u021d\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0221\3\2\2\2\u0220"+
		"\u0222\5B\"\2\u0221\u0220\3\2\2\2\u0221\u0222\3\2\2\2\u0222\u0224\3\2"+
		"\2\2\u0223\u0225\5P)\2\u0224\u0223\3\2\2\2\u0224\u0225\3\2\2\2\u0225\u0227"+
		"\3\2\2\2\u0226\u0228\5@!\2\u0227\u0226\3\2\2\2\u0227\u0228\3\2\2\2\u0228"+
		"\u022c\3\2\2\2\u0229\u022a\7\5\2\2\u022a\u022d\5\u00e2r\2\u022b\u022d"+
		"\7N\2\2\u022c\u0229\3\2\2\2\u022c\u022b\3\2\2\2\u022c\u022d\3\2\2\2\u022d"+
		"\u022f\3\2\2\2\u022e\u0230\5\u00b6\\\2\u022f\u022e\3\2\2\2\u022f\u0230"+
		"\3\2\2\2\u0230\u0250\3\2\2\2\u0231\u0232\7\u0087\2\2\u0232\u0235\7\37"+
		"\2\2\u0233\u0234\7a\2\2\u0234\u0236\7D\2\2\u0235\u0233\3\2\2\2\u0235\u0236"+
		"\3\2\2\2\u0236\u0237\3\2\2\2\u0237\u0239\5\u0104\u0083\2\u0238\u023a\5"+
		"\u00d2j\2\u0239\u0238\3\2\2\2\u0239\u023a\3\2\2\2\u023a\u023c\3\2\2\2"+
		"\u023b\u023d\5J&\2\u023c\u023b\3\2\2\2\u023c\u023d\3\2\2\2\u023d\u023f"+
		"\3\2\2\2\u023e\u0240\5B\"\2\u023f\u023e\3\2\2\2\u023f\u0240\3\2\2\2\u0240"+
		"\u0242\3\2\2\2\u0241\u0243\5P)\2\u0242\u0241\3\2\2\2\u0242\u0243\3\2\2"+
		"\2\u0243\u0245\3\2\2\2\u0244\u0246\5@!\2\u0245\u0244\3\2\2\2\u0245\u0246"+
		"\3\2\2\2\u0246\u024a\3\2\2\2\u0247\u0248\7\5\2\2\u0248\u024b\5\u00e2r"+
		"\2\u0249\u024b\7N\2\2\u024a\u0247\3\2\2\2\u024a\u0249\3\2\2\2\u024a\u024b"+
		"\3\2\2\2\u024b\u024d\3\2\2\2\u024c\u024e\5\u00b6\\\2\u024d\u024c\3\2\2"+
		"\2\u024d\u024e\3\2\2\2\u024e\u0250\3\2\2\2\u024f\u0157\3\2\2\2\u024f\u0164"+
		"\3\2\2\2\u024f\u0170\3\2\2\2\u024f\u017c\3\2\2\2\u024f\u0182\3\2\2\2\u024f"+
		"\u018d\3\2\2\2\u024f\u0198\3\2\2\2\u024f\u01a3\3\2\2\2\u024f\u01ac\3\2"+
		"\2\2\u024f\u01af\3\2\2\2\u024f\u01b1\3\2\2\2\u024f\u01b8\3\2\2\2\u024f"+
		"\u01bf\3\2\2\2\u024f\u01c6\3\2\2\2\u024f\u01c8\3\2\2\2\u024f\u01cc\3\2"+
		"\2\2\u024f\u01d7\3\2\2\2\u024f\u01e2\3\2\2\2\u024f\u01e6\3\2\2\2\u024f"+
		"\u01e8\3\2\2\2\u024f\u01f5\3\2\2\2\u024f\u01f7\3\2\2\2\u024f\u0201\3\2"+
		"\2\2\u024f\u0206\3\2\2\2\u024f\u020a\3\2\2\2\u024f\u020c\3\2\2\2\u024f"+
		"\u0212\3\2\2\2\u024f\u0231\3\2\2\2\u0250\13\3\2\2\2\u0251\u0256\5\16\b"+
		"\2\u0252\u0253\7\u0101\2\2\u0253\u0255\5\16\b\2\u0254\u0252\3\2\2\2\u0255"+
		"\u0258\3\2\2\2\u0256\u0254\3\2\2\2\u0256\u0257\3\2\2\2\u0257\r\3\2\2\2"+
		"\u0258\u0256\3\2\2\2\u0259\u025a\5\u00e2r\2\u025a\u025b\7\u0106\2\2\u025b"+
		"\u025c\5\u00d8m\2\u025c\17\3\2\2\2\u025d\u025e\t\2\2\2\u025e\21\3\2\2"+
		"\2\u025f\u0260\7\u009b\2\2\u0260\u0265\5\u00d8m\2\u0261\u0262\7\u009b"+
		"\2\2\u0262\u0263\7`\2\2\u0263\u0265\5\u00f8}\2\u0264\u025f\3\2\2\2\u0264"+
		"\u0261\3\2\2\2\u0265\23\3\2\2\2\u0266\u0267\7\22\2\2\u0267\u0268\7\66"+
		"\2\2\u0268\u026a\5\u00eav\2\u0269\u026b\5.\30\2\u026a\u0269\3\2\2\2\u026a"+
		"\u026b\3\2\2\2\u026b\25\3\2\2\2\u026c\u026d\7\31\2\2\u026d\u026e\7\u00cd"+
		"\2\2\u026e\u0270\5\u00eav\2\u026f\u0271\5\22\n\2\u0270\u026f\3\2\2\2\u0270"+
		"\u0271\3\2\2\2\u0271\27\3\2\2\2\u0272\u0273\t\3\2\2\u0273\u0277\7)\2\2"+
		"\u0274\u0275\7a\2\2\u0275\u0276\7\u008e\2\2\u0276\u0278\7D\2\2\u0277\u0274"+
		"\3\2\2\2\u0277\u0278\3\2\2\2\u0278\u0279\3\2\2\2\u0279\u027b\5\u00f0y"+
		"\2\u027a\u027c\5.\30\2\u027b\u027a\3\2\2\2\u027b\u027c\3\2\2\2\u027c\u027e"+
		"\3\2\2\2\u027d\u027f\5D#\2\u027e\u027d\3\2\2\2\u027e\u027f\3\2\2\2\u027f"+
		"\u0281\3\2\2\2\u0280\u0282\5B\"\2\u0281\u0280\3\2\2\2\u0281\u0282\3\2"+
		"\2\2\u0282\u02f1\3\2\2\2\u0283\u028b\7\22\2\2\u0284\u0287\7#\2\2\u0285"+
		"\u0286\7\u0095\2\2\u0286\u0288\7\u00b2\2\2\u0287\u0285\3\2\2\2\u0287\u0288"+
		"\3\2\2\2\u0288\u028b\3\2\2\2\u0289\u028b\7\u00b2\2\2\u028a\u0283\3\2\2"+
		"\2\u028a\u0284\3\2\2\2\u028a\u0289\3\2\2\2\u028b\u028c\3\2\2\2\u028c\u0290"+
		"\7\66\2\2\u028d\u028e\7a\2\2\u028e\u028f\7\u008e\2\2\u028f\u0291\7D\2"+
		"\2\u0290\u028d\3\2\2\2\u0290\u0291\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u0294"+
		"\5\u00eav\2\u0293\u0295\5\60\31\2\u0294\u0293\3\2\2\2\u0294\u0295\3\2"+
		"\2\2\u0295\u0297\3\2\2\2\u0296\u0298\5.\30\2\u0297\u0296\3\2\2\2\u0297"+
		"\u0298\3\2\2\2\u0298\u0299\3\2\2\2\u0299\u029a\5\32\16\2\u029a\u029b\5"+
		"\36\20\2\u029b\u02f1\3\2\2\2\u029c\u029d\t\3\2\2\u029d\u029e\7\u0080\2"+
		"\2\u029e\u02a2\7\u00e7\2\2\u029f\u02a0\7a\2\2\u02a0\u02a1\7\u008e\2\2"+
		"\u02a1\u02a3\7D\2\2\u02a2\u029f\3\2\2\2\u02a2\u02a3\3\2\2\2\u02a3\u02a4"+
		"\3\2\2\2\u02a4\u02a6\5\u00eav\2\u02a5\u02a7\5\60\31\2\u02a6\u02a5\3\2"+
		"\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02a9\3\2\2\2\u02a8\u02aa\5.\30\2\u02a9"+
		"\u02a8\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02ac\3\2\2\2\u02ab\u02ad\5\66"+
		"\34\2\u02ac\u02ab\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad\u02b3\3\2\2\2\u02ae"+
		"\u02b4\5\62\32\2\u02af\u02b1\58\35\2\u02b0\u02b2\7\u00a0\2\2\u02b1\u02b0"+
		"\3\2\2\2\u02b1\u02b2\3\2\2\2\u02b2\u02b4\3\2\2\2\u02b3\u02ae\3\2\2\2\u02b3"+
		"\u02af\3\2\2\2\u02b4\u02b5\3\2\2\2\u02b5\u02b6\5\64\33\2\u02b6\u02f1\3"+
		"\2\2\2\u02b7\u02bf\7\22\2\2\u02b8\u02bb\7#\2\2\u02b9\u02ba\7\u0095\2\2"+
		"\u02ba\u02bc\7\u00b2\2\2\u02bb\u02b9\3\2\2\2\u02bb\u02bc\3\2\2\2\u02bc"+
		"\u02bf\3\2\2\2\u02bd\u02bf\7\u00b2\2\2\u02be\u02b7\3\2\2\2\u02be\u02b8"+
		"\3\2\2\2\u02be\u02bd\3\2\2\2\u02bf\u02c1\3\2\2\2\u02c0\u02c2\7\u00cf\2"+
		"\2\u02c1\u02c0\3\2\2\2\u02c1\u02c2\3\2\2\2\u02c2\u02c3\3\2\2\2\u02c3\u02c7"+
		"\7\u00cd\2\2\u02c4\u02c5\7a\2\2\u02c5\u02c6\7\u008e\2\2\u02c6\u02c8\7"+
		"D\2\2\u02c7\u02c4\3\2\2\2\u02c7\u02c8\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9"+
		"\u02cb\5\u00eav\2\u02ca\u02cc\5\60\31\2\u02cb\u02ca\3\2\2\2\u02cb\u02cc"+
		"\3\2\2\2\u02cc\u02ce\3\2\2\2\u02cd\u02cf\5.\30\2\u02ce\u02cd\3\2\2\2\u02ce"+
		"\u02cf\3\2\2\2\u02cf\u02d1\3\2\2\2\u02d0\u02d2\5\66\34\2\u02d1\u02d0\3"+
		"\2\2\2\u02d1\u02d2\3\2\2\2\u02d2\u02d4\3\2\2\2\u02d3\u02d5\58\35\2\u02d4"+
		"\u02d3\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5\u02d7\3\2\2\2\u02d6\u02d8\5\64"+
		"\33\2\u02d7\u02d6\3\2\2\2\u02d7\u02d8\3\2\2\2\u02d8\u02f1\3\2\2\2\u02d9"+
		"\u02dc\t\3\2\2\u02da\u02db\7\u0095\2\2\u02db\u02dd\7\u00b2\2\2\u02dc\u02da"+
		"\3\2\2\2\u02dc\u02dd\3\2\2\2\u02dd\u02de\3\2\2\2\u02de\u02e2\7\u00e7\2"+
		"\2\u02df\u02e0\7a\2\2\u02e0\u02e1\7\u008e\2\2\u02e1\u02e3\7D\2\2\u02e2"+
		"\u02df\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e3\u02e4\3\2\2\2\u02e4\u02e6\5\u00ea"+
		"v\2\u02e5\u02e7\5\60\31\2\u02e6\u02e5\3\2\2\2\u02e6\u02e7\3\2\2\2\u02e7"+
		"\u02e9\3\2\2\2\u02e8\u02ea\5.\30\2\u02e9\u02e8\3\2\2\2\u02e9\u02ea\3\2"+
		"\2\2\u02ea\u02ec\3\2\2\2\u02eb\u02ed\5\66\34\2\u02ec\u02eb\3\2\2\2\u02ec"+
		"\u02ed\3\2\2\2\u02ed\u02ee\3\2\2\2\u02ee\u02ef\5\64\33\2\u02ef\u02f1\3"+
		"\2\2\2\u02f0\u0272\3\2\2\2\u02f0\u028a\3\2\2\2\u02f0\u029c\3\2\2\2\u02f0"+
		"\u02be\3\2\2\2\u02f0\u02d9\3\2\2\2\u02f1\31\3\2\2\2\u02f2\u02f3\7\u010c"+
		"\2\2\u02f3\u02f8\5\34\17\2\u02f4\u02f5\7\u0101\2\2\u02f5\u02f7\5\34\17"+
		"\2\u02f6\u02f4\3\2\2\2\u02f7\u02fa\3\2\2\2\u02f8\u02f6\3\2\2\2\u02f8\u02f9"+
		"\3\2\2\2\u02f9\u02fb\3\2\2\2\u02fa\u02f8\3\2\2\2\u02fb\u02fc\7\u0116\2"+
		"\2\u02fc\33\3\2\2\2\u02fd\u02fe\5\u0104\u0083\2\u02fe\u0314\5\u00d2j\2"+
		"\u02ff\u0300\6\17\2\3\u0300\u0301\7.\2\2\u0301\u0302\5\u00fa~\2\u0302"+
		"\u0303\b\17\1\2\u0303\u0313\3\2\2\2\u0304\u0305\6\17\3\3\u0305\u0306\7"+
		"F\2\2\u0306\u0307\5\u00d8m\2\u0307\u0308\b\17\1\2\u0308\u0313\3\2\2\2"+
		"\u0309\u030a\6\17\4\3\u030a\u030b\7^\2\2\u030b\u0313\b\17\1\2\u030c\u030d"+
		"\6\17\5\3\u030d\u030e\7i\2\2\u030e\u0313\b\17\1\2\u030f\u0310\6\17\6\3"+
		"\u0310\u0311\7p\2\2\u0311\u0313\b\17\1\2\u0312\u02ff\3\2\2\2\u0312\u0304"+
		"\3\2\2\2\u0312\u0309\3\2\2\2\u0312\u030c\3\2\2\2\u0312\u030f\3\2\2\2\u0313"+
		"\u0316\3\2\2\2\u0314\u0312\3\2\2\2\u0314\u0315\3\2\2\2\u0315\35\3\2\2"+
		"\2\u0316\u0314\3\2\2\2\u0317\u0319\5 \21\2\u0318\u0317\3\2\2\2\u0318\u0319"+
		"\3\2\2\2\u0319\u0330\3\2\2\2\u031a\u031b\6\20\7\3\u031b\u031c\5$\23\2"+
		"\u031c\u031d\b\20\1\2\u031d\u032f\3\2\2\2\u031e\u031f\6\20\b\3\u031f\u0320"+
		"\5&\24\2\u0320\u0321\b\20\1\2\u0321\u032f\3\2\2\2\u0322\u0323\6\20\t\3"+
		"\u0323\u0324\5(\25\2\u0324\u0325\b\20\1\2\u0325\u032f\3\2\2\2\u0326\u0327"+
		"\6\20\n\3\u0327\u0328\5*\26\2\u0328\u0329\b\20\1\2\u0329\u032f\3\2\2\2"+
		"\u032a\u032b\6\20\13\3\u032b\u032c\5,\27\2\u032c\u032d\b\20\1\2\u032d"+
		"\u032f\3\2\2\2\u032e\u031a\3\2\2\2\u032e\u031e\3\2\2\2\u032e\u0322\3\2"+
		"\2\2\u032e\u0326\3\2\2\2\u032e\u032a\3\2\2\2\u032f\u0332\3\2\2\2\u0330"+
		"\u032e\3\2\2\2\u0330\u0331\3\2\2\2\u0331\37\3\2\2\2\u0332\u0330\3\2\2"+
		"\2\u0333\u0334\7\u00a4\2\2\u0334\u0335\7r\2\2\u0335\u0336\5\u00d4k\2\u0336"+
		"!\3\2\2\2\u0337\u033e\5\u0104\u0083\2\u0338\u033b\5\u0104\u0083\2\u0339"+
		"\u033a\7\u010c\2\2\u033a\u033c\7\u0116\2\2\u033b\u0339\3\2\2\2\u033b\u033c"+
		"\3\2\2\2\u033c\u033f\3\2\2\2\u033d\u033f\5\u00fa~\2\u033e\u0338\3\2\2"+
		"\2\u033e\u033d\3\2\2\2\u033f#\3\2\2\2\u0340\u0341\7\u00c5\2\2\u0341\u0342"+
		"\7\u010c\2\2\u0342\u0343\5\u0104\u0083\2\u0343\u0347\7\u010c\2\2\u0344"+
		"\u0346\5\"\22\2\u0345\u0344\3\2\2\2\u0346\u0349\3\2\2\2\u0347\u0345\3"+
		"\2\2\2\u0347\u0348\3\2\2\2\u0348\u034a\3\2\2\2\u0349\u0347\3\2\2\2\u034a"+
		"\u034b\7\u0116\2\2\u034b\u034c\7\u0116\2\2\u034c%\3\2\2\2\u034d\u034e"+
		"\7y\2\2\u034e\u0358\7\u010c\2\2\u034f\u0359\7\u00f7\2\2\u0350\u0351\7"+
		"\u0085\2\2\u0351\u0352\7\u00f7\2\2\u0352\u0353\7\u0081\2\2\u0353\u0359"+
		"\7\u00f7\2\2\u0354\u0355\7\u0081\2\2\u0355\u0356\7\u00f7\2\2\u0356\u0357"+
		"\7\u0085\2\2\u0357\u0359\7\u00f7\2\2\u0358\u034f\3\2\2\2\u0358\u0350\3"+
		"\2\2\2\u0358\u0354\3\2\2\2\u0359\u035a\3\2\2\2\u035a\u035b\7\u0116\2\2"+
		"\u035b\'\3\2\2\2\u035c\u035d\7v\2\2\u035d\u035e\7\u010c\2\2\u035e\u035f"+
		"\5\u0104\u0083\2\u035f\u0363\7\u010c\2\2\u0360\u0362\5\"\22\2\u0361\u0360"+
		"\3\2\2\2\u0362\u0365\3\2\2\2\u0363\u0361\3\2\2\2\u0363\u0364\3\2\2\2\u0364"+
		"\u0366\3\2\2\2\u0365\u0363\3\2\2\2\u0366\u0367\7\u0116\2\2\u0367\u0368"+
		"\7\u0116\2\2\u0368)\3\2\2\2\u0369\u036a\7\u00ad\2\2\u036a\u0375\7\u010c"+
		"\2\2\u036b\u036c\7\u0085\2\2\u036c\u036d\5\u0104\u0083\2\u036d\u036e\7"+
		"\u0081\2\2\u036e\u036f\5\u0104\u0083\2\u036f\u0376\3\2\2\2\u0370\u0371"+
		"\7\u0081\2\2\u0371\u0372\5\u0104\u0083\2\u0372\u0373\7\u0085\2\2\u0373"+
		"\u0374\5\u0104\u0083\2\u0374\u0376\3\2\2\2\u0375\u036b\3\2\2\2\u0375\u0370"+
		"\3\2\2\2\u0376\u0377\3\2\2\2\u0377\u0378\7\u0116\2\2\u0378+\3\2\2\2\u0379"+
		"\u037a\7\u00c3\2\2\u037a\u037b\7\u010c\2\2\u037b\u037c\5\u00b6\\\2\u037c"+
		"\u037d\7\u0116\2\2\u037d-\3\2\2\2\u037e\u037f\7\u0093\2\2\u037f\u0382"+
		"\7\33\2\2\u0380\u0383\5\u0104\u0083\2\u0381\u0383\5\u00f8}\2\u0382\u0380"+
		"\3\2\2\2\u0382\u0381\3\2\2\2\u0383/\3\2\2\2\u0384\u0385\7\u00e5\2\2\u0385"+
		"\u0386\5\u00f8}\2\u0386\61\3\2\2\2\u0387\u0388\7\u00d5\2\2\u0388\u0389"+
		"\5\u00eav\2\u0389\63\3\2\2\2\u038a\u038b\7\r\2\2\u038b\u038c\5z>\2\u038c"+
		"\65\3\2\2\2\u038d\u038e\7\u010c\2\2\u038e\u0393\5F$\2\u038f\u0390\7\u0101"+
		"\2\2\u0390\u0392\5F$\2\u0391\u038f\3\2\2\2\u0392\u0395\3\2\2\2\u0393\u0391"+
		"\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0396\3\2\2\2\u0395\u0393\3\2\2\2\u0396"+
		"\u0397\7\u0116\2\2\u0397\u039d\3\2\2\2\u0398\u0399\7\r\2\2\u0399\u039d"+
		"\5\u00eav\2\u039a\u039b\7\r\2\2\u039b\u039d\5\u00e8u\2\u039c\u038d\3\2"+
		"\2\2\u039c\u0398\3\2\2\2\u039c\u039a\3\2\2\2\u039d\67\3\2\2\2\u039e\u03bd"+
		"\5D#\2\u039f\u03a0\6\35\f\3\u03a0\u03a1\5\u0098M\2\u03a1\u03a2\b\35\1"+
		"\2\u03a2\u03bc\3\2\2\2\u03a3\u03a4\6\35\r\3\u03a4\u03a5\5:\36\2\u03a5"+
		"\u03a6\b\35\1\2\u03a6\u03bc\3\2\2\2\u03a7\u03a8\6\35\16\3\u03a8\u03a9"+
		"\5<\37\2\u03a9\u03aa\b\35\1\2\u03aa\u03bc\3\2\2\2\u03ab\u03ac\6\35\17"+
		"\3\u03ac\u03ad\5> \2\u03ad\u03ae\b\35\1\2\u03ae\u03bc\3\2\2\2\u03af\u03b0"+
		"\6\35\20\3\u03b0\u03b1\5@!\2\u03b1\u03b2\b\35\1\2\u03b2\u03bc\3\2\2\2"+
		"\u03b3\u03b4\6\35\21\3\u03b4\u03b5\5\u00a2R\2\u03b5\u03b6\b\35\1\2\u03b6"+
		"\u03bc\3\2\2\2\u03b7\u03b8\6\35\22\3\u03b8\u03b9\5B\"\2\u03b9\u03ba\b"+
		"\35\1\2\u03ba\u03bc\3\2\2\2\u03bb\u039f\3\2\2\2\u03bb\u03a3\3\2\2\2\u03bb"+
		"\u03a7\3\2\2\2\u03bb\u03ab\3\2\2\2\u03bb\u03af\3\2\2\2\u03bb\u03b3\3\2"+
		"\2\2\u03bb\u03b7\3\2\2\2\u03bc\u03bf\3\2\2\2\u03bd\u03bb\3\2\2\2\u03bd"+
		"\u03be\3\2\2\2\u03be9\3\2\2\2\u03bf\u03bd\3\2\2\2\u03c0\u03c1\7\u009b"+
		"\2\2\u03c1\u03c2\7\25\2\2\u03c2\u03c3\5\u00d8m\2\u03c3;\3\2\2\2\u03c4"+
		"\u03c5\7\u00a4\2\2\u03c5\u03c6\7r\2\2\u03c6\u03c7\5\u00d8m\2\u03c7=\3"+
		"\2\2\2\u03c8\u03c9\7\u00bb\2\2\u03c9\u03ca\7\25\2\2\u03ca\u03cb\5\u00d8"+
		"m\2\u03cb?\3\2\2\2\u03cc\u03cd\7\u00dc\2\2\u03cd\u03d2\5T+\2\u03ce\u03cf"+
		"\7\u0101\2\2\u03cf\u03d1\5T+\2\u03d0\u03ce\3\2\2\2\u03d1\u03d4\3\2\2\2"+
		"\u03d2\u03d0\3\2\2\2\u03d2\u03d3\3\2\2\2\u03d3A\3\2\2\2\u03d4\u03d2\3"+
		"\2\2\2\u03d5\u03d6\7!\2\2\u03d6\u03d7\7\u00fa\2\2\u03d7C\3\2\2\2\u03d8"+
		"\u03da\7>\2\2\u03d9\u03db\7\u0106\2\2\u03da\u03d9\3\2\2\2\u03da\u03db"+
		"\3\2\2\2\u03db\u03dc\3\2\2\2\u03dc\u03e2\5\u0106\u0084\2\u03dd\u03df\7"+
		"\u010c\2\2\u03de\u03e0\5\u00d4k\2\u03df\u03de\3\2\2\2\u03df\u03e0\3\2"+
		"\2\2\u03e0\u03e1\3\2\2\2\u03e1\u03e3\7\u0116\2\2\u03e2\u03dd\3\2\2\2\u03e2"+
		"\u03e3\3\2\2\2\u03e3E\3\2\2\2\u03e4\u03e5\7\u00a4\2\2\u03e5\u03e6\7r\2"+
		"\2\u03e6\u03f2\5\u00d8m\2\u03e7\u03f2\5H%\2\u03e8\u03e9\7\"\2\2\u03e9"+
		"\u03ea\5\u0104\u0083\2\u03ea\u03eb\7\31\2\2\u03eb\u03ec\5\u00d8m\2\u03ec"+
		"\u03f2\3\2\2\2\u03ed\u03ee\7e\2\2\u03ee\u03f2\5L\'\2\u03ef\u03f0\7\u00a9"+
		"\2\2\u03f0\u03f2\5N(\2\u03f1\u03e4\3\2\2\2\u03f1\u03e7\3\2\2\2\u03f1\u03e8"+
		"\3\2\2\2\u03f1\u03ed\3\2\2\2\u03f1\u03ef\3\2\2\2\u03f2G\3\2\2\2\u03f3"+
		"\u03fa\5\u00e2r\2\u03f4\u03f5\7\u0090\2\2\u03f5\u03f6\7\u010c\2\2\u03f6"+
		"\u03f7\5\u00d2j\2\u03f7\u03f8\7\u0116\2\2\u03f8\u03fb\3\2\2\2\u03f9\u03fb"+
		"\5\u00d2j\2\u03fa\u03f4\3\2\2\2\u03fa\u03f9\3\2\2\2\u03fb\u03fd\3\2\2"+
		"\2\u03fc\u03fe\5J&\2\u03fd\u03fc\3\2\2\2\u03fd\u03fe\3\2\2\2\u03fe\u0401"+
		"\3\2\2\2\u03ff\u0400\7!\2\2\u0400\u0402\5\u00f8}\2\u0401\u03ff\3\2\2\2"+
		"\u0401\u0402\3\2\2\2\u0402\u0404\3\2\2\2\u0403\u0405\5P)\2\u0404\u0403"+
		"\3\2\2\2\u0404\u0405\3\2\2\2\u0405\u0408\3\2\2\2\u0406\u0407\7\u00dc\2"+
		"\2\u0407\u0409\5\u00d8m\2\u0408\u0406\3\2\2\2\u0408\u0409\3\2\2\2\u0409"+
		"I\3\2\2\2\u040a\u040b\t\4\2\2\u040b\u040c\5\u00d8m\2\u040cK\3\2\2\2\u040d"+
		"\u040e\5\u00e2r\2\u040e\u040f\5\u00d8m\2\u040f\u0410\7\u00dd\2\2\u0410"+
		"\u0411\5\u00d2j\2\u0411\u0412\7Y\2\2\u0412\u0413\7\u00f7\2\2\u0413M\3"+
		"\2\2\2\u0414\u0415\5\u00e2r\2\u0415\u0416\5x=\2\u0416O\3\2\2\2\u0417\u0418"+
		"\7\35\2\2\u0418\u0419\7\u010c\2\2\u0419\u041e\5R*\2\u041a\u041b\7\u0101"+
		"\2\2\u041b\u041d\5R*\2\u041c\u041a\3\2\2\2\u041d\u0420\3\2\2\2\u041e\u041c"+
		"\3\2\2\2\u041e\u041f\3\2\2\2\u041f\u0421\3\2\2\2\u0420\u041e\3\2\2\2\u0421"+
		"\u0422\7\u0116\2\2\u0422Q\3\2\2\2\u0423\u0429\5\u0104\u0083\2\u0424\u0426"+
		"\7\u010c\2\2\u0425\u0427\5\u00d4k\2\u0426\u0425\3\2\2\2\u0426\u0427\3"+
		"\2\2\2\u0427\u0428\3\2\2\2\u0428\u042a\7\u0116\2\2\u0429\u0424\3\2\2\2"+
		"\u0429\u042a\3\2\2\2\u042aS\3\2\2\2\u042b\u0436\5\u00d8m\2\u042c\u042e"+
		"\7\60\2\2\u042d\u042f\5\u0092J\2\u042e\u042d\3\2\2\2\u042e\u042f\3\2\2"+
		"\2\u042f\u0437\3\2\2\2\u0430\u0431\7\u00d5\2\2\u0431\u0432\7\67\2\2\u0432"+
		"\u0437\5\u00f8}\2\u0433\u0434\7\u00d5\2\2\u0434\u0435\7\u00e8\2\2\u0435"+
		"\u0437\5\u00f8}\2\u0436\u042c\3\2\2\2\u0436\u0430\3\2\2\2\u0436\u0433"+
		"\3\2\2\2\u0436\u0437\3\2\2\2\u0437\u0444\3\2\2\2\u0438\u0439\5\u00d8m"+
		"\2\u0439\u043a\5\u0094K\2\u043a\u043b\7\u00c0\2\2\u043b\u0440\5V,\2\u043c"+
		"\u043d\7\u0101\2\2\u043d\u043f\5V,\2\u043e\u043c\3\2\2\2\u043f\u0442\3"+
		"\2\2\2\u0440\u043e\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u0444\3\2\2\2\u0442"+
		"\u0440\3\2\2\2\u0443\u042b\3\2\2\2\u0443\u0438\3\2\2\2\u0444U\3\2\2\2"+
		"\u0445\u0446\5\u00d8m\2\u0446\u0447\7\u0106\2\2\u0447\u0448\5\u00d8m\2"+
		"\u0448W\3\2\2\2\u0449\u044b\t\5\2\2\u044a\u044c\7\u00cd\2\2\u044b\u044a"+
		"\3\2\2\2\u044b\u044c\3\2\2\2\u044c\u044d\3\2\2\2\u044d\u044e\5\u00e6t"+
		"\2\u044eY\3\2\2\2\u044f\u0450\t\6\2\2\u0450\u0453\7)\2\2\u0451\u0452\7"+
		"a\2\2\u0452\u0454\7D\2\2\u0453\u0451\3\2\2\2\u0453\u0454\3\2\2\2\u0454"+
		"\u0455\3\2\2\2\u0455\u0457\5\u00f0y\2\u0456\u0458\5.\30\2\u0457\u0456"+
		"\3\2\2\2\u0457\u0458\3\2\2\2\u0458\u046f\3\2\2\2\u0459\u0460\t\6\2\2\u045a"+
		"\u0461\7\66\2\2\u045b\u045d\7\u00cf\2\2\u045c\u045b\3\2\2\2\u045c\u045d"+
		"\3\2\2\2\u045d\u045e\3\2\2\2\u045e\u0461\7\u00cd\2\2\u045f\u0461\7\u00e7"+
		"\2\2\u0460\u045a\3\2\2\2\u0460\u045c\3\2\2\2\u0460\u045f\3\2\2\2\u0461"+
		"\u0464\3\2\2\2\u0462\u0463\7a\2\2\u0463\u0465\7D\2\2\u0464\u0462\3\2\2"+
		"\2\u0464\u0465\3\2\2\2\u0465\u0466\3\2\2\2\u0466\u0468\5\u00eav\2\u0467"+
		"\u0469\5.\30\2\u0468\u0467\3\2\2\2\u0468\u0469\3\2\2\2\u0469\u046c\3\2"+
		"\2\2\u046a\u046b\7\u008d\2\2\u046b\u046d\7/\2\2\u046c\u046a\3\2\2\2\u046c"+
		"\u046d\3\2\2\2\u046d\u046f\3\2\2\2\u046e\u044f\3\2\2\2\u046e\u0459\3\2"+
		"\2\2\u046f[\3\2\2\2\u0470\u0471\7D\2\2\u0471\u0472\7)\2\2\u0472\u047e"+
		"\5\u00f0y\2\u0473\u047a\7D\2\2\u0474\u047b\7\66\2\2\u0475\u0477\7\u00cf"+
		"\2\2\u0476\u0475\3\2\2\2\u0476\u0477\3\2\2\2\u0477\u0478\3\2\2\2\u0478"+
		"\u047b\7\u00cd\2\2\u0479\u047b\7\u00e7\2\2\u047a\u0474\3\2\2\2\u047a\u0476"+
		"\3\2\2\2\u047a\u0479\3\2\2\2\u047a\u047b\3\2\2\2\u047b\u047c\3\2\2\2\u047c"+
		"\u047e\5\u00eav\2\u047d\u0470\3\2\2\2\u047d\u0473\3\2\2\2\u047e]\3\2\2"+
		"\2\u047f\u0489\7E\2\2\u0480\u048a\7\20\2\2\u0481\u048a\7\u00ca\2\2\u0482"+
		"\u0483\7\u0111\2\2\u0483\u048a\7\u00d9\2\2\u0484\u048a\7\u009d\2\2\u0485"+
		"\u048a\7\u009c\2\2\u0486\u048a\7@\2\2\u0487\u0488\7\u00cd\2\2\u0488\u048a"+
		"\7\u009a\2\2\u0489\u0480\3\2\2\2\u0489\u0481\3\2\2\2\u0489\u0482\3\2\2"+
		"\2\u0489\u0484\3\2\2\2\u0489\u0485\3\2\2\2\u0489\u0486\3\2\2\2\u0489\u0487"+
		"\3\2\2\2\u0489\u048a\3\2\2\2\u048a\u048c\3\2\2\2\u048b\u048d\5\u00b6\\"+
		"\2\u048c\u048b\3\2\2\2\u048c\u048d\3\2\2\2\u048d\u048e\3\2\2\2\u048e\u048f"+
		"\5z>\2\u048f_\3\2\2\2\u0490\u0491\7k\2\2\u0491\u0493\7n\2\2\u0492\u0494"+
		"\7\u00cd\2\2\u0493\u0492\3\2\2\2\u0493\u0494\3\2\2\2\u0494\u0498\3\2\2"+
		"\2\u0495\u0499\5\u00eav\2\u0496\u0497\7V\2\2\u0497\u0499\5\u00e8u\2\u0498"+
		"\u0495\3\2\2\2\u0498\u0496\3\2\2\2\u0499\u049b\3\2\2\2\u049a\u049c\5b"+
		"\62\2\u049b\u049a\3\2\2\2\u049b\u049c\3\2\2\2\u049c\u049d\3\2\2\2\u049d"+
		"\u049e\5d\63\2\u049ea\3\2\2\2\u049f\u04a0\7\u010c\2\2\u04a0\u04a5\5\u00e2"+
		"r\2\u04a1\u04a2\7\u0101\2\2\u04a2\u04a4\5\u00e2r\2\u04a3\u04a1\3\2\2\2"+
		"\u04a4\u04a7\3\2\2\2\u04a5\u04a3\3\2\2\2\u04a5\u04a6\3\2\2\2\u04a6\u04a8"+
		"\3\2\2\2\u04a7\u04a5\3\2\2\2\u04a8\u04a9\7\u0116\2\2\u04a9c\3\2\2\2\u04aa"+
		"\u04ab\7R\2\2\u04ab\u04b7\5\u0104\u0083\2\u04ac\u04ad\7\u00e6\2\2\u04ad"+
		"\u04b2\5f\64\2\u04ae\u04af\7\u0101\2\2\u04af\u04b1\5f\64\2\u04b0\u04ae"+
		"\3\2\2\2\u04b1\u04b4\3\2\2\2\u04b2\u04b0\3\2\2\2\u04b2\u04b3\3\2\2\2\u04b3"+
		"\u04b7\3\2\2\2\u04b4\u04b2\3\2\2\2\u04b5\u04b7\5z>\2\u04b6\u04aa\3\2\2"+
		"\2\u04b6\u04ac\3\2\2\2\u04b6\u04b5\3\2\2\2\u04b7e\3\2\2\2\u04b8\u04b9"+
		"\7\u010c\2\2\u04b9\u04be\5h\65\2\u04ba\u04bb\7\u0101\2\2\u04bb\u04bd\5"+
		"h\65\2\u04bc\u04ba\3\2\2\2\u04bd\u04c0\3\2\2\2\u04be\u04bc\3\2\2\2\u04be"+
		"\u04bf\3\2\2\2\u04bf\u04c1\3\2\2\2\u04c0\u04be\3\2\2\2\u04c1\u04c2\7\u0116"+
		"\2\2\u04c2\u04c6\3\2\2\2\u04c3\u04c4\7\u010c\2\2\u04c4\u04c6\7\u0116\2"+
		"\2\u04c5\u04b8\3\2\2\2\u04c5\u04c3\3\2\2\2\u04c6g\3\2\2\2\u04c7\u04c8"+
		"\5\u00fa~\2\u04c8i\3\2\2\2\u04c9\u04ca\7\60\2\2\u04ca\u04cb\7T\2\2\u04cb"+
		"\u04cd\5\u00e2r\2\u04cc\u04ce\5.\30\2\u04cd\u04cc\3\2\2\2\u04cd\u04ce"+
		"\3\2\2\2\u04ce\u04d0\3\2\2\2\u04cf\u04d1\5l\67\2\u04d0\u04cf\3\2\2\2\u04d0"+
		"\u04d1\3\2\2\2\u04d1\u04d3\3\2\2\2\u04d2\u04d4\5\u0092J\2\u04d3\u04d2"+
		"\3\2\2\2\u04d3\u04d4\3\2\2\2\u04d4k\3\2\2\2\u04d5\u04d6\7d\2\2\u04d6\u04d7"+
		"\7\u009b\2\2\u04d7\u04d8\5\u00d8m\2\u04d8m\3\2\2\2\u04d9\u04da\7\u00e0"+
		"\2\2\u04da\u04db\5\u00e2r\2\u04db\u04dc\7\u00c0\2\2\u04dc\u04de\5\f\7"+
		"\2\u04dd\u04df\5.\30\2\u04de\u04dd\3\2\2\2\u04de\u04df\3\2\2\2\u04df\u04e1"+
		"\3\2\2\2\u04e0\u04e2\5l\67\2\u04e1\u04e0\3\2\2\2\u04e1\u04e2\3\2\2\2\u04e2"+
		"\u04e3\3\2\2\2\u04e3\u04e4\5\u0092J\2\u04e4o\3\2\2\2\u04e5\u04e6\7t\2"+
		"\2\u04e6\u04e8\7\u008a\2\2\u04e7\u04e9\5.\30\2\u04e8\u04e7\3\2\2\2\u04e8"+
		"\u04e9\3\2\2\2\u04e9\u04ea\3\2\2\2\u04ea\u04ec\5\u0092J\2\u04eb\u04ed"+
		"\t\7\2\2\u04ec\u04eb\3\2\2\2\u04ec\u04ed\3\2\2\2\u04edq\3\2\2\2\u04ee"+
		"\u04ef\7\u0094\2\2\u04ef\u04f0\7\u00cd\2\2\u04f0\u04f2\5\u00eav\2\u04f1"+
		"\u04f3\5.\30\2\u04f2\u04f1\3\2\2\2\u04f2\u04f3\3\2\2\2\u04f3\u04f5\3\2"+
		"\2\2\u04f4\u04f6\5\22\n\2\u04f5\u04f4\3\2\2\2\u04f5\u04f6\3\2\2\2\u04f6"+
		"\u04f8\3\2\2\2\u04f7\u04f9\7M\2\2\u04f8\u04f7\3\2\2\2\u04f8\u04f9\3\2"+
		"\2\2\u04f9\u04fb\3\2\2\2\u04fa\u04fc\7-\2\2\u04fb\u04fa\3\2\2\2\u04fb"+
		"\u04fc\3\2\2\2\u04fcs\3\2\2\2\u04fd\u04fe\7\u00b1\2\2\u04fe\u0500\5v<"+
		"\2\u04ff\u0501\5.\30\2\u0500\u04ff\3\2\2\2\u0500\u0501\3\2\2\2\u0501u"+
		"\3\2\2\2\u0502\u0503\7\u00cd\2\2\u0503\u0504\5\u00eav\2\u0504\u0505\7"+
		"\u00d5\2\2\u0505\u050d\5\u00eav\2\u0506\u0507\7\u0101\2\2\u0507\u0508"+
		"\5\u00eav\2\u0508\u0509\7\u00d5\2\2\u0509\u050a\5\u00eav\2\u050a\u050c"+
		"\3\2\2\2\u050b\u0506\3\2\2\2\u050c\u050f\3\2\2\2\u050d\u050b\3\2\2\2\u050d"+
		"\u050e\3\2\2\2\u050e\u052d\3\2\2\2\u050f\u050d\3\2\2\2\u0510\u0511\7)"+
		"\2\2\u0511\u0512\5\u00f0y\2\u0512\u0513\7\u00d5\2\2\u0513\u051b\5\u00f0"+
		"y\2\u0514\u0515\7\u0101\2\2\u0515\u0516\5\u00f0y\2\u0516\u0517\7\u00d5"+
		"\2\2\u0517\u0518\5\u00f0y\2\u0518\u051a\3\2\2\2\u0519\u0514\3\2\2\2\u051a"+
		"\u051d\3\2\2\2\u051b\u0519\3\2\2\2\u051b\u051c\3\2\2\2\u051c\u052d\3\2"+
		"\2\2\u051d\u051b\3\2\2\2\u051e\u051f\7\66\2\2\u051f\u0520\5\u00f2z\2\u0520"+
		"\u0521\7\u00d5\2\2\u0521\u0529\5\u00f2z\2\u0522\u0523\7\u0101\2\2\u0523"+
		"\u0524\5\u00f2z\2\u0524\u0525\7\u00d5\2\2\u0525\u0526\5\u00f2z\2\u0526"+
		"\u0528\3\2\2\2\u0527\u0522\3\2\2\2\u0528\u052b\3\2\2\2\u0529\u0527\3\2"+
		"\2\2\u0529\u052a\3\2\2\2\u052a\u052d\3\2\2\2\u052b\u0529\3\2\2\2\u052c"+
		"\u0502\3\2\2\2\u052c\u0510\3\2\2\2\u052c\u051e\3\2\2\2\u052dw\3\2\2\2"+
		"\u052e\u0530\7\u010c\2\2\u052f\u0531\5\u0080A\2\u0530\u052f\3\2\2\2\u0530"+
		"\u0531\3\2\2\2\u0531\u0532\3\2\2\2\u0532\u0533\7\u00bd\2\2\u0533\u0535"+
		"\5\u00d4k\2\u0534\u0536\5\u0094K\2\u0535\u0534\3\2\2\2\u0535\u0536\3\2"+
		"\2\2\u0536\u0538\3\2\2\2\u0537\u0539\5\u009cO\2\u0538\u0537\3\2\2\2\u0538"+
		"\u0539\3\2\2\2\u0539\u053a\3\2\2\2\u053a\u053b\7\u0116\2\2\u053by\3\2"+
		"\2\2\u053c\u053e\5\u0080A\2\u053d\u053c\3\2\2\2\u053d\u053e\3\2\2\2\u053e"+
		"\u053f\3\2\2\2\u053f\u054b\5|?\2\u0540\u0542\7\u00df\2\2\u0541\u0543\t"+
		"\b\2\2\u0542\u0541\3\2\2\2\u0542\u0543\3\2\2\2\u0543\u0547\3\2\2\2\u0544"+
		"\u0547\7B\2\2\u0545\u0547\7C\2\2\u0546\u0540\3\2\2\2\u0546\u0544\3\2\2"+
		"\2\u0546\u0545\3\2\2\2\u0547\u0548\3\2\2\2\u0548\u054a\5|?\2\u0549\u0546"+
		"\3\2\2\2\u054a\u054d\3\2\2\2\u054b\u0549\3\2\2\2\u054b\u054c\3\2\2\2\u054c"+
		"{\3\2\2\2\u054d\u054b\3\2\2\2\u054e\u0554\5~@\2\u054f\u0550\7\u010c\2"+
		"\2\u0550\u0551\5z>\2\u0551\u0552\7\u0116\2\2\u0552\u0554\3\2\2\2\u0553"+
		"\u054e\3\2\2\2\u0553\u054f\3\2\2\2\u0554}\3\2\2\2\u0555\u0557\7\u00bd"+
		"\2\2\u0556\u0558\78\2\2\u0557\u0556\3\2\2\2\u0557\u0558\3\2\2\2\u0558"+
		"\u055a\3\2\2\2\u0559\u055b\5\u0086D\2\u055a\u0559\3\2\2\2\u055a\u055b"+
		"\3\2\2\2\u055b\u055c\3\2\2\2\u055c\u055e\5\u00d4k\2\u055d\u055f\5\u0088"+
		"E\2\u055e\u055d\3\2\2\2\u055e\u055f\3\2\2\2\u055f\u0561\3\2\2\2\u0560"+
		"\u0562\5\u008aF\2\u0561\u0560\3\2\2\2\u0561\u0562\3\2\2\2\u0562\u0564"+
		"\3\2\2\2\u0563\u0565\5\u008cG\2\u0564\u0563\3\2\2\2\u0564\u0565\3\2\2"+
		"\2\u0565\u0567\3\2\2\2\u0566\u0568\5\u008eH\2\u0567\u0566\3\2\2\2\u0567"+
		"\u0568\3\2\2\2\u0568\u056a\3\2\2\2\u0569\u056b\5\u0092J\2\u056a\u0569"+
		"\3\2\2\2\u056a\u056b\3\2\2\2\u056b\u056d\3\2\2\2\u056c\u056e\5\u0090I"+
		"\2\u056d\u056c\3\2\2\2\u056d\u056e\3\2\2\2\u056e\u0570\3\2\2\2\u056f\u0571"+
		"\5\u0094K\2\u0570\u056f\3\2\2\2\u0570\u0571\3\2\2\2\u0571\u0574\3\2\2"+
		"\2\u0572\u0573\7\u00ee\2\2\u0573\u0575\t\t\2\2\u0574\u0572\3\2\2\2\u0574"+
		"\u0575\3\2\2\2\u0575\u0578\3\2\2\2\u0576\u0577\7\u00ee\2\2\u0577\u0579"+
		"\7\u00d7\2\2\u0578\u0576\3\2\2\2\u0578\u0579\3\2\2\2\u0579\u057b\3\2\2"+
		"\2\u057a\u057c\5\u0096L\2\u057b\u057a\3\2\2\2\u057b\u057c\3\2\2\2\u057c"+
		"\u057e\3\2\2\2\u057d\u057f\5\u0098M\2\u057e\u057d\3\2\2\2\u057e\u057f"+
		"\3\2\2\2\u057f\u0581\3\2\2\2\u0580\u0582\5\u009aN\2\u0581\u0580\3\2\2"+
		"\2\u0581\u0582\3\2\2\2\u0582\u0584\3\2\2\2\u0583\u0585\5\u009eP\2\u0584"+
		"\u0583\3\2\2\2\u0584\u0585\3\2\2\2\u0585\u0587\3\2\2\2\u0586\u0588\5\u00a0"+
		"Q\2\u0587\u0586\3\2\2\2\u0587\u0588\3\2\2\2\u0588\u058a\3\2\2\2\u0589"+
		"\u058b\5\u00a2R\2\u058a\u0589\3\2\2\2\u058a\u058b\3\2\2\2\u058b\177\3"+
		"\2\2\2\u058c\u058d\7\u00ee\2\2\u058d\u058e\5\u0082B\2\u058e\u0081\3\2"+
		"\2\2\u058f\u0594\5\u0084C\2\u0590\u0591\7\u0101\2\2\u0591\u0593\5\u0084"+
		"C\2\u0592\u0590\3\2\2\2\u0593\u0596\3\2\2\2\u0594\u0592\3\2\2\2\u0594"+
		"\u0595\3\2\2\2\u0595\u0083\3\2\2\2\u0596\u0594\3\2\2\2\u0597\u0599\7\u00ae"+
		"\2\2\u0598\u0597\3\2\2\2\u0598\u0599\3\2\2\2\u0599\u059a\3\2\2\2\u059a"+
		"\u059b\5\u0104\u0083\2\u059b\u059c\7\r\2\2\u059c\u059d\7\u010c\2\2\u059d"+
		"\u059e\5z>\2\u059e\u059f\7\u0116\2\2\u059f\u05a5\3\2\2\2\u05a0\u05a1\5"+
		"\u00d8m\2\u05a1\u05a2\7\r\2\2\u05a2\u05a3\5\u0104\u0083\2\u05a3\u05a5"+
		"\3\2\2\2\u05a4\u0598\3\2\2\2\u05a4\u05a0\3\2\2\2\u05a5\u0085\3\2\2\2\u05a6"+
		"\u05a7\7\u00d6\2\2\u05a7\u05aa\7\u00f7\2\2\u05a8\u05a9\7\u00ee\2\2\u05a9"+
		"\u05ab\7\u00d2\2\2\u05aa\u05a8\3\2\2\2\u05aa\u05ab\3\2\2\2\u05ab\u0087"+
		"\3\2\2\2\u05ac\u05ad\7T\2\2\u05ad\u05ae\5\u00a4S\2\u05ae\u0089\3\2\2\2"+
		"\u05af\u05b1\t\n\2\2\u05b0\u05af\3\2\2\2\u05b0\u05b1\3\2\2\2\u05b1\u05b2"+
		"\3\2\2\2\u05b2\u05b3\7\f\2\2\u05b3\u05b4\7q\2\2\u05b4\u05b5\5\u00d4k\2"+
		"\u05b5\u008b\3\2\2\2\u05b6\u05b7\7\u00ed\2\2\u05b7\u05b8\5\u0104\u0083"+
		"\2\u05b8\u05b9\7\r\2\2\u05b9\u05ba\7\u010c\2\2\u05ba\u05bb\5\u00ba^\2"+
		"\u05bb\u05bc\7\u0116\2\2\u05bc\u008d\3\2\2\2\u05bd\u05be\7\u00a2\2\2\u05be"+
		"\u05bf\5\u00d8m\2\u05bf\u008f\3\2\2\2\u05c0\u05c1\7\u00a3\2\2\u05c1\u05c2"+
		"\5\u00d8m\2\u05c2\u0091\3\2\2\2\u05c3\u05c4\7\u00ec\2\2\u05c4\u05c5\5"+
		"\u00d8m\2\u05c5\u0093\3\2\2\2\u05c6\u05c7\7[\2\2\u05c7\u05c8\7\25\2\2"+
		"\u05c8\u05dc\7\7\2\2\u05c9\u05ca\7[\2\2\u05ca\u05cb\7\25\2\2\u05cb\u05cc"+
		"\t\t\2\2\u05cc\u05cd\7\u010c\2\2\u05cd\u05ce\5\u00d4k\2\u05ce\u05cf\7"+
		"\u0116\2\2\u05cf\u05dc\3\2\2\2\u05d0\u05d1\7[\2\2\u05d1\u05d2\7\25\2\2"+
		"\u05d2\u05d3\7\\\2\2\u05d3\u05d4\7\u00c1\2\2\u05d4\u05d5\7\u010c\2\2\u05d5"+
		"\u05d6\5\u00d4k\2\u05d6\u05d7\7\u0116\2\2\u05d7\u05dc\3\2\2\2\u05d8\u05d9"+
		"\7[\2\2\u05d9\u05da\7\25\2\2\u05da\u05dc\5\u00d4k\2\u05db\u05c6\3\2\2"+
		"\2\u05db\u05c9\3\2\2\2\u05db\u05d0\3\2\2\2\u05db\u05d8\3\2\2\2\u05dc\u0095"+
		"\3\2\2\2\u05dd\u05de\7]\2\2\u05de\u05df\5\u00d8m\2\u05df\u0097\3\2\2\2"+
		"\u05e0\u05e1\7\u0096\2\2\u05e1\u05e2\7\25\2\2\u05e2\u05e3\5\u00b0Y\2\u05e3"+
		"\u0099\3\2\2\2\u05e4\u05e5\7l\2\2\u05e5\u05e6\7\u010c\2\2\u05e6\u05e7"+
		"\5\u00d4k\2\u05e7\u05e8\7\u0116\2\2\u05e8\u009b\3\2\2\2\u05e9\u05ea\7"+
		"\u0096\2\2\u05ea\u05eb\7\25\2\2\u05eb\u05ec\5\u00d4k\2\u05ec\u009d\3\2"+
		"\2\2\u05ed\u05ee\7{\2\2\u05ee\u05ef\5\u00aeX\2\u05ef\u05f0\7\25\2\2\u05f0"+
		"\u05f1\5\u00d4k\2\u05f1\u009f\3\2\2\2\u05f2\u05f3\7{\2\2\u05f3\u05f6\5"+
		"\u00aeX\2\u05f4\u05f5\7\u00ee\2\2\u05f5\u05f7\7\u00d2\2\2\u05f6\u05f4"+
		"\3\2\2\2\u05f6\u05f7\3\2\2\2\u05f7\u00a1\3\2\2\2\u05f8\u05f9\7\u00c3\2"+
		"\2\u05f9\u05fa\5\u00b6\\\2\u05fa\u00a3\3\2\2\2\u05fb\u05fc\bS\1\2\u05fc"+
		"\u05fe\5\u00e6t\2\u05fd\u05ff\7M\2\2\u05fe\u05fd\3\2\2\2\u05fe\u05ff\3"+
		"\2\2\2\u05ff\u0601\3\2\2\2\u0600\u0602\5\u00acW\2\u0601\u0600\3\2\2\2"+
		"\u0601\u0602\3\2\2\2\u0602\u0608\3\2\2\2\u0603\u0604\7\u010c\2\2\u0604"+
		"\u0605\5\u00a4S\2\u0605\u0606\7\u0116\2\2\u0606\u0608\3\2\2\2\u0607\u05fb"+
		"\3\2\2\2\u0607\u0603\3\2\2\2\u0608\u061a\3\2\2\2\u0609\u060a\f\5\2\2\u060a"+
		"\u060b\5\u00a8U\2\u060b\u060c\5\u00a4S\6\u060c\u0619\3\2\2\2\u060d\u060f"+
		"\f\6\2\2\u060e\u0610\t\13\2\2\u060f\u060e\3\2\2\2\u060f\u0610\3\2\2\2"+
		"\u0610\u0612\3\2\2\2\u0611\u0613\5\u00a6T\2\u0612\u0611\3\2\2\2\u0612"+
		"\u0613\3\2\2\2\u0613\u0614\3\2\2\2\u0614\u0615\7q\2\2\u0615\u0616\5\u00a4"+
		"S\2\u0616\u0617\5\u00aaV\2\u0617\u0619\3\2\2\2\u0618\u0609\3\2\2\2\u0618"+
		"\u060d\3\2\2\2\u0619\u061c\3\2\2\2\u061a\u0618\3\2\2\2\u061a\u061b\3\2"+
		"\2\2\u061b\u00a5\3\2\2\2\u061c\u061a\3\2\2\2\u061d\u061f\t\f\2\2\u061e"+
		"\u061d\3\2\2\2\u061e\u061f\3\2\2\2\u061f\u0620\3\2\2\2\u0620\u0627\7j"+
		"\2\2\u0621\u0623\7j\2\2\u0622\u0624\t\f\2\2\u0623\u0622\3\2\2\2\u0623"+
		"\u0624\3\2\2\2\u0624\u0627\3\2\2\2\u0625\u0627\t\f\2\2\u0626\u061e\3\2"+
		"\2\2\u0626\u0621\3\2\2\2\u0626\u0625\3\2\2\2\u0627\u0649\3\2\2\2\u0628"+
		"\u062a\t\r\2\2\u0629\u0628\3\2\2\2\u0629\u062a\3\2\2\2\u062a\u062b\3\2"+
		"\2\2\u062b\u062d\t\16\2\2\u062c\u062e\7\u0097\2\2\u062d\u062c\3\2\2\2"+
		"\u062d\u062e\3\2\2\2\u062e\u0637\3\2\2\2\u062f\u0631\t\16\2\2\u0630\u0632"+
		"\7\u0097\2\2\u0631\u0630\3\2\2\2\u0631\u0632\3\2\2\2\u0632\u0634\3\2\2"+
		"\2\u0633\u0635\t\r\2\2\u0634\u0633\3\2\2\2\u0634\u0635\3\2\2\2\u0635\u0637"+
		"\3\2\2\2\u0636\u0629\3\2\2\2\u0636\u062f\3\2\2\2\u0637\u0649\3\2\2\2\u0638"+
		"\u063a\t\17\2\2\u0639\u0638\3\2\2\2\u0639\u063a\3\2\2\2\u063a\u063b\3"+
		"\2\2\2\u063b\u063d\7U\2\2\u063c\u063e\7\u0097\2\2\u063d\u063c\3\2\2\2"+
		"\u063d\u063e\3\2\2\2\u063e\u0647\3\2\2\2\u063f\u0641\7U\2\2\u0640\u0642"+
		"\7\u0097\2\2\u0641\u0640\3\2\2\2\u0641\u0642\3\2\2\2\u0642\u0644\3\2\2"+
		"\2\u0643\u0645\t\17\2\2\u0644\u0643\3\2\2\2\u0644\u0645\3\2\2\2\u0645"+
		"\u0647\3\2\2\2\u0646\u0639\3\2\2\2\u0646\u063f\3\2\2\2\u0647\u0649\3\2"+
		"\2\2\u0648\u0626\3\2\2\2\u0648\u0636\3\2\2\2\u0648\u0646\3\2\2\2\u0649"+
		"\u00a7\3\2\2\2\u064a\u064c\t\13\2\2\u064b\u064a\3\2\2\2\u064b\u064c\3"+
		"\2\2\2\u064c\u064d\3\2\2\2\u064d\u064e\7$\2\2\u064e\u0651\7q\2\2\u064f"+
		"\u0651\7\u0101\2\2\u0650\u064b\3\2\2\2\u0650\u064f\3\2\2\2\u0651\u00a9"+
		"\3\2\2\2\u0652\u0653\7\u0093\2\2\u0653\u065c\5\u00d4k\2\u0654\u0655\7"+
		"\u00e4\2\2\u0655\u0656\7\u010c\2\2\u0656\u0657\5\u00d4k\2\u0657\u0658"+
		"\7\u0116\2\2\u0658\u065c\3\2\2\2\u0659\u065a\7\u00e4\2\2\u065a\u065c\5"+
		"\u00d4k\2\u065b\u0652\3\2\2\2\u065b\u0654\3\2\2\2\u065b\u0659\3\2\2\2"+
		"\u065c\u00ab\3\2\2\2\u065d\u065e\7\u00bb\2\2\u065e\u0661\5\u00b4[\2\u065f"+
		"\u0660\7\u0092\2\2\u0660\u0662\5\u00b4[\2\u0661\u065f\3\2\2\2\u0661\u0662"+
		"\3\2\2\2\u0662\u00ad\3\2\2\2\u0663\u0666\5\u00d8m\2\u0664\u0665\t\20\2"+
		"\2\u0665\u0667\5\u00d8m\2\u0666\u0664\3\2\2\2\u0666\u0667\3\2\2\2\u0667"+
		"\u00af\3\2\2\2\u0668\u066d\5\u00b2Z\2\u0669\u066a\7\u0101\2\2\u066a\u066c"+
		"\5\u00b2Z\2\u066b\u0669\3\2\2\2\u066c\u066f\3\2\2\2\u066d\u066b\3\2\2"+
		"\2\u066d\u066e\3\2\2\2\u066e\u00b1\3\2\2\2\u066f\u066d\3\2\2\2\u0670\u0672"+
		"\5\u00d8m\2\u0671\u0673\t\21\2\2\u0672\u0671\3\2\2\2\u0672\u0673\3\2\2"+
		"\2\u0673\u0676\3\2\2\2\u0674\u0675\7\u0091\2\2\u0675\u0677\t\22\2\2\u0676"+
		"\u0674\3\2\2\2\u0676\u0677\3\2\2\2\u0677\u067a\3\2\2\2\u0678\u0679\7\36"+
		"\2\2\u0679\u067b\5\u00f8}\2\u067a\u0678\3\2\2\2\u067a\u067b\3\2\2\2\u067b"+
		"\u068a\3\2\2\2\u067c\u067d\7\u00ee\2\2\u067d\u0680\7L\2\2\u067e\u067f"+
		"\7T\2\2\u067f\u0681\5\u00d8m\2\u0680\u067e\3\2\2\2\u0680\u0681\3\2\2\2"+
		"\u0681\u0684\3\2\2\2\u0682\u0683\7\u00d5\2\2\u0683\u0685\5\u00d8m\2\u0684"+
		"\u0682\3\2\2\2\u0684\u0685\3\2\2\2\u0685\u0688\3\2\2\2\u0686\u0687\7\u00cc"+
		"\2\2\u0687\u0689\5\u00d8m\2\u0688\u0686\3\2\2\2\u0688\u0689\3\2\2\2\u0689"+
		"\u068b\3\2\2\2\u068a\u067c\3\2\2\2\u068a\u068b\3\2\2\2\u068b\u00b3\3\2"+
		"\2\2\u068c\u068f\5\u00f6|\2\u068d\u068e\7\u0118\2\2\u068e\u0690\5\u00f6"+
		"|\2\u068f\u068d\3\2\2\2\u068f\u0690\3\2\2\2\u0690\u00b5\3\2\2\2\u0691"+
		"\u0696\5\u00b8]\2\u0692\u0693\7\u0101\2\2\u0693\u0695\5\u00b8]\2\u0694"+
		"\u0692\3\2\2\2\u0695\u0698\3\2\2\2\u0696\u0694\3\2\2\2\u0696\u0697\3\2"+
		"\2\2\u0697\u00b7\3\2\2\2\u0698\u0696\3\2\2\2\u0699\u069a\5\u0104\u0083"+
		"\2\u069a\u069b\7\u0106\2\2\u069b\u069c\5\u00fa~\2\u069c\u00b9\3\2\2\2"+
		"\u069d\u069f\5\u00bc_\2\u069e\u069d\3\2\2\2\u069e\u069f\3\2\2\2\u069f"+
		"\u06a1\3\2\2\2\u06a0\u06a2\5\u00be`\2\u06a1\u06a0\3\2\2\2\u06a1\u06a2"+
		"\3\2\2\2\u06a2\u06a4\3\2\2\2\u06a3\u06a5\5\u00c0a\2\u06a4\u06a3\3\2\2"+
		"\2\u06a4\u06a5\3\2\2\2\u06a5\u00bb\3\2\2\2\u06a6\u06a7\7\u009b\2\2\u06a7"+
		"\u06a8\7\25\2\2\u06a8\u06a9\5\u00d4k\2\u06a9\u00bd\3\2\2\2\u06aa\u06ab"+
		"\7\u0096\2\2\u06ab\u06ac\7\25\2\2\u06ac\u06ad\5\u00b0Y\2\u06ad\u00bf\3"+
		"\2\2\2\u06ae\u06af\t\23\2\2\u06af\u06b0\5\u00c2b\2\u06b0\u00c1\3\2\2\2"+
		"\u06b1\u06b8\5\u00c4c\2\u06b2\u06b3\7\23\2\2\u06b3\u06b4\5\u00c4c\2\u06b4"+
		"\u06b5\7\t\2\2\u06b5\u06b6\5\u00c4c\2\u06b6\u06b8\3\2\2\2\u06b7\u06b1"+
		"\3\2\2\2\u06b7\u06b2\3\2\2\2\u06b8\u00c3\3\2\2\2\u06b9\u06ba\7&\2\2\u06ba"+
		"\u06c6\7\u00b9\2\2\u06bb\u06bc\7\u00de\2\2\u06bc\u06c6\7\u00a1\2\2\u06bd"+
		"\u06be\7\u00de\2\2\u06be\u06c6\7P\2\2\u06bf\u06c0\5\u00f6|\2\u06c0\u06c1"+
		"\7\u00a1\2\2\u06c1\u06c6\3\2\2\2\u06c2\u06c3\5\u00f6|\2\u06c3\u06c4\7"+
		"P\2\2\u06c4\u06c6\3\2\2\2\u06c5\u06b9\3\2\2\2\u06c5\u06bb\3\2\2\2\u06c5"+
		"\u06bd\3\2\2\2\u06c5\u06bf\3\2\2\2\u06c5\u06c2\3\2\2\2\u06c6\u00c5\3\2"+
		"\2\2\u06c7\u06c8\7\u00c0\2\2\u06c8\u06c9\5\u00b6\\\2\u06c9\u00c7\3\2\2"+
		"\2\u06ca\u06cb\7\u00c4\2\2\u06cb\u06cc\7#\2\2\u06cc\u06cd\7)\2\2\u06cd"+
		"\u07f6\5\u00f0y\2\u06ce\u06cf\7\u00c4\2\2\u06cf\u06d0\7#\2\2\u06d0\u06d1"+
		"\7\66\2\2\u06d1\u07f6\5\u00eav\2\u06d2\u06d4\7\u00c4\2\2\u06d3\u06d5\7"+
		"#\2\2\u06d4\u06d3\3\2\2\2\u06d4\u06d5\3\2\2\2\u06d5\u06d6\3\2\2\2\u06d6"+
		"\u06d7\7\u00e7\2\2\u06d7\u07f6\5\u00eav\2\u06d8\u06d9\7\u00c4\2\2\u06d9"+
		"\u06e1\7*\2\2\u06da\u06dc\7\u008e\2\2\u06db\u06da\3\2\2\2\u06db\u06dc"+
		"\3\2\2\2\u06dc\u06dd\3\2\2\2\u06dd\u06de\t\24\2\2\u06de\u06e2\5\u00f8"+
		"}\2\u06df\u06e0\7\u00ec\2\2\u06e0\u06e2\5\u00d8m\2\u06e1\u06db\3\2\2\2"+
		"\u06e1\u06df\3\2\2\2\u06e1\u06e2\3\2\2\2\u06e2\u06e5\3\2\2\2\u06e3\u06e4"+
		"\7{\2\2\u06e4\u06e6\7\u00f7\2\2\u06e5\u06e3\3\2\2\2\u06e5\u06e6\3\2\2"+
		"\2\u06e6\u07f6\3\2\2\2\u06e7\u06e8\7\u00c4\2\2\u06e8\u06eb\7\65\2\2\u06e9"+
		"\u06ea\7T\2\2\u06ea\u06ec\5\u00f0y\2\u06eb\u06e9\3\2\2\2\u06eb\u06ec\3"+
		"\2\2\2\u06ec\u06f4\3\2\2\2\u06ed\u06ef\7\u008e\2\2\u06ee\u06ed\3\2\2\2"+
		"\u06ee\u06ef\3\2\2\2\u06ef\u06f0\3\2\2\2\u06f0\u06f1\t\24\2\2\u06f1\u06f5"+
		"\5\u00f8}\2\u06f2\u06f3\7\u00ec\2\2\u06f3\u06f5\5\u00d8m\2\u06f4\u06ee"+
		"\3\2\2\2\u06f4\u06f2\3\2\2\2\u06f4\u06f5\3\2\2\2\u06f5\u06f8\3\2\2\2\u06f6"+
		"\u06f7\7{\2\2\u06f7\u06f9\7\u00f7\2\2\u06f8\u06f6\3\2\2\2\u06f8\u06f9"+
		"\3\2\2\2\u06f9\u07f6\3\2\2\2\u06fa\u06fc\7\u00c4\2\2\u06fb\u06fd\7U\2"+
		"\2\u06fc\u06fb\3\2\2\2\u06fc\u06fd\3\2\2\2\u06fd\u06ff\3\2\2\2\u06fe\u0700"+
		"\7\u00cf\2\2\u06ff\u06fe\3\2\2\2\u06ff\u0700\3\2\2\2\u0700\u0701\3\2\2"+
		"\2\u0701\u0704\7\u00ce\2\2\u0702\u0703\t\25\2\2\u0703\u0705\5\u00f0y\2"+
		"\u0704\u0702\3\2\2\2\u0704\u0705\3\2\2\2\u0705\u070d\3\2\2\2\u0706\u0708"+
		"\7\u008e\2\2\u0707\u0706\3\2\2\2\u0707\u0708\3\2\2\2\u0708\u0709\3\2\2"+
		"\2\u0709\u070a\t\24\2\2\u070a\u070e\5\u00f8}\2\u070b\u070c\7\u00ec\2\2"+
		"\u070c\u070e\5\u00d8m\2\u070d\u0707\3\2\2\2\u070d\u070b\3\2\2\2\u070d"+
		"\u070e\3\2\2\2\u070e\u0711\3\2\2\2\u070f\u0710\7{\2\2\u0710\u0712\7\u00f7"+
		"\2\2\u0711\u070f\3\2\2\2\u0711\u0712\3\2\2\2\u0712\u07f6\3\2\2\2\u0713"+
		"\u0715\7\u00c4\2\2\u0714\u0716\7G\2\2\u0715\u0714\3\2\2\2\u0715\u0716"+
		"\3\2\2\2\u0716\u0718\3\2\2\2\u0717\u0719\7U\2\2\u0718\u0717\3\2\2\2\u0718"+
		"\u0719\3\2\2\2\u0719\u071a\3\2\2\2\u071a\u071b\t\26\2\2\u071b\u0721\t"+
		"\25\2\2\u071c\u0722\5\u00eav\2\u071d\u071e\5\u0104\u0083\2\u071e\u071f"+
		"\t\25\2\2\u071f\u0720\5\u0104\u0083\2\u0720\u0722\3\2\2\2\u0721\u071c"+
		"\3\2\2\2\u0721\u071d\3\2\2\2\u0722\u072a\3\2\2\2\u0723\u0725\7\u008e\2"+
		"\2\u0724\u0723\3\2\2\2\u0724\u0725\3\2\2\2\u0725\u0726\3\2\2\2\u0726\u0727"+
		"\t\24\2\2\u0727\u072b\5\u00f8}\2\u0728\u0729\7\u00ec\2\2\u0729\u072b\5"+
		"\u00d8m\2\u072a\u0724\3\2\2\2\u072a\u0728\3\2\2\2\u072a\u072b\3\2\2\2"+
		"\u072b\u072e\3\2\2\2\u072c\u072d\7{\2\2\u072d\u072f\7\u00f7\2\2\u072e"+
		"\u072c\3\2\2\2\u072e\u072f\3\2\2\2\u072f\u07f6\3\2\2\2\u0730\u0732\7\u00c4"+
		"\2\2\u0731\u0733\7G\2\2\u0732\u0731\3\2\2\2\u0732\u0733\3\2\2\2\u0733"+
		"\u0734\3\2\2\2\u0734\u0735\t\27\2\2\u0735\u073b\t\25\2\2\u0736\u073c\5"+
		"\u00eav\2\u0737\u0738\5\u0104\u0083\2\u0738\u0739\t\25\2\2\u0739\u073a"+
		"\5\u0104\u0083\2\u073a\u073c\3\2\2\2\u073b\u0736\3\2\2\2\u073b\u0737\3"+
		"\2\2\2\u073c\u073f\3\2\2\2\u073d\u073e\7\u00ec\2\2\u073e\u0740\5\u00d8"+
		"m\2\u073f\u073d\3\2\2\2\u073f\u0740\3\2\2\2\u0740\u07f6\3\2\2\2\u0741"+
		"\u0742\7\u00c4\2\2\u0742\u07f6\7\u00a6\2\2\u0743\u0744\7\u00c4\2\2\u0744"+
		"\u074e\7Z\2\2\u0745\u0746\7Q\2\2\u0746\u074b\5\u0104\u0083\2\u0747\u0748"+
		"\7\u0101\2\2\u0748\u074a\5\u0104\u0083\2\u0749\u0747\3\2\2\2\u074a\u074d"+
		"\3\2\2\2\u074b\u0749\3\2\2\2\u074b\u074c\3\2\2\2\u074c\u074f\3\2\2\2\u074d"+
		"\u074b\3\2\2\2\u074e\u0745\3\2\2\2\u074e\u074f\3\2\2\2\u074f\u0752\3\2"+
		"\2\2\u0750\u0751\7\u00ee\2\2\u0751\u0753\7c\2\2\u0752\u0750\3\2\2\2\u0752"+
		"\u0753\3\2\2\2\u0753\u0755\3\2\2\2\u0754\u0756\7M\2\2\u0755\u0754\3\2"+
		"\2\2\u0755\u0756\3\2\2\2\u0756\u07f6\3\2\2\2\u0757\u0758\7\u00c4\2\2\u0758"+
		"\u0759\7#\2\2\u0759\u0763\7\u00e2\2\2\u075a\u075f\5\u0104\u0083\2\u075b"+
		"\u075c\7\u0101\2\2\u075c\u075e\5\u0104\u0083\2\u075d\u075b\3\2\2\2\u075e"+
		"\u0761\3\2\2\2\u075f\u075d\3\2\2\2\u075f\u0760\3\2\2\2\u0760\u0764\3\2"+
		"\2\2\u0761\u075f\3\2\2\2\u0762\u0764\7\'\2\2\u0763\u075a\3\2\2\2\u0763"+
		"\u0762\3\2\2\2\u0763\u0764\3\2\2\2\u0764\u07f6\3\2\2\2\u0765\u0766\7\u00c4"+
		"\2\2\u0766\u0767\7#\2\2\u0767\u0768\7\u00b6\2\2\u0768\u076d\5\u0104\u0083"+
		"\2\u0769\u076a\7\u0101\2\2\u076a\u076c\5\u0104\u0083\2\u076b\u0769\3\2"+
		"\2\2\u076c\u076f\3\2\2\2\u076d\u076b\3\2\2\2\u076d\u076e\3\2\2\2\u076e"+
		"\u07f6\3\2\2\2\u076f\u076d\3\2\2\2\u0770\u0771\7\u00c4\2\2\u0771\u0773"+
		"\7#\2\2\u0772\u0774\7\u00b9\2\2\u0773\u0772\3\2\2\2\u0773\u0774\3\2\2"+
		"\2\u0774\u0775\3\2\2\2\u0775\u0776\7\u009e\2\2\u0776\u0777\5\u0104\u0083"+
		"\2\u0777\u0778\7\u0093\2\2\u0778\u077d\5\u00eav\2\u0779\u077a\7\u0101"+
		"\2\2\u077a\u077c\5\u00eav\2\u077b\u0779\3\2\2\2\u077c\u077f\3\2\2\2\u077d"+
		"\u077b\3\2\2\2\u077d\u077e\3\2\2\2\u077e\u07f6\3\2\2\2\u077f\u077d\3\2"+
		"\2\2\u0780\u0781\7\u00c4\2\2\u0781\u0782\7#\2\2\u0782\u078c\7\u00ab\2"+
		"\2\u0783\u0788\5\u0104\u0083\2\u0784\u0785\7\u0101\2\2\u0785\u0787\5\u0104"+
		"\u0083\2\u0786\u0784\3\2\2\2\u0787\u078a\3\2\2\2\u0788\u0786\3\2\2\2\u0788"+
		"\u0789\3\2\2\2\u0789\u078d\3\2\2\2\u078a\u0788\3\2\2\2\u078b\u078d\7&"+
		"\2\2\u078c\u0783\3\2\2\2\u078c\u078b\3\2\2\2\u078d\u07f6\3\2\2\2\u078e"+
		"\u078f\7\u00c4\2\2\u078f\u0791\7#\2\2\u0790\u0792\7\u00c3\2\2\u0791\u0790"+
		"\3\2\2\2\u0791\u0792\3\2\2\2\u0792\u0793\3\2\2\2\u0793\u0794\7\u00a7\2"+
		"\2\u0794\u0799\5\u0104\u0083\2\u0795\u0796\7\u0101\2\2\u0796\u0798\5\u0104"+
		"\u0083\2\u0797\u0795\3\2\2\2\u0798\u079b\3\2\2\2\u0799\u0797\3\2\2\2\u0799"+
		"\u079a\3\2\2\2\u079a\u07f6\3\2\2\2\u079b\u0799\3\2\2\2\u079c\u079d\7\u00c4"+
		"\2\2\u079d\u07f6\7\u00e3\2\2\u079e\u07a0\7\u00c4\2\2\u079f\u07a1\t\30"+
		"\2\2\u07a0\u079f\3\2\2\2\u07a0\u07a1\3\2\2\2\u07a1\u07a2\3\2\2\2\u07a2"+
		"\u07f6\7\u00b7\2\2\u07a3\u07a5\7\u00c4\2\2\u07a4\u07a6\7\u00c3\2\2\u07a5"+
		"\u07a4\3\2\2\2\u07a5\u07a6\3\2\2\2\u07a6\u07a7\3\2\2\2\u07a7\u07f6\7\u00a8"+
		"\2\2\u07a8\u07aa\7\u00c4\2\2\u07a9\u07ab\7\u00b9\2\2\u07aa\u07a9\3\2\2"+
		"\2\u07aa\u07ab\3\2\2\2\u07ab\u07ac\3\2\2\2\u07ac\u07af\7\u009f\2\2\u07ad"+
		"\u07ae\7\u0093\2\2\u07ae\u07b0\5\u00eav\2\u07af\u07ad\3\2\2\2\u07af\u07b0"+
		"\3\2\2\2\u07b0\u07f6\3\2\2\2\u07b1\u07b2\7\u00c4\2\2\u07b2\u07f6\7\u00ac"+
		"\2\2\u07b3\u07b5\7\u00c4\2\2\u07b4\u07b6\7&\2\2\u07b5\u07b4\3\2\2\2\u07b5"+
		"\u07b6\3\2\2\2\u07b6\u07b7\3\2\2\2\u07b7\u07f6\7\u00ab\2\2\u07b8\u07b9"+
		"\7\u00c4\2\2\u07b9\u07f6\7\3\2\2\u07ba\u07bb\7\u00c4\2\2\u07bb\u07bc\7"+
		"\33\2\2\u07bc\u07f6\5\u00f8}\2\u07bd\u07be\7\u00c4\2\2\u07be\u07c4\7\34"+
		"\2\2\u07bf\u07c1\7\u008e\2\2\u07c0\u07bf\3\2\2\2\u07c0\u07c1\3\2\2\2\u07c1"+
		"\u07c2\3\2\2\2\u07c2\u07c3\t\24\2\2\u07c3\u07c5\5\u00f8}\2\u07c4\u07c0"+
		"\3\2\2\2\u07c4\u07c5\3\2\2\2\u07c5\u07c8\3\2\2\2\u07c6\u07c7\7{\2\2\u07c7"+
		"\u07c9\7\u00f7\2\2\u07c8\u07c6\3\2\2\2\u07c8\u07c9\3\2\2\2\u07c9\u07f6"+
		"\3\2\2\2\u07ca\u07cc\7\u00c4\2\2\u07cb\u07cd\7(\2\2\u07cc\u07cb\3\2\2"+
		"\2\u07cc\u07cd\3\2\2\2\u07cd\u07ce\3\2\2\2\u07ce\u07cf\7\u00c3\2\2\u07cf"+
		"\u07d0\t\24\2\2\u07d0\u07f6\5\u00f8}\2\u07d1\u07d2\7\u00c4\2\2\u07d2\u07d3"+
		"\7\u00c2\2\2\u07d3\u07f6\5\u00f8}\2\u07d4\u07d5\7\u00c4\2\2\u07d5\u07d6"+
		"\7K\2\2\u07d6\u07f6\7\26\2\2\u07d7\u07d8\7\u00c4\2\2\u07d8\u07f6\7?\2"+
		"\2\u07d9\u07da\7\u00c4\2\2\u07da\u07dd\7W\2\2\u07db\u07dc\t\24\2\2\u07dc"+
		"\u07de\5\u00f8}\2\u07dd\u07db\3\2\2\2\u07dd\u07de\3\2\2\2\u07de\u07f6"+
		"\3\2\2\2\u07df\u07e0\7\u00c4\2\2\u07e0\u07e6\7\u0082\2\2\u07e1\u07e3\7"+
		"\u008e\2\2\u07e2\u07e1\3\2\2\2\u07e2\u07e3\3\2\2\2\u07e3\u07e4\3\2\2\2"+
		"\u07e4\u07e5\t\24\2\2\u07e5\u07e7\5\u00f8}\2\u07e6\u07e2\3\2\2\2\u07e6"+
		"\u07e7\3\2\2\2\u07e7\u07ea\3\2\2\2\u07e8\u07e9\7{\2\2\u07e9\u07eb\7\u00f7"+
		"\2\2\u07ea\u07e8\3\2\2\2\u07ea\u07eb\3\2\2\2\u07eb\u07f6\3\2\2\2\u07ec"+
		"\u07ed\7\u00c4\2\2\u07ed\u07f6\7\u00a5\2\2\u07ee\u07ef\7\u00c4\2\2\u07ef"+
		"\u07f1\7#\2\2\u07f0\u07f2\7\u00cf\2\2\u07f1\u07f0\3\2\2\2\u07f1\u07f2"+
		"\3\2\2\2\u07f2\u07f3\3\2\2\2\u07f3\u07f4\7\u00cd\2\2\u07f4\u07f6\5\u00ea"+
		"v\2\u07f5\u06ca\3\2\2\2\u07f5\u06ce\3\2\2\2\u07f5\u06d2\3\2\2\2\u07f5"+
		"\u06d8\3\2\2\2\u07f5\u06e7\3\2\2\2\u07f5\u06fa\3\2\2\2\u07f5\u0713\3\2"+
		"\2\2\u07f5\u0730\3\2\2\2\u07f5\u0741\3\2\2\2\u07f5\u0743\3\2\2\2\u07f5"+
		"\u0757\3\2\2\2\u07f5\u0765\3\2\2\2\u07f5\u0770\3\2\2\2\u07f5\u0780\3\2"+
		"\2\2\u07f5\u078e\3\2\2\2\u07f5\u079c\3\2\2\2\u07f5\u079e\3\2\2\2\u07f5"+
		"\u07a3\3\2\2\2\u07f5\u07a8\3\2\2\2\u07f5\u07b1\3\2\2\2\u07f5\u07b3\3\2"+
		"\2\2\u07f5\u07b8\3\2\2\2\u07f5\u07ba\3\2\2\2\u07f5\u07bd\3\2\2\2\u07f5"+
		"\u07ca\3\2\2\2\u07f5\u07d1\3\2\2\2\u07f5\u07d4\3\2\2\2\u07f5\u07d7\3\2"+
		"\2\2\u07f5\u07d9\3\2\2\2\u07f5\u07df\3\2\2\2\u07f5\u07ec\3\2\2\2\u07f5"+
		"\u07ee\3\2\2\2\u07f6\u00c9\3\2\2\2\u07f7\u07f8\7\u00cb\2\2\u07f8\u07f9"+
		"\7O\2\2\u07f9\u07fa\79\2\2\u07fa\u081a\5\u00eav\2\u07fb\u07fc\7\u00cb"+
		"\2\2\u07fc\u07fd\7O\2\2\u07fd\u081a\7~\2\2\u07fe\u07ff\7\u00cb\2\2\u07ff"+
		"\u0800\7\u00af\2\2\u0800\u081a\7\65\2\2\u0801\u0802\7\u00cb\2\2\u0802"+
		"\u0803\7\u00af\2\2\u0803\u0804\7\66\2\2\u0804\u081a\5\u00eav\2\u0805\u0806"+
		"\7\u00cb\2\2\u0806\u080e\t\31\2\2\u0807\u0808\79\2\2\u0808\u080f\7\u00bf"+
		"\2\2\u0809\u080f\7I\2\2\u080a\u080c\7\u00dc\2\2\u080b\u080a\3\2\2\2\u080b"+
		"\u080c\3\2\2\2\u080c\u080d\3\2\2\2\u080d\u080f\7\u0082\2\2\u080e\u0807"+
		"\3\2\2\2\u080e\u0809\3\2\2\2\u080e\u080b\3\2\2\2\u080f\u0810\3\2\2\2\u0810"+
		"\u081a\5\u00eav\2\u0811\u0812\7\u00cb\2\2\u0812\u0813\t\31\2\2\u0813\u0814"+
		"\7\u00b4\2\2\u0814\u081a\7\u00bf\2\2\u0815\u0816\7\u00cb\2\2\u0816\u0817"+
		"\7\u00c9\2\2\u0817\u0818\7\u00b3\2\2\u0818\u081a\5\u00eav\2\u0819\u07f7"+
		"\3\2\2\2\u0819\u07fb\3\2\2\2\u0819\u07fe\3\2\2\2\u0819\u0801\3\2\2\2\u0819"+
		"\u0805\3\2\2\2\u0819\u0811\3\2\2\2\u0819\u0815\3\2\2\2\u081a\u00cb\3\2"+
		"\2\2\u081b\u081d\7\u00db\2\2\u081c\u081e\7\u00cf\2\2\u081d\u081c\3\2\2"+
		"\2\u081d\u081e\3\2\2\2\u081e\u0820\3\2\2\2\u081f\u0821\7\u00cd\2\2\u0820"+
		"\u081f\3\2\2\2\u0820\u0821\3\2\2\2\u0821\u0824\3\2\2\2\u0822\u0823\7a"+
		"\2\2\u0823\u0825\7D\2\2\u0824\u0822\3\2\2\2\u0824\u0825\3\2\2\2\u0825"+
		"\u0826\3\2\2\2\u0826\u0828\5\u00eav\2\u0827\u0829\5.\30\2\u0828\u0827"+
		"\3\2\2\2\u0828\u0829\3\2\2\2\u0829\u00cd\3\2\2\2\u082a\u082b\7\u00e1\2"+
		"\2\u082b\u082c\5\u00f0y\2\u082c\u00cf\3\2\2\2\u082d\u082e\7\u00e9\2\2"+
		"\u082e\u0830\5\u00eav\2\u082f\u0831\7A\2\2\u0830\u082f\3\2\2\2\u0830\u0831"+
		"\3\2\2\2\u0831\u0834\3\2\2\2\u0832\u0833\7{\2\2\u0833\u0835\7\u00f7\2"+
		"\2\u0834\u0832\3\2\2\2\u0834\u0835\3\2\2\2\u0835\u00d1\3\2\2\2\u0836\u0866"+
		"\5\u0104\u0083\2\u0837\u0838\5\u0104\u0083\2\u0838\u0839\7\u010c\2\2\u0839"+
		"\u083a\5\u0104\u0083\2\u083a\u0841\5\u00d2j\2\u083b\u083c\7\u0101\2\2"+
		"\u083c\u083d\5\u0104\u0083\2\u083d\u083e\5\u00d2j\2\u083e\u0840\3\2\2"+
		"\2\u083f\u083b\3\2\2\2\u0840\u0843\3\2\2\2\u0841\u083f\3\2\2\2\u0841\u0842"+
		"\3\2\2\2\u0842\u0844\3\2\2\2\u0843\u0841\3\2\2\2\u0844\u0845\7\u0116\2"+
		"\2\u0845\u0866\3\2\2\2\u0846\u0847\5\u0104\u0083\2\u0847\u0848\7\u010c"+
		"\2\2\u0848\u084d\5\u0108\u0085\2\u0849\u084a\7\u0101\2\2\u084a\u084c\5"+
		"\u0108\u0085\2\u084b\u0849\3\2\2\2\u084c\u084f\3\2\2\2\u084d\u084b\3\2"+
		"\2\2\u084d\u084e\3\2\2\2\u084e\u0850\3\2\2\2\u084f\u084d\3\2\2\2\u0850"+
		"\u0851\7\u0116\2\2\u0851\u0866\3\2\2\2\u0852\u0853\5\u0104\u0083\2\u0853"+
		"\u0854\7\u010c\2\2\u0854\u0859\5\u00d2j\2\u0855\u0856\7\u0101\2\2\u0856"+
		"\u0858\5\u00d2j\2\u0857\u0855\3\2\2\2\u0858\u085b\3\2\2\2\u0859\u0857"+
		"\3\2\2\2\u0859\u085a\3\2\2\2\u085a\u085c\3\2\2\2\u085b\u0859\3\2\2\2\u085c"+
		"\u085d\7\u0116\2\2\u085d\u0866\3\2\2\2\u085e\u085f\5\u0104\u0083\2\u085f"+
		"\u0861\7\u010c\2\2\u0860\u0862\5\u00d4k\2\u0861\u0860\3\2\2\2\u0861\u0862"+
		"\3\2\2\2\u0862\u0863\3\2\2\2\u0863\u0864\7\u0116\2\2\u0864\u0866\3\2\2"+
		"\2\u0865\u0836\3\2\2\2\u0865\u0837\3\2\2\2\u0865\u0846\3\2\2\2\u0865\u0852"+
		"\3\2\2\2\u0865\u085e\3\2\2\2\u0866\u00d3\3\2\2\2\u0867\u086c\5\u00d6l"+
		"\2\u0868\u0869\7\u0101\2\2\u0869\u086b\5\u00d6l\2\u086a\u0868\3\2\2\2"+
		"\u086b\u086e\3\2\2\2\u086c\u086a\3\2\2\2\u086c\u086d\3\2\2\2\u086d\u00d5"+
		"\3\2\2\2\u086e\u086c\3\2\2\2\u086f\u0870\5\u00eav\2\u0870\u0871\7\u0104"+
		"\2\2\u0871\u0873\3\2\2\2\u0872\u086f\3\2\2\2\u0872\u0873\3\2\2\2\u0873"+
		"\u0874\3\2\2\2\u0874\u0876\7\u00fc\2\2\u0875\u0877\5\u00e4s\2\u0876\u0875"+
		"\3\2\2\2\u0876\u0877\3\2\2\2\u0877\u087e\3\2\2\2\u0878\u0879\7\u010c\2"+
		"\2\u0879\u087a\5z>\2\u087a\u087b\7\u0116\2\2\u087b\u087e\3\2\2\2\u087c"+
		"\u087e\5\u00d8m\2\u087d\u0872\3\2\2\2\u087d\u0878\3\2\2\2\u087d\u087c"+
		"\3\2\2\2\u087e\u00d7\3\2\2\2\u087f\u0880\bm\1\2\u0880\u0882\7\27\2\2\u0881"+
		"\u0883\5\u00d8m\2\u0882\u0881\3\2\2\2\u0882\u0883\3\2\2\2\u0883\u0889"+
		"\3\2\2\2\u0884\u0885\7\u00eb\2\2\u0885\u0886\5\u00d8m\2\u0886\u0887\7"+
		"\u00d1\2\2\u0887\u0888\5\u00d8m\2\u0888\u088a\3\2\2\2\u0889\u0884\3\2"+
		"\2\2\u088a\u088b\3\2\2\2\u088b\u0889\3\2\2\2\u088b\u088c\3\2\2\2\u088c"+
		"\u088f\3\2\2\2\u088d\u088e\7;\2\2\u088e\u0890\5\u00d8m\2\u088f\u088d\3"+
		"\2\2\2\u088f\u0890\3\2\2\2\u0890\u0891\3\2\2\2\u0891\u0892\7=\2\2\u0892"+
		"\u0902\3\2\2\2\u0893\u0894\7\30\2\2\u0894\u0895\7\u010c\2\2\u0895\u0896"+
		"\5\u00d8m\2\u0896\u0897\7\r\2\2\u0897\u0898\5\u00d2j\2\u0898\u0899\7\u0116"+
		"\2\2\u0899\u0902\3\2\2\2\u089a\u089b\7+\2\2\u089b\u0902\5\u00f8}\2\u089c"+
		"\u089d\7H\2\2\u089d\u089e\7\u010c\2\2\u089e\u089f\5\u00fc\177\2\u089f"+
		"\u08a0\7T\2\2\u08a0\u08a1\5\u00d8m\2\u08a1\u08a2\7\u0116\2\2\u08a2\u0902"+
		"\3\2\2\2\u08a3\u08a4\7m\2\2\u08a4\u08a5\5\u00d8m\2\u08a5\u08a6\5\u00fc"+
		"\177\2\u08a6\u0902\3\2\2\2\u08a7\u08a8\7\u00c8\2\2\u08a8\u08a9\7\u010c"+
		"\2\2\u08a9\u08aa\5\u00d8m\2\u08aa\u08ab\7T\2\2\u08ab\u08ae\5\u00d8m\2"+
		"\u08ac\u08ad\7Q\2\2\u08ad\u08af\5\u00d8m\2\u08ae\u08ac\3\2\2\2\u08ae\u08af"+
		"\3\2\2\2\u08af\u08b0\3\2\2\2\u08b0\u08b1\7\u0116\2\2\u08b1\u0902\3\2\2"+
		"\2\u08b2\u08b3\7\u00d4\2\2\u08b3\u0902\5\u00f8}\2\u08b4\u08b5\7\u00da"+
		"\2\2\u08b5\u08b6\7\u010c\2\2\u08b6\u08b7\t\32\2\2\u08b7\u08b8\5\u00f8"+
		"}\2\u08b8\u08b9\7T\2\2\u08b9\u08ba\5\u00d8m\2\u08ba\u08bb\7\u0116\2\2"+
		"\u08bb\u0902\3\2\2\2\u08bc\u08bd\7\u008e\2\2\u08bd\u0902\5\u00d8m\30\u08be"+
		"\u08bf\5\u0104\u0083\2\u08bf\u08c1\7\u010c\2\2\u08c0\u08c2\5\u00d4k\2"+
		"\u08c1\u08c0\3\2\2\2\u08c1\u08c2\3\2\2\2\u08c2\u08c3\3\2\2\2\u08c3\u08c4"+
		"\7\u0116\2\2\u08c4\u08c5\3\2\2\2\u08c5\u08c6\7\u0099\2\2\u08c6\u08c7\7"+
		"\u010c\2\2\u08c7\u08c8\5\u00ba^\2\u08c8\u08c9\7\u0116\2\2\u08c9\u0902"+
		"\3\2\2\2\u08ca\u08cb\5\u0104\u0083\2\u08cb\u08cd\7\u010c\2\2\u08cc\u08ce"+
		"\5\u00d4k\2\u08cd\u08cc\3\2\2\2\u08cd\u08ce\3\2\2\2\u08ce\u08cf\3\2\2"+
		"\2\u08cf\u08d0\7\u0116\2\2\u08d0\u08d1\3\2\2\2\u08d1\u08d2\7\u0099\2\2"+
		"\u08d2\u08d3\5\u0104\u0083\2\u08d3\u0902\3\2\2\2\u08d4\u08da\5\u0104\u0083"+
		"\2\u08d5\u08d7\7\u010c\2\2\u08d6\u08d8\5\u00d4k\2\u08d7\u08d6\3\2\2\2"+
		"\u08d7\u08d8\3\2\2\2\u08d8\u08d9\3\2\2\2\u08d9\u08db\7\u0116\2\2\u08da"+
		"\u08d5\3\2\2\2\u08da\u08db\3\2\2\2\u08db\u08dc\3\2\2\2\u08dc\u08de\7\u010c"+
		"\2\2\u08dd\u08df\78\2\2\u08de\u08dd\3\2\2\2\u08de\u08df\3\2\2\2\u08df"+
		"\u08e1\3\2\2\2\u08e0\u08e2\5\u00dan\2\u08e1\u08e0\3\2\2\2\u08e1\u08e2"+
		"\3\2\2\2\u08e2\u08e3\3\2\2\2\u08e3\u08e4\7\u0116\2\2\u08e4\u0902\3\2\2"+
		"\2\u08e5\u0902\5\u00fa~\2\u08e6\u08e7\7\u0103\2\2\u08e7\u0902\5\u00d8"+
		"m\21\u08e8\u08e9\5\u00eav\2\u08e9\u08ea\7\u0104\2\2\u08ea\u08ec\3\2\2"+
		"\2\u08eb\u08e8\3\2\2\2\u08eb\u08ec\3\2\2\2\u08ec\u08ed\3\2\2\2\u08ed\u0902"+
		"\7\u00fc\2\2\u08ee\u08ef\7\u010c\2\2\u08ef\u08f0\5z>\2\u08f0\u08f1\7\u0116"+
		"\2\2\u08f1\u0902\3\2\2\2\u08f2\u08f3\7\u010c\2\2\u08f3\u08f4\5\u00d8m"+
		"\2\u08f4\u08f5\7\u0116\2\2\u08f5\u0902\3\2\2\2\u08f6\u08f8\7\u010c\2\2"+
		"\u08f7\u08f9\5\u00d4k\2\u08f8\u08f7\3\2\2\2\u08f8\u08f9\3\2\2\2\u08f9"+
		"\u08fa\3\2\2\2\u08fa\u0902\7\u0116\2\2\u08fb\u08fd\7\u010a\2\2\u08fc\u08fe"+
		"\5\u00d4k\2\u08fd\u08fc\3\2\2\2\u08fd\u08fe\3\2\2\2\u08fe\u08ff\3\2\2"+
		"\2\u08ff\u0902\7\u0115\2\2\u0900\u0902\5\u00e0q\2\u0901\u087f\3\2\2\2"+
		"\u0901\u0893\3\2\2\2\u0901\u089a\3\2\2\2\u0901\u089c\3\2\2\2\u0901\u08a3"+
		"\3\2\2\2\u0901\u08a7\3\2\2\2\u0901\u08b2\3\2\2\2\u0901\u08b4\3\2\2\2\u0901"+
		"\u08bc\3\2\2\2\u0901\u08be\3\2\2\2\u0901\u08ca\3\2\2\2\u0901\u08d4\3\2"+
		"\2\2\u0901\u08e5\3\2\2\2\u0901\u08e6\3\2\2\2\u0901";
	private static final String _serializedATNSegment1 =
		"\u08eb\3\2\2\2\u0901\u08ee\3\2\2\2\u0901\u08f2\3\2\2\2\u0901\u08f6\3\2"+
		"\2\2\u0901\u08fb\3\2\2\2\u0901\u0900\3\2\2\2\u0902\u0951\3\2\2\2\u0903"+
		"\u0905\f\31\2\2\u0904\u0906\7\u008e\2\2\u0905\u0904\3\2\2\2\u0905\u0906"+
		"\3\2\2\2\u0906\u0907\3\2\2\2\u0907\u0908\7\23\2\2\u0908\u0909\5\u00d8"+
		"m\2\u0909\u090a\7\t\2\2\u090a\u090b\5\u00d8m\32\u090b\u0950\3\2\2\2\u090c"+
		"\u090d\f\20\2\2\u090d\u090e\t\33\2\2\u090e\u0950\5\u00d8m\21\u090f\u0910"+
		"\f\17\2\2\u0910\u0911\t\34\2\2\u0911\u0950\5\u00d8m\20\u0912\u0925\f\16"+
		"\2\2\u0913\u0926\7\u0105\2\2\u0914\u0926\7\u0106\2\2\u0915\u0926\7\u010e"+
		"\2\2\u0916\u0926\7\u010b\2\2\u0917\u0926\7\u0107\2\2\u0918\u0926\7\u010d"+
		"\2\2\u0919\u0926\7\u0108\2\2\u091a\u091c\7X\2\2\u091b\u091a\3\2\2\2\u091b"+
		"\u091c\3\2\2\2\u091c\u091e\3\2\2\2\u091d\u091f\7\u008e\2\2\u091e\u091d"+
		"\3\2\2\2\u091e\u091f\3\2\2\2\u091f\u0920\3\2\2\2\u0920\u0926\7d\2\2\u0921"+
		"\u0923\7\u008e\2\2\u0922\u0921\3\2\2\2\u0922\u0923\3\2\2\2\u0923\u0924"+
		"\3\2\2\2\u0924\u0926\t\24\2\2\u0925\u0913\3\2\2\2\u0925\u0914\3\2\2\2"+
		"\u0925\u0915\3\2\2\2\u0925\u0916\3\2\2\2\u0925\u0917\3\2\2\2\u0925\u0918"+
		"\3\2\2\2\u0925\u0919\3\2\2\2\u0925\u091b\3\2\2\2\u0925\u0922\3\2\2\2\u0926"+
		"\u0927\3\2\2\2\u0927\u0950\5\u00d8m\17\u0928\u0929\f\f\2\2\u0929\u092a"+
		"\7\t\2\2\u092a\u0950\5\u00d8m\r\u092b\u092c\f\13\2\2\u092c\u092d\7\u0095"+
		"\2\2\u092d\u0950\5\u00d8m\f\u092e\u092f\f\n\2\2\u092f\u0930\7\u0111\2"+
		"\2\u0930\u0931\5\u00d8m\2\u0931\u0932\7\u0100\2\2\u0932\u0933\5\u00d8"+
		"m\n\u0933\u0950\3\2\2\2\u0934\u0935\f \2\2\u0935\u0936\7\u00ff\2\2\u0936"+
		"\u0950\5\u00d2j\2\u0937\u0938\f\23\2\2\u0938\u0939\7\u010a\2\2\u0939\u093a"+
		"\5\u00d8m\2\u093a\u093b\7\u0115\2\2\u093b\u0950\3\2\2\2\u093c\u093d\f"+
		"\22\2\2\u093d\u0941\7\u0104\2\2\u093e\u0942\7\u00f7\2\2\u093f\u0942\5"+
		"\u00f8}\2\u0940\u0942\5\u0104\u0083\2\u0941\u093e\3\2\2\2\u0941\u093f"+
		"\3\2\2\2\u0941\u0940\3\2\2\2\u0942\u0950\3\2\2\2\u0943\u0944\f\r\2\2\u0944"+
		"\u0946\7o\2\2\u0945\u0947\7\u008e\2\2\u0946\u0945\3\2\2\2\u0946\u0947"+
		"\3\2\2\2\u0947\u0948\3\2\2\2\u0948\u0950\7\u008f\2\2\u0949\u094d\f\t\2"+
		"\2\u094a\u094e\5\u0102\u0082\2\u094b\u094c\7\r\2\2\u094c\u094e\5\u0104"+
		"\u0083\2\u094d\u094a\3\2\2\2\u094d\u094b\3\2\2\2\u094e\u0950\3\2\2\2\u094f"+
		"\u0903\3\2\2\2\u094f\u090c\3\2\2\2\u094f\u090f\3\2\2\2\u094f\u0912\3\2"+
		"\2\2\u094f\u0928\3\2\2\2\u094f\u092b\3\2\2\2\u094f\u092e\3\2\2\2\u094f"+
		"\u0934\3\2\2\2\u094f\u0937\3\2\2\2\u094f\u093c\3\2\2\2\u094f\u0943\3\2"+
		"\2\2\u094f\u0949\3\2\2\2\u0950\u0953\3\2\2\2\u0951\u094f\3\2\2\2\u0951"+
		"\u0952\3\2\2\2\u0952\u00d9\3\2\2\2\u0953\u0951\3\2\2\2\u0954\u0959\5\u00dc"+
		"o\2\u0955\u0956\7\u0101\2\2\u0956\u0958\5\u00dco\2\u0957\u0955\3\2\2\2"+
		"\u0958\u095b\3\2\2\2\u0959\u0957\3\2\2\2\u0959\u095a\3\2\2\2\u095a\u00db"+
		"\3\2\2\2\u095b\u0959\3\2\2\2\u095c\u095f\5\u00dep\2\u095d\u095f\5\u00d8"+
		"m\2\u095e\u095c\3\2\2\2\u095e\u095d\3\2\2\2\u095f\u00dd\3\2\2\2\u0960"+
		"\u0961\7\u010c\2\2\u0961\u0966\5\u0104\u0083\2\u0962\u0963\7\u0101\2\2"+
		"\u0963\u0965\5\u0104\u0083\2\u0964\u0962\3\2\2\2\u0965\u0968\3\2\2\2\u0966"+
		"\u0964\3\2\2\2\u0966\u0967\3\2\2\2\u0967\u0969\3\2\2\2\u0968\u0966\3\2"+
		"\2\2\u0969\u096a\7\u0116\2\2\u096a\u0974\3\2\2\2\u096b\u0970\5\u0104\u0083"+
		"\2\u096c\u096d\7\u0101\2\2\u096d\u096f\5\u0104\u0083\2\u096e\u096c\3\2"+
		"\2\2\u096f\u0972\3\2\2\2\u0970\u096e\3\2\2\2\u0970\u0971\3\2\2\2\u0971"+
		"\u0974\3\2\2\2\u0972\u0970\3\2\2\2\u0973\u0960\3\2\2\2\u0973\u096b\3\2"+
		"\2\2\u0974\u0975\3\2\2\2\u0975\u0976\7\u00fb\2\2\u0976\u0977\5\u00d8m"+
		"\2\u0977\u00df\3\2\2\2\u0978\u0979\5\u00eav\2\u0979\u097a\7\u0104\2\2"+
		"\u097a\u097c\3\2\2\2\u097b\u0978\3\2\2\2\u097b\u097c\3\2\2\2\u097c\u097d"+
		"\3\2\2\2\u097d\u097e\5\u00e2r\2\u097e\u00e1\3\2\2\2\u097f\u0984\5\u0104"+
		"\u0083\2\u0980\u0981\7\u0104\2\2\u0981\u0983\5\u0104\u0083\2\u0982\u0980"+
		"\3\2\2\2\u0983\u0986\3\2\2\2\u0984\u0982\3\2\2\2\u0984\u0985\3\2\2\2\u0985"+
		"\u00e3\3\2\2\2\u0986\u0984\3\2\2\2\u0987\u098d\7B\2\2\u0988\u098e\5\u00f8"+
		"}\2\u0989\u098a\7\u010c\2\2\u098a\u098b\5\u00f8}\2\u098b\u098c\7\u0116"+
		"\2\2\u098c\u098e\3\2\2\2\u098d\u0988\3\2\2\2\u098d\u0989\3\2\2\2\u098e"+
		"\u099f\3\2\2\2\u098f\u099c\7B\2\2\u0990\u099d\5\u0104\u0083\2\u0991\u0992"+
		"\7\u010c\2\2\u0992\u0997\5\u0104\u0083\2\u0993\u0994\7\u0101\2\2\u0994"+
		"\u0996\5\u0104\u0083\2\u0995\u0993\3\2\2\2\u0996\u0999\3\2\2\2\u0997\u0995"+
		"\3\2\2\2\u0997\u0998\3\2\2\2\u0998\u099a\3\2\2\2\u0999\u0997\3\2\2\2\u099a"+
		"\u099b\7\u0116\2\2\u099b\u099d\3\2\2\2\u099c\u0990\3\2\2\2\u099c\u0991"+
		"\3\2\2\2\u099d\u099f\3\2\2\2\u099e\u0987\3\2\2\2\u099e\u098f\3\2\2\2\u099f"+
		"\u00e5\3\2\2\2\u09a0\u09a1\bt\1\2\u09a1\u09a8\5\u00eav\2\u09a2\u09a8\5"+
		"\u00e8u\2\u09a3\u09a4\7\u010c\2\2\u09a4\u09a5\5z>\2\u09a5\u09a6\7\u0116"+
		"\2\2\u09a6\u09a8\3\2\2\2\u09a7\u09a0\3\2\2\2\u09a7\u09a2\3\2\2\2\u09a7"+
		"\u09a3\3\2\2\2\u09a8\u09b1\3\2\2\2\u09a9\u09ad\f\3\2\2\u09aa\u09ae\5\u0102"+
		"\u0082\2\u09ab\u09ac\7\r\2\2\u09ac\u09ae\5\u0104\u0083\2\u09ad\u09aa\3"+
		"\2\2\2\u09ad\u09ab\3\2\2\2\u09ae\u09b0\3\2\2\2\u09af\u09a9\3\2\2\2\u09b0"+
		"\u09b3\3\2\2\2\u09b1\u09af\3\2\2\2\u09b1\u09b2\3\2\2\2\u09b2\u00e7\3\2"+
		"\2\2\u09b3\u09b1\3\2\2\2\u09b4\u09b5\5\u0104\u0083\2\u09b5\u09b7\7\u010c"+
		"\2\2\u09b6\u09b8\5\u00ecw\2\u09b7\u09b6\3\2\2\2\u09b7\u09b8\3\2\2\2\u09b8"+
		"\u09b9\3\2\2\2\u09b9\u09ba\7\u0116\2\2\u09ba\u00e9\3\2\2\2\u09bb\u09bc"+
		"\5\u00f0y\2\u09bc\u09bd\7\u0104\2\2\u09bd\u09bf\3\2\2\2\u09be\u09bb\3"+
		"\2\2\2\u09be\u09bf\3\2\2\2\u09bf\u09c0\3\2\2\2\u09c0\u09c1\5\u0104\u0083"+
		"\2\u09c1\u00eb\3\2\2\2\u09c2\u09c7\5\u00eex\2\u09c3\u09c4\7\u0101\2\2"+
		"\u09c4\u09c6\5\u00eex\2\u09c5\u09c3\3\2\2\2\u09c6\u09c9\3\2\2\2\u09c7"+
		"\u09c5\3\2\2\2\u09c7\u09c8\3\2\2\2\u09c8\u00ed\3\2\2\2\u09c9\u09c7\3\2"+
		"\2\2\u09ca\u09ce\5\u00e8u\2\u09cb\u09ce\5\u00fa~\2\u09cc\u09ce\5\u00e2"+
		"r\2\u09cd\u09ca\3\2\2\2\u09cd\u09cb\3\2\2\2\u09cd\u09cc\3\2\2\2\u09ce"+
		"\u00ef\3\2\2\2\u09cf\u09d0\5\u0104\u0083\2\u09d0\u00f1\3\2\2\2\u09d1\u09d2"+
		"\5\u00f0y\2\u09d2\u09d3\7\u0104\2\2\u09d3\u09d5\3\2\2\2\u09d4\u09d1\3"+
		"\2\2\2\u09d4\u09d5\3\2\2\2\u09d5\u09d6\3\2\2\2\u09d6\u09d7\5\u0104\u0083"+
		"\2\u09d7\u00f3\3\2\2\2\u09d8\u09e1\7\u00f5\2\2\u09d9\u09da\7\u0104\2\2"+
		"\u09da\u09e1\t\35\2\2\u09db\u09dc\7\u00f7\2\2\u09dc\u09de\7\u0104\2\2"+
		"\u09dd\u09df\t\35\2\2\u09de\u09dd\3\2\2\2\u09de\u09df\3\2\2\2\u09df\u09e1"+
		"\3\2\2\2\u09e0\u09d8\3\2\2\2\u09e0\u09d9\3\2\2\2\u09e0\u09db\3\2\2\2\u09e1"+
		"\u00f5\3\2\2\2\u09e2\u09e4\t\36\2\2\u09e3\u09e2\3\2\2\2\u09e3\u09e4\3"+
		"\2\2\2\u09e4\u09ec\3\2\2\2\u09e5\u09ed\5\u00f4{\2\u09e6\u09ed\7\u00f6"+
		"\2\2\u09e7\u09ed\7\u00f7\2\2\u09e8\u09ed\7\u00f8\2\2\u09e9\u09ed\7\u00f9"+
		"\2\2\u09ea\u09ed\7h\2\2\u09eb\u09ed\7\u008b\2\2\u09ec\u09e5\3\2\2\2\u09ec"+
		"\u09e6\3\2\2\2\u09ec\u09e7\3\2\2\2\u09ec\u09e8\3\2\2\2\u09ec\u09e9\3\2"+
		"\2\2\u09ec\u09ea\3\2\2\2\u09ec\u09eb\3\2\2\2\u09ed\u00f7\3\2\2\2\u09ee"+
		"\u09ef\t\37\2\2\u09ef\u00f9\3\2\2\2\u09f0\u09f6\5\u00f6|\2\u09f1\u09f6"+
		"\7\u00f0\2\2\u09f2\u09f6\7\u00f1\2\2\u09f3\u09f6\5\u00f8}\2\u09f4\u09f6"+
		"\7\u008f\2\2\u09f5\u09f0\3\2\2\2\u09f5\u09f1\3\2\2\2\u09f5\u09f2\3\2\2"+
		"\2\u09f5\u09f3\3\2\2\2\u09f5\u09f4\3\2\2\2\u09f6\u00fb\3\2\2\2\u09f7\u09f8"+
		"\t \2\2\u09f8\u00fd\3\2\2\2\u09f9\u09fa\t!\2\2\u09fa\u00ff\3\2\2\2\u09fb"+
		"\u09fc\t\"\2\2\u09fc\u0101\3\2\2\2\u09fd\u0a00\7\u00f4\2\2\u09fe\u0a00"+
		"\5\u0100\u0081\2\u09ff\u09fd\3\2\2\2\u09ff\u09fe\3\2\2\2\u0a00\u0103\3"+
		"\2\2\2\u0a01\u0a05\7\u00f4\2\2\u0a02\u0a05\5\u00fc\177\2\u0a03\u0a05\5"+
		"\u00fe\u0080\2\u0a04\u0a01\3\2\2\2\u0a04\u0a02\3\2\2\2\u0a04\u0a03\3\2"+
		"\2\2\u0a05\u0105\3\2\2\2\u0a06\u0a09\5\u0104\u0083\2\u0a07\u0a09\7\u008f"+
		"\2\2\u0a08\u0a06\3\2\2\2\u0a08\u0a07\3\2\2\2\u0a09\u0107\3\2\2\2\u0a0a"+
		"\u0a0b\5\u00f8}\2\u0a0b\u0a0c\7\u0106\2\2\u0a0c\u0a0d\5\u00f6|\2\u0a0d"+
		"\u0109\3\2\2\2\u0161\u010d\u0111\u0116\u011b\u0121\u012a\u012e\u0133\u0147"+
		"\u014d\u0154\u015c\u0162\u0169\u016e\u0175\u017a\u0180\u0186\u018b\u0191"+
		"\u0196\u019c\u01a1\u01a7\u01b5\u01bc\u01c3\u01ca\u01d0\u01d5\u01db\u01e0"+
		"\u01f3\u01fb\u0216\u021b\u021e\u0221\u0224\u0227\u022c\u022f\u0235\u0239"+
		"\u023c\u023f\u0242\u0245\u024a\u024d\u024f\u0256\u0264\u026a\u0270\u0277"+
		"\u027b\u027e\u0281\u0287\u028a\u0290\u0294\u0297\u02a2\u02a6\u02a9\u02ac"+
		"\u02b1\u02b3\u02bb\u02be\u02c1\u02c7\u02cb\u02ce\u02d1\u02d4\u02d7\u02dc"+
		"\u02e2\u02e6\u02e9\u02ec\u02f0\u02f8\u0312\u0314\u0318\u032e\u0330\u033b"+
		"\u033e\u0347\u0358\u0363\u0375\u0382\u0393\u039c\u03bb\u03bd\u03d2\u03da"+
		"\u03df\u03e2\u03f1\u03fa\u03fd\u0401\u0404\u0408\u041e\u0426\u0429\u042e"+
		"\u0436\u0440\u0443\u044b\u0453\u0457\u045c\u0460\u0464\u0468\u046c\u046e"+
		"\u0476\u047a\u047d\u0489\u048c\u0493\u0498\u049b\u04a5\u04b2\u04b6\u04be"+
		"\u04c5\u04cd\u04d0\u04d3\u04de\u04e1\u04e8\u04ec\u04f2\u04f5\u04f8\u04fb"+
		"\u0500\u050d\u051b\u0529\u052c\u0530\u0535\u0538\u053d\u0542\u0546\u054b"+
		"\u0553\u0557\u055a\u055e\u0561\u0564\u0567\u056a\u056d\u0570\u0574\u0578"+
		"\u057b\u057e\u0581\u0584\u0587\u058a\u0594\u0598\u05a4\u05aa\u05b0\u05db"+
		"\u05f6\u05fe\u0601\u0607\u060f\u0612\u0618\u061a\u061e\u0623\u0626\u0629"+
		"\u062d\u0631\u0634\u0636\u0639\u063d\u0641\u0644\u0646\u0648\u064b\u0650"+
		"\u065b\u0661\u0666\u066d\u0672\u0676\u067a\u0680\u0684\u0688\u068a\u068f"+
		"\u0696\u069e\u06a1\u06a4\u06b7\u06c5\u06d4\u06db\u06e1\u06e5\u06eb\u06ee"+
		"\u06f4\u06f8\u06fc\u06ff\u0704\u0707\u070d\u0711\u0715\u0718\u0721\u0724"+
		"\u072a\u072e\u0732\u073b\u073f\u074b\u074e\u0752\u0755\u075f\u0763\u076d"+
		"\u0773\u077d\u0788\u078c\u0791\u0799\u07a0\u07a5\u07aa\u07af\u07b5\u07c0"+
		"\u07c4\u07c8\u07cc\u07dd\u07e2\u07e6\u07ea\u07f1\u07f5\u080b\u080e\u0819"+
		"\u081d\u0820\u0824\u0828\u0830\u0834\u0841\u084d\u0859\u0861\u0865\u086c"+
		"\u0872\u0876\u087d\u0882\u088b\u088f\u08ae\u08c1\u08cd\u08d7\u08da\u08de"+
		"\u08e1\u08eb\u08f8\u08fd\u0901\u0905\u091b\u091e\u0922\u0925\u0941\u0946"+
		"\u094d\u094f\u0951\u0959\u095e\u0966\u0970\u0973\u097b\u0984\u098d\u0997"+
		"\u099c\u099e\u09a7\u09ad\u09b1\u09b7\u09be\u09c7\u09cd\u09d4\u09de\u09e0"+
		"\u09e3\u09ec\u09f5\u09ff\u0a04\u0a08";
	public static final String _serializedATN = Utils.join(
		new String[] {
			_serializedATNSegment0,
			_serializedATNSegment1
		},
		""
	);
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}