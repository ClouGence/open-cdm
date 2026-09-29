parser grammar CbSqlParser;

options {
    tokenVocab = PgSqlLexer;
    superClass = CbSqlParserBase;
}

@header {
import com.clougence.clouddm.ds.cloudberry.sql.parser.CbSqlParserBase;
}

root
    : statement SEMI? EOF
    ;

statement
    : protocol_stmt
    | protocol_privilege_stmt
    | role_stmt
    | external_stmt
    | resource_stmt
    | retrieve_stmt
    ;

protocol_stmt
    : CREATE TRUSTED? protocol_keyword identifier OPEN_PAREN protocol_handler (COMMA protocol_handler)* CLOSE_PAREN
    | ALTER protocol_keyword identifier (RENAME TO identifier | OWNER TO identifier)
    | DROP protocol_keyword (IF_P EXISTS)? identifier
    ;

protocol_handler
    : identifier EQUAL StringConstant
    ;

protocol_privilege_stmt
    : GRANT protocol_privileges ON protocol_keyword identifier TO role_name_list (WITH GRANT OPTION)?
    | REVOKE (GRANT OPTION FOR)? protocol_privileges ON protocol_keyword identifier FROM role_name_list (CASCADE | RESTRICT)?
    ;

protocol_privileges
    : (SELECT | INSERT) (COMMA (SELECT | INSERT))*
    | ALL PRIVILEGES?
    ;

role_stmt
    : CREATE role_keyword role_name WITH? create_role_option*
    | ALTER role_keyword role_name WITH? alter_role_option+
    ;

create_role_option
    : alter_role_option
    | SYSID Integral
    | ADMIN role_name_list
    | ROLE role_name_list
    | IN_P (ROLE | GROUP_P) role_name_list
    ;

alter_role_option
    : cloudberry_role_option
    | PASSWORD (StringConstant | NULL_P)
    | (ENCRYPTED | UNENCRYPTED) PASSWORD StringConstant
    | INHERIT
    | CONNECTION LIMIT MINUS? Integral
    | VALID UNTIL StringConstant
    | USER role_name_list
    | identifier
    ;

cloudberry_role_option
    : resource_keyword resource_kind (role_name | NONE)
    | create_external_permission role_attributes?
    | deny_keyword (BETWEEN deny_point AND deny_point | deny_point)
    | DROP deny_keyword FOR deny_point
    ;

create_external_permission
    : {keyword("CREATEEXTTABLE")}? Identifier
    | {keyword("NOCREATEEXTTABLE")}? Identifier
    ;

role_attributes
    : OPEN_PAREN role_attribute (COMMA role_attribute)* CLOSE_PAREN
    ;

role_attribute
    : (TYPE_P | protocol_keyword) EQUAL StringConstant
    ;

deny_keyword
    : {keyword("DENY")}? Identifier
    ;

deny_point
    : DAY_P (StringConstant | Integral) (TIME StringConstant)?
    ;

role_keyword
    : ROLE
    | USER
    ;

role_name_list
    : role_name (COMMA role_name)*
    ;

role_name
    : identifier
    | CURRENT_USER
    | SESSION_USER
    ;

external_stmt
    : CREATE external_mode? EXTERNAL web_keyword? (TEMP | TEMPORARY)? TABLE qualified_name
        external_columns external_source
        FORMAT StringConstant format_options? (ENCODING StringConstant)? distribution_clause?
    | ALTER EXTERNAL TABLE qualified_name ADD_P COLUMN? external_column
    | DROP EXTERNAL TABLE (IF_P EXISTS)? qualified_name
    ;

external_mode
    : {keyword("READABLE")}? Identifier
    | {keyword("WRITABLE")}? Identifier
    ;

web_keyword
    : {keyword("WEB")}? Identifier
    ;

external_source
    : LOCATION OPEN_PAREN StringConstant (COMMA StringConstant)* CLOSE_PAREN
        (ON {keyword("COORDINATOR")}? Identifier)?
    | EXECUTE StringConstant (ON (ALL | {keyword("COORDINATOR")}? Identifier))?
    ;

external_columns
    : OPEN_PAREN (LIKE qualified_name | column_token+) CLOSE_PAREN
    ;

external_column
    : column_token+
    ;

column_token
    : OPEN_PAREN column_token* CLOSE_PAREN
    | ~(OPEN_PAREN | CLOSE_PAREN | SEMI | LIKE)
    ;

format_options
    : OPEN_PAREN format_option (COMMA? format_option)* CLOSE_PAREN
    ;

format_option
    : HEADER_P
    | DELIMITER AS? StringConstant
    | NULL_P AS? StringConstant
    | QUOTE AS? StringConstant
    | ESCAPE AS? StringConstant
    ;

distribution_clause
    : {keyword("DISTRIBUTED")}? Identifier
        (BY OPEN_PAREN identifier (COMMA identifier)* CLOSE_PAREN
        | {keyword("RANDOMLY")}? Identifier
        | {keyword("REPLICATED")}? Identifier)
    ;

resource_stmt
    : CREATE resource_keyword resource_kind identifier WITH OPEN_PAREN resource_option (COMMA resource_option)* CLOSE_PAREN
    | ALTER resource_keyword queue_keyword identifier WITH OPEN_PAREN resource_option (COMMA resource_option)* CLOSE_PAREN
    | ALTER resource_keyword GROUP_P identifier SET identifier resource_value
    | DROP resource_keyword resource_kind identifier
    ;

resource_keyword
    : {keyword("RESOURCE")}? Identifier
    ;

resource_kind
    : queue_keyword
    | GROUP_P
    ;

queue_keyword
    : {keyword("QUEUE")}? Identifier
    ;

resource_option
    : identifier EQUAL resource_value
    ;

resource_value
    : Integral
    | Numeric
    | StringConstant
    | TRUE_P
    | FALSE_P
    | identifier
    ;

retrieve_stmt
    : {keyword("RETRIEVE")}? Identifier (ALL | Integral) FROM endpoint_keyword identifier
    ;

endpoint_keyword
    : {keyword("ENDPOINT")}? Identifier
    ;

protocol_keyword
    : {keyword("PROTOCOL")}? Identifier
    ;

qualified_name
    : identifier (DOT identifier)*
    ;

identifier
    : Identifier
    | QuotedIdentifier
    | LABEL
    ;
