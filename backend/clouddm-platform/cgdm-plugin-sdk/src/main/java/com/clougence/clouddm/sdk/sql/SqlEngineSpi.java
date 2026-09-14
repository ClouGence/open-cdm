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
package com.clougence.clouddm.sdk.sql;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.backup.PriorBackupSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

/** SQL parser engine capabilities. */
public interface SqlEngineSpi extends Spi {

    // parser

    /**
     * Returns the SQL split and statement type analysis SPI. This method must not return null.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters);

    /**
     * Returns the ANTLR DSL provider, or null if language services are not supported.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    DslProvider dslProvider(SqlParserParameters parameters);

    // analysis

    /**
     * Returns the SQL statement behavior analysis SPI. This method must not return null.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters);

    /**
     * Returns the SELECT column analysis SPI, or null if column-level analysis is not supported.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters);

    /**
     * Returns the security-domain resolve SPI, or null if security-domain analysis is not supported.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters);

    // editor

    /**
     * Returns the SQL rewrite SPI, or null if query rewrite is not supported.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    RewriteSpi rewriteSpi(SqlParserParameters parameters);

    // backup

    /**
     * Returns the prior-backup analysis SPI, or null if row-level backup before UPDATE/DELETE is
     * not supported by this engine.
     *
     * @param parameters parser parameters, or null/empty to use implementation defaults.
     */
    default PriorBackupSpi priorBackupSpi(SqlParserParameters parameters) {
        return null;
    }
}
