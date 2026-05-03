// Define a grammar called Hello
lexer grammar MongoLexer;

channels { COMMENT }

/* skip spaces */
SPACE      : [ \t\f\r\n]+              -> channel(HIDDEN); // skip spaces, (空格\水平制表符\换行\回车\换页)
COMMENT1: '//' (~[\n\r])*   -> channel(COMMENT);
COMMENT2: '/*' .*? '*/'         -> channel(COMMENT);
//EOL     : ('\r''\n'? | '\n');

/* key command words */
DB: 'db';
SHOW: 'show';
DATABASES: 'databases';
DBS: 'dbs';
COLLECTIONS: 'collections';
USE: 'use';



//db method
SERVER_BUILD_INFO: 'serverBuildInfo';
HELLO: 'hello';
SERVER_STATUS:  'serverStatus';
DROP_DATABASE:  'dropDatabase';
CREATE_COLLECTION: 'createCollection';
STATS: 'stats';
SET_PROFILING_LEVEL: 'setProfilingLevel';
ADMIN_COMMAND: 'adminCommand';


// func
GET_COLLECTION: 'getCollection';
AGGREGATE: 'aggregate';
COUNT_DOCUMENTS: 'countDocuments';
CREATE_INDEXES: 'createIndexes';
CREATE_INDEX: 'createIndex';
DISTINCT:'distinct';
DROP_INDEXES: 'dropIndexes';
DROP_INDEX: 'dropIndex';
COUNT: 'count';
ESTIMATED_DOCUMENT_COUNT: 'estimatedDocumentCount';
GET_INDEXES: 'getIndexes';
LATENCY_STATS: 'latencyStats';
RENAME_COLLECTION: 'renameCollection';
REPLACE_ONE:'replaceOne';
VALIDATE: 'validate';
RUN_COMMAND: 'runCommand';
HIDE_INDEX: 'hideIndex';
UNHIDE_INDEX: 'unhideIndex';
// collection func
FIND: 'find';
S_SKIP: 'skip';
LIMIT:  'limit';
SORT:   'sort';
S_COMMENT: 'comment';
BATCH_SIZE: 'batchSize';
MAX_TIME_MS: 'maxTimeMS';
COLLATION: 'collation';
LET: 'let';
MAX: 'max';
MIN: 'min';
RETURN_KEY: 'returnKey';
SHOW_RECORD_ID: 'showRecordId';
NO_CURSOR_TIMEOUT: 'noCursorTimeout';
MAX_AWAIT_TIME_MS: 'maxAwaitTimeMS';
ALLOW_DISK_USE: 'allowDiskUse';
FIND_ONE: 'findOne';
FIND_ONE_AND_DELETE: 'findOneAndDelete';
FIND_ONE_AND_REPLACE: 'findOneAndReplace';
FIND_ONE_AND_UPDATE: 'findOneAndUpdate';
INSERT:     'insert';
INSERT_ONE: 'insertOne';
INSERT_MANY: 'insertMany';
DELETE_ONE: 'deleteOne';
DELETE_MANY: 'deleteMany';
UPDATE_ONE: 'updateOne';
UPDATE_MANY: 'updateMany';
DROP: 'drop';

// unsupported

WATCH: 'watch';
VERSION: 'version';
SHUTDOWN_SERVER: 'shutdownServer';
SET_LOG_LEVEL: 'setLogLevel';
SERVER_CMD_LINE_OPTS: 'serverCmdLineOpts';
ROTATE_CERTIFICATES: 'rotateCertificates';
PRINT_SHARDING_STATUS: 'printShardingStatus';
PRINT_SECONDARY_REPLICATION_INFO: 'printSecondaryReplicationInfo';
PRINT_REPLICATION_INFO: 'printReplicationInfo';
PRINT_COLLECTION_STATS: 'printCollectionStats';
LIST_COMMANDS: 'listCommands';
HOST_INFO: 'hostInfo';
HELP: 'help';
GET_SIBLING_DB: 'getSiblingDB';
GET_REPLICATION_INFO: 'getReplicationInfo';
GET_PROFILING_STATUS: 'getProfilingStatus';
GET_NAME: 'getName';
GET_MONGO: 'getMongo';
GET_LOG_COMPONENTS: 'getLogComponents';
GET_COLLECTION_NAMES: 'getCollectionNames';
GET_COLLECTION_INFOS: 'getCollectionInfos';
FSYNC_UNLOCK: 'fsyncUnlock';
FSYNC_LOCK: 'fsyncLock';
CREATE_VIEW: 'createView';
COMMAND_HELP: 'commandHelp';
CREATE_SEARCH_INDEX:'createSearchIndex';
DROP_SEARCH_INDEX:'dropSearchIndex';
GET_SEARCH_INDEXES:'getSearchIndexes';
UPDATE_SEARCH_INDEX:'updateSearchIndex';
CREATE_ROLE: 'createRole';
DROP_ROLE: 'dropRole';
DROP_ALL_ROLES: 'dropAllRoles';
GET_ROLE: 'getRole';
GET_ROLES: 'getRoles';
GRANT_PRIVILEGES_TO_ROLE: 'grantPrivilegesToRole';
REVOKE_PRIVILEGES_FROM_ROLE: 'revokePrivilegesFromRole';
GRANT_ROLES_TO_ROLE: 'grantRolesToRole';
REVOKE_ROLES_FROM_ROLE: 'revokeRolesFromRole';
UPDATE_ROLE: 'updateRole';
AUTH: 'auth';
CHANGE_USER_PASSWORD: 'changeUserPassword';
CREATE_USER: 'createUser';
DROP_USER: 'dropUser';
DROP_ALL_USERS: 'dropAllUsers';
GET_USER:'getUser';
GET_USERS:'getUsers';
GRANT_ROLES_TO_USER: 'grantRolesToUser';
REMOVE_USER:'removeUser';
REVOKE_ROLES_FROM_USER:'revokeRolesFromUser';
UPDATE_USER:'updateUser';




TRUE    : T R U E;
FALSE   : F A L S E;
NULL    : N U L L;


STRING:
    DOUBLE_QUOTA ( ~([\\"]) | ('\\' .) | (DOUBLE_QUOTA DOUBLE_QUOTA) )* DOUBLE_QUOTA
    | SINGLE_QUOTA ( ~([\\']) | ('\\' .) | (SINGLE_QUOTA SINGLE_QUOTA) )* SINGLE_QUOTA
    ;


//
DOT:                                 '.';
LS_BRACKET:                          '(';
RS_BRACKET:                          ')';
LM_BRACKET:                           '[';
RM_BRACKET:                           ']';
LB_BRACKET:                           '{';
RB_BRACKET:                           '}';
COMMA:                                ',';
COLON:                                ':';
DOUBLE_QUOTA:                          '"';
SINGLE_QUOTA:                          '\'';
SEMI:                                   ';';


ID:                                  ID_LITERAL;

fragment ID_LITERAL:                 [a-zA-Z_$0-9\u0080-\uFFFF]*?[a-zA-Z_$\u0080-\uFFFF]+?[a-zA-Z_$0-9\u0080-\uFFFF]*;

//
fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];






NUMBER
    : '-'? INT ('.' [0-9]+)? EXP?
    ;

fragment INT
    // integer part forbids leading 0s (e.g. `01`)
    : '0'
    | [1-9] [0-9]*
    ;

// no leading zeros

fragment EXP
    // exponent number permits leading 0s (e.g. `1e01`)
    : [Ee] [+-]? [0-9]+
    ;
