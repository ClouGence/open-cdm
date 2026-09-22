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
package com.clougence.clouddm.sdk.sql.backup;

import com.clougence.clouddm.sdk.Spi;

/**
 * Analyzes a single UPDATE/DELETE statement for the prior-backup feature.
 *
 * <p>Implementations parse the statement with the dialect parser and report the target table and
 * the affected-row query. Statements that are not a plain single-table UPDATE/DELETE (CTE, multi
 * table update, USING clause, RETURNING, or any parse failure) must return {@code null} so the
 * caller can skip the backup without blocking the ticket.</p>
 */
public interface PriorBackupSpi extends Spi {

    /**
     * Analyzes one statement and returns the backup facts, or {@code null} if the statement is not
     * eligible for automatic row-level backup.
     *
     * @param query a single UPDATE or DELETE statement.
     */
    BackupStatement analysisBackup(String query);

    /**
     * Generates the reverse statement for a structural DDL, or {@code null} when no lossless
     * reverse exists. Only additive/renaming DDL should be reversed (CREATE TABLE → DROP TABLE,
     * CREATE INDEX → DROP INDEX, ADD COLUMN → DROP COLUMN, RENAME → reverse RENAME); destructive
     * DDL (DROP/TRUNCATE) loses data and must return {@code null}.
     *
     * @param query a single DDL statement.
     */
    default String reverseDdl(String query) {
        return null;
    }
}
