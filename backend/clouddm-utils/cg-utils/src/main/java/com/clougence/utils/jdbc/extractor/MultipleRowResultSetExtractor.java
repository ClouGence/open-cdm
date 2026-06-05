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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class MultipleRowResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;
    private final int          rowsExpected;

    /**
     * 创建 {@link MultipleRowResultSetExtractor} 对象
     * 
     * @param rowMapper 行映射器。
     */
    public MultipleRowResultSetExtractor(final RowMapper<T> rowMapper){
        this(rowMapper, 0);
    }

    /**
     * 创建 {@link MultipleRowResultSetExtractor} 对象
     * 
     * @param rowMapper 行映射器。
     * @param rowsExpected 预期结果集大小（实际得到的结果集条目不受此参数限制）。
     */
    public MultipleRowResultSetExtractor(final RowMapper<T> rowMapper, final int rowsExpected){
        Objects.requireNonNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
    }

    @Override
    public List<T> extractData(final ResultSet rs) throws SQLException {
        List<T> results = this.rowsExpected > 0 ? new ArrayList<>(this.rowsExpected) : new ArrayList<T>();
        int rowNum = 0;
        while (rs.next()) {
            T mapRow = this.rowMapper.mapRow(rs, rowNum++);
            if (testRow(mapRow)) {
                results.add(mapRow);
                if (this.rowsExpected > 0 && results.size() >= this.rowsExpected) {
                    break;
                }
            }
        }
        return results;
    }

    protected boolean testRow(T mapRow) {
        return mapRow != null;
    }
}
