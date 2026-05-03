lexer grammar ObForOracleLexer;

import PlSqlLexer;

@header {
    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlLexerBase;
}


options {
    superClass = PlSqlLexerBase;
}


REPLICA_NUM: R E P L I C A '_' N U M;
BLOCK_SIZE: B L O C K '_' S I Z E;
USE_BLOOM_FILTER: U S E '_' B L O O M '_' F I L T E R;
TABLET_SIZE: T A B L E T '_' S I Z E;