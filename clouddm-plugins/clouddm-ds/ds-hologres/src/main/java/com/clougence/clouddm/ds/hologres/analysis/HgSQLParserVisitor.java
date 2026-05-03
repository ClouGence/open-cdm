package com.clougence.clouddm.ds.hologres.analysis;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSQLParserVisitor;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;

public class HgSQLParserVisitor extends PgSQLParserVisitor {

    public HgSQLParserVisitor(PgBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

}
