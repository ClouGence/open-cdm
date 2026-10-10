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
package com.clougence.clouddm.ds.kafka.language;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.split.SplitSqlStatement;
import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.DiagnosticSeverity;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.sql.kafka.KafkaCommand;
import com.clougence.sql.kafka.KafkaCommandParser;
import com.clougence.sql.kafka.KafkaCommandSpec;
import com.clougence.sql.kafka.KafkaCommandType;
import com.clougence.sql.kafka.KafkaEditorParser;
import com.clougence.sql.kafka.KafkaOption;
import com.clougence.sql.kafka.KafkaParseException;
import com.clougence.sql.kafka.KafkaSqlI18nKeys;
import com.clougence.sql.kafka.KafkaToken;
import com.clougence.utils.i18n.I18nUtils;

public class KafkaLanguageSpi implements DsLanguageSpi {
    private final I18nUtils i18n = I18nUtils.initI18n(KafkaSqlI18nKeys.class);

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = init(request, new CompletionResult());
        String before = beforeCursor(request);
        var completion = KafkaEditorParser.completionTokens(before);
        if (completion.isEmpty()) {
            return result;
        }
        List<KafkaToken> tokens = new ArrayList<>(completion.get());
        String prefix = "";
        if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).end() == before.codePointCount(0, before.length())) {
            prefix = tokens.remove(tokens.size() - 1).value();
        }
        if (tokens.isEmpty()) {
            for (KafkaCommandType type : KafkaCommandType.values()) {
                String command = type.getCommand();
                if (prefix.contains(".")) {
                    command += ".sh";
                }
                add(result, command, prefix);
            }
            return result;
        }
        KafkaCommandType type = KafkaCommandType.find(tokens.get(0).value());
        if (type == null) {
            return result;
        }
        completeArguments(type, tokens.subList(1, tokens.size()), prefix, result);
        return result;
    }

    private void completeArguments(KafkaCommandType type, List<KafkaToken> tokens, String prefix, CompletionResult result) {
        Map<String, KafkaOption> spec = KafkaCommandSpec.options(type);
        Set<String> used = new HashSet<>();
        String pending = null;
        String action = null;
        for (KafkaToken token : tokens) {
            if (pending != null) {
                if (!token.value().equals("=")) {
                    pending = null;
                }
                continue;
            }
            String option = token.value().split("=", 2)[0];
            KafkaOption definition = spec.get(option);
            if (definition == null) {
                return;
            }
            used.add(option);
            if (type.getActions().contains(option)) {
                action = option;
            }
            if (definition.valueRequired() && !token.value().contains("=")) {
                pending = option;
            }
        }
        String insertionPrefix = "";
        if (pending == null && prefix.contains("=")) {
            int equals = prefix.indexOf('=');
            pending = prefix.substring(0, equals);
            insertionPrefix = pending + "=";
        }
        if (pending != null) {
            List<String> values = switch (pending) {
                case "--offset" -> List.of("latest");
                case "--partition" -> List.of("0");
                case "--partitions", "--replication-factor" -> List.of("1");
                case "--max-messages" -> List.of("10");
                case "--timeout-ms" -> List.of("1000");
                default -> List.of();
            };
            for (String value : values) {
                add(result, insertionPrefix + value, prefix);
            }
            return;
        }
        for (String option : KafkaCommandSpec.allowed(type, action)) {
            if (used.contains(option) && !spec.get(option).repeatable()) {
                continue;
            }
            if ((option.equals("--offset") && used.contains("--from-beginning")) || (option.equals("--from-beginning") && used.contains("--offset"))) {
                continue;
            }
            if (List.of("--offsets", "--members", "--state").contains(option) && used.stream().anyMatch(List.of("--offsets", "--members", "--state")::contains)) {
                continue;
            }
            if (option.equals("--verbose") && !used.contains("--members")) {
                continue;
            }
            add(result, option, prefix);
        }
    }

    private void add(CompletionResult result, String value, String prefix) {
        if (value.startsWith(prefix)) {
            CompletionItem item = new CompletionItem();
            item.setLabel(value);
            item.setInsertText(value);
            item.setKind(CompletionItemKind.KEYWORD);
            item.setWeight(800);
            result.getItems().add(item);
        }
    }

    private String beforeCursor(CompletionRequest request) {
        String text = request.getSqlText();
        int offset = 0;
        int line = 1;
        int column = 0;
        while (offset < text.length()) {
            if (line == request.getCursorLineNumber() && column == request.getCursorColNumber()) {
                break;
            }
            if (text.charAt(offset++) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return text.substring(0, offset);
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = init(request, new ValidateResult());
        KafkaCommandParser parser = new KafkaCommandParser();
        for (SplitScript script : KafkaEditorParser.split(request.getSqlText(), request.getBasicCodeLine(), request.getBasicCodeColumn())) {
            try {
                KafkaCommand command = parser.parse(script.getScript());
                if (command.has("--help")) {
                    String message = i18n.getMessage("KAFKA_HELP_" + command.getType().name()) + "\n" + i18n.getMessage(KafkaSqlI18nKeys.KAFKA_HELP_COMMON);
                    if (command.getType() == KafkaCommandType.CONSUMER) {
                        message += "\n" + i18n.getMessage(KafkaSqlI18nKeys.KAFKA_CONSUMER_DEFAULTS);
                    }
                    result.getDiagnostics().add(diagnostic(script, 0, script.getScript().codePointCount(0, script.getScript().length()), DiagnosticSeverity.INFO, message));
                } else if (command.getType() == KafkaCommandType.CONSUMER && (!command.has("--max-messages") || !command.has("--timeout-ms"))) {
                    result.getDiagnostics()
                        .add(diagnostic(script, 0, script.getScript().codePointCount(0, script.getScript().length()), DiagnosticSeverity.HINT, i18n
                            .getMessage(KafkaSqlI18nKeys.KAFKA_CONSUMER_DEFAULTS)));
                }
            } catch (KafkaParseException e) {
                result.getDiagnostics().add(diagnostic(script, e.getStart(), e.getEnd(), DiagnosticSeverity.ERROR, i18n.getMessage(e.getMessageKey(), e.getMessageArgs())));
            }
        }
        return result;
    }

    private Diagnostic diagnostic(SplitScript script, int start, int end, DiagnosticSeverity severity, String message) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setSeverity(severity);
        diagnostic.setMessage(message);
        BlockLocation range = new BlockLocation();
        range.setStartPosition(position(script, start));
        range.setEndPosition(position(script, end));
        diagnostic.setRange(range);
        return diagnostic;
    }

    private CodeLocation position(SplitScript script, int offset) {
        int line = script.getBodyStartCodeLine();
        int column = script.getBodyStartCodeColumn();
        int end = script.getScript().offsetByCodePoints(0, offset);
        for (int i = 0; i < end; i += Character.charCount(script.getScript().codePointAt(i))) {
            if (script.getScript().charAt(i) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return new CodeLocation(line, column);
    }

    @Override
    public SplitResult split(SplitRequest request) {
        SplitResult result = init(request, new SplitResult());
        for (SplitScript script : KafkaEditorParser.split(request.getSqlText(), request.getBasicCodeLine(), request.getBasicCodeColumn())) {
            SplitSqlStatement statement = new SplitSqlStatement();
            statement.setSql(script.getScript());
            BlockLocation range = new BlockLocation();
            range.setStartPosition(new CodeLocation(script.getBodyStartCodeLine(), script.getBodyStartCodeColumn()));
            range.setEndPosition(new CodeLocation(script.getBodyEndCodeLine(), script.getBodyEndCodeColumn()));
            statement.setRange(range);
            result.getStatements().add(statement);
        }
        return result;
    }

    private static <T extends LanguageResult> T init(AbstractRequest request, T result) {
        result.setRequestId(request.getRequestId());
        result.setRequestVersion(request.getRequestVersion());
        return result;
    }
}
