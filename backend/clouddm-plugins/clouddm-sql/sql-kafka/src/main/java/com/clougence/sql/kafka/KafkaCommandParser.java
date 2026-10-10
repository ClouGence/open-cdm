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

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.clougence.sql.kafka.antlr.KafkaLexer;
import com.clougence.sql.kafka.antlr.KafkaParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class KafkaCommandParser {
    public KafkaCommand parse(String text) {
        KafkaLexer lexer = new KafkaLexer(CharStreams.fromString(text));
        KafkaParser parser = new KafkaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(KafkaSyntaxErrorListener.INSTANCE);
        return from(parser.singleCommand().command());
    }

    public KafkaCommand from(KafkaParser.CommandContext context) {
        Token start = context.getStart();
        KafkaToken head = new KafkaToken(start.getText(), start.getStartIndex(), start.getStopIndex() + 1);
        KafkaCommandType type = KafkaCommandType.find(head.value());
        Map<String, KafkaOption> spec = KafkaCommandSpec.options(type);
        Map<String, List<String>> options = new LinkedHashMap<>();
        Map<String, KafkaToken> locations = new LinkedHashMap<>();
        for (KafkaParser.OptionContext optionContext : context.option()) {
            Token nameToken = optionContext.getStart();
            String name = nameToken.getText();
            KafkaToken token = new KafkaToken(name, nameToken.getStartIndex(), optionContext.getStop().getStopIndex() + 1);
            KafkaOption option = spec.get(name);
            if (option == null) {
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_OPTION_UNKNOWN, name);
            }
            if (options.containsKey(name) && !option.repeatable()) {
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_OPTION_DUPLICATE, name);
            }
            List<String> values = options.computeIfAbsent(name, key -> new ArrayList<>());
            if (optionContext.valueOption() != null) {
                String value = KafkaWords.decode(optionContext.valueOption().argument().getText());
                if (value.isBlank()) {
                    throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_VALUE_REQUIRED, name);
                }
                values.add(value);
            }
            locations.put(name, token);
        }
        Map<String, List<String>> immutable = new LinkedHashMap<>();
        options.forEach((name, values) -> immutable.put(name, List.copyOf(values)));
        KafkaCommand command = new KafkaCommand(type, Collections.unmodifiableMap(immutable));
        validate(command, locations, head);
        return command;
    }

    private void validate(KafkaCommand command, Map<String, KafkaToken> locations, KafkaToken head) {
        if (command.has("--help")) {
            return;
        }
        KafkaCommandType type = command.getType();
        List<String> actions = type.getActions().stream().filter(command::has).toList();
        if (type != KafkaCommandType.CONSUMER && actions.size() != 1) {
            throw new KafkaParseException(head.start(), head.end(), KafkaSqlI18nKeys.KAFKA_ACTION_REQUIRED, String.join(" / ", type.getActions()));
        }
        String action = null;
        if (!actions.isEmpty()) {
            action = actions.get(0);
        }
        List<String> allowed = KafkaCommandSpec.allowed(type, action);
        for (String option : command.getOptions().keySet()) {
            KafkaToken token = locations.get(option);
            if (!allowed.contains(option)) {
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_OPTION_ACTION, option, action);
            }
            if (List.of("--partitions", "--replication-factor", "--partition", "--max-messages", "--timeout-ms").contains(option)) {
                validateNumber(option, command.value(option), token);
            }
        }
        if (type == KafkaCommandType.CONSUMER || command.has("--create") || (type == KafkaCommandType.TOPICS && command.has("--delete"))) {
            require(command, "--topic", head);
        }
        if (type == KafkaCommandType.CONSUMER) {
            if (command.has("--from-beginning") && command.has("--offset")) {
                KafkaToken token = locations.get("--offset");
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_OFFSET_CONFLICT);
            }
            if (command.has("--offset") && !"latest".equals(command.value("--offset"))) {
                KafkaToken token = locations.get("--offset");
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_OFFSET_UNSUPPORTED);
            }
        }
        if (command.has("--config")) {
            validateConfigs(command, locations.get("--config"));
        }
        if (type == KafkaCommandType.TOPICS && command.has("--topic") && !command.has("--create")) {
            try {
                Pattern.compile(command.value("--topic"));
            } catch (PatternSyntaxException e) {
                KafkaToken token = locations.get("--topic");
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_PATTERN_INVALID);
            }
        }
        if (command.has("--topic") && (type == KafkaCommandType.CONSUMER || command.has("--create"))) {
            String topic = command.value("--topic");
            if (topic.length() > 249 || topic.equals(".") || topic.equals("..") || !topic.matches("[a-zA-Z0-9._-]+")) {
                KafkaToken token = locations.get("--topic");
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_TOPIC_INVALID);
            }
        }
    }

    private void require(KafkaCommand command, String option, KafkaToken head) {
        if (!command.has(option)) {
            throw new KafkaParseException(head.start(), head.end(), KafkaSqlI18nKeys.KAFKA_VALUE_REQUIRED, option);
        }
    }

    private void validateNumber(String name, String value, KafkaToken token) {
        long max = Integer.MAX_VALUE;
        if (name.equals("--replication-factor")) {
            max = Short.MAX_VALUE;
        } else if (name.equals("--timeout-ms")) {
            max = Long.MAX_VALUE;
        }
        long min = 1;
        if (name.equals("--partition")) {
            min = 0;
        }
        try {
            long number = Long.parseLong(value);
            if (number < min || number > max) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_NUMBER_INVALID, name, min, max);
        }
    }

    private void validateConfigs(KafkaCommand command, KafkaToken token) {
        Set<String> keys = new HashSet<>();
        for (String config : command.getOptions().get("--config")) {
            int equals = config.indexOf('=');
            if (equals <= 0 || config.substring(0, equals).isBlank()) {
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_CONFIG_INVALID);
            }
            if (!keys.add(config.substring(0, equals))) {
                throw new KafkaParseException(token.start(), token.end(), KafkaSqlI18nKeys.KAFKA_CONFIG_DUPLICATE);
            }
        }
    }
}
