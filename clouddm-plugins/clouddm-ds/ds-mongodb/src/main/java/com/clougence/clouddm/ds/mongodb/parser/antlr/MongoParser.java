// Generated from MongoParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.mongodb.parser.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MongoParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SPACE=1, COMMENT1=2, COMMENT2=3, DB=4, SHOW=5, DATABASES=6, DBS=7, COLLECTIONS=8, 
		USE=9, SERVER_BUILD_INFO=10, HELLO=11, SERVER_STATUS=12, DROP_DATABASE=13, 
		CREATE_COLLECTION=14, STATS=15, SET_PROFILING_LEVEL=16, ADMIN_COMMAND=17, 
		GET_COLLECTION=18, AGGREGATE=19, CURRENT_OP=20, KILL_OP=21, COUNT_DOCUMENTS=22, 
		CREATE_INDEXES=23, CREATE_INDEX=24, DISTINCT=25, DROP_INDEXES=26, DROP_INDEX=27, 
		COUNT=28, ESTIMATED_DOCUMENT_COUNT=29, GET_INDEXES=30, LATENCY_STATS=31, 
		RENAME_COLLECTION=32, REPLACE_ONE=33, VALIDATE=34, EXPLAIN=35, BULK_WRITE=36, 
		DATA_SIZE=37, RUN_COMMAND=38, HIDE_INDEX=39, UNHIDE_INDEX=40, FIND=41, 
		S_SKIP=42, LIMIT=43, SORT=44, S_COMMENT=45, BATCH_SIZE=46, MAX_TIME_MS=47, 
		COLLATION=48, LET=49, MAX=50, MIN=51, RETURN_KEY=52, SHOW_RECORD_ID=53, 
		NO_CURSOR_TIMEOUT=54, MAX_AWAIT_TIME_MS=55, ALLOW_DISK_USE=56, FIND_ONE=57, 
		FIND_ONE_AND_DELETE=58, FIND_ONE_AND_REPLACE=59, FIND_ONE_AND_UPDATE=60, 
		INSERT=61, INSERT_ONE=62, INSERT_MANY=63, DELETE_ONE=64, DELETE_MANY=65, 
		UPDATE_ONE=66, UPDATE_MANY=67, UPDATE=68, DROP=69, WATCH=70, VERSION=71, 
		SHUTDOWN_SERVER=72, SET_LOG_LEVEL=73, SERVER_CMD_LINE_OPTS=74, ROTATE_CERTIFICATES=75, 
		PRINT_SHARDING_STATUS=76, PRINT_SECONDARY_REPLICATION_INFO=77, PRINT_REPLICATION_INFO=78, 
		PRINT_COLLECTION_STATS=79, LIST_COMMANDS=80, HOST_INFO=81, HELP=82, GET_SIBLING_DB=83, 
		GET_REPLICATION_INFO=84, GET_PROFILING_STATUS=85, GET_NAME=86, GET_MONGO=87, 
		GET_LOG_COMPONENTS=88, GET_COLLECTION_NAMES=89, GET_COLLECTION_INFOS=90, 
		FSYNC_UNLOCK=91, FSYNC_LOCK=92, CREATE_VIEW=93, NEW=94, TRUE=95, FALSE=96, 
		NULL=97, STRING=98, DOT=99, LS_BRACKET=100, RS_BRACKET=101, LM_BRACKET=102, 
		RM_BRACKET=103, LB_BRACKET=104, RB_BRACKET=105, COMMA=106, COLON=107, 
		DOUBLE_QUOTA=108, SINGLE_QUOTA=109, SEMI=110, ID=111, NUMBER=112;
	public static final int
		RULE_root = 0, RULE_command = 1, RULE_functionCommand = 2, RULE_use = 3, 
		RULE_databaseFunction = 4, RULE_showFunction = 5, RULE_environmentFunction = 6, 
		RULE_collectionFunction = 7, RULE_collectionMethod = 8, RULE_dataSize = 9, 
		RULE_explain = 10, RULE_explainMethod = 11, RULE_validate = 12, RULE_replaceOne = 13, 
		RULE_renameCollection = 14, RULE_latencyStats = 15, RULE_getIndexes = 16, 
		RULE_hideIndex = 17, RULE_estimatedDocumentCount = 18, RULE_count = 19, 
		RULE_dropIndex = 20, RULE_dropIndexes = 21, RULE_distinct = 22, RULE_createIndex = 23, 
		RULE_createIndexes = 24, RULE_aggregate = 25, RULE_countDocuments = 26, 
		RULE_find = 27, RULE_findConstarint = 28, RULE_findOne = 29, RULE_findOneAndDelete = 30, 
		RULE_findOneAndReplace = 31, RULE_findOneAndUpdate = 32, RULE_insert = 33, 
		RULE_insertOne = 34, RULE_insertMany = 35, RULE_deleteOne = 36, RULE_deleteMany = 37, 
		RULE_update = 38, RULE_drop = 39, RULE_collectionName = 40, RULE_jsonString = 41, 
		RULE_obj = 42, RULE_pair = 43, RULE_key = 44, RULE_arr = 45, RULE_value = 46, 
		RULE_keyWordId = 47;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "command", "functionCommand", "use", "databaseFunction", "showFunction", 
			"environmentFunction", "collectionFunction", "collectionMethod", "dataSize", 
			"explain", "explainMethod", "validate", "replaceOne", "renameCollection", 
			"latencyStats", "getIndexes", "hideIndex", "estimatedDocumentCount", 
			"count", "dropIndex", "dropIndexes", "distinct", "createIndex", "createIndexes", 
			"aggregate", "countDocuments", "find", "findConstarint", "findOne", "findOneAndDelete", 
			"findOneAndReplace", "findOneAndUpdate", "insert", "insertOne", "insertMany", 
			"deleteOne", "deleteMany", "update", "drop", "collectionName", "jsonString", 
			"obj", "pair", "key", "arr", "value", "keyWordId"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'db'", "'show'", "'databases'", "'dbs'", "'collections'", 
			"'use'", "'serverBuildInfo'", "'hello'", "'serverStatus'", "'dropDatabase'", 
			"'createCollection'", "'stats'", "'setProfilingLevel'", "'adminCommand'", 
			"'getCollection'", "'aggregate'", "'currentOp'", "'killOp'", "'countDocuments'", 
			"'createIndexes'", "'createIndex'", "'distinct'", "'dropIndexes'", "'dropIndex'", 
			"'count'", "'estimatedDocumentCount'", "'getIndexes'", "'latencyStats'", 
			"'renameCollection'", "'replaceOne'", "'validate'", "'explain'", "'bulkWrite'", 
			"'dataSize'", "'runCommand'", "'hideIndex'", "'unhideIndex'", "'find'", 
			"'skip'", "'limit'", "'sort'", "'comment'", "'batchSize'", "'maxTimeMS'", 
			"'collation'", "'let'", "'max'", "'min'", "'returnKey'", "'showRecordId'", 
			"'noCursorTimeout'", "'maxAwaitTimeMS'", "'allowDiskUse'", "'findOne'", 
			"'findOneAndDelete'", "'findOneAndReplace'", "'findOneAndUpdate'", "'insert'", 
			"'insertOne'", "'insertMany'", "'deleteOne'", "'deleteMany'", "'updateOne'", 
			"'updateMany'", "'update'", "'drop'", "'watch'", "'version'", "'shutdownServer'", 
			"'setLogLevel'", "'serverCmdLineOpts'", "'rotateCertificates'", "'printShardingStatus'", 
			"'printSecondaryReplicationInfo'", "'printReplicationInfo'", "'printCollectionStats'", 
			"'listCommands'", "'hostInfo'", "'help'", "'getSiblingDB'", "'getReplicationInfo'", 
			"'getProfilingStatus'", "'getName'", "'getMongo'", "'getLogComponents'", 
			"'getCollectionNames'", "'getCollectionInfos'", "'fsyncUnlock'", "'fsyncLock'", 
			"'createView'", "'new'", null, null, null, null, "'.'", "'('", "')'", 
			"'['", "']'", "'{'", "'}'", "','", "':'", "'\"'", "'''", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SPACE", "COMMENT1", "COMMENT2", "DB", "SHOW", "DATABASES", "DBS", 
			"COLLECTIONS", "USE", "SERVER_BUILD_INFO", "HELLO", "SERVER_STATUS", 
			"DROP_DATABASE", "CREATE_COLLECTION", "STATS", "SET_PROFILING_LEVEL", 
			"ADMIN_COMMAND", "GET_COLLECTION", "AGGREGATE", "CURRENT_OP", "KILL_OP", 
			"COUNT_DOCUMENTS", "CREATE_INDEXES", "CREATE_INDEX", "DISTINCT", "DROP_INDEXES", 
			"DROP_INDEX", "COUNT", "ESTIMATED_DOCUMENT_COUNT", "GET_INDEXES", "LATENCY_STATS", 
			"RENAME_COLLECTION", "REPLACE_ONE", "VALIDATE", "EXPLAIN", "BULK_WRITE", 
			"DATA_SIZE", "RUN_COMMAND", "HIDE_INDEX", "UNHIDE_INDEX", "FIND", "S_SKIP", 
			"LIMIT", "SORT", "S_COMMENT", "BATCH_SIZE", "MAX_TIME_MS", "COLLATION", 
			"LET", "MAX", "MIN", "RETURN_KEY", "SHOW_RECORD_ID", "NO_CURSOR_TIMEOUT", 
			"MAX_AWAIT_TIME_MS", "ALLOW_DISK_USE", "FIND_ONE", "FIND_ONE_AND_DELETE", 
			"FIND_ONE_AND_REPLACE", "FIND_ONE_AND_UPDATE", "INSERT", "INSERT_ONE", 
			"INSERT_MANY", "DELETE_ONE", "DELETE_MANY", "UPDATE_ONE", "UPDATE_MANY", 
			"UPDATE", "DROP", "WATCH", "VERSION", "SHUTDOWN_SERVER", "SET_LOG_LEVEL", 
			"SERVER_CMD_LINE_OPTS", "ROTATE_CERTIFICATES", "PRINT_SHARDING_STATUS", 
			"PRINT_SECONDARY_REPLICATION_INFO", "PRINT_REPLICATION_INFO", "PRINT_COLLECTION_STATS", 
			"LIST_COMMANDS", "HOST_INFO", "HELP", "GET_SIBLING_DB", "GET_REPLICATION_INFO", 
			"GET_PROFILING_STATUS", "GET_NAME", "GET_MONGO", "GET_LOG_COMPONENTS", 
			"GET_COLLECTION_NAMES", "GET_COLLECTION_INFOS", "FSYNC_UNLOCK", "FSYNC_LOCK", 
			"CREATE_VIEW", "NEW", "TRUE", "FALSE", "NULL", "STRING", "DOT", "LS_BRACKET", 
			"RS_BRACKET", "LM_BRACKET", "RM_BRACKET", "LB_BRACKET", "RB_BRACKET", 
			"COMMA", "COLON", "DOUBLE_QUOTA", "SINGLE_QUOTA", "SEMI", "ID", "NUMBER"
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
	public String getGrammarFileName() { return "MongoParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MongoParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext {
		public List<CommandContext> command() {
			return getRuleContexts(CommandContext.class);
		}
		public CommandContext command(int i) {
			return getRuleContext(CommandContext.class,i);
		}
		public TerminalNode EOF() { return getToken(MongoParser.EOF, 0); }
		public List<TerminalNode> SEMI() { return getTokens(MongoParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(MongoParser.SEMI, i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitRoot(this);
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
			setState(96);
			command();
			setState(105);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(98); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(97);
						match(SEMI);
						}
						}
						setState(100); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SEMI );
					setState(102);
					command();
					}
					} 
				}
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(108);
				match(SEMI);
				}
				}
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(114);
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

	public static class CommandContext extends ParserRuleContext {
		public FunctionCommandContext functionCommand() {
			return getRuleContext(FunctionCommandContext.class,0);
		}
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_command);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			functionCommand();
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

	public static class FunctionCommandContext extends ParserRuleContext {
		public DatabaseFunctionContext databaseFunction() {
			return getRuleContext(DatabaseFunctionContext.class,0);
		}
		public CollectionFunctionContext collectionFunction() {
			return getRuleContext(CollectionFunctionContext.class,0);
		}
		public EnvironmentFunctionContext environmentFunction() {
			return getRuleContext(EnvironmentFunctionContext.class,0);
		}
		public ShowFunctionContext showFunction() {
			return getRuleContext(ShowFunctionContext.class,0);
		}
		public UseContext use() {
			return getRuleContext(UseContext.class,0);
		}
		public FunctionCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFunctionCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFunctionCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFunctionCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCommandContext functionCommand() throws RecognitionException {
		FunctionCommandContext _localctx = new FunctionCommandContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_functionCommand);
		try {
			setState(123);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(118);
				databaseFunction();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(119);
				collectionFunction();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(120);
				environmentFunction();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(121);
				showFunction();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(122);
				use();
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

	public static class UseContext extends ParserRuleContext {
		public TerminalNode USE() { return getToken(MongoParser.USE, 0); }
		public TerminalNode ID() { return getToken(MongoParser.ID, 0); }
		public UseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_use; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterUse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitUse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitUse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseContext use() throws RecognitionException {
		UseContext _localctx = new UseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_use);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			match(USE);
			setState(126);
			match(ID);
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

	public static class DatabaseFunctionContext extends ParserRuleContext {
		public DatabaseFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_databaseFunction; }
	 
		public DatabaseFunctionContext() { }
		public void copyFrom(DatabaseFunctionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FsyncLockContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode FSYNC_LOCK() { return getToken(MongoParser.FSYNC_LOCK, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FsyncLockContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFsyncLock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFsyncLock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFsyncLock(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class HostInfoContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode HOST_INFO() { return getToken(MongoParser.HOST_INFO, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public HostInfoContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterHostInfo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitHostInfo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitHostInfo(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AdminCommandContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode ADMIN_COMMAND() { return getToken(MongoParser.ADMIN_COMMAND, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public AdminCommandContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterAdminCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitAdminCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitAdminCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CurrentOpContext extends DatabaseFunctionContext {
		public ObjContext options;
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode CURRENT_OP() { return getToken(MongoParser.CURRENT_OP, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public CurrentOpContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCurrentOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCurrentOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCurrentOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FsyncUnlockContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode FSYNC_UNLOCK() { return getToken(MongoParser.FSYNC_UNLOCK, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FsyncUnlockContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFsyncUnlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFsyncUnlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFsyncUnlock(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GetCollectionNamesContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode GET_COLLECTION_NAMES() { return getToken(MongoParser.GET_COLLECTION_NAMES, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public GetCollectionNamesContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterGetCollectionNames(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitGetCollectionNames(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitGetCollectionNames(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GetProfilingStatusContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode GET_PROFILING_STATUS() { return getToken(MongoParser.GET_PROFILING_STATUS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public GetProfilingStatusContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterGetProfilingStatus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitGetProfilingStatus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitGetProfilingStatus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GetCollectionInfosContext extends DatabaseFunctionContext {
		public ObjContext filter;
		public ObjContext options;
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode GET_COLLECTION_INFOS() { return getToken(MongoParser.GET_COLLECTION_INFOS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public GetCollectionInfosContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterGetCollectionInfos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitGetCollectionInfos(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitGetCollectionInfos(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class KillOpContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode KILL_OP() { return getToken(MongoParser.KILL_OP, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public KillOpContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterKillOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitKillOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitKillOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbStatsContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode STATS() { return getToken(MongoParser.STATS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public DbStatsContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbStats(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbStats(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbStats(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GetLogComponentsContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode GET_LOG_COMPONENTS() { return getToken(MongoParser.GET_LOG_COMPONENTS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public GetLogComponentsContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterGetLogComponents(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitGetLogComponents(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitGetLogComponents(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbCreateCollectionContext extends DatabaseFunctionContext {
		public ObjContext options;
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode CREATE_COLLECTION() { return getToken(MongoParser.CREATE_COLLECTION, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public DbCreateCollectionContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbCreateCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbCreateCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbCreateCollection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbDropDatabaseContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode DROP_DATABASE() { return getToken(MongoParser.DROP_DATABASE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public DbDropDatabaseContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbDropDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbDropDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbDropDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbAggregateContext extends DatabaseFunctionContext {
		public ArrContext pipline;
		public ObjContext options;
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode AGGREGATE() { return getToken(MongoParser.AGGREGATE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public DbAggregateContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbAggregate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbAggregate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbAggregate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RunCommandContext extends DatabaseFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode RUN_COMMAND() { return getToken(MongoParser.RUN_COMMAND, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public RunCommandContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterRunCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitRunCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitRunCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetProfilingLevelContext extends DatabaseFunctionContext {
		public ObjContext options;
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode SET_PROFILING_LEVEL() { return getToken(MongoParser.SET_PROFILING_LEVEL, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public SetProfilingLevelContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterSetProfilingLevel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitSetProfilingLevel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitSetProfilingLevel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbCreateViewContext extends DatabaseFunctionContext {
		public Token viewName;
		public Token sourceName;
		public ArrContext pipeline;
		public ObjContext options;
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode CREATE_VIEW() { return getToken(MongoParser.CREATE_VIEW, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<TerminalNode> STRING() { return getTokens(MongoParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(MongoParser.STRING, i);
		}
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public DbCreateViewContext(DatabaseFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbCreateView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbCreateView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbCreateView(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatabaseFunctionContext databaseFunction() throws RecognitionException {
		DatabaseFunctionContext _localctx = new DatabaseFunctionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_databaseFunction);
		int _la;
		try {
			setState(258);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				_localctx = new DbDropDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(128);
				match(DB);
				setState(129);
				match(DOT);
				setState(130);
				match(DROP_DATABASE);
				setState(131);
				match(LS_BRACKET);
				setState(132);
				match(RS_BRACKET);
				}
				break;
			case 2:
				_localctx = new DbCreateCollectionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(DB);
				setState(134);
				match(DOT);
				setState(135);
				match(CREATE_COLLECTION);
				setState(136);
				match(LS_BRACKET);
				setState(137);
				match(STRING);
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(138);
					match(COMMA);
					setState(139);
					((DbCreateCollectionContext)_localctx).options = obj();
					}
				}

				setState(142);
				match(RS_BRACKET);
				}
				break;
			case 3:
				_localctx = new DbCreateViewContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(143);
				match(DB);
				setState(144);
				match(DOT);
				setState(145);
				match(CREATE_VIEW);
				setState(146);
				match(LS_BRACKET);
				setState(147);
				((DbCreateViewContext)_localctx).viewName = match(STRING);
				setState(148);
				match(COMMA);
				setState(149);
				((DbCreateViewContext)_localctx).sourceName = match(STRING);
				setState(150);
				match(COMMA);
				setState(151);
				((DbCreateViewContext)_localctx).pipeline = arr();
				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(152);
					match(COMMA);
					setState(153);
					((DbCreateViewContext)_localctx).options = obj();
					}
				}

				setState(156);
				match(RS_BRACKET);
				}
				break;
			case 4:
				_localctx = new DbStatsContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(158);
				match(DB);
				setState(159);
				match(DOT);
				setState(160);
				match(STATS);
				setState(161);
				match(LS_BRACKET);
				setState(164);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LB_BRACKET:
					{
					setState(162);
					obj();
					}
					break;
				case NUMBER:
					{
					setState(163);
					match(NUMBER);
					}
					break;
				case RS_BRACKET:
					break;
				default:
					break;
				}
				setState(166);
				match(RS_BRACKET);
				}
				break;
			case 5:
				_localctx = new SetProfilingLevelContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(167);
				match(DB);
				setState(168);
				match(DOT);
				setState(169);
				match(SET_PROFILING_LEVEL);
				setState(170);
				match(LS_BRACKET);
				setState(171);
				match(NUMBER);
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(172);
					match(COMMA);
					setState(173);
					((SetProfilingLevelContext)_localctx).options = obj();
					}
				}

				setState(176);
				match(RS_BRACKET);
				}
				break;
			case 6:
				_localctx = new GetCollectionNamesContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(177);
				match(DB);
				setState(178);
				match(DOT);
				setState(179);
				match(GET_COLLECTION_NAMES);
				setState(180);
				match(LS_BRACKET);
				setState(181);
				match(RS_BRACKET);
				}
				break;
			case 7:
				_localctx = new GetCollectionInfosContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(182);
				match(DB);
				setState(183);
				match(DOT);
				setState(184);
				match(GET_COLLECTION_INFOS);
				setState(185);
				match(LS_BRACKET);
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LB_BRACKET) {
					{
					setState(186);
					((GetCollectionInfosContext)_localctx).filter = obj();
					setState(189);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(187);
						match(COMMA);
						setState(188);
						((GetCollectionInfosContext)_localctx).options = obj();
						}
					}

					}
				}

				setState(193);
				match(RS_BRACKET);
				}
				break;
			case 8:
				_localctx = new GetLogComponentsContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(194);
				match(DB);
				setState(195);
				match(DOT);
				setState(196);
				match(GET_LOG_COMPONENTS);
				setState(197);
				match(LS_BRACKET);
				setState(198);
				match(RS_BRACKET);
				}
				break;
			case 9:
				_localctx = new GetProfilingStatusContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(199);
				match(DB);
				setState(200);
				match(DOT);
				setState(201);
				match(GET_PROFILING_STATUS);
				setState(202);
				match(LS_BRACKET);
				setState(203);
				match(RS_BRACKET);
				}
				break;
			case 10:
				_localctx = new HostInfoContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(204);
				match(DB);
				setState(205);
				match(DOT);
				setState(206);
				match(HOST_INFO);
				setState(207);
				match(LS_BRACKET);
				setState(208);
				match(RS_BRACKET);
				}
				break;
			case 11:
				_localctx = new AdminCommandContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(209);
				match(DB);
				setState(210);
				match(DOT);
				setState(211);
				match(ADMIN_COMMAND);
				setState(212);
				match(LS_BRACKET);
				setState(213);
				obj();
				setState(214);
				match(RS_BRACKET);
				}
				break;
			case 12:
				_localctx = new RunCommandContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(216);
				match(DB);
				setState(217);
				match(DOT);
				setState(218);
				match(RUN_COMMAND);
				setState(219);
				match(LS_BRACKET);
				setState(220);
				obj();
				setState(221);
				match(RS_BRACKET);
				}
				break;
			case 13:
				_localctx = new DbAggregateContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(223);
				match(DB);
				setState(224);
				match(DOT);
				setState(225);
				match(AGGREGATE);
				setState(226);
				match(LS_BRACKET);
				setState(227);
				((DbAggregateContext)_localctx).pipline = arr();
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(228);
					match(COMMA);
					setState(229);
					((DbAggregateContext)_localctx).options = obj();
					}
				}

				setState(232);
				match(RS_BRACKET);
				}
				break;
			case 14:
				_localctx = new CurrentOpContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(234);
				match(DB);
				setState(235);
				match(DOT);
				setState(236);
				match(CURRENT_OP);
				setState(237);
				match(LS_BRACKET);
				setState(239);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LB_BRACKET) {
					{
					setState(238);
					((CurrentOpContext)_localctx).options = obj();
					}
				}

				setState(241);
				match(RS_BRACKET);
				}
				break;
			case 15:
				_localctx = new KillOpContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(242);
				match(DB);
				setState(243);
				match(DOT);
				setState(244);
				match(KILL_OP);
				setState(245);
				match(LS_BRACKET);
				setState(246);
				match(NUMBER);
				setState(247);
				match(RS_BRACKET);
				}
				break;
			case 16:
				_localctx = new FsyncLockContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(248);
				match(DB);
				setState(249);
				match(DOT);
				setState(250);
				match(FSYNC_LOCK);
				setState(251);
				match(LS_BRACKET);
				setState(252);
				match(RS_BRACKET);
				}
				break;
			case 17:
				_localctx = new FsyncUnlockContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(253);
				match(DB);
				setState(254);
				match(DOT);
				setState(255);
				match(FSYNC_UNLOCK);
				setState(256);
				match(LS_BRACKET);
				setState(257);
				match(RS_BRACKET);
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

	public static class ShowFunctionContext extends ParserRuleContext {
		public ShowFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_showFunction; }
	 
		public ShowFunctionContext() { }
		public void copyFrom(ShowFunctionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ShowCollectionsContext extends ShowFunctionContext {
		public TerminalNode SHOW() { return getToken(MongoParser.SHOW, 0); }
		public TerminalNode COLLECTIONS() { return getToken(MongoParser.COLLECTIONS, 0); }
		public ShowCollectionsContext(ShowFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterShowCollections(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitShowCollections(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitShowCollections(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowDatabasesContext extends ShowFunctionContext {
		public TerminalNode SHOW() { return getToken(MongoParser.SHOW, 0); }
		public TerminalNode DATABASES() { return getToken(MongoParser.DATABASES, 0); }
		public TerminalNode DBS() { return getToken(MongoParser.DBS, 0); }
		public ShowDatabasesContext(ShowFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterShowDatabases(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitShowDatabases(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitShowDatabases(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShowFunctionContext showFunction() throws RecognitionException {
		ShowFunctionContext _localctx = new ShowFunctionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_showFunction);
		int _la;
		try {
			setState(264);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				_localctx = new ShowDatabasesContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(260);
				match(SHOW);
				setState(261);
				_la = _input.LA(1);
				if ( !(_la==DATABASES || _la==DBS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				_localctx = new ShowCollectionsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(262);
				match(SHOW);
				setState(263);
				match(COLLECTIONS);
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

	public static class EnvironmentFunctionContext extends ParserRuleContext {
		public EnvironmentFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_environmentFunction; }
	 
		public EnvironmentFunctionContext() { }
		public void copyFrom(EnvironmentFunctionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DbServerBuildInfoContext extends EnvironmentFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode SERVER_BUILD_INFO() { return getToken(MongoParser.SERVER_BUILD_INFO, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public DbServerBuildInfoContext(EnvironmentFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbServerBuildInfo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbServerBuildInfo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbServerBuildInfo(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbHelloContext extends EnvironmentFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode HELLO() { return getToken(MongoParser.HELLO, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public DbHelloContext(EnvironmentFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbHello(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbHello(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbHello(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbServerStatusContext extends EnvironmentFunctionContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode SERVER_STATUS() { return getToken(MongoParser.SERVER_STATUS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public DbServerStatusContext(EnvironmentFunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDbServerStatus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDbServerStatus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDbServerStatus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnvironmentFunctionContext environmentFunction() throws RecognitionException {
		EnvironmentFunctionContext _localctx = new EnvironmentFunctionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_environmentFunction);
		try {
			setState(281);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				_localctx = new DbServerBuildInfoContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(266);
				match(DB);
				setState(267);
				match(DOT);
				setState(268);
				match(SERVER_BUILD_INFO);
				setState(269);
				match(LS_BRACKET);
				setState(270);
				match(RS_BRACKET);
				}
				break;
			case 2:
				_localctx = new DbHelloContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(271);
				match(DB);
				setState(272);
				match(DOT);
				setState(273);
				match(HELLO);
				setState(274);
				match(LS_BRACKET);
				setState(275);
				match(RS_BRACKET);
				}
				break;
			case 3:
				_localctx = new DbServerStatusContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(276);
				match(DB);
				setState(277);
				match(DOT);
				setState(278);
				match(SERVER_STATUS);
				setState(279);
				match(LS_BRACKET);
				setState(280);
				match(RS_BRACKET);
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

	public static class CollectionFunctionContext extends ParserRuleContext {
		public CollectionNameContext collectionName() {
			return getRuleContext(CollectionNameContext.class,0);
		}
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public CollectionMethodContext collectionMethod() {
			return getRuleContext(CollectionMethodContext.class,0);
		}
		public CollectionFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCollectionFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCollectionFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCollectionFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionFunctionContext collectionFunction() throws RecognitionException {
		CollectionFunctionContext _localctx = new CollectionFunctionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_collectionFunction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			collectionName();
			setState(284);
			match(DOT);
			setState(285);
			collectionMethod();
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

	public static class CollectionMethodContext extends ParserRuleContext {
		public InsertContext insert() {
			return getRuleContext(InsertContext.class,0);
		}
		public InsertOneContext insertOne() {
			return getRuleContext(InsertOneContext.class,0);
		}
		public InsertManyContext insertMany() {
			return getRuleContext(InsertManyContext.class,0);
		}
		public FindContext find() {
			return getRuleContext(FindContext.class,0);
		}
		public FindOneContext findOne() {
			return getRuleContext(FindOneContext.class,0);
		}
		public FindOneAndDeleteContext findOneAndDelete() {
			return getRuleContext(FindOneAndDeleteContext.class,0);
		}
		public FindOneAndReplaceContext findOneAndReplace() {
			return getRuleContext(FindOneAndReplaceContext.class,0);
		}
		public FindOneAndUpdateContext findOneAndUpdate() {
			return getRuleContext(FindOneAndUpdateContext.class,0);
		}
		public DeleteOneContext deleteOne() {
			return getRuleContext(DeleteOneContext.class,0);
		}
		public DeleteManyContext deleteMany() {
			return getRuleContext(DeleteManyContext.class,0);
		}
		public UpdateContext update() {
			return getRuleContext(UpdateContext.class,0);
		}
		public DropContext drop() {
			return getRuleContext(DropContext.class,0);
		}
		public AggregateContext aggregate() {
			return getRuleContext(AggregateContext.class,0);
		}
		public CountDocumentsContext countDocuments() {
			return getRuleContext(CountDocumentsContext.class,0);
		}
		public CreateIndexContext createIndex() {
			return getRuleContext(CreateIndexContext.class,0);
		}
		public CreateIndexesContext createIndexes() {
			return getRuleContext(CreateIndexesContext.class,0);
		}
		public DistinctContext distinct() {
			return getRuleContext(DistinctContext.class,0);
		}
		public DropIndexContext dropIndex() {
			return getRuleContext(DropIndexContext.class,0);
		}
		public DropIndexesContext dropIndexes() {
			return getRuleContext(DropIndexesContext.class,0);
		}
		public CountContext count() {
			return getRuleContext(CountContext.class,0);
		}
		public EstimatedDocumentCountContext estimatedDocumentCount() {
			return getRuleContext(EstimatedDocumentCountContext.class,0);
		}
		public GetIndexesContext getIndexes() {
			return getRuleContext(GetIndexesContext.class,0);
		}
		public HideIndexContext hideIndex() {
			return getRuleContext(HideIndexContext.class,0);
		}
		public LatencyStatsContext latencyStats() {
			return getRuleContext(LatencyStatsContext.class,0);
		}
		public RenameCollectionContext renameCollection() {
			return getRuleContext(RenameCollectionContext.class,0);
		}
		public ReplaceOneContext replaceOne() {
			return getRuleContext(ReplaceOneContext.class,0);
		}
		public ValidateContext validate() {
			return getRuleContext(ValidateContext.class,0);
		}
		public ExplainContext explain() {
			return getRuleContext(ExplainContext.class,0);
		}
		public DataSizeContext dataSize() {
			return getRuleContext(DataSizeContext.class,0);
		}
		public CollectionMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCollectionMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCollectionMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCollectionMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionMethodContext collectionMethod() throws RecognitionException {
		CollectionMethodContext _localctx = new CollectionMethodContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_collectionMethod);
		try {
			setState(316);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INSERT:
				enterOuterAlt(_localctx, 1);
				{
				setState(287);
				insert();
				}
				break;
			case INSERT_ONE:
				enterOuterAlt(_localctx, 2);
				{
				setState(288);
				insertOne();
				}
				break;
			case INSERT_MANY:
				enterOuterAlt(_localctx, 3);
				{
				setState(289);
				insertMany();
				}
				break;
			case FIND:
				enterOuterAlt(_localctx, 4);
				{
				setState(290);
				find();
				}
				break;
			case FIND_ONE:
				enterOuterAlt(_localctx, 5);
				{
				setState(291);
				findOne();
				}
				break;
			case FIND_ONE_AND_DELETE:
				enterOuterAlt(_localctx, 6);
				{
				setState(292);
				findOneAndDelete();
				}
				break;
			case FIND_ONE_AND_REPLACE:
				enterOuterAlt(_localctx, 7);
				{
				setState(293);
				findOneAndReplace();
				}
				break;
			case FIND_ONE_AND_UPDATE:
				enterOuterAlt(_localctx, 8);
				{
				setState(294);
				findOneAndUpdate();
				}
				break;
			case DELETE_ONE:
				enterOuterAlt(_localctx, 9);
				{
				setState(295);
				deleteOne();
				}
				break;
			case DELETE_MANY:
				enterOuterAlt(_localctx, 10);
				{
				setState(296);
				deleteMany();
				}
				break;
			case UPDATE_ONE:
			case UPDATE_MANY:
			case UPDATE:
				enterOuterAlt(_localctx, 11);
				{
				setState(297);
				update();
				}
				break;
			case DROP:
				enterOuterAlt(_localctx, 12);
				{
				setState(298);
				drop();
				}
				break;
			case AGGREGATE:
				enterOuterAlt(_localctx, 13);
				{
				setState(299);
				aggregate();
				}
				break;
			case COUNT_DOCUMENTS:
				enterOuterAlt(_localctx, 14);
				{
				setState(300);
				countDocuments();
				}
				break;
			case CREATE_INDEX:
				enterOuterAlt(_localctx, 15);
				{
				setState(301);
				createIndex();
				}
				break;
			case CREATE_INDEXES:
				enterOuterAlt(_localctx, 16);
				{
				setState(302);
				createIndexes();
				}
				break;
			case DISTINCT:
				enterOuterAlt(_localctx, 17);
				{
				setState(303);
				distinct();
				}
				break;
			case DROP_INDEX:
				enterOuterAlt(_localctx, 18);
				{
				setState(304);
				dropIndex();
				}
				break;
			case DROP_INDEXES:
				enterOuterAlt(_localctx, 19);
				{
				setState(305);
				dropIndexes();
				}
				break;
			case COUNT:
				enterOuterAlt(_localctx, 20);
				{
				setState(306);
				count();
				}
				break;
			case ESTIMATED_DOCUMENT_COUNT:
				enterOuterAlt(_localctx, 21);
				{
				setState(307);
				estimatedDocumentCount();
				}
				break;
			case GET_INDEXES:
				enterOuterAlt(_localctx, 22);
				{
				setState(308);
				getIndexes();
				}
				break;
			case HIDE_INDEX:
			case UNHIDE_INDEX:
				enterOuterAlt(_localctx, 23);
				{
				setState(309);
				hideIndex();
				}
				break;
			case LATENCY_STATS:
				enterOuterAlt(_localctx, 24);
				{
				setState(310);
				latencyStats();
				}
				break;
			case RENAME_COLLECTION:
				enterOuterAlt(_localctx, 25);
				{
				setState(311);
				renameCollection();
				}
				break;
			case REPLACE_ONE:
				enterOuterAlt(_localctx, 26);
				{
				setState(312);
				replaceOne();
				}
				break;
			case VALIDATE:
				enterOuterAlt(_localctx, 27);
				{
				setState(313);
				validate();
				}
				break;
			case EXPLAIN:
				enterOuterAlt(_localctx, 28);
				{
				setState(314);
				explain();
				}
				break;
			case DATA_SIZE:
				enterOuterAlt(_localctx, 29);
				{
				setState(315);
				dataSize();
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

	public static class DataSizeContext extends ParserRuleContext {
		public TerminalNode DATA_SIZE() { return getToken(MongoParser.DATA_SIZE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public DataSizeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataSize; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDataSize(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDataSize(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDataSize(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataSizeContext dataSize() throws RecognitionException {
		DataSizeContext _localctx = new DataSizeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_dataSize);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(DATA_SIZE);
			setState(319);
			match(LS_BRACKET);
			setState(320);
			match(RS_BRACKET);
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

	public static class ExplainContext extends ParserRuleContext {
		public Token verbosity;
		public TerminalNode EXPLAIN() { return getToken(MongoParser.EXPLAIN, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public ExplainMethodContext explainMethod() {
			return getRuleContext(ExplainMethodContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public ExplainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explain; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterExplain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitExplain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitExplain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplainContext explain() throws RecognitionException {
		ExplainContext _localctx = new ExplainContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_explain);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			match(EXPLAIN);
			setState(323);
			match(LS_BRACKET);
			setState(325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(324);
				((ExplainContext)_localctx).verbosity = match(STRING);
				}
			}

			setState(327);
			match(RS_BRACKET);
			setState(328);
			match(DOT);
			setState(329);
			explainMethod();
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

	public static class ExplainMethodContext extends ParserRuleContext {
		public AggregateContext aggregate() {
			return getRuleContext(AggregateContext.class,0);
		}
		public FindContext find() {
			return getRuleContext(FindContext.class,0);
		}
		public CountContext count() {
			return getRuleContext(CountContext.class,0);
		}
		public DistinctContext distinct() {
			return getRuleContext(DistinctContext.class,0);
		}
		public ExplainMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explainMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterExplainMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitExplainMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitExplainMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplainMethodContext explainMethod() throws RecognitionException {
		ExplainMethodContext _localctx = new ExplainMethodContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_explainMethod);
		try {
			setState(335);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AGGREGATE:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				aggregate();
				}
				break;
			case FIND:
				enterOuterAlt(_localctx, 2);
				{
				setState(332);
				find();
				}
				break;
			case COUNT:
				enterOuterAlt(_localctx, 3);
				{
				setState(333);
				count();
				}
				break;
			case DISTINCT:
				enterOuterAlt(_localctx, 4);
				{
				setState(334);
				distinct();
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

	public static class ValidateContext extends ParserRuleContext {
		public ObjContext options;
		public TerminalNode VALIDATE() { return getToken(MongoParser.VALIDATE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public ValidateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterValidate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitValidate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitValidate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidateContext validate() throws RecognitionException {
		ValidateContext _localctx = new ValidateContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_validate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			match(VALIDATE);
			setState(338);
			match(LS_BRACKET);
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(339);
				((ValidateContext)_localctx).options = obj();
				}
			}

			setState(342);
			match(RS_BRACKET);
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

	public static class ReplaceOneContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext replacement;
		public ObjContext options;
		public TerminalNode REPLACE_ONE() { return getToken(MongoParser.REPLACE_ONE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public ReplaceOneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_replaceOne; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterReplaceOne(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitReplaceOne(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitReplaceOne(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReplaceOneContext replaceOne() throws RecognitionException {
		ReplaceOneContext _localctx = new ReplaceOneContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_replaceOne);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(REPLACE_ONE);
			setState(345);
			match(LS_BRACKET);
			setState(346);
			((ReplaceOneContext)_localctx).filter = obj();
			setState(347);
			match(COMMA);
			setState(348);
			((ReplaceOneContext)_localctx).replacement = obj();
			setState(351);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(349);
				match(COMMA);
				setState(350);
				((ReplaceOneContext)_localctx).options = obj();
				}
			}

			setState(353);
			match(RS_BRACKET);
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

	public static class RenameCollectionContext extends ParserRuleContext {
		public Token dropTarget;
		public TerminalNode RENAME_COLLECTION() { return getToken(MongoParser.RENAME_COLLECTION, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public TerminalNode TRUE() { return getToken(MongoParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MongoParser.FALSE, 0); }
		public RenameCollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_renameCollection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterRenameCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitRenameCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitRenameCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenameCollectionContext renameCollection() throws RecognitionException {
		RenameCollectionContext _localctx = new RenameCollectionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_renameCollection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			match(RENAME_COLLECTION);
			setState(356);
			match(LS_BRACKET);
			setState(357);
			match(STRING);
			setState(360);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(358);
				match(COMMA);
				setState(359);
				((RenameCollectionContext)_localctx).dropTarget = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
					((RenameCollectionContext)_localctx).dropTarget = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(362);
			match(RS_BRACKET);
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

	public static class LatencyStatsContext extends ParserRuleContext {
		public TerminalNode LATENCY_STATS() { return getToken(MongoParser.LATENCY_STATS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public LatencyStatsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_latencyStats; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterLatencyStats(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitLatencyStats(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitLatencyStats(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LatencyStatsContext latencyStats() throws RecognitionException {
		LatencyStatsContext _localctx = new LatencyStatsContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_latencyStats);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			match(LATENCY_STATS);
			setState(365);
			match(LS_BRACKET);
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(366);
				obj();
				}
			}

			setState(369);
			match(RS_BRACKET);
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

	public static class GetIndexesContext extends ParserRuleContext {
		public TerminalNode GET_INDEXES() { return getToken(MongoParser.GET_INDEXES, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public GetIndexesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getIndexes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterGetIndexes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitGetIndexes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitGetIndexes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GetIndexesContext getIndexes() throws RecognitionException {
		GetIndexesContext _localctx = new GetIndexesContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_getIndexes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			match(GET_INDEXES);
			setState(372);
			match(LS_BRACKET);
			setState(373);
			match(RS_BRACKET);
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

	public static class HideIndexContext extends ParserRuleContext {
		public ObjContext partten;
		public Token name;
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode HIDE_INDEX() { return getToken(MongoParser.HIDE_INDEX, 0); }
		public TerminalNode UNHIDE_INDEX() { return getToken(MongoParser.UNHIDE_INDEX, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public HideIndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hideIndex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterHideIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitHideIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitHideIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HideIndexContext hideIndex() throws RecognitionException {
		HideIndexContext _localctx = new HideIndexContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_hideIndex);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(375);
			_la = _input.LA(1);
			if ( !(_la==HIDE_INDEX || _la==UNHIDE_INDEX) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(376);
			match(LS_BRACKET);
			setState(379);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LB_BRACKET:
				{
				setState(377);
				((HideIndexContext)_localctx).partten = obj();
				}
				break;
			case STRING:
				{
				setState(378);
				((HideIndexContext)_localctx).name = match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(381);
			match(RS_BRACKET);
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

	public static class EstimatedDocumentCountContext extends ParserRuleContext {
		public ObjContext option;
		public TerminalNode ESTIMATED_DOCUMENT_COUNT() { return getToken(MongoParser.ESTIMATED_DOCUMENT_COUNT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public EstimatedDocumentCountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_estimatedDocumentCount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterEstimatedDocumentCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitEstimatedDocumentCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitEstimatedDocumentCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EstimatedDocumentCountContext estimatedDocumentCount() throws RecognitionException {
		EstimatedDocumentCountContext _localctx = new EstimatedDocumentCountContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_estimatedDocumentCount);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			match(ESTIMATED_DOCUMENT_COUNT);
			setState(384);
			match(LS_BRACKET);
			setState(386);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(385);
				((EstimatedDocumentCountContext)_localctx).option = obj();
				}
			}

			setState(388);
			match(RS_BRACKET);
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

	public static class CountContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext option;
		public TerminalNode COUNT() { return getToken(MongoParser.COUNT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public CountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_count; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountContext count() throws RecognitionException {
		CountContext _localctx = new CountContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_count);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(COUNT);
			setState(391);
			match(LS_BRACKET);
			setState(397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(392);
				((CountContext)_localctx).filter = obj();
				setState(395);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(393);
					match(COMMA);
					setState(394);
					((CountContext)_localctx).option = obj();
					}
				}

				}
			}

			setState(399);
			match(RS_BRACKET);
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

	public static class DropIndexContext extends ParserRuleContext {
		public TerminalNode DROP_INDEX() { return getToken(MongoParser.DROP_INDEX, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public DropIndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dropIndex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDropIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDropIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDropIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DropIndexContext dropIndex() throws RecognitionException {
		DropIndexContext _localctx = new DropIndexContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_dropIndex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(DROP_INDEX);
			setState(402);
			match(LS_BRACKET);
			setState(405);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LB_BRACKET:
				{
				setState(403);
				obj();
				}
				break;
			case STRING:
				{
				setState(404);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(407);
			match(RS_BRACKET);
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

	public static class DropIndexesContext extends ParserRuleContext {
		public TerminalNode DROP_INDEXES() { return getToken(MongoParser.DROP_INDEXES, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public DropIndexesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dropIndexes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDropIndexes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDropIndexes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDropIndexes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DropIndexesContext dropIndexes() throws RecognitionException {
		DropIndexesContext _localctx = new DropIndexesContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_dropIndexes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			match(DROP_INDEXES);
			setState(410);
			match(LS_BRACKET);
			setState(412);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (NEW - 94)) | (1L << (TRUE - 94)) | (1L << (FALSE - 94)) | (1L << (NULL - 94)) | (1L << (STRING - 94)) | (1L << (LM_BRACKET - 94)) | (1L << (LB_BRACKET - 94)) | (1L << (ID - 94)) | (1L << (NUMBER - 94)))) != 0)) {
				{
				setState(411);
				jsonString();
				}
			}

			setState(414);
			match(RS_BRACKET);
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

	public static class DistinctContext extends ParserRuleContext {
		public Token field;
		public ObjContext query;
		public ObjContext option;
		public TerminalNode DISTINCT() { return getToken(MongoParser.DISTINCT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public DistinctContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_distinct; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDistinct(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDistinct(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDistinct(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DistinctContext distinct() throws RecognitionException {
		DistinctContext _localctx = new DistinctContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_distinct);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(DISTINCT);
			setState(417);
			match(LS_BRACKET);
			setState(418);
			((DistinctContext)_localctx).field = match(STRING);
			setState(425);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(419);
				match(COMMA);
				setState(420);
				((DistinctContext)_localctx).query = obj();
				setState(423);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(421);
					match(COMMA);
					setState(422);
					((DistinctContext)_localctx).option = obj();
					}
				}

				}
			}

			setState(427);
			match(RS_BRACKET);
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

	public static class CreateIndexContext extends ParserRuleContext {
		public ObjContext keys;
		public ObjContext options;
		public Token commitQuorum;
		public TerminalNode CREATE_INDEX() { return getToken(MongoParser.CREATE_INDEX, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public CreateIndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createIndex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCreateIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCreateIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCreateIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateIndexContext createIndex() throws RecognitionException {
		CreateIndexContext _localctx = new CreateIndexContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_createIndex);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			match(CREATE_INDEX);
			setState(430);
			match(LS_BRACKET);
			setState(431);
			((CreateIndexContext)_localctx).keys = obj();
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(432);
				match(COMMA);
				setState(433);
				((CreateIndexContext)_localctx).options = obj();
				setState(436);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(434);
					match(COMMA);
					setState(435);
					((CreateIndexContext)_localctx).commitQuorum = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==STRING || _la==NUMBER) ) {
						((CreateIndexContext)_localctx).commitQuorum = (Token)_errHandler.recoverInline(this);
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

			setState(440);
			match(RS_BRACKET);
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

	public static class CreateIndexesContext extends ParserRuleContext {
		public ArrContext keys;
		public ObjContext options;
		public Token commitQuorum;
		public TerminalNode CREATE_INDEXES() { return getToken(MongoParser.CREATE_INDEXES, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public CreateIndexesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createIndexes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCreateIndexes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCreateIndexes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCreateIndexes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateIndexesContext createIndexes() throws RecognitionException {
		CreateIndexesContext _localctx = new CreateIndexesContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_createIndexes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			match(CREATE_INDEXES);
			setState(443);
			match(LS_BRACKET);
			setState(444);
			((CreateIndexesContext)_localctx).keys = arr();
			setState(451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(445);
				match(COMMA);
				setState(446);
				((CreateIndexesContext)_localctx).options = obj();
				setState(449);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(447);
					match(COMMA);
					setState(448);
					((CreateIndexesContext)_localctx).commitQuorum = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==STRING || _la==NUMBER) ) {
						((CreateIndexesContext)_localctx).commitQuorum = (Token)_errHandler.recoverInline(this);
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

			setState(453);
			match(RS_BRACKET);
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

	public static class AggregateContext extends ParserRuleContext {
		public ObjContext options;
		public TerminalNode AGGREGATE() { return getToken(MongoParser.AGGREGATE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public AggregateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aggregate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterAggregate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitAggregate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitAggregate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AggregateContext aggregate() throws RecognitionException {
		AggregateContext _localctx = new AggregateContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_aggregate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			match(AGGREGATE);
			setState(456);
			match(LS_BRACKET);
			setState(462);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LM_BRACKET) {
				{
				setState(457);
				arr();
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(458);
					match(COMMA);
					setState(459);
					((AggregateContext)_localctx).options = obj();
					}
				}

				}
			}

			setState(464);
			match(RS_BRACKET);
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

	public static class CountDocumentsContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext option;
		public TerminalNode COUNT_DOCUMENTS() { return getToken(MongoParser.COUNT_DOCUMENTS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public CountDocumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_countDocuments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCountDocuments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCountDocuments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCountDocuments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountDocumentsContext countDocuments() throws RecognitionException {
		CountDocumentsContext _localctx = new CountDocumentsContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_countDocuments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			match(COUNT_DOCUMENTS);
			setState(467);
			match(LS_BRACKET);
			setState(473);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(468);
				((CountDocumentsContext)_localctx).filter = obj();
				setState(471);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(469);
					match(COMMA);
					setState(470);
					((CountDocumentsContext)_localctx).option = obj();
					}
				}

				}
			}

			setState(475);
			match(RS_BRACKET);
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

	public static class FindContext extends ParserRuleContext {
		public JsonStringContext filter;
		public JsonStringContext projection;
		public ObjContext options;
		public TerminalNode FIND() { return getToken(MongoParser.FIND, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<TerminalNode> DOT() { return getTokens(MongoParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(MongoParser.DOT, i);
		}
		public List<FindConstarintContext> findConstarint() {
			return getRuleContexts(FindConstarintContext.class);
		}
		public FindConstarintContext findConstarint(int i) {
			return getRuleContext(FindConstarintContext.class,i);
		}
		public List<JsonStringContext> jsonString() {
			return getRuleContexts(JsonStringContext.class);
		}
		public JsonStringContext jsonString(int i) {
			return getRuleContext(JsonStringContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public FindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_find; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFind(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFind(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FindContext find() throws RecognitionException {
		FindContext _localctx = new FindContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_find);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			match(FIND);
			setState(478);
			match(LS_BRACKET);
			setState(488);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (NEW - 94)) | (1L << (TRUE - 94)) | (1L << (FALSE - 94)) | (1L << (NULL - 94)) | (1L << (STRING - 94)) | (1L << (LM_BRACKET - 94)) | (1L << (LB_BRACKET - 94)) | (1L << (ID - 94)) | (1L << (NUMBER - 94)))) != 0)) {
				{
				setState(479);
				((FindContext)_localctx).filter = jsonString();
				setState(486);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(480);
					match(COMMA);
					setState(481);
					((FindContext)_localctx).projection = jsonString();
					setState(484);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(482);
						match(COMMA);
						setState(483);
						((FindContext)_localctx).options = obj();
						}
					}

					}
				}

				}
			}

			setState(490);
			match(RS_BRACKET);
			setState(495);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(491);
				match(DOT);
				setState(492);
				findConstarint();
				}
				}
				setState(497);
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

	public static class FindConstarintContext extends ParserRuleContext {
		public FindConstarintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_findConstarint; }
	 
		public FindConstarintContext() { }
		public void copyFrom(FindConstarintContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExplainConstraintContext extends FindConstarintContext {
		public Token verbosity;
		public TerminalNode EXPLAIN() { return getToken(MongoParser.EXPLAIN, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public ExplainConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterExplainConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitExplainConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitExplainConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindMinConstraintContext extends FindConstarintContext {
		public TerminalNode MIN() { return getToken(MongoParser.MIN, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindMinConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindMinConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindMinConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindMinConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindSortConstraintContext extends FindConstarintContext {
		public TerminalNode SORT() { return getToken(MongoParser.SORT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindSortConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindSortConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindSortConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindSortConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindSkipConstraintContext extends FindConstarintContext {
		public TerminalNode S_SKIP() { return getToken(MongoParser.S_SKIP, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindSkipConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindSkipConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindSkipConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindSkipConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindAllowDiskUseConstraintContext extends FindConstarintContext {
		public Token bool;
		public TerminalNode ALLOW_DISK_USE() { return getToken(MongoParser.ALLOW_DISK_USE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode TRUE() { return getToken(MongoParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MongoParser.FALSE, 0); }
		public FindAllowDiskUseConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindAllowDiskUseConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindAllowDiskUseConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindAllowDiskUseConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindMaxTimeMSConstriantContext extends FindConstarintContext {
		public TerminalNode MAX_TIME_MS() { return getToken(MongoParser.MAX_TIME_MS, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindMaxTimeMSConstriantContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindMaxTimeMSConstriant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindMaxTimeMSConstriant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindMaxTimeMSConstriant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindReturnKeyConstraintContext extends FindConstarintContext {
		public Token bool;
		public TerminalNode RETURN_KEY() { return getToken(MongoParser.RETURN_KEY, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode TRUE() { return getToken(MongoParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MongoParser.FALSE, 0); }
		public FindReturnKeyConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindReturnKeyConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindReturnKeyConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindReturnKeyConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindLimitConstraintContext extends FindConstarintContext {
		public TerminalNode LIMIT() { return getToken(MongoParser.LIMIT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindLimitConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindLimitConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindLimitConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindLimitConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindCollationConstraintContext extends FindConstarintContext {
		public TerminalNode COLLATION() { return getToken(MongoParser.COLLATION, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindCollationConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindCollationConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindCollationConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindCollationConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindCommentConstraintContext extends FindConstarintContext {
		public TerminalNode S_COMMENT() { return getToken(MongoParser.S_COMMENT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindCommentConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindCommentConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindCommentConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindCommentConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindMaxConstraintContext extends FindConstarintContext {
		public TerminalNode MAX() { return getToken(MongoParser.MAX, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindMaxConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindMaxConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindMaxConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindMaxConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindShowRecordIdConstraintContext extends FindConstarintContext {
		public Token bool;
		public TerminalNode SHOW_RECORD_ID() { return getToken(MongoParser.SHOW_RECORD_ID, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode TRUE() { return getToken(MongoParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MongoParser.FALSE, 0); }
		public FindShowRecordIdConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindShowRecordIdConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindShowRecordIdConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindShowRecordIdConstraint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FindLetConstraintContext extends FindConstarintContext {
		public TerminalNode LET() { return getToken(MongoParser.LET, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public JsonStringContext jsonString() {
			return getRuleContext(JsonStringContext.class,0);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public FindLetConstraintContext(FindConstarintContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindLetConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindLetConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindLetConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FindConstarintContext findConstarint() throws RecognitionException {
		FindConstarintContext _localctx = new FindConstarintContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_findConstarint);
		int _la;
		try {
			setState(558);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case S_SKIP:
				_localctx = new FindSkipConstraintContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(498);
				match(S_SKIP);
				setState(499);
				match(LS_BRACKET);
				setState(500);
				match(NUMBER);
				setState(501);
				match(RS_BRACKET);
				}
				break;
			case LIMIT:
				_localctx = new FindLimitConstraintContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(502);
				match(LIMIT);
				setState(503);
				match(LS_BRACKET);
				setState(504);
				match(NUMBER);
				setState(505);
				match(RS_BRACKET);
				}
				break;
			case SORT:
				_localctx = new FindSortConstraintContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(506);
				match(SORT);
				setState(507);
				match(LS_BRACKET);
				setState(508);
				jsonString();
				setState(509);
				match(RS_BRACKET);
				}
				break;
			case S_COMMENT:
				_localctx = new FindCommentConstraintContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(511);
				match(S_COMMENT);
				setState(512);
				match(LS_BRACKET);
				setState(513);
				jsonString();
				setState(514);
				match(RS_BRACKET);
				}
				break;
			case MAX_TIME_MS:
				_localctx = new FindMaxTimeMSConstriantContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(516);
				match(MAX_TIME_MS);
				setState(517);
				match(LS_BRACKET);
				setState(518);
				match(NUMBER);
				setState(519);
				match(RS_BRACKET);
				}
				break;
			case COLLATION:
				_localctx = new FindCollationConstraintContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(520);
				match(COLLATION);
				setState(521);
				match(LS_BRACKET);
				setState(522);
				jsonString();
				setState(523);
				match(RS_BRACKET);
				}
				break;
			case ALLOW_DISK_USE:
				_localctx = new FindAllowDiskUseConstraintContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(525);
				match(ALLOW_DISK_USE);
				setState(526);
				match(LS_BRACKET);
				setState(527);
				((FindAllowDiskUseConstraintContext)_localctx).bool = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
					((FindAllowDiskUseConstraintContext)_localctx).bool = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(528);
				match(RS_BRACKET);
				}
				break;
			case MAX:
				_localctx = new FindMaxConstraintContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(529);
				match(MAX);
				setState(530);
				match(LS_BRACKET);
				setState(531);
				jsonString();
				setState(532);
				match(RS_BRACKET);
				}
				break;
			case MIN:
				_localctx = new FindMinConstraintContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(534);
				match(MIN);
				setState(535);
				match(LS_BRACKET);
				setState(536);
				jsonString();
				setState(537);
				match(RS_BRACKET);
				}
				break;
			case RETURN_KEY:
				_localctx = new FindReturnKeyConstraintContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(539);
				match(RETURN_KEY);
				setState(540);
				match(LS_BRACKET);
				setState(541);
				((FindReturnKeyConstraintContext)_localctx).bool = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
					((FindReturnKeyConstraintContext)_localctx).bool = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(542);
				match(RS_BRACKET);
				}
				break;
			case SHOW_RECORD_ID:
				_localctx = new FindShowRecordIdConstraintContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(543);
				match(SHOW_RECORD_ID);
				setState(544);
				match(LS_BRACKET);
				setState(545);
				((FindShowRecordIdConstraintContext)_localctx).bool = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
					((FindShowRecordIdConstraintContext)_localctx).bool = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(546);
				match(RS_BRACKET);
				}
				break;
			case LET:
				_localctx = new FindLetConstraintContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(547);
				match(LET);
				setState(548);
				match(LS_BRACKET);
				setState(549);
				jsonString();
				setState(550);
				match(RS_BRACKET);
				}
				break;
			case EXPLAIN:
				_localctx = new ExplainConstraintContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(552);
				match(EXPLAIN);
				setState(553);
				match(LS_BRACKET);
				setState(555);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(554);
					((ExplainConstraintContext)_localctx).verbosity = match(STRING);
					}
				}

				setState(557);
				match(RS_BRACKET);
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

	public static class FindOneContext extends ParserRuleContext {
		public JsonStringContext filter;
		public JsonStringContext projection;
		public ObjContext options;
		public TerminalNode FIND_ONE() { return getToken(MongoParser.FIND_ONE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<JsonStringContext> jsonString() {
			return getRuleContexts(JsonStringContext.class);
		}
		public JsonStringContext jsonString(int i) {
			return getRuleContext(JsonStringContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public FindOneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_findOne; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindOne(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindOne(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindOne(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FindOneContext findOne() throws RecognitionException {
		FindOneContext _localctx = new FindOneContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_findOne);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(560);
			match(FIND_ONE);
			setState(561);
			match(LS_BRACKET);
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (NEW - 94)) | (1L << (TRUE - 94)) | (1L << (FALSE - 94)) | (1L << (NULL - 94)) | (1L << (STRING - 94)) | (1L << (LM_BRACKET - 94)) | (1L << (LB_BRACKET - 94)) | (1L << (ID - 94)) | (1L << (NUMBER - 94)))) != 0)) {
				{
				setState(562);
				((FindOneContext)_localctx).filter = jsonString();
				setState(569);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(563);
					match(COMMA);
					setState(564);
					((FindOneContext)_localctx).projection = jsonString();
					setState(567);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(565);
						match(COMMA);
						setState(566);
						((FindOneContext)_localctx).options = obj();
						}
					}

					}
				}

				}
			}

			setState(573);
			match(RS_BRACKET);
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

	public static class FindOneAndDeleteContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext options;
		public TerminalNode FIND_ONE_AND_DELETE() { return getToken(MongoParser.FIND_ONE_AND_DELETE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public FindOneAndDeleteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_findOneAndDelete; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindOneAndDelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindOneAndDelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindOneAndDelete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FindOneAndDeleteContext findOneAndDelete() throws RecognitionException {
		FindOneAndDeleteContext _localctx = new FindOneAndDeleteContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_findOneAndDelete);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575);
			match(FIND_ONE_AND_DELETE);
			setState(576);
			match(LS_BRACKET);
			setState(582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(577);
				((FindOneAndDeleteContext)_localctx).filter = obj();
				setState(580);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(578);
					match(COMMA);
					setState(579);
					((FindOneAndDeleteContext)_localctx).options = obj();
					}
				}

				}
			}

			setState(584);
			match(RS_BRACKET);
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

	public static class FindOneAndReplaceContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext replace;
		public ObjContext options;
		public TerminalNode FIND_ONE_AND_REPLACE() { return getToken(MongoParser.FIND_ONE_AND_REPLACE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public FindOneAndReplaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_findOneAndReplace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindOneAndReplace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindOneAndReplace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindOneAndReplace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FindOneAndReplaceContext findOneAndReplace() throws RecognitionException {
		FindOneAndReplaceContext _localctx = new FindOneAndReplaceContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_findOneAndReplace);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586);
			match(FIND_ONE_AND_REPLACE);
			setState(587);
			match(LS_BRACKET);
			setState(588);
			((FindOneAndReplaceContext)_localctx).filter = obj();
			setState(595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(589);
				match(COMMA);
				setState(590);
				((FindOneAndReplaceContext)_localctx).replace = obj();
				setState(593);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(591);
					match(COMMA);
					setState(592);
					((FindOneAndReplaceContext)_localctx).options = obj();
					}
				}

				}
			}

			setState(597);
			match(RS_BRACKET);
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

	public static class FindOneAndUpdateContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext objUpdate;
		public ArrContext arrUpdate;
		public ObjContext options;
		public TerminalNode FIND_ONE_AND_UPDATE() { return getToken(MongoParser.FIND_ONE_AND_UPDATE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public FindOneAndUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_findOneAndUpdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterFindOneAndUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitFindOneAndUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitFindOneAndUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FindOneAndUpdateContext findOneAndUpdate() throws RecognitionException {
		FindOneAndUpdateContext _localctx = new FindOneAndUpdateContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_findOneAndUpdate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(599);
			match(FIND_ONE_AND_UPDATE);
			setState(600);
			match(LS_BRACKET);
			setState(601);
			((FindOneAndUpdateContext)_localctx).filter = obj();
			setState(602);
			match(COMMA);
			setState(605);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LB_BRACKET:
				{
				setState(603);
				((FindOneAndUpdateContext)_localctx).objUpdate = obj();
				}
				break;
			case LM_BRACKET:
				{
				setState(604);
				((FindOneAndUpdateContext)_localctx).arrUpdate = arr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(609);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(607);
				match(COMMA);
				setState(608);
				((FindOneAndUpdateContext)_localctx).options = obj();
				}
			}

			setState(611);
			match(RS_BRACKET);
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

	public static class InsertContext extends ParserRuleContext {
		public ObjContext document;
		public ArrContext documents;
		public ObjContext option;
		public TerminalNode INSERT() { return getToken(MongoParser.INSERT, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public InsertContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterInsert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitInsert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitInsert(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertContext insert() throws RecognitionException {
		InsertContext _localctx = new InsertContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_insert);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(613);
			match(INSERT);
			setState(614);
			match(LS_BRACKET);
			setState(617);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LB_BRACKET:
				{
				setState(615);
				((InsertContext)_localctx).document = obj();
				}
				break;
			case LM_BRACKET:
				{
				setState(616);
				((InsertContext)_localctx).documents = arr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(619);
				match(COMMA);
				setState(620);
				((InsertContext)_localctx).option = obj();
				}
			}

			setState(623);
			match(RS_BRACKET);
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

	public static class InsertOneContext extends ParserRuleContext {
		public ObjContext document;
		public ObjContext option;
		public TerminalNode INSERT_ONE() { return getToken(MongoParser.INSERT_ONE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public InsertOneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertOne; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterInsertOne(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitInsertOne(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitInsertOne(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertOneContext insertOne() throws RecognitionException {
		InsertOneContext _localctx = new InsertOneContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_insertOne);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(625);
			match(INSERT_ONE);
			setState(626);
			match(LS_BRACKET);
			setState(627);
			((InsertOneContext)_localctx).document = obj();
			setState(630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(628);
				match(COMMA);
				setState(629);
				((InsertOneContext)_localctx).option = obj();
				}
			}

			setState(632);
			match(RS_BRACKET);
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

	public static class InsertManyContext extends ParserRuleContext {
		public ArrContext documents;
		public ObjContext option;
		public TerminalNode INSERT_MANY() { return getToken(MongoParser.INSERT_MANY, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public InsertManyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertMany; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterInsertMany(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitInsertMany(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitInsertMany(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertManyContext insertMany() throws RecognitionException {
		InsertManyContext _localctx = new InsertManyContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_insertMany);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(634);
			match(INSERT_MANY);
			setState(635);
			match(LS_BRACKET);
			setState(636);
			((InsertManyContext)_localctx).documents = arr();
			setState(639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(637);
				match(COMMA);
				setState(638);
				((InsertManyContext)_localctx).option = obj();
				}
			}

			setState(641);
			match(RS_BRACKET);
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

	public static class DeleteOneContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext option;
		public TerminalNode DELETE_ONE() { return getToken(MongoParser.DELETE_ONE, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public DeleteOneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteOne; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDeleteOne(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDeleteOne(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDeleteOne(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteOneContext deleteOne() throws RecognitionException {
		DeleteOneContext _localctx = new DeleteOneContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_deleteOne);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			match(DELETE_ONE);
			setState(644);
			match(LS_BRACKET);
			setState(646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(645);
				((DeleteOneContext)_localctx).filter = obj();
				}
			}

			setState(650);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(648);
				match(COMMA);
				setState(649);
				((DeleteOneContext)_localctx).option = obj();
				}
			}

			setState(652);
			match(RS_BRACKET);
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

	public static class DeleteManyContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext option;
		public TerminalNode DELETE_MANY() { return getToken(MongoParser.DELETE_MANY, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(MongoParser.COMMA, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public DeleteManyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteMany; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDeleteMany(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDeleteMany(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDeleteMany(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteManyContext deleteMany() throws RecognitionException {
		DeleteManyContext _localctx = new DeleteManyContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_deleteMany);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(654);
			match(DELETE_MANY);
			setState(655);
			match(LS_BRACKET);
			setState(657);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(656);
				((DeleteManyContext)_localctx).filter = obj();
				}
			}

			setState(661);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(659);
				match(COMMA);
				setState(660);
				((DeleteManyContext)_localctx).option = obj();
				}
			}

			setState(663);
			match(RS_BRACKET);
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

	public static class UpdateContext extends ParserRuleContext {
		public ObjContext filter;
		public ObjContext objUpdate;
		public ArrContext arrUpdate;
		public ObjContext option;
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode UPDATE_ONE() { return getToken(MongoParser.UPDATE_ONE, 0); }
		public TerminalNode UPDATE_MANY() { return getToken(MongoParser.UPDATE_MANY, 0); }
		public TerminalNode UPDATE() { return getToken(MongoParser.UPDATE, 0); }
		public List<ObjContext> obj() {
			return getRuleContexts(ObjContext.class);
		}
		public ObjContext obj(int i) {
			return getRuleContext(ObjContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_update);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(665);
			_la = _input.LA(1);
			if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (UPDATE_ONE - 66)) | (1L << (UPDATE_MANY - 66)) | (1L << (UPDATE - 66)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(666);
			match(LS_BRACKET);
			setState(667);
			((UpdateContext)_localctx).filter = obj();
			{
			setState(668);
			match(COMMA);
			setState(671);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LB_BRACKET:
				{
				setState(669);
				((UpdateContext)_localctx).objUpdate = obj();
				}
				break;
			case LM_BRACKET:
				{
				setState(670);
				((UpdateContext)_localctx).arrUpdate = arr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
			setState(675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(673);
				match(COMMA);
				setState(674);
				((UpdateContext)_localctx).option = obj();
				}
			}

			setState(677);
			match(RS_BRACKET);
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

	public static class DropContext extends ParserRuleContext {
		public ObjContext option;
		public TerminalNode DROP() { return getToken(MongoParser.DROP, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public DropContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterDrop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitDrop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitDrop(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DropContext drop() throws RecognitionException {
		DropContext _localctx = new DropContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_drop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(679);
			match(DROP);
			setState(680);
			match(LS_BRACKET);
			setState(682);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LB_BRACKET) {
				{
				setState(681);
				((DropContext)_localctx).option = obj();
				}
			}

			setState(684);
			match(RS_BRACKET);
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

	public static class CollectionNameContext extends ParserRuleContext {
		public TerminalNode DB() { return getToken(MongoParser.DB, 0); }
		public TerminalNode DOT() { return getToken(MongoParser.DOT, 0); }
		public TerminalNode ID() { return getToken(MongoParser.ID, 0); }
		public TerminalNode GET_COLLECTION() { return getToken(MongoParser.GET_COLLECTION, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode LM_BRACKET() { return getToken(MongoParser.LM_BRACKET, 0); }
		public TerminalNode RM_BRACKET() { return getToken(MongoParser.RM_BRACKET, 0); }
		public CollectionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterCollectionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitCollectionName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitCollectionName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionNameContext collectionName() throws RecognitionException {
		CollectionNameContext _localctx = new CollectionNameContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_collectionName);
		try {
			setState(699);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(686);
				match(DB);
				setState(687);
				match(DOT);
				setState(688);
				match(ID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(689);
				match(DB);
				setState(690);
				match(DOT);
				setState(691);
				match(GET_COLLECTION);
				setState(692);
				match(LS_BRACKET);
				setState(693);
				match(STRING);
				setState(694);
				match(RS_BRACKET);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(695);
				match(DB);
				setState(696);
				match(LM_BRACKET);
				setState(697);
				match(STRING);
				setState(698);
				match(RM_BRACKET);
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

	public static class JsonStringContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public JsonStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterJsonString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitJsonString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitJsonString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonStringContext jsonString() throws RecognitionException {
		JsonStringContext _localctx = new JsonStringContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_jsonString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			value();
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

	public static class ObjContext extends ParserRuleContext {
		public TerminalNode LB_BRACKET() { return getToken(MongoParser.LB_BRACKET, 0); }
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public TerminalNode RB_BRACKET() { return getToken(MongoParser.RB_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ObjContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_obj; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterObj(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitObj(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitObj(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjContext obj() throws RecognitionException {
		ObjContext _localctx = new ObjContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_obj);
		int _la;
		try {
			setState(716);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(703);
				match(LB_BRACKET);
				setState(704);
				pair();
				setState(709);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(705);
					match(COMMA);
					setState(706);
					pair();
					}
					}
					setState(711);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(712);
				match(RB_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(714);
				match(LB_BRACKET);
				setState(715);
				match(RB_BRACKET);
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

	public static class PairContext extends ParserRuleContext {
		public KeyContext key() {
			return getRuleContext(KeyContext.class,0);
		}
		public TerminalNode COLON() { return getToken(MongoParser.COLON, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(718);
			key();
			setState(719);
			match(COLON);
			setState(720);
			value();
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

	public static class KeyContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MongoParser.ID, 0); }
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public KeyWordIdContext keyWordId() {
			return getRuleContext(KeyWordIdContext.class,0);
		}
		public KeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_key; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitKey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyContext key() throws RecognitionException {
		KeyContext _localctx = new KeyContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_key);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(725);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(722);
				match(ID);
				}
				break;
			case STRING:
				{
				setState(723);
				match(STRING);
				}
				break;
			case SERVER_BUILD_INFO:
			case HELLO:
			case SERVER_STATUS:
			case DROP_DATABASE:
			case AGGREGATE:
			case CURRENT_OP:
			case KILL_OP:
			case COUNT_DOCUMENTS:
			case CREATE_INDEXES:
			case CREATE_INDEX:
			case DISTINCT:
			case DROP_INDEXES:
			case DROP_INDEX:
			case COUNT:
			case ESTIMATED_DOCUMENT_COUNT:
			case GET_INDEXES:
			case LATENCY_STATS:
			case RENAME_COLLECTION:
			case REPLACE_ONE:
			case VALIDATE:
			case EXPLAIN:
			case HIDE_INDEX:
			case UNHIDE_INDEX:
			case FIND:
			case S_SKIP:
			case LIMIT:
			case SORT:
			case S_COMMENT:
			case MAX_TIME_MS:
			case COLLATION:
			case LET:
			case MAX:
			case MIN:
			case RETURN_KEY:
			case SHOW_RECORD_ID:
			case ALLOW_DISK_USE:
			case FIND_ONE:
			case FIND_ONE_AND_DELETE:
			case FIND_ONE_AND_REPLACE:
			case FIND_ONE_AND_UPDATE:
			case INSERT:
			case INSERT_ONE:
			case INSERT_MANY:
			case DELETE_ONE:
			case DELETE_MANY:
			case UPDATE_ONE:
			case UPDATE_MANY:
			case UPDATE:
			case DROP:
			case HOST_INFO:
				{
				setState(724);
				keyWordId();
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

	public static class ArrContext extends ParserRuleContext {
		public TerminalNode LM_BRACKET() { return getToken(MongoParser.LM_BRACKET, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode RM_BRACKET() { return getToken(MongoParser.RM_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ArrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterArr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitArr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitArr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrContext arr() throws RecognitionException {
		ArrContext _localctx = new ArrContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_arr);
		int _la;
		try {
			setState(740);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(727);
				match(LM_BRACKET);
				setState(728);
				value();
				setState(733);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(729);
					match(COMMA);
					setState(730);
					value();
					}
					}
					setState(735);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(736);
				match(RM_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(738);
				match(LM_BRACKET);
				setState(739);
				match(RM_BRACKET);
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

	public static class ValueContext extends ParserRuleContext {
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MongoParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(MongoParser.NUMBER, 0); }
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public TerminalNode TRUE() { return getToken(MongoParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MongoParser.FALSE, 0); }
		public TerminalNode NULL() { return getToken(MongoParser.NULL, 0); }
		public TerminalNode ID() { return getToken(MongoParser.ID, 0); }
		public TerminalNode LS_BRACKET() { return getToken(MongoParser.LS_BRACKET, 0); }
		public TerminalNode RS_BRACKET() { return getToken(MongoParser.RS_BRACKET, 0); }
		public TerminalNode NEW() { return getToken(MongoParser.NEW, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MongoParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MongoParser.COMMA, i);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_value);
		int _la;
		try {
			setState(767);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LB_BRACKET:
				enterOuterAlt(_localctx, 1);
				{
				setState(742);
				obj();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(743);
				match(STRING);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 3);
				{
				setState(744);
				match(NUMBER);
				}
				break;
			case LM_BRACKET:
				enterOuterAlt(_localctx, 4);
				{
				setState(745);
				arr();
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 5);
				{
				setState(746);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 6);
				{
				setState(747);
				match(FALSE);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 7);
				{
				setState(748);
				match(NULL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 8);
				{
				setState(749);
				match(ID);
				setState(750);
				match(LS_BRACKET);
				setState(751);
				_la = _input.LA(1);
				if ( !(_la==STRING || _la==NUMBER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(752);
				match(RS_BRACKET);
				}
				break;
			case NEW:
				enterOuterAlt(_localctx, 9);
				{
				setState(753);
				match(NEW);
				setState(754);
				match(ID);
				setState(755);
				match(LS_BRACKET);
				setState(764);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (NEW - 94)) | (1L << (TRUE - 94)) | (1L << (FALSE - 94)) | (1L << (NULL - 94)) | (1L << (STRING - 94)) | (1L << (LM_BRACKET - 94)) | (1L << (LB_BRACKET - 94)) | (1L << (ID - 94)) | (1L << (NUMBER - 94)))) != 0)) {
					{
					setState(756);
					value();
					setState(761);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(757);
						match(COMMA);
						setState(758);
						value();
						}
						}
						setState(763);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(766);
				match(RS_BRACKET);
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

	public static class KeyWordIdContext extends ParserRuleContext {
		public TerminalNode MAX_TIME_MS() { return getToken(MongoParser.MAX_TIME_MS, 0); }
		public TerminalNode S_COMMENT() { return getToken(MongoParser.S_COMMENT, 0); }
		public TerminalNode LET() { return getToken(MongoParser.LET, 0); }
		public TerminalNode COLLATION() { return getToken(MongoParser.COLLATION, 0); }
		public TerminalNode S_SKIP() { return getToken(MongoParser.S_SKIP, 0); }
		public TerminalNode LIMIT() { return getToken(MongoParser.LIMIT, 0); }
		public TerminalNode SORT() { return getToken(MongoParser.SORT, 0); }
		public TerminalNode MAX() { return getToken(MongoParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(MongoParser.MIN, 0); }
		public TerminalNode ALLOW_DISK_USE() { return getToken(MongoParser.ALLOW_DISK_USE, 0); }
		public TerminalNode SHOW_RECORD_ID() { return getToken(MongoParser.SHOW_RECORD_ID, 0); }
		public TerminalNode RETURN_KEY() { return getToken(MongoParser.RETURN_KEY, 0); }
		public TerminalNode COUNT() { return getToken(MongoParser.COUNT, 0); }
		public TerminalNode FIND() { return getToken(MongoParser.FIND, 0); }
		public TerminalNode LATENCY_STATS() { return getToken(MongoParser.LATENCY_STATS, 0); }
		public TerminalNode HELLO() { return getToken(MongoParser.HELLO, 0); }
		public TerminalNode SERVER_STATUS() { return getToken(MongoParser.SERVER_STATUS, 0); }
		public TerminalNode SERVER_BUILD_INFO() { return getToken(MongoParser.SERVER_BUILD_INFO, 0); }
		public TerminalNode GET_INDEXES() { return getToken(MongoParser.GET_INDEXES, 0); }
		public TerminalNode HIDE_INDEX() { return getToken(MongoParser.HIDE_INDEX, 0); }
		public TerminalNode UNHIDE_INDEX() { return getToken(MongoParser.UNHIDE_INDEX, 0); }
		public TerminalNode ESTIMATED_DOCUMENT_COUNT() { return getToken(MongoParser.ESTIMATED_DOCUMENT_COUNT, 0); }
		public TerminalNode COUNT_DOCUMENTS() { return getToken(MongoParser.COUNT_DOCUMENTS, 0); }
		public TerminalNode CREATE_INDEX() { return getToken(MongoParser.CREATE_INDEX, 0); }
		public TerminalNode CREATE_INDEXES() { return getToken(MongoParser.CREATE_INDEXES, 0); }
		public TerminalNode DISTINCT() { return getToken(MongoParser.DISTINCT, 0); }
		public TerminalNode DROP_INDEX() { return getToken(MongoParser.DROP_INDEX, 0); }
		public TerminalNode DROP_INDEXES() { return getToken(MongoParser.DROP_INDEXES, 0); }
		public TerminalNode AGGREGATE() { return getToken(MongoParser.AGGREGATE, 0); }
		public TerminalNode FIND_ONE() { return getToken(MongoParser.FIND_ONE, 0); }
		public TerminalNode FIND_ONE_AND_DELETE() { return getToken(MongoParser.FIND_ONE_AND_DELETE, 0); }
		public TerminalNode FIND_ONE_AND_REPLACE() { return getToken(MongoParser.FIND_ONE_AND_REPLACE, 0); }
		public TerminalNode FIND_ONE_AND_UPDATE() { return getToken(MongoParser.FIND_ONE_AND_UPDATE, 0); }
		public TerminalNode INSERT() { return getToken(MongoParser.INSERT, 0); }
		public TerminalNode INSERT_ONE() { return getToken(MongoParser.INSERT_ONE, 0); }
		public TerminalNode INSERT_MANY() { return getToken(MongoParser.INSERT_MANY, 0); }
		public TerminalNode DELETE_ONE() { return getToken(MongoParser.DELETE_ONE, 0); }
		public TerminalNode DELETE_MANY() { return getToken(MongoParser.DELETE_MANY, 0); }
		public TerminalNode UPDATE() { return getToken(MongoParser.UPDATE, 0); }
		public TerminalNode UPDATE_ONE() { return getToken(MongoParser.UPDATE_ONE, 0); }
		public TerminalNode UPDATE_MANY() { return getToken(MongoParser.UPDATE_MANY, 0); }
		public TerminalNode DROP() { return getToken(MongoParser.DROP, 0); }
		public TerminalNode RENAME_COLLECTION() { return getToken(MongoParser.RENAME_COLLECTION, 0); }
		public TerminalNode REPLACE_ONE() { return getToken(MongoParser.REPLACE_ONE, 0); }
		public TerminalNode VALIDATE() { return getToken(MongoParser.VALIDATE, 0); }
		public TerminalNode HOST_INFO() { return getToken(MongoParser.HOST_INFO, 0); }
		public TerminalNode KILL_OP() { return getToken(MongoParser.KILL_OP, 0); }
		public TerminalNode CURRENT_OP() { return getToken(MongoParser.CURRENT_OP, 0); }
		public TerminalNode DROP_DATABASE() { return getToken(MongoParser.DROP_DATABASE, 0); }
		public TerminalNode EXPLAIN() { return getToken(MongoParser.EXPLAIN, 0); }
		public KeyWordIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).enterKeyWordId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MongoParserListener ) ((MongoParserListener)listener).exitKeyWordId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MongoParserVisitor ) return ((MongoParserVisitor<? extends T>)visitor).visitKeyWordId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordIdContext keyWordId() throws RecognitionException {
		KeyWordIdContext _localctx = new KeyWordIdContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_keyWordId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SERVER_BUILD_INFO) | (1L << HELLO) | (1L << SERVER_STATUS) | (1L << DROP_DATABASE) | (1L << AGGREGATE) | (1L << CURRENT_OP) | (1L << KILL_OP) | (1L << COUNT_DOCUMENTS) | (1L << CREATE_INDEXES) | (1L << CREATE_INDEX) | (1L << DISTINCT) | (1L << DROP_INDEXES) | (1L << DROP_INDEX) | (1L << COUNT) | (1L << ESTIMATED_DOCUMENT_COUNT) | (1L << GET_INDEXES) | (1L << LATENCY_STATS) | (1L << RENAME_COLLECTION) | (1L << REPLACE_ONE) | (1L << VALIDATE) | (1L << EXPLAIN) | (1L << HIDE_INDEX) | (1L << UNHIDE_INDEX) | (1L << FIND) | (1L << S_SKIP) | (1L << LIMIT) | (1L << SORT) | (1L << S_COMMENT) | (1L << MAX_TIME_MS) | (1L << COLLATION) | (1L << LET) | (1L << MAX) | (1L << MIN) | (1L << RETURN_KEY) | (1L << SHOW_RECORD_ID) | (1L << ALLOW_DISK_USE) | (1L << FIND_ONE) | (1L << FIND_ONE_AND_DELETE) | (1L << FIND_ONE_AND_REPLACE) | (1L << FIND_ONE_AND_UPDATE) | (1L << INSERT) | (1L << INSERT_ONE) | (1L << INSERT_MANY))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (DELETE_ONE - 64)) | (1L << (DELETE_MANY - 64)) | (1L << (UPDATE_ONE - 64)) | (1L << (UPDATE_MANY - 64)) | (1L << (UPDATE - 64)) | (1L << (DROP - 64)) | (1L << (HOST_INFO - 64)))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3r\u0306\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\3\2\3\2\6\2e\n\2\r\2\16\2f"+
		"\3\2\7\2j\n\2\f\2\16\2m\13\2\3\2\7\2p\n\2\f\2\16\2s\13\2\3\2\3\2\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\5\4~\n\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\5\6\u008f\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\5\6\u009d\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00a7"+
		"\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00b1\n\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00c0\n\6\5\6\u00c2\n\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5"+
		"\6\u00e9\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00f2\n\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u0105\n\6\3\7"+
		"\3\7\3\7\3\7\5\7\u010b\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\5\b\u011c\n\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\5\n\u013f\n\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\5"+
		"\f\u0148\n\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\5\r\u0152\n\r\3\16\3\16\3"+
		"\16\5\16\u0157\n\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17"+
		"\u0162\n\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\5\20\u016b\n\20\3\20\3"+
		"\20\3\21\3\21\3\21\5\21\u0172\n\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\5\23\u017e\n\23\3\23\3\23\3\24\3\24\3\24\5\24\u0185\n"+
		"\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\5\25\u018e\n\25\5\25\u0190\n\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\5\26\u0198\n\26\3\26\3\26\3\27\3\27\3\27"+
		"\5\27\u019f\n\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u01aa"+
		"\n\30\5\30\u01ac\n\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31"+
		"\u01b7\n\31\5\31\u01b9\n\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\5\32\u01c4\n\32\5\32\u01c6\n\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33"+
		"\5\33\u01cf\n\33\5\33\u01d1\n\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\5"+
		"\34\u01da\n\34\5\34\u01dc\n\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\5\35\u01e7\n\35\5\35\u01e9\n\35\5\35\u01eb\n\35\3\35\3\35\3\35\7"+
		"\35\u01f0\n\35\f\35\16\35\u01f3\13\35\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\5\36\u022e\n\36\3\36\5\36"+
		"\u0231\n\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u023a\n\37\5\37\u023c"+
		"\n\37\5\37\u023e\n\37\3\37\3\37\3 \3 \3 \3 \3 \5 \u0247\n \5 \u0249\n"+
		" \3 \3 \3!\3!\3!\3!\3!\3!\3!\5!\u0254\n!\5!\u0256\n!\3!\3!\3\"\3\"\3\""+
		"\3\"\3\"\3\"\5\"\u0260\n\"\3\"\3\"\5\"\u0264\n\"\3\"\3\"\3#\3#\3#\3#\5"+
		"#\u026c\n#\3#\3#\5#\u0270\n#\3#\3#\3$\3$\3$\3$\3$\5$\u0279\n$\3$\3$\3"+
		"%\3%\3%\3%\3%\5%\u0282\n%\3%\3%\3&\3&\3&\5&\u0289\n&\3&\3&\5&\u028d\n"+
		"&\3&\3&\3\'\3\'\3\'\5\'\u0294\n\'\3\'\3\'\5\'\u0298\n\'\3\'\3\'\3(\3("+
		"\3(\3(\3(\3(\5(\u02a2\n(\3(\3(\5(\u02a6\n(\3(\3(\3)\3)\3)\5)\u02ad\n)"+
		"\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\5*\u02be\n*\3+\3+\3,\3,"+
		"\3,\3,\7,\u02c6\n,\f,\16,\u02c9\13,\3,\3,\3,\3,\5,\u02cf\n,\3-\3-\3-\3"+
		"-\3.\3.\3.\5.\u02d8\n.\3/\3/\3/\3/\7/\u02de\n/\f/\16/\u02e1\13/\3/\3/"+
		"\3/\3/\5/\u02e7\n/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3"+
		"\60\3\60\3\60\3\60\3\60\3\60\3\60\7\60\u02fa\n\60\f\60\16\60\u02fd\13"+
		"\60\5\60\u02ff\n\60\3\60\5\60\u0302\n\60\3\61\3\61\3\61\2\2\62\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVX"+
		"Z\\^`\2\b\3\2\b\t\3\2ab\3\2)*\4\2ddrr\3\2DF\b\2\f\17\25%)/\61\67:GSS\2"+
		"\u0363\2b\3\2\2\2\4v\3\2\2\2\6}\3\2\2\2\b\177\3\2\2\2\n\u0104\3\2\2\2"+
		"\f\u010a\3\2\2\2\16\u011b\3\2\2\2\20\u011d\3\2\2\2\22\u013e\3\2\2\2\24"+
		"\u0140\3\2\2\2\26\u0144\3\2\2\2\30\u0151\3\2\2\2\32\u0153\3\2\2\2\34\u015a"+
		"\3\2\2\2\36\u0165\3\2\2\2 \u016e\3\2\2\2\"\u0175\3\2\2\2$\u0179\3\2\2"+
		"\2&\u0181\3\2\2\2(\u0188\3\2\2\2*\u0193\3\2\2\2,\u019b\3\2\2\2.\u01a2"+
		"\3\2\2\2\60\u01af\3\2\2\2\62\u01bc\3\2\2\2\64\u01c9\3\2\2\2\66\u01d4\3"+
		"\2\2\28\u01df\3\2\2\2:\u0230\3\2\2\2<\u0232\3\2\2\2>\u0241\3\2\2\2@\u024c"+
		"\3\2\2\2B\u0259\3\2\2\2D\u0267\3\2\2\2F\u0273\3\2\2\2H\u027c\3\2\2\2J"+
		"\u0285\3\2\2\2L\u0290\3\2\2\2N\u029b\3\2\2\2P\u02a9\3\2\2\2R\u02bd\3\2"+
		"\2\2T\u02bf\3\2\2\2V\u02ce\3\2\2\2X\u02d0\3\2\2\2Z\u02d7\3\2\2\2\\\u02e6"+
		"\3\2\2\2^\u0301\3\2\2\2`\u0303\3\2\2\2bk\5\4\3\2ce\7p\2\2dc\3\2\2\2ef"+
		"\3\2\2\2fd\3\2\2\2fg\3\2\2\2gh\3\2\2\2hj\5\4\3\2id\3\2\2\2jm\3\2\2\2k"+
		"i\3\2\2\2kl\3\2\2\2lq\3\2\2\2mk\3\2\2\2np\7p\2\2on\3\2\2\2ps\3\2\2\2q"+
		"o\3\2\2\2qr\3\2\2\2rt\3\2\2\2sq\3\2\2\2tu\7\2\2\3u\3\3\2\2\2vw\5\6\4\2"+
		"w\5\3\2\2\2x~\5\n\6\2y~\5\20\t\2z~\5\16\b\2{~\5\f\7\2|~\5\b\5\2}x\3\2"+
		"\2\2}y\3\2\2\2}z\3\2\2\2}{\3\2\2\2}|\3\2\2\2~\7\3\2\2\2\177\u0080\7\13"+
		"\2\2\u0080\u0081\7q\2\2\u0081\t\3\2\2\2\u0082\u0083\7\6\2\2\u0083\u0084"+
		"\7e\2\2\u0084\u0085\7\17\2\2\u0085\u0086\7f\2\2\u0086\u0105\7g\2\2\u0087"+
		"\u0088\7\6\2\2\u0088\u0089\7e\2\2\u0089\u008a\7\20\2\2\u008a\u008b\7f"+
		"\2\2\u008b\u008e\7d\2\2\u008c\u008d\7l\2\2\u008d\u008f\5V,\2\u008e\u008c"+
		"\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0105\7g\2\2\u0091"+
		"\u0092\7\6\2\2\u0092\u0093\7e\2\2\u0093\u0094\7_\2\2\u0094\u0095\7f\2"+
		"\2\u0095\u0096\7d\2\2\u0096\u0097\7l\2\2\u0097\u0098\7d\2\2\u0098\u0099"+
		"\7l\2\2\u0099\u009c\5\\/\2\u009a\u009b\7l\2\2\u009b\u009d\5V,\2\u009c"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\7g"+
		"\2\2\u009f\u0105\3\2\2\2\u00a0\u00a1\7\6\2\2\u00a1\u00a2\7e\2\2\u00a2"+
		"\u00a3\7\21\2\2\u00a3\u00a6\7f\2\2\u00a4\u00a7\5V,\2\u00a5\u00a7\7r\2"+
		"\2\u00a6\u00a4\3\2\2\2\u00a6\u00a5\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8"+
		"\3\2\2\2\u00a8\u0105\7g\2\2\u00a9\u00aa\7\6\2\2\u00aa\u00ab\7e\2\2\u00ab"+
		"\u00ac\7\22\2\2\u00ac\u00ad\7f\2\2\u00ad\u00b0\7r\2\2\u00ae\u00af\7l\2"+
		"\2\u00af\u00b1\5V,\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2"+
		"\3\2\2\2\u00b2\u0105\7g\2\2\u00b3\u00b4\7\6\2\2\u00b4\u00b5\7e\2\2\u00b5"+
		"\u00b6\7[\2\2\u00b6\u00b7\7f\2\2\u00b7\u0105\7g\2\2\u00b8\u00b9\7\6\2"+
		"\2\u00b9\u00ba\7e\2\2\u00ba\u00bb\7\\\2\2\u00bb\u00c1\7f\2\2\u00bc\u00bf"+
		"\5V,\2\u00bd\u00be\7l\2\2\u00be\u00c0\5V,\2\u00bf\u00bd\3\2\2\2\u00bf"+
		"\u00c0\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1\u00bc\3\2\2\2\u00c1\u00c2\3\2"+
		"\2\2\u00c2\u00c3\3\2\2\2\u00c3\u0105\7g\2\2\u00c4\u00c5\7\6\2\2\u00c5"+
		"\u00c6\7e\2\2\u00c6\u00c7\7Z\2\2\u00c7\u00c8\7f\2\2\u00c8\u0105\7g\2\2"+
		"\u00c9\u00ca\7\6\2\2\u00ca\u00cb\7e\2\2\u00cb\u00cc\7W\2\2\u00cc\u00cd"+
		"\7f\2\2\u00cd\u0105\7g\2\2\u00ce\u00cf\7\6\2\2\u00cf\u00d0\7e\2\2\u00d0"+
		"\u00d1\7S\2\2\u00d1\u00d2\7f\2\2\u00d2\u0105\7g\2\2\u00d3\u00d4\7\6\2"+
		"\2\u00d4\u00d5\7e\2\2\u00d5\u00d6\7\23\2\2\u00d6\u00d7\7f\2\2\u00d7\u00d8"+
		"\5V,\2\u00d8\u00d9\7g\2\2\u00d9\u0105\3\2\2\2\u00da\u00db\7\6\2\2\u00db"+
		"\u00dc\7e\2\2\u00dc\u00dd\7(\2\2\u00dd\u00de\7f\2\2\u00de\u00df\5V,\2"+
		"\u00df\u00e0\7g\2\2\u00e0\u0105\3\2\2\2\u00e1\u00e2\7\6\2\2\u00e2\u00e3"+
		"\7e\2\2\u00e3\u00e4\7\25\2\2\u00e4\u00e5\7f\2\2\u00e5\u00e8\5\\/\2\u00e6"+
		"\u00e7\7l\2\2\u00e7\u00e9\5V,\2\u00e8\u00e6\3\2\2\2\u00e8\u00e9\3\2\2"+
		"\2\u00e9\u00ea\3\2\2\2\u00ea\u00eb\7g\2\2\u00eb\u0105\3\2\2\2\u00ec\u00ed"+
		"\7\6\2\2\u00ed\u00ee\7e\2\2\u00ee\u00ef\7\26\2\2\u00ef\u00f1\7f\2\2\u00f0"+
		"\u00f2\5V,\2\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\3\2\2"+
		"\2\u00f3\u0105\7g\2\2\u00f4\u00f5\7\6\2\2\u00f5\u00f6\7e\2\2\u00f6\u00f7"+
		"\7\27\2\2\u00f7\u00f8\7f\2\2\u00f8\u00f9\7r\2\2\u00f9\u0105\7g\2\2\u00fa"+
		"\u00fb\7\6\2\2\u00fb\u00fc\7e\2\2\u00fc\u00fd\7^\2\2\u00fd\u00fe\7f\2"+
		"\2\u00fe\u0105\7g\2\2\u00ff\u0100\7\6\2\2\u0100\u0101\7e\2\2\u0101\u0102"+
		"\7]\2\2\u0102\u0103\7f\2\2\u0103\u0105\7g\2\2\u0104\u0082\3\2\2\2\u0104"+
		"\u0087\3\2\2\2\u0104\u0091\3\2\2\2\u0104\u00a0\3\2\2\2\u0104\u00a9\3\2"+
		"\2\2\u0104\u00b3\3\2\2\2\u0104\u00b8\3\2\2\2\u0104\u00c4\3\2\2\2\u0104"+
		"\u00c9\3\2\2\2\u0104\u00ce\3\2\2\2\u0104\u00d3\3\2\2\2\u0104\u00da\3\2"+
		"\2\2\u0104\u00e1\3\2\2\2\u0104\u00ec\3\2\2\2\u0104\u00f4\3\2\2\2\u0104"+
		"\u00fa\3\2\2\2\u0104\u00ff\3\2\2\2\u0105\13\3\2\2\2\u0106\u0107\7\7\2"+
		"\2\u0107\u010b\t\2\2\2\u0108\u0109\7\7\2\2\u0109\u010b\7\n\2\2\u010a\u0106"+
		"\3\2\2\2\u010a\u0108\3\2\2\2\u010b\r\3\2\2\2\u010c\u010d\7\6\2\2\u010d"+
		"\u010e\7e\2\2\u010e\u010f\7\f\2\2\u010f\u0110\7f\2\2\u0110\u011c\7g\2"+
		"\2\u0111\u0112\7\6\2\2\u0112\u0113\7e\2\2\u0113\u0114\7\r\2\2\u0114\u0115"+
		"\7f\2\2\u0115\u011c\7g\2\2\u0116\u0117\7\6\2\2\u0117\u0118\7e\2\2\u0118"+
		"\u0119\7\16\2\2\u0119\u011a\7f\2\2\u011a\u011c\7g\2\2\u011b\u010c\3\2"+
		"\2\2\u011b\u0111\3\2\2\2\u011b\u0116\3\2\2\2\u011c\17\3\2\2\2\u011d\u011e"+
		"\5R*\2\u011e\u011f\7e\2\2\u011f\u0120\5\22\n\2\u0120\21\3\2\2\2\u0121"+
		"\u013f\5D#\2\u0122\u013f\5F$\2\u0123\u013f\5H%\2\u0124\u013f\58\35\2\u0125"+
		"\u013f\5<\37\2\u0126\u013f\5> \2\u0127\u013f\5@!\2\u0128\u013f\5B\"\2"+
		"\u0129\u013f\5J&\2\u012a\u013f\5L\'\2\u012b\u013f\5N(\2\u012c\u013f\5"+
		"P)\2\u012d\u013f\5\64\33\2\u012e\u013f\5\66\34\2\u012f\u013f\5\60\31\2"+
		"\u0130\u013f\5\62\32\2\u0131\u013f\5.\30\2\u0132\u013f\5*\26\2\u0133\u013f"+
		"\5,\27\2\u0134\u013f\5(\25\2\u0135\u013f\5&\24\2\u0136\u013f\5\"\22\2"+
		"\u0137\u013f\5$\23\2\u0138\u013f\5 \21\2\u0139\u013f\5\36\20\2\u013a\u013f"+
		"\5\34\17\2\u013b\u013f\5\32\16\2\u013c\u013f\5\26\f\2\u013d\u013f\5\24"+
		"\13\2\u013e\u0121\3\2\2\2\u013e\u0122\3\2\2\2\u013e\u0123\3\2\2\2\u013e"+
		"\u0124\3\2\2\2\u013e\u0125\3\2\2\2\u013e\u0126\3\2\2\2\u013e\u0127\3\2"+
		"\2\2\u013e\u0128\3\2\2\2\u013e\u0129\3\2\2\2\u013e\u012a\3\2\2\2\u013e"+
		"\u012b\3\2\2\2\u013e\u012c\3\2\2\2\u013e\u012d\3\2\2\2\u013e\u012e\3\2"+
		"\2\2\u013e\u012f\3\2\2\2\u013e\u0130\3\2\2\2\u013e\u0131\3\2\2\2\u013e"+
		"\u0132\3\2\2\2\u013e\u0133\3\2\2\2\u013e\u0134\3\2\2\2\u013e\u0135\3\2"+
		"\2\2\u013e\u0136\3\2\2\2\u013e\u0137\3\2\2\2\u013e\u0138\3\2\2\2\u013e"+
		"\u0139\3\2\2\2\u013e\u013a\3\2\2\2\u013e\u013b\3\2\2\2\u013e\u013c\3\2"+
		"\2\2\u013e\u013d\3\2\2\2\u013f\23\3\2\2\2\u0140\u0141\7\'\2\2\u0141\u0142"+
		"\7f\2\2\u0142\u0143\7g\2\2\u0143\25\3\2\2\2\u0144\u0145\7%\2\2\u0145\u0147"+
		"\7f\2\2\u0146\u0148\7d\2\2\u0147\u0146\3\2\2\2\u0147\u0148\3\2\2\2\u0148"+
		"\u0149\3\2\2\2\u0149\u014a\7g\2\2\u014a\u014b\7e\2\2\u014b\u014c\5\30"+
		"\r\2\u014c\27\3\2\2\2\u014d\u0152\5\64\33\2\u014e\u0152\58\35\2\u014f"+
		"\u0152\5(\25\2\u0150\u0152\5.\30\2\u0151\u014d\3\2\2\2\u0151\u014e\3\2"+
		"\2\2\u0151\u014f\3\2\2\2\u0151\u0150\3\2\2\2\u0152\31\3\2\2\2\u0153\u0154"+
		"\7$\2\2\u0154\u0156\7f\2\2\u0155\u0157\5V,\2\u0156\u0155\3\2\2\2\u0156"+
		"\u0157\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u0159\7g\2\2\u0159\33\3\2\2\2"+
		"\u015a\u015b\7#\2\2\u015b\u015c\7f\2\2\u015c\u015d\5V,\2\u015d\u015e\7"+
		"l\2\2\u015e\u0161\5V,\2\u015f\u0160\7l\2\2\u0160\u0162\5V,\2\u0161\u015f"+
		"\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0164\7g\2\2\u0164"+
		"\35\3\2\2\2\u0165\u0166\7\"\2\2\u0166\u0167\7f\2\2\u0167\u016a\7d\2\2"+
		"\u0168\u0169\7l\2\2\u0169\u016b\t\3\2\2\u016a\u0168\3\2\2\2\u016a\u016b"+
		"\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016d\7g\2\2\u016d\37\3\2\2\2\u016e"+
		"\u016f\7!\2\2\u016f\u0171\7f\2\2\u0170\u0172\5V,\2\u0171\u0170\3\2\2\2"+
		"\u0171\u0172\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0174\7g\2\2\u0174!\3\2"+
		"\2\2\u0175\u0176\7 \2\2\u0176\u0177\7f\2\2\u0177\u0178\7g\2\2\u0178#\3"+
		"\2\2\2\u0179\u017a\t\4\2\2\u017a\u017d\7f\2\2\u017b\u017e\5V,\2\u017c"+
		"\u017e\7d\2\2\u017d\u017b\3\2\2\2\u017d\u017c\3\2\2\2\u017e\u017f\3\2"+
		"\2\2\u017f\u0180\7g\2\2\u0180%\3\2\2\2\u0181\u0182\7\37\2\2\u0182\u0184"+
		"\7f\2\2\u0183\u0185\5V,\2\u0184\u0183\3\2\2\2\u0184\u0185\3\2\2\2\u0185"+
		"\u0186\3\2\2\2\u0186\u0187\7g\2\2\u0187\'\3\2\2\2\u0188\u0189\7\36\2\2"+
		"\u0189\u018f\7f\2\2\u018a\u018d\5V,\2\u018b\u018c\7l\2\2\u018c\u018e\5"+
		"V,\2\u018d\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018e\u0190\3\2\2\2\u018f"+
		"\u018a\3\2\2\2\u018f\u0190\3\2\2\2\u0190\u0191\3\2\2\2\u0191\u0192\7g"+
		"\2\2\u0192)\3\2\2\2\u0193\u0194\7\35\2\2\u0194\u0197\7f\2\2\u0195\u0198"+
		"\5V,\2\u0196\u0198\7d\2\2\u0197\u0195\3\2\2\2\u0197\u0196\3\2\2\2\u0198"+
		"\u0199\3\2\2\2\u0199\u019a\7g\2\2\u019a+\3\2\2\2\u019b\u019c\7\34\2\2"+
		"\u019c\u019e\7f\2\2\u019d\u019f\5T+\2\u019e\u019d\3\2\2\2\u019e\u019f"+
		"\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u01a1\7g\2\2\u01a1-\3\2\2\2\u01a2\u01a3"+
		"\7\33\2\2\u01a3\u01a4\7f\2\2\u01a4\u01ab\7d\2\2\u01a5\u01a6\7l\2\2\u01a6"+
		"\u01a9\5V,\2\u01a7\u01a8\7l\2\2\u01a8\u01aa\5V,\2\u01a9\u01a7\3\2\2\2"+
		"\u01a9\u01aa\3\2\2\2\u01aa\u01ac\3\2\2\2\u01ab\u01a5\3\2\2\2\u01ab\u01ac"+
		"\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01ae\7g\2\2\u01ae/\3\2\2\2\u01af\u01b0"+
		"\7\32\2\2\u01b0\u01b1\7f\2\2\u01b1\u01b8\5V,\2\u01b2\u01b3\7l\2\2\u01b3"+
		"\u01b6\5V,\2\u01b4\u01b5\7l\2\2\u01b5\u01b7\t\5\2\2\u01b6\u01b4\3\2\2"+
		"\2\u01b6\u01b7\3\2\2\2\u01b7\u01b9\3\2\2\2\u01b8\u01b2\3\2\2\2\u01b8\u01b9"+
		"\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\7g\2\2\u01bb\61\3\2\2\2\u01bc"+
		"\u01bd\7\31\2\2\u01bd\u01be\7f\2\2\u01be\u01c5\5\\/\2\u01bf\u01c0\7l\2"+
		"\2\u01c0\u01c3\5V,\2\u01c1\u01c2\7l\2\2\u01c2\u01c4\t\5\2\2\u01c3\u01c1"+
		"\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c6\3\2\2\2\u01c5\u01bf\3\2\2\2\u01c5"+
		"\u01c6\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c8\7g\2\2\u01c8\63\3\2\2\2"+
		"\u01c9\u01ca\7\25\2\2\u01ca\u01d0\7f\2\2\u01cb\u01ce\5\\/\2\u01cc\u01cd"+
		"\7l\2\2\u01cd\u01cf\5V,\2\u01ce\u01cc\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf"+
		"\u01d1\3\2\2\2\u01d0\u01cb\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d2\3\2"+
		"\2\2\u01d2\u01d3\7g\2\2\u01d3\65\3\2\2\2\u01d4\u01d5\7\30\2\2\u01d5\u01db"+
		"\7f\2\2\u01d6\u01d9\5V,\2\u01d7\u01d8\7l\2\2\u01d8\u01da\5V,\2\u01d9\u01d7"+
		"\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01dc\3\2\2\2\u01db\u01d6\3\2\2\2\u01db"+
		"\u01dc\3\2\2\2\u01dc\u01dd\3\2\2\2\u01dd\u01de\7g\2\2\u01de\67\3\2\2\2"+
		"\u01df\u01e0\7+\2\2\u01e0\u01ea\7f\2\2\u01e1\u01e8\5T+\2\u01e2\u01e3\7"+
		"l\2\2\u01e3\u01e6\5T+\2\u01e4\u01e5\7l\2\2\u01e5\u01e7\5V,\2\u01e6\u01e4"+
		"\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01e9\3\2\2\2\u01e8\u01e2\3\2\2\2\u01e8"+
		"\u01e9\3\2\2\2\u01e9\u01eb\3\2\2\2\u01ea\u01e1\3\2\2\2\u01ea\u01eb\3\2"+
		"\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01f1\7g\2\2\u01ed\u01ee\7e\2\2\u01ee\u01f0"+
		"\5:\36\2\u01ef\u01ed\3\2\2\2\u01f0\u01f3\3\2\2\2\u01f1\u01ef\3\2\2\2\u01f1"+
		"\u01f2\3\2\2\2\u01f29\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f4\u01f5\7,\2\2\u01f5"+
		"\u01f6\7f\2\2\u01f6\u01f7\7r\2\2\u01f7\u0231\7g\2\2\u01f8\u01f9\7-\2\2"+
		"\u01f9\u01fa\7f\2\2\u01fa\u01fb\7r\2\2\u01fb\u0231\7g\2\2\u01fc\u01fd"+
		"\7.\2\2\u01fd\u01fe\7f\2\2\u01fe\u01ff\5T+\2\u01ff\u0200\7g\2\2\u0200"+
		"\u0231\3\2\2\2\u0201\u0202\7/\2\2\u0202\u0203\7f\2\2\u0203\u0204\5T+\2"+
		"\u0204\u0205\7g\2\2\u0205\u0231\3\2\2\2\u0206\u0207\7\61\2\2\u0207\u0208"+
		"\7f\2\2\u0208\u0209\7r\2\2\u0209\u0231\7g\2\2\u020a\u020b\7\62\2\2\u020b"+
		"\u020c\7f\2\2\u020c\u020d\5T+\2\u020d\u020e\7g\2\2\u020e\u0231\3\2\2\2"+
		"\u020f\u0210\7:\2\2\u0210\u0211\7f\2\2\u0211\u0212\t\3\2\2\u0212\u0231"+
		"\7g\2\2\u0213\u0214\7\64\2\2\u0214\u0215\7f\2\2\u0215\u0216\5T+\2\u0216"+
		"\u0217\7g\2\2\u0217\u0231\3\2\2\2\u0218\u0219\7\65\2\2\u0219\u021a\7f"+
		"\2\2\u021a\u021b\5T+\2\u021b\u021c\7g\2\2\u021c\u0231\3\2\2\2\u021d\u021e"+
		"\7\66\2\2\u021e\u021f\7f\2\2\u021f\u0220\t\3\2\2\u0220\u0231\7g\2\2\u0221"+
		"\u0222\7\67\2\2\u0222\u0223\7f\2\2\u0223\u0224\t\3\2\2\u0224\u0231\7g"+
		"\2\2\u0225\u0226\7\63\2\2\u0226\u0227\7f\2\2\u0227\u0228\5T+\2\u0228\u0229"+
		"\7g\2\2\u0229\u0231\3\2\2\2\u022a\u022b\7%\2\2\u022b\u022d\7f\2\2\u022c"+
		"\u022e\7d\2\2\u022d\u022c\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2"+
		"\2\2\u022f\u0231\7g\2\2\u0230\u01f4\3\2\2\2\u0230\u01f8\3\2\2\2\u0230"+
		"\u01fc\3\2\2\2\u0230\u0201\3\2\2\2\u0230\u0206\3\2\2\2\u0230\u020a\3\2"+
		"\2\2\u0230\u020f\3\2\2\2\u0230\u0213\3\2\2\2\u0230\u0218\3\2\2\2\u0230"+
		"\u021d\3\2\2\2\u0230\u0221\3\2\2\2\u0230\u0225\3\2\2\2\u0230\u022a\3\2"+
		"\2\2\u0231;\3\2\2\2\u0232\u0233\7;\2\2\u0233\u023d\7f\2\2\u0234\u023b"+
		"\5T+\2\u0235\u0236\7l\2\2\u0236\u0239\5T+\2\u0237\u0238\7l\2\2\u0238\u023a"+
		"\5V,\2\u0239\u0237\3\2\2\2\u0239\u023a\3\2\2\2\u023a\u023c\3\2\2\2\u023b"+
		"\u0235\3\2\2\2\u023b\u023c\3\2\2\2\u023c\u023e\3\2\2\2\u023d\u0234\3\2"+
		"\2\2\u023d\u023e\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u0240\7g\2\2\u0240"+
		"=\3\2\2\2\u0241\u0242\7<\2\2\u0242\u0248\7f\2\2\u0243\u0246\5V,\2\u0244"+
		"\u0245\7l\2\2\u0245\u0247\5V,\2\u0246\u0244\3\2\2\2\u0246\u0247\3\2\2"+
		"\2\u0247\u0249\3\2\2\2\u0248\u0243\3\2\2\2\u0248\u0249\3\2\2\2\u0249\u024a"+
		"\3\2\2\2\u024a\u024b\7g\2\2\u024b?\3\2\2\2\u024c\u024d\7=\2\2\u024d\u024e"+
		"\7f\2\2\u024e\u0255\5V,\2\u024f\u0250\7l\2\2\u0250\u0253\5V,\2\u0251\u0252"+
		"\7l\2\2\u0252\u0254\5V,\2\u0253\u0251\3\2\2\2\u0253\u0254\3\2\2\2\u0254"+
		"\u0256\3\2\2\2\u0255\u024f\3\2\2\2\u0255\u0256\3\2\2\2\u0256\u0257\3\2"+
		"\2\2\u0257\u0258\7g\2\2\u0258A\3\2\2\2\u0259\u025a\7>\2\2\u025a\u025b"+
		"\7f\2\2\u025b\u025c\5V,\2\u025c\u025f\7l\2\2\u025d\u0260\5V,\2\u025e\u0260"+
		"\5\\/\2\u025f\u025d\3\2\2\2\u025f\u025e\3\2\2\2\u0260\u0263\3\2\2\2\u0261"+
		"\u0262\7l\2\2\u0262\u0264\5V,\2\u0263\u0261\3\2\2\2\u0263\u0264\3\2\2"+
		"\2\u0264\u0265\3\2\2\2\u0265\u0266\7g\2\2\u0266C\3\2\2\2\u0267\u0268\7"+
		"?\2\2\u0268\u026b\7f\2\2\u0269\u026c\5V,\2\u026a\u026c\5\\/\2\u026b\u0269"+
		"\3\2\2\2\u026b\u026a\3\2\2\2\u026c\u026f\3\2\2\2\u026d\u026e\7l\2\2\u026e"+
		"\u0270\5V,\2\u026f\u026d\3\2\2\2\u026f\u0270\3\2\2\2\u0270\u0271\3\2\2"+
		"\2\u0271\u0272\7g\2\2\u0272E\3\2\2\2\u0273\u0274\7@\2\2\u0274\u0275\7"+
		"f\2\2\u0275\u0278\5V,\2\u0276\u0277\7l\2\2\u0277\u0279\5V,\2\u0278\u0276"+
		"\3\2\2\2\u0278\u0279\3\2\2\2\u0279\u027a\3\2\2\2\u027a\u027b\7g\2\2\u027b"+
		"G\3\2\2\2\u027c\u027d\7A\2\2\u027d\u027e\7f\2\2\u027e\u0281\5\\/\2\u027f"+
		"\u0280\7l\2\2\u0280\u0282\5V,\2\u0281\u027f\3\2\2\2\u0281\u0282\3\2\2"+
		"\2\u0282\u0283\3\2\2\2\u0283\u0284\7g\2\2\u0284I\3\2\2\2\u0285\u0286\7"+
		"B\2\2\u0286\u0288\7f\2\2\u0287\u0289\5V,\2\u0288\u0287\3\2\2\2\u0288\u0289"+
		"\3\2\2\2\u0289\u028c\3\2\2\2\u028a\u028b\7l\2\2\u028b\u028d\5V,\2\u028c"+
		"\u028a\3\2\2\2\u028c\u028d\3\2\2\2\u028d\u028e\3\2\2\2\u028e\u028f\7g"+
		"\2\2\u028fK\3\2\2\2\u0290\u0291\7C\2\2\u0291\u0293\7f\2\2\u0292\u0294"+
		"\5V,\2\u0293\u0292\3\2\2\2\u0293\u0294\3\2\2\2\u0294\u0297\3\2\2\2\u0295"+
		"\u0296\7l\2\2\u0296\u0298\5V,\2\u0297\u0295\3\2\2\2\u0297\u0298\3\2\2"+
		"\2\u0298\u0299\3\2\2\2\u0299\u029a\7g\2\2\u029aM\3\2\2\2\u029b\u029c\t"+
		"\6\2\2\u029c\u029d\7f\2\2\u029d\u029e\5V,\2\u029e\u02a1\7l\2\2\u029f\u02a2"+
		"\5V,\2\u02a0\u02a2\5\\/\2\u02a1\u029f\3\2\2\2\u02a1\u02a0\3\2\2\2\u02a2"+
		"\u02a5\3\2\2\2\u02a3\u02a4\7l\2\2\u02a4\u02a6\5V,\2\u02a5\u02a3\3\2\2"+
		"\2\u02a5\u02a6\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02a8\7g\2\2\u02a8O\3"+
		"\2\2\2\u02a9\u02aa\7G\2\2\u02aa\u02ac\7f\2\2\u02ab\u02ad\5V,\2\u02ac\u02ab"+
		"\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad\u02ae\3\2\2\2\u02ae\u02af\7g\2\2\u02af"+
		"Q\3\2\2\2\u02b0\u02b1\7\6\2\2\u02b1\u02b2\7e\2\2\u02b2\u02be\7q\2\2\u02b3"+
		"\u02b4\7\6\2\2\u02b4\u02b5\7e\2\2\u02b5\u02b6\7\24\2\2\u02b6\u02b7\7f"+
		"\2\2\u02b7\u02b8\7d\2\2\u02b8\u02be\7g\2\2\u02b9\u02ba\7\6\2\2\u02ba\u02bb"+
		"\7h\2\2\u02bb\u02bc\7d\2\2\u02bc\u02be\7i\2\2\u02bd\u02b0\3\2\2\2\u02bd"+
		"\u02b3\3\2\2\2\u02bd\u02b9\3\2\2\2\u02beS\3\2\2\2\u02bf\u02c0\5^\60\2"+
		"\u02c0U\3\2\2\2\u02c1\u02c2\7j\2\2\u02c2\u02c7\5X-\2\u02c3\u02c4\7l\2"+
		"\2\u02c4\u02c6\5X-\2\u02c5\u02c3\3\2\2\2\u02c6\u02c9\3\2\2\2\u02c7\u02c5"+
		"\3\2\2\2\u02c7\u02c8\3\2\2\2\u02c8\u02ca\3\2\2\2\u02c9\u02c7\3\2\2\2\u02ca"+
		"\u02cb\7k\2\2\u02cb\u02cf\3\2\2\2\u02cc\u02cd\7j\2\2\u02cd\u02cf\7k\2"+
		"\2\u02ce\u02c1\3\2\2\2\u02ce\u02cc\3\2\2\2\u02cfW\3\2\2\2\u02d0\u02d1"+
		"\5Z.\2\u02d1\u02d2\7m\2\2\u02d2\u02d3\5^\60\2\u02d3Y\3\2\2\2\u02d4\u02d8"+
		"\7q\2\2\u02d5\u02d8\7d\2\2\u02d6\u02d8\5`\61\2\u02d7\u02d4\3\2\2\2\u02d7"+
		"\u02d5\3\2\2\2\u02d7\u02d6\3\2\2\2\u02d8[\3\2\2\2\u02d9\u02da\7h\2\2\u02da"+
		"\u02df\5^\60\2\u02db\u02dc\7l\2\2\u02dc\u02de\5^\60\2\u02dd\u02db\3\2"+
		"\2\2\u02de\u02e1\3\2\2\2\u02df\u02dd\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0"+
		"\u02e2\3\2\2\2\u02e1\u02df\3\2\2\2\u02e2\u02e3\7i\2\2\u02e3\u02e7\3\2"+
		"\2\2\u02e4\u02e5\7h\2\2\u02e5\u02e7\7i\2\2\u02e6\u02d9\3\2\2\2\u02e6\u02e4"+
		"\3\2\2\2\u02e7]\3\2\2\2\u02e8\u0302\5V,\2\u02e9\u0302\7d\2\2\u02ea\u0302"+
		"\7r\2\2\u02eb\u0302\5\\/\2\u02ec\u0302\7a\2\2\u02ed\u0302\7b\2\2\u02ee"+
		"\u0302\7c\2\2\u02ef\u02f0\7q\2\2\u02f0\u02f1\7f\2\2\u02f1\u02f2\t\5\2"+
		"\2\u02f2\u0302\7g\2\2\u02f3\u02f4\7`\2\2\u02f4\u02f5\7q\2\2\u02f5\u02fe"+
		"\7f\2\2\u02f6\u02fb\5^\60\2\u02f7\u02f8\7l\2\2\u02f8\u02fa\5^\60\2\u02f9"+
		"\u02f7\3\2\2\2\u02fa\u02fd\3\2\2\2\u02fb\u02f9\3\2\2\2\u02fb\u02fc\3\2"+
		"\2\2\u02fc\u02ff\3\2\2\2\u02fd\u02fb\3\2\2\2\u02fe\u02f6\3\2\2\2\u02fe"+
		"\u02ff\3\2\2\2\u02ff\u0300\3\2\2\2\u0300\u0302\7g\2\2\u0301\u02e8\3\2"+
		"\2\2\u0301\u02e9\3\2\2\2\u0301\u02ea\3\2\2\2\u0301\u02eb\3\2\2\2\u0301"+
		"\u02ec\3\2\2\2\u0301\u02ed\3\2\2\2\u0301\u02ee\3\2\2\2\u0301\u02ef\3\2"+
		"\2\2\u0301\u02f3\3\2\2\2\u0302_\3\2\2\2\u0303\u0304\t\7\2\2\u0304a\3\2"+
		"\2\2Kfkq}\u008e\u009c\u00a6\u00b0\u00bf\u00c1\u00e8\u00f1\u0104\u010a"+
		"\u011b\u013e\u0147\u0151\u0156\u0161\u016a\u0171\u017d\u0184\u018d\u018f"+
		"\u0197\u019e\u01a9\u01ab\u01b6\u01b8\u01c3\u01c5\u01ce\u01d0\u01d9\u01db"+
		"\u01e6\u01e8\u01ea\u01f1\u022d\u0230\u0239\u023b\u023d\u0246\u0248\u0253"+
		"\u0255\u025f\u0263\u026b\u026f\u0278\u0281\u0288\u028c\u0293\u0297\u02a1"+
		"\u02a5\u02ac\u02bd\u02c7\u02ce\u02d7\u02df\u02e6\u02fb\u02fe\u0301";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}