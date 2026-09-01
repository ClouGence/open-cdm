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

import java.util.Collection;

/**
 * Window aggregation snapshot of parser performance (per dialect + statement-size tier).
 *
 * @param dialect      dialect id (mysql / oracle / ...)
 * @param lexer        concrete lexer class; metrics from different lexers never share a bucket
 * @param tier         statement-size tier (S / M / L / XL)
 * @param invocations  lexer instances completed in the window
 * @param statements   statements in the window
 * @param inputChars   UTF-16 input chars read by the lexer
 * @param inputBytes   UTF-8 bytes represented by the input read by the lexer
 * @param outputTokens non-EOF tokens emitted by the lexer
 * @param elapsedNanos time spent inside {@code Lexer.nextToken()} in the window
 * @param statementsPerSec statement throughput = statements * 1e9 / elapsedNanos
 * @param tokensPerSec token throughput = tokens * 1e9 / elapsedNanos
 * @param charsPerSec  input char throughput = inputChars * 1e9 / elapsedNanos
 * @param bytesPerSec  input UTF-8 byte throughput = inputBytes * 1e9 / elapsedNanos
 * @param timestamp    snapshot timestamp (millis)
 */
public record ParserPerfSnapshot(String dialect, String lexer, String tier, long invocations, long statements, long inputChars, long inputBytes, long outputTokens,
                                 long elapsedNanos, double statementsPerSec, double tokensPerSec, double charsPerSec, double bytesPerSec, long timestamp) {

    public static ParserPerfSnapshot of(String dialect, String lexer, String tier, long invocations, long statements, long inputChars, long inputBytes, long outputTokens,
                                        long elapsedNanos) {
        double statementsPerSec = statements * 1e9 / Math.max(1, elapsedNanos);
        double tokensPerSec = outputTokens * 1e9 / Math.max(1, elapsedNanos);
        double charsPerSec = inputChars * 1e9 / Math.max(1, elapsedNanos);
        double bytesPerSec = inputBytes * 1e9 / Math.max(1, elapsedNanos);
        return new ParserPerfSnapshot(dialect,
            lexer,
            tier,
            invocations,
            statements,
            inputChars,
            inputBytes,
            outputTokens,
            elapsedNanos,
            statementsPerSec,
            tokensPerSec,
            charsPerSec,
            bytesPerSec,
            System.currentTimeMillis());
    }

    /** Combines arbitrary per-lexer snapshots into one outer monitoring view. */
    public static ParserPerfSnapshot sum(String dialect, String lexer, Collection<ParserPerfSnapshot> snapshots) {
        long invocations = 0;
        long statements = 0;
        long inputChars = 0;
        long inputBytes = 0;
        long outputTokens = 0;
        long elapsedNanos = 0;
        for (ParserPerfSnapshot snapshot : snapshots) {
            invocations += snapshot.invocations();
            statements += snapshot.statements();
            inputChars += snapshot.inputChars();
            inputBytes += snapshot.inputBytes();
            outputTokens += snapshot.outputTokens();
            elapsedNanos += snapshot.elapsedNanos();
        }
        return of(dialect, lexer, "ALL", invocations, statements, inputChars, inputBytes, outputTokens, elapsedNanos);
    }
}
