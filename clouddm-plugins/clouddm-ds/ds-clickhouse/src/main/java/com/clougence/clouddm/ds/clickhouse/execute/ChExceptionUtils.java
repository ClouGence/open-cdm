package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.SQLException;

/**
 * only for integration test
 *
 * @author Ekko 2022/11/03 16:48
 **/
public class ChExceptionUtils {

    public static SQLException toException(SQLException e) throws SQLException {
        String msg = e.getMessage();
        String msgTop = msg.split("\n")[0];
        if (msgTop.contains("Database ") && msgTop.contains(" doesn't exist.")) {
            int index1 = msgTop.indexOf("Database ");
            int index2 = msgTop.indexOf(" doesn't exist.");
            if (index1 > 0 && index2 > 0) {
                throw new SQLException(msgTop.substring(index1, index2 + " doesn't exist.".length()));
            }
        }
        throw e;
    }
}
