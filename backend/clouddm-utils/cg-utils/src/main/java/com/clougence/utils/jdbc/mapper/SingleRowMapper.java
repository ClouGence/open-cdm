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
package com.clougence.utils.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.utils.jdbc.RowMapper;

@FunctionalInterface
public interface SingleRowMapper<T> extends RowMapper<T> {

    default T mapRow(final ResultSet rs) throws SQLException {
        return this.mapRow(rs, 1);
    }

    @Override
    default T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        if (rs.next()) {
            return getResultSetValue(rs);
        } else {
            return null;
        }
    }

    T getResultSetValue(ResultSet rs) throws SQLException;
}
