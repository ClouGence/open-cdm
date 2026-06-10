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

lexer grammar TypeProcessorDSLLexer;

/* skip spaces */
WS      : [ \t\n\r\f]+          -> skip ; // Skip spaces: space, horizontal tab, line break, carriage return, and form feed.
COMMENT1: '//' (~[\n\r])* EOL?  -> skip ;
COMMENT2: '#' (~[\n\r])* EOL?  -> skip ;
COMMENT3: '/*' .*? '*/';
EOL     : [\n\r\f];

/* key words */
DEFINE  : 'define';
ALIAS   : 'alias';
FOLLOW  : 'follow';
THROW   : 'throw';
TRUE    : 'true';
FALSE   : 'false';
NULL    : 'null';

/* Asist Words Connector is used in certain settings */
SET     : '=';      // Settings
APPEND  : '+=';     // Additional Settings
SEM     : ';';      // ;

ALL     : '*';      // ALL
COLON   : ':';      // Use for object type
COMMA   : ',';      // Parameter \ Partition
LBT     : '(';      // Tool Functions
RBT     : ')';      // Tool Functions
LSBT    : '[';      // Collapse or Type Definition
RSBT    : ']';      // Collapse or Type Definition
ENV     : '${';     // ENV
OCBR    : '{';      // Map
CCBR    : '}';      // Map

/* String */
STRING          : '"' (~["\r\n] | '""' | TRANS)* '"'
                | '\'' (~['\r\n] | '\'\'' | TRANS)* '\''
                | '`' (~['\r\n] | '``' | TRANS)* '`'
                ;
fragment TRANS  : '\\' (['"\\/bfnrt] | UNICODE);
fragment UNICODE: 'u' HEX HEX HEX HEX;
fragment HEX    : [0-9a-fA-F];

/* Numbers */
HEX_NUM         : '0' [xX] [0-9a-fA-F]+;                // Hexadecimal: 0x12345
OCT_NUM         : '0' [oO] [0-7]+;                      // Octal: 0o1234567
BIT_NUM         : '0' [bB] [01]+;                       // Binary: 0b010101100
SIZE            : [1-9][0-9]* (B | KB | MB) ;           // Size
INTEGER_NUM     : '-'? [0-9]+;                          // Decimals: -000234 or 123
DECIMAL_NUM     : '-'? (([0-9]* '.' [0-9]+) | [1-9]+)   // Float
                  ([eE] [+-]? [1-9][0-9]*)?;            // Scientific mode
fragment B      : 'b';
fragment KB     : 'k';
fragment MB     : 'm';

/* Identifier */
IDENTIFIER      : ([_a-zA-Z] [_0-9a-zA-Z]*);

/* TYPE */
TYPE            : ((IDENTIFIER '.')+)? IDENTIFIER;
