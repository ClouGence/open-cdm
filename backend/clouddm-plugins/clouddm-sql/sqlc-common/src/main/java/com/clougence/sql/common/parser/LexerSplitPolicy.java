/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.parser;

import org.antlr.v4.runtime.Token;

/** Per-stream dialect policy. Implementations must not be shared between split streams. */
public interface LexerSplitPolicy {

    default boolean isContentToken(Token token) {
        return token.getChannel() == Token.DEFAULT_CHANNEL && token.getText() != null && !token.getText().isBlank();
    }

    default int leadingTriviaDiscardLength(String trivia) {
        return 0;
    }

    /** Returns {@code null} when the current token does not complete a boundary. */
    LexerSplitBoundary boundary(Token token, LexerSplitContext context);

    /** Called after an emitted/discarded boundary and at stream close. */
    default void reset() {
    }
}
