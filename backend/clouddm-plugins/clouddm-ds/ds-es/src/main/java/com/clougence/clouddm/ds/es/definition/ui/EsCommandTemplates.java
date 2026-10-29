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
package com.clougence.clouddm.ds.es.definition.ui;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.clougence.sql.es.EsIndexReference;
import com.clougence.sql.es.EsSourceRange;

public final class EsCommandTemplates {
    private EsCommandTemplates(){
    }

    public static String indexPath(String name) {
        new EsIndexReference(name, new EsSourceRange(0, name.length(), 1, 0, 1, name.length()));
        return "/" + URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static String quickQuery(String name, int limit) {
        return "GET " + indexPath(name) + "/_search\n{\n  \"size\": " + limit + ",\n  \"query\": {\"match_all\": {}}\n}";
    }

    public static List<String> commands(String name) {
        String path = indexPath(name);
        return List.of(quickQuery(name, 20), "GET " + path + "/_count", "GET " + path + "/_mapping", "GET " + path + "/_settings", "GET " + path + "/_alias");
    }
}
