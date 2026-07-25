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

grammar DrCreateTable;

singleStatement
    : SEMICOLON* createTable SEMICOLON* EOF
    ;

createTable
    : CREATE (TEMPORARY | EXTERNAL)? TABLE (IF NOT EXISTS)? name=qualifiedName
      LEFT_PAREN tableElement (COMMA tableElement)* RIGHT_PAREN tableProperty*
    ;

tableElement
    : columnDef
    | indexDef
    ;

tableProperty
    : engineDesc
    | keyDesc
    | commentClause
    | distributionDesc
    | unknownTableToken
    ;

engineDesc
    : ENGINE EQ? engine=identifier
    ;

keyDesc
    : keyType=(AGGREGATE | UNIQUE | DUPLICATE | PRIMARY)? KEY keys=identifierList
    ;

distributionDesc
    : DISTRIBUTED BY (HASH hashKeys=identifierList | RANDOM) (BUCKETS (bucketNumber=INTEGER_VALUE | AUTO))?
    ;

columnDef
    : name=identifier type? columnOption*
    ;

columnOption
    : KEY
    | aggDesc
    | (NOT? NULL)
    | AUTO_INCREMENT (LEFT_PAREN INTEGER_VALUE RIGHT_PAREN)?
    | DEFAULT defaultValue
    | ON UPDATE CURRENT_TIMESTAMP (LEFT_PAREN INTEGER_VALUE? RIGHT_PAREN)?
    | commentClause
    | GENERATED ALWAYS? AS LEFT_PAREN defaultValue* RIGHT_PAREN
    ;

aggDesc
    : identifier
    ;

defaultValue
    : stringLiteral
    | NULL
    | CURRENT_TIMESTAMP (LEFT_PAREN INTEGER_VALUE? RIGHT_PAREN)?
    | CURRENT_DATE
    | SUBTRACT? (INTEGER_VALUE | DECIMAL_VALUE)
    | identifier (LEFT_PAREN RIGHT_PAREN)?
    ;

indexDef
    : INDEX (IF NOT EXISTS)? indexName=identifier identifierList (USING indexType=identifier)? indexOption*
    ;

indexOption
    : PROPERTIES propertyList
    | commentClause
    ;

commentClause
    : COMMENT comment=stringLiteral
    ;

type
    : typeName typeParameter? complexTypeTail?
    ;

complexTypeTail
    : LT type (COMMA type)* GT
    ;

typeParameter
    : LEFT_PAREN precision=INTEGER_VALUE (COMMA scale=INTEGER_VALUE)* RIGHT_PAREN
    ;

typeName
    : ARRAY
    | MAP
    | STRUCT
    | TINYINT
    | SMALLINT
    | INT
    | INTEGER
    | BIGINT
    | LARGEINT
    | BOOLEAN
    | FLOAT
    | DOUBLE
    | DATE
    | DATETIME
    | TIME
    | DATEV2
    | DATETIMEV2
    | DATEV1
    | DATETIMEV1
    | BITMAP
    | QUANTILE_STATE
    | HLL
    | AGG_STATE
    | STRING
    | JSON
    | JSONB
    | TEXT
    | VARCHAR
    | CHAR
    | DECIMAL
    | DECIMALV2
    | DECIMALV3
    | IPV4
    | IPV6
    | VARIANT
    | identifier
    ;

propertyList
    : LEFT_PAREN property (COMMA property)* RIGHT_PAREN
    ;

property
    : stringLiteral EQ stringLiteral
    ;

identifierList
    : LEFT_PAREN identifierSeq RIGHT_PAREN
    ;

identifierSeq
    : ident+=identifier (COMMA ident+=identifier)*
    ;

qualifiedName
    : identifier (DOT identifier)*
    ;

identifier
    : IDENTIFIER
    | BACKQUOTED_IDENTIFIER
    | nonReserved
    ;

nonReserved
    : AGG_STATE
    | BITMAP
    | HLL
    | STRING
    | JSON
    | JSONB
    | TEXT
    | VARIANT
    | AUTO
    ;

stringLiteral
    : STRING_LITERAL
    ;

unknownTableToken
    : ~SEMICOLON
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

AGG_STATE: 'AGG_STATE';
AGGREGATE: 'AGGREGATE';
ALWAYS: 'ALWAYS';
ARRAY: 'ARRAY';
AS: 'AS';
AUTO: 'AUTO';
AUTO_INCREMENT: 'AUTO_INCREMENT';
BIGINT: 'BIGINT';
BITMAP: 'BITMAP';
BOOLEAN: 'BOOLEAN';
BUCKETS: 'BUCKETS';
BY: 'BY';
CHAR: 'CHAR' | 'CHARACTER';
COMMENT: 'COMMENT';
CREATE: 'CREATE';
CURRENT_DATE: 'CURRENT_DATE';
CURRENT_TIMESTAMP: 'CURRENT_TIMESTAMP';
DATE: 'DATE';
DATETIME: 'DATETIME';
DATETIMEV1: 'DATETIMEV1';
DATETIMEV2: 'DATETIMEV2';
DATEV1: 'DATEV1';
DATEV2: 'DATEV2';
DECIMAL: 'DECIMAL';
DECIMALV2: 'DECIMALV2';
DECIMALV3: 'DECIMALV3';
DEFAULT: 'DEFAULT';
DISTRIBUTED: 'DISTRIBUTED';
DOUBLE: 'DOUBLE';
DUPLICATE: 'DUPLICATE';
ENGINE: 'ENGINE';
EXISTS: 'EXISTS';
EXTERNAL: 'EXTERNAL';
FLOAT: 'FLOAT';
GENERATED: 'GENERATED';
HASH: 'HASH';
HLL: 'HLL';
IF: 'IF';
INDEX: 'INDEX';
INT: 'INT';
INTEGER: 'INTEGER';
IPV4: 'IPV4';
IPV6: 'IPV6';
JSON: 'JSON';
JSONB: 'JSONB';
KEY: 'KEY';
LARGEINT: 'LARGEINT';
MAP: 'MAP';
NOT: 'NOT';
NULL: 'NULL';
ON: 'ON';
PRIMARY: 'PRIMARY';
PROPERTIES: 'PROPERTIES';
QUANTILE_STATE: 'QUANTILE_STATE';
RANDOM: 'RANDOM';
SMALLINT: 'SMALLINT';
STRING: 'STRING';
STRUCT: 'STRUCT';
TABLE: 'TABLE';
TEMPORARY: 'TEMPORARY';
TEXT: 'TEXT';
TIME: 'TIME';
TINYINT: 'TINYINT';
UNIQUE: 'UNIQUE';
UPDATE: 'UPDATE';
USING: 'USING';
VARIANT: 'VARIANT';
VARCHAR: 'VARCHAR';

DECIMAL_VALUE
    : DIGIT+ DOT DIGIT*
    | DOT DIGIT+
    ;

INTEGER_VALUE
    : DIGIT+
    ;

STRING_LITERAL
    : '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\''
    | '"' ('\\'. | '""' | ~('"' | '\\'))* '"'
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
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
