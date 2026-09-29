parser grammar CrdbSqlParser;
options { tokenVocab = PgSqlLexer; }
root : statement SEMI? EOF;
statement
    : SHOW (COLUMNS | CONSTRAINTS | INDEX | INDEXES) FROM table_name=qualified_name (WITH COMMENT)?
    | SHOW CREATE (TABLE | VIEW | SEQUENCE) table_name=qualified_name
    | SHOW CREATE DATABASE catalog_name=qualified_name {$catalog_name.ctx.identifier().size() == 1}?
    | SHOW TABLES (FROM schema_name=qualified_name {$schema_name.ctx.identifier().size() <= 2}?)? (WITH COMMENT)?
    | SHOW (SCHEMAS | SEQUENCES | TYPES_P) (FROM catalog_name=qualified_name {$catalog_name.ctx.identifier().size() == 1}?)?
    | SHOW list_keyword (WITH COMMENT {"DATABASES".equalsIgnoreCase($list_keyword.text)}?)?
    ;
qualified_name : identifier (DOT identifier)? (DOT identifier)?;
identifier : Identifier | QuotedIdentifier;
list_keyword : word=Identifier {java.util.Set.of("DATABASES", "ROLES", "USERS", "ENUMS").contains($word.text.toUpperCase(java.util.Locale.ROOT))}?;
