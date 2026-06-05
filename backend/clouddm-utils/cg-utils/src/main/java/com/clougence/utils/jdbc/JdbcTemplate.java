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
package com.clougence.utils.jdbc;

import java.sql.*;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.clougence.utils.jdbc.extractor.RowMapperResultSetExtractor;

import lombok.extern.slf4j.Slf4j;

/**
 * dbVisitor based and reimplements
 * @version : 2013-10-12
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Thomas Risberg
 * @author 赵永春 (zyc@hasor.net)
 * @see ResultSetExtractor
 * @see RowMapper
 */
@Slf4j
public class JdbcTemplate extends JdbcConnection {

    /**
     * Construct a new JdbcTemplate for bean usage.
     * <p>Note: The DataSource has to be set before using the instance.
     * @see #setDataSource
     */
    public JdbcTemplate(){
        super();
    }

    /**
     * Construct a new JdbcTemplate, given a DataSource to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param dataSource the JDBC DataSource to obtain connections from
     */
    public JdbcTemplate(final DataSource dataSource){
        super(dataSource);
    }

    /**
     * Construct a new JdbcTemplate, given a Connection to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param conn the JDBC Connection
     */
    public JdbcTemplate(final Connection conn){
        super(conn);
    }

    public boolean execute(final String sql) throws SQLException {
        return this.execute(con -> {
            try (Statement stmt = con.createStatement()) {
                applyStatementSettings(stmt);
                return stmt.execute(sql);
            }
        });
    }

    public <T> T query(final String sql, final ResultSetExtractor<T> rse) throws SQLException {
        return this.execute(con -> {
            try (Statement s = con.createStatement()) {
                applyStatementSettings(s);

                try (ResultSet rs = s.executeQuery(sql)) {
                    return rse.extractData(rs);
                }
            }
        });
    }

    public <T> T query(final String sql, final Object[] args, final ResultSetExtractor<T> rse) throws SQLException {
        return this.execute(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                argPreparedStatementSetValues(ps, args);
                applyStatementSettings(ps);

                try (ResultSet rs = ps.executeQuery()) {
                    return rse.extractData(rs);
                }
            }
        });
    }

    public <T> List<T> queryForList(final String sql, final RowMapper<T> rowMapper) throws SQLException {
        return this.execute(con -> {
            try (Statement s = con.createStatement()) {
                applyStatementSettings(s);

                try (ResultSet rs = s.executeQuery(sql)) {
                    return new RowMapperResultSetExtractor<>(rowMapper).extractData(rs);
                }
            }
        });
    }

    public <T> List<T> queryForList(final String sql, final Object[] args, final RowMapper<T> rowMapper) throws SQLException {
        return this.execute(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                argPreparedStatementSetValues(ps, args);
                applyStatementSettings(ps);

                try (ResultSet rs = ps.executeQuery()) {
                    return new RowMapperResultSetExtractor<>(rowMapper).extractData(rs);
                }
            }
        });
    }

    public <T> T queryForObject(final String sql, final RowMapper<T> rowMapper) throws SQLException {
        return requiredSingleResult(this.query(sql, new RowMapperResultSetExtractor<>(rowMapper, 1)));
    }

    public <T> T queryForObject(final String sql, final Object[] args, final RowMapper<T> rowMapper) throws SQLException {
        return this.execute(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                argPreparedStatementSetValues(ps, args);
                applyStatementSettings(ps);

                try (ResultSet rs = ps.executeQuery()) {
                    List<T> result = new RowMapperResultSetExtractor<>(rowMapper, 1).extractData(rs);
                    return requiredSingleResult(result);
                }
            }
        });
    }

    public long queryForLong(final String sql) throws SQLException {
        Long number = this.queryForObject(sql, (rs, rowNum) -> tryWasNull(rs.getLong(1), rs));
        return number != null ? number : 0;
    }

    public long queryForLong(final String sql, final Object[] args) throws SQLException {
        Long number = this.queryForObject(sql, args, (rs, rowNum) -> tryWasNull(rs.getLong(1), rs));
        return number != null ? number : 0;
    }

    public int queryForInt(final String sql) throws SQLException {
        Integer number = this.queryForObject(sql, (rs, rowNum) -> tryWasNull(rs.getInt(1), rs));
        return number != null ? number : 0;
    }

    public int queryForInt(final String sql, final Object[] args) throws SQLException {
        Integer number = this.queryForObject(sql, args, (rs, rowNum) -> tryWasNull(rs.getInt(1), rs));
        return number != null ? number : 0;
    }

    public String queryForString(final String sql) throws SQLException {
        return this.queryForObject(sql, (rs, rowNum) -> rs.getString(1));
    }

    public String queryForString(final String sql, final Object[] args) throws SQLException {
        return this.queryForObject(sql, args, (rs, rowNum) -> rs.getString(1));
    }

    public int executeUpdate(final String sql) throws SQLException {
        return this.execute(con -> {
            try (Statement s = con.createStatement()) {
                applyStatementSettings(s);

                return s.executeUpdate(sql);
            }
        });
    }

    public int executeUpdate(final String sql, final Object[] args) throws SQLException {
        return this.execute(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                argPreparedStatementSetValues(ps, args);
                applyStatementSettings(ps);

                return ps.getUpdateCount();
            }
        });

    }

    private void argPreparedStatementSetValues(PreparedStatement ps, Object[] args) throws SQLException {
        ArgumentTypePreparedStatementSetter.setValues(ps, args);
    }

    /** 至返回结果集中的一条数据。*/
    private static <T> T requiredSingleResult(final Collection<T> results) throws SQLException {
        if (results == null || results.isEmpty()) {
            return null;
        }
        int size = results.size();
        if (size > 1) {
            throw new SQLException("Incorrect record count: expected 1, actual " + size);
        }
        return results.iterator().next();
    }

    protected static Integer tryWasNull(int value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    protected static Long tryWasNull(long value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }
}
