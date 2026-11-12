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

package com.clougence.clouddm.ds.greptimedb.execute;

import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.clougence.clouddm.dsfamily.execute.fetcher.DateTimeValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

final class GreptimeTimestampFetcher extends DateTimeValueFetcher {

    @Override
    public LocalDateTime getDateTime(String column, ResultSet rows, ValueFetcherContext context) throws SQLException {
        // GreptimeDB sends session-local time and advertises a scale of zero even when the wire value has fractions.
        return rows.getObject(column, LocalDateTime.class);
    }

    @Override
    public String getString(String column, ResultSet rows, ValueFetcherContext context) throws SQLException {
        LocalDateTime time = getDateTime(column, rows, context);
        if (time == null) {
            return null;
        }
        return getFormat(context).format(time);
    }

    @Override
    public Reader getReader(String column, ResultSet rows, ValueFetcherContext context) throws SQLException {
        String value = getString(column, rows, context);
        if (value == null) {
            return null;
        }
        return new StringReader(value);
    }
}
