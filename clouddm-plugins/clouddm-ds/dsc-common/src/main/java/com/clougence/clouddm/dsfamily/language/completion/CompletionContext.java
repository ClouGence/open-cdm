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
package com.clougence.clouddm.dsfamily.language.completion;

import java.util.List;
import java.util.Objects;

import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class CompletionContext {

    private final CompletionRequest              request;
    private final CompletionDialect              dialect;
    private final List<CompletionStatementState> statementStates;
    private final CompletionStatementState       currentState;

    public CompletionContext(CompletionRequest request, CompletionDialect dialect){
        this.request = request;
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.statementStates = List.of(new CompletionStatementState(request
            .getSqlText(), fullRange(request.getSqlText()), request, request.getCursorLineNumber(), request.getCursorColNumber(), this.dialect, true));
        this.currentState = this.statementStates.get(0);
    }

    private CompletionContext(CompletionRequest request, CompletionDialect dialect, List<CompletionStatementState> statementStates){
        this.request = request;
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.statementStates = List.copyOf(statementStates);
        this.currentState = this.statementStates.stream()
            .filter(CompletionStatementState::isCursorInState)
            .findFirst()
            .orElseGet(() -> new CompletionStatementState(request
                .getSqlText(), fullRange(request.getSqlText()), request, request.getCursorLineNumber(), request.getCursorColNumber(), this.dialect, true));
    }

    public static CompletionContext build(CompletionRequest request, CompletionDialect dialect, List<AstSplitScript> splitScripts) {
        if (splitScripts == null || splitScripts.isEmpty()) {
            return new CompletionContext(request, dialect);
        }

        List<CompletionStatementState> statementStates = splitScripts.stream().map(splitScript -> {
            boolean cursorInState = cursorInRange(request, splitScript.toLocation());
            Integer cursorLineNumber = cursorInState ? relativeLine(request, splitScript.toLocation()) : null;
            Integer cursorColNumber = cursorInState ? relativeColumn(request, splitScript.toLocation()) : null;
            return new CompletionStatementState(splitScript.getScript(), splitScript.toLocation(), request, cursorLineNumber, cursorColNumber, dialect, cursorInState);
        }).toList();
        return new CompletionContext(request, dialect, statementStates);
    }

    public String previousToken() {
        return currentState.previousToken();
    }

    public String tokenFromEnd(int index) {
        return currentState.tokenFromEnd(index);
    }

    public boolean matchPrefix(String value) {
        return currentState.matchPrefix(value);
    }

    public boolean hasQualifier() {
        return currentState.hasQualifier();
    }

    public List<String> tokenize(String text) {
        return CompletionStatementState.tokenize(text, this.dialect);
    }

    public String getSqlText() { return currentState.getSqlText(); }

    public int getCursorOffset() { return currentState.getCursorOffset(); }

    public String getPrefix() { return currentState.getPrefix(); }

    public String getQualifier() { return currentState.getQualifier(); }

    public char getPreviousSignificantChar() { return currentState.getPreviousSignificantChar(); }

    public List<String> getTokensBeforeCursor() { return currentState.getTokensBeforeCursor(); }

    private static boolean cursorInRange(CompletionRequest request, BlockLocation range) {
        if (request.getCursorLineNumber() == null || request.getCursorColNumber() == null || range == null) {
            return false;
        }

        CodeLocation start = range.getStartPosition();
        CodeLocation end = range.getEndPosition();
        int cursorLine = request.getCursorLineNumber();
        int cursorColumn = request.getCursorColNumber();
        if (cursorLine < start.getLineNumber() || cursorLine > end.getLineNumber()) {
            return false;
        }
        if (cursorLine == start.getLineNumber() && cursorColumn < start.getColumnNumber()) {
            return false;
        }
        return cursorLine != end.getLineNumber() || cursorColumn <= end.getColumnNumber();
    }

    private static int relativeLine(CompletionRequest request, BlockLocation range) {
        return request.getCursorLineNumber() - range.getStartPosition().getLineNumber() + 1;
    }

    private static int relativeColumn(CompletionRequest request, BlockLocation range) {
        return request.getCursorLineNumber().equals(range.getStartPosition().getLineNumber()) ? //
            request.getCursorColNumber() - range.getStartPosition().getColumnNumber() : request.getCursorColNumber();
    }

    private static BlockLocation fullRange(String sqlText) {
        BlockLocation range = new BlockLocation();
        range.setStartPosition(new CodeLocation(1, 0));
        range.setEndPosition(positionAtEnd(sqlText));
        return range;
    }

    private static CodeLocation positionAtEnd(String sqlText) {
        String text = StringUtils.toString(sqlText);
        int line = 1;
        int column = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                line++;
                column = 0;
            } else if (text.charAt(i) != '\r') {
                column++;
            }
        }
        return new CodeLocation(line, column);
    }
}
