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
package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

public class ChTimeValueFetcher extends StringAsClobFetcher {

    private static final LocalDateTime EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0);

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        if (ctx.getContext() instanceof StringValueFCD) {
            return (StringValueFCD) ctx.getContext();
        }

        // JDBC Time drops fractions and wraps at 24 hours. The driver's date-time retains both.
        LocalDateTime value = rs.getObject(columnName, LocalDateTime.class);
        StringValueFCD fcd;
        if (value == null) {
            fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
        } else {
            Duration duration = Duration.between(EPOCH, value);
            String sign = "";
            if (duration.isNegative()) {
                sign = "-";
                duration = duration.abs();
            }
            long seconds = duration.getSeconds();
            String text = String.format(Locale.ROOT, "%s%02d:%02d:%02d", sign, seconds / 3600, seconds / 60 % 60, seconds % 60);
            if (duration.getNano() != 0) {
                String fraction = String.format(Locale.ROOT, "%09d", duration.getNano());
                int end = fraction.length();
                while (fraction.charAt(end - 1) == '0') {
                    end--;
                }
                text += "." + fraction.substring(0, end);
            }
            fcd = StringValueFCD.ofInMemory(true, text.length(), text.length(), text, text.getBytes());
        }
        ctx.setContext(fcd);
        return fcd;
    }
}
