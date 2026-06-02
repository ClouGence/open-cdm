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
package com.clougence.clouddm.dsfamily.mysql.language;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.language.completion.MyCompletionStrategyCenter;
import com.clougence.clouddm.dsfamily.mysql.parser.MyDslProvider;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.split.SplitSqlStatement;
import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.DiagnosticSeverity;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class MyLanguageSpi implements DsLanguageSpi {

    private final MetaService                metaService;
    private final MyCompletionStrategyCenter completionStrategyCenter = new MyCompletionStrategyCenter();

    public MyLanguageSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public String name() {
        return "mysql";
    }

    private static <T extends LanguageResult> T initResult(AbstractRequest request, T result) {
        if (request != null) {
            result.setRequestId(request.getRequestId());
            result.setRequestVersion(request.getRequestVersion());
        }

        return result;
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = initResult(request, new CompletionResult());
        result.getItems().addAll(completionStrategyCenter.complete(request, this.metaService));
        return result;
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = initResult(request, new ValidateResult());
        try {
            splitScripts(request.getSqlText());
        } catch (AntlerSyntaxException e) {
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setSeverity(DiagnosticSeverity.ERROR);
            diagnostic.setMessage(syntaxErrorMessage(e));
            diagnostic.setRange(errorRange(e, request.getSqlText()));
            result.getDiagnostics().add(diagnostic);
        }
        return result;
    }

    private static String syntaxErrorMessage(AntlerSyntaxException e) {
        if (StringUtils.isNotBlank(e.getMessage())) {
            return e.getMessage();
        }

        return "SQL syntax error at line " + e.getLine() + ", column " + e.getColumn();
    }

    @Override
    public SplitResult split(SplitRequest request) {
        SplitResult result = initResult(request, new SplitResult());
        for (AstSplitScript splitScript : splitScripts(request.getSqlText())) {
            SplitSqlStatement statement = new SplitSqlStatement();
            statement.setSql(splitScript.getScript());
            statement.setRange(splitScript.toLocation());
            result.getStatements().add(statement);
        }
        return result;
    }

    private static List<AstSplitScript> splitScripts(String sqlText) {
        if (StringUtils.isBlank(sqlText)) {
            return Collections.emptyList();
        }
        return DslHelper.splitDsl(MyDslProvider.INSTANCE, sqlText, new CodeLocation(1, 0));
    }

    private static BlockLocation errorRange(AntlerSyntaxException e, String sqlText) {
        String token = offendingToken(e.getMessage());
        int startColumn = e.getColumn();
        int endColumn = startColumn + Math.max(1, token == null ? 1 : token.length());

        String lineText = lineText(sqlText, e.getLine());
        if (StringUtils.isNotBlank(lineText)) {
            int[] tokenRange = tokenRange(lineText, startColumn, token);
            startColumn = tokenRange[0];
            endColumn = tokenRange[1];
        }

        CodeLocation start = new CodeLocation(e.getLine(), startColumn);
        CodeLocation end = new CodeLocation(e.getLine(), endColumn);
        BlockLocation range = new BlockLocation();
        range.setStartPosition(start);
        range.setEndPosition(end);
        return range;
    }

    private static String offendingToken(String message) {
        if (StringUtils.isBlank(message)) {
            return null;
        }

        String[] prefixes = { "mismatched input '", "extraneous input '" };
        for (String prefix : prefixes) {
            int start = message.indexOf(prefix);
            if (start < 0) {
                continue;
            }

            start += prefix.length();
            int end = message.indexOf('\'', start);
            if (end > start) {
                return message.substring(start, end);
            }
        }

        int atIndex = message.lastIndexOf(" at '");
        if (atIndex >= 0) {
            int start = atIndex + " at '".length();
            int end = message.indexOf('\'', start);
            if (end > start) {
                return message.substring(start, end);
            }
        }
        return null;
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

    private static int[] tokenRange(String lineText, int column, String token) {
        if (StringUtils.isNotBlank(token)) {
            int fromIndex = Math.clamp(column, 0, lineText.length());
            int tokenStart = lineText.indexOf(token, fromIndex);
            if (tokenStart < 0) {
                tokenStart = lineText.indexOf(token);
            }
            if (tokenStart >= 0) {
                return new int[] { tokenStart, tokenStart + token.length() };
            }
        }

        int start = Math.clamp(column, 0, lineText.length());
        while (start > 0 && isTokenChar(lineText.charAt(start - 1))) {
            start--;
        }

        int end = Math.clamp(column, 0, lineText.length());
        while (end < lineText.length() && isTokenChar(lineText.charAt(end))) {
            end++;
        }

        if (end <= start) {
            end = Math.min(lineText.length(), start + 1);
        }
        return new int[] { start, end };
    }

    private static boolean isTokenChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$';
    }
}
