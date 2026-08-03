/*
 * Copyright 2026 Hangzhou Kaiyun Jizhi Technology Co., Ltd.
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

grammar SrCreateTable;

sqlStatements
    : SEMICOLON* createTableStatement SEMICOLON* EOF
    ;

createTableStatement
    : CREATE (TEMPORARY | EXTERNAL)? TABLE (IF NOT EXISTS)? qualifiedName
      LEFT_PAREN tableElement (COMMA tableElement)* RIGHT_PAREN tableProperty*
    ;

tableElement
    : columnDesc
    | indexDesc
    ;

tableProperty
    : engineDesc
    | keyDesc
    | comment
    | distributionDesc
    | anyToken
    ;

engineDesc
    : ENGINE EQ? identifier
    ;

keyDesc
    : keyType=(AGGREGATE | UNIQUE | PRIMARY | DUPLICATE) KEY identifierList
    ;

distributionDesc
    : DISTRIBUTED BY HASH identifierList (BUCKETS bucketNumber=INTEGER_VALUE)?
    | DISTRIBUTED BY RANDOM (BUCKETS bucketNumber=INTEGER_VALUE)?
    ;

columnDesc
    : identifier type? columnOption*
    ;

columnOption
    : KEY
    | aggDesc
    | columnNullable
    | defaultDesc
    | AUTO_INCREMENT
    | generatedColumnDesc
    | comment
    ;

columnNullable
    : NULL
    | NOT NULL
    ;

defaultDesc
    : DEFAULT (string | NULL | CURRENT_TIMESTAMP (LEFT_PAREN INTEGER_VALUE? RIGHT_PAREN)? | LEFT_PAREN qualifiedName LEFT_PAREN RIGHT_PAREN RIGHT_PAREN)
    ;

generatedColumnDesc
    : AS anyToken+
    ;

aggDesc
    : identifier EQ defaultValue
    ;

defaultValue
    : string
    | NULL
    | CURRENT_TIMESTAMP
    | SUBTRACT? (INTEGER_VALUE | DECIMAL_VALUE)
    | identifier
    ;

indexDesc
    : INDEX indexName=identifier identifierList indexType? comment?
    ;

indexType
    : USING? identifier propertyList?
    ;

comment
    : COMMENT string
    ;

type
    : typeName typeParameter? complexTypeTail?
    ;

complexTypeTail
    : LT type (COMMA type)* GT
    ;

typeParameter
    : LEFT_PAREN precision=INTEGER_VALUE (COMMA scale=INTEGER_VALUE)? RIGHT_PAREN
    ;

typeName
    : BOOLEAN
    | TINYINT
    | SMALLINT
    | SIGNED
    | UNSIGNED
    | INT
    | INTEGER
    | BIGINT
    | LARGEINT
    | FLOAT
    | DOUBLE
    | DATE
    | DATETIME
    | TIME
    | CHAR
    | VARCHAR
    | STRING
    | TEXT
    | BITMAP
    | HLL
    | PERCENTILE
    | JSON
    | VARBINARY
    | BINARY
    | DECIMAL
    | DECIMALV2
    | DECIMAL32
    | DECIMAL64
    | DECIMAL128
    | NUMERIC
    | NUMBER
    | ARRAY
    | STRUCT
    | MAP
    | identifier
    ;

propertyList
    : LEFT_PAREN property (COMMA property)* RIGHT_PAREN
    ;

property
    : string EQ string
    ;

qualifiedName
    : identifier (DOT identifier)*
    ;

identifierList
    : LEFT_PAREN identifier (COMMA identifier)* RIGHT_PAREN
    ;

identifier
    : IDENTIFIER
    | DIGIT_IDENTIFIER
    | BACKQUOTED_IDENTIFIER
    | nonReserved
    ;

nonReserved
    : BITMAP
    | HLL
    | STRING
    | TEXT
    | JSON
    | BINARY
    | ARRAY
    | STRUCT
    | MAP
    ;

string
    : SINGLE_QUOTED_TEXT
    | DOUBLE_QUOTED_TEXT
    ;

anyToken
    : .
    ;

SEMICOLON: ';';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
COMMA: ',';
DOT: '.';
EQ: '=';
LT: '<';
GT: '>';
SUBTRACT: '-';

AGGREGATE: 'AGGREGATE';
ARRAY: 'ARRAY';
AS: 'AS';
AUTO_INCREMENT: 'AUTO_INCREMENT';
BIGINT: 'BIGINT';
BINARY: 'BINARY';
BITMAP: 'BITMAP';
BOOLEAN: 'BOOLEAN';
BUCKETS: 'BUCKETS';
BY: 'BY';
CHAR: 'CHAR' | 'CHARACTER';
COMMENT: 'COMMENT';
CREATE: 'CREATE';
CURRENT_TIMESTAMP: 'CURRENT_TIMESTAMP';
DATE: 'DATE';
DATETIME: 'DATETIME';
DECIMAL: 'DECIMAL';
DECIMAL128: 'DECIMAL128';
DECIMAL32: 'DECIMAL32';
DECIMAL64: 'DECIMAL64';
DECIMALV2: 'DECIMALV2';
DEFAULT: 'DEFAULT';
DISTRIBUTED: 'DISTRIBUTED';
DOUBLE: 'DOUBLE';
DUPLICATE: 'DUPLICATE';
ENGINE: 'ENGINE';
EXISTS: 'EXISTS';
EXTERNAL: 'EXTERNAL';
FLOAT: 'FLOAT';
HASH: 'HASH';
HLL: 'HLL';
IF: 'IF';
INDEX: 'INDEX';
INT: 'INT';
INTEGER: 'INTEGER';
JSON: 'JSON';
KEY: 'KEY';
LARGEINT: 'LARGEINT';
MAP: 'MAP';
NOT: 'NOT';
NULL: 'NULL';
NUMBER: 'NUMBER';
NUMERIC: 'NUMERIC';
PERCENTILE: 'PERCENTILE';
PRIMARY: 'PRIMARY';
PROPERTIES: 'PROPERTIES';
RANDOM: 'RANDOM';
SIGNED: 'SIGNED';
SMALLINT: 'SMALLINT';
STRING: 'STRING';
STRUCT: 'STRUCT';
TABLE: 'TABLE';
TEMPORARY: 'TEMPORARY';
TEXT: 'TEXT';
TIME: 'TIME';
TINYINT: 'TINYINT';
UNIQUE: 'UNIQUE';
UNSIGNED: 'UNSIGNED';
USING: 'USING';
VARBINARY: 'VARBINARY';
VARCHAR: 'VARCHAR';

DECIMAL_VALUE
    : DIGIT+ DOT DIGIT*
    | DOT DIGIT+
    ;

INTEGER_VALUE
    : DIGIT+
    ;

SINGLE_QUOTED_TEXT
    : '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\''
    ;

DOUBLE_QUOTED_TEXT
    : '"' ('\\'. | '""' | ~('"' | '\\'))* '"'
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

DIGIT_IDENTIFIER
    : DIGIT+ [A-Z_] [A-Z_0-9]*
    ;

IDENTIFIER
    : [A-Z_] [A-Z_0-9]*
    ;

LINE_COMMENT
    : '--' ~[\r\n]* -> channel(HIDDEN)
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

WS
    : [ \t\r\n]+ -> channel(HIDDEN)
    ;

UNRECOGNIZED
    : .
    ;

fragment DIGIT: [0-9];
