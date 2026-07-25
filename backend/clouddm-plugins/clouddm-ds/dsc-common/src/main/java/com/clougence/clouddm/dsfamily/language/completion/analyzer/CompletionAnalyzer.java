/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionDialect;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStatementState;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class CompletionAnalyzer {

    public static final CompletionAnalyzer INSTANCE = new CompletionAnalyzer();

    public CompletionContext analyze(CompletionRequest request, CompletionDialect dialect) {
        String sqlText = StringUtils.toString(request.getSqlText());
        int cursorOffset = CompletionStatementState.offsetOf(sqlText, request.getCursorLineNumber(), request.getCursorColNumber());
        return analyze(request, dialect, cursorOffset);
    }

    private CompletionContext analyze(CompletionRequest request, CompletionDialect dialect, int cursorOffset) {
        String sqlText = StringUtils.toString(request.getSqlText());
        DslProvider provider = request.getSqlEngine().dslProvider(new SqlParserParameters(request.getSqlParameters()));
        CompletionLexerState lexerState = lexerState(request, dialect, provider, sqlText, cursorOffset);
        CompletionParseState parseState = parseState(dialect, provider, sqlText, lexerState);
        CompletionStatementState statementState = new CompletionStatementState(sqlText,
            fullRange(sqlText),
            request,
            lexerState.getCursorOffset(),
            lexerState.getPrefix(),
            lexerState.getQualifier(),
            lexerState.getPreviousSignificantChar(),
            lexerState.getTokensBeforeCursor(),
            true);
        return new CompletionContext(request, dialect, List.of(statementState), lexerState, parseState);
    }

    private CompletionLexerState lexerState(CompletionRequest request, CompletionDialect dialect, DslProvider provider, String sqlText, int cursorOffset) {
        cursorOffset = Math.max(0, Math.min(cursorOffset, sqlText.length()));
        List<CompletionToken> tokens = currentStatementTokens(tokens(provider, sqlText), cursorOffset, sqlText, request.getSqlEngine());
        List<String> tokensBeforeCursor = new ArrayList<>();
        List<String> operatorsBeforeCursor = new ArrayList<>();
        CompletionToken tokenBeforeCursor = null;
        CompletionToken tokenAfterCursor = null;
        CompletionToken currentToken = null;
        for (CompletionToken token : tokens) {
            if (token.containsOffset(cursorOffset)) {
                currentToken = token;
            }
            if (token.getStopIndex() < cursorOffset && isWord(token.getText(), dialect)) {
                tokensBeforeCursor.add(dialect.unquoteIdentifier(token.getText()));
                tokenBeforeCursor = token;
            }
            if (token.getStopIndex() < cursorOffset && isOperator(token.getText())) {
                operatorsBeforeCursor.add(token.getText());
            }
            if (tokenAfterCursor == null && token.getStartIndex() >= cursorOffset) {
                tokenAfterCursor = token;
            }
        }

        String prefix = extractPrefix(request, sqlText, cursorOffset, dialect);
        String qualifier = extractQualifier(sqlText, cursorOffset, prefix, dialect);
        CompletionNamePath namePath = namePath(tokens, cursorOffset, prefix, dialect);
        FunctionContext functionContext = functionContext(tokens, cursorOffset, dialect);
        return CompletionLexerState.builder()
            .tokens(tokens)
            .tokensBeforeCursor(tokensBeforeCursor)
            .operatorsBeforeCursor(operatorsBeforeCursor)
            .tokenBeforeCursor(tokenBeforeCursor)
            .tokenAfterCursor(tokenAfterCursor)
            .currentToken(currentToken)
            .functionName(functionContext.functionName)
            .functionParameterIndex(functionContext.parameterIndex)
            .operatorBeforeCursor(operatorsBeforeCursor.isEmpty() ? null : operatorsBeforeCursor.get(operatorsBeforeCursor.size() - 1))
            .prefix(prefix)
            .qualifier(qualifier)
            .namePath(namePath)
            .previousSignificantChar(previousSignificantChar(sqlText, cursorOffset, prefix))
            .cursorOffset(cursorOffset)
            .build();
    }

    private CompletionParseState parseState(CompletionDialect dialect, DslProvider provider, String sqlText, CompletionLexerState lexerState) {
        TolerantParseResult parseResult = tolerantParse(provider, sqlText);
        return CompletionParseState.builder()
            .parsed(parseResult.parsed)
            .hasSyntaxError(!parseResult.syntaxErrors.isEmpty())
            .syntaxErrors(parseResult.syntaxErrors)
            .clause(clause(lexerState.getTokensBeforeCursor(), lexerState.getTokens(), dialect))
            .tableRefs(tableRefs(lexerState.getTokens(), dialect))
            .columnRefs(columnRefs(lexerState.getTokens(), dialect))
            .build();
    }

    private TolerantParseResult tolerantParse(DslProvider provider, String sqlText) {
        if (StringUtils.isBlank(sqlText)) {
            return new TolerantParseResult(false, Collections.emptyList());
        }
        CollectingErrorListener listener = new CollectingErrorListener();
        try {
            Lexer lexer = provider.createLexer(CharStreams.fromString(sqlText));
            lexer.removeErrorListeners();
            lexer.addErrorListener(listener);

            Parser parser = provider.createParser(lexer);
            parser.removeErrorListeners();
            parser.addErrorListener(listener);
            List<AstSplitScript> scripts = provider.doSplit(lexer, parser);
            for (AstSplitScript script : scripts) {
                ParseTree tree = script.getAstTree();
                if (tree == null) {
                    continue;
                }
            }
            return new TolerantParseResult(!listener.hasErrors(), listener.syntaxErrors());
        } catch (RuntimeException e) {
            return new TolerantParseResult(false, listener.syntaxErrors());
        }
    }

    private List<CompletionToken> tokens(DslProvider provider, String sqlText) {
        if (StringUtils.isBlank(sqlText)) {
            return Collections.emptyList();
        }
        try {
            Lexer lexer = provider.createLexer(CharStreams.fromString(sqlText));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new CollectingErrorListener());
            CommonTokenStream stream = new CommonTokenStream(lexer);
            stream.fill();

            List<CompletionToken> result = new ArrayList<>();
            for (Token token : stream.getTokens()) {
                if (token.getType() == Token.EOF || token.getChannel() != Token.DEFAULT_CHANNEL || StringUtils.isBlank(token.getText())) {
                    continue;
                }
                result.add(CompletionToken.builder()
                    .text(token.getText())
                    .type(token.getType())
                    .channel(token.getChannel())
                    .line(token.getLine())
                    .column(token.getCharPositionInLine())
                    .startIndex(token.getStartIndex())
                    .stopIndex(token.getStopIndex())
                    .build());
            }
            return result;
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    private static List<CompletionToken> currentStatementTokens(List<CompletionToken> tokens, int cursorOffset, String sqlText, SqlEngineSpi sqlEngine) {
        if (tokens.isEmpty()) {
            return Collections.emptyList();
        }

        int startIndex = 0;
        int endIndex = tokens.size();
        for (int i = 0; i < tokens.size(); i++) {
            CompletionToken token = tokens.get(i);
            if (token.getStopIndex() < cursorOffset && isStatementSeparator(token, sqlText, sqlEngine)) {
                startIndex = i + 1;
            }
            if (token.getStartIndex() >= cursorOffset && isStatementSeparator(token, sqlText, sqlEngine)) {
                endIndex = i;
                break;
            }
        }
        if (startIndex >= endIndex) {
            return Collections.emptyList();
        }
        return List.copyOf(tokens.subList(startIndex, endIndex));
    }

    private static boolean isStatementSeparator(CompletionToken token, String sqlText, SqlEngineSpi sqlEngine) {
        String text = lower(token.getText());
        return ";".equals(text) || (isSqlServer(sqlEngine) && "go".equals(text) && lineText(sqlText, token).trim().equalsIgnoreCase("go"));
    }

    private static boolean isSqlServer(SqlEngineSpi sqlEngine) {
        String name = sqlEngine == null ? "" : StringUtils.toString(sqlEngine.name()).toLowerCase(Locale.ROOT);
        return name.contains("t-sql") || name.contains("sqlserver") || name.contains("sql server");
    }

    private static String lineText(String sqlText, CompletionToken token) {
        int start = Math.max(0, Math.min(token.getStartIndex(), sqlText.length()));
        while (start > 0 && sqlText.charAt(start - 1) != '\n' && sqlText.charAt(start - 1) != '\r') {
            start--;
        }

        int end = Math.max(0, Math.min(token.getStopIndex() + 1, sqlText.length()));
        while (end < sqlText.length() && sqlText.charAt(end) != '\n' && sqlText.charAt(end) != '\r') {
            end++;
        }
        return sqlText.substring(start, end);
    }

    private static CompletionClause clause(List<String> tokensBeforeCursor, List<CompletionToken> tokens, CompletionDialect dialect) {
        if (isInsertColumnList(tokens, dialect)) {
            return CompletionClause.INSERT_COLUMNS;
        }
        for (int i = tokensBeforeCursor.size() - 1; i >= 0; i--) {
            String token = tokensBeforeCursor.get(i).toLowerCase(Locale.ROOT);
            switch (token) {
                case "from" -> {
                    return CompletionClause.FROM_TABLE;
                }
                case "join" -> {
                    return CompletionClause.JOIN_TABLE;
                }
                case "on" -> {
                    return CompletionClause.JOIN_CONDITION;
                }
                case "where", "having" -> {
                    return CompletionClause.WHERE_CONDITION;
                }
                case "by" -> {
                    String before = i > 0 ? tokensBeforeCursor.get(i - 1).toLowerCase(Locale.ROOT) : "";
                    if ("group".equals(before)) {
                        return CompletionClause.GROUP_BY;
                    }
                    if ("order".equals(before)) {
                        return CompletionClause.ORDER_BY;
                    }
                }
                case "into" -> {
                    return CompletionClause.INSERT_TARGET;
                }
                case "set" -> {
                    return CompletionClause.UPDATE_SET;
                }
                case "update" -> {
                    return CompletionClause.UPDATE_TARGET;
                }
                case "select" -> {
                    return CompletionClause.SELECT_LIST;
                }
                default -> {
                    // continue scanning backwards
                }
            }
        }
        return CompletionClause.UNKNOWN;
    }

    private static boolean isInsertColumnList(List<CompletionToken> tokens, CompletionDialect dialect) {
        if (tokens.isEmpty() || !"insert".equals(lower(tokens.get(0).getText()))) {
            return false;
        }

        for (CompletionToken token : tokens) {
            if ("values".equals(lower(token.getText()))) {
                return false;
            }
        }

        for (int i = 0; i < tokens.size(); i++) {
            if (!"into".equals(lower(tokens.get(i).getText()))) {
                continue;
            }
            TableFactorIndexResult tableFactor = readTableFactorIndexes(tokens, i + 1, dialect);
            if (tableFactor == null) {
                return false;
            }
            return hasUnclosedParen(tokens, tableFactor.nextIndex);
        }
        return false;
    }

    private static boolean hasUnclosedParen(List<CompletionToken> tokens, int startIndex) {
        int depth = 0;
        for (int i = Math.max(0, startIndex); i < tokens.size(); i++) {
            String text = tokens.get(i).getText();
            if ("(".equals(text)) {
                depth++;
            } else if (")".equals(text) && depth > 0) {
                depth--;
            }
        }
        return depth > 0;
    }

    private static List<CompletionTableRef> tableRefs(List<CompletionToken> tokens, CompletionDialect dialect) {
        if (tokens.isEmpty()) {
            return Collections.emptyList();
        }

        List<CompletionTableRef> refs = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = lower(tokens.get(i).getText());
            if (!"from".equals(token) && !"join".equals(token) && !"update".equals(token) && !"into".equals(token)) {
                continue;
            }
            int index = i + 1;
            while (index < tokens.size()) {
                TableFactorResult tableFactor = readTableFactor(tokens, index, dialect);
                if (tableFactor == null) {
                    break;
                }
                NameResult name = tableFactor.name;
                refs.add(CompletionTableRef.builder().catalog(name.catalog).schema(name.schema).table(name.table).alias(tableFactor.alias).build());
                index = tableFactor.nextIndex;
                if (index < tokens.size() && ",".equals(tokens.get(index).getText())) {
                    index++;
                    continue;
                }
                break;
            }
        }
        return refs;
    }

    private static List<CompletionColumnRef> columnRefs(List<CompletionToken> tokens, CompletionDialect dialect) {
        if (tokens.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Integer> tableTokenIndexes = tableTokenIndexes(tokens, dialect);
        List<CompletionColumnRef> refs = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (tableTokenIndexes.contains(i)) {
                continue;
            }
            String text = tokens.get(i).getText();
            if (!isWord(text, dialect)) {
                continue;
            }
            if (isFunctionName(tokens, i)) {
                continue;
            }

            if (i + 2 < tokens.size() && !tableTokenIndexes.contains(i + 2) && ".".equals(tokens.get(i + 1).getText()) && isWord(tokens.get(i + 2).getText(), dialect)) {
                refs.add(CompletionColumnRef.builder().qualifier(dialect.unquoteIdentifier(text)).column(dialect.unquoteIdentifier(tokens.get(i + 2).getText())).build());
                i += 2;
            } else if (!isKeyword(text)) {
                refs.add(CompletionColumnRef.builder().column(dialect.unquoteIdentifier(text)).build());
            }
        }
        return refs;
    }

    private static boolean isFunctionName(List<CompletionToken> tokens, int index) {
        return index + 1 < tokens.size() && "(".equals(tokens.get(index + 1).getText());
    }

    private static Set<Integer> tableTokenIndexes(List<CompletionToken> tokens, CompletionDialect dialect) {
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = lower(tokens.get(i).getText());
            if (!"from".equals(token) && !"join".equals(token) && !"update".equals(token) && !"into".equals(token)) {
                continue;
            }

            int index = i + 1;
            while (index < tokens.size()) {
                TableFactorIndexResult tableFactor = readTableFactorIndexes(tokens, index, dialect);
                if (tableFactor == null) {
                    break;
                }
                indexes.addAll(tableFactor.tokenIndexes);
                index = tableFactor.nextIndex;
                if (index < tokens.size() && ",".equals(tokens.get(index).getText())) {
                    index++;
                    continue;
                }
                break;
            }
        }
        return indexes;
    }

    private static TableFactorResult readTableFactor(List<CompletionToken> tokens, int index, CompletionDialect dialect) {
        TableFactorIndexResult indexResult = readTableFactorIndexes(tokens, index, dialect);
        if (indexResult == null) {
            return null;
        }
        NameResult name = toNameResult(indexResult.name);
        if (name == null || StringUtils.isBlank(name.table)) {
            return null;
        }
        TableFactorResult result = new TableFactorResult();
        result.name = name;
        result.alias = indexResult.alias;
        result.nextIndex = indexResult.nextIndex;
        return result;
    }

    private static TableFactorIndexResult readTableFactorIndexes(List<CompletionToken> tokens, int index, CompletionDialect dialect) {
        NameIndexResult name = readNameIndexes(tokens, index, dialect);
        if (name == null || name.nameIndexes.isEmpty()) {
            return null;
        }

        TableFactorIndexResult result = new TableFactorIndexResult();
        result.name = name;
        result.tokenIndexes = new ArrayList<>(name.nameIndexes);
        result.nextIndex = name.nextIndex;
        if (result.nextIndex < tokens.size() && "as".equalsIgnoreCase(tokens.get(result.nextIndex).getText())) {
            result.tokenIndexes.add(result.nextIndex);
            result.nextIndex++;
        }
        if (result.nextIndex < tokens.size() && isWord(tokens.get(result.nextIndex).getText(), dialect) && !isStopToken(tokens.get(result.nextIndex).getText())) {
            result.alias = dialect.unquoteIdentifier(tokens.get(result.nextIndex).getText());
            result.tokenIndexes.add(result.nextIndex);
            result.nextIndex++;
        }
        return result;
    }

    private static NameResult readName(List<CompletionToken> tokens, int index, CompletionDialect dialect) {
        NameIndexResult indexResult = readNameIndexes(tokens, index, dialect);
        if (indexResult == null) {
            return null;
        }
        return toNameResult(indexResult);
    }

    private static NameResult toNameResult(NameIndexResult indexResult) {
        List<String> parts = indexResult.parts;
        NameResult result = new NameResult();
        result.nextIndex = indexResult.nextIndex;
        result.table = parts.get(parts.size() - 1);
        if (parts.size() >= 2) {
            result.schema = parts.get(parts.size() - 2);
        }
        if (parts.size() >= 3) {
            result.catalog = parts.get(parts.size() - 3);
        }
        return result;
    }

    private static NameIndexResult readNameIndexes(List<CompletionToken> tokens, int index, CompletionDialect dialect) {
        List<String> parts = new ArrayList<>();
        List<Integer> nameIndexes = new ArrayList<>();
        boolean expectPart = true;
        while (index < tokens.size()) {
            String text = tokens.get(index).getText();
            if (isStopToken(text) || "(".equals(text) || ")".equals(text) || ",".equals(text)) {
                break;
            }
            if (".".equals(text)) {
                expectPart = true;
                index++;
                continue;
            }
            if (!expectPart || !isWord(text, dialect)) {
                break;
            }
            parts.add(dialect.unquoteIdentifier(text));
            nameIndexes.add(index);
            expectPart = false;
            index++;
        }
        if (parts.isEmpty()) {
            return null;
        }
        NameIndexResult result = new NameIndexResult();
        result.nextIndex = index;
        result.parts = parts;
        result.nameIndexes = nameIndexes;
        return result;
    }

    private static boolean isStopToken(String token) {
        return switch (lower(token)) {
            case "", "where", "join", "left", "right", "inner", "outer", "cross", "full", "on", "order", "group", "having", "limit", "union", "select", "set", "values" -> true;
            default -> false;
        };
    }

    private static boolean isKeyword(String token) {
        return switch (lower(token)) {
            case "select", "from", "where", "join", "left", "right", "inner", "outer", "cross", "full", "on", "order", "group", "by", "having", "limit", "union", "insert", "into",
                    "update", "delete", "set", "values", "as", "and", "or", "not", "null", "is", "in", "exists", "between", "like", "case", "when", "then", "else", "end",
                    "distinct", "all", "any", "some" ->
                true;
            default -> false;
        };
    }

    private static boolean isWord(String text, CompletionDialect dialect) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        if (isQuotedIdentifier(text)) {
            return true;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!dialect.isIdentifierChar(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isQuotedIdentifier(String text) {
        String value = StringUtils.toString(text).trim();
        return value.length() >= 2
               && ((value.startsWith("[") && value.endsWith("]")) || (value.startsWith("`") && value.endsWith("`")) || (value.startsWith("\"") && value.endsWith("\"")));
    }

    private static boolean isOperator(String text) {
        return switch (lower(text)) {
            case "=", "<", ">", "<=", ">=", "<>", "!=", "+", "-", "*", "/", "%", "||", "&&", "like", "in", "between", "is", "not" -> true;
            default -> false;
        };
    }

    private static FunctionContext functionContext(List<CompletionToken> tokens, int cursorOffset, CompletionDialect dialect) {
        Deque<Integer> openParens = new ArrayDeque<>();
        for (int i = 0; i < tokens.size(); i++) {
            CompletionToken token = tokens.get(i);
            if (token.getStartIndex() >= cursorOffset) {
                break;
            }
            if ("(".equals(token.getText())) {
                openParens.push(i);
            } else if (")".equals(token.getText()) && !openParens.isEmpty()) {
                openParens.pop();
            }
        }
        while (!openParens.isEmpty()) {
            int openIndex = openParens.pop();
            if (openIndex <= 0 || !isFunctionNameToken(tokens.get(openIndex - 1), dialect)) {
                continue;
            }
            FunctionContext context = new FunctionContext();
            context.functionName = dialect.unquoteIdentifier(tokens.get(openIndex - 1).getText());
            context.parameterIndex = 0;
            int depth = 0;
            for (int i = openIndex + 1; i < tokens.size() && tokens.get(i).getStartIndex() < cursorOffset; i++) {
                String text = tokens.get(i).getText();
                if ("(".equals(text)) {
                    depth++;
                } else if (")".equals(text) && depth > 0) {
                    depth--;
                } else if (",".equals(text) && depth == 0) {
                    context.parameterIndex++;
                }
            }
            return context;
        }

        FunctionContext context = new FunctionContext();
        context.parameterIndex = -1;
        return context;
    }

    private static boolean isFunctionNameToken(CompletionToken token, CompletionDialect dialect) {
        return isWord(token.getText(), dialect) && !isKeyword(token.getText());
    }

    private static String extractPrefix(CompletionRequest request, String sqlText, int offset, CompletionDialect dialect) {
        if (StringUtils.isBlank(sqlText) || cursorAfterTrimmedWhitespace(request, sqlText) || offset <= 0 || Character.isWhitespace(sqlText.charAt(offset - 1))) {
            return "";
        }
        int start = Math.min(offset, sqlText.length());
        while (start > 0 && dialect.isIdentifierChar(sqlText.charAt(start - 1))) {
            start--;
        }
        return sqlText.substring(start, Math.min(offset, sqlText.length()));
    }

    private static boolean cursorAfterTrimmedWhitespace(CompletionRequest request, String sqlText) {
        if (request.getCursorLineNumber() == null || request.getCursorColNumber() == null) {
            return false;
        }

        String lineText = lineText(sqlText, Math.max(1, request.getCursorLineNumber()));
        return lineText != null && Math.max(0, request.getCursorColNumber()) > lineText.length();
    }

    private static String lineText(String sqlText, int targetLine) {
        if (StringUtils.isBlank(sqlText)) {
            return null;
        }

        int line = 1;
        int start = 0;
        for (int i = 0; i < sqlText.length(); i++) {
            if (sqlText.charAt(i) != '\n') {
                continue;
            }

            if (line == targetLine) {
                return sqlText.substring(start, i).replace("\r", "");
            }
            line++;
            start = i + 1;
        }
        return line == targetLine ? sqlText.substring(start).replace("\r", "") : null;
    }

    private static String extractQualifier(String sqlText, int offset, String prefix, CompletionDialect dialect) {
        int end = Math.max(0, Math.min(offset - StringUtils.toString(prefix).length(), sqlText.length()));
        int dot = end - 1;
        while (dot >= 0 && Character.isWhitespace(sqlText.charAt(dot))) {
            dot--;
        }
        if (dot < 0 || sqlText.charAt(dot) != '.') {
            return "";
        }

        int start = dot;
        while (start > 0 && dialect.isIdentifierChar(sqlText.charAt(start - 1))) {
            start--;
        }
        return dialect.unquoteIdentifier(sqlText.substring(start, dot));
    }

    private static CompletionNamePath namePath(List<CompletionToken> tokens, int cursorOffset, String prefix, CompletionDialect dialect) {
        List<CompletionToken> before = new ArrayList<>();
        for (CompletionToken token : tokens) {
            if (token.getStartIndex() >= cursorOffset) {
                break;
            }
            before.add(token);
        }

        List<String> parts = new ArrayList<>();
        boolean expectName = true;
        for (int i = before.size() - 1; i >= 0; i--) {
            String text = before.get(i).getText();
            if (expectName && ".".equals(text)) {
                continue;
            }
            if (expectName) {
                if (!isWord(text, dialect)) {
                    break;
                }
                parts.add(dialect.unquoteIdentifier(text));
                expectName = false;
            } else {
                if (!".".equals(text)) {
                    break;
                }
                expectName = true;
            }
        }
        Collections.reverse(parts);
        if (StringUtils.isNotBlank(prefix) && !parts.isEmpty() && StringUtils.equals(parts.get(parts.size() - 1), prefix)) {
            parts.remove(parts.size() - 1);
        }
        return CompletionNamePath.builder().qualifiers(parts).prefix(prefix).build();
    }

    private static char previousSignificantChar(String sqlText, int offset, String prefix) {
        int index = Math.max(0, Math.min(offset - StringUtils.toString(prefix).length(), sqlText.length())) - 1;
        while (index >= 0 && Character.isWhitespace(sqlText.charAt(index))) {
            index--;
        }
        return index >= 0 ? sqlText.charAt(index) : 0;
    }

    private static String lower(String token) {
        return StringUtils.toString(token).toLowerCase(Locale.ROOT);
    }

    private static BlockLocation fullRange(String sqlText) {
        BlockLocation range = new BlockLocation();
        range.setStartPosition(new CodeLocation(1, 0));
        range.setEndPosition(positionAtEnd(sqlText));
        return range;
    }

    private static CodeLocation positionAtEnd(String sqlText) {
        int line = 1;
        int column = 0;
        for (int i = 0; i < sqlText.length(); i++) {
            if (sqlText.charAt(i) == '\n') {
                line++;
                column = 0;
            } else if (sqlText.charAt(i) != '\r') {
                column++;
            }
        }
        return new CodeLocation(line, column);
    }

    private static class NameResult {
        private String catalog;
        private String schema;
        private String table;
        private int    nextIndex;
    }

    private static class NameIndexResult {
        private List<String>  parts;
        private List<Integer> nameIndexes;
        private int           nextIndex;
    }

    private static class TableFactorResult {
        private NameResult name;
        private String     alias;
        private int        nextIndex;
    }

    private static class TableFactorIndexResult {
        private NameIndexResult name;
        private String          alias;
        private List<Integer>   tokenIndexes;
        private int             nextIndex;
    }

    private static class FunctionContext {
        private String functionName;
        private int    parameterIndex;
    }

    private static class CollectingErrorListener extends BaseErrorListener {
        private final List<CompletionSyntaxError> syntaxErrors = new ArrayList<>();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            this.syntaxErrors.add(CompletionSyntaxError.builder().line(line).column(charPositionInLine).message(msg).offendingText(offendingText(offendingSymbol)).build());
        }

        public boolean hasErrors() {
            return !syntaxErrors.isEmpty();
        }

        public List<CompletionSyntaxError> syntaxErrors() {
            return List.copyOf(syntaxErrors);
        }

        private static String offendingText(Object offendingSymbol) {
            if (offendingSymbol instanceof Token token) {
                return token.getText();
            }
            return offendingSymbol == null ? null : offendingSymbol.toString();
        }
    }

    private record TolerantParseResult(boolean parsed, List<CompletionSyntaxError> syntaxErrors) {
    }
}
