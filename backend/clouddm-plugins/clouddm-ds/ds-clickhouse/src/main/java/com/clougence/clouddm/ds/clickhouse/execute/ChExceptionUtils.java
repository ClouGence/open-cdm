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
