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
import java.util.Locale;

import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.parser.MyDslProvider;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.AbstractResult;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
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
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class MyLanguageSpi implements DsLanguageSpi {

    @Override
    public String name() {
        return "mysql";
    }

    private static <T extends AbstractResult> T initResult(AbstractRequest request, T result) {
        if (request != null) {
            result.setRequestId(request.getRequestId());
            result.setRequestVersion(request.getRequestVersion());
        }
        return result;
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = initResult(request, new CompletionResult());
        String prefix = extractPrefix(request);
        for (String keyword : MySqlDialect.INSTANCE.keywords()) {
            if (StringUtils.isNotBlank(prefix) && !keyword.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT))) {
                continue;
            }

            CompletionItem item = new CompletionItem();
            item.setLabel(keyword);
            item.setKind(CompletionItemKind.KEYWORD);
            item.setInsertText(keyword);
            item.setDetail("MySQL KeyWords");
            item.setSortText(keyword);
            result.getItems().add(item);
        }
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
            diagnostic.setRange(errorRange(e));
            result.getDiagnostics().add(diagnostic);
        }
        return result;
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

    private static BlockLocation errorRange(AntlerSyntaxException e) {
        CodeLocation start = new CodeLocation(e.getLine(), e.getColumn());
        CodeLocation end = new CodeLocation(e.getLine(), e.getColumn() + 1);
        BlockLocation range = new BlockLocation();
        range.setStartPosition(start);
        range.setEndPosition(end);
        return range;
    }

    private static String syntaxErrorMessage(AntlerSyntaxException e) {
        if (StringUtils.isNotBlank(e.getMessage())) {
            return e.getMessage();
        }
        return "SQL syntax error at line " + e.getLine() + ", column " + e.getColumn();
    }

    private static String extractPrefix(CompletionRequest request) {
        if (request == null || StringUtils.isBlank(request.getSqlText()) || request.getPosition() == null) {
            return "";
        }

        int offset = offsetOf(request.getSqlText(), request.getPosition());
        if (offset <= 0) {
            return "";
        }

        int start = offset;
        while (start > 0) {
            char c = request.getSqlText().charAt(start - 1);
            if (Character.isLetterOrDigit(c) || c == '_') {
                start--;
            } else {
                break;
            }
        }
        return request.getSqlText().substring(start, offset);
    }

    private static int offsetOf(String sqlText, CodeLocation position) {
        int lineNumber = Math.max(1, position.getLineNumber());
        int columnNumber = Math.max(1, position.getColumnNumber());
        int line = 1;
        int column = 1;
        for (int i = 0; i < sqlText.length(); i++) {
            if (line == lineNumber && column == columnNumber) {
                return i;
            }
            char c = sqlText.charAt(i);
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        return sqlText.length();
    }
}
