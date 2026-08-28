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
package com.clougence.clouddm.ds.kingbasees.execute;

import java.sql.ResultSet;
import java.sql.Statement;

import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class KingbaseESMetaQueryUtils {

    private KingbaseESMetaQueryUtils(){
    }

    public static String query(Session session, String sql, String operation) {
        try {
            return session.executeQuery(connection -> {
                try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, typeName, className) -> rs.getString(1)).mapRow(resultSet);
                }
            });
        } catch (Exception e) {
            String msg = operation + " failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
