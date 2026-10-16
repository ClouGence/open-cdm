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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KafkaCommand implements Statement {
    private final KafkaCommandType          type;
    private final Map<String, List<String>> options;

    @Override
    public void accept(Visitor visitor) {
        // The common Visitor is a marker; ANTLR visitors handle command traversal.
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write(type.getCommand());
        for (Map.Entry<String, List<String>> option : options.entrySet()) {
            if (option.getValue().isEmpty()) {
                writer.write(" " + option.getKey());
            }
            for (String value : option.getValue()) {
                writer.write(" " + option.getKey() + " '" + value.replace("'", "'\\''") + "'");
            }
        }
    }

    public boolean has(String option) {
        return options.containsKey(option);
    }

    public String value(String option) {
        List<String> values = options.get(option);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    public SplitQueryType queryType() {
        if (has("--help")) {
            return SplitQueryType.METADATA;
        }
        if (type == KafkaCommandType.CONSUMER) {
            return SplitQueryType.LOG_READ;
        }
        if (has("--create")) {
            return SplitQueryType.CREATE_PUB_SUB;
        }
        if (has("--delete")) {
            return SplitQueryType.DROP_PUB_SUB;
        }
        return SplitQueryType.METADATA;
    }
}
