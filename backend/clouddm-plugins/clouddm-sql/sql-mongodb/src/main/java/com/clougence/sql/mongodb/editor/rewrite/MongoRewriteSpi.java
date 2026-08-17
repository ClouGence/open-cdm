/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.mongodb.editor.rewrite;

import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.mongodb.parser.MongoDslProvider;
import com.clougence.sql.mongodb.parser.antlr.MongoParser;
import com.clougence.sql.mongodb.parser.ast.MongoConstant;

public class MongoRewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(MongoDslProvider.INSTANCE, new StringReader(queryStr));
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            MongoParser.CommandContext command = (MongoParser.CommandContext) astTree;
            ParseTree child = command.functionCommand().collectionFunction().collectionMethod().getChild(0);
            if (child instanceof MongoParser.FindContext) {
                return findRewrite((MongoParser.FindContext) child, maxLimit, rewriter);
            } else if (child instanceof MongoParser.AggregateContext) {
                return aggregateRewrite((MongoParser.AggregateContext) child, maxLimit, rewriter);
            }
        }

        return rewriter.getText();
    }

    private String aggregateRewrite(MongoParser.AggregateContext agg, long maxLimit, TokenStreamRewriter rewriter) {
        if (agg.arr() == null) {
            rewriter.insertBefore(agg.RS_BRACKET().getSymbol(), "[{$limit:" + maxLimit + "}]");
        } else {
            if (agg.arr().value().isEmpty()) {
                rewriter.insertBefore(agg.arr().RM_BRACKET().getSymbol(), "{$limit:" + maxLimit + "}");
            } else {
                rewriter.insertBefore(agg.arr().RM_BRACKET().getSymbol(), ",{$limit:" + maxLimit + "}");
            }
        }

        return rewriter.getText();
    }

    private String findRewrite(MongoParser.FindContext child, long maxLimit, TokenStreamRewriter rewriter) {
        MongoParser.FindContext find = child;
        for (int i = find.findConstarint().size() - 1; i >= 0; i--) {
            if (find.findConstarint(i) instanceof MongoParser.FindLimitConstraintContext limit) {
                if (parserLimit(limit.NUMBER().getText()) > maxLimit) {
                    rewriter.replace(limit.NUMBER().getSymbol().getTokenIndex(), maxLimit);
                }
                return rewriter.getText();
            }
        }

        if (find.options != null) {
            for (int i = find.options.pair().size() - 1; i >= 0; i--) {
                if (MongoConstant.LIMIT.equals(getString(find.options.pair(i).key().getText()))) {
                    if (parserLimit(getString(find.options.pair(i).value().getText())) > maxLimit) {
                        rewriter.replace(find.options.pair(i).value().NUMBER().getSymbol().getTokenIndex(), maxLimit);
                    }
                    return rewriter.getText();
                }
            }
        }

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

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        return null;
    }
}
