parser grammar GaussSqlParser;

import PgSqlParser;

@header {
    import com.clougence.clouddm.ds.gauss.parser.base.GaussSqlParserBase;
}

options {
    tokenVocab = GaussSqlLexer;
    superClass = GaussSqlParserBase;
}




createfunc_opt_item
    : AS func_as
    | LANGUAGE nonreservedword_or_sconst
    | TRANSFORM transform_type_list
    | WINDOW
    | common_func_opt_item
    | AS DECLARE? func_body
    | NOT? FENCED
    | NOT? SHIPPABLE
    ;

partitionspec
    : PARTITION BY colid OPEN_PAREN part_params CLOSE_PAREN
    | PARTITION BY colid OPEN_PAREN part_params CLOSE_PAREN (OPEN_PAREN (partition_item | partition_item ',')+  CLOSE_PAREN)
    ;

union_context
    : (UNION | EXCEPT | INTERSECT)
    | ( UNION | EXCEPT | INTERSECT | MINUS_)
    ;


