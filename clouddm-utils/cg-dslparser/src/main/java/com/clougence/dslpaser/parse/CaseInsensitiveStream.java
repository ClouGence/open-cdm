package com.clougence.dslpaser.parse;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Interval;

public class CaseInsensitiveStream implements CharStream {

    private final CharStream charStream;

    public CaseInsensitiveStream(CharStream charStream){
        this.charStream = charStream;
    }

    @Override
    public String getText(Interval interval) {
        return this.charStream.getText(interval);

    }

    @Override
    public void consume() {
        this.charStream.consume();
    }

    @Override
    public int LA(int i) {
        int result = charStream.LA(i);

        switch (result) {
            case 0:
            case IntStream.EOF:
                return result;
            default:
                return Character.toUpperCase(result);
        }
    }

    @Override
    public int mark() {
        return this.charStream.mark();
    }

    @Override
    public void release(int i) {
        this.charStream.release(i);
    }

    @Override
    public int index() {
        return this.charStream.index();
    }

    @Override
    public void seek(int i) {
        this.charStream.seek(i);
    }

    @Override
    public int size() {
        return this.charStream.size();
    }

    @Override
    public String getSourceName() { return this.charStream.getSourceName(); }
}
