/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clougence.sql.greptimedb;

import static com.clougence.sql.greptimedb.GreptimeDslProvider.identifier;
import static com.clougence.sql.greptimedb.antlr.GreptimeSqlLexer.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.sql.greptimedb.antlr.GreptimeSqlBaseListener;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser.CreateTableContext;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser.DataTypeContext;

final class GreptimeCreateTableValidator extends GreptimeSqlBaseListener {

    private static final Set<String> TIMESTAMP_ALIASES = Set.of("TIMESTAMP_S", "TIMESTAMP_SEC", "TIMESTAMPSECOND", "TIMESTAMP_MS",
        "TIMESTAMPMILLISECOND", "TIMESTAMP_US", "TIMESTAMPMICROSECOND", "TIMESTAMP_NS", "TIMESTAMPNANOSECOND");

    private final GreptimeSqlParser parser;

    GreptimeCreateTableValidator(GreptimeSqlParser parser) {
        this.parser = parser;
    }

    @Override
    public void exitCreateTable(CreateTableContext context) {
        var definition = context.tableDefinition();
        if (definition == null || parser.getNumberOfSyntaxErrors() != 0) {
            return;
        }

        Map<String, DataTypeContext> columns = new HashMap<>();
        ParserRuleContext indexName = null;
        char indexQuote = 0;
        for (var element : definition.tableElement()) {
            ParserRuleContext name = element.identifier();
            char quote = 0;
            var column = element.columnDefinition();
            if (column != null) {
                var columnName = element.wordIdentifier();
                columns.putIfAbsent(identifier(columnName), column.dataType());
                long indexes = column.columnOption().stream().filter(option -> option.TIME() != null).count();
                if (indexes > 1) {
                    invalidTimeIndex(columnName, "Duplicate TIME INDEX on column");
                    return;
                }
                if (indexes == 1) {
                    if (column.columnOption().stream().anyMatch(option -> option.NULL() != null && option.NOT() == null)) {
                        invalidTimeIndex(columnName, "TIME INDEX column cannot be NULL");
                        return;
                    }
                    // GreptimeDB stores an inline TIME INDEX as an unquoted column reference.
                    name = columnName;
                }
            } else if (name != null) {
                var token = name.getStart();
                if (token.getType() == QUOTED_IDENTIFIER || token.getType() == STRING_LITERAL || token.getType() == DOUBLE_QUOTED_STRING) {
                    quote = token.getText().charAt(0);
                }
            }
            if (name != null) {
                if (indexName != null && (indexQuote != quote || !identifier(indexName).equals(identifier(name)))) {
                    invalidTimeIndex(name, "Expected only one TIME INDEX");
                    return;
                }
                indexName = name;
                indexQuote = quote;
            }
        }

        if (indexName == null) {
            invalidTimeIndex(definition, "Missing TIME INDEX");
            return;
        }
        var type = columns.get(identifier(indexName));
        if (type == null) {
            invalidTimeIndex(indexName, "TIME INDEX column is not defined");
        } else if (!isTimestamp(type)) {
            invalidTimeIndex(indexName, "TIME INDEX column must have a timestamp type");
        }
    }

    private boolean isTimestamp(DataTypeContext type) {
        if (type.timestampType() != null) {
            return true;
        }
        return type.getChildCount() == 1 && TIMESTAMP_ALIASES.contains(identifier(type.wordIdentifier()).toUpperCase(Locale.ROOT));
    }

    private void invalidTimeIndex(ParserRuleContext context, String message) {
        parser.notifyErrorListeners(context.getStart(), message, null);
    }
}
