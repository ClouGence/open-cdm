package com.clougence.utils.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

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
public class JdbcUtils {

    public static Integer tryWasNull(int value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    public static Boolean tryWasNull(boolean value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    public static Long tryWasNull(long value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    public static Integer tryWasNull(short value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return (int) value;
        }
    }
}
