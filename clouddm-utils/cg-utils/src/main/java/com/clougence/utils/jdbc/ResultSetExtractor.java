package com.clougence.utils.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @version : 2013-10-9
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {

    T extractData(ResultSet rs) throws SQLException;
}
