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
package com.clougence.drivers.adapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AdapterResponse {

    private boolean          resultIsResult;
    private boolean          resultIsError;
    private List<JdbcColumn> columns;
    private AdapterCursor    resultSet;
    private long             updateCount;
    private Throwable        exception;

    public List<JdbcColumn> getColumnList() { return this.columns; }

    public boolean isResult() { return this.resultIsResult; }

    public boolean isError() { return this.resultIsError; }

    public boolean isPending() { return this.resultIsResult && this.resultSet.isPending(); }

    public AdapterCursor toCursor() {
        return this.resultSet;
    }

    public SQLException toError() {
        if (this.exception instanceof SQLException) {
            return (SQLException) this.exception;
        } else {
            return new SQLException(this.exception);
        }
    }

    public long getUpdateCount() { return this.updateCount; }

    public static AdapterResponse ofError(Throwable e) {
        AdapterResponse res = new AdapterResponse();
        res.resultIsError = true;

        res.columns = Collections.emptyList();
        res.resultSet = null;
        res.updateCount = 0;
        res.exception = e;
        return res;
    }

    public static AdapterResponse ofCursor(AdapterCursor cursor) {
        AdapterResponse res = new AdapterResponse();
        res.resultIsResult = true;

        res.columns = cursor.columns();
        res.resultSet = cursor;
        res.updateCount = 0;
        res.exception = null;
        return res;
    }

    public static AdapterResponse ofUpdateCount(long updateCount) {
        AdapterResponse res = new AdapterResponse();
        res.resultIsResult = false;

        res.columns = Collections.emptyList();
        res.resultSet = null;
        res.updateCount = updateCount;
        res.exception = null;
        return res;
    }
}
