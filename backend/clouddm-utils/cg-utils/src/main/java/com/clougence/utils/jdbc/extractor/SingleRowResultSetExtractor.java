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
package com.clougence.utils.jdbc.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clougence.utils.jdbc.ResultSetExtractor;
import com.clougence.utils.jdbc.RowMapper;

/**
 * impl ResultSetExtractor
 *
 * <pre class="code">
 * 
 * JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); // reusable object
 * RowMapper rowMapper = new UserRowMapper(); // reusable object
 *
 * List allUsers = (List) jdbcTemplate.query("select * from user", new RowMapperResultSetExtractor(rowMapper, 10));
 *
 * User user = (User) jdbcTemplate.queryForObject("select * from user where id=?", new Object[] { id }, new RowMapperResultSetExtractor(rowMapper, 1));
 * </pre>
 *
 * @author Juergen Hoeller
 * @see RowMapper
 */
public class SingleRowResultSetExtractor<T> implements ResultSetExtractor<T> {

    private final MultipleRowResultSetExtractor<T> resultSetExtractor;

    public SingleRowResultSetExtractor(final RowMapper<T> rowMapper){
        this.resultSetExtractor = new MultipleRowResultSetExtractor<>(rowMapper);
    }

    @Override
    public T extractData(final ResultSet rs) throws SQLException {
        List<T> results = this.resultSetExtractor.extractData(rs);
        return results == null || results.isEmpty() ? null : results.get(0);
    }
}
