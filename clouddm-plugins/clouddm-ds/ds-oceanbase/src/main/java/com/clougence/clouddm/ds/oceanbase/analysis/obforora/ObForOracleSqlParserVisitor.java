package com.clougence.clouddm.ds.oceanbase.analysis.obforora;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.oracle.analysis.builder.OraBuilderFactory;

public class ObForOracleSqlParserVisitor extends AbstractObForOraSQLParserVisitor {

    public ObForOracleSqlParserVisitor(OraBuilderFactory builder, Parser parser){
        super(builder, parser);
    }
}
