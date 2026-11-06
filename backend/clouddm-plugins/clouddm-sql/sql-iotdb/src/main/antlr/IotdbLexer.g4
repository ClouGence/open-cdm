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
lexer grammar IotdbLexer;

tokens { UNTERMINATED_BLOCK_COMMENT }

@members {
    private boolean treeModel;
    public void setTreeModel(boolean treeModel) {
        this.treeModel = treeModel;
    }
}

SELECT: 'SELECT';
LAST: 'LAST';
DISTINCT: 'DISTINCT';
ALL: 'ALL';
FROM: 'FROM';
WHERE: 'WHERE';
AS: 'AS';
BY: 'BY';
ALIGN: 'ALIGN';
DEVICE: 'DEVICE';
DEVICES: 'DEVICES';
TIMESERIES: 'TIMESERIES';
ALIGNED: 'ALIGNED';
DATABASE: 'DATABASE';
DATABASES: 'DATABASES';
TABLE: 'TABLE';
TABLES: 'TABLES';
VIEW: 'VIEW';
ROOT: 'ROOT';
CREATE: 'CREATE';
ALTER: 'ALTER';
ADD: 'ADD';
COLUMN: 'COLUMN';
COMMENT: 'COMMENT';
TAGS: 'TAGS';
ATTRIBUTES: 'ATTRIBUTES';
DROP: 'DROP';
INSERT: 'INSERT';
INTO: 'INTO';
VALUES: 'VALUES';
DELETE: 'DELETE';
UPDATE: 'UPDATE';
SET: 'SET';
SHOW: 'SHOW';
DESCRIBE: 'DESCRIBE';
VERSION: 'VERSION';
CURRENT_SQL_DIALECT: 'CURRENT_SQL_DIALECT';
CURRENT_DATABASE: 'CURRENT_DATABASE';
DETAILS: 'DETAILS';
WITH: 'WITH';
IF: 'IF';
EXISTS: 'EXISTS';
NOT: 'NOT';
AND: 'AND';
OR: 'OR';
IS: 'IS';
NULL: 'NULL';
TRUE: 'TRUE';
FALSE: 'FALSE';
IN: 'IN';
BETWEEN: 'BETWEEN';
LIKE: 'LIKE';
REGEXP: 'REGEXP';
TIME: 'TIME';
TIMESTAMP: 'TIMESTAMP';
DATE: 'DATE';
NOW: 'NOW';
ORDER: 'ORDER';
GROUP: 'GROUP';
HAVING: 'HAVING';
ASC: 'ASC';
DESC: 'DESC';
NULLS: 'NULLS';
FIRST: 'FIRST';
LIMIT: 'LIMIT';
OFFSET: 'OFFSET';
SLIMIT: 'SLIMIT';
SOFFSET: 'SOFFSET';
JOIN: 'JOIN';
ON: 'ON';
INNER: 'INNER';
LEFT: 'LEFT';
RIGHT: 'RIGHT';
FULL: 'FULL';
OUTER: 'OUTER';
CROSS: 'CROSS';
UNION: 'UNION';
TAG: 'TAG';
ATTRIBUTE: 'ATTRIBUTE';
FIELD: 'FIELD';

SEMI: ';';
COMMA: ',';
DOT: '.';
LPAREN: '(';
RPAREN: ')';
DOUBLE_STAR: '**';
STAR: '*';
PLUS: '+';
MINUS: '-';
SLASH: '/';
PERCENT: '%';
EQ: '=' | '==';
NE: '!=' | '<>';
LE: '<=';
GE: '>=';
LT: '<';
GT: '>';

TREE_STRING: {treeModel}? '\'' ('\'\'' | ~['])* '\'';
TREE_DOUBLE_STRING: {treeModel}? '"' ('""' | ~["])* '"';
TABLE_STRING: {!treeModel}? '\'' ('\'\'' | ~['])* '\'';
QUOTED_IDENTIFIER: {!treeModel}? '"' ('""' | ~["])* '"';
BACKTICK_IDENTIFIER: {treeModel}? '\u0060' ('\u0060\u0060' | ~[\u0060])* '\u0060';
DECIMAL: ([0-9]+ '.' [0-9]+ | '.' [0-9]+) ('E' [+-]? [0-9]+)? | [0-9]+ 'E' [+-]? [0-9]+;
INTEGER: [0-9]+;
IDENTIFIER: [A-Z_\p{L}] [A-Z0-9_\p{L}\p{N}]*;
TREE_IDENTIFIER: {treeModel}? [A-Z0-9_@#$:\p{L}\p{N}]+;

BLOCK_COMMENT: '/*' .*? ('*/' {setChannel(HIDDEN);} | EOF {setType(UNTERMINATED_BLOCK_COMMENT);});
LINE_COMMENT: '--' ~[\r\n]* -> channel(HIDDEN);
WS: [ \t\r\n\f]+ -> channel(HIDDEN);
UNRECOGNIZED: .;
