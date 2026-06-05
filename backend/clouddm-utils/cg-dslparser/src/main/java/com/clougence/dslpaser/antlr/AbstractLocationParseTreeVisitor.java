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
package com.clougence.dslpaser.antlr;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.ast.location.Location;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AbstractParseTreeVisitor}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public abstract class AbstractLocationParseTreeVisitor<T> extends AbstractParseTreeVisitor<T> {

    protected <T extends Location> T code(T location, TerminalNode context) {
        Token symbol = context.getSymbol();
        int endTokenLength = symbol.getText().length();
        location.setStartPosition(new CodeLocation(symbol.getLine(), symbol.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(symbol.getLine(), symbol.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, ParserRuleContext context) {
        Token startToken = context.start;
        Token endToken = context.stop;
        int endTokenLength = endToken.getText().length();
        location.setStartPosition(new CodeLocation(startToken.getLine(), startToken.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, ParserRuleContext start, ParserRuleContext end) {
        Token startToken = start.start;
        Token endToken = end.stop;
        int endTokenLength = endToken.getText().length();
        location.setStartPosition(new CodeLocation(startToken.getLine(), startToken.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, TerminalNode start, TerminalNode end) {
        Token startToken = start.getSymbol();
        Token endToken = end.getSymbol();
        int endTokenLength = endToken.getText().length();
        location.setStartPosition(new CodeLocation(startToken.getLine(), startToken.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, ParserRuleContext start, TerminalNode end) {
        Token startToken = start.start;
        Token endToken = end.getSymbol();
        int endTokenLength = endToken.getText().length();
        location.setStartPosition(new CodeLocation(startToken.getLine(), startToken.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, TerminalNode start, ParserRuleContext end) {
        Token startToken = start.getSymbol();
        Token endToken = end.stop;
        int endTokenLength = endToken.getText().length();
        location.setStartPosition(new CodeLocation(startToken.getLine(), startToken.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location, V extends Location> T code(T location, V other) {
        location.setStartPosition(other.getStartPosition());
        location.setEndPosition(other.getEndPosition());
        return location;
    }

    protected <T extends Location> T code(T location, List<TerminalNode> otherList) {
        Token firstTerm = otherList.get(0).getSymbol();
        Token lastTerm = otherList.get(otherList.size() - 1).getSymbol();
        int endTokenLength = lastTerm.getText().length();
        location.setStartPosition(new CodeLocation(firstTerm.getLine(), firstTerm.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(lastTerm.getLine(), lastTerm.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, Token token) {
        int endTokenLength = token.getText().length();
        location.setStartPosition(new CodeLocation(token.getLine(), token.getCharPositionInLine()));
        location.setEndPosition(new CodeLocation(token.getLine(), token.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location, V extends CodeLocation> T code(T location, V startPos, V endPos) {
        location.setStartPosition(startPos);
        location.setEndPosition(endPos);
        return location;
    }

    protected <T extends Location, V extends CodeLocation> T code(T location, V startPos, TerminalNode end) {
        Token endToken = end.getSymbol();
        int endTokenLength = end.getSymbol().getText().length();
        location.setStartPosition(startPos);
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location, V extends CodeLocation> T code(T location, V startPos, ParserRuleContext end) {
        Token endToken = end.getStop();
        int endTokenLength = endToken.getText().length();
        location.setStartPosition(startPos);
        location.setEndPosition(new CodeLocation(endToken.getLine(), endToken.getCharPositionInLine() + endTokenLength));
        return location;
    }

    protected <T extends Location> T code(T location, T startPos, T endPos) {
        location.setStartPosition(startPos.getStartPosition());
        location.setEndPosition(endPos.getEndPosition());
        return location;
    }
}
