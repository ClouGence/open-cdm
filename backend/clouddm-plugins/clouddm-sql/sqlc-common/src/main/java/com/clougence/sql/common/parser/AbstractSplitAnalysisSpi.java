/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.parser;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.sql.common.parser.perf.ParserPerfCollector;
import com.clougence.sql.common.parser.perf.ParserPerfCollectors;
import com.clougence.sql.common.parser.perf.ParserPerfRecorder;

/**
 * Streaming infrastructure for a datasource-owned lexer split policy.
 *
 * <p>This class deliberately knows nothing about SQL tokens, keywords, delimiters, comments, or
 * procedural blocks. Each split stream receives a fresh {@link LexerSplitPolicy}; only that
 * datasource policy decides which lexer tokens form a statement boundary.</p>
 */
public abstract class AbstractSplitAnalysisSpi implements SplitAnalysisSpi {

    private final ParserPerfCollector performanceCollector = ParserPerfCollectors.create();

    protected DslProvider dslProvider() {
        throw new UnsupportedOperationException("This splitter creates its lexer directly");
    }

    protected Lexer createLexer(CharStream source) {
        return dslProvider().createLexer(source);
    }

    /** Datasource/dialect dimension used by the lexer performance monitor. */
    protected String performanceDialect() {
        String[] names = dslProvider().getDslName();
        if (names.length == 0 || names[0].isBlank()) {
            return getClass().getName();
        }
        return names[0];
    }

    /** Returns a new, unshared policy for one split stream. */
    protected abstract LexerSplitPolicy createSplitPolicy();

    protected void beforeSplitStream() {
    }

    protected void afterSplitStream() {
    }

    @Override
    public Stream<SplitScript> splitScriptStream(Reader reader, List<QueryArg> args, int baseLine, int baseColumn) {
        Objects.requireNonNull(reader, "reader");
        LexerSplit split = new LexerSplit(reader, args, baseLine, baseColumn);
        return StreamSupport.stream(split, false).onClose(split::close);
    }

    private final class LexerSplit extends Spliterators.AbstractSpliterator<SplitScript> implements AutoCloseable, LexerSplitContext {

        private final WindowedReader      source;
        private final Lexer               lexer;
        private final LexerSplitPolicy    policy;
        private final ParserPerfRecorder  performance;
        private final List<QueryArg>      args;
        private final AtomicBoolean       closed          = new AtomicBoolean();
        private final Deque<PendingToken> pending         = new ArrayDeque<>();
        private Token                     offsetToken;
        private TokenOffsets              offsetValue;
        private int                       sourceOffset;
        private int                       line;
        private int                       column;
        private int                       lastContentStop = -1;
        private int                       lastContentLine = -1;
        private int                       lastVisibleLine = -1;
        private boolean                   hasContent;
        private boolean                   eof;

        private LexerSplit(Reader reader, List<QueryArg> args, int baseLine, int baseColumn){
            super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
            beforeSplitStream();
            this.args = args;
            this.line = Math.max(1, baseLine);
            this.column = Math.max(0, baseColumn);
            boolean performanceEnabled = AbstractSplitAnalysisSpi.this.performanceCollector.enabled();
            this.source = new WindowedReader(new NonClosingReader(reader), performanceEnabled);
            this.lexer = createLexer(new UnbufferedCharStream(this.source));
            this.performance = performanceEnabled ? ParserPerfRecorder
                .begin(AbstractSplitAnalysisSpi.this.performanceCollector, performanceDialect(), this.lexer.getClass()) : null;
            isolatePredictionCache(this.lexer);
            this.lexer.setTokenFactory(new CommonTokenFactory(true));
            this.lexer.removeErrorListeners();
            this.lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
            this.policy = Objects.requireNonNull(createSplitPolicy(), "split policy");
        }

        /** Generated lexers otherwise share mutable static DFA state across worker threads. */
        private void isolatePredictionCache(Lexer lexer) {
            ATN atn = lexer.getATN();
            DFA[] decisionToDfa = new DFA[atn.getNumberOfDecisions()];
            for (int index = 0; index < decisionToDfa.length; index++) {
                decisionToDfa[index] = new DFA(atn.getDecisionState(index), index);
            }
            lexer.setInterpreter(new LexerATNSimulator(lexer, atn, decisionToDfa, new PredictionContextCache()));
        }

        @Override
        public boolean tryAdvance(Consumer<? super SplitScript> action) {
            Objects.requireNonNull(action, "action");
            if (this.eof || this.closed.get()) {
                return false;
            }
            while (true) {
                Token token = nextToken();
                resolveOffsets(token);
                if (token.getType() == Token.EOF) {
                    this.eof = true;
                    SplitScript tail = emit(this.source.endOffset());
                    close();
                    if (tail != null) {
                        action.accept(tail);
                        return true;
                    }
                    return false;
                }

                LexerSplitBoundary boundary = this.policy.boundary(token, this);
                if (boundary != null) {
                    SplitScript statement = emit(boundary.bodyEndOffset());
                    skip(boundary.resumeOffset());
                    discardConsumedPending(boundary.resumeOffset());
                    resetStatement();
                    if (boundary.reprocessToken()) {
                        this.pending.addFirst(new PendingToken(token, resolveOffsets(token)));
                    } else if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                        this.lastVisibleLine = token.getLine();
                    }
                    if (statement != null) {
                        action.accept(statement);
                        return true;
                    }
                    continue;
                }

                acceptToken(token);
                if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                    this.lastVisibleLine = token.getLine();
                }
            }
        }

        private Token nextToken() {
            if (this.pending.isEmpty()) {
                if (this.performance == null) {
                    return this.lexer.nextToken();
                }
                long startedNanos = System.nanoTime();
                try {
                    Token token = this.lexer.nextToken();
                    long elapsedNanos = System.nanoTime() - startedNanos;
                    if (token.getType() == Token.EOF) {
                        this.performance.addLexerTime(elapsedNanos);
                    } else {
                        this.performance.addToken(elapsedNanos);
                    }
                    return token;
                } catch (RuntimeException | Error e) {
                    this.performance.addLexerTime(System.nanoTime() - startedNanos);
                    throw e;
                }
            }
            PendingToken pendingToken = this.pending.removeFirst();
            this.offsetToken = pendingToken.token();
            this.offsetValue = pendingToken.offsets();
            return pendingToken.token();
        }

        private void discardConsumedPending(int resumeOffset) {
            while (!this.pending.isEmpty() && this.pending.peekFirst().offsets().end() <= resumeOffset) {
                this.pending.removeFirst();
            }
        }

        private void acceptToken(Token token) {
            if (!this.policy.isContentToken(token)) {
                return;
            }
            TokenOffsets offsets = resolveOffsets(token);
            if (!this.hasContent && offsets.start() > this.sourceOffset) {
                String trivia = this.source.getText(this.sourceOffset, offsets.start());
                int discard = Math.max(0, Math.min(trivia.length(), this.policy.leadingTriviaDiscardLength(trivia)));
                skip(this.sourceOffset + discard);
            }
            this.hasContent = true;
            this.lastContentStop = offsets.end();
            this.lastContentLine = token.getLine();
        }

        private SplitScript emit(int endOffset) {
            if (endOffset <= this.sourceOffset) {
                return null;
            }
            String raw = this.source.getText(this.sourceOffset, endOffset);
            int start = 0;
            while (start < raw.length() && Character.isWhitespace(raw.charAt(start))) {
                start++;
            }
            int end = raw.length();
            while (end > start && Character.isWhitespace(raw.charAt(end - 1))) {
                end--;
            }

            advance(raw, 0, start);
            int startLine = this.line;
            int startColumn = this.column;
            String script = raw.substring(start, end);
            advance(raw, start, end);
            int endLine = this.line;
            int endColumn = this.column;
            advance(raw, end, raw.length());
            this.sourceOffset = endOffset;
            this.source.discardBefore(endOffset);

            if (script.isBlank() || !this.hasContent) {
                return null;
            }
            SplitScript split = new SplitScript();
            split.setScript(script);
            split.setScriptArgs(this.args);
            split.setBodyStartCodeLine(startLine);
            split.setBodyStartCodeColumn(startColumn);
            split.setBodyEndCodeLine(endLine);
            split.setBodyEndCodeColumn(endColumn);
            if (this.performance != null) {
                this.performance.addStatement();
            }
            return split;
        }

        private void skip(int endOffset) {
            if (endOffset <= this.sourceOffset) {
                return;
            }
            String raw = this.source.getText(this.sourceOffset, endOffset);
            advance(raw, 0, raw.length());
            this.sourceOffset = endOffset;
            this.source.discardBefore(endOffset);
        }

        private void resetStatement() {
            this.hasContent = false;
            this.lastContentStop = -1;
            this.lastContentLine = -1;
            this.policy.reset();
        }

        private void advance(String value, int start, int end) {
            for (int index = start; index < end; index++) {
                if (value.charAt(index) == '\n') {
                    this.line++;
                    this.column = 0;
                } else {
                    this.column++;
                }
            }
        }

        private TokenOffsets resolveOffsets(Token token) {
            if (token == this.offsetToken) {
                return this.offsetValue;
            }
            if (token.getType() == Token.EOF) {
                TokenOffsets offsets = new TokenOffsets(this.source.endOffset(), this.source.endOffset());
                this.offsetToken = token;
                this.offsetValue = offsets;
                return offsets;
            }
            int start;
            int end;
            try {
                start = this.source.utf16OffsetForCodePoint(token.getStartIndex());
                end = this.source.utf16OffsetForCodePoint(token.getStopIndex() + 1);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Lexer token offset is unavailable: type=" + token.getType() + ", start=" + token.getStartIndex() + ", stop=" + token.getStopIndex()
                                                + ", windowUtf16=" + this.source.startOffset() + ".." + this.source.endOffset() + ", windowCodePoints="
                                                + this.source.startCodePointOffset() + ".." + this.source.endCodePointOffset(),
                    e);
            }
            TokenOffsets offsets = new TokenOffsets(start, end);
            this.offsetToken = token;
            this.offsetValue = offsets;
            return offsets;
        }

        @Override
        public int sourceOffset() {
            return this.sourceOffset;
        }

        @Override
        public int sourceEndOffset() {
            return this.source.endOffset();
        }

        @Override
        public boolean hasContent() {
            return this.hasContent;
        }

        @Override
        public int lastContentStopOffset() {
            return this.lastContentStop;
        }

        @Override
        public int lastContentLine() {
            return this.lastContentLine;
        }

        @Override
        public int tokenStartOffset(Token token) {
            return resolveOffsets(token).start();
        }

        @Override
        public int tokenEndOffset(Token token) {
            return resolveOffsets(token).end();
        }

        @Override
        public String sourceText(int startOffset, int endOffset) {
            return this.source.getText(startOffset, endOffset);
        }

        @Override
        public boolean firstVisibleTokenOnLine(Token token) {
            return token.getChannel() == Token.DEFAULT_CHANNEL && token.getLine() > this.lastVisibleLine;
        }

        @Override
        public boolean onlyVisibleTokenOnLine(Token token) {
            if (!firstVisibleTokenOnLine(token)) {
                return false;
            }
            while (true) {
                Token next = this.lexer.nextToken();
                TokenOffsets offsets = resolveOffsets(next);
                this.pending.addLast(new PendingToken(next, offsets));
                if (next.getType() == Token.EOF || next.getChannel() == Token.DEFAULT_CHANNEL) {
                    return next.getType() == Token.EOF || next.getLine() > token.getLine();
                }
            }
        }

        @Override
        public int physicalLineEndOffset(Token token) {
            int start = tokenEndOffset(token);
            String available = this.source.getText(start, this.source.endOffset());
            int newline = available.indexOf('\n');
            return newline < 0 ? this.source.endOffset() : start + newline + 1;
        }

        @Override
        public void close() {
            if (this.closed.compareAndSet(false, true)) {
                this.policy.reset();
                if (this.performance != null) {
                    this.performance.input(this.source.totalChars(), this.source.totalUtf8Bytes());
                    this.performance.close();
                }
                afterSplitStream();
            }
        }
    }

    private record TokenOffsets(int start, int end) {
    }

    private record PendingToken(Token token, TokenOffsets offsets) {
    }

    private static final class WindowedReader extends FilterReader {

        private final StringBuilder window = new StringBuilder();
        private final boolean       countPerformance;
        private int                 startOffset;
        private int                 startCodePointOffset;
        private int                 mappedOffset;
        private int                 mappedCodePointOffset;
        private long                totalChars;
        private long                totalUtf8Bytes;
        private boolean             pendingHighSurrogate;

        private WindowedReader(Reader reader, boolean countPerformance){
            super(reader);
            this.countPerformance = countPerformance;
        }

        @Override
        public int read() throws IOException {
            int value = super.read();
            if (value >= 0) {
                this.window.append((char) value);
                countInput((char) value);
            } else {
                finishByteCount();
            }
            return value;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int read = super.read(cbuf, off, len);
            if (read > 0) {
                this.window.append(cbuf, off, read);
                for (int index = off; index < off + read; index++) {
                    countInput(cbuf[index]);
                }
            } else if (read < 0) {
                finishByteCount();
            }
            return read;
        }

        private void countInput(char value) {
            if (!this.countPerformance) {
                return;
            }
            this.totalChars++;
            if (this.pendingHighSurrogate) {
                if (Character.isLowSurrogate(value)) {
                    this.totalUtf8Bytes += 4;
                    this.pendingHighSurrogate = false;
                    return;
                }
                this.totalUtf8Bytes++;
                this.pendingHighSurrogate = false;
            }
            if (Character.isHighSurrogate(value)) {
                this.pendingHighSurrogate = true;
            } else if (value <= 0x7f) {
                this.totalUtf8Bytes++;
            } else if (value <= 0x7ff) {
                this.totalUtf8Bytes += 2;
            } else if (Character.isLowSurrogate(value)) {
                this.totalUtf8Bytes++;
            } else {
                this.totalUtf8Bytes += 3;
            }
        }

        private void finishByteCount() {
            if (this.pendingHighSurrogate) {
                this.totalUtf8Bytes++;
                this.pendingHighSurrogate = false;
            }
        }

        private long totalChars() {
            return this.totalChars;
        }

        private long totalUtf8Bytes() {
            finishByteCount();
            return this.totalUtf8Bytes;
        }

        private int startOffset() {
            return this.startOffset;
        }

        private int endOffset() {
            return this.startOffset + this.window.length();
        }

        private int startCodePointOffset() {
            return this.startCodePointOffset;
        }

        private int endCodePointOffset() {
            return this.startCodePointOffset + this.window.codePointCount(0, this.window.length());
        }

        private int utf16OffsetForCodePoint(int codePointOffset) {
            if (codePointOffset < this.startCodePointOffset) {
                throw new IllegalStateException("Lexer token offset is unavailable in the split source window");
            }
            int baseCodePoint = this.mappedCodePointOffset;
            int baseOffset = this.mappedOffset;
            if (codePointOffset < baseCodePoint || baseOffset < this.startOffset) {
                baseCodePoint = this.startCodePointOffset;
                baseOffset = this.startOffset;
            }
            try {
                int relativeBase = baseOffset - this.startOffset;
                int mappedRelative = this.window.offsetByCodePoints(relativeBase, codePointOffset - baseCodePoint);
                this.mappedCodePointOffset = codePointOffset;
                this.mappedOffset = this.startOffset + mappedRelative;
                return this.mappedOffset;
            } catch (IndexOutOfBoundsException error) {
                throw new IllegalStateException("Lexer token offset is unavailable in the split source window", error);
            }
        }

        private String getText(int start, int end) {
            if (start < this.startOffset || end > endOffset() || start > end) {
                throw new IllegalStateException("Split source window is unavailable");
            }
            return this.window.substring(start - this.startOffset, end - this.startOffset);
        }

        private void discardBefore(int offset) {
            int discard = Math.min(Math.max(0, offset - this.startOffset), this.window.length());
            if (discard > 0) {
                this.startCodePointOffset += this.window.codePointCount(0, discard);
                this.window.delete(0, discard);
                this.startOffset += discard;
                if (this.mappedOffset < this.startOffset) {
                    this.mappedOffset = this.startOffset;
                    this.mappedCodePointOffset = this.startCodePointOffset;
                }
            }
        }
    }

    private static final class NonClosingReader extends FilterReader {

        private NonClosingReader(Reader reader){
            super(reader);
        }

        @Override
        public void close() {
            // The SplitAnalysisSpi caller owns the supplied reader.
        }
    }
}
