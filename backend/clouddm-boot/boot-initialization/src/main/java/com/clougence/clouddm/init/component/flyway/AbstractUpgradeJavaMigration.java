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
package com.clougence.clouddm.init.component.flyway;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import com.clougence.utils.ExceptionUtils;

public abstract class AbstractUpgradeJavaMigration extends BaseJavaMigration {

    private static final Set<Integer> IGNORABLE_ERROR_CODES = Set.of(1050, 1054, 1060, 1061, 1062, 1072, 1091);

    @Override
    public void migrate(Context context) throws Exception {
        Connection c = context.getConnection();
        beforeMigrate(c);
        for (String sql : collectScript()) {
            safeExecute(c, sql);
        }
    }

    protected void beforeMigrate(Connection connection) throws Exception {
    }

    public abstract List<String> collectScript();

    protected final void safeExecute(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            if (IGNORABLE_ERROR_CODES.contains(e.getErrorCode())) {
                System.out.println("Flyway java exec error but skip, msg:" + ExceptionUtils.getRootCauseMessage(e) + ", sql: " + sql);
                return;
            }

            throw new RuntimeException("Failed to execute: " + sql, e);
        }
    }
}
