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
package com.clougence.sql.iotdb;

import java.io.StringReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

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
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.iotdb.antlr.IotdbLexer;
import com.clougence.utils.StringUtils;

public final class IotdbLanguageSpi implements DsLanguageSpi {

    private static final List<String> TREE_COMMANDS  = List
        .of("SELECT", "SELECT LAST", "INSERT INTO", "DELETE FROM", "CREATE TIMESERIES", "CREATE ALIGNED TIMESERIES", "DROP TIMESERIES", "CREATE DATABASE", "DROP DATABASE", "SHOW DEVICES", "SHOW TIMESERIES", "SHOW DATABASES", "SHOW VERSION");
    private static final List<String> TABLE_COMMANDS = List
        .of("SELECT", "INSERT INTO", "UPDATE", "DELETE FROM", "CREATE TABLE", "ALTER TABLE", "DROP TABLE", "DROP VIEW", "CREATE DATABASE", "DROP DATABASE", "SHOW TABLES", "SHOW DATABASES", "SHOW VERSION", "DESCRIBE");
    private static final Pattern      PREFIX         = Pattern.compile("[\\p{L}\\p{N}_.@#$:]*\\z");

    private final MetaService         metaService;

    public IotdbLanguageSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = result(request, new CompletionResult());
        IotdbDslProvider provider = provider(request);
        String before = beforeCursor(request);
        List<? extends Token> tokens = provider.createLexer(CharStreams.fromString(before)).getAllTokens();
        if (tokens.stream().anyMatch(token -> token.getType() == IotdbLexer.UNRECOGNIZED || token.getType() == IotdbLexer.UNTERMINATED_BLOCK_COMMENT)) {
            return result;
        }
        if (!tokens.isEmpty()) {
            Token last = tokens.get(tokens.size() - 1);
            if (last.getStopIndex() + 1 == before.codePointCount(0, before.length()) && last.getType() == IotdbLexer.LINE_COMMENT) {
                return result;
            }
        }

        var prefixMatch = PREFIX.matcher(before);
        prefixMatch.find();
        String prefix = prefixMatch.group();
        String prior = before.substring(0, prefixMatch.start());
        int prefixIndex = prior.codePointCount(0, prior.length());
        int statementStart = 0;
        for (Token token : tokens) {
            if (token.getType() == IotdbLexer.SEMI) {
                statementStart = token.getStopIndex() + 1;
            }
        }
        int start = statementStart;
        List<Integer> statement = tokens.stream()
            .filter(token -> token.getChannel() == Token.DEFAULT_CHANNEL && token.getStartIndex() >= start && token.getStartIndex() < prefixIndex)
            .map(Token::getType)
            .toList();

        int line = (int) before.chars().filter(ch -> ch == '\n').count() + 1;
        int column = before.length() - before.lastIndexOf('\n') - 1;
        BlockLocation replacement = range(line, column - prefix.length(), line, column);
        boolean tree = provider == IotdbDslProvider.TREE;
        if (statement.isEmpty()) {
            addKeywords(result, tree ? TREE_COMMANDS : TABLE_COMMANDS, prefix, replacement);
            return result;
        }
        if (tree && statement.equals(List.of(IotdbLexer.SELECT))) {
            addKeywords(result, List.of("LAST", "*"), prefix, replacement);
        }

        Set<UmiTypes> objectTypes = completionTypes(statement, tree);
        if (!objectTypes.isEmpty() && metaService != null && request.getDataSourceId() != null) {
            completeObjects(request, result, objectTypes, tree, prefix, replacement);
        }
        return result;
    }

    private Set<UmiTypes> completionTypes(List<Integer> tokens, boolean tree) {
        int first = tokens.get(0);
        int last = tokens.get(tokens.size() - 1);
        if (tree) {
            if (last == IotdbLexer.TIMESERIES || (first == IotdbLexer.DELETE && last == IotdbLexer.FROM)) {
                return Set.of(UmiTypes.Timeseries);
            }
            if (last == IotdbLexer.FROM || last == IotdbLexer.INTO || last == IotdbLexer.DEVICES) {
                return Set.of(UmiTypes.Device);
            }
        } else if (last == IotdbLexer.FROM || last == IotdbLexer.JOIN || last == IotdbLexer.INTO || last == IotdbLexer.UPDATE || last == IotdbLexer.DESCRIBE) {
            return Set.of(UmiTypes.Table, UmiTypes.View);
        }
        return Set.of();
    }

    private void completeObjects(CompletionRequest request, CompletionResult result, Set<UmiTypes> objectTypes, boolean tree, String prefix, BlockLocation replacement) {
        List<MetaObj> objects = metaService
            .cachedObjectNames(request.getPrimaryUserId(), request.getCurrentUserId(), request.getDataSourceId(), request.getLevels(), request.getLevelsParam());
        String qualifier = "";
        String namePrefix = prefix;
        if (!tree && prefix.contains(".")) {
            int dot = prefix.indexOf('.');
            String schema = prefix.substring(0, dot);
            String currentSchema = request.getSchema();
            if (currentSchema == null) {
                currentSchema = (String) request.getLevelsParam().get(UmiTypes.Schema);
            }
            if (!schema.equalsIgnoreCase(currentSchema)) {
                return;
            }
            qualifier = quote(currentSchema) + ".";
            namePrefix = prefix.substring(dot + 1);
        }
        for (MetaObj object : objects) {
            if (!objectTypes.contains(object.getType())) {
                continue;
            }
            String name = object.getName();
            boolean matches = tree ? name.startsWith(namePrefix) : name.toLowerCase(Locale.ROOT).startsWith(namePrefix.toLowerCase(Locale.ROOT));
            if (matches) {
                CompletionItemKind kind = switch (object.getType()) {
                    case Table -> CompletionItemKind.TABLE;
                    case View -> CompletionItemKind.VIEW;
                    default -> CompletionItemKind.TEXT;
                };
                add(result, name, tree ? name : qualifier + quote(name), kind, object.getType(), replacement);
            }
        }
    }

    private void addKeywords(CompletionResult result, List<String> keywords, String prefix, BlockLocation replacement) {
        for (String keyword : keywords) {
            if (keyword.startsWith(prefix.toUpperCase(Locale.ROOT))) {
                add(result, keyword, keyword, CompletionItemKind.KEYWORD, null, replacement);
            }
        }
    }

    private void add(CompletionResult result, String label, String text, CompletionItemKind kind, UmiTypes type, BlockLocation replacement) {
        CompletionItem item = new CompletionItem();
        item.setLabel(label);
        item.setInsertText(text);
        item.setKind(kind);
        item.setUmiType(type);
        item.setRange(replacement);
        result.getItems().add(item);
    }

    private String quote(String name) {
        return "\"" + name.replace("\"", "\"\"") + "\"";
    }

    private String beforeCursor(CompletionRequest request) {
        String text = StringUtils.toString(request.getSqlText());
        if (request.getCursorLineNumber() == null || request.getCursorColNumber() == null) {
            return text;
        }
        int start = 0;
        for (int line = 1; line < request.getCursorLineNumber(); line++) {
            int newline = text.indexOf('\n', start);
            if (newline == -1) {
                return text;
            }
            start = newline + 1;
        }
        int end = text.indexOf('\n', start);
        if (end == -1) {
            end = text.length();
        }
        return text.substring(0, start + Math.min(end - start, Math.max(0, request.getCursorColNumber())));
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = result(request, new ValidateResult());
        if (StringUtils.isBlank(request.getSqlText())) {
            return result;
        }
        try (var scripts = new IotdbSplitAnalysisSpi(provider(request)).splitSourceStream(new StringReader(request.getSqlText()), 1, 0)) {
            scripts.count();
        } catch (AntlerSyntaxException e) {
            String[] lines = request.getSqlText().split("\\n", -1);
            String line = lines[e.getLine() - 1];
            int column = line.offsetByCodePoints(0, Math.min(e.getColumn(), line.codePointCount(0, line.length())));
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setSeverity(DiagnosticSeverity.ERROR);
            diagnostic.setMessage(e.getMessage());
            diagnostic.setRange(range(e.getLine(), column, e.getLine(), column + 1));
            result.getDiagnostics().add(diagnostic);
        }
        return result;
    }

    @Override
    public SplitResult split(SplitRequest request) {
        SplitResult result = result(request, new SplitResult());
        if (StringUtils.isBlank(request.getSqlText())) {
            return result;
        }
        try (var scripts = new IotdbSplitAnalysisSpi(provider(request))
            .splitSourceStream(new StringReader(request.getSqlText()), request.getBasicCodeLine(), request.getBasicCodeColumn())) {
            scripts.forEach(script -> {
                SplitSqlStatement statement = new SplitSqlStatement();
                statement.setSql(script.getScript());
                statement.setRange(range(script.getBodyStartCodeLine(), script.getBodyStartCodeColumn(), script.getBodyEndCodeLine(), script.getBodyEndCodeColumn()));
                result.getStatements().add(statement);
            });
        }
        return result;
    }

    private IotdbDslProvider provider(AbstractRequest request) {
        Map<String, String> parameters = request.getSqlParameters();
        String model = request.getCatalog();
        if (parameters != null && parameters.containsKey(IotdbSqlEngineSpi.SQL_DIALECT)) {
            model = parameters.get(IotdbSqlEngineSpi.SQL_DIALECT);
        } else if (model == null) {
            model = (String) request.getLevelsParam().get(UmiTypes.Catalog);
        }
        return IotdbSqlEngineSpi.provider(model);
    }

    private <T extends LanguageResult> T result(AbstractRequest request, T result) {
        result.setRequestId(request.getRequestId());
        result.setRequestVersion(request.getRequestVersion());
        return result;
    }

    private BlockLocation range(int startLine, int startColumn, int endLine, int endColumn) {
        BlockLocation location = new BlockLocation();
        location.setStartPosition(new CodeLocation(startLine, startColumn));
        location.setEndPosition(new CodeLocation(endLine, endColumn));
        return location;
    }
}
