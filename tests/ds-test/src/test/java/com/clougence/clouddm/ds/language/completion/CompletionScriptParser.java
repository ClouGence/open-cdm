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
package com.clougence.clouddm.ds.language.completion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;

final class CompletionScriptParser {

    private CompletionScriptParser(){
    }

    static List<CompletionScriptCase> parse(String resourcePath) {
        List<CompletionScriptCase> cases = new ArrayList<>();
        for (CaseBlock block : TextCaseSupport.loadBlocks(resourcePath)) {
            cases.add(parseBlock(block));
        }
        return cases;
    }

    private static CompletionScriptCase parseBlock(CaseBlock block) {
        List<String> lines = block.body().lines().toList();
        Map<String, String> fields = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.endsWith(":")) {
                String key = line.substring(0, line.length() - 1);
                StringBuilder value = new StringBuilder();
                i++;
                while (i < lines.size() && !isInlineField(lines.get(i))) {
                    value.append(lines.get(i)).append('\n');
                    i++;
                }
                i--;
                fields.put(key, value.toString().stripTrailing());
                continue;
            }
            int split = line.indexOf(':');
            if (split > 0) {
                fields.put(line.substring(0, split).trim(), line.substring(split + 1).trim());
            }
        }

        String sql = required(fields, "sql");
        CursorSql cursorSql = resolveCursor(sql, fields);
        return new CompletionScriptCase(block.resourcePath(),
            block.name(),
            required(fields, "languageClass"),
            fields.getOrDefault("meta", "_meta"),
            Long.parseLong(fields.getOrDefault("dataSourceId", "1")),
            fields.get("catalog"),
            fields.getOrDefault("schema", fields.get("database")),
            cursorSql.sqlText(),
            cursorSql.line(),
            cursorSql.column(),
            required(fields, "expect"));
    }

    private static boolean isInlineField(String line) {
        int split = line.indexOf(':');
        return split > 0 && !line.substring(0, split).contains(" ");
    }

    private static String required(Map<String, String> fields, String key) {
        String value = fields.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing field: " + key);
        }
        return value;
    }

    private static CursorSql resolveCursor(String sql, Map<String, String> fields) {
        int marker = sql.indexOf('|');
        if (marker < 0) {
            return new CursorSql(sql, Integer.parseInt(required(fields, "cursorLineNumber")), Integer.parseInt(required(fields, "cursorColNumber")));
        }
        String before = sql.substring(0, marker);
        int line = 1;
        int column = 0;
        for (int i = 0; i < before.length(); i++) {
            if (before.charAt(i) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return new CursorSql(sql.substring(0, marker) + sql.substring(marker + 1), line, column);
    }

    private record CursorSql(String sqlText, int line, int column) {
    }
}
