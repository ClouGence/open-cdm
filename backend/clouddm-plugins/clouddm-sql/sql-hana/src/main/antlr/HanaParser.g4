parser grammar HanaParser;
options { tokenVocab = HanaLexer; }

// Strict analysis grammar: there is deliberately no opaque/unknown statement fallback.
statementRoot: statement SEMI? EOF;
statement: query | insertStatement | updateStatement | deleteStatement | mergeStatement
    | createSchema | createTable | createView | createIndex | createRoutine | createTrigger | createSequence | createSynonym
    | alterTable | dropStatement | renameStatement | truncateStatement | commentStatement
    | callStatement | anonymousBlock | block | sessionStatement | connectStatement | historySessionStatement | systemSessionStatement
    | transactionStatement | explainStatement | securityStatement;

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

insertStatement: (INSERT | UPSERT | REPLACE) INTO qualifiedName columnNames? (valuesClause | (OVERRIDING USER VALUE)? query) (WITH PRIMARY KEY)?;
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
connectStatement: CONNECT (userName=identifier PASSWORD (identifier | STRING) | WITH SAML ASSERTION STRING | WITH JWT STRING);
historySessionStatement: SET historySessionTarget TO (NOW | COMMIT ID INTEGER | UTCTIMESTAMP STRING);
historySessionTarget: HISTORY SESSION;
systemSessionStatement: ALTER SYSTEM (
    ALTER SESSION sessionId=INTEGER (SET key=STRING EQ value=STRING | UNSET key=STRING)
    | CANCEL (WORK IN)? SESSION connectionId=STRING
    | DISCONNECT SESSION connectionId=STRING
);
// Security management uses strict, typed rules. Payload strings remain opaque.
securityStatement: securityUser | securityUsergroup | securityRole | securityGrant
    | securityPolicy | securityCredential | securityProvider | securityValidate
    | securityAudit | securityAuditLog | securityCertificate | securityPublicKey
    | securityPse | securityClientKey | securityEncryption | securityKeyConfiguration | securityClientPki;
securityUser: CREATE RESTRICTED? USER userName=identifier securityCreateUserOption*
    | ALTER USER userName=identifier (securitySpecialGrant | securityConnectAttempts | securityAlterUserOption+)
    | DROP USER userName=identifier (CASCADE | RESTRICT)?;
securityCreateUserOption: securityPassword | securityValidity | securityUserParameters
    | SET securityUsergroupReference | securityAuthorization | IDENTIFIED EXTERNALLY AS STRING
    | WITH securityIdentity | WITH securityRemoteIdentity;
securityAlterUserOption: securityCreateUserOption | securityClearParameters
    | UNSET USERGROUP | (ENABLE | DISABLE) (securityAuthMethod | PASSWORD LIFETIME | CLIENT CONNECT)
    | FORCE PASSWORD CHANGE | ACTIVATE | DEACTIVATE USER NOW
    | (ADD | DROP) (securityIdentity | securityRemoteIdentity);
securityPassword: PASSWORD (identifier | STRING) (NO FORCE_FIRST_PASSWORD_CHANGE)?;
securityValidity: VALID (FROM (NOW | STRING))? UNTIL (FOREVER | STRING);
securityUserParameters: SET PARAMETER securityUserParameter (COMMA securityUserParameter)*;
securityUserParameter: securityParameterName EQ STRING;
securityParameterName: CLIENT | LOCALE | TIME ZONE | EMAIL ADDRESS | STATEMENT (MEMORY | THREAD) LIMIT;
securityClearParameters: CLEAR (PARAMETER securityParameterName (COMMA securityParameterName)* | securityAllParameters);
securityAllParameters: ALL PARAMETERS;
securityConnectAttempts: (RESET | DROP) securityAttemptKey;
securityAttemptKey: CONNECT ATTEMPTS;
securityUsergroupReference: USERGROUP identifier;
securityAuthorization: AUTHORIZATION (LOCAL | LDAP);
securityAuthMethod: PASSWORD | KERBEROS | SAML | X509 | SAP LOGON TICKET | SAP ASSERTION TICKET | JWT | LDAP;
securityIdentity: IDENTITY (STRING | ANY)? (ISSUER STRING)? securityIdentityProvider+;
securityIdentityProvider: FOR ((SAML | JWT | X509) PROVIDER providerName=identifier | securityLdapIdentity | X509 | KERBEROS | SAP (LOGON | ASSERTION) TICKET);
securityLdapIdentity: LDAP PROVIDER;
securityRemoteIdentity: REMOTE IDENTITY userName=identifier AT DATABASE databaseName=identifier;
securitySpecialGrant: (GRANT | REVOKE) (CREATE ANY ON OWN SCHEMA | ROLE PUBLIC);

securityUsergroup: CREATE USERGROUP groupName=identifier securityGroupOption* (NO GRANT TO CREATOR)?
    | ALTER USERGROUP groupName=identifier securityGroupOption+
    | DROP USERGROUP groupName=identifier;
securityGroupOption: (ENABLE | DISABLE) (USER ADMIN | CLIENT CONNECT)
    | securityGroupParameters | securityGroupClearParameters | securityParameterSet
    | (ADD | ALTER)? CONNECT RESTRICTION securityRestrictionDefinitions
    | (ENABLE | DISABLE | DROP) CONNECT RESTRICTION (securityRestrictionNames | ALL);
securityGroupParameters: SET PARAMETER securityGroupParameter (COMMA securityGroupParameter)*;
securityGroupParameter: key=STRING EQ value=STRING;
securityGroupClearParameters: CLEAR PARAMETER STRING (COMMA STRING)*;
securityParameterSet: (ENABLE | DISABLE) PARAMETER SET STRING;
securityRestrictionDefinitions: securityRestrictionDefinition | LPAREN securityRestrictionDefinition (COMMA securityRestrictionDefinition)* RPAREN;
securityRestrictionDefinition: identifier (IP securityStrings (APPLICATION (ALLOW | DENY) securityStrings)? | APPLICATION (ALLOW | DENY) securityStrings);
securityRestrictionNames: identifier (COMMA identifier)*;
securityStrings: STRING | LPAREN STRING (COMMA STRING)* RPAREN;
securityRole: CREATE ROLE roleName=qualifiedName (LDAP GROUP STRING (COMMA STRING)*)? (NO GRANT TO CREATOR)?
    | ALTER ROLE roleName=qualifiedName (ADD | DROP) LDAP GROUP STRING (COMMA STRING)*
    | DROP ROLE roleName=qualifiedName;

securityGrant: GRANT securityGrantScope TO recipient=qualifiedName (WITH (ADMIN | GRANT) OPTION)?
    | REVOKE securityGrantScope FROM recipient=qualifiedName (GRANTED BY identifier)?;
securityGrantScope: securitySystemPrivilege (COMMA securitySystemPrivilege)*
    | securityObjectPrivilege (COMMA securityObjectPrivilege)* ON securityGrantObject
    | STRUCTURED PRIVILEGE policyName=qualifiedName
    | roles+=qualifiedName (COMMA roles+=qualifiedName)*;
securitySystemPrivilege: (USER | ROLE | AUDIT | CREDENTIAL | TRUST | CERTIFICATE | LDAP | DATABASE | DATA | INIFILE | RESOURCE | SESSION) ADMIN
    | AUDIT (READ | OPERATOR) | CATALOG READ | CREATE (SCHEMA | STRUCTURED PRIVILEGE | USERGROUP | REMOTE SOURCE)
    | ENCRYPTION ROOT KEY ADMIN;
securityObjectPrivilege: SELECT | INSERT | UPDATE | DELETE | EXECUTE | UNMASKED | CREATE ANY | ALL PRIVILEGES
    | ALTER | DROP | INDEX | REFERENCES | TRIGGER | CREATE VIRTUAL TABLE | LINKED DATABASE | USAGE | USERGROUP OPERATOR;
securityGrantObject: SCHEMA schemaName=identifier | USERGROUP groupName=identifier
    | REMOTE SOURCE sourceName=identifier | CLIENTSIDE ENCRYPTION COLUMN KEY keyName=qualifiedName
    | objectName=qualifiedName;

securityPolicy: (CREATE | ALTER) STRUCTURED PRIVILEGE policyName=qualifiedName FOR SELECT ON
    views+=qualifiedName (COMMA views+=qualifiedName)* (WHERE expression | CONDITION PROVIDER procedureName=qualifiedName)
    | DROP STRUCTURED PRIVILEGE policyName=qualifiedName;
securityCredential: (CREATE | ALTER) securityCredentialIdentity (USING STRING)? | DROP securityCredentialIdentity;
securityCredentialIdentity: CREDENTIAL FOR (USER userName=identifier)? COMPONENT component=STRING PURPOSE purpose=STRING TYPE mechanism=STRING;

securityProvider: CREATE JWT PROVIDER providerName=identifier WITH ISSUER STRING securityJwtClaims? securityProviderOption*
    | ALTER JWT PROVIDER providerName=identifier (SET (WITH ISSUER STRING | securityJwtClaim | PRIORITY INTEGER)
        | UNSET (CLAIM STRING)+ | securityProviderOption)
    | CREATE SAML PROVIDER providerName=identifier WITH SUBJECT STRING ISSUER STRING securitySamlOption*
    | ALTER SAML PROVIDER providerName=identifier (SET SUBJECT STRING ISSUER STRING | securitySamlOption)
    | CREATE LDAP PROVIDER providerName=identifier securityLdapOption+
    | ALTER LDAP PROVIDER providerName=identifier securityLdapOption+
    | CREATE X509 PROVIDER providerName=identifier WITH ISSUER STRING securityMatchingRules?
    | ALTER X509 PROVIDER providerName=identifier (SET (ISSUER STRING securityMatchingRules? | securityMatchingRules) | UNSET MATCHING RULES)
    | DROP (JWT | SAML | LDAP | X509) PROVIDER providerName=identifier CASCADE?;
securityJwtClaims: securityJwtClaim (COMMA? securityJwtClaim)*;
securityJwtClaim: CLAIM STRING (AS (EXTERNAL IDENTITY | APPLICATION USER) | HAS MEMBER STRING | EQ STRING);
securityProviderOption: securityIdentityCase | PRIORITY INTEGER | securityUserCreation;
securityIdentityCase: CASE {"SENSITIVE".equalsIgnoreCase(_input.LT(1).getText()) || "INSENSITIVE".equalsIgnoreCase(_input.LT(1).getText())}? WORD IDENTITY;
securitySamlOption: ENTITY ID STRING | securityProviderOption;
securityUserCreation: ENABLE USER CREATION (USER TYPE (STANDARD | RESTRICTED))? securityUsergroupReference? (LDAP AUTHORIZATION)?
    | DISABLE USER CREATION;
securityMatchingRules: MATCHING RULES STRING (COMMA STRING)*;
securityLdapOption: CREDENTIAL TYPE STRING USING STRING | USER LOOKUP URL STRING
    | NESTED GROUP LOOKUP URL (STRING | NULL) | ATTRIBUTE (DN | MEMBER_OF) (STRING | NULL)
    | SSL (ON | OFF) | DEFAULT (ON | OFF) | (ENABLE | DISABLE) PROVIDER
    | (ENABLE | DISABLE) USER CREATION FOR LDAP (USER TYPE (STANDARD | RESTRICTED))?;
securityValidate: VALIDATE LDAP PROVIDER providerName=identifier (CHECK USER (CREATION FOR LDAP USER)? userName=identifier
        (PASSWORD STRING | NO AUTHORIZATION CHECK)?)?
    | VALIDATE USER (userName=identifier PASSWORD (identifier | STRING) | WITH ASSERTION STRING);

securityAudit: CREATE AUDIT POLICY policyName=identifier (FOR databaseName=identifier)? AUDITING
        (ALL | SUCCESSFUL | UNSUCCESSFUL) securityAuditActions LEVEL (INFO | WARNING | CRITICAL | ALERT | EMERGENCY) securityAuditTrail?
    | ALTER AUDIT POLICY policyName=identifier ((FOR databaseName=identifier)? (ENABLE | DISABLE)
        | SET securityAuditTrail | RESET TRAIL TYPE | (SET RETENTION INTEGER | RESET RETENTION))
    | DROP AUDIT POLICY policyName=identifier (FOR databaseName=identifier)?;
securityAuditActions: ACTIONS EXCEPT? FOR securityAuditUsers
    | securityAuditAction (COMMA securityAuditAction)*
      (ON (SCHEMA schemaName=identifier | objects+=qualifiedName (COMMA objects+=qualifiedName)*))?
      (EXCEPT? FOR securityAuditUsers)?;
securityAuditUsers: identifier (COMMA identifier)*;
securityAuditAction: (GRANT | REVOKE) (PRIVILEGE | ROLE) | CONNECT | DISCONNECT SESSION
    | SELECT | INSERT | UPDATE | DELETE | EXECUTE | (CREATE | ALTER | DROP | RENAME) (TABLE | VIEW | PROCEDURE | FUNCTION | USER | ROLE | PSE);
securityAuditTrail: TRAIL TYPE securityAuditTrailType (COMMA securityAuditTrailType)*;
securityAuditTrailType: TABLE (RETENTION INTEGER)? | SYSLOG | CSV;
securityAuditLog: ALTER SYSTEM (ALTER securityAuditLogName (PAGE | COLUMN | DEFAULT)? LOADABLE
    | CLEAR securityAuditLogName (FOR AUDIT POLICY policyName=identifier)? (ALL | UNTIL STRING));
securityAuditLogName: AUDIT LOG;

securityCertificate: CREATE CERTIFICATE certificateName=identifier FROM STRING (COMMENT STRING)?
    | DROP CERTIFICATE securityCertificateId;
securityCertificateId: identifier | INTEGER;
securityPublicKey: CREATE securityPublicKeyType keyName=identifier? FROM STRING (KEY ID HINT STRING)? (COMMENT STRING)?
    | DROP securityPublicKeyType keyName=identifier;
securityPublicKeyType: PUBLIC KEY;
securityPse: CREATE PSE pseName=identifier
    | ALTER PSE pseName=identifier securityPseOption
    | SET PSE pseName=identifier PURPOSE securityPsePurpose (FOR securityPseTargets)?
    | UNSET PSE pseName=identifier PURPOSE securityPsePurpose
    | DROP PSE pseName=identifier CASCADE?;
securityPseOption: (ADD | DROP) (CERTIFICATE LPAREN securityCertificateId (COMMA securityCertificateId)* RPAREN
        | PUBLIC KEY LPAREN INTEGER (COMMA INTEGER)* RPAREN | securityPseTargets)
    | SET OWN CERTIFICATE STRING | UNSET OWN CERTIFICATE;
securityPseTargets: PROVIDER identifier (COMMA identifier)* | REMOTE SOURCE identifier (COMMA identifier)* | HOST STRING (COMMA STRING)*;
securityPsePurpose: JWT | SAML | SSL | REMOTE SOURCE | SAP LOGON | X509 | LDAP | DATABASE REPLICATION;
securityClientKey: CREATE CLIENTSIDE ENCRYPTION KEYPAIR pairName=identifier (ALGORITHM STRING)?
    | ALTER CLIENTSIDE ENCRYPTION KEYPAIR pairName=identifier securityKeyVersions
    | DROP CLIENTSIDE ENCRYPTION KEYPAIR pairName=identifier
    | CREATE CLIENTSIDE ENCRYPTION COLUMN KEY columnName=qualifiedName (ALGORITHM STRING)? (securityKeyEncryption | HEADER ONLY)
    | ALTER CLIENTSIDE ENCRYPTION COLUMN KEY columnName=qualifiedName ((ADD | DROP) KEYCOPY securityKeyEncryption | securityKeyEncryption | securityKeyVersions)
    | DROP CLIENTSIDE ENCRYPTION COLUMN KEY columnName=qualifiedName;
securityKeyEncryption: ENCRYPTED WITH KEYPAIR identifier;
securityKeyVersions: ADD NEW VERSION | DROP OLD VERSIONS;
securityEncryption: ALTER SYSTEM (APPLICATION ENCRYPTION securityApplicationKey
    | (BACKUP | LOG) ENCRYPTION securityRootEncryption
    | PERSISTENCE ENCRYPTION (securityApplicationKey | APPLY securityCurrentKey | ON | OFF)
    | securityEncryptionControl CONTROLLED BY (SYSTEM DATABASE | LOCAL DATABASES)
    | (SET | VALIDATE) securityEncryptionPassword STRING);
securityApplicationKey: CREATE NEW (securityRootKey (WITHOUT ACTIVATE)? | KEY) | ACTIVATE NEW securityRootKey;
securityRootEncryption: CREATE NEW securityRootKey (WITHOUT ACTIVATE)? | ACTIVATE NEW securityRootKey | ON | OFF;
securityRootKey: ROOT KEY;
securityCurrentKey: CURRENT KEY;
securityEncryptionControl: ENCRYPTION CONFIGURATION;
securityEncryptionPassword: ENCRYPTION ROOT KEYS BACKUP PASSWORD | LSS AUTO BACKUP PASSWORD;
securityKeyConfiguration: ALTER SYSTEM ((ADD | ALTER) KEY MANAGEMENT CONFIGURATION configName=identifier PROPERTIES STRING
    | (ACTIVATE | DROP) KEY MANAGEMENT CONFIGURATION configName=identifier);
securityClientPki: ALTER SYSTEM CLIENTPKI (UPDATE (CERTIFICATES | securityRootCa) | DROP securityRootCa);
securityRootCa: ROOT CA;

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
identifier: WORD | USER | QUOTED_IDENTIFIER | KEY | NEW | OLD | BTREE | CPBTREE | INVERTED | HASH | VALUE | INDIVIDUAL
    | CONNECT | PASSWORD | SAML | ASSERTION | JWT | HISTORY | NOW | ID | UTCTIMESTAMP | SYSTEM | CANCEL | DISCONNECT
    | ACTIONS | ACTIVATE | ADDRESS | ADMIN | ALERT | ALGORITHM | ALLOW | ANY | APPLICATION | APPLY | AT | ATTEMPTS
    | ATTRIBUTE | AUDIT | AUDITING | AUTHORIZATION | AUTO | BACKUP | CA | CATALOG | CERTIFICATE | CERTIFICATES
    | CHANGE | CLAIM | CLEAR | CLIENT | CLIENTPKI | CLIENTSIDE | COMPONENT | CONDITION | CONFIGURATION | CONTROLLED
    | CREATION | CREATOR | CREDENTIAL | CRITICAL | CSV | CURRENT | DATABASE | DATABASES | DEACTIVATE | DENY
    | DISABLE | DN | EMAIL | EMERGENCY | ENABLE | ENCRYPTED | ENCRYPTION | ENTITY | EXECUTE | EXTERNAL | EXTERNALLY
    | FORCE | FORCE_FIRST_PASSWORD_CHANGE | FOREVER | GRANT | GRANTED | HAS | HEADER | HOST | IDENTIFIED | INFO
    | INIFILE | IP | ISSUER | KERBEROS | KEYCOPY | KEYPAIR | KEYS | LDAP | LIFETIME | LINKED | LOADABLE | LOCALE
    | LOG | LOGON | LOOKUP | LSS | MANAGEMENT | MATCHING | MEMBER | MEMBER_OF | MEMORY | NESTED | OFF | OPERATOR
    | OWN | PAGE | PARAMETER | PARAMETERS | PERSISTENCE | POLICY | PRIORITY | PRIVILEGE | PRIVILEGES | PROPERTIES
    | PROVIDER | PSE | PUBLIC | PURPOSE | REMOTE | REPLICATION | RESET | RESOURCE | RESTRICTED | RESTRICTION
    | RETENTION | REVOKE | ROLE | ROOT | RULES | SAP | SOURCE | SSL | STANDARD | STRUCTURED | SUBJECT | SUCCESSFUL
    | SYSLOG | THREAD | TICKET | TIME | TRAIL | TRUST | TYPE | UNMASKED | UNSUCCESSFUL | UNTIL | URL | USAGE
    | USERGROUP | VALID | VALIDATE | VERSION | VERSIONS | VIRTUAL | WARNING | WITHOUT | X509 | ZONE;

// Editor splitting checks structural boundaries even while expressions are incomplete.
// Execution and audit must enter statementRoot; they never fall back to splitRoot.
splitRoot: (splitStatement SEMI? | SEMI)* EOF;
splitStatement: splitItem+;
splitItem: splitParentheses | splitCase | splitBlock | splitAtom;
splitParentheses: LPAREN splitItem* RPAREN;
splitAtom: ~(SEMI | LPAREN | RPAREN | BEGIN | END | CASE | ELSEIF | INVALID_CHARACTER | UNTERMINATED_COMMENT);
// Provider identity comparison is not a CASE expression. Keep its soft keywords
// as WORD tokens so existing identifiers and strict expression parsing are unchanged.
splitCase: CASE (
    {"SENSITIVE".equalsIgnoreCase(_input.LT(1).getText()) || "INSENSITIVE".equalsIgnoreCase(_input.LT(1).getText())}? WORD IDENTITY
    | splitCaseItem+ END
);
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
