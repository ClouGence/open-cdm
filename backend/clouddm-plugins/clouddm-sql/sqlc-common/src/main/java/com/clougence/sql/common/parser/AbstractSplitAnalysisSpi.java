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
package com.clougence.sql.common.parser;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;

public abstract class AbstractSplitAnalysisSpi implements SplitAnalysisSpi {

    private static final AtomicLong STREAM_SEQUENCE = new AtomicLong();

    protected abstract DslProvider dslProvider();

    protected abstract AbstractParseTreeVisitor<SplitQueryType> splitVisitor();

    protected abstract void parseRoot(Parser parser);

    protected abstract boolean isStatementContext(ParserRuleContext context);

    protected abstract AntlrStatementParser statementParser();

    protected DslProvider fallbackDslProvider() {
        return null;
    }

    protected boolean isStatementTerminated(Parser parser) {
        return true;
    }

    protected void beforeSplitStream() {
    }

    protected void afterSplitStream() {
    }

    //

    protected SplitQueryType normalizeType(SplitQueryType type) {
        return type == null ? SplitQueryType.UNKNOWN : type;
    }

    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(normalizeType(context.accept(splitVisitor())));
        collectAdditionalTypes(context, types);
        return types;
    }

    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        return Collections.emptyList();
    }

    protected final SplitScript createChild(ParserRuleContext context, CommonTokenStream tokens, Set<SplitQueryType> types, List<SplitScript> children) {
        String script = tokens.getText(context.getStart(), context.getStop());
        SplitScript split = new SplitScript();
        split.setScript(script);
        split.setType(types);
        split.setChildren(children);
        split.setBodyStartCodeLine(context.getStart().getLine());
        split.setBodyStartCodeColumn(context.getStart().getCharPositionInLine());

        int endLine = context.getStart().getLine();
        int endColumn = context.getStart().getCharPositionInLine();
        for (int i = 0; i < script.length(); i++) {
            if (script.charAt(i) == '\n') {
                endLine++;
                endColumn = 0;
            } else {
                endColumn++;
            }
        }
        split.setBodyEndCodeLine(endLine);
        split.setBodyEndCodeColumn(endColumn);
        return split;
    }

    protected SplitQueryType additionalType(ParseTree tree) {
        return null;
    }

    private void collectAdditionalTypes(ParseTree tree, Set<SplitQueryType> types) {
        SplitQueryType type = additionalType(tree);
        if (type != null) {
            types.add(type);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectAdditionalTypes(tree.getChild(i), types);
        }
    }

    @Override
    public Stream<SplitScript> splitScriptStream(Reader reader, List<QueryArg> args, int baseLine, int baseColumn) {
        StreamingSplit streamingSplit = new StreamingSplit(reader, baseLine, baseColumn);
        return StreamSupport.stream(streamingSplit, false).onClose(streamingSplit::close);
    }

    private void streamingSplit(Reader reader, int baseLine, int baseColumn, Consumer<SplitScript> resultConsumer) {
        WindowedReader sourceReader = new WindowedReader(new NonClosingReader(reader));
        LocationCursor location = new LocationCursor(sourceReader, new CodeLocation(baseLine, baseColumn));
        try {
            parseStream(dslProvider(), sourceReader, location, resultConsumer);
        } catch (AntlerSyntaxException firstFailure) {
            DslProvider fallback = fallbackDslProvider();
            if (fallback == null) {
                throw firstFailure;
            }
            // The window starts after the last emitted SQL and includes lexer read-ahead.
            // Replay only this suffix so consumers never receive the same statement twice.
            sourceReader.rewind();
            LocationCursor retryLocation = new LocationCursor(sourceReader, new CodeLocation(location.line, location.column));
            retryLocation.statementIndex = location.statementIndex;
            try {
                parseStream(fallback, sourceReader, retryLocation, resultConsumer);
            } catch (AntlerSyntaxException fallbackFailure) {
                firstFailure.addSuppressed(fallbackFailure);
                throw firstFailure;
            }
        }
    }

    private void parseStream(DslProvider provider, WindowedReader sourceReader, LocationCursor location, Consumer<SplitScript> resultConsumer) {
        CharStream source = new UnbufferedCharStream(sourceReader);
        Lexer lexer = provider.createLexer(source);
        lexer.setTokenFactory(new CommonTokenFactory(true));
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);

        Parser parser = provider.createParser(lexer);
        StreamingCommonTokenStream tokens = new StreamingCommonTokenStream(lexer);
        parser.setTokenStream(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        parser.setBuildParseTree(true);
        // A retry uses a different configuration and must not reuse the initial mode's DFA.
        try (AntlrPredictionCaches.Lease ignored = AntlrPredictionCaches.acquire(lexer, parser, provider)) {
            lexer.setLine(location.line);
            lexer.setCharPositionInLine(location.column);
            parser.addParseListener(new SplitListener(parser, tokens, location, resultConsumer));
            this.parseRoot(parser);
        }
    }

    private final class SplitListener implements ParseTreeListener {

        private final Parser                     parser;
        private final StreamingCommonTokenStream tokens;
        private final LocationCursor             location;
        private final Consumer<SplitScript>      resultConsumer;
        private ParserRuleContext                lastStatement;

        private SplitListener(Parser parser, StreamingCommonTokenStream tokens, LocationCursor location, Consumer<SplitScript> resultConsumer){
            this.parser = parser;
            this.tokens = tokens;
            this.location = location;
            this.resultConsumer = resultConsumer;
        }

        @Override
        public void visitTerminal(TerminalNode node) {
        }

        @Override
        public void visitErrorNode(ErrorNode node) {
        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            if (parser.getNumberOfSyntaxErrors() > 0 || !isStatementContext(ctx) || !isStatementTerminated(parser)) {
                return;
            }

            Token startToken = ctx.getStart();
            Token stopToken = ctx.getStop();
            // ANTLR also exits unfinished rules while a syntax exception unwinds the parser.
            if (stopToken == null) {
                return;
            }
            String script = statementParser().getTextKeepComment(this.tokens, this.lastStatement, startToken, stopToken);
            ScriptLocation scriptLocation = this.location.locate(script);

            SplitScript split = new SplitScript();
            split.setIndex(this.location.statementIndex++);
            split.setScript(script);
            split.setType(collectTypes(ctx, script));
            split.setChildren(collectChildren(ctx, this.tokens));
            split.setBodyStartCodeLine(scriptLocation.startLine());
            split.setBodyStartCodeColumn(scriptLocation.startColumn());
            split.setBodyEndCodeLine(scriptLocation.endLine());
            split.setBodyEndCodeColumn(scriptLocation.endColumn());
            this.resultConsumer.accept(split);

            ParserRuleContext parent = ctx.getParent();
            if (parent != null && parent.children != null) {
                parent.children.remove(ctx);
            }
            this.lastStatement = ctx;
        }
    }

    private static final class LocationCursor {

        private final WindowedReader source;
        private int                  sourceOffset;
        private int                  line;
        private int                  column;
        private long                 statementIndex;

        private LocationCursor(WindowedReader source, CodeLocation base){
            this.source = source;
            this.line = Math.max(1, base == null ? 1 : base.getLineNumber());
            this.column = Math.max(0, base == null ? 0 : base.getColumnNumber());
        }

        private ScriptLocation locate(String script) {
            // Token offsets count code points; reader offsets count UTF-16 units.
            String sourceWindow = this.source.getText(this.sourceOffset, this.source.endOffset());
            int scriptOffset = sourceWindow.indexOf(script);
            if (scriptOffset < 0) {
                throw new IllegalStateException("Split script is not part of its source");
            }

            advance(sourceWindow, 0, scriptOffset);
            int startLine = this.line;
            int startColumn = this.column;
            advance(script, 0, script.length());
            this.sourceOffset += scriptOffset + script.length();
            this.source.discardBefore(this.sourceOffset);
            return new ScriptLocation(startLine, startColumn, this.line, this.column);
        }

        private void advance(String value, int start, int end) {
            for (int i = start; i < end; i += Character.charCount(value.codePointAt(i))) {
                if (value.charAt(i) == '\n') {
                    this.line++;
                    this.column = 0;
                } else {
                    this.column++;
                }
            }
        }
    }

    private record ScriptLocation(int startLine, int startColumn, int endLine, int endColumn) {
    }

    private static final class StreamingCommonTokenStream extends CommonTokenStream {

        private StreamingCommonTokenStream(TokenSource tokenSource){
            super(tokenSource);
        }

        @Override
        public String getText(Interval interval) {
            int start = interval.a;
            int stop = interval.b;
            if (start < 0 || stop < 0) {
                return "";
            }
            sync(stop);
            int availableStop = Math.min(stop, this.tokens.size() - 1);
            StringBuilder text = new StringBuilder();
            for (int index = start; index <= availableStop; index++) {
                Token token = this.tokens.get(index);
                if (token.getType() == Token.EOF) {
                    break;
                }
                text.append(token.getText());
            }
            return text.toString();
        }
    }

    private static final class WindowedReader extends FilterReader {

        private final StringBuilder window = new StringBuilder();
        private int                 windowStart;
        private int                 readOffset;

        private WindowedReader(Reader reader){
            super(reader);
        }

        @Override
        public int read() throws IOException {
            checkInterrupted();
            if (this.readOffset < this.window.length()) {
                return this.window.charAt(this.readOffset++);
            }
            int value = super.read();
            if (value >= 0) {
                this.window.append((char) value);
                this.readOffset++;
            }
            return value;
        }

        @Override
        public int read(char[] chars, int offset, int length) throws IOException {
            checkInterrupted();
            if (this.readOffset < this.window.length()) {
                int read = Math.min(length, this.window.length() - this.readOffset);
                this.window.getChars(this.readOffset, this.readOffset + read, chars, offset);
                this.readOffset += read;
                return read;
            }
            int read = super.read(chars, offset, length);
            if (read > 0) {
                this.window.append(chars, offset, read);
                this.readOffset += read;
            }
            return read;
        }

        private static void checkInterrupted() throws InterruptedIOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException("SQL split stream was closed");
            }
        }

        private int endOffset() {
            return this.windowStart + this.window.length();
        }

        private void rewind() {
            this.windowStart = 0;
            this.readOffset = 0;
        }

        private String getText(int startOffset, int endOffset) {
            if (startOffset < this.windowStart || endOffset > endOffset()) {
                throw new IllegalStateException("SQL source interval is outside the streaming window");
            }
            return this.window.substring(startOffset - this.windowStart, endOffset - this.windowStart);
        }

        private void discardBefore(int offset) {
            int discardLength = Math.min(this.window.length(), Math.max(0, offset - this.windowStart));
            if (discardLength > 0) {
                this.window.delete(0, discardLength);
                this.windowStart += discardLength;
                this.readOffset -= discardLength;
            }
        }
    }

    private final class StreamingSplit extends Spliterators.AbstractSpliterator<SplitScript> implements AutoCloseable {

        private static final Object         END     = new Object();
        private final Reader                reader;
        private final int                   baseLine;
        private final int                   baseColumn;
        private final BlockingQueue<Object> results = new ArrayBlockingQueue<>(1);
        private final AtomicBoolean         started = new AtomicBoolean();
        private final AtomicBoolean         closed  = new AtomicBoolean();
        private volatile Thread             producer;
        private boolean                     finished;

        private StreamingSplit(Reader reader, int baseLine, int baseColumn){
            super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
            this.reader = reader;
            this.baseLine = baseLine;
            this.baseColumn = baseColumn;
        }

        @Override
        public boolean tryAdvance(Consumer<? super SplitScript> action) {
            Objects.requireNonNull(action, "action");
            if (this.finished) {
                return false;
            }
            start();
            Object next;
            try {
                next = this.results.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                close();
                throw new SplitStreamException("interrupted while waiting for SQL split result", e);
            }

            if (next == END) {
                this.finished = true;
                return false;
            }
            if (next instanceof SplitFailure failure) {
                this.finished = true;
                throw failure.asRuntimeException();
            }
            action.accept((SplitScript) next);
            return true;
        }

        private void start() {
            if (!this.started.compareAndSet(false, true)) {
                return;
            }
            if (this.closed.get()) {
                this.results.offer(END);
                return;
            }

            Thread thread = new Thread(this::produce, "sql-split-stream-" + STREAM_SEQUENCE.incrementAndGet());
            thread.setDaemon(true);
            this.producer = thread;
            thread.start();
        }

        private void produce() {
            Throwable failure = null;
            try {
                beforeSplitStream();
                streamingSplit(this.reader, this.baseLine, this.baseColumn, this::publish);
            } catch (Throwable e) {
                failure = e;
            } finally {
                try {
                    afterSplitStream();
                } catch (Throwable e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
            if (!this.closed.get()) {
                publish(failure == null ? END : new SplitFailure(failure));
            }
        }

        private void publish(Object result) {
            if (this.closed.get()) {
                throw new SplitCancelledException();
            }
            try {
                this.results.put(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                if (this.closed.get()) {
                    throw new SplitCancelledException();
                }
                throw new SplitStreamException("interrupted while publishing SQL split result", e);
            }
        }

        @Override
        public void close() {
            if (!this.closed.compareAndSet(false, true)) {
                return;
            }
            this.finished = true;
            Thread thread = this.producer;
            if (thread != null) {
                thread.interrupt();
            }
            this.results.clear();
            this.results.offer(END);
        }
    }

    private record SplitFailure(Throwable cause) {

        private RuntimeException asRuntimeException() {
            if (this.cause instanceof RuntimeException runtimeException) {
                return runtimeException;
            }
            if (this.cause instanceof IOException ioException) {
                return new UncheckedIOException("read SQL script failed", ioException);
            }
            return new SplitStreamException("split SQL script failed", this.cause);
        }
    }

    private static final class SplitStreamException extends RuntimeException {

        private SplitStreamException(String message, Throwable cause){
            super(message, cause);
        }
    }

    private static final class SplitCancelledException extends RuntimeException {
    }

    private static final class NonClosingReader extends FilterReader {

        private NonClosingReader(Reader reader){
            super(reader);
        }

        @Override
        public void close() {
        }
    }
}
