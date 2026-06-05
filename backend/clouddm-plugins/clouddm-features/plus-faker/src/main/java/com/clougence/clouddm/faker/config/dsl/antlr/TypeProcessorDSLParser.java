// Generated from TypeProcessorDSLParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.faker.config.dsl.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TypeProcessorDSLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, COMMENT1=2, COMMENT2=3, COMMENT3=4, EOL=5, DEFINE=6, ALIAS=7, FOLLOW=8, 
		THROW=9, TRUE=10, FALSE=11, NULL=12, SET=13, APPEND=14, SEM=15, ALL=16, 
		COLON=17, COMMA=18, LBT=19, RBT=20, LSBT=21, RSBT=22, ENV=23, OCBR=24, 
		CCBR=25, STRING=26, HEX_NUM=27, OCT_NUM=28, BIT_NUM=29, SIZE=30, INTEGER_NUM=31, 
		DECIMAL_NUM=32, IDENTIFIER=33, TYPE=34;
	public static final int
		RULE_rootInstSet = 0, RULE_defineInst = 1, RULE_defineAlias = 2, RULE_defineConf = 3, 
		RULE_typeSetInst = 4, RULE_typeInst = 5, RULE_colTypeName = 6, RULE_colTypeConf = 7, 
		RULE_flowName = 8, RULE_anyValue = 9, RULE_funcCall = 10, RULE_baseValue = 11, 
		RULE_extValue = 12, RULE_envValue = 13, RULE_listValue = 14, RULE_objectValue = 15, 
		RULE_objectItem = 16, RULE_idStr = 17;
	private static String[] makeRuleNames() {
		return new String[] {
			"rootInstSet", "defineInst", "defineAlias", "defineConf", "typeSetInst", 
			"typeInst", "colTypeName", "colTypeConf", "flowName", "anyValue", "funcCall", 
			"baseValue", "extValue", "envValue", "listValue", "objectValue", "objectItem", 
			"idStr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'define'", "'alias'", "'follow'", 
			"'throw'", "'true'", "'false'", "'null'", "'='", "'+='", "';'", "'*'", 
			"':'", "','", "'('", "')'", "'['", "']'", "'${'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "COMMENT1", "COMMENT2", "COMMENT3", "EOL", "DEFINE", "ALIAS", 
			"FOLLOW", "THROW", "TRUE", "FALSE", "NULL", "SET", "APPEND", "SEM", "ALL", 
			"COLON", "COMMA", "LBT", "RBT", "LSBT", "RSBT", "ENV", "OCBR", "CCBR", 
			"STRING", "HEX_NUM", "OCT_NUM", "BIT_NUM", "SIZE", "INTEGER_NUM", "DECIMAL_NUM", 
			"IDENTIFIER", "TYPE"
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
	public String getGrammarFileName() { return "TypeProcessorDSLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TypeProcessorDSLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootInstSetContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TypeProcessorDSLParser.EOF, 0); }
		public DefineInstContext defineInst() {
			return getRuleContext(DefineInstContext.class,0);
		}
		public List<TypeSetInstContext> typeSetInst() {
			return getRuleContexts(TypeSetInstContext.class);
		}
		public TypeSetInstContext typeSetInst(int i) {
			return getRuleContext(TypeSetInstContext.class,i);
		}
		public RootInstSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rootInstSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterRootInstSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitRootInstSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitRootInstSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootInstSetContext rootInstSet() throws RecognitionException {
		RootInstSetContext _localctx = new RootInstSetContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_rootInstSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFINE) {
				{
				setState(36);
				defineInst();
				}
			}

			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LSBT) {
				{
				{
				setState(39);
				typeSetInst();
				}
				}
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(45);
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

	public static class DefineInstContext extends ParserRuleContext {
		public TerminalNode DEFINE() { return getToken(TypeProcessorDSLParser.DEFINE, 0); }
		public IdStrContext idStr() {
			return getRuleContext(IdStrContext.class,0);
		}
		public TerminalNode ALIAS() { return getToken(TypeProcessorDSLParser.ALIAS, 0); }
		public TerminalNode LSBT() { return getToken(TypeProcessorDSLParser.LSBT, 0); }
		public DefineAliasContext defineAlias() {
			return getRuleContext(DefineAliasContext.class,0);
		}
		public TerminalNode RSBT() { return getToken(TypeProcessorDSLParser.RSBT, 0); }
		public TerminalNode OCBR() { return getToken(TypeProcessorDSLParser.OCBR, 0); }
		public TerminalNode CCBR() { return getToken(TypeProcessorDSLParser.CCBR, 0); }
		public List<DefineConfContext> defineConf() {
			return getRuleContexts(DefineConfContext.class);
		}
		public DefineConfContext defineConf(int i) {
			return getRuleContext(DefineConfContext.class,i);
		}
		public DefineInstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineInst; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterDefineInst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitDefineInst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitDefineInst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineInstContext defineInst() throws RecognitionException {
		DefineInstContext _localctx = new DefineInstContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_defineInst);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(DEFINE);
			setState(48);
			idStr();
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS) {
				{
				setState(49);
				match(ALIAS);
				setState(50);
				match(LSBT);
				setState(51);
				defineAlias();
				setState(52);
				match(RSBT);
				}
			}

			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OCBR) {
				{
				setState(56);
				match(OCBR);
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << TYPE))) != 0)) {
					{
					{
					setState(57);
					defineConf();
					}
					}
					setState(62);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(63);
				match(CCBR);
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

	public static class DefineAliasContext extends ParserRuleContext {
		public List<IdStrContext> idStr() {
			return getRuleContexts(IdStrContext.class);
		}
		public IdStrContext idStr(int i) {
			return getRuleContext(IdStrContext.class,i);
		}
		public DefineAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterDefineAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitDefineAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitDefineAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineAliasContext defineAlias() throws RecognitionException {
		DefineAliasContext _localctx = new DefineAliasContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_defineAlias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(66);
				idStr();
				}
				}
				setState(69); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << TYPE))) != 0) );
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

	public static class DefineConfContext extends ParserRuleContext {
		public IdStrContext idStr() {
			return getRuleContext(IdStrContext.class,0);
		}
		public TerminalNode SET() { return getToken(TypeProcessorDSLParser.SET, 0); }
		public AnyValueContext anyValue() {
			return getRuleContext(AnyValueContext.class,0);
		}
		public TerminalNode SEM() { return getToken(TypeProcessorDSLParser.SEM, 0); }
		public DefineConfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineConf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterDefineConf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitDefineConf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitDefineConf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineConfContext defineConf() throws RecognitionException {
		DefineConfContext _localctx = new DefineConfContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_defineConf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			idStr();
			setState(72);
			match(SET);
			setState(73);
			anyValue();
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEM) {
				{
				setState(74);
				match(SEM);
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

	public static class TypeSetInstContext extends ParserRuleContext {
		public List<TypeInstContext> typeInst() {
			return getRuleContexts(TypeInstContext.class);
		}
		public TypeInstContext typeInst(int i) {
			return getRuleContext(TypeInstContext.class,i);
		}
		public List<TerminalNode> SEM() { return getTokens(TypeProcessorDSLParser.SEM); }
		public TerminalNode SEM(int i) {
			return getToken(TypeProcessorDSLParser.SEM, i);
		}
		public TypeSetInstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSetInst; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterTypeSetInst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitTypeSetInst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitTypeSetInst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSetInstContext typeSetInst() throws RecognitionException {
		TypeSetInstContext _localctx = new TypeSetInstContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typeSetInst);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(81); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(77);
					typeInst();
					setState(79);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SEM) {
						{
						setState(78);
						match(SEM);
						}
					}

					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(83); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static class TypeInstContext extends ParserRuleContext {
		public ColTypeNameContext colTypeName() {
			return getRuleContext(ColTypeNameContext.class,0);
		}
		public TerminalNode FOLLOW() { return getToken(TypeProcessorDSLParser.FOLLOW, 0); }
		public FlowNameContext flowName() {
			return getRuleContext(FlowNameContext.class,0);
		}
		public TerminalNode THROW() { return getToken(TypeProcessorDSLParser.THROW, 0); }
		public TerminalNode STRING() { return getToken(TypeProcessorDSLParser.STRING, 0); }
		public List<ColTypeConfContext> colTypeConf() {
			return getRuleContexts(ColTypeConfContext.class);
		}
		public ColTypeConfContext colTypeConf(int i) {
			return getRuleContext(ColTypeConfContext.class,i);
		}
		public TypeInstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeInst; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterTypeInst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitTypeInst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitTypeInst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeInstContext typeInst() throws RecognitionException {
		TypeInstContext _localctx = new TypeInstContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_typeInst);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			colTypeName();
			setState(95);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
			case IDENTIFIER:
			case TYPE:
				{
				setState(87); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(86);
					colTypeConf();
					}
					}
					setState(89); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << TYPE))) != 0) );
				}
				break;
			case FOLLOW:
				{
				{
				setState(91);
				match(FOLLOW);
				setState(92);
				flowName();
				}
				}
				break;
			case THROW:
				{
				{
				setState(93);
				match(THROW);
				setState(94);
				match(STRING);
				}
				}
				break;
			case EOF:
			case SEM:
			case LSBT:
				break;
			default:
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

	public static class ColTypeNameContext extends ParserRuleContext {
		public TerminalNode LSBT() { return getToken(TypeProcessorDSLParser.LSBT, 0); }
		public TerminalNode RSBT() { return getToken(TypeProcessorDSLParser.RSBT, 0); }
		public TerminalNode ALL() { return getToken(TypeProcessorDSLParser.ALL, 0); }
		public IdStrContext idStr() {
			return getRuleContext(IdStrContext.class,0);
		}
		public ColTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterColTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitColTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitColTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColTypeNameContext colTypeName() throws RecognitionException {
		ColTypeNameContext _localctx = new ColTypeNameContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_colTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			match(LSBT);
			setState(100);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ALL:
				{
				setState(98);
				match(ALL);
				}
				break;
			case STRING:
			case IDENTIFIER:
			case TYPE:
				{
				setState(99);
				idStr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(102);
			match(RSBT);
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

	public static class ColTypeConfContext extends ParserRuleContext {
		public IdStrContext idStr() {
			return getRuleContext(IdStrContext.class,0);
		}
		public AnyValueContext anyValue() {
			return getRuleContext(AnyValueContext.class,0);
		}
		public TerminalNode SET() { return getToken(TypeProcessorDSLParser.SET, 0); }
		public TerminalNode APPEND() { return getToken(TypeProcessorDSLParser.APPEND, 0); }
		public ColTypeConfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colTypeConf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterColTypeConf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitColTypeConf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitColTypeConf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColTypeConfContext colTypeConf() throws RecognitionException {
		ColTypeConfContext _localctx = new ColTypeConfContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_colTypeConf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			idStr();
			setState(105);
			_la = _input.LA(1);
			if ( !(_la==SET || _la==APPEND) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(106);
			anyValue();
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

	public static class FlowNameContext extends ParserRuleContext {
		public ColTypeNameContext colTypeName() {
			return getRuleContext(ColTypeNameContext.class,0);
		}
		public FlowNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterFlowName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitFlowName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitFlowName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowNameContext flowName() throws RecognitionException {
		FlowNameContext _localctx = new FlowNameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_flowName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			colTypeName();
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

	public static class AnyValueContext extends ParserRuleContext {
		public ExtValueContext extValue() {
			return getRuleContext(ExtValueContext.class,0);
		}
		public BaseValueContext baseValue() {
			return getRuleContext(BaseValueContext.class,0);
		}
		public ListValueContext listValue() {
			return getRuleContext(ListValueContext.class,0);
		}
		public ObjectValueContext objectValue() {
			return getRuleContext(ObjectValueContext.class,0);
		}
		public EnvValueContext envValue() {
			return getRuleContext(EnvValueContext.class,0);
		}
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public AnyValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterAnyValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitAnyValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitAnyValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyValueContext anyValue() throws RecognitionException {
		AnyValueContext _localctx = new AnyValueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_anyValue);
		try {
			setState(116);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(110);
				extValue();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(111);
				baseValue();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(112);
				listValue();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(113);
				objectValue();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(114);
				envValue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(115);
				funcCall();
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

	public static class FuncCallContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TypeProcessorDSLParser.IDENTIFIER, 0); }
		public TerminalNode LBT() { return getToken(TypeProcessorDSLParser.LBT, 0); }
		public TerminalNode RBT() { return getToken(TypeProcessorDSLParser.RBT, 0); }
		public List<AnyValueContext> anyValue() {
			return getRuleContexts(AnyValueContext.class);
		}
		public AnyValueContext anyValue(int i) {
			return getRuleContext(AnyValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TypeProcessorDSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TypeProcessorDSLParser.COMMA, i);
		}
		public FuncCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterFuncCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitFuncCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitFuncCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncCallContext funcCall() throws RecognitionException {
		FuncCallContext _localctx = new FuncCallContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_funcCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(IDENTIFIER);
			setState(119);
			match(LBT);
			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << NULL) | (1L << LSBT) | (1L << ENV) | (1L << OCBR) | (1L << STRING) | (1L << HEX_NUM) | (1L << OCT_NUM) | (1L << BIT_NUM) | (1L << SIZE) | (1L << INTEGER_NUM) | (1L << DECIMAL_NUM) | (1L << IDENTIFIER) | (1L << TYPE))) != 0)) {
				{
				setState(120);
				anyValue();
				setState(125);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(121);
					match(COMMA);
					setState(122);
					anyValue();
					}
					}
					setState(127);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(130);
			match(RBT);
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

	public static class BaseValueContext extends ParserRuleContext {
		public BaseValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseValue; }
	 
		public BaseValueContext() { }
		public void copyFrom(BaseValueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StringValueContext extends BaseValueContext {
		public TerminalNode STRING() { return getToken(TypeProcessorDSLParser.STRING, 0); }
		public StringValueContext(BaseValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterStringValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitStringValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitStringValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanValueContext extends BaseValueContext {
		public TerminalNode TRUE() { return getToken(TypeProcessorDSLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(TypeProcessorDSLParser.FALSE, 0); }
		public BooleanValueContext(BaseValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterBooleanValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitBooleanValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitBooleanValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeValueContext extends BaseValueContext {
		public TerminalNode TYPE() { return getToken(TypeProcessorDSLParser.TYPE, 0); }
		public TypeValueContext(BaseValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterTypeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitTypeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitTypeValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberValueContext extends BaseValueContext {
		public TerminalNode DECIMAL_NUM() { return getToken(TypeProcessorDSLParser.DECIMAL_NUM, 0); }
		public TerminalNode INTEGER_NUM() { return getToken(TypeProcessorDSLParser.INTEGER_NUM, 0); }
		public TerminalNode HEX_NUM() { return getToken(TypeProcessorDSLParser.HEX_NUM, 0); }
		public TerminalNode OCT_NUM() { return getToken(TypeProcessorDSLParser.OCT_NUM, 0); }
		public TerminalNode BIT_NUM() { return getToken(TypeProcessorDSLParser.BIT_NUM, 0); }
		public NumberValueContext(BaseValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterNumberValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitNumberValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitNumberValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullValueContext extends BaseValueContext {
		public TerminalNode NULL() { return getToken(TypeProcessorDSLParser.NULL, 0); }
		public NullValueContext(BaseValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterNullValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitNullValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitNullValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseValueContext baseValue() throws RecognitionException {
		BaseValueContext _localctx = new BaseValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_baseValue);
		int _la;
		try {
			setState(137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringValueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(132);
				match(STRING);
				}
				break;
			case NULL:
				_localctx = new NullValueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(NULL);
				}
				break;
			case TRUE:
			case FALSE:
				_localctx = new BooleanValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(134);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case HEX_NUM:
			case OCT_NUM:
			case BIT_NUM:
			case INTEGER_NUM:
			case DECIMAL_NUM:
				_localctx = new NumberValueContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(135);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << HEX_NUM) | (1L << OCT_NUM) | (1L << BIT_NUM) | (1L << INTEGER_NUM) | (1L << DECIMAL_NUM))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case TYPE:
				_localctx = new TypeValueContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(136);
				match(TYPE);
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

	public static class ExtValueContext extends ParserRuleContext {
		public TerminalNode SIZE() { return getToken(TypeProcessorDSLParser.SIZE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TypeProcessorDSLParser.IDENTIFIER, 0); }
		public ExtValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterExtValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitExtValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitExtValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtValueContext extValue() throws RecognitionException {
		ExtValueContext _localctx = new ExtValueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_extValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			_la = _input.LA(1);
			if ( !(_la==SIZE || _la==IDENTIFIER) ) {
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

	public static class EnvValueContext extends ParserRuleContext {
		public TerminalNode ENV() { return getToken(TypeProcessorDSLParser.ENV, 0); }
		public IdStrContext idStr() {
			return getRuleContext(IdStrContext.class,0);
		}
		public TerminalNode CCBR() { return getToken(TypeProcessorDSLParser.CCBR, 0); }
		public EnvValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_envValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterEnvValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitEnvValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitEnvValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnvValueContext envValue() throws RecognitionException {
		EnvValueContext _localctx = new EnvValueContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_envValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(ENV);
			setState(142);
			idStr();
			setState(143);
			match(CCBR);
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

	public static class ListValueContext extends ParserRuleContext {
		public TerminalNode LSBT() { return getToken(TypeProcessorDSLParser.LSBT, 0); }
		public TerminalNode RSBT() { return getToken(TypeProcessorDSLParser.RSBT, 0); }
		public List<AnyValueContext> anyValue() {
			return getRuleContexts(AnyValueContext.class);
		}
		public AnyValueContext anyValue(int i) {
			return getRuleContext(AnyValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TypeProcessorDSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TypeProcessorDSLParser.COMMA, i);
		}
		public ListValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterListValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitListValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitListValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListValueContext listValue() throws RecognitionException {
		ListValueContext _localctx = new ListValueContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_listValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			match(LSBT);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << NULL) | (1L << LSBT) | (1L << ENV) | (1L << OCBR) | (1L << STRING) | (1L << HEX_NUM) | (1L << OCT_NUM) | (1L << BIT_NUM) | (1L << SIZE) | (1L << INTEGER_NUM) | (1L << DECIMAL_NUM) | (1L << IDENTIFIER) | (1L << TYPE))) != 0)) {
				{
				setState(147); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(146);
					anyValue();
					}
					}
					setState(149); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << NULL) | (1L << LSBT) | (1L << ENV) | (1L << OCBR) | (1L << STRING) | (1L << HEX_NUM) | (1L << OCT_NUM) | (1L << BIT_NUM) | (1L << SIZE) | (1L << INTEGER_NUM) | (1L << DECIMAL_NUM) | (1L << IDENTIFIER) | (1L << TYPE))) != 0) );
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(151);
					match(COMMA);
					setState(152);
					anyValue();
					}
					}
					setState(157);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(160);
			match(RSBT);
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

	public static class ObjectValueContext extends ParserRuleContext {
		public TerminalNode OCBR() { return getToken(TypeProcessorDSLParser.OCBR, 0); }
		public TerminalNode CCBR() { return getToken(TypeProcessorDSLParser.CCBR, 0); }
		public List<ObjectItemContext> objectItem() {
			return getRuleContexts(ObjectItemContext.class);
		}
		public ObjectItemContext objectItem(int i) {
			return getRuleContext(ObjectItemContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TypeProcessorDSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TypeProcessorDSLParser.COMMA, i);
		}
		public ObjectValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterObjectValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitObjectValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitObjectValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectValueContext objectValue() throws RecognitionException {
		ObjectValueContext _localctx = new ObjectValueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_objectValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(OCBR);
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << TYPE))) != 0)) {
				{
				setState(164); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(163);
					objectItem();
					}
					}
					setState(166); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << TYPE))) != 0) );
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(168);
					match(COMMA);
					setState(169);
					objectItem();
					}
					}
					setState(174);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(177);
			match(CCBR);
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

	public static class ObjectItemContext extends ParserRuleContext {
		public IdStrContext idStr() {
			return getRuleContext(IdStrContext.class,0);
		}
		public TerminalNode COLON() { return getToken(TypeProcessorDSLParser.COLON, 0); }
		public AnyValueContext anyValue() {
			return getRuleContext(AnyValueContext.class,0);
		}
		public ObjectItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterObjectItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitObjectItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitObjectItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectItemContext objectItem() throws RecognitionException {
		ObjectItemContext _localctx = new ObjectItemContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_objectItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			idStr();
			setState(180);
			match(COLON);
			setState(181);
			anyValue();
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

	public static class IdStrContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(TypeProcessorDSLParser.STRING, 0); }
		public TerminalNode TYPE() { return getToken(TypeProcessorDSLParser.TYPE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TypeProcessorDSLParser.IDENTIFIER, 0); }
		public IdStrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idStr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).enterIdStr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeProcessorDSLParserListener ) ((TypeProcessorDSLParserListener)listener).exitIdStr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeProcessorDSLParserVisitor ) return ((TypeProcessorDSLParserVisitor<? extends T>)visitor).visitIdStr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdStrContext idStr() throws RecognitionException {
		IdStrContext _localctx = new IdStrContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_idStr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << TYPE))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3$\u00bc\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\5\2(\n\2\3\2\7\2+\n\2\f\2\16\2.\13\2\3\2\3\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\39\n\3\3\3\3\3\7\3=\n\3\f\3\16\3@\13\3\3\3\5\3C\n"+
		"\3\3\4\6\4F\n\4\r\4\16\4G\3\5\3\5\3\5\3\5\5\5N\n\5\3\6\3\6\5\6R\n\6\6"+
		"\6T\n\6\r\6\16\6U\3\7\3\7\6\7Z\n\7\r\7\16\7[\3\7\3\7\3\7\3\7\5\7b\n\7"+
		"\3\b\3\b\3\b\5\bg\n\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\5\13w\n\13\3\f\3\f\3\f\3\f\3\f\7\f~\n\f\f\f\16\f\u0081\13"+
		"\f\5\f\u0083\n\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\5\r\u008c\n\r\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\20\3\20\6\20\u0096\n\20\r\20\16\20\u0097\3\20\3\20"+
		"\7\20\u009c\n\20\f\20\16\20\u009f\13\20\5\20\u00a1\n\20\3\20\3\20\3\21"+
		"\3\21\6\21\u00a7\n\21\r\21\16\21\u00a8\3\21\3\21\7\21\u00ad\n\21\f\21"+
		"\16\21\u00b0\13\21\5\21\u00b2\n\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\7\3\2\17"+
		"\20\3\2\f\r\4\2\35\37!\"\4\2  ##\4\2\34\34#$\2\u00c8\2\'\3\2\2\2\4\61"+
		"\3\2\2\2\6E\3\2\2\2\bI\3\2\2\2\nS\3\2\2\2\fW\3\2\2\2\16c\3\2\2\2\20j\3"+
		"\2\2\2\22n\3\2\2\2\24v\3\2\2\2\26x\3\2\2\2\30\u008b\3\2\2\2\32\u008d\3"+
		"\2\2\2\34\u008f\3\2\2\2\36\u0093\3\2\2\2 \u00a4\3\2\2\2\"\u00b5\3\2\2"+
		"\2$\u00b9\3\2\2\2&(\5\4\3\2\'&\3\2\2\2\'(\3\2\2\2(,\3\2\2\2)+\5\n\6\2"+
		"*)\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2\2-/\3\2\2\2.,\3\2\2\2/\60\7\2\2"+
		"\3\60\3\3\2\2\2\61\62\7\b\2\2\628\5$\23\2\63\64\7\t\2\2\64\65\7\27\2\2"+
		"\65\66\5\6\4\2\66\67\7\30\2\2\679\3\2\2\28\63\3\2\2\289\3\2\2\29B\3\2"+
		"\2\2:>\7\32\2\2;=\5\b\5\2<;\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3"+
		"\2\2\2@>\3\2\2\2AC\7\33\2\2B:\3\2\2\2BC\3\2\2\2C\5\3\2\2\2DF\5$\23\2E"+
		"D\3\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2H\7\3\2\2\2IJ\5$\23\2JK\7\17\2"+
		"\2KM\5\24\13\2LN\7\21\2\2ML\3\2\2\2MN\3\2\2\2N\t\3\2\2\2OQ\5\f\7\2PR\7"+
		"\21\2\2QP\3\2\2\2QR\3\2\2\2RT\3\2\2\2SO\3\2\2\2TU\3\2\2\2US\3\2\2\2UV"+
		"\3\2\2\2V\13\3\2\2\2Wa\5\16\b\2XZ\5\20\t\2YX\3\2\2\2Z[\3\2\2\2[Y\3\2\2"+
		"\2[\\\3\2\2\2\\b\3\2\2\2]^\7\n\2\2^b\5\22\n\2_`\7\13\2\2`b\7\34\2\2aY"+
		"\3\2\2\2a]\3\2\2\2a_\3\2\2\2ab\3\2\2\2b\r\3\2\2\2cf\7\27\2\2dg\7\22\2"+
		"\2eg\5$\23\2fd\3\2\2\2fe\3\2\2\2gh\3\2\2\2hi\7\30\2\2i\17\3\2\2\2jk\5"+
		"$\23\2kl\t\2\2\2lm\5\24\13\2m\21\3\2\2\2no\5\16\b\2o\23\3\2\2\2pw\5\32"+
		"\16\2qw\5\30\r\2rw\5\36\20\2sw\5 \21\2tw\5\34\17\2uw\5\26\f\2vp\3\2\2"+
		"\2vq\3\2\2\2vr\3\2\2\2vs\3\2\2\2vt\3\2\2\2vu\3\2\2\2w\25\3\2\2\2xy\7#"+
		"\2\2y\u0082\7\25\2\2z\177\5\24\13\2{|\7\24\2\2|~\5\24\13\2}{\3\2\2\2~"+
		"\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\u0083\3\2\2\2\u0081"+
		"\177\3\2\2\2\u0082z\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0084\3\2\2\2\u0084"+
		"\u0085\7\26\2\2\u0085\27\3\2\2\2\u0086\u008c\7\34\2\2\u0087\u008c\7\16"+
		"\2\2\u0088\u008c\t\3\2\2\u0089\u008c\t\4\2\2\u008a\u008c\7$\2\2\u008b"+
		"\u0086\3\2\2\2\u008b\u0087\3\2\2\2\u008b\u0088\3\2\2\2\u008b\u0089\3\2"+
		"\2\2\u008b\u008a\3\2\2\2\u008c\31\3\2\2\2\u008d\u008e\t\5\2\2\u008e\33"+
		"\3\2\2\2\u008f\u0090\7\31\2\2\u0090\u0091\5$\23\2\u0091\u0092\7\33\2\2"+
		"\u0092\35\3\2\2\2\u0093\u00a0\7\27\2\2\u0094\u0096\5\24\13\2\u0095\u0094"+
		"\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098"+
		"\u009d\3\2\2\2\u0099\u009a\7\24\2\2\u009a\u009c\5\24\13\2\u009b\u0099"+
		"\3\2\2\2\u009c\u009f\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e"+
		"\u00a1\3\2\2\2\u009f\u009d\3\2\2\2\u00a0\u0095\3\2\2\2\u00a0\u00a1\3\2"+
		"\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a3\7\30\2\2\u00a3\37\3\2\2\2\u00a4\u00b1"+
		"\7\32\2\2\u00a5\u00a7\5\"\22\2\u00a6\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2"+
		"\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ae\3\2\2\2\u00aa\u00ab"+
		"\7\24\2\2\u00ab\u00ad\5\"\22\2\u00ac\u00aa\3\2\2\2\u00ad\u00b0\3\2\2\2"+
		"\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b2\3\2\2\2\u00b0\u00ae"+
		"\3\2\2\2\u00b1\u00a6\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3"+
		"\u00b4\7\33\2\2\u00b4!\3\2\2\2\u00b5\u00b6\5$\23\2\u00b6\u00b7\7\23\2"+
		"\2\u00b7\u00b8\5\24\13\2\u00b8#\3\2\2\2\u00b9\u00ba\t\6\2\2\u00ba%\3\2"+
		"\2\2\30\',8>BGMQU[afv\177\u0082\u008b\u0097\u009d\u00a0\u00a8\u00ae\u00b1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}