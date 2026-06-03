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
package com.clougence.clouddm.dsfamily.language.split;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.split.SplitSqlStatement;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class SplitStrategyCenter {

    public SplitResult split(SplitRequest request, DslProvider dslProvider) {
        return split(request, dslProvider, SplitOptions.defaultSql());
    }

    public SplitResult split(SplitRequest request, DslProvider dslProvider, SplitOptions options) {
        SplitResult result = initResult(request, new SplitResult());
        if (request == null || StringUtils.isBlank(request.getSqlText())) {
            return result;
        }

        Objects.requireNonNull(dslProvider, "dslProvider");
        Objects.requireNonNull(options, "options");
        String sqlText = request.getSqlText();
        CodeLocation base = new CodeLocation(request.getBasicCodeLine(), request.getBasicCodeColumn());
        try {
            List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider, sqlText, base);
            if (!scripts.isEmpty()) {
                appendStatements(result, scripts);
                return result;
            }
        } catch (RuntimeException ignored) {
            // Fall back to token-level split so incomplete SQL can still locate statements.
        }

        appendTolerantStatements(result, sqlText, dslProvider, base, options);
        return result;
    }

    private void appendStatements(SplitResult result, List<AstSplitScript> scripts) {
        for (AstSplitScript ss : scripts) {
            SplitSqlStatement statement = new SplitSqlStatement();
            statement.setSql(ss.getScript());
            statement.setRange(ss.toLocation());
            result.getStatements().add(statement);
        }
    }

    private static <T extends LanguageResult> T initResult(AbstractRequest request, T result) {
        if (request != null) {
            result.setRequestId(request.getRequestId());
            result.setRequestVersion(request.getRequestVersion());
        }
        return result;
    }

    private void appendTolerantStatements(SplitResult result, String sqlText, DslProvider dslProvider, CodeLocation base, SplitOptions options) {
        List<Token> tokens = filterTokens(tokenize(sqlText, dslProvider));
        if (tokens.isEmpty()) {
            return;
        }

        int previousEnd = 0;
        int statementStart = -1;
        int nestedLevel = 0;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String tokenText = token.getText();
            int tokenStart = Math.max(0, token.getStartIndex());
            if (statementStart < 0) {
                statementStart = skipWhitespace(sqlText, previousEnd, tokenStart);
            }

            if ("(".equals(tokenText)) {
                nestedLevel++;
            } else if (")".equals(tokenText) && nestedLevel > 0) {
                nestedLevel--;
            }

            SplitTerminator.Match match = options.match(tokens, i, sqlText, nestedLevel);
            if (match != null) {
                appendTolerantStatement(result, sqlText, statementStart, match.getStartIndex(), base);
                previousEnd = match.getEndIndex();
                statementStart = -1;
                nestedLevel = 0;
                i += match.getTokenCount() - 1;
            }
        }

        if (statementStart >= 0) {
            appendTolerantStatement(result, sqlText, statementStart, sqlText.length(), base);
        }
    }

    private List<Token> filterTokens(List<Token> tokens) {
        List<Token> result = new ArrayList<>();
        for (Token token : tokens) {
            if (token.getType() == Token.EOF || StringUtils.isBlank(token.getText())) {
                continue;
            }
            result.add(token);
        }
        return result;
    }

    private List<Token> tokenize(String sqlText, DslProvider dslProvider) {
        try {
            Lexer lexer = dslProvider.createLexer(CharStreams.fromReader(new StringReader(sqlText)));
            lexer.removeErrorListeners();
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            tokenStream.fill();
            return tokenStream.getTokens();
        } catch (Exception e) {
            return List.of();
        }
    }

    private void appendTolerantStatement(SplitResult result, String sqlText, int start, int end, CodeLocation base) {
        int statementStart = skipWhitespace(sqlText, start, end);
        int statementEnd = trimWhitespaceEnd(sqlText, statementStart, end);
        if (statementStart >= statementEnd) {
            return;
        }

        SplitSqlStatement statement = new SplitSqlStatement();
        statement.setSql(sqlText.substring(statementStart, statementEnd));
        statement.setRange(toRange(sqlText, statementStart, statementEnd, base));
        result.getStatements().add(statement);
    }

    private int skipWhitespace(String sqlText, int start, int end) {
        int offset = Math.max(0, start);
        int limit = Math.min(sqlText.length(), Math.max(start, end));
        while (offset < limit && Character.isWhitespace(sqlText.charAt(offset))) {
            offset++;
        }
        return offset;
    }

    private int trimWhitespaceEnd(String sqlText, int start, int end) {
        int offset = Math.min(sqlText.length(), Math.max(start, end));
        while (offset > start && Character.isWhitespace(sqlText.charAt(offset - 1))) {
            offset--;
        }
        return offset;
    }

    private BlockLocation toRange(String sqlText, int start, int end, CodeLocation base) {
        BlockLocation range = new BlockLocation();
        range.setStartPosition(toLocation(sqlText, start, base));
        range.setEndPosition(toLocation(sqlText, end, base));
        return range;
    }

    private CodeLocation toLocation(String sqlText, int offset, CodeLocation base) {
        int line = Math.max(1, base == null ? 1 : base.getLineNumber());
        int column = Math.max(0, base == null ? 0 : base.getColumnNumber());
        int limit = Math.min(sqlText.length(), Math.max(0, offset));
        for (int i = 0; i < limit; i++) {
            if (sqlText.charAt(i) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return new CodeLocation(line, column);
    }
}
