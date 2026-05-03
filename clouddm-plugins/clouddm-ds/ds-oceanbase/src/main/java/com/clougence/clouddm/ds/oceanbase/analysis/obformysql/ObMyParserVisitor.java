package com.clougence.clouddm.ds.oceanbase.analysis.obformysql;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlParser;

public class ObMyParserVisitor extends AbstractObSQLParserVisitor {

    public ObMyParserVisitor(MyBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

    @Override
    public Void visitTableOptionCompression(ObForMySqlParser.TableOptionCompressionContext ctx) {
        return null;
    }

    @Override
    public Void visitTableOptionRowFormat(ObForMySqlParser.TableOptionRowFormatContext ctx) {
        return null;
    }

    @Override
    public Void visitObMyTableOption(ObForMySqlParser.ObMyTableOptionContext ctx) {
        return null;
    }

    @Override
    public Void visitSelectIntoTextFile(ObForMySqlParser.SelectIntoTextFileContext ctx) {
        return null;
    }


}
