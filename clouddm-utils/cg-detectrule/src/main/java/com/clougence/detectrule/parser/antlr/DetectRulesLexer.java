// Generated from DetectRulesLexer.g4 by ANTLR 4.9.2
package com.clougence.detectrule.parser.antlr;

import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

import com.clougence.detectrule.parser.DetectRulesFeature;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class DetectRulesLexer extends DetectRulesBasicLexer {

    public static final int                       DEFINE              = 1, DEFAULT = 2, ENUM = 3, HINT = 4, CAST = 5, CEILING = 6, DOWN = 7, FLOOR = 8, HALF_EVEN = 9,
            HALF_DOWN = 10, HALF_UP = 11, UP = 12, UNNECESSARY = 13, BOOL = 14, DATETIME = 15, DATE = 16, DECIMAL = 17, FLOAT = 18, INTEGER = 19, STRING = 20, TIME = 21,
            INSTANCEOF = 22, TYPEOF = 23, LABEL = 24, GOTO = 25, SWITCH = 26, CASE = 27, FOR = 28, CONTINUE = 29, BREAK = 30, VAR = 31, DEF = 32, FUNC = 33, WS = 34, COMMENT1 = 35,
            COMMENT3 = 36, EOL = 37, IF = 38, THEN = 39, ELSEIF = 40, ELSE = 41, END = 42, FALSE = 43, TRUE = 44, NULL = 45, ASSERT = 46, RETURN = 47, THROW = 48, IN = 49,
            MATCHES = 50, NOT = 51, AS = 52, BO_RSHIFT2 = 53, BO_RSHIFT = 54, CO_GE = 55, CO_GT = 56, BO_LSHIFT = 57, CO_LE = 58, CO_LT = 59, CO_IEQ = 60, CO_EQ = 61, CO_NE = 62,
            LO_NOT = 63, LO_AND = 64, BO_AND = 65, LO_OR = 66, BO_OR = 67, BO_NOT = 68, AO_INCR = 69, AO_PLUS = 70, AO_DECR = 71, AO_MINUS = 72, AO_MUL = 73, AO_DIV = 74,
            AO_MOD = 75, BO_XOR = 76, LBT = 77, RBT = 78, LSBT = 79, RSBT = 80, OCBR = 81, CCBR = 82, OREP = 83, WELL = 84, DOT = 85, COLON = 86, COMMA = 87, REF = 88, QUE = 89,
            ASSIGN = 90, PRI_DECIMAL = 91, PRI_FLOAT = 92, PRI_INTEGER = 93, PRI_XOB_NUM = 94, PRI_STR = 95, PRI_DATETIME = 96, PRI_DATETIME_FMT = 97, IDENTIFIER = 98,
            PRI_IDENTIFIER = 99;
    public static final int                       COMMENT             = 2;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[]   tokenNames;
    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2e\u01e1\b\1\4\2\t"
                                                + "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
                                                + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
                                                + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
                                                + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
                                                + "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"
                                                + ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"
                                                + "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="
                                                + "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"
                                                + "\tI\4J\tJ\4K\tK\3\2\6\2\u0099\n\2\r\2\16\2\u009a\3\2\3\2\3\3\3\3\3\3\3"
                                                + "\3\7\3\u00a3\n\3\f\3\16\3\u00a6\13\3\3\3\5\3\u00a9\n\3\3\3\3\3\3\4\3\4"
                                                + "\3\4\3\4\7\4\u00b1\n\4\f\4\16\4\u00b4\13\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5"
                                                + "\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3"
                                                + "\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3"
                                                + "\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17"
                                                + "\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21"
                                                + "\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24"
                                                + "\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31"
                                                + "\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35"
                                                + "\3\36\3\36\3\36\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3$\3$\3%\3"
                                                + "%\3%\3&\3&\3\'\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3"
                                                + "/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\63\3\64\3\64\3\65\3\65\3\66"
                                                + "\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3;\3;\3;\5;\u016b\n;\3;\3;\3;"
                                                + "\3;\3;\3;\3;\5;\u0174\n;\3<\5<\u0177\n<\3<\3<\3<\3=\3=\3>\3>\3>\5>\u0181"
                                                + "\n>\3?\6?\u0184\n?\r?\16?\u0185\3@\3@\5@\u018a\n@\3@\3@\3A\3A\3A\6A\u0191"
                                                + "\nA\rA\16A\u0192\3B\3B\3B\6B\u0198\nB\rB\16B\u0199\3C\3C\3C\6C\u019f\n"
                                                + "C\rC\16C\u01a0\3D\3D\3D\7D\u01a6\nD\fD\16D\u01a9\13D\3D\3D\3D\3D\7D\u01af"
                                                + "\nD\fD\16D\u01b2\13D\3D\5D\u01b5\nD\3E\3E\3E\5E\u01ba\nE\3F\3F\3F\3F\3"
                                                + "F\3F\3G\3G\3H\3H\3H\3I\3I\3I\3I\3I\3I\3J\3J\7J\u01cf\nJ\fJ\16J\u01d2\13"
                                                + "J\3J\3J\3K\3K\3K\3K\3K\7K\u01db\nK\fK\16K\u01de\13K\3K\3K\3\u00b2\2L\3"
                                                + "$\5%\7&\t\'\13(\r)\17*\21+\23,\25-\27.\31/\33\60\35\61\37\62!\63#\64%"
                                                + "\65\'\66)\67+8-9/:\61;\63<\65=\67>9?;@=A?BACCDEEGFIGKHMIOJQKSLUMWNYO["
                                                + "P]Q_RaScTeUgViWkXmYoZq[s\\u]w^y_{`}\2\177\2\u0081\2\u0083\2\u0085\2\u0087"
                                                + "a\u0089\2\u008b\2\u008d\2\u008fb\u0091c\u0093d\u0095e\3\2\"\5\2\13\f\16"
                                                + "\17\"\"\4\2\f\f\17\17\4\2\f\f\16\17\4\2KKkk\4\2HHhh\4\2VVvv\4\2JJjj\4"
                                                + "\2GGgg\4\2PPpp\4\2NNnn\4\2UUuu\4\2FFff\4\2CCcc\4\2TTtt\4\2WWww\4\2QQq"
                                                + "q\4\2YYyy\4\2OOoo\4\2EEee\3\2\62;\4\2--//\4\2ZZzz\5\2\62;CHch\3\2\629"
                                                + "\4\2DDdd\3\2\62\63\5\2\f\f\17\17$$\5\2\f\f\17\17))\f\2$$))\61\61^^ddh"
                                                + "hppttvvxx\5\2C\\aac|\6\2\62;C\\aac|\5\2\f\f\17\17bb\2\u01f1\2\3\3\2\2"
                                                + "\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"
                                                + "\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"
                                                + "\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"
                                                + "\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"
                                                + "\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3"
                                                + "\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"
                                                + "\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2"
                                                + "W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3"
                                                + "\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2"
                                                + "\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2"
                                                + "\u0087\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095"
                                                + "\3\2\2\2\3\u0098\3\2\2\2\5\u009e\3\2\2\2\7\u00ac\3\2\2\2\t\u00ba\3\2\2"
                                                + "\2\13\u00bc\3\2\2\2\r\u00bf\3\2\2\2\17\u00c4\3\2\2\2\21\u00cb\3\2\2\2"
                                                + "\23\u00d0\3\2\2\2\25\u00d4\3\2\2\2\27\u00da\3\2\2\2\31\u00df\3\2\2\2\33"
                                                + "\u00e4\3\2\2\2\35\u00eb\3\2\2\2\37\u00f2\3\2\2\2!\u00f8\3\2\2\2#\u00fb"
                                                + "\3\2\2\2%\u0103\3\2\2\2\'\u0107\3\2\2\2)\u010a\3\2\2\2+\u010e\3\2\2\2"
                                                + "-\u0111\3\2\2\2/\u0114\3\2\2\2\61\u0116\3\2\2\2\63\u0119\3\2\2\2\65\u011c"
                                                + "\3\2\2\2\67\u011e\3\2\2\29\u0122\3\2\2\2;\u0125\3\2\2\2=\u0128\3\2\2\2"
                                                + "?\u012a\3\2\2\2A\u012d\3\2\2\2C\u012f\3\2\2\2E\u0132\3\2\2\2G\u0134\3"
                                                + "\2\2\2I\u0136\3\2\2\2K\u0139\3\2\2\2M\u013b\3\2\2\2O\u013e\3\2\2\2Q\u0140"
                                                + "\3\2\2\2S\u0142\3\2\2\2U\u0144\3\2\2\2W\u0146\3\2\2\2Y\u0148\3\2\2\2["
                                                + "\u014a\3\2\2\2]\u014c\3\2\2\2_\u014e\3\2\2\2a\u0150\3\2\2\2c\u0152\3\2"
                                                + "\2\2e\u0154\3\2\2\2g\u0157\3\2\2\2i\u0159\3\2\2\2k\u015b\3\2\2\2m\u015d"
                                                + "\3\2\2\2o\u015f\3\2\2\2q\u0161\3\2\2\2s\u0163\3\2\2\2u\u0173\3\2\2\2w"
                                                + "\u0176\3\2\2\2y\u017b\3\2\2\2{\u0180\3\2\2\2}\u0183\3\2\2\2\177\u0187"
                                                + "\3\2\2\2\u0081\u018d\3\2\2\2\u0083\u0194\3\2\2\2\u0085\u019b\3\2\2\2\u0087"
                                                + "\u01b4\3\2\2\2\u0089\u01b6\3\2\2\2\u008b\u01bb\3\2\2\2\u008d\u01c1\3\2"
                                                + "\2\2\u008f\u01c3\3\2\2\2\u0091\u01c6\3\2\2\2\u0093\u01cc\3\2\2\2\u0095"
                                                + "\u01d5\3\2\2\2\u0097\u0099\t\2\2\2\u0098\u0097\3\2\2\2\u0099\u009a\3\2"
                                                + "\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\3\2\2\2\u009c"
                                                + "\u009d\b\2\2\2\u009d\4\3\2\2\2\u009e\u009f\7\61\2\2\u009f\u00a0\7\61\2"
                                                + "\2\u00a0\u00a4\3\2\2\2\u00a1\u00a3\n\3\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a6"
                                                + "\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6"
                                                + "\u00a4\3\2\2\2\u00a7\u00a9\5\t\5\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2"
                                                + "\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\b\3\3\2\u00ab\6\3\2\2\2\u00ac\u00ad"
                                                + "\7\61\2\2\u00ad\u00ae\7,\2\2\u00ae\u00b2\3\2\2\2\u00af\u00b1\13\2\2\2"
                                                + "\u00b0\u00af\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b2\u00b0"
                                                + "\3\2\2\2\u00b3\u00b5\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b6\7,\2\2\u00b6"
                                                + "\u00b7\7\61\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b9\b\4\3\2\u00b9\b\3\2\2"
                                                + "\2\u00ba\u00bb\t\4\2\2\u00bb\n\3\2\2\2\u00bc\u00bd\t\5\2\2\u00bd\u00be"
                                                + "\t\6\2\2\u00be\f\3\2\2\2\u00bf\u00c0\t\7\2\2\u00c0\u00c1\t\b\2\2\u00c1"
                                                + "\u00c2\t\t\2\2\u00c2\u00c3\t\n\2\2\u00c3\16\3\2\2\2\u00c4\u00c5\t\t\2"
                                                + "\2\u00c5\u00c6\t\13\2\2\u00c6\u00c7\t\f\2\2\u00c7\u00c8\t\t\2\2\u00c8"
                                                + "\u00c9\t\5\2\2\u00c9\u00ca\t\6\2\2\u00ca\20\3\2\2\2\u00cb\u00cc\t\t\2"
                                                + "\2\u00cc\u00cd\t\13\2\2\u00cd\u00ce\t\f\2\2\u00ce\u00cf\t\t\2\2\u00cf"
                                                + "\22\3\2\2\2\u00d0\u00d1\t\t\2\2\u00d1\u00d2\t\n\2\2\u00d2\u00d3\t\r\2"
                                                + "\2\u00d3\24\3\2\2\2\u00d4\u00d5\t\6\2\2\u00d5\u00d6\t\16\2\2\u00d6\u00d7"
                                                + "\t\13\2\2\u00d7\u00d8\t\f\2\2\u00d8\u00d9\t\t\2\2\u00d9\26\3\2\2\2\u00da"
                                                + "\u00db\t\7\2\2\u00db\u00dc\t\17\2\2\u00dc\u00dd\t\20\2\2\u00dd\u00de\t"
                                                + "\t\2\2\u00de\30\3\2\2\2\u00df\u00e0\t\n\2\2\u00e0\u00e1\t\20\2\2\u00e1"
                                                + "\u00e2\t\13\2\2\u00e2\u00e3\t\13\2\2\u00e3\32\3\2\2\2\u00e4\u00e5\t\16"
                                                + "\2\2\u00e5\u00e6\t\f\2\2\u00e6\u00e7\t\f\2\2\u00e7\u00e8\t\t\2\2\u00e8"
                                                + "\u00e9\t\17\2\2\u00e9\u00ea\t\7\2\2\u00ea\34\3\2\2\2\u00eb\u00ec\t\17"
                                                + "\2\2\u00ec\u00ed\t\t\2\2\u00ed\u00ee\t\7\2\2\u00ee\u00ef\t\20\2\2\u00ef"
                                                + "\u00f0\t\17\2\2\u00f0\u00f1\t\n\2\2\u00f1\36\3\2\2\2\u00f2\u00f3\t\7\2"
                                                + "\2\u00f3\u00f4\t\b\2\2\u00f4\u00f5\t\17\2\2\u00f5\u00f6\t\21\2\2\u00f6"
                                                + "\u00f7\t\22\2\2\u00f7 \3\2\2\2\u00f8\u00f9\t\5\2\2\u00f9\u00fa\t\n\2\2"
                                                + "\u00fa\"\3\2\2\2\u00fb\u00fc\t\23\2\2\u00fc\u00fd\t\16\2\2\u00fd\u00fe"
                                                + "\t\7\2\2\u00fe\u00ff\t\24\2\2\u00ff\u0100\t\b\2\2\u0100\u0101\t\t\2\2"
                                                + "\u0101\u0102\t\f\2\2\u0102$\3\2\2\2\u0103\u0104\t\n\2\2\u0104\u0105\t"
                                                + "\21\2\2\u0105\u0106\t\7\2\2\u0106&\3\2\2\2\u0107\u0108\t\16\2\2\u0108"
                                                + "\u0109\t\f\2\2\u0109(\3\2\2\2\u010a\u010b\7@\2\2\u010b\u010c\7@\2\2\u010c"
                                                + "\u010d\7@\2\2\u010d*\3\2\2\2\u010e\u010f\7@\2\2\u010f\u0110\7@\2\2\u0110"
                                                + ",\3\2\2\2\u0111\u0112\7@\2\2\u0112\u0113\7?\2\2\u0113.\3\2\2\2\u0114\u0115"
                                                + "\7@\2\2\u0115\60\3\2\2\2\u0116\u0117\7>\2\2\u0117\u0118\7>\2\2\u0118\62"
                                                + "\3\2\2\2\u0119\u011a\7>\2\2\u011a\u011b\7?\2\2\u011b\64\3\2\2\2\u011c"
                                                + "\u011d\7>\2\2\u011d\66\3\2\2\2\u011e\u011f\7?\2\2\u011f\u0120\7?\2\2\u0120"
                                                + "\u0121\7?\2\2\u01218\3\2\2\2\u0122\u0123\7?\2\2\u0123\u0124\7?\2\2\u0124"
                                                + ":\3\2\2\2\u0125\u0126\7#\2\2\u0126\u0127\7?\2\2\u0127<\3\2\2\2\u0128\u0129"
                                                + "\7#\2\2\u0129>\3\2\2\2\u012a\u012b\7(\2\2\u012b\u012c\7(\2\2\u012c@\3"
                                                + "\2\2\2\u012d\u012e\7(\2\2\u012eB\3\2\2\2\u012f\u0130\7~\2\2\u0130\u0131"
                                                + "\7~\2\2\u0131D\3\2\2\2\u0132\u0133\7~\2\2\u0133F\3\2\2\2\u0134\u0135\7"
                                                + "\u0080\2\2\u0135H\3\2\2\2\u0136\u0137\7-\2\2\u0137\u0138\7-\2\2\u0138"
                                                + "J\3\2\2\2\u0139\u013a\7-\2\2\u013aL\3\2\2\2\u013b\u013c\7/\2\2\u013c\u013d"
                                                + "\7/\2\2\u013dN\3\2\2\2\u013e\u013f\7/\2\2\u013fP\3\2\2\2\u0140\u0141\7"
                                                + ",\2\2\u0141R\3\2\2\2\u0142\u0143\7\61\2\2\u0143T\3\2\2\2\u0144\u0145\7"
                                                + "\'\2\2\u0145V\3\2\2\2\u0146\u0147\7`\2\2\u0147X\3\2\2\2\u0148\u0149\7"
                                                + "*\2\2\u0149Z\3\2\2\2\u014a\u014b\7+\2\2\u014b\\\3\2\2\2\u014c\u014d\7"
                                                + "]\2\2\u014d^\3\2\2\2\u014e\u014f\7_\2\2\u014f`\3\2\2\2\u0150\u0151\7}"
                                                + "\2\2\u0151b\3\2\2\2\u0152\u0153\7\177\2\2\u0153d\3\2\2\2\u0154\u0155\7"
                                                + "%\2\2\u0155\u0156\7}\2\2\u0156f\3\2\2\2\u0157\u0158\7%\2\2\u0158h\3\2"
                                                + "\2\2\u0159\u015a\7\60\2\2\u015aj\3\2\2\2\u015b\u015c\7<\2\2\u015cl\3\2"
                                                + "\2\2\u015d\u015e\7.\2\2\u015en\3\2\2\2\u015f\u0160\7B\2\2\u0160p\3\2\2"
                                                + "\2\u0161\u0162\7A\2\2\u0162r\3\2\2\2\u0163\u0164\7?\2\2\u0164t\3\2\2\2"
                                                + "\u0165\u0166\5}?\2\u0166\u0167\7\60\2\2\u0167\u0168\5\177@\2\u0168\u0174"
                                                + "\3\2\2\2\u0169\u016b\5}?\2\u016a\u0169\3\2\2\2\u016a\u016b\3\2\2\2\u016b"
                                                + "\u016c\3\2\2\2\u016c\u016d\7\60\2\2\u016d\u016e\5}?\2\u016e\u016f\5\177"
                                                + "@\2\u016f\u0174\3\2\2\2\u0170\u0171\5}?\2\u0171\u0172\5\177@\2\u0172\u0174"
                                                + "\3\2\2\2\u0173\u0165\3\2\2\2\u0173\u016a\3\2\2\2\u0173\u0170\3\2\2\2\u0174"
                                                + "v\3\2\2\2\u0175\u0177\5}?\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177"
                                                + "\u0178\3\2\2\2\u0178\u0179\7\60\2\2\u0179\u017a\5}?\2\u017ax\3\2\2\2\u017b"
                                                + "\u017c\5}?\2\u017cz\3\2\2\2\u017d\u0181\5\u0081A\2\u017e\u0181\5\u0083"
                                                + "B\2\u017f\u0181\5\u0085C\2\u0180\u017d\3\2\2\2\u0180\u017e\3\2\2\2\u0180"
                                                + "\u017f\3\2\2\2\u0181|\3\2\2\2\u0182\u0184\t\25\2\2\u0183\u0182\3\2\2\2"
                                                + "\u0184\u0185\3\2\2\2\u0185\u0183\3\2\2\2\u0185\u0186\3\2\2\2\u0186~\3"
                                                + "\2\2\2\u0187\u0189\t\t\2\2\u0188\u018a\t\26\2\2\u0189\u0188\3\2\2\2\u0189"
                                                + "\u018a\3\2\2\2\u018a\u018b\3\2\2\2\u018b\u018c\5}?\2\u018c\u0080\3\2\2"
                                                + "\2\u018d\u018e\7\62\2\2\u018e\u0190\t\27\2\2\u018f\u0191\t\30\2\2\u0190"
                                                + "\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2"
                                                + "\2\2\u0193\u0082\3\2\2\2\u0194\u0195\7\62\2\2\u0195\u0197\t\21\2\2\u0196"
                                                + "\u0198\t\31\2\2\u0197\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u0197\3"
                                                + "\2\2\2\u0199\u019a\3\2\2\2\u019a\u0084\3\2\2\2\u019b\u019c\7\62\2\2\u019c"
                                                + "\u019e\t\32\2\2\u019d\u019f\t\33\2\2\u019e\u019d\3\2\2\2\u019f\u01a0\3"
                                                + "\2\2\2\u01a0\u019e\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1\u0086\3\2\2\2\u01a2"
                                                + "\u01a7\7$\2\2\u01a3\u01a6\n\34\2\2\u01a4\u01a6\5\u0089E\2\u01a5\u01a3"
                                                + "\3\2\2\2\u01a5\u01a4\3\2\2\2\u01a6\u01a9\3\2\2\2\u01a7\u01a5\3\2\2\2\u01a7"
                                                + "\u01a8\3\2\2\2\u01a8\u01aa\3\2\2\2\u01a9\u01a7\3\2\2\2\u01aa\u01b5\7$"
                                                + "\2\2\u01ab\u01b0\7)\2\2\u01ac\u01af\n\35\2\2\u01ad\u01af\5\u0089E\2\u01ae"
                                                + "\u01ac\3\2\2\2\u01ae\u01ad\3\2\2\2\u01af\u01b2\3\2\2\2\u01b0\u01ae\3\2"
                                                + "\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3\3\2\2\2\u01b2\u01b0\3\2\2\2\u01b3"
                                                + "\u01b5\7)\2\2\u01b4\u01a2\3\2\2\2\u01b4\u01ab\3\2\2\2\u01b5\u0088\3\2"
                                                + "\2\2\u01b6\u01b9\7^\2\2\u01b7\u01ba\t\36\2\2\u01b8\u01ba\5\u008bF\2\u01b9"
                                                + "\u01b7\3\2\2\2\u01b9\u01b8\3\2\2\2\u01ba\u008a\3\2\2\2\u01bb\u01bc\7w"
                                                + "\2\2\u01bc\u01bd\5\u008dG\2\u01bd\u01be\5\u008dG\2\u01be\u01bf\5\u008d"
                                                + "G\2\u01bf\u01c0\5\u008dG\2\u01c0\u008c\3\2\2\2\u01c1\u01c2\t\30\2\2\u01c2"
                                                + "\u008e\3\2\2\2\u01c3\u01c4\7V\2\2\u01c4\u01c5\5\u0087D\2\u01c5\u0090\3"
                                                + "\2\2\2\u01c6\u01c7\7V\2\2\u01c7\u01c8\7*\2\2\u01c8\u01c9\3\2\2\2\u01c9"
                                                + "\u01ca\5\u0087D\2\u01ca\u01cb\7+\2\2\u01cb\u0092\3\2\2\2\u01cc\u01d0\t"
                                                + "\37\2\2\u01cd\u01cf\t \2\2\u01ce\u01cd\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0"
                                                + "\u01ce\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d3\3\2\2\2\u01d2\u01d0\3\2"
                                                + "\2\2\u01d3\u01d4\bJ\4\2\u01d4\u0094\3\2\2\2\u01d5\u01dc\7b\2\2\u01d6\u01db"
                                                + "\n!\2\2\u01d7\u01d8\7^\2\2\u01d8\u01db\7b\2\2\u01d9\u01db\5\u0089E\2\u01da"
                                                + "\u01d6\3\2\2\2\u01da\u01d7\3\2\2\2\u01da\u01d9\3\2\2\2\u01db\u01de\3\2"
                                                + "\2\2\u01dc\u01da\3\2\2\2\u01dc\u01dd\3\2\2\2\u01dd\u01df\3\2\2\2\u01de"
                                                + "\u01dc\3\2\2\2\u01df\u01e0\7b\2\2\u01e0\u0096\3\2\2\2\31\2\u009a\u00a4"
                                                + "\u00a8\u00b2\u016a\u0173\u0176\u0180\u0185\u0189\u0192\u0199\u01a0\u01a5"
                                                + "\u01a7\u01ae\u01b0\u01b4\u01b9\u01d0\u01da\u01dc\5\b\2\2\2\4\2\3J\2";
    public static final ATN    _ATN           = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[]                  _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[]  _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY      = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    public static String[]                        channelNames        = { "DEFAULT_TOKEN_CHANNEL", "HIDDEN", "COMMENT" };
    public static String[]                        modeNames           = { "DEFAULT_MODE" };

    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

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

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }

    public DetectRulesLexer(CharStream input, List<DetectRulesFeature> features){
        this(input);
        super.initFeatures(features);
    }

    public DetectRulesLexer(CharStream input){
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[] { "WS", "COMMENT1", "COMMENT3", "EOL", "IF", "THEN", "ELSEIF", "ELSE", "END", "FALSE", "TRUE", "NULL", "ASSERT", "RETURN", "THROW", "IN", "MATCHES",
                              "NOT", "AS", "BO_RSHIFT2", "BO_RSHIFT", "CO_GE", "CO_GT", "BO_LSHIFT", "CO_LE", "CO_LT", "CO_IEQ", "CO_EQ", "CO_NE", "LO_NOT", "LO_AND", "BO_AND",
                              "LO_OR", "BO_OR", "BO_NOT", "AO_INCR", "AO_PLUS", "AO_DECR", "AO_MINUS", "AO_MUL", "AO_DIV", "AO_MOD", "BO_XOR", "LBT", "RBT", "LSBT", "RSBT", "OCBR",
                              "CCBR", "OREP", "WELL", "DOT", "COLON", "COMMA", "REF", "QUE", "ASSIGN", "PRI_DECIMAL", "PRI_FLOAT", "PRI_INTEGER", "PRI_XOB_NUM", "DIGIT",
                              "EXPONENT_DIGIT", "HEX_NOTATION", "OCT_NOTATION", "BIT_NOTATION", "PRI_STR", "TRANS", "UNICODE", "HEX", "PRI_DATETIME", "PRI_DATETIME_FMT",
                              "IDENTIFIER", "PRI_IDENTIFIER" };
    }

    private static String[] makeLiteralNames() {
        return new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                              null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                              null, null, null, "'>>>'", "'>>'", "'>='", "'>'", "'<<'", "'<='", "'<'", "'==='", "'=='", "'!='", "'!'", "'&&'", "'&'", "'||'", "'|'", "'~'", "'++'",
                              "'+'", "'--'", "'-'", "'*'", "'/'", "'%'", "'^'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'#{'", "'#'", "'.'", "':'", "','", "'@'", "'?'", "'='" };
    }

    private static String[] makeSymbolicNames() {
        return new String[] { null, "DEFINE", "DEFAULT", "ENUM", "HINT", "CAST", "CEILING", "DOWN", "FLOOR", "HALF_EVEN", "HALF_DOWN", "HALF_UP", "UP", "UNNECESSARY", "BOOL",
                              "DATETIME", "DATE", "DECIMAL", "FLOAT", "INTEGER", "STRING", "TIME", "INSTANCEOF", "TYPEOF", "LABEL", "GOTO", "SWITCH", "CASE", "FOR", "CONTINUE",
                              "BREAK", "VAR", "DEF", "FUNC", "WS", "COMMENT1", "COMMENT3", "EOL", "IF", "THEN", "ELSEIF", "ELSE", "END", "FALSE", "TRUE", "NULL", "ASSERT",
                              "RETURN", "THROW", "IN", "MATCHES", "NOT", "AS", "BO_RSHIFT2", "BO_RSHIFT", "CO_GE", "CO_GT", "BO_LSHIFT", "CO_LE", "CO_LT", "CO_IEQ", "CO_EQ",
                              "CO_NE", "LO_NOT", "LO_AND", "BO_AND", "LO_OR", "BO_OR", "BO_NOT", "AO_INCR", "AO_PLUS", "AO_DECR", "AO_MINUS", "AO_MUL", "AO_DIV", "AO_MOD",
                              "BO_XOR", "LBT", "RBT", "LSBT", "RSBT", "OCBR", "CCBR", "OREP", "WELL", "DOT", "COLON", "COMMA", "REF", "QUE", "ASSIGN", "PRI_DECIMAL", "PRI_FLOAT",
                              "PRI_INTEGER", "PRI_XOB_NUM", "PRI_STR", "PRI_DATETIME", "PRI_DATETIME_FMT", "IDENTIFIER", "PRI_IDENTIFIER" };
    }

    @Override
    @Deprecated
    public String[] getTokenNames() { return tokenNames; }

    @Override

    public Vocabulary getVocabulary() { return VOCABULARY; }

    @Override
    public String getGrammarFileName() { return "DetectRulesLexer.g4"; }

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

    @Override
    public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
        switch (ruleIndex) {
            case 72:
                IDENTIFIER_action((RuleContext) _localctx, actionIndex);
                break;
        }
    }

    private void IDENTIFIER_action(RuleContext _localctx, int actionIndex) {
        switch (actionIndex) {
            case 0:

                String upStr = getText().toUpperCase();
                if (keyWords.containsKey(upStr)) {
                    setType(keyWords.get(upStr));
                }

                break;
        }
    }
}
