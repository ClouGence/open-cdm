/*
 * Auto-generated split lexer from BNF grammar by bnf2antlr.py.
 * Do not edit by hand; regenerate from the .bnf source.
 */
lexer grammar Sql92Lexer;

// Literal tokens used by the parser grammar.
T__0 : 'E';
T__1 : '+';
T__2 : '-';
T__3 : ['];
T__4 : ':';
T__5 : 'NAMES';
T__6 : 'ARE';
T__7 : 'LANGUAGE';
T__8 : 'ADA';
T__9 : 'C';
T__10 : 'COBOL';
T__11 : 'FORTRAN';
T__12 : 'MUMPS';
T__13 : 'PASCAL';
T__14 : 'PLI';
T__15 : 'SCHEMA';
T__16 : 'AUTHORIZATION';
T__17 : 'DECLARE';
T__18 : 'LOCAL';
T__19 : 'TEMPORARY';
T__20 : 'TABLE';
T__21 : 'ON';
T__22 : 'COMMIT';
T__23 : 'PRESERVE';
T__24 : 'DELETE';
T__25 : 'ROWS';
T__26 : 'MODULE';
T__27 : 'CHARACTER';
T__28 : 'SET';
T__29 : 'CHAR';
T__30 : 'VARYING';
T__31 : 'VARCHAR';
T__32 : 'NATIONAL';
T__33 : 'NCHAR';
T__34 : 'NUMERIC';
T__35 : 'DECIMAL';
T__36 : 'DEC';
T__37 : 'INTEGER';
T__38 : 'INT';
T__39 : 'SMALLINT';
T__40 : 'FLOAT';
T__41 : 'REAL';
T__42 : 'DOUBLE';
T__43 : 'PRECISION';
T__44 : 'DATE';
T__45 : 'TIME';
T__46 : 'WITH';
T__47 : 'ZONE';
T__48 : 'TIMESTAMP';
T__49 : 'INTERVAL';
T__50 : 'TO';
T__51 : 'YEAR';
T__52 : 'MONTH';
T__53 : 'DAY';
T__54 : 'HOUR';
T__55 : 'MINUTE';
T__56 : 'SECOND';
T__57 : 'DEFAULT';
T__58 : 'USER';
T__59 : 'NULL';
T__60 : 'CONSTRAINT';
T__61 : 'NOT';
T__62 : 'UNIQUE';
T__63 : 'PRIMARY';
T__64 : 'KEY';
T__65 : 'REFERENCES';
T__66 : 'MATCH';
T__67 : 'FULL';
T__68 : 'PARTIAL';
T__69 : 'UPDATE';
T__70 : 'CASCADE';
T__71 : 'NO';
T__72 : 'ACTION';
T__73 : 'CHECK';
T__74 : 'OR';
T__75 : 'AND';
T__76 : 'IS';
T__77 : '*';
T__78 : '/';
T__79 : 'VALUE';
T__80 : 'INDICATOR';
T__81 : '?';
T__82 : 'COUNT';
T__83 : 'AVG';
T__84 : 'MAX';
T__85 : 'MIN';
T__86 : 'SUM';
T__87 : 'DISTINCT';
T__88 : 'ALL';
T__89 : 'INTERSECT';
T__90 : 'SELECT';
T__91 : 'AS';
T__92 : 'FROM';
T__93 : 'CROSS';
T__94 : 'JOIN';
T__95 : 'NATURAL';
T__96 : 'INNER';
T__97 : 'OUTER';
T__98 : 'UNION';
T__99 : 'LEFT';
T__100 : 'RIGHT';
T__101 : 'USING';
T__102 : 'WHERE';
T__103 : 'GROUP';
T__104 : 'BY';
T__105 : 'COLLATE';
T__106 : 'HAVING';
T__107 : 'VALUES';
T__108 : 'CORRESPONDING';
T__109 : 'NULLIF';
T__110 : 'COALESCE';
T__111 : 'CASE';
T__112 : 'END';
T__113 : 'WHEN';
T__114 : 'THEN';
T__115 : 'ELSE';
T__116 : 'CAST';
T__117 : 'POSITION';
T__118 : 'IN';
T__119 : 'SUBSTRING';
T__120 : 'FOR';
T__121 : 'UPPER';
T__122 : 'LOWER';
T__123 : 'CONVERT';
T__124 : 'TRANSLATE';
T__125 : 'TRIM';
T__126 : 'LEADING';
T__127 : 'TRAILING';
T__128 : 'BOTH';
T__129 : 'EXTRACT';
T__130 : 'AT';
T__131 : '=';
T__132 : '<';
T__133 : '>';
T__134 : 'BETWEEN';
T__135 : 'LIKE';
T__136 : 'ESCAPE';
T__137 : 'SOME';
T__138 : 'ANY';
T__139 : 'EXISTS';
T__140 : 'OVERLAPS';
T__141 : 'TRUE';
T__142 : 'FALSE';
T__143 : 'UNKNOWN';
T__144 : 'DEFERRABLE';
T__145 : 'INITIALLY';
T__146 : 'DEFERRED';
T__147 : 'IMMEDIATE';
T__148 : 'FOREIGN';
T__149 : 'INSENSITIVE';
T__150 : 'SCROLL';
T__151 : 'CURSOR';
T__152 : 'ORDER';
T__153 : 'ASC';
T__154 : 'DESC';
T__155 : 'READ';
T__156 : 'ONLY';
T__157 : 'OF';
T__158 : 'PROCEDURE';
T__159 : 'SQLCODE';
T__160 : 'SQLSTATE';
T__161 : 'CREATE';
T__162 : 'DOMAIN';
T__163 : 'GLOBAL';
T__164 : 'VIEW';
T__165 : 'OPTION';
T__166 : 'CASCADED';
T__167 : 'GRANT';
T__168 : 'PRIVILEGES';
T__169 : 'INSERT';
T__170 : 'USAGE';
T__171 : 'COLLATION';
T__172 : 'TRANSLATION';
T__173 : 'PUBLIC';
T__174 : 'ASSERTION';
T__175 : 'GET';
T__176 : 'EXTERNAL';
T__177 : 'PAD';
T__178 : 'SPACE';
T__179 : 'IDENTITY';
T__180 : 'DROP';
T__181 : 'RESTRICT';
T__182 : 'ALTER';
T__183 : 'ADD';
T__184 : 'COLUMN';
T__185 : 'REVOKE';
T__186 : 'OPEN';
T__187 : 'FETCH';
T__188 : 'INTO';
T__189 : 'NEXT';
T__190 : 'PRIOR';
T__191 : 'FIRST';
T__192 : 'LAST';
T__193 : 'ABSOLUTE';
T__194 : 'RELATIVE';
T__195 : 'CLOSE';
T__196 : 'CURRENT';
T__197 : 'TRANSACTION';
T__198 : 'ISOLATION';
T__199 : 'LEVEL';
T__200 : 'UNCOMMITTED';
T__201 : 'COMMITTED';
T__202 : 'REPEATABLE';
T__203 : 'SERIALIZABLE';
T__204 : 'WRITE';
T__205 : 'DIAGNOSTICS';
T__206 : 'SIZE';
T__207 : 'CONSTRAINTS';
T__208 : 'WORK';
T__209 : 'ROLLBACK';
T__210 : 'CONNECT';
T__211 : 'CONNECTION';
T__212 : 'DISCONNECT';
T__213 : 'CATALOG';
T__214 : 'SESSION';
T__215 : 'ALLOCATE';
T__216 : 'DESCRIPTOR';
T__217 : 'DEALLOCATE';
T__218 : 'TYPE';
T__219 : 'LENGTH';
T__220 : 'SCALE';
T__221 : 'NULLABLE';
T__222 : 'DATA';
T__223 : 'NAME';
T__224 : 'UNNAMED';
T__225 : 'PREPARE';
T__226 : 'DESCRIBE';
T__227 : 'INPUT';
T__228 : 'SQL';
T__229 : 'OUTPUT';
T__230 : 'EXECUTE';
T__231 : 'NUMBER';
T__232 : 'MORE';
T__233 : 'EXCEPTION';
T__234 : 'BEGIN';
T__235 : 'SECTION';
T__236 : 'EXEC';
T__237 : '&';
T__238 : 'BIT';
T__239 : '[';
T__240 : ']';
T__241 : 'WHENEVER';
T__242 : 'SQLERROR';
T__243 : 'FOUND';
T__244 : 'CONTINUE';
T__245 : 'GOTO';
T__246 : 'GO';

// --- Basic tokens ---
SEMI        : ';';
LEFT_PAREN  : '(';
RIGHT_PAREN : ')';
COMMA       : ',';
DOT         : '.';

IDENTIFIER
    : [a-zA-Z_] [a-zA-Z0-9_]*
    ;

NUMBER
    : DIGIT+ ( '.' DIGIT* )?
    | '.' DIGIT+
    ;

fragment DIGIT : [0-9];

STRING
    : ['] (~['] | [']['])* [']
    ;

WS
    : [ \t\r\n]+ -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '--' ~[\r\n]* -> channel(HIDDEN)
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;
