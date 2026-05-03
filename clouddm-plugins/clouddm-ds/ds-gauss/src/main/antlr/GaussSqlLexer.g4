lexer grammar GaussSqlLexer;

import PgSqlLexer;

@header {
    import com.clougence.clouddm.ds.gauss.parser.base.GaussSqlLexerBase;
}

options {
    superClass = GaussSqlLexerBase;
}


// for gs
LESS:  L E S S;
THAN:  T H A N;
EVERY:  E V E R Y;
MINUS_ : M I N U S;
SHIPPABLE: S H I P P A B L E;
FENCED: F E N C E D;


