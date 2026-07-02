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

import com.clougence.clouddm.dsfamily.language.completion.analyzer.*;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class CompletionContext {

    private final CompletionRequest              request;
    private final CompletionDialect              dialect;
    private final List<CompletionStatementState> statementStates;
    private final CompletionStatementState       currentState;
    private final CompletionLexerState           lexerState;
    private final CompletionParseState           parseState;

    public CompletionContext(CompletionRequest request, CompletionDialect dialect){
        this.request = request;
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.statementStates = List.of(new CompletionStatementState(request
            .getSqlText(), fullRange(request.getSqlText()), request, request.getCursorLineNumber(), request.getCursorColNumber(), this.dialect, true));
        this.currentState = this.statementStates.get(0);
        this.lexerState = null;
        this.parseState = null;
    }

    public CompletionContext(CompletionRequest request, CompletionDialect dialect, List<CompletionStatementState> statementStates, CompletionLexerState lexerState,
                             CompletionParseState parseState){
        this.request = request;
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.statementStates = statementStates == null || statementStates.isEmpty() ? List.of(new CompletionStatementState(request
            .getSqlText(), fullRange(request.getSqlText()), request, request.getCursorLineNumber(), request.getCursorColNumber(), this.dialect, true)) : List
                .copyOf(statementStates);
        this.currentState = this.statementStates.stream().filter(CompletionStatementState::isCursorInState).findFirst().orElse(this.statementStates.get(0));
        this.lexerState = lexerState;
        this.parseState = parseState;
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

    public List<String> getQualifiers() {
        CompletionNamePath namePath = lexerState == null ? null : lexerState.getNamePath();
        return namePath == null || namePath.getQualifiers() == null ? List.of() : namePath.getQualifiers();
    }

    public CompletionNamePath getNamePath() { return lexerState == null ? null : lexerState.getNamePath(); }

    public String getCatalogName() {
        CompletionNamePath namePath = getNamePath();
        return namePath == null ? null : namePath.catalog();
    }

    public String getSchemaName() {
        CompletionNamePath namePath = getNamePath();
        return namePath == null ? null : namePath.schema();
    }

    public String getTableName() {
        CompletionNamePath namePath = getNamePath();
        return namePath == null ? null : namePath.table();
    }

    public String getCurrentName() {
        CompletionNamePath namePath = getNamePath();
        return namePath == null ? null : namePath.currentName();
    }

    public char getPreviousSignificantChar() { return currentState.getPreviousSignificantChar(); }

    public List<String> getTokensBeforeCursor() { return currentState.getTokensBeforeCursor(); }

    public CompletionClause getClause() { return parseState == null || parseState.getClause() == null ? CompletionClause.UNKNOWN : parseState.getClause(); }

    public boolean isInFromClause() {
        return getClause() == CompletionClause.FROM_TABLE ||   //
               getClause() == CompletionClause.JOIN_TABLE ||   //
               getClause() == CompletionClause.INSERT_TARGET ||//
               getClause() == CompletionClause.UPDATE_TARGET;
    }

    public boolean isInSelectList() { return getClause() == CompletionClause.SELECT_LIST; }

    public boolean isInPredicate() {
        return getClause() == CompletionClause.WHERE_CONDITION || getClause() == CompletionClause.JOIN_CONDITION || getClause() == CompletionClause.UPDATE_SET;
    }

    public boolean isInColumnList() { return getClause() == CompletionClause.INSERT_COLUMNS; }

    public boolean isInOrderGroupByClause() { return getClause() == CompletionClause.ORDER_BY || getClause() == CompletionClause.GROUP_BY; }

    public List<CompletionTableRef> getTableRefs() { return parseState == null || parseState.getTableRefs() == null ? List.of() : parseState.getTableRefs(); }

    public List<CompletionColumnRef> getColumnRefs() { return parseState == null || parseState.getColumnRefs() == null ? List.of() : parseState.getColumnRefs(); }

    public SecQueryType getStatementType() { return parseState == null || parseState.getStatementType() == null ? SecQueryType.UNKNOWN : parseState.getStatementType(); }

    public boolean hasSyntaxError() {
        return parseState != null && parseState.isHasSyntaxError();
    }

    public List<CompletionSyntaxError> getSyntaxErrors() { return parseState == null || parseState.getSyntaxErrors() == null ? List.of() : parseState.getSyntaxErrors(); }

    public CompletionToken getTokenBeforeCursor() { return lexerState == null ? null : lexerState.getTokenBeforeCursor(); }

    public CompletionToken getTokenAfterCursor() { return lexerState == null ? null : lexerState.getTokenAfterCursor(); }

    public CompletionToken getCurrentToken() { return lexerState == null ? null : lexerState.getCurrentToken(); }

    public List<String> getOperatorsBeforeCursor() {
        return lexerState == null || lexerState.getOperatorsBeforeCursor() == null ? List.of() : lexerState.getOperatorsBeforeCursor();
    }

    public String getOperatorBeforeCursor() { return lexerState == null ? null : lexerState.getOperatorBeforeCursor(); }

    public String getFunctionName() { return lexerState == null ? null : lexerState.getFunctionName(); }

    public int getFunctionParameterIndex() { return lexerState == null ? -1 : lexerState.getFunctionParameterIndex(); }

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
