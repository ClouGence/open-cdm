/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.WritableToken;

import com.clougence.clouddm.ds.hana.sql.parser.antlr.HanaLexer;

/**
 * HANA lexer with SQLScript boundary hints for the shared splitter.
 *
 * <p>A fixture whose first visible construct is {@code DO BEGIN} is one anonymous SQLScript
 * statement. Its internal and terminal semicolons stay lexical content so trailing source
 * comments remain attached through EOF. A {@code DO BEGIN} sequence encountered after another
 * construct has started cannot retroactively turn that construct into an anonymous block; those
 * two words are hidden from the shared block tracker while remaining present in the source text.</p>
 */
final class HanaSplitLexer extends HanaLexer {

    private int     visibleWords;
    private boolean firstWordDo;
    private boolean statementAction;
    private boolean anonymousBlock;
    private boolean programDefinition;
    private boolean midStatementDo;

    HanaSplitLexer(CharStream input){
        super(input);
    }

    @Override
    public Token nextToken() {
        Token token = super.nextToken();
        if (token.getType() == WORD && token.getChannel() == Token.DEFAULT_CHANNEL) {
            String word = token.getText();
            if (this.visibleWords == 0) {
                this.firstWordDo = "DO".equalsIgnoreCase(word);
                this.statementAction = "CREATE".equalsIgnoreCase(word) || "ALTER".equalsIgnoreCase(word);
            } else if (this.visibleWords == 1 && this.firstWordDo) {
                this.anonymousBlock = "BEGIN".equalsIgnoreCase(word);
            } else if (this.statementAction && this.visibleWords < 6 && isProgramObject(word)) {
                this.programDefinition = true;
            } else if (!this.anonymousBlock && "DO".equalsIgnoreCase(word)) {
                this.midStatementDo = true;
                hide(token);
            } else if (this.midStatementDo) {
                if ("BEGIN".equalsIgnoreCase(word)) {
                    hide(token);
                }
                this.midStatementDo = false;
            }
            this.visibleWords++;
        } else if (token.getType() == SEMICOLON && (this.anonymousBlock || this.programDefinition)) {
            hide(token);
        }
        return token;
    }

    private boolean isProgramObject(String word) {
        return "PROCEDURE".equalsIgnoreCase(word) || "FUNCTION".equalsIgnoreCase(word) || "TRIGGER".equalsIgnoreCase(word)
               || "PACKAGE".equalsIgnoreCase(word);
    }

    private void hide(Token token) {
        ((WritableToken) token).setChannel(Token.HIDDEN_CHANNEL);
    }
}
