parser grammar HanaParser;
options { tokenVocab = HanaLexer; }

// Strict analysis grammar: there is deliberately no opaque/unknown statement fallback.
statementRoot: statement SEMI? EOF;
statement: query | insertStatement | updateStatement | deleteStatement | mergeStatement
    | createSchema | createTable | createView | createIndex | createRoutine | createTrigger | createSequence | createSynonym
    | alterTable | dropStatement | renameStatement | truncateStatement | commentStatement
    | callStatement | anonymousBlock | block | sessionStatement | connectStatement | historySessionStatement | systemSessionStatement
    | transactionStatement | explainStatement | securityStatement | systemManagement | databaseManagement | diagnosticStatement;

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

insertStatement: (INSERT INTO | (UPSERT | REPLACE) INTO?) qualifiedName columnNames? (valuesClause | (OVERRIDING USER VALUE)? query) (WITH PRIMARY KEY)?;
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
truncateStatement: TRUNCATE TABLE qualifiedName maintenancePartitions?;
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
// System/resource management: typed syntax, with opaque property and license values.
systemManagement: systemIni | systemIniHistory | systemReconfigure | systemStopService | systemLicense
    | workloadClass | workloadMapping | systemPlacement | systemVolume | systemReclaim | systemReplication;
systemIni: ALTER SYSTEM ALTER CONFIGURATION LPAREN fileName=STRING COMMA layer=STRING (COMMA layerName=STRING)? RPAREN
    (SET systemIniSet (COMMA systemIniSet)* | UNSET systemIniKey (COMMA systemIniKey)*)
    (WITH RECONFIGURE)? (COMMENT STRING)?;
systemIniKey: LPAREN section=STRING COMMA key=STRING RPAREN;
systemIniSet: systemIniKey EQ STRING;
systemIniHistory: ALTER SYSTEM CLEAR INIFILE CONTENT HISTORY;
systemReconfigure: ALTER SYSTEM RECONFIGURE SERVICE LPAREN serviceName=STRING COMMA host=STRING COMMA port=INTEGER RPAREN;
systemStopService: ALTER SYSTEM STOP SERVICE (address=STRING | LPAREN host=STRING COMMA port=INTEGER RPAREN)
    (IMMEDIATE (WITH COREFILE)?)?;
systemLicense: SET SYSTEM LICENSE STRING | UNSET SYSTEM LICENSE ALL;
workloadClass: CREATE WORKLOAD CLASS className=identifier workloadParent? workloadProperties? workloadState?
    | ALTER WORKLOAD CLASS (ALL workloadState | className=identifier
        (workloadParent workloadProperties? workloadState? | workloadProperties workloadState? | workloadState))
    | DROP WORKLOAD CLASS className=identifier;
workloadParent: PARENT (identifier | NULL);
workloadState: ENABLE | DISABLE;
workloadProperties: SET? workloadProperty (COMMA workloadProperty)* (UNSET workloadKeys)?
    | UNSET workloadKeys (SET workloadProperty (COMMA workloadProperty)*)?;
workloadProperty: key=STRING EQ value=STRING;
workloadKeys: STRING (COMMA STRING)*;
workloadMapping: (CREATE | ALTER) WORKLOAD MAPPING mappingName=identifier WORKLOAD CLASS className=identifier
        workloadProperties? (WITH WILDCARD STRING?)?
    | DROP WORKLOAD MAPPING mappingName=identifier;
systemPlacement: ALTER SYSTEM ALTER TABLE PLACEMENT
    (LOCATION locationName=identifier (SET LPAREN placementLocationProperty (COMMA placementLocationProperty)* RPAREN | UNSET)
    | (LPAREN placementSelector (COMMA placementSelector)* RPAREN)?
        (SET LPAREN placementProperty (COMMA placementProperty)* RPAREN
        | UNSET (LPAREN placementKey (COMMA placementKey)* RPAREN)?));
placementSelector: key=(SCHEMA_NAME | TABLE_NAME | GROUP_NAME | GROUP_TYPE | SUB_TYPE) ARROW value=STRING;
placementLocationProperty: (INCLUDE | EXCLUDE) ARROW STRING;
placementProperty: placementKey ARROW (STRING | INTEGER);
placementKey: LOCATION | MIN_ROWS_FOR_PARTITIONING | INITIAL_PARTITIONS | REPARTITIONING_THRESHOLD
    | SAME_PARTITION_COUNT | DYNAMIC_RANGE_THRESHOLD | PAGE_LOADABLE | PERSISTENT_MEMORY | NUMA_NODE_INDEXES
    | REPLICA_COUNT | REPLICA_TYPE;
systemVolume: ALTER SYSTEM ALTER DATAVOLUME (ADD PARTITION (PATH path=STRING)? | DROP PARTITION partitionId=INTEGER)
    (SYSTEM REPLICATION SITE (siteName=STRING | siteId=INTEGER))?;
systemReclaim: ALTER SYSTEM RECLAIM (ROW? DATA SPACE IMMEDIATE? address=STRING?
    | DATAVOLUME SPACE? address=STRING? INTEGER DEFRAGMENT | FLEXIBLE TABLES | (ROW | COLUMN)? LOB SPACE | VERSION SPACE);
systemReplication: ALTER SYSTEM (
    ENABLE SYSTEM REPLICATION siteName=STRING
    | DISABLE SYSTEM REPLICATION FORCE?
    | (REGISTER | UNREGISTER) SYSTEM REPLICATION SITE siteName=STRING REMOTE HOST host=STRING
        REPLICATION MODE STRING (OPERATION MODE STRING)? FORCE? (FULL REPLICA)?
);

// Database lifecycle and data maintenance use typed clauses; payloads remain opaque.
databaseManagement: tenantDatabase | databaseRuntime | databaseMaintenance | tableReplicas
    | backupData | backupCancel | backupCheck | backupCatalog | backupSnapshot | backupList
    | recoverDatabase | backupKeys | recoverKeys | tableLoad | tableUnload | deltaMerge
    | catalogExport | catalogImport | exportRows | importRows | importScan;
tenantDatabase: CREATE DATABASE tenant=identifier
        (AS REPLICA OF sourceTenant=identifier? databaseLocation
        | databaseLocation? databaseAddService* SYSTEM USER PASSWORD (identifier | STRING)
            databaseOsIdentity? (NO START)? databaseRestart?)
    | ALTER DATABASE tenant=identifier (databaseAddService+ | REMOVE STRING databaseLocation
        | ALTER STRING databaseLocation TO LOCATION? STRING | databaseOsIdentity | databaseRestart
        | (CREATE | DROP) FALLBACK SNAPSHOT | FINALIZE REPLICA (DROP SOURCE DATABASE)? | CANCEL REPLICA)
    | RENAME DATABASE tenant=identifier TO newTenant=identifier
    | DROP DATABASE tenant=identifier (DROP BACKUPS)?;
databaseAddService: ADD STRING databaseLocation?;
databaseLocation: AT LOCATION? STRING;
databaseOsIdentity: OS USER STRING OS GROUP STRING;
databaseRestart: (NO | DEFAULT) RESTART;
databaseRuntime: ALTER SYSTEM (START DATABASE tenant=identifier (FROM FALLBACK SNAPSHOT)?
    | STOP DATABASE tenant=identifier (IMMEDIATE (WITH COREFILE)?)?);
databaseMaintenance: ALTER SYSTEM (SAVEPOINT | LOGGING (ON | OFF) | RECLAIM LOG);
tableReplicas: ALTER SYSTEM (ENABLE | DISABLE) ALL (ASYNCHRONOUS | SYNCHRONOUS)? TABLE REPLICAS;
backupTenant: FOR identifier;
backupDestination: USING (FILE | BACKINT) LPAREN (directory=STRING COMMA)? prefix=STRING RPAREN;
backupPasswords: ENCRYPTION ROOT KEYS BACKUP PASSWORD (STRING | LPAREN STRING (COMMA STRING)* RPAREN);
backupData: BACKUP DATA (INCREMENTAL | DIFFERENTIAL)? backupTenant? backupDestination backupDataOption*;
backupDataOption: ASYNCHRONOUS | COMPRESSED | RETAINED | (INCLUDE | EXCLUDE) CONFIGURATION
    | TOOLOPTION STRING | COMMENT STRING | backupPasswords;
backupCancel: BACKUP CANCEL backupTenant? backupId=INTEGER;
backupCheck: BACKUP CHECK backupTenant? USING FILE LPAREN path=STRING RPAREN SIZE INTEGER
    | BACKUP CHECK ACCESS backupTenant? BACKUP_ID backupId=INTEGER recoveryReference*;
backupCatalog: BACKUP CATALOG ((SET | UNSET)? RETAINED backupTenant? BACKUP_ID backupId=INTEGER
    | DELETE backupTenant? (ALL BEFORE)? BACKUP_ID backupId=INTEGER (WITH (FILE | BACKINT) | COMPLETE)?);
backupSnapshot: BACKUP DATA FOR FULL SYSTEM
    (CREATE SNAPSHOT (COMMENT STRING)? (TIMEOUT INTEGER)?
    | CLOSE SNAPSHOT BACKUP_ID backupId=INTEGER (SUCCESSFUL STRING | UNSUCCESSFUL STRING?)
    | DROP SNAPSHOT);
backupList: BACKUP (LIST DATA backupTenant recoveryUntil | COMPLETE LIST DATA backupTenant)
    (recoveryReference | backupPasswords | LIMIT INTEGER (OFFSET INTEGER)?)*;
recoveryUntil: UNTIL (TIMESTAMP STRING | LOG POSITION INTEGER AT VOLUME INTEGER);
recoveryReference: USING SOURCE source=STRING
    | USING (CATALOG | LOG | DATA) PATH LPAREN paths+=STRING (COMMA paths+=STRING)* RPAREN
    | USING BACKUP_ID backupId=INTEGER | USING SNAPSHOT | USING CATALOG BACKINT | USING RESUME
    | backupDestination;
recoveryOption: recoveryReference | CHECK ACCESS (ALL | USING (FILE | BACKINT))
    | IGNORE (DELTA DATA BACKUPS | WORKERGROUPS | COMPATIBILITY CHECK)
    | CLEAR LOG | SET LICENSE STRING | INCLUDE CONFIGURATION | TOOLOPTION STRING | backupPasswords;
recoverDatabase: RECOVER DATA backupTenant? recoveryReference+ CLEAR LOG recoveryOption*
    | RECOVER DATABASE backupTenant recoveryUntil recoveryOption*;
rootKeys: ENCRYPTION ROOT KEYS;
backupKeys: BACKUP rootKeys (rootKeyPurpose (COMMA rootKeyPurpose)*)? backupTenant? backupDestination;
rootKeyPurpose: PERSISTENCE | APPLICATION | BACKUP | LOG;
recoverKeys: RECOVER rootKeys (AND SETTINGS)? backupTenant backupDestination PASSWORD STRING;
tableLoad: LOAD qualifiedName HISTORY? (ALL | DELTA | columnNames);
tableUnload: UNLOAD qualifiedName (persistentMemory | PARTITION LPAREN unloadPartition (COMMA unloadPartition)* RPAREN)?;
unloadPartition: INTEGER persistentMemory?;
persistentMemory: (DELETE | RETAIN) PERSISTENT MEMORY;
deltaMerge: MERGE HISTORY? DELTA OF qualifiedName (PART partitionId=INTEGER)?
    (WITH PARAMETERS LPAREN STRING EQ STRING (COMMA STRING EQ STRING)* RPAREN)? (FORCE REBUILD)?;
maintenancePartitions: PARTITION LPAREN INTEGER (COMMA INTEGER)* RPAREN;
transferObjects: ALL | qualifiedName (COMMA qualifiedName)*;
transferFormat: AS (BINARY (RAW | DATA)? | CSV);
transferCredential: CREDENTIAL STRING;
catalogExport: EXPORT transferObjects whereClause? transferFormat? INTO path=STRING
    (WITH catalogExportOption (COMMA? catalogExportOption)*)?;
catalogExportOption: REPLACE | THREADS INTEGER | SCRAMBLE BY STRING | NO (DEPENDENCIES | STATISTICS)
    | ENCRYPTION PASSWORD STRING | transferCredential;
catalogImport: IMPORT transferObjects transferFormat? FROM path=STRING databaseLocation?
    (WITH catalogImportOption (COMMA? catalogImportOption)*)?;
catalogImportOption: REPLACE | IGNORE EXISTING | DATA ONLY | THREADS INTEGER
    | NO (DEPENDENCIES | STATISTICS) | ENCRYPTION PASSWORD STRING | transferCredential | IGNORE NUMA NODE;
exportRows: EXPORT INTO path=STRING FROM qualifiedName (WITH exportRowsOption (COMMA? exportRowsOption)*)?;
exportRowsOption: THREADS INTEGER | COLUMN LIST IN FIRST ROW | FIELD DELIMITED BY STRING
    | OPTIONALLY ENCLOSED BY STRING | ESCAPE STRING | transferCredential;
importRows: IMPORT FROM (CSV FILE | CONTROL FILE)? path=STRING (INTO qualifiedName)?
    (WITH importRowsOption (COMMA? importRowsOption)*)?;
importRowsOption: THREADS INTEGER | BATCH INTEGER | TABLE LOCK | SKIP_ROWS FIRST INTEGER ROW
    | COLUMN LIST (IN FIRST ROW | columnNames) | FIELD DELIMITED BY STRING
    | OPTIONALLY ENCLOSED BY STRING | ESCAPE STRING | (DATE | TIME | TIMESTAMP) FORMAT STRING
    | FAIL ON INVALID DATA | ERROR LOG errorPath=STRING | transferCredential;
importScan: IMPORT SCAN path=STRING (WITH transferCredential)?;

// Diagnostic SQL uses typed rules; embedded hint SQL is a matching definition.
diagnosticStatement: diagnosticHint | abstractFilter | abstractPlan | diagnosticCache | diagnosticStatistics
    | monitoringReset | runtimeDump | kernelProfiler | sqlscriptProfiler | perftrace | traceFiles;
cachedPlan: SQL PLAN CACHE ENTRY planId=INTEGER (VARIANT variantId=INTEGER)?;
diagnosticHint: ALTER SYSTEM (ADD STATEMENT HINT diagnosticHints diagnosticHintOverride? (COMMENT STRING)? diagnosticHintRoutine? FOR diagnosticHintTarget
    | ALTER STATEMENT HINT (diagnosticHintOverride (COMMENT STRING)? | COMMENT STRING) diagnosticHintRoutine? FOR diagnosticHintTarget
    | REMOVE STATEMENT HINT ALL? diagnosticHintRoutine? FOR diagnosticHintTarget
    | (ENABLE | DISABLE) STATEMENT HINT (ALL | diagnosticHintRoutine? FOR diagnosticHintTarget));
diagnosticHints: LPAREN (identifier (LPAREN (literal (COMMA literal)*)? RPAREN)?
    (COMMA identifier (LPAREN (literal (COMMA literal)*)? RPAREN)?)*)? RPAREN;
diagnosticHintOverride: NO? OVERRIDE;
diagnosticHintRoutine: ON (PROCEDURE | FUNCTION) qualifiedName;
diagnosticHintTarget: STATEMENT HASH hash=STRING | query | insertStatement | updateStatement | deleteStatement | mergeStatement;
diagnosticPattern: {getInvokingContext(RULE_diagnosticHint) != null}? (HINT_PARAMETER | HINT_LIST);
abstractFilter: ALTER SYSTEM (ADD ABSTRACT SQL PLAN FILTER filterName=STRING SET abstractFilterProperty (COMMA abstractFilterProperty)*
    | REMOVE ABSTRACT SQL PLAN FILTER (filterName=STRING | ALL));
abstractFilterProperty: key=STRING EQ value=STRING;
abstractPlan: ALTER SYSTEM ((ENABLE | DISABLE | REMOVE) ABSTRACT SQL PLAN ENTRY (ALL | FOR planIds+=INTEGER (COMMA planIds+=INTEGER)*)
    | (START | STOP) CAPTURE ABSTRACT SQL PLAN (FOR USER identifier (COMMA identifier)*)? (WITH SQL PLAN CACHE)?
    | (START | STOP) APPLY ABSTRACT SQL PLAN
    | MIGRATE ABSTRACT SQL PLAN
    | UPDATE ABSTRACT SQL PLAN SET LOCATION abstractPlanLocation (COMMA abstractPlanLocation)* ALL);
abstractPlanLocation: source=STRING TO destination=STRING;
diagnosticCache: ALTER SYSTEM (CLEAR CACHE LPAREN cacheIds+=INTEGER (COMMA cacheIds+=INTEGER)* SYNC? RPAREN
    | CLEAR COLUMN JOIN DATA STATISTICS
    | CLEAR TIMEZONE CACHE DATASET dataset=STRING
    | CLEAR diagnosticResultCache
    | REFRESH diagnosticResultCache (qualifiedName | ENTRY entryId=INTEGER)
    | REMOVE diagnosticResultCache ENTRY entryId=INTEGER
    | CLEAR diagnosticSqlCache
    | (PIN | UNPIN) diagnosticSqlCache ENTRY LIKE? entryId=INTEGER (WITH HINT diagnosticHints)?
    | RECOMPILE diagnosticSqlCache ENTRY entryId=INTEGER
    | REMOVE diagnosticSqlCache (ENTRY entryId=INTEGER | WHERE expression));
diagnosticResultCache: (STATIC | DYNAMIC)? RESULT CACHE;
diagnosticSqlCache: SQL PLAN CACHE;
diagnosticStatistics: CREATE STATISTICS statisticName=qualifiedName? ON diagnosticStatisticsSource
        (TYPE diagnosticStatisticsType)? diagnosticStatisticsProperties? diagnosticInitialRefresh?
    | ALTER STATISTICS diagnosticStatisticsSelector diagnosticStatisticsAlter+ diagnosticInitialRefresh?
    | (REFRESH | DROP) STATISTICS diagnosticStatisticsSelector;
diagnosticStatisticsSelector: qualifiedName (COMMA qualifiedName)*
    | ON diagnosticStatisticsSource (HAVING? TYPE (diagnosticStatisticsType | ALL) (REFRESH TYPE (AUTO | MANUAL | ALL))?)?;
diagnosticStatisticsSource: qualifiedName columnNames? (EXACT | CASCADE)?;
diagnosticStatisticsAlter: SET diagnosticStatisticsProperties | (ADD | DROP) VALID FOR diagnosticStatisticsUsage;
diagnosticStatisticsProperties: diagnosticStatisticsProperty (COMMA diagnosticStatisticsProperty)*;
diagnosticStatisticsProperty: BUCKETS INTEGER | CONSTRAINT STRING | QERROR (INTEGER | DECIMAL) | QTHETA (INTEGER | DECIMAL)
    | MEMORY PERCENT? INTEGER | PERSISTENT (ON | OFF) | VALID FOR diagnosticStatisticsUsage
    | REFRESH TYPE (AUTO | MANUAL | DEFAULT) | ENABLE (ON | OFF) | ACCURACY (INTEGER | DECIMAL) | PREFIX BITS INTEGER;
diagnosticStatisticsUsage: (ESTIMATION | DATA DEPENDENCY) (COMMA (ESTIMATION | DATA DEPENDENCY))*;
diagnosticStatisticsType: HISTOGRAM | SIMPLE | TOPK | SKETCH | SAMPLE (SAMPLE SIZE INTEGER)? | RECORD COUNT;
diagnosticInitialRefresh: NO? INITIAL REFRESH;
monitoringReset: ALTER SYSTEM RESET MONITORING VIEW qualifiedName;
diagnosticLocation: AT LOCATION? (address=STRING | LPAREN host=STRING COMMA port=INTEGER RPAREN);
runtimeDump: ALTER SYSTEM CREATE (RUNTIMEDUMP diagnosticLocation? (PROFILE profile=STRING)?
    | WAITGRAPH diagnosticLocation? (ALL THREADS)?) (INTO FILE fileName=STRING)?;
kernelProfiler: ALTER SYSTEM (START KERNEL PROFILER diagnosticLocation? kernelProfilerScope*
        (USING ROLLING STRATEGY (INTERVAL INTEGER)?)? (MEMORY LIMIT INTEGER)? (SAMPLING INTERVAL INTEGER)?
    | STOP KERNEL PROFILER diagnosticLocation? (USING ROLLING STRATEGY)?
    | SAVE KERNEL PROFILER diagnosticLocation? (INTO (CPU FILE cpuFile=STRING (WAIT FILE waitFile=STRING)? | WAIT FILE waitFile=STRING))?
    | CLEAR KERNEL PROFILER diagnosticLocation?);
kernelProfilerScope: USER identifier | APPLICATIONUSER STRING | SESSION INTEGER | ROOT_STATEMENT_HASH STRING | TRACEPROFILE STRING;
sqlscriptProfiler: ALTER SYSTEM ((START | STOP | CLEAR) SQLSCRIPT PLAN PROFILER (FOR (SESSION sessionId=INTEGER | PROCEDURE qualifiedName))?
    | (START | STOP) SQLSCRIPT CODE COVERAGE (FOR SESSION sessionName=STRING)?);
perftrace: ALTER SYSTEM (START PERFTRACE perftraceOption* | STOP PERFTRACE
    | SAVE PERFTRACE (INTO FILE fileName=STRING)?
    | LOAD PERFTRACE (FILE fileName=STRING)? INTO TABLES qualifiedName (WITH REPLACE)?);
perftraceOption: USER identifier | APPLICATIONUSER STRING | APPLICATION STRING | PASSPORT_TRACELEVEL (MEDIUM | HIGH)
    | PLAN_EXECUTION | FUNCTION_PROFILER | DURATION INTEGER | ROOT_STATEMENT_HASH STRING;
traceFiles: ALTER SYSTEM (CLEAR TRACES LPAREN categories+=STRING (COMMA categories+=STRING)* RPAREN (UNTIL STRING)? (WITH BACKUP)?
    | REMOVE TRACES LPAREN host=STRING COMMA files+=STRING (COMMA files+=STRING)* RPAREN);

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
explainStatement: EXPLAIN PLAN (SET STATEMENT_NAME EQ STRING)? FOR (cachedPlan | query | insertStatement | updateStatement | deleteStatement | mergeStatement);

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
primary: literal | variable | parameterMarker | diagnosticPattern | functionCall | qualifiedName
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
    | USERGROUP | VALID | VALIDATE | VERSION | VERSIONS | VIRTUAL | WARNING | WITHOUT | X509 | ZONE
    | CLASS | CONTENT | COREFILE | DATAVOLUME | DEFRAGMENT | DYNAMIC_RANGE_THRESHOLD | EXCLUDE | FLEXIBLE
    | GROUP_NAME | GROUP_TYPE | IMMEDIATE | INCLUDE | INITIAL_PARTITIONS | LICENSE | LOB | LOCATION | MAPPING
    | MIN_ROWS_FOR_PARTITIONING | MODE | NUMA_NODE_INDEXES | OPERATION | PAGE_LOADABLE | PARENT | PATH
    | PERSISTENT_MEMORY | PLACEMENT | RECLAIM | RECONFIGURE | REGISTER | REPARTITIONING_THRESHOLD | REPLICA
    | REPLICA_COUNT | REPLICA_TYPE | SAME_PARTITION_COUNT | SCHEMA_NAME | SERVICE | SITE | SPACE | STOP | SUB_TYPE
    | TABLES | TABLE_NAME | UNREGISTER | WILDCARD | WORKLOAD
    | ACCESS | ASYNCHRONOUS | BACKINT | BACKUPS | BACKUP_ID | BATCH | BINARY | CLOSE | COMPATIBILITY | COMPLETE
    | COMPRESSED | CONTROL | DATE | DELIMITED | DELTA | DEPENDENCIES | DIFFERENTIAL | ENCLOSED | ERROR | EXISTING
    | EXPORT | FAIL | FALLBACK | FIELD | FILE | FINALIZE | FORMAT | IMPORT | INCREMENTAL | INVALID | LIST | LOAD
    | LOCK | LOGGING | NODE | NUMA | OPTIONALLY | OS | PART | PERSISTENT | POSITION | RAW | REBUILD | RECOVER
    | REMOVE | REPLICAS | RESTART | RESUME | RETAIN | RETAINED | SCAN | SCRAMBLE | SETTINGS | SIZE | SKIP_ROWS
    | SNAPSHOT | STATISTICS | SYNCHRONOUS | THREADS | TIMEOUT | TIMESTAMP | TOOLOPTION | UNLOAD | VOLUME
    | WORKERGROUPS
    | ABSTRACT | ACCURACY | APPLICATIONUSER | BITS | BUCKETS | CACHE | CAPTURE | CODE | COUNT | COVERAGE | CPU
    | DATASET | DEPENDENCY | DURATION | DYNAMIC | ENTRY | ESTIMATION | EXACT | FILTER | FUNCTION_PROFILER | HIGH
    | HISTOGRAM | INITIAL | INTERVAL | KERNEL | MANUAL | MEDIUM | MIGRATE | MONITORING | OVERRIDE
    | PASSPORT_TRACELEVEL | PERCENT | PERFTRACE | PIN | PLAN_EXECUTION | PREFIX | PROFILE | PROFILER | QERROR
    | QTHETA | RECOMPILE | RECORD | REFRESH | RESULT | ROLLING | ROOT_STATEMENT_HASH | RUNTIMEDUMP | SAMPLE
    | SAMPLING | SAVE | SIMPLE | SKETCH | STATIC | STRATEGY | SYNC | TIMEZONE | TOPK | TRACEPROFILE | TRACES
    | UNPIN | VARIANT | WAITGRAPH;

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
