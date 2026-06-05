/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.detectrule.parser.antlr;

import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.detectrule.parser.DetectRulesFeature;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class DetectRulesParser extends DetectRulesBasicParser {

    public static final int                       DEFINE              = 1, DEFAULT = 2, ENUM = 3, HINT = 4, CAST = 5, CEILING = 6, DOWN = 7, FLOOR = 8, HALF_EVEN = 9,
            HALF_DOWN = 10, HALF_UP = 11, UP = 12, UNNECESSARY = 13, BOOL = 14, DATETIME = 15, DATE = 16, DECIMAL = 17, FLOAT = 18, INTEGER = 19, STRING = 20, TIME = 21,
            INSTANCEOF = 22, TYPEOF = 23, LABEL = 24, GOTO = 25, SWITCH = 26, CASE = 27, FOR = 28, CONTINUE = 29, BREAK = 30, VAR = 31, DEF = 32, FUNC = 33, WS = 34, COMMENT1 = 35,
            COMMENT3 = 36, EOL = 37, IF = 38, THEN = 39, ELSEIF = 40, ELSE = 41, END = 42, FALSE = 43, TRUE = 44, NULL = 45, ASSERT = 46, RETURN = 47, THROW = 48, IN = 49,
            MATCHES = 50, NOT = 51, AS = 52, BO_RSHIFT2 = 53, BO_RSHIFT = 54, CO_GE = 55, CO_GT = 56, BO_LSHIFT = 57, CO_LE = 58, CO_LT = 59, CO_IEQ = 60, CO_EQ = 61, CO_NE = 62,
            LO_NOT = 63, LO_AND = 64, BO_AND = 65, LO_OR = 66, BO_OR = 67, BO_NOT = 68, AO_INCR = 69, AO_PLUS = 70, AO_DECR = 71, AO_MINUS = 72, AO_MUL = 73, AO_DIV = 74,
            AO_MOD = 75, BO_XOR = 76, LBT = 77, RBT = 78, LSBT = 79, RSBT = 80, OCBR = 81, CCBR = 82, OREP = 83, WELL = 84, DOT = 85, COLON = 86, COMMA = 87, REF = 88, QUE = 89,
            ASSIGN = 90, PRI_DECIMAL = 91, PRI_FLOAT = 92, PRI_INTEGER = 93, PRI_XOB_NUM = 94, PRI_STR = 95, PRI_DATETIME = 96, PRI_DATETIME_FMT = 97, IDENTIFIER = 98,
            PRI_IDENTIFIER = 99;
    public static final int                       RULE_root           = 0, RULE_comment = 1, RULE_multiStatement = 2, RULE_singleStatement = 3, RULE_defineStatement = 4,
            RULE_defineType = 5, RULE_defineDefault = 6, RULE_defineEnum = 7, RULE_defineHINT = 8, RULE_ifStatement = 9, RULE_conditionThen = 10, RULE_elseifThen = 11,
            RULE_elseThen = 12, RULE_returnStatement = 13, RULE_throwStatement = 14, RULE_assertStatement = 15, RULE_assertExpr = 16, RULE_assignStatement = 17,
            RULE_expression = 18, RULE_unarySymbol = 19, RULE_multiplicativeSymbol = 20, RULE_additiveSymbol = 21, RULE_bitwiseSymbol = 22, RULE_shiftSymbol = 23,
            RULE_compareSymbol = 24, RULE_matchSymbol = 25, RULE_paramExpr = 26, RULE_funOrDetectExpr = 27, RULE_detectPart = 28, RULE_funcPart = 29, RULE_funcArgPart = 30,
            RULE_identifyExpr = 31, RULE_subExpr = 32, RULE_identifyPart = 33, RULE_identify = 34, RULE_anyPrimary = 35, RULE_primitiveValue = 36, RULE_signSymbol = 37,
            RULE_numValue = 38, RULE_dtValue = 39, RULE_collectionValue = 40, RULE_listValue = 41, RULE_objectValue = 42, RULE_objectItem = 43, RULE_castExpr = 44,
            RULE_dataType = 45, RULE_numFmtValue = 46, RULE_numFmtRounding = 47, RULE_allIdentifierKeywords = 48;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[]   tokenNames;
    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3e\u01dd\4\2\t\2\4"
                                                + "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
                                                + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
                                                + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
                                                + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
                                                + "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"
                                                + ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\3\2\7\2f\n\2\f\2"
                                                + "\16\2i\13\2\3\2\5\2l\n\2\3\2\3\2\3\3\3\3\3\4\6\4s\n\4\r\4\16\4t\3\5\3"
                                                + "\5\3\5\3\5\3\5\5\5|\n\5\3\6\3\6\3\6\3\6\3\6\5\6\u0083\n\6\3\6\5\6\u0086"
                                                + "\n\6\3\6\5\6\u0089\n\6\3\6\5\6\u008c\n\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3"
                                                + "\t\3\t\3\t\7\t\u0098\n\t\f\t\16\t\u009b\13\t\5\t\u009d\n\t\3\t\3\t\3\n"
                                                + "\3\n\3\n\3\13\3\13\7\13\u00a6\n\13\f\13\16\13\u00a9\13\13\3\13\5\13\u00ac"
                                                + "\n\13\3\13\3\13\3\f\3\f\3\f\3\f\5\f\u00b4\n\f\3\r\3\r\3\r\3\r\5\r\u00ba"
                                                + "\n\r\3\16\3\16\5\16\u00be\n\16\3\17\3\17\5\17\u00c2\n\17\3\20\3\20\3\20"
                                                + "\3\21\3\21\3\21\5\21\u00ca\n\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24"
                                                + "\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"
                                                + "\3\24\3\24\5\24\u00e4\n\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"
                                                + "\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24"
                                                + "\u00fc\n\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"
                                                + "\3\24\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u0111\n\24\f\24\16\24\u0114\13"
                                                + "\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3"
                                                + "\33\3\34\3\34\3\34\5\34\u0127\n\34\3\34\3\34\3\35\3\35\5\35\u012d\n\35"
                                                + "\3\36\3\36\3\36\3\37\3\37\5\37\u0134\n\37\3\37\3\37\3\37\3\37\5\37\u013a"
                                                + "\n\37\3 \3 \3 \7 \u013f\n \f \16 \u0142\13 \3!\3!\5!\u0146\n!\3\"\3\""
                                                + "\3\"\3\"\3\"\3\"\5\"\u014e\n\"\3#\3#\3#\7#\u0153\n#\f#\16#\u0156\13#\3"
                                                + "$\3$\3$\5$\u015b\n$\3%\3%\3%\3%\3%\5%\u0162\n%\3&\3&\5&\u0166\n&\3&\3"
                                                + "&\3&\3&\3&\5&\u016d\n&\3\'\3\'\3(\3(\3(\3(\5(\u0175\n(\3(\5(\u0178\n("
                                                + "\3)\3)\3)\5)\u017d\n)\3*\3*\5*\u0181\n*\3+\3+\3+\3+\7+\u0187\n+\f+\16"
                                                + "+\u018a\13+\5+\u018c\n+\3+\3+\3,\3,\3,\3,\7,\u0194\n,\f,\16,\u0197\13"
                                                + ",\5,\u0199\n,\3,\3,\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3/\3/\5/\u01aa\n"
                                                + "/\3/\3/\5/\u01ae\n/\3\60\3\60\3\60\3\60\5\60\u01b4\n\60\3\60\3\60\3\61"
                                                + "\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62"
                                                + "\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62"
                                                + "\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u01db\n\62\3\62\2\3&\63\2\4\6\b\n"
                                                + "\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\"
                                                + "^`b\2\20\3\2%&\3\2\20\27\4\2AAFF\5\2\65\65HHJJ\3\2KM\4\2HHJJ\5\2CCEEN"
                                                + "N\4\2\678;;\4\29:<@\3\2\63\64\3\2-.\3\2\23\25\4\2\20\22\26\27\3\2\b\17"
                                                + "\2\u0214\2g\3\2\2\2\4o\3\2\2\2\6r\3\2\2\2\b{\3\2\2\2\n}\3\2\2\2\f\u008d"
                                                + "\3\2\2\2\16\u008f\3\2\2\2\20\u0092\3\2\2\2\22\u00a0\3\2\2\2\24\u00a3\3"
                                                + "\2\2\2\26\u00af\3\2\2\2\30\u00b5\3\2\2\2\32\u00bb\3\2\2\2\34\u00bf\3\2"
                                                + "\2\2\36\u00c3\3\2\2\2 \u00c6\3\2\2\2\"\u00cb\3\2\2\2$\u00ce\3\2\2\2&\u00e3"
                                                + "\3\2\2\2(\u0115\3\2\2\2*\u0117\3\2\2\2,\u0119\3\2\2\2.\u011b\3\2\2\2\60"
                                                + "\u011d\3\2\2\2\62\u011f\3\2\2\2\64\u0121\3\2\2\2\66\u0123\3\2\2\28\u012a"
                                                + "\3\2\2\2:\u012e\3\2\2\2<\u0131\3\2\2\2>\u013b\3\2\2\2@\u0143\3\2\2\2B"
                                                + "\u0147\3\2\2\2D\u014f\3\2\2\2F\u015a\3\2\2\2H\u0161\3\2\2\2J\u016c\3\2"
                                                + "\2\2L\u016e\3\2\2\2N\u0177\3\2\2\2P\u017c\3\2\2\2R\u0180\3\2\2\2T\u0182"
                                                + "\3\2\2\2V\u018f\3\2\2\2X\u019c\3\2\2\2Z\u01a0\3\2\2\2\\\u01ad\3\2\2\2"
                                                + "^\u01af\3\2\2\2`\u01b7\3\2\2\2b\u01da\3\2\2\2df\5\n\6\2ed\3\2\2\2fi\3"
                                                + "\2\2\2ge\3\2\2\2gh\3\2\2\2hk\3\2\2\2ig\3\2\2\2jl\5\6\4\2kj\3\2\2\2kl\3"
                                                + "\2\2\2lm\3\2\2\2mn\7\2\2\3n\3\3\2\2\2op\t\2\2\2p\5\3\2\2\2qs\5\b\5\2r"
                                                + "q\3\2\2\2st\3\2\2\2tr\3\2\2\2tu\3\2\2\2u\7\3\2\2\2v|\5\24\13\2w|\5\34"
                                                + "\17\2x|\5\36\20\2y|\5 \21\2z|\5$\23\2{v\3\2\2\2{w\3\2\2\2{x\3\2\2\2{y"
                                                + "\3\2\2\2{z\3\2\2\2|\t\3\2\2\2}~\7V\2\2~\177\7\3\2\2\177\u0082\7a\2\2\u0080"
                                                + "\u0081\7\66\2\2\u0081\u0083\5\f\7\2\u0082\u0080\3\2\2\2\u0082\u0083\3"
                                                + "\2\2\2\u0083\u0085\3\2\2\2\u0084\u0086\5\16\b\2\u0085\u0084\3\2\2\2\u0085"
                                                + "\u0086\3\2\2\2\u0086\u0088\3\2\2\2\u0087\u0089\5\20\t\2\u0088\u0087\3"
                                                + "\2\2\2\u0088\u0089\3\2\2\2\u0089\u008b\3\2\2\2\u008a\u008c\5\22\n\2\u008b"
                                                + "\u008a\3\2\2\2\u008b\u008c\3\2\2\2\u008c\13\3\2\2\2\u008d\u008e\t\3\2"
                                                + "\2\u008e\r\3\2\2\2\u008f\u0090\7\4\2\2\u0090\u0091\7a\2\2\u0091\17\3\2"
                                                + "\2\2\u0092\u0093\7\5\2\2\u0093\u009c\7Q\2\2\u0094\u0099\7a\2\2\u0095\u0096"
                                                + "\7Y\2\2\u0096\u0098\7a\2\2\u0097\u0095\3\2\2\2\u0098\u009b\3\2\2\2\u0099"
                                                + "\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2"
                                                + "\2\2\u009c\u0094\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e\3\2\2\2\u009e"
                                                + "\u009f\7R\2\2\u009f\21\3\2\2\2\u00a0\u00a1\7\6\2\2\u00a1\u00a2\7a\2\2"
                                                + "\u00a2\23\3\2\2\2\u00a3\u00a7\5\26\f\2\u00a4\u00a6\5\30\r\2\u00a5\u00a4"
                                                + "\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8"
                                                + "\u00ab\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00ac\5\32\16\2\u00ab\u00aa\3"
                                                + "\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\7,\2\2\u00ae"
                                                + "\25\3\2\2\2\u00af\u00b0\7(\2\2\u00b0\u00b1\5&\24\2\u00b1\u00b3\7)\2\2"
                                                + "\u00b2\u00b4\5\6\4\2\u00b3\u00b2\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\27"
                                                + "\3\2\2\2\u00b5\u00b6\7*\2\2\u00b6\u00b7\5&\24\2\u00b7\u00b9\7)\2\2\u00b8"
                                                + "\u00ba\5\6\4\2\u00b9\u00b8\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\31\3\2\2"
                                                + "\2\u00bb\u00bd\7+\2\2\u00bc\u00be\5\6\4\2\u00bd\u00bc\3\2\2\2\u00bd\u00be"
                                                + "\3\2\2\2\u00be\33\3\2\2\2\u00bf\u00c1\7\61\2\2\u00c0\u00c2\5&\24\2\u00c1"
                                                + "\u00c0\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\35\3\2\2\2\u00c3\u00c4\7\62\2"
                                                + "\2\u00c4\u00c5\5&\24\2\u00c5\37\3\2\2\2\u00c6\u00c7\7\60\2\2\u00c7\u00c9"
                                                + "\5&\24\2\u00c8\u00ca\5\"\22\2\u00c9\u00c8\3\2\2\2\u00c9\u00ca\3\2\2\2"
                                                + "\u00ca!\3\2\2\2\u00cb\u00cc\7Y\2\2\u00cc\u00cd\5&\24\2\u00cd#\3\2\2\2"
                                                + "\u00ce\u00cf\7d\2\2\u00cf\u00d0\7\\\2\2\u00d0\u00d1\5&\24\2\u00d1%\3\2"
                                                + "\2\2\u00d2\u00d3\b\24\1\2\u00d3\u00e4\5H%\2\u00d4\u00e4\5\66\34\2\u00d5"
                                                + "\u00e4\58\35\2\u00d6\u00d7\7O\2\2\u00d7\u00d8\5&\24\2\u00d8\u00d9\7P\2"
                                                + "\2\u00d9\u00e4\3\2\2\2\u00da\u00db\7G\2\2\u00db\u00e4\5&\24\21\u00dc\u00dd"
                                                + "\7I\2\2\u00dd\u00e4\5&\24\20\u00de\u00df\t\4\2\2\u00df\u00e4\5&\24\r\u00e0"
                                                + "\u00e1\5(\25\2\u00e1\u00e2\5&\24\f\u00e2\u00e4\3\2\2\2\u00e3\u00d2\3\2"
                                                + "\2\2\u00e3\u00d4\3\2\2\2\u00e3\u00d5\3\2\2\2\u00e3\u00d6\3\2\2\2\u00e3"
                                                + "\u00da\3\2\2\2\u00e3\u00dc\3\2\2\2\u00e3\u00de\3\2\2\2\u00e3\u00e0\3\2"
                                                + "\2\2\u00e4\u0112\3\2\2\2\u00e5\u00e6\f\13\2\2\u00e6\u00e7\5*\26\2\u00e7"
                                                + "\u00e8\5&\24\f\u00e8\u0111\3\2\2\2\u00e9\u00ea\f\n\2\2\u00ea\u00eb\5,"
                                                + "\27\2\u00eb\u00ec\5&\24\13\u00ec\u0111\3\2\2\2\u00ed\u00ee\f\t\2\2\u00ee"
                                                + "\u00ef\5.\30\2\u00ef\u00f0\5&\24\n\u00f0\u0111\3\2\2\2\u00f1\u00f2\f\b"
                                                + "\2\2\u00f2\u00f3\5\60\31\2\u00f3\u00f4\5&\24\t\u00f4\u0111\3\2\2\2\u00f5"
                                                + "\u00f6\f\7\2\2\u00f6\u00f7\5\62\32\2\u00f7\u00f8\5&\24\b\u00f8\u0111\3"
                                                + "\2\2\2\u00f9\u00fb\f\6\2\2\u00fa\u00fc\7\65\2\2\u00fb\u00fa\3\2\2\2\u00fb"
                                                + "\u00fc\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fe\5\64\33\2\u00fe\u00ff\5"
                                                + "&\24\7\u00ff\u0111\3\2\2\2\u0100\u0101\f\5\2\2\u0101\u0102\7B\2\2\u0102"
                                                + "\u0111\5&\24\6\u0103\u0104\f\4\2\2\u0104\u0105\7D\2\2\u0105\u0111\5&\24"
                                                + "\5\u0106\u0107\f\3\2\2\u0107\u0108\7[\2\2\u0108\u0109\5&\24\2\u0109\u010a"
                                                + "\7X\2\2\u010a\u010b\5&\24\4\u010b\u0111\3\2\2\2\u010c\u010d\f\17\2\2\u010d"
                                                + "\u0111\7G\2\2\u010e\u010f\f\16\2\2\u010f\u0111\7I\2\2\u0110\u00e5\3\2"
                                                + "\2\2\u0110\u00e9\3\2\2\2\u0110\u00ed\3\2\2\2\u0110\u00f1\3\2\2\2\u0110"
                                                + "\u00f5\3\2\2\2\u0110\u00f9\3\2\2\2\u0110\u0100\3\2\2\2\u0110\u0103\3\2"
                                                + "\2\2\u0110\u0106\3\2\2\2\u0110\u010c\3\2\2\2\u0110\u010e\3\2\2\2\u0111"
                                                + "\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\'\3\2\2\2"
                                                + "\u0114\u0112\3\2\2\2\u0115\u0116\t\5\2\2\u0116)\3\2\2\2\u0117\u0118\t"
                                                + "\6\2\2\u0118+\3\2\2\2\u0119\u011a\t\7\2\2\u011a-\3\2\2\2\u011b\u011c\t"
                                                + "\b\2\2\u011c/\3\2\2\2\u011d\u011e\t\t\2\2\u011e\61\3\2\2\2\u011f\u0120"
                                                + "\t\n\2\2\u0120\63\3\2\2\2\u0121\u0122\t\13\2\2\u0122\65\3\2\2\2\u0123"
                                                + "\u0126\7U\2\2\u0124\u0127\5F$\2\u0125\u0127\7a\2\2\u0126\u0124\3\2\2\2"
                                                + "\u0126\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u0129\7T\2\2\u0129\67\3"
                                                + "\2\2\2\u012a\u012c\5:\36\2\u012b\u012d\5<\37\2\u012c\u012b\3\2\2\2\u012c"
                                                + "\u012d\3\2\2\2\u012d9\3\2\2\2\u012e\u012f\7Z\2\2\u012f\u0130\5@!\2\u0130"
                                                + ";\3\2\2\2\u0131\u0133\7O\2\2\u0132\u0134\5> \2\u0133\u0132\3\2\2\2\u0133"
                                                + "\u0134\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0139\7P\2\2\u0136\u0137\7W\2"
                                                + "\2\u0137\u013a\5@!\2\u0138\u013a\5B\"\2\u0139\u0136\3\2\2\2\u0139\u0138"
                                                + "\3\2\2\2\u0139\u013a\3\2\2\2\u013a=\3\2\2\2\u013b\u0140\5&\24\2\u013c"
                                                + "\u013d\7Y\2\2\u013d\u013f\5&\24\2\u013e\u013c\3\2\2\2\u013f\u0142\3\2"
                                                + "\2\2\u0140\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141?\3\2\2\2\u0142\u0140"
                                                + "\3\2\2\2\u0143\u0145\5D#\2\u0144\u0146\5B\"\2\u0145\u0144\3\2\2\2\u0145"
                                                + "\u0146\3\2\2\2\u0146A\3\2\2\2\u0147\u0148\7Q\2\2\u0148\u0149\5&\24\2\u0149"
                                                + "\u014d\7R\2\2\u014a\u014e\5B\"\2\u014b\u014c\7W\2\2\u014c\u014e\5@!\2"
                                                + "\u014d\u014a\3\2\2\2\u014d\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014eC\3"
                                                + "\2\2\2\u014f\u0154\5F$\2\u0150\u0151\7W\2\2\u0151\u0153\5F$\2\u0152\u0150"
                                                + "\3\2\2\2\u0153\u0156\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155"
                                                + "E\3\2\2\2\u0156\u0154\3\2\2\2\u0157\u015b\7d\2\2\u0158\u015b\7e\2\2\u0159"
                                                + "\u015b\5b\62\2\u015a\u0157\3\2\2\2\u015a\u0158\3\2\2\2\u015a\u0159\3\2"
                                                + "\2\2\u015bG\3\2\2\2\u015c\u0162\58\35\2\u015d\u0162\5J&\2\u015e\u0162"
                                                + "\5R*\2\u015f\u0162\5Z.\2\u0160\u0162\5@!\2\u0161\u015c\3\2\2\2\u0161\u015d"
                                                + "\3\2\2\2\u0161\u015e\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0160\3\2\2\2\u0162"
                                                + "I\3\2\2\2\u0163\u016d\t\f\2\2\u0164\u0166\5L\'\2\u0165\u0164\3\2\2\2\u0165"
                                                + "\u0166\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u016d\5N(\2\u0168\u016d\7`\2"
                                                + "\2\u0169\u016d\7a\2\2\u016a\u016d\5P)\2\u016b\u016d\7/\2\2\u016c\u0163"
                                                + "\3\2\2\2\u016c\u0165\3\2\2\2\u016c\u0168\3\2\2\2\u016c\u0169\3\2\2\2\u016c"
                                                + "\u016a\3\2\2\2\u016c\u016b\3\2\2\2\u016dK\3\2\2\2\u016e\u016f\t\7\2\2"
                                                + "\u016fM\3\2\2\2\u0170\u0175\7^\2\2\u0171\u0175\7]\2\2\u0172\u0173\7_\2"
                                                + "\2\u0173\u0175\7W\2\2\u0174\u0170\3\2\2\2\u0174\u0171\3\2\2\2\u0174\u0172"
                                                + "\3\2\2\2\u0175\u0178\3\2\2\2\u0176\u0178\7_\2\2\u0177\u0174\3\2\2\2\u0177"
                                                + "\u0176\3\2\2\2\u0178O\3\2\2\2\u0179\u017a\7c\2\2\u017a\u017d\7a\2\2\u017b"
                                                + "\u017d\7b\2\2\u017c\u0179\3\2\2\2\u017c\u017b\3\2\2\2\u017dQ\3\2\2\2\u017e"
                                                + "\u0181\5T+\2\u017f\u0181\5V,\2\u0180\u017e\3\2\2\2\u0180\u017f\3\2\2\2"
                                                + "\u0181S\3\2\2\2\u0182\u018b\7Q\2\2\u0183\u0188\5H%\2\u0184\u0185\7Y\2"
                                                + "\2\u0185\u0187\5H%\2\u0186\u0184\3\2\2\2\u0187\u018a\3\2\2\2\u0188\u0186"
                                                + "\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u018c\3\2\2\2\u018a\u0188\3\2\2\2\u018b"
                                                + "\u0183\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\7R"
                                                + "\2\2\u018eU\3\2\2\2\u018f\u0198\7S\2\2\u0190\u0195\5X-\2\u0191\u0192\7"
                                                + "Y\2\2\u0192\u0194\5X-\2\u0193\u0191\3\2\2\2\u0194\u0197\3\2\2\2\u0195"
                                                + "\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0199\3\2\2\2\u0197\u0195\3\2"
                                                + "\2\2\u0198\u0190\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019a\3\2\2\2\u019a"
                                                + "\u019b\7T\2\2\u019bW\3\2\2\2\u019c\u019d\7a\2\2\u019d\u019e\7X\2\2\u019e"
                                                + "\u019f\5H%\2\u019fY\3\2\2\2\u01a0\u01a1\7\7\2\2\u01a1\u01a2\7O\2\2\u01a2"
                                                + "\u01a3\5&\24\2\u01a3\u01a4\7\66\2\2\u01a4\u01a5\5\\/\2\u01a5\u01a6\7P"
                                                + "\2\2\u01a6[\3\2\2\2\u01a7\u01a9\t\r\2\2\u01a8\u01aa\5^\60\2\u01a9\u01a8"
                                                + "\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ae\3\2\2\2\u01ab\u01ae\t\16\2\2"
                                                + "\u01ac\u01ae\7c\2\2\u01ad\u01a7\3\2\2\2\u01ad\u01ab\3\2\2\2\u01ad\u01ac"
                                                + "\3\2\2\2\u01ae]\3\2\2\2\u01af\u01b0\7O\2\2\u01b0\u01b3\7_\2\2\u01b1\u01b2"
                                                + "\7Y\2\2\u01b2\u01b4\5`\61\2\u01b3\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4"
                                                + "\u01b5\3\2\2\2\u01b5\u01b6\7P\2\2\u01b6_\3\2\2\2\u01b7\u01b8\t\17\2\2"
                                                + "\u01b8a\3\2\2\2\u01b9\u01db\7\3\2\2\u01ba\u01db\7\4\2\2\u01bb\u01db\7"
                                                + "\5\2\2\u01bc\u01db\7\6\2\2\u01bd\u01db\7\7\2\2\u01be\u01db\7\b\2\2\u01bf"
                                                + "\u01db\7\t\2\2\u01c0\u01db\7\n\2\2\u01c1\u01db\7\13\2\2\u01c2\u01db\7"
                                                + "\f\2\2\u01c3\u01db\7\r\2\2\u01c4\u01db\7\16\2\2\u01c5\u01db\7\17\2\2\u01c6"
                                                + "\u01db\7\20\2\2\u01c7\u01db\7\21\2\2\u01c8\u01db\7\22\2\2\u01c9\u01db"
                                                + "\7\23\2\2\u01ca\u01db\7\24\2\2\u01cb\u01db\7\25\2\2\u01cc\u01db\7\26\2"
                                                + "\2\u01cd\u01db\7\27\2\2\u01ce\u01db\7\30\2\2\u01cf\u01d0\7\31\2\2\u01d0"
                                                + "\u01db\7\32\2\2\u01d1\u01db\7\33\2\2\u01d2\u01db\7\34\2\2\u01d3\u01db"
                                                + "\7\35\2\2\u01d4\u01db\7\36\2\2\u01d5\u01db\7\37\2\2\u01d6\u01db\7 \2\2"
                                                + "\u01d7\u01db\7!\2\2\u01d8\u01db\7\"\2\2\u01d9\u01db\7#\2\2\u01da\u01b9"
                                                + "\3\2\2\2\u01da\u01ba\3\2\2\2\u01da\u01bb\3\2\2\2\u01da\u01bc\3\2\2\2\u01da"
                                                + "\u01bd\3\2\2\2\u01da\u01be\3\2\2\2\u01da\u01bf\3\2\2\2\u01da\u01c0\3\2"
                                                + "\2\2\u01da\u01c1\3\2\2\2\u01da\u01c2\3\2\2\2\u01da\u01c3\3\2\2\2\u01da"
                                                + "\u01c4\3\2\2\2\u01da\u01c5\3\2\2\2\u01da\u01c6\3\2\2\2\u01da\u01c7\3\2"
                                                + "\2\2\u01da\u01c8\3\2\2\2\u01da\u01c9\3\2\2\2\u01da\u01ca\3\2\2\2\u01da"
                                                + "\u01cb\3\2\2\2\u01da\u01cc\3\2\2\2\u01da\u01cd\3\2\2\2\u01da\u01ce\3\2"
                                                + "\2\2\u01da\u01cf\3\2\2\2\u01da\u01d1\3\2\2\2\u01da\u01d2\3\2\2\2\u01da"
                                                + "\u01d3\3\2\2\2\u01da\u01d4\3\2\2\2\u01da\u01d5\3\2\2\2\u01da\u01d6\3\2"
                                                + "\2\2\u01da\u01d7\3\2\2\2\u01da\u01d8\3\2\2\2\u01da\u01d9\3\2\2\2\u01db"
                                                + "c\3\2\2\2/gkt{\u0082\u0085\u0088\u008b\u0099\u009c\u00a7\u00ab\u00b3\u00b9"
                                                + "\u00bd\u00c1\u00c9\u00e3\u00fb\u0110\u0112\u0126\u012c\u0133\u0139\u0140"
                                                + "\u0145\u014d\u0154\u015a\u0161\u0165\u016c\u0174\u0177\u017c\u0180\u0188" + "\u018b\u0195\u0198\u01a9\u01ad\u01b3\u01da";
    public static final ATN    _ATN           = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[]                  _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[]  _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY      = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

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

    public DetectRulesParser(TokenStream input, List<DetectRulesFeature> features){
        this(input);
        super.initFeatures(features);
    }

    public DetectRulesParser(TokenStream input){
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[] { "root", "comment", "multiStatement", "singleStatement", "defineStatement", "defineType", "defineDefault", "defineEnum", "defineHINT", "ifStatement",
                              "conditionThen", "elseifThen", "elseThen", "returnStatement", "throwStatement", "assertStatement", "assertExpr", "assignStatement", "expression",
                              "unarySymbol", "multiplicativeSymbol", "additiveSymbol", "bitwiseSymbol", "shiftSymbol", "compareSymbol", "matchSymbol", "paramExpr",
                              "funOrDetectExpr", "detectPart", "funcPart", "funcArgPart", "identifyExpr", "subExpr", "identifyPart", "identify", "anyPrimary", "primitiveValue",
                              "signSymbol", "numValue", "dtValue", "collectionValue", "listValue", "objectValue", "objectItem", "castExpr", "dataType", "numFmtValue",
                              "numFmtRounding", "allIdentifierKeywords" };
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
    public String getGrammarFileName() { return "DetectRulesParser.g4"; }

    @Override
    public String[] getRuleNames() { return ruleNames; }

    @Override
    public String getSerializedATN() { return _serializedATN; }

    @Override
    public ATN getATN() { return _ATN; }

    public final RootContext root() throws RecognitionException {
        RootContext _localctx = new RootContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_root);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(101);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == WELL) {
                    {
                        {
                            setState(98);
                            defineStatement();
                        }
                    }
                    setState(103);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(105);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (((((_la - 38)) & ~0x3f) == 0
                     && ((1L << (_la - 38)) & ((1L << (IF - 38)) | (1L << (ASSERT - 38)) | (1L << (RETURN - 38)) | (1L << (THROW - 38)) | (1L << (IDENTIFIER - 38)))) != 0)) {
                    {
                        setState(104);
                        multiStatement();
                    }
                }

                setState(107);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CommentContext comment() throws RecognitionException {
        CommentContext _localctx = new CommentContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_comment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(109);
                _la = _input.LA(1);
                if (!(_la == COMMENT1 || _la == COMMENT3)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MultiStatementContext multiStatement() throws RecognitionException {
        MultiStatementContext _localctx = new MultiStatementContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_multiStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(112);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(111);
                            singleStatement();
                        }
                    }
                    setState(114);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (((((_la - 38)) & ~0x3f) == 0
                          && ((1L << (_la - 38)) & ((1L << (IF - 38)) | (1L << (ASSERT - 38)) | (1L << (RETURN - 38)) | (1L << (THROW - 38)) | (1L << (IDENTIFIER - 38)))) != 0));
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SingleStatementContext singleStatement() throws RecognitionException {
        SingleStatementContext _localctx = new SingleStatementContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_singleStatement);
        try {
            setState(121);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IF:
                    enterOuterAlt(_localctx, 1); {
                    setState(116);
                    ifStatement();
                }
                    break;
                case RETURN:
                    enterOuterAlt(_localctx, 2); {
                    setState(117);
                    returnStatement();
                }
                    break;
                case THROW:
                    enterOuterAlt(_localctx, 3); {
                    setState(118);
                    throwStatement();
                }
                    break;
                case ASSERT:
                    enterOuterAlt(_localctx, 4); {
                    setState(119);
                    assertStatement();
                }
                    break;
                case IDENTIFIER:
                    enterOuterAlt(_localctx, 5); {
                    setState(120);
                    assignStatement();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DefineStatementContext defineStatement() throws RecognitionException {
        DefineStatementContext _localctx = new DefineStatementContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_defineStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(123);
                match(WELL);
                setState(124);
                match(DEFINE);
                setState(125);
                match(PRI_STR);
                setState(128);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == AS) {
                    {
                        setState(126);
                        match(AS);
                        setState(127);
                        defineType();
                    }
                }

                setState(131);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == DEFAULT) {
                    {
                        setState(130);
                        defineDefault();
                    }
                }

                setState(134);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == ENUM) {
                    {
                        setState(133);
                        defineEnum();
                    }
                }

                setState(137);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == HINT) {
                    {
                        setState(136);
                        defineHINT();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DefineTypeContext defineType() throws RecognitionException {
        DefineTypeContext _localctx = new DefineTypeContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_defineType);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(139);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0
                       && ((1L << _la)
                           & ((1L << BOOL) | (1L << DATETIME) | (1L << DATE) | (1L << DECIMAL) | (1L << FLOAT) | (1L << INTEGER) | (1L << STRING) | (1L << TIME))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DefineDefaultContext defineDefault() throws RecognitionException {
        DefineDefaultContext _localctx = new DefineDefaultContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_defineDefault);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(141);
                match(DEFAULT);
                setState(142);
                match(PRI_STR);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DefineEnumContext defineEnum() throws RecognitionException {
        DefineEnumContext _localctx = new DefineEnumContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_defineEnum);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(144);
                match(ENUM);
                setState(145);
                match(LSBT);
                setState(154);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == PRI_STR) {
                    {
                        setState(146);
                        match(PRI_STR);
                        setState(151);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == COMMA) {
                            {
                                {
                                    setState(147);
                                    match(COMMA);
                                    setState(148);
                                    match(PRI_STR);
                                }
                            }
                            setState(153);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(156);
                match(RSBT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DefineHINTContext defineHINT() throws RecognitionException {
        DefineHINTContext _localctx = new DefineHINTContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_defineHINT);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(158);
                match(HINT);
                setState(159);
                match(PRI_STR);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IfStatementContext ifStatement() throws RecognitionException {
        IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_ifStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(161);
                conditionThen();
                setState(165);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == ELSEIF) {
                    {
                        {
                            setState(162);
                            elseifThen();
                        }
                    }
                    setState(167);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(169);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == ELSE) {
                    {
                        setState(168);
                        elseThen();
                    }
                }

                setState(171);
                match(END);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ConditionThenContext conditionThen() throws RecognitionException {
        ConditionThenContext _localctx = new ConditionThenContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_conditionThen);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(173);
                match(IF);
                setState(174);
                expression(0);
                setState(175);
                match(THEN);
                setState(177);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (((((_la - 38)) & ~0x3f) == 0
                     && ((1L << (_la - 38)) & ((1L << (IF - 38)) | (1L << (ASSERT - 38)) | (1L << (RETURN - 38)) | (1L << (THROW - 38)) | (1L << (IDENTIFIER - 38)))) != 0)) {
                    {
                        setState(176);
                        multiStatement();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ElseifThenContext elseifThen() throws RecognitionException {
        ElseifThenContext _localctx = new ElseifThenContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_elseifThen);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(179);
                match(ELSEIF);
                setState(180);
                expression(0);
                setState(181);
                match(THEN);
                setState(183);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (((((_la - 38)) & ~0x3f) == 0
                     && ((1L << (_la - 38)) & ((1L << (IF - 38)) | (1L << (ASSERT - 38)) | (1L << (RETURN - 38)) | (1L << (THROW - 38)) | (1L << (IDENTIFIER - 38)))) != 0)) {
                    {
                        setState(182);
                        multiStatement();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ElseThenContext elseThen() throws RecognitionException {
        ElseThenContext _localctx = new ElseThenContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_elseThen);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(185);
                match(ELSE);
                setState(187);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (((((_la - 38)) & ~0x3f) == 0
                     && ((1L << (_la - 38)) & ((1L << (IF - 38)) | (1L << (ASSERT - 38)) | (1L << (RETURN - 38)) | (1L << (THROW - 38)) | (1L << (IDENTIFIER - 38)))) != 0)) {
                    {
                        setState(186);
                        multiStatement();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ReturnStatementContext returnStatement() throws RecognitionException {
        ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_returnStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(189);
                match(RETURN);
                setState(191);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
                    case 1: {
                        setState(190);
                        expression(0);
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ThrowStatementContext throwStatement() throws RecognitionException {
        ThrowStatementContext _localctx = new ThrowStatementContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_throwStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(193);
                match(THROW);
                setState(194);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AssertStatementContext assertStatement() throws RecognitionException {
        AssertStatementContext _localctx = new AssertStatementContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_assertStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(196);
                match(ASSERT);
                setState(197);
                expression(0);
                setState(199);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == COMMA) {
                    {
                        setState(198);
                        assertExpr();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AssertExprContext assertExpr() throws RecognitionException {
        AssertExprContext _localctx = new AssertExprContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_assertExpr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(201);
                match(COMMA);
                setState(202);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AssignStatementContext assignStatement() throws RecognitionException {
        AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_assignStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(204);
                match(IDENTIFIER);
                setState(205);
                match(ASSIGN);
                setState(206);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ExpressionContext expression() throws RecognitionException {
        return expression(0);
    }

    private ExpressionContext expression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
        ExpressionContext _prevctx = _localctx;
        int _startState = 36;
        enterRecursionRule(_localctx, 36, RULE_expression, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(225);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                    case 1: {
                        _localctx = new AtomExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;

                        setState(209);
                        anyPrimary();
                    }
                        break;
                    case 2: {
                        _localctx = new AccessParamExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(210);
                        paramExpr();
                    }
                        break;
                    case 3: {
                        _localctx = new FuncOrDetectExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(211);
                        funOrDetectExpr();
                    }
                        break;
                    case 4: {
                        _localctx = new PrivilegeExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(212);
                        match(LBT);
                        setState(213);
                        expression(0);
                        setState(214);
                        match(RBT);
                    }
                        break;
                    case 5: {
                        _localctx = new UnaryBeforeIncreExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(216);
                        match(AO_INCR);
                        setState(217);
                        expression(15);
                    }
                        break;
                    case 6: {
                        _localctx = new UnaryBeforeDecrExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(218);
                        match(AO_DECR);
                        setState(219);
                        expression(14);
                    }
                        break;
                    case 7: {
                        _localctx = new UnaryNotPlusExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(220);
                        _la = _input.LA(1);
                        if (!(_la == LO_NOT || _la == BO_NOT)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF)
                                matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(221);
                        expression(11);
                    }
                        break;
                    case 8: {
                        _localctx = new UnaryExprContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(222);
                        unarySymbol();
                        setState(223);
                        expression(10);
                    }
                        break;
                }
                _ctx.stop = _input.LT(-1);
                setState(272);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 20, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null)
                            triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(270);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 19, _ctx)) {
                                case 1: {
                                    _localctx = new MultiplicativeExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(227);
                                    if (!(precpred(_ctx, 9)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                                    setState(228);
                                    multiplicativeSymbol();
                                    setState(229);
                                    expression(10);
                                }
                                    break;
                                case 2: {
                                    _localctx = new AdditiveExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(231);
                                    if (!(precpred(_ctx, 8)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                                    setState(232);
                                    additiveSymbol();
                                    setState(233);
                                    expression(9);
                                }
                                    break;
                                case 3: {
                                    _localctx = new BitwiseExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(235);
                                    if (!(precpred(_ctx, 7)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    setState(236);
                                    bitwiseSymbol();
                                    setState(237);
                                    expression(8);
                                }
                                    break;
                                case 4: {
                                    _localctx = new ShiftExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(239);
                                    if (!(precpred(_ctx, 6)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                                    setState(240);
                                    shiftSymbol();
                                    setState(241);
                                    expression(7);
                                }
                                    break;
                                case 5: {
                                    _localctx = new CompareExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(243);
                                    if (!(precpred(_ctx, 5)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    setState(244);
                                    compareSymbol();
                                    setState(245);
                                    expression(6);
                                }
                                    break;
                                case 6: {
                                    _localctx = new MatchExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(247);
                                    if (!(precpred(_ctx, 4)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    setState(249);
                                    _errHandler.sync(this);
                                    _la = _input.LA(1);
                                    if (_la == NOT) {
                                        {
                                            setState(248);
                                            match(NOT);
                                        }
                                    }

                                    setState(251);
                                    matchSymbol();
                                    setState(252);
                                    expression(5);
                                }
                                    break;
                                case 7: {
                                    _localctx = new LogicAndExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(254);
                                    if (!(precpred(_ctx, 3)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    setState(255);
                                    match(LO_AND);
                                    setState(256);
                                    expression(4);
                                }
                                    break;
                                case 8: {
                                    _localctx = new LogicOrExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(257);
                                    if (!(precpred(_ctx, 2)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    setState(258);
                                    match(LO_OR);
                                    setState(259);
                                    expression(3);
                                }
                                    break;
                                case 9: {
                                    _localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(260);
                                    if (!(precpred(_ctx, 1)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    setState(261);
                                    match(QUE);
                                    setState(262);
                                    expression(0);
                                    setState(263);
                                    match(COLON);
                                    setState(264);
                                    expression(2);
                                }
                                    break;
                                case 10: {
                                    _localctx = new UnaryAfterIncreExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(266);
                                    if (!(precpred(_ctx, 13)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 13)");
                                    setState(267);
                                    match(AO_INCR);
                                }
                                    break;
                                case 11: {
                                    _localctx = new UnaryAfterDecrExprContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(268);
                                    if (!(precpred(_ctx, 12)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 12)");
                                    setState(269);
                                    match(AO_DECR);
                                }
                                    break;
                            }
                        }
                    }
                    setState(274);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 20, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public final UnarySymbolContext unarySymbol() throws RecognitionException {
        UnarySymbolContext _localctx = new UnarySymbolContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_unarySymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(275);
                _la = _input.LA(1);
                if (!(((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & ((1L << (NOT - 51)) | (1L << (AO_PLUS - 51)) | (1L << (AO_MINUS - 51)))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MultiplicativeSymbolContext multiplicativeSymbol() throws RecognitionException {
        MultiplicativeSymbolContext _localctx = new MultiplicativeSymbolContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_multiplicativeSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(277);
                _la = _input.LA(1);
                if (!(((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & ((1L << (AO_MUL - 73)) | (1L << (AO_DIV - 73)) | (1L << (AO_MOD - 73)))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AdditiveSymbolContext additiveSymbol() throws RecognitionException {
        AdditiveSymbolContext _localctx = new AdditiveSymbolContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_additiveSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(279);
                _la = _input.LA(1);
                if (!(_la == AO_PLUS || _la == AO_MINUS)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BitwiseSymbolContext bitwiseSymbol() throws RecognitionException {
        BitwiseSymbolContext _localctx = new BitwiseSymbolContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_bitwiseSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(281);
                _la = _input.LA(1);
                if (!(((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (BO_AND - 65)) | (1L << (BO_OR - 65)) | (1L << (BO_XOR - 65)))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ShiftSymbolContext shiftSymbol() throws RecognitionException {
        ShiftSymbolContext _localctx = new ShiftSymbolContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_shiftSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(283);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BO_RSHIFT2) | (1L << BO_RSHIFT) | (1L << BO_LSHIFT))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CompareSymbolContext compareSymbol() throws RecognitionException {
        CompareSymbolContext _localctx = new CompareSymbolContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_compareSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(285);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0
                       && ((1L << _la) & ((1L << CO_GE) | (1L << CO_GT) | (1L << CO_LE) | (1L << CO_LT) | (1L << CO_IEQ) | (1L << CO_EQ) | (1L << CO_NE))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MatchSymbolContext matchSymbol() throws RecognitionException {
        MatchSymbolContext _localctx = new MatchSymbolContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_matchSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(287);
                _la = _input.LA(1);
                if (!(_la == IN || _la == MATCHES)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ParamExprContext paramExpr() throws RecognitionException {
        ParamExprContext _localctx = new ParamExprContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_paramExpr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(289);
                match(OREP);
                setState(292);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case DEFINE:
                    case DEFAULT:
                    case ENUM:
                    case HINT:
                    case CAST:
                    case CEILING:
                    case DOWN:
                    case FLOOR:
                    case HALF_EVEN:
                    case HALF_DOWN:
                    case HALF_UP:
                    case UP:
                    case UNNECESSARY:
                    case BOOL:
                    case DATETIME:
                    case DATE:
                    case DECIMAL:
                    case FLOAT:
                    case INTEGER:
                    case STRING:
                    case TIME:
                    case INSTANCEOF:
                    case TYPEOF:
                    case GOTO:
                    case SWITCH:
                    case CASE:
                    case FOR:
                    case CONTINUE:
                    case BREAK:
                    case VAR:
                    case DEF:
                    case FUNC:
                    case IDENTIFIER:
                    case PRI_IDENTIFIER: {
                        setState(290);
                        identify();
                    }
                        break;
                    case PRI_STR: {
                        setState(291);
                        match(PRI_STR);
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(294);
                match(CCBR);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final FunOrDetectExprContext funOrDetectExpr() throws RecognitionException {
        FunOrDetectExprContext _localctx = new FunOrDetectExprContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_funOrDetectExpr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(296);
                detectPart();
                setState(298);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
                    case 1: {
                        setState(297);
                        funcPart();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DetectPartContext detectPart() throws RecognitionException {
        DetectPartContext _localctx = new DetectPartContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_detectPart);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(300);
                match(REF);
                setState(301);
                identifyExpr();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final FuncPartContext funcPart() throws RecognitionException {
        FuncPartContext _localctx = new FuncPartContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_funcPart);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(303);
                match(LBT);
                setState(305);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0
                     && ((1L << _la) & ((1L << DEFINE) | (1L << DEFAULT) | (1L << ENUM) | (1L << HINT) | (1L << CAST) | (1L << CEILING) | (1L << DOWN) | (1L << FLOOR)
                                        | (1L << HALF_EVEN) | (1L << HALF_DOWN) | (1L << HALF_UP) | (1L << UP) | (1L << UNNECESSARY) | (1L << BOOL) | (1L << DATETIME)
                                        | (1L << DATE) | (1L << DECIMAL) | (1L << FLOAT) | (1L << INTEGER) | (1L << STRING) | (1L << TIME) | (1L << INSTANCEOF) | (1L << TYPEOF)
                                        | (1L << GOTO) | (1L << SWITCH) | (1L << CASE) | (1L << FOR) | (1L << CONTINUE) | (1L << BREAK) | (1L << VAR) | (1L << DEF) | (1L << FUNC)
                                        | (1L << FALSE) | (1L << TRUE) | (1L << NULL) | (1L << NOT) | (1L << LO_NOT))) != 0)
                    || ((((_la - 68)) & ~0x3f) == 0
                        && ((1L << (_la - 68))
                            & ((1L << (BO_NOT - 68)) | (1L << (AO_INCR - 68)) | (1L << (AO_PLUS - 68)) | (1L << (AO_DECR - 68)) | (1L << (AO_MINUS - 68)) | (1L << (LBT - 68))
                               | (1L << (LSBT - 68)) | (1L << (OCBR - 68)) | (1L << (OREP - 68)) | (1L << (REF - 68)) | (1L << (PRI_DECIMAL - 68)) | (1L << (PRI_FLOAT - 68))
                               | (1L << (PRI_INTEGER - 68)) | (1L << (PRI_XOB_NUM - 68)) | (1L << (PRI_STR - 68)) | (1L << (PRI_DATETIME - 68)) | (1L << (PRI_DATETIME_FMT - 68))
                               | (1L << (IDENTIFIER - 68)) | (1L << (PRI_IDENTIFIER - 68)))) != 0)) {
                    {
                        setState(304);
                        funcArgPart();
                    }
                }

                setState(307);
                match(RBT);
                setState(311);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 24, _ctx)) {
                    case 1: {
                        {
                            setState(308);
                            match(DOT);
                            setState(309);
                            identifyExpr();
                        }
                    }
                        break;
                    case 2: {
                        setState(310);
                        subExpr();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final FuncArgPartContext funcArgPart() throws RecognitionException {
        FuncArgPartContext _localctx = new FuncArgPartContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_funcArgPart);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(313);
                expression(0);
                setState(318);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(314);
                            match(COMMA);
                            setState(315);
                            expression(0);
                        }
                    }
                    setState(320);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IdentifyExprContext identifyExpr() throws RecognitionException {
        IdentifyExprContext _localctx = new IdentifyExprContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_identifyExpr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(321);
                identifyPart();
                setState(323);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 26, _ctx)) {
                    case 1: {
                        setState(322);
                        subExpr();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SubExprContext subExpr() throws RecognitionException {
        SubExprContext _localctx = new SubExprContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_subExpr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(325);
                match(LSBT);
                setState(326);
                expression(0);
                setState(327);
                match(RSBT);
                setState(331);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 27, _ctx)) {
                    case 1: {
                        setState(328);
                        subExpr();
                    }
                        break;
                    case 2: {
                        setState(329);
                        match(DOT);
                        setState(330);
                        identifyExpr();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IdentifyPartContext identifyPart() throws RecognitionException {
        IdentifyPartContext _localctx = new IdentifyPartContext(_ctx, getState());
        enterRule(_localctx, 66, RULE_identifyPart);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(333);
                identify();
                setState(338);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 28, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(334);
                                match(DOT);
                                setState(335);
                                identify();
                            }
                        }
                    }
                    setState(340);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 28, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IdentifyContext identify() throws RecognitionException {
        IdentifyContext _localctx = new IdentifyContext(_ctx, getState());
        enterRule(_localctx, 68, RULE_identify);
        try {
            setState(344);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFIER:
                    enterOuterAlt(_localctx, 1); {
                    setState(341);
                    match(IDENTIFIER);
                }
                    break;
                case PRI_IDENTIFIER:
                    enterOuterAlt(_localctx, 2); {
                    setState(342);
                    match(PRI_IDENTIFIER);
                }
                    break;
                case DEFINE:
                case DEFAULT:
                case ENUM:
                case HINT:
                case CAST:
                case CEILING:
                case DOWN:
                case FLOOR:
                case HALF_EVEN:
                case HALF_DOWN:
                case HALF_UP:
                case UP:
                case UNNECESSARY:
                case BOOL:
                case DATETIME:
                case DATE:
                case DECIMAL:
                case FLOAT:
                case INTEGER:
                case STRING:
                case TIME:
                case INSTANCEOF:
                case TYPEOF:
                case GOTO:
                case SWITCH:
                case CASE:
                case FOR:
                case CONTINUE:
                case BREAK:
                case VAR:
                case DEF:
                case FUNC:
                    enterOuterAlt(_localctx, 3); {
                    setState(343);
                    allIdentifierKeywords();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AnyPrimaryContext anyPrimary() throws RecognitionException {
        AnyPrimaryContext _localctx = new AnyPrimaryContext(_ctx, getState());
        enterRule(_localctx, 70, RULE_anyPrimary);
        try {
            setState(351);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(346);
                    funOrDetectExpr();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(347);
                    primitiveValue();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(348);
                    collectionValue();
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(349);
                    castExpr();
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(350);
                    identifyExpr();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PrimitiveValueContext primitiveValue() throws RecognitionException {
        PrimitiveValueContext _localctx = new PrimitiveValueContext(_ctx, getState());
        enterRule(_localctx, 72, RULE_primitiveValue);
        int _la;
        try {
            setState(362);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case FALSE:
                case TRUE:
                    _localctx = new BooleanValueContext(_localctx);
                    enterOuterAlt(_localctx, 1); {
                    setState(353);
                    _la = _input.LA(1);
                    if (!(_la == FALSE || _la == TRUE)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF)
                            matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                }
                    break;
                case AO_PLUS:
                case AO_MINUS:
                case PRI_DECIMAL:
                case PRI_FLOAT:
                case PRI_INTEGER:
                    _localctx = new NumberValueContext(_localctx);
                    enterOuterAlt(_localctx, 2); {
                    setState(355);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == AO_PLUS || _la == AO_MINUS) {
                        {
                            setState(354);
                            signSymbol();
                        }
                    }

                    setState(357);
                    numValue();
                }
                    break;
                case PRI_XOB_NUM:
                    _localctx = new XobValueContext(_localctx);
                    enterOuterAlt(_localctx, 3); {
                    setState(358);
                    match(PRI_XOB_NUM);
                }
                    break;
                case PRI_STR:
                    _localctx = new StringValueContext(_localctx);
                    enterOuterAlt(_localctx, 4); {
                    setState(359);
                    match(PRI_STR);
                }
                    break;
                case PRI_DATETIME:
                case PRI_DATETIME_FMT:
                    _localctx = new DatetimeValueContext(_localctx);
                    enterOuterAlt(_localctx, 5); {
                    setState(360);
                    dtValue();
                }
                    break;
                case NULL:
                    _localctx = new NullVaueContext(_localctx);
                    enterOuterAlt(_localctx, 6); {
                    setState(361);
                    match(NULL);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SignSymbolContext signSymbol() throws RecognitionException {
        SignSymbolContext _localctx = new SignSymbolContext(_ctx, getState());
        enterRule(_localctx, 74, RULE_signSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(364);
                _la = _input.LA(1);
                if (!(_la == AO_PLUS || _la == AO_MINUS)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final NumValueContext numValue() throws RecognitionException {
        NumValueContext _localctx = new NumValueContext(_ctx, getState());
        enterRule(_localctx, 76, RULE_numValue);
        try {
            setState(373);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 34, _ctx)) {
                case 1:
                    _localctx = new FloatNumValuePartContext(_localctx);
                    enterOuterAlt(_localctx, 1); {
                    setState(370);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case PRI_FLOAT: {
                            setState(366);
                            match(PRI_FLOAT);
                        }
                            break;
                        case PRI_DECIMAL: {
                            setState(367);
                            match(PRI_DECIMAL);
                        }
                            break;
                        case PRI_INTEGER: {
                            {
                                setState(368);
                                match(PRI_INTEGER);
                                setState(369);
                                match(DOT);
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                }
                    break;
                case 2:
                    _localctx = new IntNumValuePartContext(_localctx);
                    enterOuterAlt(_localctx, 2); {
                    setState(372);
                    match(PRI_INTEGER);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DtValueContext dtValue() throws RecognitionException {
        DtValueContext _localctx = new DtValueContext(_ctx, getState());
        enterRule(_localctx, 78, RULE_dtValue);
        try {
            setState(378);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case PRI_DATETIME_FMT:
                    _localctx = new FmtDateTimeValuePartContext(_localctx);
                    enterOuterAlt(_localctx, 1); {
                    setState(375);
                    match(PRI_DATETIME_FMT);
                    setState(376);
                    match(PRI_STR);
                }
                    break;
                case PRI_DATETIME:
                    _localctx = new DateTimeValueValuePartContext(_localctx);
                    enterOuterAlt(_localctx, 2); {
                    setState(377);
                    match(PRI_DATETIME);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CollectionValueContext collectionValue() throws RecognitionException {
        CollectionValueContext _localctx = new CollectionValueContext(_ctx, getState());
        enterRule(_localctx, 80, RULE_collectionValue);
        try {
            setState(382);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case LSBT:
                    enterOuterAlt(_localctx, 1); {
                    setState(380);
                    listValue();
                }
                    break;
                case OCBR:
                    enterOuterAlt(_localctx, 2); {
                    setState(381);
                    objectValue();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ListValueContext listValue() throws RecognitionException {
        ListValueContext _localctx = new ListValueContext(_ctx, getState());
        enterRule(_localctx, 82, RULE_listValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(384);
                match(LSBT);
                setState(393);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0
                     && ((1L << _la)
                         & ((1L << DEFINE) | (1L << DEFAULT) | (1L << ENUM) | (1L << HINT) | (1L << CAST) | (1L << CEILING) | (1L << DOWN) | (1L << FLOOR) | (1L << HALF_EVEN)
                            | (1L << HALF_DOWN) | (1L << HALF_UP) | (1L << UP) | (1L << UNNECESSARY) | (1L << BOOL) | (1L << DATETIME) | (1L << DATE) | (1L << DECIMAL)
                            | (1L << FLOAT) | (1L << INTEGER) | (1L << STRING) | (1L << TIME) | (1L << INSTANCEOF) | (1L << TYPEOF) | (1L << GOTO) | (1L << SWITCH) | (1L << CASE)
                            | (1L << FOR) | (1L << CONTINUE) | (1L << BREAK) | (1L << VAR) | (1L << DEF) | (1L << FUNC) | (1L << FALSE) | (1L << TRUE) | (1L << NULL))) != 0)
                    || ((((_la - 70)) & ~0x3f) == 0
                        && ((1L << (_la - 70))
                            & ((1L << (AO_PLUS - 70)) | (1L << (AO_MINUS - 70)) | (1L << (LSBT - 70)) | (1L << (OCBR - 70)) | (1L << (REF - 70)) | (1L << (PRI_DECIMAL - 70))
                               | (1L << (PRI_FLOAT - 70)) | (1L << (PRI_INTEGER - 70)) | (1L << (PRI_XOB_NUM - 70)) | (1L << (PRI_STR - 70)) | (1L << (PRI_DATETIME - 70))
                               | (1L << (PRI_DATETIME_FMT - 70)) | (1L << (IDENTIFIER - 70)) | (1L << (PRI_IDENTIFIER - 70)))) != 0)) {
                    {
                        setState(385);
                        anyPrimary();
                        setState(390);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == COMMA) {
                            {
                                {
                                    setState(386);
                                    match(COMMA);
                                    setState(387);
                                    anyPrimary();
                                }
                            }
                            setState(392);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(395);
                match(RSBT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ObjectValueContext objectValue() throws RecognitionException {
        ObjectValueContext _localctx = new ObjectValueContext(_ctx, getState());
        enterRule(_localctx, 84, RULE_objectValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(397);
                match(OCBR);
                setState(406);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == PRI_STR) {
                    {
                        setState(398);
                        objectItem();
                        setState(403);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == COMMA) {
                            {
                                {
                                    setState(399);
                                    match(COMMA);
                                    setState(400);
                                    objectItem();
                                }
                            }
                            setState(405);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(408);
                match(CCBR);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ObjectItemContext objectItem() throws RecognitionException {
        ObjectItemContext _localctx = new ObjectItemContext(_ctx, getState());
        enterRule(_localctx, 86, RULE_objectItem);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(410);
                match(PRI_STR);
                setState(411);
                match(COLON);
                setState(412);
                anyPrimary();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CastExprContext castExpr() throws RecognitionException {
        CastExprContext _localctx = new CastExprContext(_ctx, getState());
        enterRule(_localctx, 88, RULE_castExpr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(414);
                match(CAST);
                setState(415);
                match(LBT);
                setState(416);
                expression(0);
                setState(417);
                match(AS);
                setState(418);
                dataType();
                setState(419);
                match(RBT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DataTypeContext dataType() throws RecognitionException {
        DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
        enterRule(_localctx, 90, RULE_dataType);
        int _la;
        try {
            setState(427);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case DECIMAL:
                case FLOAT:
                case INTEGER:
                    _localctx = new CastNumContext(_localctx);
                    enterOuterAlt(_localctx, 1); {
                    setState(421);
                    _la = _input.LA(1);
                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DECIMAL) | (1L << FLOAT) | (1L << INTEGER))) != 0))) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF)
                            matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(423);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == LBT) {
                        {
                            setState(422);
                            numFmtValue();
                        }
                    }

                }
                    break;
                case BOOL:
                case DATETIME:
                case DATE:
                case STRING:
                case TIME:
                    _localctx = new CastOtherContext(_localctx);
                    enterOuterAlt(_localctx, 2); {
                    setState(425);
                    _la = _input.LA(1);
                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << DATETIME) | (1L << DATE) | (1L << STRING) | (1L << TIME))) != 0))) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF)
                            matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                }
                    break;
                case PRI_DATETIME_FMT:
                    _localctx = new CastDtFmtContext(_localctx);
                    enterOuterAlt(_localctx, 3); {
                    setState(426);
                    match(PRI_DATETIME_FMT);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final NumFmtValueContext numFmtValue() throws RecognitionException {
        NumFmtValueContext _localctx = new NumFmtValueContext(_ctx, getState());
        enterRule(_localctx, 92, RULE_numFmtValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(429);
                match(LBT);
                setState(430);
                match(PRI_INTEGER);
                setState(433);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == COMMA) {
                    {
                        setState(431);
                        match(COMMA);
                        setState(432);
                        numFmtRounding();
                    }
                }

                setState(435);
                match(RBT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final NumFmtRoundingContext numFmtRounding() throws RecognitionException {
        NumFmtRoundingContext _localctx = new NumFmtRoundingContext(_ctx, getState());
        enterRule(_localctx, 94, RULE_numFmtRounding);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(437);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CEILING) | (1L << DOWN) | (1L << FLOOR) | (1L << HALF_EVEN) | (1L << HALF_DOWN) | (1L << HALF_UP) | (1L << UP)
                                                               | (1L << UNNECESSARY))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AllIdentifierKeywordsContext allIdentifierKeywords() throws RecognitionException {
        AllIdentifierKeywordsContext _localctx = new AllIdentifierKeywordsContext(_ctx, getState());
        enterRule(_localctx, 96, RULE_allIdentifierKeywords);
        try {
            setState(472);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case DEFINE:
                    enterOuterAlt(_localctx, 1); {
                    setState(439);
                    match(DEFINE);
                }
                    break;
                case DEFAULT:
                    enterOuterAlt(_localctx, 2); {
                    setState(440);
                    match(DEFAULT);
                }
                    break;
                case ENUM:
                    enterOuterAlt(_localctx, 3); {
                    setState(441);
                    match(ENUM);
                }
                    break;
                case HINT:
                    enterOuterAlt(_localctx, 4); {
                    setState(442);
                    match(HINT);
                }
                    break;
                case CAST:
                    enterOuterAlt(_localctx, 5); {
                    setState(443);
                    match(CAST);
                }
                    break;
                case CEILING:
                    enterOuterAlt(_localctx, 6); {
                    setState(444);
                    match(CEILING);
                }
                    break;
                case DOWN:
                    enterOuterAlt(_localctx, 7); {
                    setState(445);
                    match(DOWN);
                }
                    break;
                case FLOOR:
                    enterOuterAlt(_localctx, 8); {
                    setState(446);
                    match(FLOOR);
                }
                    break;
                case HALF_EVEN:
                    enterOuterAlt(_localctx, 9); {
                    setState(447);
                    match(HALF_EVEN);
                }
                    break;
                case HALF_DOWN:
                    enterOuterAlt(_localctx, 10); {
                    setState(448);
                    match(HALF_DOWN);
                }
                    break;
                case HALF_UP:
                    enterOuterAlt(_localctx, 11); {
                    setState(449);
                    match(HALF_UP);
                }
                    break;
                case UP:
                    enterOuterAlt(_localctx, 12); {
                    setState(450);
                    match(UP);
                }
                    break;
                case UNNECESSARY:
                    enterOuterAlt(_localctx, 13); {
                    setState(451);
                    match(UNNECESSARY);
                }
                    break;
                case BOOL:
                    enterOuterAlt(_localctx, 14); {
                    setState(452);
                    match(BOOL);
                }
                    break;
                case DATETIME:
                    enterOuterAlt(_localctx, 15); {
                    setState(453);
                    match(DATETIME);
                }
                    break;
                case DATE:
                    enterOuterAlt(_localctx, 16); {
                    setState(454);
                    match(DATE);
                }
                    break;
                case DECIMAL:
                    enterOuterAlt(_localctx, 17); {
                    setState(455);
                    match(DECIMAL);
                }
                    break;
                case FLOAT:
                    enterOuterAlt(_localctx, 18); {
                    setState(456);
                    match(FLOAT);
                }
                    break;
                case INTEGER:
                    enterOuterAlt(_localctx, 19); {
                    setState(457);
                    match(INTEGER);
                }
                    break;
                case STRING:
                    enterOuterAlt(_localctx, 20); {
                    setState(458);
                    match(STRING);
                }
                    break;
                case TIME:
                    enterOuterAlt(_localctx, 21); {
                    setState(459);
                    match(TIME);
                }
                    break;
                case INSTANCEOF:
                    enterOuterAlt(_localctx, 22); {
                    setState(460);
                    match(INSTANCEOF);
                }
                    break;
                case TYPEOF:
                    enterOuterAlt(_localctx, 23); {
                    setState(461);
                    match(TYPEOF);
                    setState(462);
                    match(LABEL);
                }
                    break;
                case GOTO:
                    enterOuterAlt(_localctx, 24); {
                    setState(463);
                    match(GOTO);
                }
                    break;
                case SWITCH:
                    enterOuterAlt(_localctx, 25); {
                    setState(464);
                    match(SWITCH);
                }
                    break;
                case CASE:
                    enterOuterAlt(_localctx, 26); {
                    setState(465);
                    match(CASE);
                }
                    break;
                case FOR:
                    enterOuterAlt(_localctx, 27); {
                    setState(466);
                    match(FOR);
                }
                    break;
                case CONTINUE:
                    enterOuterAlt(_localctx, 28); {
                    setState(467);
                    match(CONTINUE);
                }
                    break;
                case BREAK:
                    enterOuterAlt(_localctx, 29); {
                    setState(468);
                    match(BREAK);
                }
                    break;
                case VAR:
                    enterOuterAlt(_localctx, 30); {
                    setState(469);
                    match(VAR);
                }
                    break;
                case DEF:
                    enterOuterAlt(_localctx, 31); {
                    setState(470);
                    match(DEF);
                }
                    break;
                case FUNC:
                    enterOuterAlt(_localctx, 32); {
                    setState(471);
                    match(FUNC);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 18:
                return expression_sempred((ExpressionContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 9);
            case 1:
                return precpred(_ctx, 8);
            case 2:
                return precpred(_ctx, 7);
            case 3:
                return precpred(_ctx, 6);
            case 4:
                return precpred(_ctx, 5);
            case 5:
                return precpred(_ctx, 4);
            case 6:
                return precpred(_ctx, 3);
            case 7:
                return precpred(_ctx, 2);
            case 8:
                return precpred(_ctx, 1);
            case 9:
                return precpred(_ctx, 13);
            case 10:
                return precpred(_ctx, 12);
        }
        return true;
    }

    public static class RootContext extends ParserRuleContext {

        public RootContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode EOF() {
            return getToken(DetectRulesParser.EOF, 0);
        }

        public List<DefineStatementContext> defineStatement() {
            return getRuleContexts(DefineStatementContext.class);
        }

        public DefineStatementContext defineStatement(int i) {
            return getRuleContext(DefineStatementContext.class, i);
        }

        public MultiStatementContext multiStatement() {
            return getRuleContext(MultiStatementContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_root; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterRoot(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitRoot(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitRoot(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CommentContext extends ParserRuleContext {

        public CommentContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode COMMENT1() {
            return getToken(DetectRulesParser.COMMENT1, 0);
        }

        public TerminalNode COMMENT3() {
            return getToken(DetectRulesParser.COMMENT3, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_comment; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterComment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitComment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitComment(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class MultiStatementContext extends ParserRuleContext {

        public MultiStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public List<SingleStatementContext> singleStatement() {
            return getRuleContexts(SingleStatementContext.class);
        }

        public SingleStatementContext singleStatement(int i) {
            return getRuleContext(SingleStatementContext.class, i);
        }

        @Override
        public int getRuleIndex() { return RULE_multiStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterMultiStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitMultiStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitMultiStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class SingleStatementContext extends ParserRuleContext {

        public SingleStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public IfStatementContext ifStatement() {
            return getRuleContext(IfStatementContext.class, 0);
        }

        public ReturnStatementContext returnStatement() {
            return getRuleContext(ReturnStatementContext.class, 0);
        }

        public ThrowStatementContext throwStatement() {
            return getRuleContext(ThrowStatementContext.class, 0);
        }

        public AssertStatementContext assertStatement() {
            return getRuleContext(AssertStatementContext.class, 0);
        }

        public AssignStatementContext assignStatement() {
            return getRuleContext(AssignStatementContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_singleStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterSingleStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitSingleStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitSingleStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DefineStatementContext extends ParserRuleContext {

        public DefineStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode WELL() {
            return getToken(DetectRulesParser.WELL, 0);
        }

        public TerminalNode DEFINE() {
            return getToken(DetectRulesParser.DEFINE, 0);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        public TerminalNode AS() {
            return getToken(DetectRulesParser.AS, 0);
        }

        public DefineTypeContext defineType() {
            return getRuleContext(DefineTypeContext.class, 0);
        }

        public DefineDefaultContext defineDefault() {
            return getRuleContext(DefineDefaultContext.class, 0);
        }

        public DefineEnumContext defineEnum() {
            return getRuleContext(DefineEnumContext.class, 0);
        }

        public DefineHINTContext defineHINT() {
            return getRuleContext(DefineHINTContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_defineStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDefineStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDefineStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDefineStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DefineTypeContext extends ParserRuleContext {

        public DefineTypeContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode INTEGER() {
            return getToken(DetectRulesParser.INTEGER, 0);
        }

        public TerminalNode FLOAT() {
            return getToken(DetectRulesParser.FLOAT, 0);
        }

        public TerminalNode DECIMAL() {
            return getToken(DetectRulesParser.DECIMAL, 0);
        }

        public TerminalNode BOOL() {
            return getToken(DetectRulesParser.BOOL, 0);
        }

        public TerminalNode STRING() {
            return getToken(DetectRulesParser.STRING, 0);
        }

        public TerminalNode DATE() {
            return getToken(DetectRulesParser.DATE, 0);
        }

        public TerminalNode TIME() {
            return getToken(DetectRulesParser.TIME, 0);
        }

        public TerminalNode DATETIME() {
            return getToken(DetectRulesParser.DATETIME, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_defineType; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDefineType(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDefineType(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDefineType(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DefineDefaultContext extends ParserRuleContext {

        public DefineDefaultContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode DEFAULT() {
            return getToken(DetectRulesParser.DEFAULT, 0);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_defineDefault; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDefineDefault(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDefineDefault(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDefineDefault(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DefineEnumContext extends ParserRuleContext {

        public DefineEnumContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode ENUM() {
            return getToken(DetectRulesParser.ENUM, 0);
        }

        public TerminalNode LSBT() {
            return getToken(DetectRulesParser.LSBT, 0);
        }

        public TerminalNode RSBT() {
            return getToken(DetectRulesParser.RSBT, 0);
        }

        public List<TerminalNode> PRI_STR() {
            return getTokens(DetectRulesParser.PRI_STR);
        }

        public TerminalNode PRI_STR(int i) {
            return getToken(DetectRulesParser.PRI_STR, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(DetectRulesParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(DetectRulesParser.COMMA, i);
        }

        @Override
        public int getRuleIndex() { return RULE_defineEnum; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDefineEnum(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDefineEnum(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDefineEnum(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DefineHINTContext extends ParserRuleContext {

        public DefineHINTContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode HINT() {
            return getToken(DetectRulesParser.HINT, 0);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_defineHINT; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDefineHINT(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDefineHINT(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDefineHINT(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class IfStatementContext extends ParserRuleContext {

        public IfStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public ConditionThenContext conditionThen() {
            return getRuleContext(ConditionThenContext.class, 0);
        }

        public TerminalNode END() {
            return getToken(DetectRulesParser.END, 0);
        }

        public List<ElseifThenContext> elseifThen() {
            return getRuleContexts(ElseifThenContext.class);
        }

        public ElseifThenContext elseifThen(int i) {
            return getRuleContext(ElseifThenContext.class, i);
        }

        public ElseThenContext elseThen() {
            return getRuleContext(ElseThenContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_ifStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterIfStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitIfStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitIfStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ConditionThenContext extends ParserRuleContext {

        public ConditionThenContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode IF() {
            return getToken(DetectRulesParser.IF, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode THEN() {
            return getToken(DetectRulesParser.THEN, 0);
        }

        public MultiStatementContext multiStatement() {
            return getRuleContext(MultiStatementContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_conditionThen; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterConditionThen(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitConditionThen(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitConditionThen(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ElseifThenContext extends ParserRuleContext {

        public ElseifThenContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode ELSEIF() {
            return getToken(DetectRulesParser.ELSEIF, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode THEN() {
            return getToken(DetectRulesParser.THEN, 0);
        }

        public MultiStatementContext multiStatement() {
            return getRuleContext(MultiStatementContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_elseifThen; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterElseifThen(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitElseifThen(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitElseifThen(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ElseThenContext extends ParserRuleContext {

        public ElseThenContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode ELSE() {
            return getToken(DetectRulesParser.ELSE, 0);
        }

        public MultiStatementContext multiStatement() {
            return getRuleContext(MultiStatementContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_elseThen; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterElseThen(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitElseThen(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitElseThen(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ReturnStatementContext extends ParserRuleContext {

        public ReturnStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode RETURN() {
            return getToken(DetectRulesParser.RETURN, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_returnStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterReturnStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitReturnStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitReturnStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ThrowStatementContext extends ParserRuleContext {

        public ThrowStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode THROW() {
            return getToken(DetectRulesParser.THROW, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_throwStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterThrowStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitThrowStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitThrowStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AssertStatementContext extends ParserRuleContext {

        public AssertStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode ASSERT() {
            return getToken(DetectRulesParser.ASSERT, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public AssertExprContext assertExpr() {
            return getRuleContext(AssertExprContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_assertStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAssertStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAssertStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAssertStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AssertExprContext extends ParserRuleContext {

        public AssertExprContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode COMMA() {
            return getToken(DetectRulesParser.COMMA, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_assertExpr; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAssertExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAssertExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAssertExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AssignStatementContext extends ParserRuleContext {

        public AssignStatementContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DetectRulesParser.IDENTIFIER, 0);
        }

        public TerminalNode ASSIGN() {
            return getToken(DetectRulesParser.ASSIGN, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_assignStatement; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAssignStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAssignStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAssignStatement(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ExpressionContext extends ParserRuleContext {

        public ExpressionContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public ExpressionContext(){
        }

        @Override
        public int getRuleIndex() { return RULE_expression; }

        public void copyFrom(ExpressionContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class LogicOrExprContext extends ExpressionContext {

        public LogicOrExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode LO_OR() {
            return getToken(DetectRulesParser.LO_OR, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterLogicOrExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitLogicOrExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitLogicOrExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnaryNotPlusExprContext extends ExpressionContext {

        public UnaryNotPlusExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode LO_NOT() {
            return getToken(DetectRulesParser.LO_NOT, 0);
        }

        public TerminalNode BO_NOT() {
            return getToken(DetectRulesParser.BO_NOT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnaryNotPlusExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnaryNotPlusExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnaryNotPlusExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class FuncOrDetectExprContext extends ExpressionContext {

        public FuncOrDetectExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public FunOrDetectExprContext funOrDetectExpr() {
            return getRuleContext(FunOrDetectExprContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterFuncOrDetectExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitFuncOrDetectExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitFuncOrDetectExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnaryBeforeDecrExprContext extends ExpressionContext {

        public UnaryBeforeDecrExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode AO_DECR() {
            return getToken(DetectRulesParser.AO_DECR, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnaryBeforeDecrExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnaryBeforeDecrExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnaryBeforeDecrExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnaryAfterDecrExprContext extends ExpressionContext {

        public UnaryAfterDecrExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode AO_DECR() {
            return getToken(DetectRulesParser.AO_DECR, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnaryAfterDecrExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnaryAfterDecrExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnaryAfterDecrExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class MatchExprContext extends ExpressionContext {

        public MatchExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public MatchSymbolContext matchSymbol() {
            return getRuleContext(MatchSymbolContext.class, 0);
        }

        public TerminalNode NOT() {
            return getToken(DetectRulesParser.NOT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterMatchExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitMatchExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitMatchExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AtomExprContext extends ExpressionContext {

        public AtomExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public AnyPrimaryContext anyPrimary() {
            return getRuleContext(AnyPrimaryContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAtomExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAtomExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAtomExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class PrivilegeExprContext extends ExpressionContext {

        public PrivilegeExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode LBT() {
            return getToken(DetectRulesParser.LBT, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode RBT() {
            return getToken(DetectRulesParser.RBT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterPrivilegeExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitPrivilegeExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitPrivilegeExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AdditiveExprContext extends ExpressionContext {

        public AdditiveExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public AdditiveSymbolContext additiveSymbol() {
            return getRuleContext(AdditiveSymbolContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAdditiveExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAdditiveExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAdditiveExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ShiftExprContext extends ExpressionContext {

        public ShiftExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public ShiftSymbolContext shiftSymbol() {
            return getRuleContext(ShiftSymbolContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterShiftExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitShiftExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitShiftExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class BitwiseExprContext extends ExpressionContext {

        public BitwiseExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public BitwiseSymbolContext bitwiseSymbol() {
            return getRuleContext(BitwiseSymbolContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterBitwiseExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitBitwiseExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitBitwiseExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnaryExprContext extends ExpressionContext {

        public UnaryExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public UnarySymbolContext unarySymbol() {
            return getRuleContext(UnarySymbolContext.class, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnaryExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnaryExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnaryExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class LogicAndExprContext extends ExpressionContext {

        public LogicAndExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode LO_AND() {
            return getToken(DetectRulesParser.LO_AND, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterLogicAndExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitLogicAndExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitLogicAndExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class TernaryExprContext extends ExpressionContext {

        public TernaryExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode QUE() {
            return getToken(DetectRulesParser.QUE, 0);
        }

        public TerminalNode COLON() {
            return getToken(DetectRulesParser.COLON, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterTernaryExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitTernaryExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitTernaryExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnaryBeforeIncreExprContext extends ExpressionContext {

        public UnaryBeforeIncreExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode AO_INCR() {
            return getToken(DetectRulesParser.AO_INCR, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnaryBeforeIncreExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnaryBeforeIncreExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnaryBeforeIncreExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnaryAfterIncreExprContext extends ExpressionContext {

        public UnaryAfterIncreExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode AO_INCR() {
            return getToken(DetectRulesParser.AO_INCR, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnaryAfterIncreExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnaryAfterIncreExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnaryAfterIncreExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AccessParamExprContext extends ExpressionContext {

        public AccessParamExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public ParamExprContext paramExpr() {
            return getRuleContext(ParamExprContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAccessParamExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAccessParamExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAccessParamExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class MultiplicativeExprContext extends ExpressionContext {

        public MultiplicativeExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public MultiplicativeSymbolContext multiplicativeSymbol() {
            return getRuleContext(MultiplicativeSymbolContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterMultiplicativeExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitMultiplicativeExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitMultiplicativeExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CompareExprContext extends ExpressionContext {

        public CompareExprContext(ExpressionContext ctx){
            copyFrom(ctx);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public CompareSymbolContext compareSymbol() {
            return getRuleContext(CompareSymbolContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCompareExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCompareExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCompareExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class UnarySymbolContext extends ParserRuleContext {

        public UnarySymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode AO_PLUS() {
            return getToken(DetectRulesParser.AO_PLUS, 0);
        }

        public TerminalNode AO_MINUS() {
            return getToken(DetectRulesParser.AO_MINUS, 0);
        }

        public TerminalNode NOT() {
            return getToken(DetectRulesParser.NOT, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_unarySymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterUnarySymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitUnarySymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitUnarySymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class MultiplicativeSymbolContext extends ParserRuleContext {

        public MultiplicativeSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode AO_MUL() {
            return getToken(DetectRulesParser.AO_MUL, 0);
        }

        public TerminalNode AO_DIV() {
            return getToken(DetectRulesParser.AO_DIV, 0);
        }

        public TerminalNode AO_MOD() {
            return getToken(DetectRulesParser.AO_MOD, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_multiplicativeSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterMultiplicativeSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitMultiplicativeSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitMultiplicativeSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AdditiveSymbolContext extends ParserRuleContext {

        public AdditiveSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode AO_PLUS() {
            return getToken(DetectRulesParser.AO_PLUS, 0);
        }

        public TerminalNode AO_MINUS() {
            return getToken(DetectRulesParser.AO_MINUS, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_additiveSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAdditiveSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAdditiveSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAdditiveSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class BitwiseSymbolContext extends ParserRuleContext {

        public BitwiseSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode BO_AND() {
            return getToken(DetectRulesParser.BO_AND, 0);
        }

        public TerminalNode BO_OR() {
            return getToken(DetectRulesParser.BO_OR, 0);
        }

        public TerminalNode BO_XOR() {
            return getToken(DetectRulesParser.BO_XOR, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_bitwiseSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterBitwiseSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitBitwiseSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitBitwiseSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ShiftSymbolContext extends ParserRuleContext {

        public ShiftSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode BO_RSHIFT2() {
            return getToken(DetectRulesParser.BO_RSHIFT2, 0);
        }

        public TerminalNode BO_RSHIFT() {
            return getToken(DetectRulesParser.BO_RSHIFT, 0);
        }

        public TerminalNode BO_LSHIFT() {
            return getToken(DetectRulesParser.BO_LSHIFT, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_shiftSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterShiftSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitShiftSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitShiftSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CompareSymbolContext extends ParserRuleContext {

        public CompareSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode CO_GE() {
            return getToken(DetectRulesParser.CO_GE, 0);
        }

        public TerminalNode CO_GT() {
            return getToken(DetectRulesParser.CO_GT, 0);
        }

        public TerminalNode CO_LE() {
            return getToken(DetectRulesParser.CO_LE, 0);
        }

        public TerminalNode CO_LT() {
            return getToken(DetectRulesParser.CO_LT, 0);
        }

        public TerminalNode CO_EQ() {
            return getToken(DetectRulesParser.CO_EQ, 0);
        }

        public TerminalNode CO_IEQ() {
            return getToken(DetectRulesParser.CO_IEQ, 0);
        }

        public TerminalNode CO_NE() {
            return getToken(DetectRulesParser.CO_NE, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_compareSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCompareSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCompareSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCompareSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class MatchSymbolContext extends ParserRuleContext {

        public MatchSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode IN() {
            return getToken(DetectRulesParser.IN, 0);
        }

        public TerminalNode MATCHES() {
            return getToken(DetectRulesParser.MATCHES, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_matchSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterMatchSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitMatchSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitMatchSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ParamExprContext extends ParserRuleContext {

        public ParamExprContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode OREP() {
            return getToken(DetectRulesParser.OREP, 0);
        }

        public TerminalNode CCBR() {
            return getToken(DetectRulesParser.CCBR, 0);
        }

        public IdentifyContext identify() {
            return getRuleContext(IdentifyContext.class, 0);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_paramExpr; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterParamExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitParamExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitParamExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class FunOrDetectExprContext extends ParserRuleContext {

        public FunOrDetectExprContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public DetectPartContext detectPart() {
            return getRuleContext(DetectPartContext.class, 0);
        }

        public FuncPartContext funcPart() {
            return getRuleContext(FuncPartContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_funOrDetectExpr; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterFunOrDetectExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitFunOrDetectExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitFunOrDetectExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DetectPartContext extends ParserRuleContext {

        public DetectPartContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode REF() {
            return getToken(DetectRulesParser.REF, 0);
        }

        public IdentifyExprContext identifyExpr() {
            return getRuleContext(IdentifyExprContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_detectPart; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDetectPart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDetectPart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDetectPart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class FuncPartContext extends ParserRuleContext {

        public FuncPartContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode LBT() {
            return getToken(DetectRulesParser.LBT, 0);
        }

        public TerminalNode RBT() {
            return getToken(DetectRulesParser.RBT, 0);
        }

        public FuncArgPartContext funcArgPart() {
            return getRuleContext(FuncArgPartContext.class, 0);
        }

        public SubExprContext subExpr() {
            return getRuleContext(SubExprContext.class, 0);
        }

        public TerminalNode DOT() {
            return getToken(DetectRulesParser.DOT, 0);
        }

        public IdentifyExprContext identifyExpr() {
            return getRuleContext(IdentifyExprContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_funcPart; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterFuncPart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitFuncPart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitFuncPart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class FuncArgPartContext extends ParserRuleContext {

        public FuncArgPartContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(DetectRulesParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(DetectRulesParser.COMMA, i);
        }

        @Override
        public int getRuleIndex() { return RULE_funcArgPart; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterFuncArgPart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitFuncArgPart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitFuncArgPart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class IdentifyExprContext extends ParserRuleContext {

        public IdentifyExprContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public IdentifyPartContext identifyPart() {
            return getRuleContext(IdentifyPartContext.class, 0);
        }

        public SubExprContext subExpr() {
            return getRuleContext(SubExprContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_identifyExpr; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterIdentifyExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitIdentifyExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitIdentifyExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class SubExprContext extends ParserRuleContext {

        public SubExprContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode LSBT() {
            return getToken(DetectRulesParser.LSBT, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode RSBT() {
            return getToken(DetectRulesParser.RSBT, 0);
        }

        public SubExprContext subExpr() {
            return getRuleContext(SubExprContext.class, 0);
        }

        public TerminalNode DOT() {
            return getToken(DetectRulesParser.DOT, 0);
        }

        public IdentifyExprContext identifyExpr() {
            return getRuleContext(IdentifyExprContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_subExpr; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterSubExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitSubExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitSubExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class IdentifyPartContext extends ParserRuleContext {

        public IdentifyPartContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public List<IdentifyContext> identify() {
            return getRuleContexts(IdentifyContext.class);
        }

        public IdentifyContext identify(int i) {
            return getRuleContext(IdentifyContext.class, i);
        }

        public List<TerminalNode> DOT() {
            return getTokens(DetectRulesParser.DOT);
        }

        public TerminalNode DOT(int i) {
            return getToken(DetectRulesParser.DOT, i);
        }

        @Override
        public int getRuleIndex() { return RULE_identifyPart; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterIdentifyPart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitIdentifyPart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitIdentifyPart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class IdentifyContext extends ParserRuleContext {

        public IdentifyContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DetectRulesParser.IDENTIFIER, 0);
        }

        public TerminalNode PRI_IDENTIFIER() {
            return getToken(DetectRulesParser.PRI_IDENTIFIER, 0);
        }

        public AllIdentifierKeywordsContext allIdentifierKeywords() {
            return getRuleContext(AllIdentifierKeywordsContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_identify; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterIdentify(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitIdentify(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitIdentify(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AnyPrimaryContext extends ParserRuleContext {

        public AnyPrimaryContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public FunOrDetectExprContext funOrDetectExpr() {
            return getRuleContext(FunOrDetectExprContext.class, 0);
        }

        public PrimitiveValueContext primitiveValue() {
            return getRuleContext(PrimitiveValueContext.class, 0);
        }

        public CollectionValueContext collectionValue() {
            return getRuleContext(CollectionValueContext.class, 0);
        }

        public CastExprContext castExpr() {
            return getRuleContext(CastExprContext.class, 0);
        }

        public IdentifyExprContext identifyExpr() {
            return getRuleContext(IdentifyExprContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_anyPrimary; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAnyPrimary(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAnyPrimary(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAnyPrimary(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class PrimitiveValueContext extends ParserRuleContext {

        public PrimitiveValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public PrimitiveValueContext(){
        }

        @Override
        public int getRuleIndex() { return RULE_primitiveValue; }

        public void copyFrom(PrimitiveValueContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class StringValueContext extends PrimitiveValueContext {

        public StringValueContext(PrimitiveValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterStringValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitStringValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitStringValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class NullVaueContext extends PrimitiveValueContext {

        public NullVaueContext(PrimitiveValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode NULL() {
            return getToken(DetectRulesParser.NULL, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterNullVaue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitNullVaue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitNullVaue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class XobValueContext extends PrimitiveValueContext {

        public XobValueContext(PrimitiveValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_XOB_NUM() {
            return getToken(DetectRulesParser.PRI_XOB_NUM, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterXobValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitXobValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitXobValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DatetimeValueContext extends PrimitiveValueContext {

        public DatetimeValueContext(PrimitiveValueContext ctx){
            copyFrom(ctx);
        }

        public DtValueContext dtValue() {
            return getRuleContext(DtValueContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDatetimeValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDatetimeValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDatetimeValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class BooleanValueContext extends PrimitiveValueContext {

        public BooleanValueContext(PrimitiveValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode TRUE() {
            return getToken(DetectRulesParser.TRUE, 0);
        }

        public TerminalNode FALSE() {
            return getToken(DetectRulesParser.FALSE, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterBooleanValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitBooleanValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitBooleanValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class NumberValueContext extends PrimitiveValueContext {

        public NumberValueContext(PrimitiveValueContext ctx){
            copyFrom(ctx);
        }

        public NumValueContext numValue() {
            return getRuleContext(NumValueContext.class, 0);
        }

        public SignSymbolContext signSymbol() {
            return getRuleContext(SignSymbolContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterNumberValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitNumberValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitNumberValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class SignSymbolContext extends ParserRuleContext {

        public SignSymbolContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode AO_PLUS() {
            return getToken(DetectRulesParser.AO_PLUS, 0);
        }

        public TerminalNode AO_MINUS() {
            return getToken(DetectRulesParser.AO_MINUS, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_signSymbol; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterSignSymbol(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitSignSymbol(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitSignSymbol(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class NumValueContext extends ParserRuleContext {

        public NumValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public NumValueContext(){
        }

        @Override
        public int getRuleIndex() { return RULE_numValue; }

        public void copyFrom(NumValueContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class FloatNumValuePartContext extends NumValueContext {

        public FloatNumValuePartContext(NumValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_FLOAT() {
            return getToken(DetectRulesParser.PRI_FLOAT, 0);
        }

        public TerminalNode PRI_DECIMAL() {
            return getToken(DetectRulesParser.PRI_DECIMAL, 0);
        }

        public TerminalNode PRI_INTEGER() {
            return getToken(DetectRulesParser.PRI_INTEGER, 0);
        }

        public TerminalNode DOT() {
            return getToken(DetectRulesParser.DOT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterFloatNumValuePart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitFloatNumValuePart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitFloatNumValuePart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class IntNumValuePartContext extends NumValueContext {

        public IntNumValuePartContext(NumValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_INTEGER() {
            return getToken(DetectRulesParser.PRI_INTEGER, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterIntNumValuePart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitIntNumValuePart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitIntNumValuePart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DtValueContext extends ParserRuleContext {

        public DtValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public DtValueContext(){
        }

        @Override
        public int getRuleIndex() { return RULE_dtValue; }

        public void copyFrom(DtValueContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class DateTimeValueValuePartContext extends DtValueContext {

        public DateTimeValueValuePartContext(DtValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_DATETIME() {
            return getToken(DetectRulesParser.PRI_DATETIME, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterDateTimeValueValuePart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitDateTimeValueValuePart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitDateTimeValueValuePart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class FmtDateTimeValuePartContext extends DtValueContext {

        public FmtDateTimeValuePartContext(DtValueContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_DATETIME_FMT() {
            return getToken(DetectRulesParser.PRI_DATETIME_FMT, 0);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterFmtDateTimeValuePart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitFmtDateTimeValuePart(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitFmtDateTimeValuePart(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CollectionValueContext extends ParserRuleContext {

        public CollectionValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public ListValueContext listValue() {
            return getRuleContext(ListValueContext.class, 0);
        }

        public ObjectValueContext objectValue() {
            return getRuleContext(ObjectValueContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_collectionValue; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCollectionValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCollectionValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCollectionValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ListValueContext extends ParserRuleContext {

        public ListValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode LSBT() {
            return getToken(DetectRulesParser.LSBT, 0);
        }

        public TerminalNode RSBT() {
            return getToken(DetectRulesParser.RSBT, 0);
        }

        public List<AnyPrimaryContext> anyPrimary() {
            return getRuleContexts(AnyPrimaryContext.class);
        }

        public AnyPrimaryContext anyPrimary(int i) {
            return getRuleContext(AnyPrimaryContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(DetectRulesParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(DetectRulesParser.COMMA, i);
        }

        @Override
        public int getRuleIndex() { return RULE_listValue; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterListValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitListValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitListValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ObjectValueContext extends ParserRuleContext {

        public ObjectValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode OCBR() {
            return getToken(DetectRulesParser.OCBR, 0);
        }

        public TerminalNode CCBR() {
            return getToken(DetectRulesParser.CCBR, 0);
        }

        public List<ObjectItemContext> objectItem() {
            return getRuleContexts(ObjectItemContext.class);
        }

        public ObjectItemContext objectItem(int i) {
            return getRuleContext(ObjectItemContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(DetectRulesParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(DetectRulesParser.COMMA, i);
        }

        @Override
        public int getRuleIndex() { return RULE_objectValue; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterObjectValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitObjectValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitObjectValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class ObjectItemContext extends ParserRuleContext {

        public ObjectItemContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode PRI_STR() {
            return getToken(DetectRulesParser.PRI_STR, 0);
        }

        public TerminalNode COLON() {
            return getToken(DetectRulesParser.COLON, 0);
        }

        public AnyPrimaryContext anyPrimary() {
            return getRuleContext(AnyPrimaryContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_objectItem; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterObjectItem(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitObjectItem(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitObjectItem(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CastExprContext extends ParserRuleContext {

        public CastExprContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode CAST() {
            return getToken(DetectRulesParser.CAST, 0);
        }

        public TerminalNode LBT() {
            return getToken(DetectRulesParser.LBT, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode AS() {
            return getToken(DetectRulesParser.AS, 0);
        }

        public DataTypeContext dataType() {
            return getRuleContext(DataTypeContext.class, 0);
        }

        public TerminalNode RBT() {
            return getToken(DetectRulesParser.RBT, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_castExpr; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCastExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCastExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCastExpr(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class DataTypeContext extends ParserRuleContext {

        public DataTypeContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public DataTypeContext(){
        }

        @Override
        public int getRuleIndex() { return RULE_dataType; }

        public void copyFrom(DataTypeContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class CastDtFmtContext extends DataTypeContext {

        public CastDtFmtContext(DataTypeContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode PRI_DATETIME_FMT() {
            return getToken(DetectRulesParser.PRI_DATETIME_FMT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCastDtFmt(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCastDtFmt(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCastDtFmt(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CastOtherContext extends DataTypeContext {

        public CastOtherContext(DataTypeContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode BOOL() {
            return getToken(DetectRulesParser.BOOL, 0);
        }

        public TerminalNode STRING() {
            return getToken(DetectRulesParser.STRING, 0);
        }

        public TerminalNode DATE() {
            return getToken(DetectRulesParser.DATE, 0);
        }

        public TerminalNode TIME() {
            return getToken(DetectRulesParser.TIME, 0);
        }

        public TerminalNode DATETIME() {
            return getToken(DetectRulesParser.DATETIME, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCastOther(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCastOther(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCastOther(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class CastNumContext extends DataTypeContext {

        public CastNumContext(DataTypeContext ctx){
            copyFrom(ctx);
        }

        public TerminalNode INTEGER() {
            return getToken(DetectRulesParser.INTEGER, 0);
        }

        public TerminalNode FLOAT() {
            return getToken(DetectRulesParser.FLOAT, 0);
        }

        public TerminalNode DECIMAL() {
            return getToken(DetectRulesParser.DECIMAL, 0);
        }

        public NumFmtValueContext numFmtValue() {
            return getRuleContext(NumFmtValueContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterCastNum(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitCastNum(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitCastNum(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class NumFmtValueContext extends ParserRuleContext {

        public NumFmtValueContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode LBT() {
            return getToken(DetectRulesParser.LBT, 0);
        }

        public TerminalNode PRI_INTEGER() {
            return getToken(DetectRulesParser.PRI_INTEGER, 0);
        }

        public TerminalNode RBT() {
            return getToken(DetectRulesParser.RBT, 0);
        }

        public TerminalNode COMMA() {
            return getToken(DetectRulesParser.COMMA, 0);
        }

        public NumFmtRoundingContext numFmtRounding() {
            return getRuleContext(NumFmtRoundingContext.class, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_numFmtValue; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterNumFmtValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitNumFmtValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitNumFmtValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class NumFmtRoundingContext extends ParserRuleContext {

        public NumFmtRoundingContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode UP() {
            return getToken(DetectRulesParser.UP, 0);
        }

        public TerminalNode DOWN() {
            return getToken(DetectRulesParser.DOWN, 0);
        }

        public TerminalNode CEILING() {
            return getToken(DetectRulesParser.CEILING, 0);
        }

        public TerminalNode FLOOR() {
            return getToken(DetectRulesParser.FLOOR, 0);
        }

        public TerminalNode HALF_UP() {
            return getToken(DetectRulesParser.HALF_UP, 0);
        }

        public TerminalNode HALF_DOWN() {
            return getToken(DetectRulesParser.HALF_DOWN, 0);
        }

        public TerminalNode HALF_EVEN() {
            return getToken(DetectRulesParser.HALF_EVEN, 0);
        }

        public TerminalNode UNNECESSARY() {
            return getToken(DetectRulesParser.UNNECESSARY, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_numFmtRounding; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterNumFmtRounding(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitNumFmtRounding(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitNumFmtRounding(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class AllIdentifierKeywordsContext extends ParserRuleContext {

        public AllIdentifierKeywordsContext(ParserRuleContext parent, int invokingState){
            super(parent, invokingState);
        }

        public TerminalNode DEFINE() {
            return getToken(DetectRulesParser.DEFINE, 0);
        }

        public TerminalNode DEFAULT() {
            return getToken(DetectRulesParser.DEFAULT, 0);
        }

        public TerminalNode ENUM() {
            return getToken(DetectRulesParser.ENUM, 0);
        }

        public TerminalNode HINT() {
            return getToken(DetectRulesParser.HINT, 0);
        }

        public TerminalNode CAST() {
            return getToken(DetectRulesParser.CAST, 0);
        }

        public TerminalNode CEILING() {
            return getToken(DetectRulesParser.CEILING, 0);
        }

        public TerminalNode DOWN() {
            return getToken(DetectRulesParser.DOWN, 0);
        }

        public TerminalNode FLOOR() {
            return getToken(DetectRulesParser.FLOOR, 0);
        }

        public TerminalNode HALF_EVEN() {
            return getToken(DetectRulesParser.HALF_EVEN, 0);
        }

        public TerminalNode HALF_DOWN() {
            return getToken(DetectRulesParser.HALF_DOWN, 0);
        }

        public TerminalNode HALF_UP() {
            return getToken(DetectRulesParser.HALF_UP, 0);
        }

        public TerminalNode UP() {
            return getToken(DetectRulesParser.UP, 0);
        }

        public TerminalNode UNNECESSARY() {
            return getToken(DetectRulesParser.UNNECESSARY, 0);
        }

        public TerminalNode BOOL() {
            return getToken(DetectRulesParser.BOOL, 0);
        }

        public TerminalNode DATETIME() {
            return getToken(DetectRulesParser.DATETIME, 0);
        }

        public TerminalNode DATE() {
            return getToken(DetectRulesParser.DATE, 0);
        }

        public TerminalNode DECIMAL() {
            return getToken(DetectRulesParser.DECIMAL, 0);
        }

        public TerminalNode FLOAT() {
            return getToken(DetectRulesParser.FLOAT, 0);
        }

        public TerminalNode INTEGER() {
            return getToken(DetectRulesParser.INTEGER, 0);
        }

        public TerminalNode STRING() {
            return getToken(DetectRulesParser.STRING, 0);
        }

        public TerminalNode TIME() {
            return getToken(DetectRulesParser.TIME, 0);
        }

        public TerminalNode INSTANCEOF() {
            return getToken(DetectRulesParser.INSTANCEOF, 0);
        }

        public TerminalNode TYPEOF() {
            return getToken(DetectRulesParser.TYPEOF, 0);
        }

        public TerminalNode LABEL() {
            return getToken(DetectRulesParser.LABEL, 0);
        }

        public TerminalNode GOTO() {
            return getToken(DetectRulesParser.GOTO, 0);
        }

        public TerminalNode SWITCH() {
            return getToken(DetectRulesParser.SWITCH, 0);
        }

        public TerminalNode CASE() {
            return getToken(DetectRulesParser.CASE, 0);
        }

        public TerminalNode FOR() {
            return getToken(DetectRulesParser.FOR, 0);
        }

        public TerminalNode CONTINUE() {
            return getToken(DetectRulesParser.CONTINUE, 0);
        }

        public TerminalNode BREAK() {
            return getToken(DetectRulesParser.BREAK, 0);
        }

        public TerminalNode VAR() {
            return getToken(DetectRulesParser.VAR, 0);
        }

        public TerminalNode DEF() {
            return getToken(DetectRulesParser.DEF, 0);
        }

        public TerminalNode FUNC() {
            return getToken(DetectRulesParser.FUNC, 0);
        }

        @Override
        public int getRuleIndex() { return RULE_allIdentifierKeywords; }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).enterAllIdentifierKeywords(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DetectRulesParserListener)
                ((DetectRulesParserListener) listener).exitAllIdentifierKeywords(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DetectRulesParserVisitor)
                return ((DetectRulesParserVisitor<? extends T>) visitor).visitAllIdentifierKeywords(this);
            else
                return visitor.visitChildren(this);
        }
    }
}
