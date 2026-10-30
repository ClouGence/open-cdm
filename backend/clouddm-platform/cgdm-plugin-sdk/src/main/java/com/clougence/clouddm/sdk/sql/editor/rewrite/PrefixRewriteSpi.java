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
package com.clougence.clouddm.sdk.sql.editor.rewrite;

public class PrefixRewriteSpi implements RewriteSpi {

    private final String prefix;

    public PrefixRewriteSpi(String prefix){
        this.prefix = prefix;
    }

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        return withPrefix(queryStr, this.prefix);
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        return withPrefix(queryStr, this.prefix);
    }

    public static String withPrefix(String queryBody, String prefix) {
        int position = queryBody.length() - queryBody.stripLeading().length();
        return new StringBuilder(queryBody).insert(position, prefix).toString();
    }
}
