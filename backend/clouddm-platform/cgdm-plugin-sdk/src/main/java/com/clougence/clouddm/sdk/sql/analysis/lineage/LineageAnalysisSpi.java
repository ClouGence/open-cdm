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
package com.clougence.clouddm.sdk.sql.analysis.lineage;

import java.util.List;

import com.clougence.clouddm.sdk.Spi;

public interface LineageAnalysisSpi extends Spi {

    /** Lineage analyzer used when a SQL engine does not currently expose lineage analysis. */
    LineageAnalysisSpi EMPTY = (sql, context) -> List.of();

    /**
     * Analyzes every result column in the query.
     *
     * <p>The returned list follows the order in which result columns appear in the SQL select list. This order must
     * not be used to match columns against an actual result set; consumers must match them by
     * {@link LineageColumn#column()}.</p>
     */
    List<LineageColumn> analyze(String sql, LineageContext context);
}
