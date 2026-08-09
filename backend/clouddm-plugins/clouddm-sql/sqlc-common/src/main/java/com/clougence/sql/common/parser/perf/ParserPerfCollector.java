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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unified SQL parser performance collector (independent component).
 *
 * <p>Any parser entry point (split, parse, analysis, engine) may record a batch via
 * {@link #record(String, String, long, long, long, long, long)} or use the convenient
 * {@link ParserPerfRecorder} wrapper. Results are aggregated per
 * (dialect, concrete lexer, statement-size tier)
 * and emitted as one JSONL snapshot line per full window:
 * <pre>
 *   {"dialect":"mysql","lexer":"...MySqlLexer","tier":"S","statements":500,
 *    "inputChars":22000,"inputBytes":22000,"outputTokens":10500,
 *    "elapsedNanos":...,"tokensPerSec":...,"charsPerSec":...,"bytesPerSec":...}
 * </pre></p>
 *
 * <p>Switch (system properties, disabled by default):
 * <ul>
 *   <li>{@code com.clougence.sql.perf.enabled} = true enables collection</li>
 *   <li>{@code com.clougence.sql.perf.file} output file, default {@code data/sql-parser-perf.jsonl}</li>
 *   <li>{@code com.clougence.sql.perf.window} statements per tier window, default 500</li>
 * </ul>
 * When disabled, {@link #record} only pays a single boolean check.</p>
 */
public final class ParserPerfCollector {

    /** Statement-size tiers (by chars): S < 100 < M < 1_000 < L < 10_000 < XL */
    private static final String                                 TIER_S          = "S";
    private static final String                                 TIER_M          = "M";
    private static final String                                 TIER_L          = "L";
    private static final String                                 TIER_XL         = "XL";

    private static final ObjectMapper                           MAPPER          = new ObjectMapper();
    private static final Object                                 FILE_WRITE_LOCK = new Object();

    private final boolean                                       enabled;
    private final Path                                          file;
    private final int                                           window;
    private final ConcurrentHashMap<MetricKey, LexerAggregator> aggregators     = new ConcurrentHashMap<>();

    public ParserPerfCollector(boolean enabled, Path file, int window){
        this.enabled = enabled;
        this.file = file;
        this.window = Math.max(1, window);
    }

    /** Creates a collector from the documented system properties. */
    public static ParserPerfCollector fromSystemProperties() {
        boolean enabled = Boolean.getBoolean("com.clougence.sql.perf.enabled");
        Path file = Paths.get(System.getProperty("com.clougence.sql.perf.file", "data/sql-parser-perf.jsonl"));
        int window = Integer.getInteger("com.clougence.sql.perf.window", 500);
        return new ParserPerfCollector(enabled, file, window);
    }

    /** Whether this collector records metrics. */
    public boolean enabled() {
        return this.enabled;
    }

    /**
     * Record one parsing batch (a single split/parse invocation may contain several statements).
     *
     * @param dialect      dialect id
     * @param lexer        concrete lexer class name
     * @param statements   statements emitted in this batch
     * @param inputChars   UTF-16 chars read by the lexer
     * @param inputBytes   UTF-8 bytes represented by the input
     * @param outputTokens non-EOF tokens emitted by the lexer
     * @param elapsedNanos time spent inside Lexer.nextToken() (nanos)
     */
    public void record(String dialect, String lexer, long statements, long inputChars, long inputBytes, long outputTokens, long elapsedNanos) {
        if (!this.enabled) {
            return;
        }
        String tier = tierOf(inputChars / Math.max(1, statements));
        MetricKey key = new MetricKey(dialect, lexer, tier);
        this.aggregators.computeIfAbsent(key, ignored -> new LexerAggregator(key)).add(statements, inputChars, inputBytes, outputTokens, elapsedNanos);
    }

    /**
     * Returns and resets the in-memory reporting interval for every lexer. File windows are
     * independent, so a live monitor cannot consume or alter persisted metrics.
     */
    public List<ParserPerfSnapshot> drainIntervals() {
        if (!this.enabled) {
            return List.of();
        }
        Map<LexerKey, Counters> intervals = new HashMap<>();
        for (LexerAggregator aggregator : this.aggregators.values()) {
            ParserPerfSnapshot snapshot = aggregator.drainInterval();
            if (snapshot != null) {
                LexerKey key = new LexerKey(snapshot.dialect(), snapshot.lexer());
                intervals.computeIfAbsent(key, ignored -> new Counters())
                    .add(snapshot.invocations(), snapshot.statements(), snapshot.inputChars(), snapshot.inputBytes(), snapshot.outputTokens(), snapshot.elapsedNanos());
            }
        }
        List<ParserPerfSnapshot> snapshots = new ArrayList<>();
        intervals.forEach((key, counters) -> snapshots.add(counters.snapshotAndReset(new MetricKey(key.dialect(), key.lexer(), "ALL"))));
        snapshots.sort(Comparator.comparing(ParserPerfSnapshot::dialect).thenComparing(ParserPerfSnapshot::lexer));
        return List.copyOf(snapshots);
    }

    /** Flush remaining partial windows (call on process exit or test teardown). */
    public void flush() {
        if (!this.enabled) {
            return;
        }
        for (LexerAggregator aggregator : this.aggregators.values()) {
            aggregator.flushFileWindow();
        }
    }

    private static String tierOf(long chars) {
        if (chars < 100) {
            return TIER_S;
        }
        if (chars < 1_000) {
            return TIER_M;
        }
        if (chars < 10_000) {
            return TIER_L;
        }
        return TIER_XL;
    }

    /** Each key owns its counters; different datasource lexers never share mutable state. */
    private final class LexerAggregator {

        private final MetricKey key;
        private final Counters  file     = new Counters();
        private final Counters  interval = new Counters();

        private LexerAggregator(MetricKey key){
            this.key = key;
        }

        private synchronized void add(long statements, long inputChars, long inputBytes, long outputTokens, long elapsedNanos) {
            this.file.add(1, statements, inputChars, inputBytes, outputTokens, elapsedNanos);
            this.interval.add(1, statements, inputChars, inputBytes, outputTokens, elapsedNanos);
            if (this.file.statements >= ParserPerfCollector.this.window) {
                write(this.file.snapshotAndReset(this.key));
            }
        }

        private synchronized ParserPerfSnapshot drainInterval() {
            return this.interval.snapshotAndReset(this.key);
        }

        private synchronized void flushFileWindow() {
            ParserPerfSnapshot snapshot = this.file.snapshotAndReset(this.key);
            if (snapshot != null) {
                write(snapshot);
            }
        }

        private void write(ParserPerfSnapshot snapshot) {
            synchronized (FILE_WRITE_LOCK) {
                try {
                    Path parent = ParserPerfCollector.this.file.getParent();
                    if (parent != null) {
                        Files.createDirectories(parent);
                    }
                    Files.write(ParserPerfCollector.this.file, (MAPPER.writeValueAsString(snapshot) + "\n")
                        .getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    System.err.println("[sql-parser-perf] write failed: " + e.getMessage());
                }
            }
        }
    }

    private static final class Counters {

        private long invocations;
        private long statements;
        private long inputChars;
        private long inputBytes;
        private long outputTokens;
        private long elapsedNanos;

        private void add(long invocationCount, long statementCount, long charCount, long byteCount, long tokenCount, long nanos) {
            this.invocations += invocationCount;
            this.statements += statementCount;
            this.inputChars += charCount;
            this.inputBytes += byteCount;
            this.outputTokens += tokenCount;
            this.elapsedNanos += nanos;
        }

        private ParserPerfSnapshot snapshotAndReset(MetricKey key) {
            if (this.invocations == 0) {
                return null;
            }
            ParserPerfSnapshot snapshot = ParserPerfSnapshot
                .of(key.dialect(), key.lexer(), key.tier(), this.invocations, this.statements, this.inputChars, this.inputBytes, this.outputTokens, this.elapsedNanos);
            this.invocations = 0;
            this.statements = 0;
            this.inputChars = 0;
            this.inputBytes = 0;
            this.outputTokens = 0;
            this.elapsedNanos = 0;
            return snapshot;
        }
    }

    private record MetricKey(String dialect, String lexer, String tier) {
    }

    private record LexerKey(String dialect, String lexer) {
    }
}
