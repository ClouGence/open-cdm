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
package com.clougence.sql.es;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;

public class EsRewriteSpi implements RewriteSpi {
    private final EsRequestParser parser;

    public EsRewriteSpi(SqlParserParameters parameters){
        parser = new EsRequestParser(parameters);
    }

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        long maximum = context.getFetchLimit();
        if (maximum <= 0) {
            return queryStr;
        }
        EsRequest request = parser.parse(queryStr);
        if (EsOperation.resolve(request) != EsOperation.SEARCH) {
            return queryStr;
        }
        EsRequestPolicy.analyze(request);
        EsSearchLimit limit = EsSearchLimit.resolve(request);
        if (limit.range() != null && limit.size() <= maximum) {
            return queryStr;
        }
        int start;
        int end;
        String replacement = Long.toString(maximum);
        if (limit.range() != null) {
            start = limit.range().start();
            end = limit.range().end();
            if (limit.parameter()) {
                replacement = "size=" + replacement;
            }
        } else if (request.body() != null) {
            start = request.body().range().end() - 1;
            end = start;
            replacement = "\"size\":" + replacement;
            if (!request.body().members().isEmpty()) {
                replacement = "," + replacement;
            }
        } else {
            start = request.range().end();
            end = start;
            replacement = "\n{\"size\":" + replacement + "}";
        }
        // AST offsets count Unicode code points. Edit only the size span to retain JSON number precision and original text.
        return queryStr.substring(0, queryStr.offsetByCodePoints(0, start)) + replacement + queryStr.substring(queryStr.offsetByCodePoints(0, end));
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        return null;
    }
}
