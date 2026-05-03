package com.clougence.dslpaser.parse;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.clougence.dslpaser.antlr.AntlerSyntaxException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyntaxErrorListener extends BaseErrorListener {

    public static SyntaxErrorListener INSTANCE = new SyntaxErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new AntlerSyntaxException(line, charPositionInLine, msg);
    }
}
