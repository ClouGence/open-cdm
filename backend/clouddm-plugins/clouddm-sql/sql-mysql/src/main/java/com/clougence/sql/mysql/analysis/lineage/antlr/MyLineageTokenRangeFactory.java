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
package com.clougence.sql.mysql.analysis.lineage.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.analysis.lineage.model.SourceRange;

public final class MyLineageTokenRangeFactory {

    private MyLineageTokenRangeFactory(){
    }

    public static SourceRange from(ParserRuleContext context) {
        return from(context.getStart(), context.getStop());
    }

    public static SourceRange from(Token token) {
        return from(token, token);
    }

    private static SourceRange from(Token start, Token stop) {
        String stopText = stop.getText();
        int endColumn = stop.getCharPositionInLine();
        if (stopText != null) {
            endColumn += stopText.length();
        }
        return new SourceRange(start.getLine(), start.getCharPositionInLine(), stop.getLine(), endColumn);
    }
}
