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
package com.clougence.sql.common.parser.perf;

/**
 * Convenient batch recorder for any parser entry point (split / parse / analysis / engine).
 *
 * <p>Usage in a parser entry:
 * <pre>
 *   try (ParserPerfRecorder perf = ParserPerfRecorder.begin(collector, "mysql", lexer.getClass())) {
 *       ... call lexer.nextToken() ...
 *       perf.addToken(elapsedNanos);
 *       perf.addStatement();
 *       perf.input(chars, utf8Bytes);
 *   } // close() records the batch
 * </pre>
 * Callers should guard recorder construction with {@link ParserPerfCollector#enabled()} so the
 * disabled path does not allocate or time token calls.</p>
 */
public final class ParserPerfRecorder implements AutoCloseable {

    private static final long TOKEN_CHECKPOINT_SIZE = 512;

    private final ParserPerfCollector collector;
    private final String              dialect;
    private final String              lexer;
    private long                      statements;
    private long                      inputChars;
    private long                      inputBytes;
    private long                      outputTokens;
    private long                      lexerNanos;
    private long                      publishedStatements;
    private long                      publishedInputChars;
    private long                      publishedInputBytes;
    private long                      publishedOutputTokens;
    private long                      publishedLexerNanos;
    private boolean                   closed;

    private ParserPerfRecorder(ParserPerfCollector collector, String dialect, Class<? extends org.antlr.v4.runtime.Lexer> lexerType){
        this.collector = collector;
        this.dialect = dialect;
        this.lexer = lexerType.getName();
    }

    /** Start an isolated recorder for one concrete lexer instance. */
    public static ParserPerfRecorder begin(ParserPerfCollector collector, String dialect, Class<? extends org.antlr.v4.runtime.Lexer> lexerType) {
        return new ParserPerfRecorder(collector, dialect, lexerType);
    }

    /** Record one statement emitted by the lexical splitter. */
    public void addStatement() {
        this.statements++;
    }

    /** Record one non-EOF token and the time spent producing it. */
    public void addToken(long elapsedNanos) {
        this.outputTokens++;
        this.lexerNanos += elapsedNanos;
    }

    /** Record an EOF call or a failed token call, which still consumes lexer time. */
    public void addLexerTime(long elapsedNanos) {
        this.lexerNanos += elapsedNanos;
    }

    /** Whether enough tokens have accumulated to publish a low-overhead live interval. */
    public boolean checkpointDue() {
        return this.outputTokens - this.publishedOutputTokens >= TOKEN_CHECKPOINT_SIZE;
    }

    /** Publish progress while retaining complete invocation totals for persisted metrics. */
    public void checkpoint(long charCount, long byteCount) {
        input(charCount, byteCount);
        publishProgress(false);
    }

    /** Set cumulative input consumed by this lexer instance. */
    public void input(long charCount, long byteCount) {
        this.inputChars = charCount;
        this.inputBytes = byteCount;
    }

    /** Submit the batch to the collector (no-op when collection is disabled). */
    @Override
    public void close() {
        if (!this.closed) {
            this.closed = true;
            publishProgress(true);
            this.collector.recordCompleted(this.dialect, this.lexer, this.statements, this.inputChars,
                    this.inputBytes, this.outputTokens, this.lexerNanos);
        }
    }

    private void publishProgress(boolean completed) {
        long statementDelta = this.statements - this.publishedStatements;
        long charDelta = this.inputChars - this.publishedInputChars;
        long byteDelta = this.inputBytes - this.publishedInputBytes;
        long tokenDelta = this.outputTokens - this.publishedOutputTokens;
        long nanosDelta = this.lexerNanos - this.publishedLexerNanos;
        if (completed || statementDelta != 0 || charDelta != 0 || byteDelta != 0 || tokenDelta != 0 || nanosDelta != 0) {
            this.collector.recordProgress(this.dialect, this.lexer, statementDelta, charDelta, byteDelta,
                    tokenDelta, nanosDelta, completed);
            this.publishedStatements = this.statements;
            this.publishedInputChars = this.inputChars;
            this.publishedInputBytes = this.inputBytes;
            this.publishedOutputTokens = this.outputTokens;
            this.publishedLexerNanos = this.lexerNanos;
        }
    }
}
