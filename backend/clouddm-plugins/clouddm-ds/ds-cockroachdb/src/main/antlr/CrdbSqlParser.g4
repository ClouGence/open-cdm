parser grammar CrdbSqlParser;
options {
    tokenVocab = PgSqlLexer;
    superClass = CrdbSqlParserBase;
}
@header {
import com.clougence.clouddm.ds.cockroachdb.sql.parser.CrdbSqlParserBase;
}
root : statement SEMI? EOF;
statement
    : privilege_stmt
    | default_privileges_stmt
    | role_stmt
    | database_stmt
    | SHOW (COLUMNS | CONSTRAINTS | INDEX | INDEXES) FROM table_name=qualified_name (WITH COMMENT)?
    | SHOW CREATE (TABLE | VIEW | SEQUENCE) table_name=qualified_name
    | SHOW CREATE DATABASE catalog_name=qualified_name {$catalog_name.ctx.identifier().size() == 1}?
    | SHOW TABLES (FROM schema_name=qualified_name {$schema_name.ctx.identifier().size() <= 2}?)? (WITH COMMENT)?
    | SHOW (SCHEMAS | SEQUENCES | TYPES_P) (FROM catalog_name=qualified_name {$catalog_name.ctx.identifier().size() == 1}?)?
    | SHOW list_keyword (WITH COMMENT {"DATABASES".equalsIgnoreCase($list_keyword.text)}?)?
    ;
qualified_name : identifier ({_input.LA(2) != STAR}? DOT identifier)? ({_input.LA(2) != STAR}? DOT identifier)?;
identifier : {identifierToken()}? .;
list_keyword : word=Identifier {java.util.Set.of("DATABASES", "ROLES", "USERS", "ENUMS").contains($word.text.toUpperCase(java.util.Locale.ROOT))}?;

privilege_stmt
    : GRANT (SYSTEM_P privileges | privileges ON privilege_target) TO grantees=role_list (WITH GRANT OPTION)?
    | GRANT membership=role_list TO grantees=role_list (WITH ADMIN OPTION)?
    | REVOKE grant_option? (SYSTEM_P privileges | privileges ON privilege_target) FROM grantees=role_list
        {$grant_option.ctx == null || $privilege_target.ctx == null || $privilege_target.ctx.ALL() == null || $privilege_target.ctx.SEQUENCES() == null}?
    | REVOKE (ADMIN OPTION FOR)? membership=role_list FROM grantees=role_list
    ;

grant_option : GRANT OPTION FOR;

privileges
    : ALL PRIVILEGES?
    | privilege (COMMA privilege)*
    ;

privilege
    : identifier | CREATE | GRANT | SELECT | INSERT | UPDATE | DELETE_P | DROP | TRIGGER | EXECUTE | REFERENCES | TRUNCATE
    ;

privilege_target
    : (TABLE | SEQUENCE)? table_pattern (COMMA table_pattern)*
    | DATABASE database_names=identifier_list
    | SCHEMA schemas=schema_list
    | TYPE_P type_names=name_list
    | EXTERNAL CONNECTION connections=identifier_list
    | (FUNCTION | PROCEDURE) routine_target (COMMA routine_target)*
    | ALL (TABLES | SEQUENCES | FUNCTIONS | PROCEDURES) IN_P SCHEMA schemas=schema_list
    ;

table_pattern
    : qualified_name (DOT STAR {$qualified_name.ctx.identifier().size() <= 2}?)?
    | STAR
    ;

routine_target
    : qualified_name (OPEN_PAREN (routine_arg (COMMA routine_arg)*)? CLOSE_PAREN)?
    ;

routine_arg
    : (IN_P | OUT_P | INOUT | VARIADIC)? identifier? type_name
    ;

type_name
    : (qualified_name | INT_P | INTEGER | BIGINT | SMALLINT | BOOLEAN_P | TEXT_P | VARCHAR | CHAR_P
        | NUMERIC | DECIMAL_P | REAL | DOUBLE_P PRECISION | TIME | TIMESTAMP | INTERVAL)
        (OPEN_PAREN Integral (COMMA Integral)? CLOSE_PAREN)? (ARRAY | OPEN_BRACKET CLOSE_BRACKET)*
    ;

name_list : qualified_name (COMMA qualified_name)*;
identifier_list : identifier (COMMA identifier)*;
schema_list : schema_ref (COMMA schema_ref)*;
schema_ref : identifier (DOT identifier)?;
role_list : role_name (COMMA role_name)*;
role_name : identifier | CURRENT_USER | SESSION_USER;

default_privileges_stmt
    : ALTER DEFAULT PRIVILEGES (FOR ((ROLE | USER | GROUP_P) creators=role_list | ALL roles_keyword))?
        (IN_P SCHEMA schemas=schema_list)?
        (GRANT privileges ON default_target TO grantees=role_list (WITH GRANT OPTION)?
        | REVOKE (GRANT OPTION FOR)? privileges ON default_target FROM grantees=role_list (CASCADE | RESTRICT)?)
    ;

roles_keyword : {keyword("ROLES")}? Identifier;
default_target : TABLES | SEQUENCES | TYPES_P | SCHEMAS | FUNCTIONS;

role_stmt
    : CREATE role_kind (IF_P NOT EXISTS)? principal=role_name (WITH? role_option+)?
    | ALTER role_kind (ALL role_setting | (IF_P EXISTS)? principal=role_name (WITH? role_option+ | role_setting)?)
    | DROP role_kind (IF_P EXISTS)? roles=role_list
    ;

role_kind : ROLE | USER | GROUP_P;
role_option
    : {roleFlag()}? Identifier
    | ENCRYPTED? PASSWORD (StringConstant | NULL_P)
    | VALID UNTIL (StringConstant | NULL_P)
    | {keyword("SUBJECT")}? Identifier (StringConstant | NULL_P)
    ;

role_setting
    : (IN_P DATABASE identifier)? (SET qualified_name (TO | EQUAL) setting_value (COMMA setting_value)*
        | RESET (qualified_name | ALL))
    ;
setting_value : identifier | StringConstant | Integral | TRUE_P | FALSE_P | DEFAULT | ON | OFF | MINUS Integral;

database_stmt
    : CREATE DATABASE IF_P NOT EXISTS database_name=identifier
    | DROP DATABASE (IF_P EXISTS)? database_name=identifier (CASCADE | RESTRICT)?
    ;
