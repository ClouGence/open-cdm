/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
lexer grammar HanaLexer;

SPACE
    : [ \t\r\n\u000B\u000C]+ -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '--' ~[\r\n]* -> channel(HIDDEN)
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

STRING_LITERAL
    : '\'' ('\'\'' | ~'\'')*? '\''
    ;

UNCLOSED_STRING_LITERAL
    : '\'' ('\'\'' | ~'\'')* EOF
    ;

QUOTED_IDENTIFIER
    : '"' ('""' | ~'"')*? '"'
    ;

UNCLOSED_QUOTED_IDENTIFIER
    : '"' ('""' | ~'"')* EOF
    ;

SEMICOLON : ';';
LEFT_PAREN : '(';
RIGHT_PAREN : ')';

WORD
    : [a-zA-Z_\u0080-\uFFFF] [a-zA-Z_0-9$#\u0080-\uFFFF]*
    ;

NUMBER
    : [0-9]+
    ;

SYMBOL
    : .
    ;
