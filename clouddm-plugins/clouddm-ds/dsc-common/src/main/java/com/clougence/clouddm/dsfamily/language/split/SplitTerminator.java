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

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

public class SplitTerminator {

    private final List<String> tokenTexts;
    private final boolean      lineAlone;

    private SplitTerminator(List<String> tokenTexts, boolean lineAlone){
        this.tokenTexts = List.copyOf(tokenTexts);
        this.lineAlone = lineAlone;
    }

    public static SplitTerminator token(String tokenText) {
        return tokens(tokenText);
    }

    public static SplitTerminator tokens(String... tokenTexts) {
        return new SplitTerminator(Arrays.asList(tokenTexts), false);
    }

    public static SplitTerminator lineAlone(String tokenText) {
        return lineAloneTokens(tokenText);
    }

    public static SplitTerminator lineAloneTokens(String... tokenTexts) {
        return new SplitTerminator(Arrays.asList(tokenTexts), true);
    }

    Match match(List<Token> tokens, int tokenIndex, String sqlText, int nestedLevel) {
        if (nestedLevel != 0 || tokenIndex + this.tokenTexts.size() > tokens.size()) {
            return null;
        }

        for (int i = 0; i < this.tokenTexts.size(); i++) {
            String expected = this.tokenTexts.get(i);
            String actual = tokens.get(tokenIndex + i).getText();
            if (!expected.equalsIgnoreCase(actual)) {
                return null;
            }
        }

        Token startToken = tokens.get(tokenIndex);
        Token stopToken = tokens.get(tokenIndex + this.tokenTexts.size() - 1);
        int start = Math.max(0, startToken.getStartIndex());
        int end = Math.max(start, stopToken.getStopIndex() + 1);
        if (this.lineAlone && !isLineAlone(sqlText, start, end)) {
            return null;
        }
        return new Match(this.tokenTexts.size(), start, end);
    }

    private boolean isLineAlone(String sqlText, int start, int end) {
        for (int i = start - 1; i >= 0 && sqlText.charAt(i) != '\n' && sqlText.charAt(i) != '\r'; i--) {
            if (!Character.isWhitespace(sqlText.charAt(i))) {
                return false;
            }
        }
        for (int i = end; i < sqlText.length() && sqlText.charAt(i) != '\n' && sqlText.charAt(i) != '\r'; i++) {
            if (!Character.isWhitespace(sqlText.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static class Match {
        private final int tokenCount;
        private final int startIndex;
        private final int endIndex;

        Match(int tokenCount, int startIndex, int endIndex){
            this.tokenCount = tokenCount;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        int getTokenCount() { return this.tokenCount; }

        int getStartIndex() { return this.startIndex; }

        int getEndIndex() { return this.endIndex; }
    }
}
