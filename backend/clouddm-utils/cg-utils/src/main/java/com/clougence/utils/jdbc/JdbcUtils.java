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
