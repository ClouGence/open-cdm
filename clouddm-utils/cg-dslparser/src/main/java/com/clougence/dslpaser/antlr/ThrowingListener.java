package com.clougence.dslpaser.antlr;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ThrowingListener extends BaseErrorListener {

    public static final ThrowingListener INSTANCE = new ThrowingListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new AntlerSyntaxException(line, charPositionInLine, msg);
    }
}
