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
