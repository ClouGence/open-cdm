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

lexer grammar DetectRulesLexer;

options {
    superClass = DetectRulesBasicLexer;
    //caseInsensitive=true;
    //exportMacro = PARSERS_PUBLIC_TYPE;
}

tokens {
    // define
    DEFINE,DEFAULT,ENUM,HINT,
    // type system.
    CAST,CEILING,DOWN,FLOOR,HALF_EVEN,HALF_DOWN,HALF_UP,UP,UNNECESSARY,
    BOOL,DATETIME,DATE,DECIMAL,FLOAT,INTEGER,STRING,TIME,
    INSTANCEOF,TYPEOF,
    // programming
    LABEL,GOTO,SWITCH,CASE,FOR,CONTINUE,BREAK,
    VAR,DEF,FUNC
}

@lexer::header {
import java.util.List;
import com.clougence.detectrule.parser.DetectRulesFeature;
import static com.clougence.detectrule.parser.DetectRulesFeature.*;
}

@lexer::members {
    public DetectRulesLexer(CharStream input, List<DetectRulesFeature> features) {
        this(input);
        super.initFeatures(features);
    }
}

channels {
    COMMENT
}

/* skip spaces */
WS      : [ \f\n\r\t]+          -> skip ; // skip spaces
COMMENT1: '//' (~[\n\r])* EOL?  -> channel(COMMENT);
//COMMENT2: '#' (~[\n\r])* EOL? -> channel(COMMENT);
COMMENT3: '/*' .*? '*/'         -> channel(COMMENT);
EOL     : [\n\r\f];

/* key words */
IF          : [iI][fF];
THEN        : [tT][hH][eE][nN];
ELSEIF      : [eE][lL][sS][eE][iI][fF];
ELSE        : [eE][lL][sS][eE];
END         : [eE][nN][dD];
FALSE       : [fF][aA][lL][sS][eE];
TRUE        : [tT][rR][uU][eE];
NULL        : [nN][uU][lL][lL];
ASSERT      : [aA][sS][sS][eE][rR][tT];
RETURN      : [rR][eE][tT][uU][rR][nN];
THROW       : [tT][hH][rR][oO][wW];
IN          : [iI][nN];
MATCHES     : [mM][aA][tT][cC][hH][eE][sS];
NOT         : [nN][oO][tT];
AS          : [aA][sS];

/* operators symbol */
BO_RSHIFT2 : '>>>';
BO_RSHIFT  : '>>';
CO_GE      : '>=';
CO_GT      : '>';
BO_LSHIFT  : '<<';
CO_LE      : '<=';
CO_LT      : '<';
CO_IEQ     : '===';
CO_EQ      : '==';
CO_NE      : '!=';
LO_NOT     : '!';
LO_AND     : '&&';
BO_AND     : '&';
LO_OR      : '||';
BO_OR      : '|';
BO_NOT     : '~';
AO_INCR    : '++';
AO_PLUS    : '+';
AO_DECR    : '--';
AO_MINUS   : '-';
AO_MUL     : '*';
AO_DIV     : '/';
AO_MOD     : '%';
BO_XOR     : '^';

/* separators operators */
LBT     : '(';
RBT     : ')';
LSBT    : '[';
RSBT    : ']';
OCBR    : '{';
CCBR    : '}';
OREP    : '#{';
WELL    : '#';
DOT     : '.';
COLON   : ':';
COMMA   : ',';
REF     : '@';
QUE     : '?';

/* other operators */
ASSIGN  : '=';

/* primitive types */
// number
PRI_DECIMAL             : DIGIT '.' EXPONENT_DIGIT
                        | DIGIT? '.' (DIGIT EXPONENT_DIGIT)
                        | DIGIT EXPONENT_DIGIT;
PRI_FLOAT               : (DIGIT)? '.' DIGIT;
PRI_INTEGER             : DIGIT;
PRI_XOB_NUM             : HEX_NOTATION | OCT_NOTATION | BIT_NOTATION;
fragment DIGIT          : [0-9]+;
fragment EXPONENT_DIGIT : [eE] [-+]? DIGIT;
fragment HEX_NOTATION   : '0' [xX] [0-9a-fA-F]+;
fragment OCT_NOTATION   : '0' [oO] [0-7]+;
fragment BIT_NOTATION   : '0' [bB] [01]+;

// string
PRI_STR             : '"' (~["\n\r] | TRANS)* '"'
                    | '\'' (~['\n\r] | TRANS)* '\''
                    ;
fragment TRANS      : '\\' (['"\\/bfnrtv] | UNICODE);
fragment UNICODE    : 'u' HEX HEX HEX HEX;
fragment HEX        : [0-9a-fA-F];

// date or time or datetime
PRI_DATETIME        : 'T' PRI_STR;
PRI_DATETIME_FMT    : 'T(' PRI_STR ')';

// identifier
IDENTIFIER      : [_a-zA-Z][_a-zA-Z0-9]*
                    {
                        String upStr = getText().toUpperCase();
                        if (keyWords.containsKey(upStr)) {
                            setType(keyWords.get(upStr));
                        }
                    }
                ;
PRI_IDENTIFIER  : '`' (~[`\r\n] | '\\`' | TRANS)* '`';
