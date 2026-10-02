parser grammar HanaParser;
options { tokenVocab = HanaLexer; }

// Strict analysis grammar: there is deliberately no opaque/unknown statement fallback.
statementRoot: statement SEMI? EOF;
statement: query | insertStatement | updateStatement | deleteStatement | mergeStatement
    | createSchema | createTable | createView | createIndex | createRoutine | createTrigger | createSequence | createSynonym
    | alterTable | dropStatement | renameStatement | truncateStatement | commentStatement
    | callStatement | anonymousBlock | block | sessionStatement | transactionStatement | explainStatement;

query: withClause? queryTerm (setOperator queryTerm)* orderBy? limitClause? lockClause? hintClause?;
withClause: WITH cte (COMMA cte)*;
cte: identifier columnNames? AS LPAREN query RPAREN;
setOperator: (UNION | INTERSECT | EXCEPT | MINUS) (ALL | DISTINCT)?;
queryTerm: selectQuery | LPAREN query RPAREN;
selectQuery: SELECT (TOP INTEGER)? (DISTINCT | ALL)? selectItem (COMMA selectItem)*
    intoClause? fromClause? whereClause? groupBy? havingClause?;
selectItem: STAR | qualifiedName DOT STAR | expression (AS? identifier)?;
intoClause: INTO variable (COMMA variable)*;
fromClause: FROM tableSource (COMMA tableSource)*;
tableSource: tablePrimary joinClause*;
tablePrimary: qualifiedName alias? | LPAREN query RPAREN alias? | functionCall alias? | COLON identifier alias?;
alias: AS? identifier;
joinClause: ((INNER | LEFT OUTER? | RIGHT OUTER? | FULL OUTER?)? JOIN tablePrimary ON expression)
    | CROSS JOIN tablePrimary;
whereClause: WHERE expression;
groupBy: GROUP BY expression (COMMA expression)*;
havingClause: HAVING expression;
orderBy: ORDER BY orderItem (COMMA orderItem)*;
orderItem: expression (ASC | DESC)? (NULLS (FIRST | LAST))?;
limitClause: LIMIT INTEGER (OFFSET INTEGER)? (TOTAL ROWCOUNT)?;
lockClause: FOR (UPDATE | SHARE) (OF qualifiedName (COMMA qualifiedName)*)? (NOWAIT | WAIT INTEGER | IGNORE LOCKED)?;
hintClause: WITH HINT LPAREN identifier (LPAREN (literal (COMMA literal)*)? RPAREN)? (COMMA identifier)* RPAREN;

insertStatement: (INSERT | UPSERT | REPLACE) INTO qualifiedName columnNames? (valuesClause | query) (WITH PRIMARY KEY)?;
valuesClause: VALUES valueRow (COMMA valueRow)*;
valueRow: LPAREN expression (COMMA expression)* RPAREN;
updateStatement: UPDATE (TOP INTEGER)? qualifiedName alias? fromClause? SET assignment (COMMA assignment)* fromClause? whereClause? hintClause?;
assignment: qualifiedName EQ expression;
deleteStatement: DELETE (TOP INTEGER)? FROM qualifiedName alias? whereClause? hintClause?;
mergeStatement: MERGE INTO qualifiedName alias? USING tablePrimary ON expression mergeAction+;
mergeAction: WHEN MATCHED (AND expression)? THEN (UPDATE SET assignment (COMMA assignment)* | DELETE)
    | WHEN NOT MATCHED (AND expression)? THEN INSERT columnNames? VALUES valueRow;
callStatement: CALL qualifiedName LPAREN (expression (COMMA expression)*)? RPAREN;

createSchema: CREATE SCHEMA identifier;
createTable: CREATE ((ROW | COLUMN) | (LOCAL | GLOBAL) TEMPORARY (ROW | COLUMN)?)? TABLE qualifiedName
    (LPAREN tableElement (COMMA tableElement)* RPAREN | AS LPAREN? query RPAREN? (WITH (NO)? DATA)?)
    (COMMENT STRING)?;
tableElement: columnDefinition | tableConstraint;
columnDefinition: identifier dataType columnOption*;
dataType: identifier (LPAREN INTEGER (COMMA INTEGER)? RPAREN)? (ARRAY)?;
columnOption: NOT NULL | NULL | DEFAULT expression | PRIMARY KEY | UNIQUE | COMMENT STRING
    | GENERATED (ALWAYS | BY DEFAULT) AS IDENTITY (LPAREN identityOption+ RPAREN)?
    | GENERATED ALWAYS AS LPAREN expression RPAREN | REFERENCES qualifiedName columnNames? referentialAction*;
identityOption: START WITH INTEGER | INCREMENT BY INTEGER | MINVALUE INTEGER | MAXVALUE INTEGER | (NO)? CYCLE;
tableConstraint: (CONSTRAINT identifier)? (PRIMARY KEY indexType? constraintColumns | UNIQUE indexType? constraintColumns
    | FOREIGN KEY columnNames REFERENCES qualifiedName columnNames referentialAction* | CHECK LPAREN expression RPAREN);
referentialAction: ON (DELETE | UPDATE) (CASCADE | RESTRICT | SET NULL | SET DEFAULT | NO ACTION);
createView: CREATE (OR REPLACE)? VIEW qualifiedName columnNames? AS query (WITH (LOCAL | CASCADED)? CHECK OPTION)?;
indexType: BTREE | CPBTREE | INVERTED (HASH | VALUE | INDIVIDUAL);
constraintColumns: LPAREN identifier (ASC | DESC)? (COMMA identifier (ASC | DESC)?)* RPAREN;
alterColumnDefinition: identifier (dataType columnOption* | columnOption+);
createIndex: CREATE UNIQUE? indexType? INDEX qualifiedName ON qualifiedName LPAREN orderItem (COMMA orderItem)* RPAREN;
createSequence: CREATE SEQUENCE qualifiedName identityOption*;
createSynonym: CREATE (OR REPLACE)? SYNONYM qualifiedName FOR qualifiedName;
alterTable: ALTER TABLE qualifiedName alterAction;
alterAction: ADD LPAREN tableElement (COMMA tableElement)* RPAREN
    | ALTER LPAREN alterColumnDefinition (COMMA alterColumnDefinition)* RPAREN
    | DROP LPAREN identifier (COMMA identifier)* RPAREN
    | ADD tableConstraint | DROP CONSTRAINT identifier | DROP PRIMARY KEY | ROW | COLUMN;
dropStatement: DROP objectType qualifiedName (CASCADE | RESTRICT)?;
objectType: SCHEMA | TABLE | VIEW | INDEX | SEQUENCE | SYNONYM | PROCEDURE | FUNCTION | TRIGGER;
renameStatement: RENAME (TABLE | COLUMN | INDEX) qualifiedName TO qualifiedName;
truncateStatement: TRUNCATE TABLE qualifiedName;
commentStatement: COMMENT ON (TABLE | VIEW | COLUMN) qualifiedName IS (STRING | NULL);

createRoutine: CREATE (OR REPLACE)? (PROCEDURE | FUNCTION) qualifiedName LPAREN (parameter (COMMA parameter)*)? RPAREN
    (RETURNS (identifier? dataType | TABLE LPAREN columnDefinition (COMMA columnDefinition)* RPAREN))?
    routineOption* AS block;
parameter: (IN | OUT | INOUT)? identifier (dataType | TABLE LPAREN columnDefinition (COMMA columnDefinition)* RPAREN) (DEFAULT expression)?;
routineOption: LANGUAGE SQLSCRIPT | SQL SECURITY (DEFINER | INVOKER) | DEFAULT SCHEMA identifier | READS SQL DATA | DETERMINISTIC;
createTrigger: CREATE TRIGGER qualifiedName (BEFORE | AFTER | INSTEAD OF) (INSERT | UPDATE | DELETE)
    (OR (INSERT | UPDATE | DELETE))* ON qualifiedName (REFERENCING (NEW | OLD) (ROW | TABLE) identifier)*
    (FOR EACH (ROW | STATEMENT))? block;
anonymousBlock: DO (LPAREN (parameter (COMMA parameter)*)? RPAREN)? block;
block: BEGIN ((SEQUENTIAL | PARALLEL) EXECUTION | AUTONOMOUS TRANSACTION)? blockItem* END;
blockItem: (statement | declaration | variableAssignment | ifStatement | whileStatement | forStatement
    | loopStatement | returnStatement | BREAK | CONTINUE) SEMI;
declaration: DECLARE identifier (dataType | TABLE LPAREN columnDefinition (COMMA columnDefinition)* RPAREN) ((EQ | DEFAULT) expression)?
    | DECLARE EXIT HANDLER FOR SQLEXCEPTION block;
variableAssignment: identifier (EQ | ASSIGN) (query | expression);
ifStatement: IF expression THEN blockItem* (ELSEIF expression THEN blockItem*)* (ELSE blockItem*)? END IF;
whileStatement: WHILE expression DO blockItem* END WHILE;
forStatement: FOR identifier IN expression DOT DOT expression DO blockItem* END FOR;
loopStatement: LOOP blockItem* END LOOP;
returnStatement: RETURN (query | expression)?;
sessionStatement: SET SCHEMA identifier | SET TRANSACTION (READ ONLY | READ WRITE | ISOLATION LEVEL (READ COMMITTED | REPEATABLE READ | SERIALIZABLE))
    | SET SESSION? STRING EQ STRING | UNSET SESSION? STRING;
transactionStatement: COMMIT WORK? | ROLLBACK WORK? (TO SAVEPOINT identifier)? | SAVEPOINT identifier;
explainStatement: EXPLAIN PLAN (SET STATEMENT_NAME EQ STRING)? FOR (query | insertStatement | updateStatement | deleteStatement | mergeStatement);

expression: orExpression;
orExpression: andExpression (OR andExpression)*;
andExpression: notExpression (AND notExpression)*;
notExpression: NOT notExpression | predicate;
predicate: additiveExpression ((EQ | NE | LT | LE | GT | GE) additiveExpression
    | IS NOT? NULL | NOT? BETWEEN additiveExpression AND additiveExpression
    | NOT? IN LPAREN (query | expression (COMMA expression)*) RPAREN
    | NOT? LIKE additiveExpression (ESCAPE additiveExpression)?)?;
additiveExpression: multiplicativeExpression ((PLUS | MINUS_OP | CONCAT) multiplicativeExpression)*;
multiplicativeExpression: unaryExpression ((STAR | DIV | MOD) unaryExpression)*;
unaryExpression: (PLUS | MINUS_OP)* primary;
primary: literal | variable | parameterMarker | functionCall | qualifiedName
    | LPAREN (query | expression) RPAREN | EXISTS LPAREN query RPAREN
    | CASE expression? (WHEN expression THEN expression)+ (ELSE expression)? END
    | CAST LPAREN expression AS dataType RPAREN;
functionCall: qualifiedName LPAREN ((DISTINCT | ALL)? (STAR | expression (COMMA expression)*))? RPAREN
    (OVER LPAREN (PARTITION BY expression (COMMA expression)*)? orderBy? RPAREN)?;
variable: COLON COLON? identifier (DOT identifier)*;
parameterMarker: QUESTION;
literal: STRING | INTEGER | DECIMAL | NULL | TRUE | FALSE | DEFAULT;
columnNames: LPAREN identifier (COMMA identifier)* RPAREN;
qualifiedName: identifier (DOT identifier)*;
identifier: WORD | QUOTED_IDENTIFIER | KEY | NEW | OLD | BTREE | CPBTREE | INVERTED | HASH | VALUE | INDIVIDUAL;

// Editor splitting checks structural boundaries even while expressions are incomplete.
// Execution and audit must enter statementRoot; they never fall back to splitRoot.
splitRoot: (splitStatement SEMI? | SEMI)* EOF;
splitStatement: splitItem+;
splitItem: splitParentheses | splitCase | splitBlock | splitAtom;
splitParentheses: LPAREN splitItem* RPAREN;
splitAtom: ~(SEMI | LPAREN | RPAREN | BEGIN | END | CASE | ELSEIF | INVALID_CHARACTER | UNTERMINATED_COMMENT);
splitCase: CASE splitCaseItem+ END;
splitCaseItem: splitParentheses | splitCase | splitAtom;
splitBlock: BEGIN splitBody END {
    int next = _input.LA(1);
    if (next == IF || next == WHILE || next == LOOP || next == CASE || next == FOR) {
        notifyErrorListeners("Unexpected control-flow qualifier after END of BEGIN block");
    }
};
splitBody: ((SEQUENTIAL | PARALLEL) EXECUTION | AUTONOMOUS TRANSACTION)? (splitBlockStatement SEMI)*;
splitBlockStatement: splitBlock | splitIf | splitWhile | splitFor | splitLoop | splitBodyHead splitItem*;
splitBodyHead: splitParentheses
    | ~(SEMI | LPAREN | RPAREN | BEGIN | END | CASE | IF | THEN | ELSEIF | ELSE | FOR | WHILE | LOOP
        | AUTONOMOUS | TRANSACTION | SEQUENTIAL | PARALLEL | EXECUTION | INVALID_CHARACTER | UNTERMINATED_COMMENT);
splitIf: IF splitCondition THEN splitBody (ELSEIF splitCondition THEN splitBody)* (ELSE splitBody)? END IF;
splitWhile: WHILE splitCondition DO splitBody END WHILE;
splitFor: FOR splitCondition DO splitBody END FOR;
splitLoop: LOOP splitBody END LOOP;
splitCondition: (splitParentheses | splitCase | splitConditionAtom)+;
splitConditionAtom: ~(SEMI | LPAREN | RPAREN | BEGIN | END | CASE | THEN | ELSEIF | ELSE | DO
    | AUTONOMOUS | TRANSACTION | SEQUENTIAL | PARALLEL | EXECUTION | INVALID_CHARACTER | UNTERMINATED_COMMENT);
