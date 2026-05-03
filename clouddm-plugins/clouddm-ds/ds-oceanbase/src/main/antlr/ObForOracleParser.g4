parser grammar ObForOracleParser;

import PlSqlParser;

@header {
    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;
}

options {
    tokenVocab=ObForOracleLexer;
    superClass = PlSqlParserBase;
}


physical_attributes_clause
    : (
        PCTFREE pctfree = UNSIGNED_INTEGER
        | PCTUSED pctused = UNSIGNED_INTEGER
        | INITRANS inittrans = UNSIGNED_INTEGER
        | MAXTRANS maxtrans = UNSIGNED_INTEGER
        | COMPUTE STATISTICS
        | storage_clause
        | compute_clauses

//
        | COMPRESS FOR ARCHIVE
        | REPLICA_NUM '=' UNSIGNED_INTEGER
        | BLOCK_SIZE '=' UNSIGNED_INTEGER
        | USE_BLOOM_FILTER '=' (TRUE | FALSE)
        | TABLET_SIZE '=' UNSIGNED_INTEGER
        | PCTFREE '=' UNSIGNED_INTEGER
    )+
    ;
