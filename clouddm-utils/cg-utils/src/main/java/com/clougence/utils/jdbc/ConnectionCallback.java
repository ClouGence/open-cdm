package com.clougence.utils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link Connection} callback
 * @version : 2013-10-9
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @author 赵永春 (zyc@hasor.net)
 */
@FunctionalInterface
public interface ConnectionCallback<T> {

    T doInConnection(Connection con) throws SQLException;
}
