package com.clougence.clouddm.ds.ads.analysis.ads4my;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.ads.parser.ads4my.antlr.AdsMyParser;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class AdsMyParserVisitor extends AbstractAdsMyParserVisitor {

    public AdsMyParserVisitor(MyBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

    @Override
    public Void visitSelectIntoTextFile(AdsMyParser.SelectIntoTextFileContext ctx) {
        return null;
    }

    @Override
    public Void visitAdbTableOption(AdsMyParser.AdbTableOptionContext ctx) {
        return null;
    }

    @Override
    public Void visitTableOptionCompression(AdsMyParser.TableOptionCompressionContext ctx) {
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AdsMyParser.AnalyzeTableContext ctx) {
        builder.handleResource(() -> {
            ctx.tableName().accept(this);
        }, SecQueryType.ANALYZE, SecQueryKind.OTHER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitLoadDataStatement(AdsMyParser.LoadDataStatementContext ctx) {
        builder.handleResource(() -> {
            ctx.tableName().accept(this);
        }, SecQueryType.LOAD, SecQueryKind.OTHER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitAdbExternalTable(AdsMyParser.AdbExternalTableContext ctx) {
        builder.handleResource(() -> {
            ctx.tableName().accept(this);
        }, SecQueryType.CREATE_OBJECT, SecQueryKind.CREATE, true, TargetType.Unknown);
        return null;
    }
}
