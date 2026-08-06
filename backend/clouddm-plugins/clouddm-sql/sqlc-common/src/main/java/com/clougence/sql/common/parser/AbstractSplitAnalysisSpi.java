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
        CharStream source = new UnbufferedCharStream(sourceReader);
        DslProvider provider = dslProvider();
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
        try (AntlrPredictionCaches.Lease ignored = AntlrPredictionCaches.acquire(lexer, parser, predictionCacheScope())) {
            parser.addParseListener(new SplitListener(tokens, new LocationCursor(sourceReader, new CodeLocation(baseLine, baseColumn)), resultConsumer));
            this.parseRoot(parser);
        }
    }

    /** Stable value containing parser features which affect semantic predicates. */
    protected Object predictionCacheScope() {
        return null;
    }

    private final class SplitListener implements ParseTreeListener {

        private final StreamingCommonTokenStream tokens;
        private final LocationCursor             location;
        private final Consumer<SplitScript>      resultConsumer;
        private ParserRuleContext                lastStatement;

        private SplitListener(StreamingCommonTokenStream tokens, LocationCursor location, Consumer<SplitScript> resultConsumer){
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
            if (!isStatementContext(ctx)) {
                return;
            }

            Token startToken = ctx.getStart();
            Token stopToken = ctx.getStop();
            String script = statementParser().getTextKeepComment(this.tokens, this.lastStatement, startToken, stopToken);
            ScriptLocation scriptLocation = this.location.locate(script, stopToken.getStopIndex());

            SplitScript split = new SplitScript();
            split.setScript(script);
            split.setType(collectTypes(ctx, script));
            split.setChildren(collectChildren(ctx, this.tokens));
            ScriptLocation bodyStart = this.location.locate(startToken);
            split.setBodyStartCodeLine(bodyStart.endLine());
            split.setBodyStartCodeColumn(bodyStart.endColumn());
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
        private final int            baseLine;
        private final int            baseColumn;
        private int                  sourceOffset;
        private int                  line;
        private int                  column;

        private LocationCursor(WindowedReader source, CodeLocation base){
            this.source = source;
            this.baseLine = Math.max(1, base == null ? 1 : base.getLineNumber());
            this.baseColumn = Math.max(0, base == null ? 0 : base.getColumnNumber());
            this.line = this.baseLine;
            this.column = this.baseColumn;
        }

        private ScriptLocation locate(String script, int stopOffset) {
            int searchEnd = Math.min(this.source.endOffset(), Math.max(this.sourceOffset, stopOffset + 1) + script.length());
            String sourceWindow = this.source.getText(this.sourceOffset, searchEnd);
            int scriptOffset = sourceWindow.indexOf(script);
            if (scriptOffset < 0) {
                throw new IllegalStateException("Split script is not part of its source");
            }

            advance(sourceWindow, 0, scriptOffset);
            advance(script, 0, script.length());
            this.sourceOffset += scriptOffset + script.length();
            this.source.discardBefore(this.sourceOffset);
            return new ScriptLocation(this.line, this.column);
        }

        private ScriptLocation locate(Token token) {
            int tokenLine = Math.max(1, token.getLine());
            int tokenColumn = Math.max(0, token.getCharPositionInLine());
            int mappedLine = this.baseLine + tokenLine - 1;
            int mappedColumn = tokenLine == 1 ? this.baseColumn + tokenColumn : tokenColumn;
            return new ScriptLocation(mappedLine, mappedColumn);
        }

        private void advance(String value, int start, int end) {
            for (int i = start; i < end; i++) {
                if (value.charAt(i) == '\n') {
                    this.line++;
                    this.column = 0;
                } else {
                    this.column++;
                }
            }
        }
    }

    private record ScriptLocation(int endLine, int endColumn) {
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

        private WindowedReader(Reader reader){
            super(reader);
        }

        @Override
        public int read() throws IOException {
            checkInterrupted();
            int value = super.read();
            if (value >= 0) {
                this.window.append((char) value);
            }
            return value;
        }

        @Override
        public int read(char[] chars, int offset, int length) throws IOException {
            checkInterrupted();
            int read = super.read(chars, offset, length);
            if (read > 0) {
                this.window.append(chars, offset, read);
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
