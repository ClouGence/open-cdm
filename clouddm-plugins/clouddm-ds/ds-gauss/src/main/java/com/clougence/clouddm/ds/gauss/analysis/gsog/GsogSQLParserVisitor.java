package com.clougence.clouddm.ds.gauss.analysis.gsog;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;

public class GsogSQLParserVisitor extends GsogAbstractSQLParserVisitor {

    public GsogSQLParserVisitor(PgBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

}
