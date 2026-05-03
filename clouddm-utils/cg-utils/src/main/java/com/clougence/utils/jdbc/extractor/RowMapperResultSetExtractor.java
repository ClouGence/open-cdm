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
 * <pre class="code">JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);  // reusable object
 * RowMapper rowMapper = new UserRowMapper();  // reusable object
 *
 * List allUsers = (List) jdbcTemplate.query(
 *     "select * from user",
 *     new RowMapperResultSetExtractor(rowMapper, 10));
 *
 * User user = (User) jdbcTemplate.queryForObject(
 *     "select * from user where id=?", new Object[] {id},
 *     new RowMapperResultSetExtractor(rowMapper, 1));</pre>
 *
 * @author Juergen Hoeller
 * @author 赵永春 (zyc@hasor.net)
 * @see RowMapper
 */
public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;
    private final int          rowsExpected;

    /**
     * 创建 {@link RowMapperResultSetExtractor} 对象
     * @param rowMapper 行映射器。
     */
    public RowMapperResultSetExtractor(final RowMapper<T> rowMapper){
        this(rowMapper, 0);
    }

    /**
     * 创建 {@link RowMapperResultSetExtractor} 对象
     * @param rowMapper 行映射器。
     * @param rowsExpected 预期结果集大小（实际得到的结果集条目不受此参数限制）。
     */
    public RowMapperResultSetExtractor(final RowMapper<T> rowMapper, final int rowsExpected){
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
        return true;
    }
}
