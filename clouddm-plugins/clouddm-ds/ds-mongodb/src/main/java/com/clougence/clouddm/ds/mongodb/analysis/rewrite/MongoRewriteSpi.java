package com.clougence.clouddm.ds.mongodb.analysis.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.mongodb.i18n.MongoDsI18nKeys;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.ds.mongodb.parser.MongoDslProvider;
import com.clougence.clouddm.ds.mongodb.parser.antlr.MongoParser;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoConstant;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class MongoRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(MongoDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            MongoParser.CommandContext command = (MongoParser.CommandContext) astTree;
            ParseTree child = command.functionCommand().collectionFunction().collectionMethod().getChild(0);
            if (child instanceof MongoParser.FindContext) {
                return findRewrite(context, (MongoParser.FindContext) child, maxLimit, rewriter);
            } else if (child instanceof MongoParser.AggregateContext) {
                return aggregateRewrite(context, (MongoParser.AggregateContext) child, maxLimit, rewriter);
            }
        }

        return rewriter.getText();
    }

    private String aggregateRewrite(RewriteContext context, MongoParser.AggregateContext agg, long maxLimit, TokenStreamRewriter rewriter) {
        if (agg.arr() == null) {
            rewriter.insertBefore(agg.RS_BRACKET().getSymbol(), "[{$limit:" + maxLimit + "}]");
        } else {
            if (agg.arr().value().isEmpty()) {
                rewriter.insertBefore(agg.arr().RM_BRACKET().getSymbol(), "{$limit:" + maxLimit + "}");
            } else {
                rewriter.insertBefore(agg.arr().RM_BRACKET().getSymbol(), ",{$limit:" + maxLimit + "}");
            }
        }

        context.addRewriterInfo(MongoDsI18nKeys.REWRITE_LIMIT_LABEL);
        return rewriter.getText();
    }

    private String findRewrite(RewriteContext context, MongoParser.FindContext child, long maxLimit, TokenStreamRewriter rewriter) {
        MongoParser.FindContext find = child;
        for (int i = find.findConstarint().size() - 1; i >= 0; i--) {
            if (find.findConstarint(i) instanceof MongoParser.FindLimitConstraintContext) {
                MongoParser.FindLimitConstraintContext limit = (MongoParser.FindLimitConstraintContext) find.findConstarint(i);
                if (parserLimit(limit.NUMBER().getText()) > maxLimit) {
                    context.addRewriterInfo(MongoDsI18nKeys.REWRITE_LIMIT_LABEL);
                    rewriter.replace(limit.NUMBER().getSymbol().getTokenIndex(), maxLimit);
                }
                return rewriter.getText();
            }
        }

        if (find.options != null) {
            for (int i = find.options.pair().size() - 1; i >= 0; i--) {
                if (MongoConstant.LIMIT.equals(getString(find.options.pair(i).key().getText()))) {
                    if (parserLimit(getString(find.options.pair(i).value().getText())) > maxLimit) {
                        context.addRewriterInfo(MongoDsI18nKeys.REWRITE_LIMIT_LABEL);
                        rewriter.replace(find.options.pair(i).value().NUMBER().getSymbol().getTokenIndex(), maxLimit);
                    }
                    return rewriter.getText();
                }
            }
        }

        context.addRewriterInfo(MongoDsI18nKeys.REWRITE_LIMIT_LABEL);
        return rewriter.getText() + ".limit(" + maxLimit + ")";
    }

    private long parserLimit(String text) {
        try {
            return Long.parseLong(text);
        } catch (Exception e) {
            throw new UnsupportedOperationException("limit must be number");
        }
    }

    private String getString(String text) {
        if (text.startsWith("'") || text.startsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
