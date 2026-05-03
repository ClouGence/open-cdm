// Generated from TypeProcessorDSLLexer.g4 by ANTLR 4.9.3
package com.clougence.clouddm.faker.config.dsl.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TypeProcessorDSLLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "COMMENT1", "COMMENT2", "COMMENT3", "EOL", "DEFINE", "ALIAS", "FOLLOW", 
			"THROW", "TRUE", "FALSE", "NULL", "SET", "APPEND", "SEM", "ALL", "COLON", 
			"COMMA", "LBT", "RBT", "LSBT", "RSBT", "ENV", "OCBR", "CCBR", "STRING", 
			"TRANS", "UNICODE", "HEX", "HEX_NUM", "OCT_NUM", "BIT_NUM", "SIZE", "INTEGER_NUM", 
			"DECIMAL_NUM", "B", "KB", "MB", "IDENTIFIER", "TYPE"
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


	public TypeProcessorDSLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TypeProcessorDSLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2$\u015c\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\6\2U\n\2\r"+
		"\2\16\2V\3\2\3\2\3\3\3\3\3\3\3\3\7\3_\n\3\f\3\16\3b\13\3\3\3\5\3e\n\3"+
		"\3\3\3\3\3\4\3\4\7\4k\n\4\f\4\16\4n\13\4\3\4\5\4q\n\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\7\5y\n\5\f\5\16\5|\13\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31"+
		"\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\33\7\33\u00ce\n\33\f\33\16\33\u00d1"+
		"\13\33\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u00d9\n\33\f\33\16\33\u00dc"+
		"\13\33\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u00e4\n\33\f\33\16\33\u00e7"+
		"\13\33\3\33\5\33\u00ea\n\33\3\34\3\34\3\34\5\34\u00ef\n\34\3\35\3\35\3"+
		"\35\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3\37\6\37\u00fc\n\37\r\37\16\37"+
		"\u00fd\3 \3 \3 \6 \u0103\n \r \16 \u0104\3!\3!\3!\6!\u010a\n!\r!\16!\u010b"+
		"\3\"\3\"\7\"\u0110\n\"\f\"\16\"\u0113\13\"\3\"\3\"\3\"\5\"\u0118\n\"\3"+
		"#\5#\u011b\n#\3#\6#\u011e\n#\r#\16#\u011f\3$\5$\u0123\n$\3$\7$\u0126\n"+
		"$\f$\16$\u0129\13$\3$\3$\6$\u012d\n$\r$\16$\u012e\3$\6$\u0132\n$\r$\16"+
		"$\u0133\5$\u0136\n$\3$\3$\5$\u013a\n$\3$\3$\7$\u013e\n$\f$\16$\u0141\13"+
		"$\5$\u0143\n$\3%\3%\3&\3&\3\'\3\'\3(\3(\7(\u014d\n(\f(\16(\u0150\13(\3"+
		")\3)\3)\6)\u0155\n)\r)\16)\u0156\5)\u0159\n)\3)\3)\3z\2*\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\29\2;\2=\35?\36A\37C E"+
		"!G\"I\2K\2M\2O#Q$\3\2\24\5\2\13\f\16\17\"\"\4\2\f\f\17\17\4\2\f\f\16\17"+
		"\5\2\f\f\17\17$$\5\2\f\f\17\17))\13\2$$))\61\61^^ddhhppttvv\5\2\62;CH"+
		"ch\4\2ZZzz\4\2QQqq\3\2\629\4\2DDdd\3\2\62\63\3\2\63;\3\2\62;\4\2GGgg\4"+
		"\2--//\5\2C\\aac|\6\2\62;C\\aac|\2\u017a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G"+
		"\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\3T\3\2\2\2\5Z\3\2\2\2\7h\3\2\2\2\tt\3\2"+
		"\2\2\13\u0080\3\2\2\2\r\u0082\3\2\2\2\17\u0089\3\2\2\2\21\u008f\3\2\2"+
		"\2\23\u0096\3\2\2\2\25\u009c\3\2\2\2\27\u00a1\3\2\2\2\31\u00a7\3\2\2\2"+
		"\33\u00ac\3\2\2\2\35\u00ae\3\2\2\2\37\u00b1\3\2\2\2!\u00b3\3\2\2\2#\u00b5"+
		"\3\2\2\2%\u00b7\3\2\2\2\'\u00b9\3\2\2\2)\u00bb\3\2\2\2+\u00bd\3\2\2\2"+
		"-\u00bf\3\2\2\2/\u00c1\3\2\2\2\61\u00c4\3\2\2\2\63\u00c6\3\2\2\2\65\u00e9"+
		"\3\2\2\2\67\u00eb\3\2\2\29\u00f0\3\2\2\2;\u00f6\3\2\2\2=\u00f8\3\2\2\2"+
		"?\u00ff\3\2\2\2A\u0106\3\2\2\2C\u010d\3\2\2\2E\u011a\3\2\2\2G\u0122\3"+
		"\2\2\2I\u0144\3\2\2\2K\u0146\3\2\2\2M\u0148\3\2\2\2O\u014a\3\2\2\2Q\u0158"+
		"\3\2\2\2SU\t\2\2\2TS\3\2\2\2UV\3\2\2\2VT\3\2\2\2VW\3\2\2\2WX\3\2\2\2X"+
		"Y\b\2\2\2Y\4\3\2\2\2Z[\7\61\2\2[\\\7\61\2\2\\`\3\2\2\2]_\n\3\2\2^]\3\2"+
		"\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2ad\3\2\2\2b`\3\2\2\2ce\5\13\6\2dc\3"+
		"\2\2\2de\3\2\2\2ef\3\2\2\2fg\b\3\2\2g\6\3\2\2\2hl\7%\2\2ik\n\3\2\2ji\3"+
		"\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2mp\3\2\2\2nl\3\2\2\2oq\5\13\6\2po"+
		"\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs\b\4\2\2s\b\3\2\2\2tu\7\61\2\2uv\7,\2\2"+
		"vz\3\2\2\2wy\13\2\2\2xw\3\2\2\2y|\3\2\2\2z{\3\2\2\2zx\3\2\2\2{}\3\2\2"+
		"\2|z\3\2\2\2}~\7,\2\2~\177\7\61\2\2\177\n\3\2\2\2\u0080\u0081\t\4\2\2"+
		"\u0081\f\3\2\2\2\u0082\u0083\7f\2\2\u0083\u0084\7g\2\2\u0084\u0085\7h"+
		"\2\2\u0085\u0086\7k\2\2\u0086\u0087\7p\2\2\u0087\u0088\7g\2\2\u0088\16"+
		"\3\2\2\2\u0089\u008a\7c\2\2\u008a\u008b\7n\2\2\u008b\u008c\7k\2\2\u008c"+
		"\u008d\7c\2\2\u008d\u008e\7u\2\2\u008e\20\3\2\2\2\u008f\u0090\7h\2\2\u0090"+
		"\u0091\7q\2\2\u0091\u0092\7n\2\2\u0092\u0093\7n\2\2\u0093\u0094\7q\2\2"+
		"\u0094\u0095\7y\2\2\u0095\22\3\2\2\2\u0096\u0097\7v\2\2\u0097\u0098\7"+
		"j\2\2\u0098\u0099\7t\2\2\u0099\u009a\7q\2\2\u009a\u009b\7y\2\2\u009b\24"+
		"\3\2\2\2\u009c\u009d\7v\2\2\u009d\u009e\7t\2\2\u009e\u009f\7w\2\2\u009f"+
		"\u00a0\7g\2\2\u00a0\26\3\2\2\2\u00a1\u00a2\7h\2\2\u00a2\u00a3\7c\2\2\u00a3"+
		"\u00a4\7n\2\2\u00a4\u00a5\7u\2\2\u00a5\u00a6\7g\2\2\u00a6\30\3\2\2\2\u00a7"+
		"\u00a8\7p\2\2\u00a8\u00a9\7w\2\2\u00a9\u00aa\7n\2\2\u00aa\u00ab\7n\2\2"+
		"\u00ab\32\3\2\2\2\u00ac\u00ad\7?\2\2\u00ad\34\3\2\2\2\u00ae\u00af\7-\2"+
		"\2\u00af\u00b0\7?\2\2\u00b0\36\3\2\2\2\u00b1\u00b2\7=\2\2\u00b2 \3\2\2"+
		"\2\u00b3\u00b4\7,\2\2\u00b4\"\3\2\2\2\u00b5\u00b6\7<\2\2\u00b6$\3\2\2"+
		"\2\u00b7\u00b8\7.\2\2\u00b8&\3\2\2\2\u00b9\u00ba\7*\2\2\u00ba(\3\2\2\2"+
		"\u00bb\u00bc\7+\2\2\u00bc*\3\2\2\2\u00bd\u00be\7]\2\2\u00be,\3\2\2\2\u00bf"+
		"\u00c0\7_\2\2\u00c0.\3\2\2\2\u00c1\u00c2\7&\2\2\u00c2\u00c3\7}\2\2\u00c3"+
		"\60\3\2\2\2\u00c4\u00c5\7}\2\2\u00c5\62\3\2\2\2\u00c6\u00c7\7\177\2\2"+
		"\u00c7\64\3\2\2\2\u00c8\u00cf\7$\2\2\u00c9\u00ce\n\5\2\2\u00ca\u00cb\7"+
		"$\2\2\u00cb\u00ce\7$\2\2\u00cc\u00ce\5\67\34\2\u00cd\u00c9\3\2\2\2\u00cd"+
		"\u00ca\3\2\2\2\u00cd\u00cc\3\2\2\2\u00ce\u00d1\3\2\2\2\u00cf\u00cd\3\2"+
		"\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d2\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d2"+
		"\u00ea\7$\2\2\u00d3\u00da\7)\2\2\u00d4\u00d9\n\6\2\2\u00d5\u00d6\7)\2"+
		"\2\u00d6\u00d9\7)\2\2\u00d7\u00d9\5\67\34\2\u00d8\u00d4\3\2\2\2\u00d8"+
		"\u00d5\3\2\2\2\u00d8\u00d7\3\2\2\2\u00d9\u00dc\3\2\2\2\u00da\u00d8\3\2"+
		"\2\2\u00da\u00db\3\2\2\2\u00db\u00dd\3\2\2\2\u00dc\u00da\3\2\2\2\u00dd"+
		"\u00ea\7)\2\2\u00de\u00e5\7b\2\2\u00df\u00e4\n\6\2\2\u00e0\u00e1\7b\2"+
		"\2\u00e1\u00e4\7b\2\2\u00e2\u00e4\5\67\34\2\u00e3\u00df\3\2\2\2\u00e3"+
		"\u00e0\3\2\2\2\u00e3\u00e2\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e3\3\2"+
		"\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e8\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8"+
		"\u00ea\7b\2\2\u00e9\u00c8\3\2\2\2\u00e9\u00d3\3\2\2\2\u00e9\u00de\3\2"+
		"\2\2\u00ea\66\3\2\2\2\u00eb\u00ee\7^\2\2\u00ec\u00ef\t\7\2\2\u00ed\u00ef"+
		"\59\35\2\u00ee\u00ec\3\2\2\2\u00ee\u00ed\3\2\2\2\u00ef8\3\2\2\2\u00f0"+
		"\u00f1\7w\2\2\u00f1\u00f2\5;\36\2\u00f2\u00f3\5;\36\2\u00f3\u00f4\5;\36"+
		"\2\u00f4\u00f5\5;\36\2\u00f5:\3\2\2\2\u00f6\u00f7\t\b\2\2\u00f7<\3\2\2"+
		"\2\u00f8\u00f9\7\62\2\2\u00f9\u00fb\t\t\2\2\u00fa\u00fc\t\b\2\2\u00fb"+
		"\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2"+
		"\2\2\u00fe>\3\2\2\2\u00ff\u0100\7\62\2\2\u0100\u0102\t\n\2\2\u0101\u0103"+
		"\t\13\2\2\u0102\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0102\3\2\2\2"+
		"\u0104\u0105\3\2\2\2\u0105@\3\2\2\2\u0106\u0107\7\62\2\2\u0107\u0109\t"+
		"\f\2\2\u0108\u010a\t\r\2\2\u0109\u0108\3\2\2\2\u010a\u010b\3\2\2\2\u010b"+
		"\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010cB\3\2\2\2\u010d\u0111\t\16\2\2"+
		"\u010e\u0110\t\17\2\2\u010f\u010e\3\2\2\2\u0110\u0113\3\2\2\2\u0111\u010f"+
		"\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0117\3\2\2\2\u0113\u0111\3\2\2\2\u0114"+
		"\u0118\5I%\2\u0115\u0118\5K&\2\u0116\u0118\5M\'\2\u0117\u0114\3\2\2\2"+
		"\u0117\u0115\3\2\2\2\u0117\u0116\3\2\2\2\u0118D\3\2\2\2\u0119\u011b\7"+
		"/\2\2\u011a\u0119\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011d\3\2\2\2\u011c"+
		"\u011e\t\17\2\2\u011d\u011c\3\2\2\2\u011e\u011f\3\2\2\2\u011f\u011d\3"+
		"\2\2\2\u011f\u0120\3\2\2\2\u0120F\3\2\2\2\u0121\u0123\7/\2\2\u0122\u0121"+
		"\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u0135\3\2\2\2\u0124\u0126\t\17\2\2"+
		"\u0125\u0124\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128"+
		"\3\2\2\2\u0128\u012a\3\2\2\2\u0129\u0127\3\2\2\2\u012a\u012c\7\60\2\2"+
		"\u012b\u012d\t\17\2\2\u012c\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e\u012c"+
		"\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u0136\3\2\2\2\u0130\u0132\t\16\2\2"+
		"\u0131\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134"+
		"\3\2\2\2\u0134\u0136\3\2\2\2\u0135\u0127\3\2\2\2\u0135\u0131\3\2\2\2\u0136"+
		"\u0142\3\2\2\2\u0137\u0139\t\20\2\2\u0138\u013a\t\21\2\2\u0139\u0138\3"+
		"\2\2\2\u0139\u013a\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013f\t\16\2\2\u013c"+
		"\u013e\t\17\2\2\u013d\u013c\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u013d\3"+
		"\2\2\2\u013f\u0140\3\2\2\2\u0140\u0143\3\2\2\2\u0141\u013f\3\2\2\2\u0142"+
		"\u0137\3\2\2\2\u0142\u0143\3\2\2\2\u0143H\3\2\2\2\u0144\u0145\7d\2\2\u0145"+
		"J\3\2\2\2\u0146\u0147\7m\2\2\u0147L\3\2\2\2\u0148\u0149\7o\2\2\u0149N"+
		"\3\2\2\2\u014a\u014e\t\22\2\2\u014b\u014d\t\23\2\2\u014c\u014b\3\2\2\2"+
		"\u014d\u0150\3\2\2\2\u014e\u014c\3\2\2\2\u014e\u014f\3\2\2\2\u014fP\3"+
		"\2\2\2\u0150\u014e\3\2\2\2\u0151\u0152\5O(\2\u0152\u0153\7\60\2\2\u0153"+
		"\u0155\3\2\2\2\u0154\u0151\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u0154\3\2"+
		"\2\2\u0156\u0157\3\2\2\2\u0157\u0159\3\2\2\2\u0158\u0154\3\2\2\2\u0158"+
		"\u0159\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015b\5O(\2\u015bR\3\2\2\2#\2"+
		"V`dlpz\u00cd\u00cf\u00d8\u00da\u00e3\u00e5\u00e9\u00ee\u00fd\u0104\u010b"+
		"\u0111\u0117\u011a\u011f\u0122\u0127\u012e\u0133\u0135\u0139\u013f\u0142"+
		"\u014e\u0156\u0158\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}