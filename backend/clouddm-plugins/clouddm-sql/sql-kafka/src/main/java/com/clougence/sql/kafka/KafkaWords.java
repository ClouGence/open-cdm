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
package com.clougence.sql.kafka;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class KafkaWords {
    private static final Pattern PARTS         = Pattern.compile("'([^']*)'?|\"((?:\\\\[\\s\\S]|[^\"\\\\])*)\"?|\\\\(\\r?\\n|[\\s\\S])|[^'\"\\\\]+|\\\\$");
    private static final Pattern DOUBLE_ESCAPE = Pattern.compile("\\\\(\\r?\\n|[\"\\\\$`])");

    private KafkaWords(){
    }

    // ANTLR determines word boundaries and quote validity. This only decodes
    // token contents; unfinished tokens are also decoded for editor completion.
    public static String decode(String word) {
        StringBuilder result = new StringBuilder();
        Matcher parts = PARTS.matcher(word);
        while (parts.find()) {
            if (parts.group(1) != null) {
                result.append(parts.group(1));
            } else if (parts.group(2) != null) {
                Matcher escapes = DOUBLE_ESCAPE.matcher(parts.group(2));
                result.append(escapes.replaceAll(match -> {
                    if (match.group(1).endsWith("\n")) {
                        return "";
                    }
                    return Matcher.quoteReplacement(match.group(1));
                }));
            } else if (parts.group(3) != null) {
                if (!parts.group(3).endsWith("\n")) {
                    result.append(parts.group(3));
                }
            } else if (!parts.group().equals("\\")) {
                result.append(parts.group());
            }
        }
        return result.toString();
    }
}
