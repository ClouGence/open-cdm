/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.parser;

import org.antlr.v4.runtime.Token;

/** Read-only lexical and source-offset facts available to a per-stream dialect policy. */
public interface LexerSplitContext {

    int sourceOffset();

    int sourceEndOffset();

    boolean hasContent();

    int lastContentStopOffset();

    int lastContentLine();

    int tokenStartOffset(Token token);

    int tokenEndOffset(Token token);

    String sourceText(int startOffset, int endOffset);

    boolean firstVisibleTokenOnLine(Token token);

    boolean onlyVisibleTokenOnLine(Token token);

    int physicalLineEndOffset(Token token);
}
