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
package com.clougence.clouddm.sdk.sql.parser;

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.QueryArg;

@FunctionalInterface
public interface SplitAnalysisSpi extends Spi {

    /**
     * The caller owns the reader and is responsible for closing it.
     */
    List<SplitScript> splitScript(Reader reader, List<QueryArg> args, int baseCodeLine, int baseCodeColumn);

    /**
     * Streaming counterpart of {@link #splitScript(Reader, List, int, int)}.
     *
     * <p>This compatibility implementation still materializes the complete result list. Implementations may override
     * this method to split and emit statements lazily. The caller owns the reader and is responsible for closing it.</p>
     */
    default Stream<SplitScript> splitScriptStream(Reader reader, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        return splitScript(reader, args, baseCodeLine, baseCodeColumn).stream();
    }
}
