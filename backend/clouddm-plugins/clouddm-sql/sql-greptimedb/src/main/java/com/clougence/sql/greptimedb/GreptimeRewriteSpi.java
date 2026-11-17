/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clougence.sql.greptimedb;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;

import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser;

final class GreptimeRewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        long maxLimit = context.getFetchLimit();
        if (maxLimit <= 0) {
            return queryStr;
        }

        List<AstSplitScript> scripts = DslHelper.splitDsl(GreptimeDslProvider.INSTANCE, new StringReader(queryStr));
        if (scripts.size() != 1) {
            return queryStr;
        }
        AstSplitScript script = scripts.get(0);
        var query = ((GreptimeSqlParser.StatementContext) script.getAstTree()).query();
        if (query == null) {
            return queryStr;
        }

        var limit = query.rowLimit();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(script.getParser().getTokenStream());
        if (limit == null || limit.count == null) {
            rewriter.insertAfter(query.getStop(), " LIMIT " + maxLimit);
        } else {
            Token count = limit.count;
            String value = count.getText();
            if (count.getType() != GreptimeSqlParser.ALL
                && (!value.matches("[0-9]+") || new BigInteger(value).compareTo(BigInteger.valueOf(maxLimit)) <= 0)) {
                return queryStr;
            }
            rewriter.replace(count, Long.toString(maxLimit));
        }
        return rewriter.getText();
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        return null;
    }
}
