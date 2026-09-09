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

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

/**
 * Prior-backup analysis result for a single UPDATE/DELETE statement.
 * Carries the facts needed to build a row-level backup (CTAS) before the change runs
 * and to generate the rollback SQL after the change completes.
 */
public class BackupStatement {

    /** statement type, only UPDATE or DELETE. */
    private SplitQueryType statementType;

    /** target table text as written in the statement, schema prefix kept if present. */
    private String         sourceTable;

    /** dialect-correct query selecting the affected rows, e.g. {@code SELECT * FROM t WHERE ...}. */
    private String         selectSql;

    /** whether the statement carries a WHERE clause; full-table changes are backed up by policy of the caller. */
    private boolean        hasWhere;

    public SplitQueryType getStatementType() {
        return statementType;
    }

    public void setStatementType(SplitQueryType statementType) {
        this.statementType = statementType;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public boolean isHasWhere() {
        return hasWhere;
    }

    public void setHasWhere(boolean hasWhere) {
        this.hasWhere = hasWhere;
    }
}
