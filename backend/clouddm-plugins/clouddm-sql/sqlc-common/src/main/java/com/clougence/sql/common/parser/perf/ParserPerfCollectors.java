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

import java.util.Objects;
import java.util.function.Supplier;

/** Controls how a splitter obtains its metrics collector. */
public final class ParserPerfCollectors {

    private static final Supplier<ParserPerfCollector>    DEFAULT_FACTORY = ParserPerfCollector::fromSystemProperties;

    private static volatile Supplier<ParserPerfCollector> factory         = DEFAULT_FACTORY;

    private ParserPerfCollectors(){
    }

    /** Creates an isolated collector by default, or returns an externally configured collector. */
    public static ParserPerfCollector create() {
        return Objects.requireNonNull(factory.get(), "parser performance collector");
    }

    /** Makes newly created splitters use collectors supplied by the outer application. */
    public static void setFactory(Supplier<ParserPerfCollector> collectorFactory) {
        factory = Objects.requireNonNull(collectorFactory, "collectorFactory");
    }

    /** Restores per-splitter collector creation. */
    public static void resetFactory() {
        factory = DEFAULT_FACTORY;
    }
}
