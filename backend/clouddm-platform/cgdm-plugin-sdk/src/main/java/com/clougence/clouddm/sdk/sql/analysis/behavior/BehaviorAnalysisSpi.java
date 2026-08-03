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
package com.clougence.clouddm.sdk.sql.analysis.behavior;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * Analyzes semantic behaviors and resource relationships expressed by SQL statements.
 *
 * <p>This SPI reports behavior facts only. It does not calculate permission plans, map behaviors to
 * CloudDM authorization labels, or check whether the current principal has permission.</p>
 */
public interface BehaviorAnalysisSpi extends Spi {

    /**
     * Analyzes every statement from {@code queryReader} and returns results in source order.
     *
     * @param queryReader SQL text to analyze. The caller owns and closes this reader.
     * @param levels current resource levels, such as catalog and schema.
     * @param baseLine one-based line number at which the SQL starts in the complete document.
     * @param baseColumn zero-based column number at which the SQL starts in the complete document.
     * @return statement behavior results in source order; never {@code null}.
     */
    List<StatementBehavior> analysisBehavior(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn);

    /**
     * Streaming counterpart of {@link #analysisBehavior(Reader, Map, int, int)}.
     *
     * <p>This compatibility implementation still materializes the complete result list. Implementations may override
     * this method to emit statement behaviors lazily. The caller owns and closes {@code queryReader}.</p>
     */
    default Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return analysisBehavior(queryReader, levels, baseLine, baseColumn).stream();
    }
}
