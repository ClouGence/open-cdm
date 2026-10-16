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
lexer grammar EsLexer;

METHOD : ('GET' | 'POST' | 'PUT' | 'DELETE' | 'HEAD' | 'OPTIONS') -> pushMode(HEADER);
LBRACE : '{' -> pushMode(JSON);
EOL : '\r'? '\n';
WS : [ \t]+ -> channel(HIDDEN);
COMMENT : ('#' ~[\r\n]* | '//' ~[\r\n]* | '/*' .*? '*/') -> channel(HIDDEN);
INVALID : .;

mode HEADER;
HWS : [ \t]+;
SLASH : '/';
QUESTION : '?';
AMP : '&';
EQUALS : '=';
HCOMMENT : '#' ~[\r\n]*;
TEXT : ~[ /?&=#{}\u0000-\u001F]+;
HEADER_EOL : '\r'? '\n' -> type(EOL), popMode;
HEADER_INVALID : . -> type(INVALID);

mode JSON;
JSON_LBRACE : '{' -> type(LBRACE), pushMode(JSON);
RBRACE : '}' -> popMode;
LBRACKET : '[';
RBRACKET : ']';
COLON : ':';
COMMA : ',';
STRING : '"' (ESCAPE | ~["\\\u0000-\u001F])* '"';
NUMBER : '-'? ('0' | [1-9] [0-9]*) ('.' [0-9]+)? ([eE] [+-]? [0-9]+)?;
TRUE : 'true';
FALSE : 'false';
NULL : 'null';
JSON_WS : [ \t\r\n]+ -> channel(HIDDEN);
JSON_INVALID : . -> type(INVALID);
fragment ESCAPE : '\\' (["\\/bfnrt] | 'u' HEX HEX HEX HEX);
fragment HEX : [0-9a-fA-F];
