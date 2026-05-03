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
