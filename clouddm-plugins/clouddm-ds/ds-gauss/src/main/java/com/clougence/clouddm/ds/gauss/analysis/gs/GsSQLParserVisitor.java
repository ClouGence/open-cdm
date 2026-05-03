package com.clougence.clouddm.ds.gauss.analysis.gs;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;

public class GsSQLParserVisitor extends GsAbstractSQLParserVisitor {

    public GsSQLParserVisitor(PgBuilderFactory builder, Parser parser){
        super(builder, parser);
    }
}
