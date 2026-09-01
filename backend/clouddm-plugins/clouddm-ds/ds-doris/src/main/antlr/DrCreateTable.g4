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
    : CREATE tableModifier? TABLE (IF NOT EXISTS)? name=qualifiedName
      (
          LIKE likeTable=qualifiedName (WITH ROLLUP identifierList?)?
        | tableDefinition tableProperty* queryClause?
        | ctasColumns? tableProperty* queryClause
        | tableProperty+
      )
    ;

tableModifier
    : TEMPORARY
    | EXTERNAL
    ;

tableDefinition
    : LEFT_PAREN tableElement (COMMA tableElement)* COMMA? RIGHT_PAREN
    ;

ctasColumns
    : identifierList
    ;

queryClause
    : AS queryToken+
    | (SELECT | WITH) queryToken*
    ;

queryToken
    : ~SEMICOLON
    ;

tableElement
    : columnDef
    | indexDef
    ;

tableProperty
    : engineDesc
    | keyDesc
    | orderByDesc
    | commentClause
    | partitionDesc
    | distributionDesc
    | rollupDesc
    | propertiesDesc
    | brokerPropertiesDesc
    ;

engineDesc
    : ENGINE EQ? engine=identifier
    ;

keyDesc
    : keyType=(AGGREGATE | UNIQUE | DUPLICATE | PRIMARY)? KEY keys=identifierList
    ;

orderByDesc
    : ORDER BY LEFT_PAREN balancedToken* RIGHT_PAREN
    ;

partitionDesc
    : AUTO? PARTITION BY partitionBody
    ;

partitionBody
    : balancedClauseToken+
    ;

distributionDesc
    : DISTRIBUTED BY (HASH hashKeys=identifierList | RANDOM) (BUCKETS (bucketNumber=INTEGER_VALUE | autoBucket=AUTO))?
    ;

rollupDesc
    : ROLLUP LEFT_PAREN balancedToken* RIGHT_PAREN
    ;

propertiesDesc
    : PROPERTIES properties=propertyList
    ;

brokerPropertiesDesc
    : BROKER PROPERTIES properties=propertyList
    ;

columnDef
    : name=qualifiedName type columnOption*
    ;

columnOption
    : KEY
    | aggDesc
    | NOT? NULL
    | AUTO_INCREMENT (LEFT_PAREN INTEGER_VALUE RIGHT_PAREN)?
    | DEFAULT defaultValue
    | ON UPDATE CURRENT_TIMESTAMP (LEFT_PAREN INTEGER_VALUE? RIGHT_PAREN)?
    | commentClause
    | GENERATED ALWAYS? AS LEFT_PAREN generatedExpression* RIGHT_PAREN
    | AS LEFT_PAREN generatedExpression* RIGHT_PAREN
    ;

aggDesc
    : aggType=(MAX | MIN | SUM | REPLACE | REPLACE_IF_NOT_NULL | HLL_UNION
      | BITMAP_UNION | QUANTILE_UNION | GENERIC)
    ;

defaultValue
    : stringLiteral
    | NULL
    | CURRENT_TIMESTAMP (LEFT_PAREN INTEGER_VALUE? RIGHT_PAREN)?
    | CURRENT_DATE
    | SUBTRACT? (INTEGER_VALUE | DECIMAL_VALUE)
    | identifier (LEFT_PAREN balancedToken* RIGHT_PAREN)?
    ;

generatedExpression
    : LEFT_PAREN generatedExpression* RIGHT_PAREN
    | ~RIGHT_PAREN
    ;

indexDef
    : INDEX (IF NOT EXISTS)? indexName=identifier identifierList
      (USING (indexType=(BITMAP | INVERTED | NGRAM_BF | ANN) | indexTypeIdentifier=identifier))?
      indexOption*
    ;

indexOption
    : propertiesDesc
    | commentClause
    ;

commentClause
    : COMMENT comment=stringLiteral
    ;

type
    : ARRAY LT elementType=type GT
    | MAP LT keyType=type COMMA valueType=type GT
    | STRUCT LT structField (COMMA structField)* GT
    | AGG_STATE LT balancedToken+ GT
    | VARIANT (LT balancedToken+ GT)?
    | typeName typeParameter?
    ;

structField
    : identifier COLON type commentClause?
    ;

typeParameter
    : LEFT_PAREN params+=typeParameterValue (COMMA params+=typeParameterValue)* RIGHT_PAREN
    ;

typeParameterValue
    : INTEGER_VALUE
    | ASTERISK
    ;

typeName
    : TINYINT
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
    | TIMESTAMPTZ
    | BITMAP
    | QUANTILE_STATE
    | HLL
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
    | VARBINARY
    | identifier
    ;

propertyList
    : LEFT_PAREN property (COMMA property)* COMMA? RIGHT_PAREN
    ;

property
    : propertyAtom EQ propertyAtom
    ;

propertyAtom
    : stringLiteral
    | identifier
    | INTEGER_VALUE
    | DECIMAL_VALUE
    ;

identifierList
    : LEFT_PAREN identifierSeq RIGHT_PAREN
    ;

identifierSeq
    : ident+=identifier (COMMA ident+=identifier)*
    ;

qualifiedName
    : parts+=identifier (DOT parts+=identifier)*
    ;

identifier
    : IDENTIFIER
    | BACKQUOTED_IDENTIFIER
    | nonReserved
    ;

nonReserved
    : AGG_STATE
    | ARRAY
    | BIGINT
    | BITMAP
    | BOOLEAN
    | BROKER
    | CHAR
    | COMMENT
    | DATE
    | DATETIME
    | DATETIMEV1
    | DATETIMEV2
    | DATEV1
    | DATEV2
    | DECIMAL
    | DECIMALV2
    | DECIMALV3
    | DOUBLE
    | FLOAT
    | HLL
    | INT
    | INTEGER
    | IPV4
    | IPV6
    | LARGEINT
    | MAP
    | PROPERTIES
    | QUANTILE_STATE
    | SMALLINT
    | STRING
    | STRUCT
    | TIMESTAMPTZ
    | TIME
    | TINYINT
    | JSON
    | JSONB
    | TEXT
    | VARBINARY
    | VARCHAR
    | VARIANT
    | AUTO
    | MAX
    | MIN
    | SUM
    | REPLACE
    | GENERIC
    ;

stringLiteral
    : STRING_LITERAL
    ;

balancedClauseToken
    : LEFT_PAREN balancedToken* RIGHT_PAREN
    | ~(
          SEMICOLON
        | DISTRIBUTED
        | ROLLUP
        | PROPERTIES
        | BROKER
        | AS
      )
    ;

balancedToken
    : LEFT_PAREN balancedToken* RIGHT_PAREN
    | LT balancedToken* GT
    | ~(
          LEFT_PAREN
        | RIGHT_PAREN
        | LT
        | GT
        | SEMICOLON
      )
    ;

SEMICOLON: ';';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
COMMA: ',';
DOT: '.';
EQ: '=';
LT: '<';
GT: '>';
COLON: ':';
ASTERISK: '*';
SUBTRACT: '-';

AGG_STATE: 'AGG_STATE';
AGGREGATE: 'AGGREGATE';
ALWAYS: 'ALWAYS';
ANN: 'ANN';
ARRAY: 'ARRAY';
AS: 'AS';
AUTO: 'AUTO';
AUTO_INCREMENT: 'AUTO_INCREMENT';
BIGINT: 'BIGINT';
BITMAP: 'BITMAP';
BITMAP_UNION: 'BITMAP_UNION';
BOOLEAN: 'BOOLEAN';
BROKER: 'BROKER';
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
GENERIC: 'GENERIC';
HASH: 'HASH';
HLL: 'HLL';
HLL_UNION: 'HLL_UNION';
IF: 'IF';
INDEX: 'INDEX';
INT: 'INT';
INTEGER: 'INTEGER';
INVERTED: 'INVERTED';
IPV4: 'IPV4';
IPV6: 'IPV6';
JSON: 'JSON';
JSONB: 'JSONB';
KEY: 'KEY';
LARGEINT: 'LARGEINT';
LIKE: 'LIKE';
MAP: 'MAP';
MAX: 'MAX';
MIN: 'MIN';
NGRAM_BF: 'NGRAM_BF';
NOT: 'NOT';
NULL: 'NULL';
ON: 'ON';
ORDER: 'ORDER';
PARTITION: 'PARTITION';
PRIMARY: 'PRIMARY';
PROPERTIES: 'PROPERTIES';
QUANTILE_STATE: 'QUANTILE_STATE';
QUANTILE_UNION: 'QUANTILE_UNION';
RANDOM: 'RANDOM';
REPLACE: 'REPLACE';
REPLACE_IF_NOT_NULL: 'REPLACE_IF_NOT_NULL';
ROLLUP: 'ROLLUP';
SELECT: 'SELECT';
SMALLINT: 'SMALLINT';
STRING: 'STRING';
STRUCT: 'STRUCT';
SUM: 'SUM';
TABLE: 'TABLE';
TEMPORARY: 'TEMPORARY';
TEXT: 'TEXT';
TIME: 'TIME';
TIMESTAMPTZ: 'TIMESTAMPTZ';
TINYINT: 'TINYINT';
UNIQUE: 'UNIQUE';
UPDATE: 'UPDATE';
USING: 'USING';
VARBINARY: 'VARBINARY';
VARIANT: 'VARIANT';
VARCHAR: 'VARCHAR';
WITH: 'WITH';

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
    : [A-Z_] [A-Z_0-9$]*
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
