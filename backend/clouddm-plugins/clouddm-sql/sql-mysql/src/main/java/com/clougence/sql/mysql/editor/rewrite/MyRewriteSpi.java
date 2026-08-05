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
package com.clougence.sql.mysql.editor.rewrite;

import java.io.Reader;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.common.analysis.SqlAnalysisI18nKeys;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.antlr.MySqlParser;

public class MyRewriteSpi implements RewriteSpi {

    private final MyDslProvider provider;

    public MyRewriteSpi(MySqlParserConfig config){
        this.provider = new MyDslProvider(config);
    }

    protected MyDslProvider dslProvider() {
        return this.provider;
    }

    @Override
    public Stream<String> rewriterQueryStream(Reader queryReader, QueryRequest request, RewriteContext context) {
        return Stream.of(rewriterQueryMaterialized(queryReader, request, context));
    }

    private String rewriterQueryMaterialized(Reader queryReader, QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), queryReader);
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(SqlAnalysisI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        MySqlParser.DmlStatementContext dmlStat = ((MySqlParser.SqlStatementContext) astTree).dmlStatement();
        if (dmlStat.selectStatement() != null) {
            MySqlParser.SelectStatementContext s = dmlStat.selectStatement();
            if (s instanceof MySqlParser.QuerySpecificationSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (MySqlParser.QuerySpecificationSelectContext) s);
            } else {
                // TODO: other select type
            }
        } else if (dmlStat.withSelectStatement() != null) {
            MySqlParser.SelectStatementContext s = dmlStat.withSelectStatement().selectStatement();
            if (s instanceof MySqlParser.QuerySpecificationSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (MySqlParser.QuerySpecificationSelectContext) s);
            } else {
                // TODO: other select type
            }
        }
        return false;
    }

    private static boolean rewriterLimit(TokenStreamRewriter rewriter, long maxLimit, MySqlParser.QuerySpecificationSelectContext s) {
        if (!s.querySpecificationSelectTail().unionStatement().isEmpty()) {
            return false;
        }

        MySqlParser.QuerySpecificationContext querySpec = s.querySpecification();
        if (querySpec.fromClause() == null) {
            return false;
        }

        if (querySpec.limitClause() != null) {
            MySqlParser.LimitClauseContext limitClause = querySpec.limitClause();
            MySqlParser.UnsignedDecimalIntegerLiteralContext integerLiteralCtx = limitClause.limit.unsignedDecimalIntegerLiteral();
            if (integerLiteralCtx == null) {
                return false;
            }

            BigInteger sqlLimit = new BigInteger(integerLiteralCtx.getText());
            if (sqlLimit.compareTo(BigInteger.valueOf(maxLimit)) > 0) {
                rewriter.replace(integerLiteralCtx.getStart(), integerLiteralCtx.getStop(), maxLimit);
                return true;
            } else {
                return false;
            }
        } else {
            rewriter.insertAfter(querySpec.getStop(), " LIMIT " + maxLimit);
            return true;
        }
    }
}
