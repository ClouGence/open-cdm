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

grammar GreptimeSql;

@lexer::members {
    private boolean executableComment;

    @Override
    public Token nextToken() {
        Token token = super.nextToken();
        if (token.getType() == Token.EOF && executableComment) {
            getErrorListenerDispatch().syntaxError(this, token, getLine(), getCharPositionInLine(),
                "Unterminated executable comment", null);
        }
        return token;
    }
}

script
    : SEMI* (statement (SEMI+ statement)* SEMI*)? EOF
    ;

statement
    : query
    | insertStatement
    | deleteStatement
    | createDatabase
    | alterDatabase
    | dropDatabase
    | createTable
    | alterTable
    | dropTable
    | truncateTable
    | createView
    | dropView
    | useDatabase
    | showStatement
    | describeStatement
    ;

query
    : withClause? queryTerm (setOperator (ALL | DISTINCT)? queryTerm)* orderBy? rowLimit?
    ;

withClause
    : WITH RECURSIVE? commonTable (',' commonTable)*
    ;

commonTable
    : identifier columnList? AS '(' query ')'
    ;

setOperator
    : UNION | INTERSECT | EXCEPT
    ;

queryTerm
    : SELECT (DISTINCT | ALL)? selectItem (',' selectItem)*
      (FROM joinedTable (',' joinedTable)*)? (WHERE expression)?
      (GROUP BY expressionList)? (HAVING expression)?
    | '(' query ')'
    ;

selectItem
    : (qualifiedIdentifier '.')? '*'
    | expression (AS? identifier)?
    ;

joinedTable
    : tableSource joinClause*
    ;

tableSource
    : (objectName | '(' query ')' | '(' joinedTable ')') (AS? identifier columnList?)?
    ;

joinClause
    : (INNER | (LEFT | RIGHT | FULL) OUTER?)? JOIN tableSource
      (ON expression | USING columnList)?
    | CROSS JOIN tableSource
    ;

orderBy
    : ORDER BY sortItem (',' sortItem)*
    ;

sortItem
    : expression (ASC | DESC)? (NULLS (FIRST | LAST))?
    ;

rowLimit
    : LIMIT (NUMBER | ALL) (OFFSET NUMBER)?
    | OFFSET NUMBER (LIMIT (NUMBER | ALL))?
    ;

insertStatement
    : INSERT INTO objectName columnList? (VALUES valuesRow (',' valuesRow)* | query)
    ;

valuesRow
    : '(' (valueExpression (',' valueExpression)*)? ')'
    ;

valueExpression
    : expression | DEFAULT
    ;

deleteStatement
    : DELETE FROM objectName (AS? identifier)? (WHERE expression)?
    ;

createDatabase
    : CREATE (DATABASE | SCHEMA) ifNotExists? identifier withOptions?
    ;

alterDatabase
    : ALTER (DATABASE | SCHEMA) identifier alterOptions
    ;

dropDatabase
    : DROP (DATABASE | SCHEMA) ifExists? identifier
    ;

createTable
    : CREATE TABLE ifNotExists? target=objectName
      (LIKE source=objectName
      | tableDefinition partitionClause? (ENGINE '=' wordIdentifier)? withOptions?)
    ;

tableDefinition
    : '(' tableElement (',' tableElement)* ','? ')'
    ;

tableElement
    : TIME INDEX '(' identifier ')'
    | PRIMARY KEY columnList
    | wordIdentifier columnDefinition
    ;

columnDefinition
    : dataType columnOption*
    ;

columnOption
    : NOT? NULL
    | DEFAULT expression
    | TIME INDEX
    | PRIMARY KEY
    | COMMENT STRING_LITERAL
    | secondaryIndex
    ;

secondaryIndex
    : (FULLTEXT | INVERTED | SKIPPING) INDEX withOptions?
    ;

partitionClause
    : PARTITION ON COLUMNS columnList '(' (expressionList ','?)? ')'
    ;

alterTable
    : ALTER TABLE objectName
      (RENAME TO? newName=identifier | alterOptions | alterColumn (',' alterColumn)*)
    ;

alterColumn
    : ADD COLUMN? ifNotExists? identifier columnDefinition (FIRST | AFTER identifier)?
    | DROP COLUMN? ifExists? identifier
    | MODIFY COLUMN? identifier
      (dataType | SET DEFAULT expression | DROP DEFAULT | SET secondaryIndex | UNSET (FULLTEXT | INVERTED | SKIPPING) INDEX)
    ;

dropTable
    : DROP TABLE ifExists? objectName (',' objectName)*
    ;

truncateTable
    : TRUNCATE TABLE objectName
    ;

createView
    : CREATE (OR REPLACE)? VIEW ifNotExists? objectName columnList? AS query
    ;

dropView
    : DROP VIEW ifExists? objectName (',' objectName)*
    ;

useDatabase
    : USE identifier
    ;

showStatement
    : SHOW FULL? (DATABASES | SCHEMAS) showFilter?                            # showDatabases
    | SHOW (FULL? TABLES | VIEWS) schemaClause? showFilter?                    # showRelations
    | SHOW CREATE (DATABASE | SCHEMA) identifier                              # showCreateDatabase
    | SHOW CREATE kind=(TABLE | VIEW) objectName                              # showCreateRelation
    | SHOW (FULL? (COLUMNS | FIELDS) showTable showFilter?
           | (INDEX | INDEXES | KEYS) showTable (WHERE expression)?)           # showRelationMetadata
    ;

describeStatement
    : (DESCRIBE | DESC) TABLE? objectName
    ;

schemaClause
    : (FROM | IN) identifier
    ;

showTable
    : (FROM | IN) identifier schemaClause?
    ;

showFilter
    : LIKE identifier | WHERE expression
    ;

ifNotExists : IF NOT EXISTS;
ifExists : IF EXISTS;
columnList : '(' identifier (',' identifier)* ')';

withOptions
    : WITH '(' (optionAssignment (',' optionAssignment)* ','?)? ')'
    ;

alterOptions
    : SET optionAssignment (',' optionAssignment)*
    | UNSET optionName (',' optionName)*
    ;

optionAssignment
    : optionName '=' (literal | wordIdentifier | '-' NUMBER)
    ;

optionName
    : identifier | COMMENT
    ;

dataType
    : timestampType
    | wordIdentifier PRECISION? ('(' NUMBER (',' NUMBER)? ')')? UNSIGNED?
    ;

timestampType
    : TIMESTAMP ('(' NUMBER ')')? ((WITH | WITHOUT) TIME ZONE)?
    | TIMESTAMPTZ ('(' NUMBER ')')?
    ;

objectName
    : identifier ('.' identifier ('.' identifier)?)?
    ;

qualifiedIdentifier
    : wordIdentifier ('.' wordIdentifier)*
    ;

identifier
    : wordIdentifier | stringLiteral
    ;

wordIdentifier
    : IDENTIFIER | QUOTED_IDENTIFIER
    | DATE | TIME | TIMESTAMP | TIMESTAMPTZ | WITHOUT | ZONE | INTERVAL | FIRST | LAST | ENGINE | INDEX | FULLTEXT | INVERTED | SKIPPING | IF
    | TABLES | VIEWS | DATABASES | SCHEMAS | FIELDS | COLUMNS | KEYS
    ;

expressionList
    : expression (',' expression)*
    ;

expression
    : andExpression (OR andExpression)*
    ;

andExpression
    : notExpression (AND notExpression)*
    ;

notExpression
    : NOT notExpression | predicate
    ;

predicate
    : arithmeticExpression
      (comparison arithmeticExpression
      | IS NOT? (NULL | TRUE | FALSE | UNKNOWN)
      | IS NOT? DISTINCT FROM arithmeticExpression
      | NOT? BETWEEN arithmeticExpression AND arithmeticExpression
      | NOT? IN '(' (query | expressionList) ')'
      | NOT? (LIKE | ILIKE) arithmeticExpression (ESCAPE arithmeticExpression)?)?
    ;

comparison
    : '=' | '<>' | '!=' | '<=' | '>=' | '<' | '>'
    ;

arithmeticExpression
    : product (('+' | '-' | '||') product)*
    ;

product
    : unaryExpression (('*' | '/' | '%') unaryExpression)*
    ;

unaryExpression
    : ('+' | '-') unaryExpression
    | atom ('::' dataType)*
    ;

atom
    : literal
    | qualifiedIdentifier '(' ('*' | DISTINCT? expressionList)? ')' filterClause? overClause?
    | (CAST | TRY_CAST) '(' expression AS dataType ')'
    | CASE expression? (WHEN expression THEN expression)+ (ELSE expression)? END
    | qualifiedIdentifier
    | EXISTS '(' query ')'
    | '(' (query | expressionList) ')'
    ;

filterClause
    : FILTER '(' WHERE expression ')'
    ;

overClause
    : OVER '(' (PARTITION BY expressionList)? orderBy? ')'
    ;

literal
    : (DATE | TIME | TIMESTAMP | INTERVAL)? stringLiteral
    | NUMBER | NULL | TRUE | FALSE
    ;

stringLiteral
    : STRING_LITERAL | DOUBLE_QUOTED_STRING
    ;

SELECT : 'SELECT';
WITH : 'WITH';
RECURSIVE : 'RECURSIVE';
AS : 'AS';
ALL : 'ALL';
DISTINCT : 'DISTINCT';
UNION : 'UNION';
INTERSECT : 'INTERSECT';
EXCEPT : 'EXCEPT';
FROM : 'FROM';
WHERE : 'WHERE';
GROUP : 'GROUP';
BY : 'BY';
HAVING : 'HAVING';
JOIN : 'JOIN';
INNER : 'INNER';
LEFT : 'LEFT';
RIGHT : 'RIGHT';
FULL : 'FULL';
OUTER : 'OUTER';
CROSS : 'CROSS';
ON : 'ON';
USING : 'USING';
ORDER : 'ORDER';
ASC : 'ASC';
DESC : 'DESC';
NULLS : 'NULLS';
FIRST : 'FIRST';
LAST : 'LAST';
LIMIT : 'LIMIT';
OFFSET : 'OFFSET';
INSERT : 'INSERT';
INTO : 'INTO';
VALUES : 'VALUES';
DELETE : 'DELETE';
CREATE : 'CREATE';
ALTER : 'ALTER';
DROP : 'DROP';
TRUNCATE : 'TRUNCATE';
RENAME : 'RENAME';
TO : 'TO';
REPLACE : 'REPLACE';
DATABASE : 'DATABASE';
DATABASES : 'DATABASES';
SCHEMA : 'SCHEMA';
SCHEMAS : 'SCHEMAS';
TABLE : 'TABLE';
TABLES : 'TABLES';
VIEW : 'VIEW';
VIEWS : 'VIEWS';
ENGINE : 'ENGINE';
COLUMN : 'COLUMN';
COLUMNS : 'COLUMNS';
FIELDS : 'FIELDS';
KEYS : 'KEYS';
INDEX : 'INDEX';
INDEXES : 'INDEXES';
FULLTEXT : 'FULLTEXT';
INVERTED : 'INVERTED';
SKIPPING : 'SKIPPING';
PRIMARY : 'PRIMARY';
KEY : 'KEY';
PARTITION : 'PARTITION';
ADD : 'ADD';
AFTER : 'AFTER';
MODIFY : 'MODIFY';
COMMENT : 'COMMENT';
DEFAULT : 'DEFAULT';
SET : 'SET';
UNSET : 'UNSET';
USE : 'USE';
SHOW : 'SHOW';
DESCRIBE : 'DESCRIBE';
IF : 'IF';
NOT : 'NOT';
EXISTS : 'EXISTS';
AND : 'AND';
OR : 'OR';
IS : 'IS';
IN : 'IN';
BETWEEN : 'BETWEEN';
LIKE : 'LIKE';
ILIKE : 'ILIKE';
ESCAPE : 'ESCAPE';
NULL : 'NULL';
TRUE : 'TRUE';
FALSE : 'FALSE';
UNKNOWN : 'UNKNOWN';
DATE : 'DATE';
TIME : 'TIME';
TIMESTAMP : 'TIMESTAMP';
TIMESTAMPTZ : 'TIMESTAMPTZ';
WITHOUT : 'WITHOUT';
ZONE : 'ZONE';
INTERVAL : 'INTERVAL';
PRECISION : 'PRECISION';
UNSIGNED : 'UNSIGNED';
CAST : 'CAST';
TRY_CAST : 'TRY_CAST';
CASE : 'CASE';
WHEN : 'WHEN';
THEN : 'THEN';
ELSE : 'ELSE';
END : 'END';
FILTER : 'FILTER';
OVER : 'OVER';

NUMBER : ([0-9]+ ('.' [0-9]*)? | '.' [0-9]+) ('E' [+-]? [0-9]+)?;
IDENTIFIER : [A-Z_$\p{L}] [A-Z_$0-9\p{L}\p{N}]*;
QUOTED_IDENTIFIER : '`' ('``' | ~'`')* '`';
DOUBLE_QUOTED_STRING : '"' ('\\' . | '""' | ~["\\])* '"';
STRING_LITERAL : '\'' ('\\' . | '\'\'' | ~['\\])* '\'';
EXECUTABLE_COMMENT_START : {!executableComment}? '/*!' [0-9]* {executableComment = true;} -> channel(HIDDEN);
EXECUTABLE_COMMENT_END : {executableComment}? '*/' {executableComment = false;} -> channel(HIDDEN);
// Splitting inside a wrapper would send an incomplete executable comment to the server.
SEMI : {!executableComment}? ';';
LINE_COMMENT : (('--' [ \t\u0000-\u0008\u000B\u000C\u000E-\u001F\u007F] | '#') ~[\r\n]*
             | '--' ('\r'? '\n' | '\r' | EOF))
             -> channel(HIDDEN);
BLOCK_COMMENT : ('/**/' | '/*' (~[!*] | '*' ~[/]) .*? '*/') -> channel(HIDDEN);
WS : [ \t\r\n\u000B\u000C]+ -> channel(HIDDEN);
UNRECOGNIZED : .;
