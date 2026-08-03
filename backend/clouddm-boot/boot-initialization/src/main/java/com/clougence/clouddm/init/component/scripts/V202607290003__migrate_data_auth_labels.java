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
package com.clougence.clouddm.init.component.scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flywaydb.core.api.migration.Context;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.utils.JsonUtils;

public class V202607290003__migrate_data_auth_labels extends AbstractUpgradeJavaMigration {

    private static final String DM_OBJ    = "DM_OBJ";
    private static final String DM_DDL    = "DM_DDL";
    private static final String DM_DCL    = "DM_DCL";
    private static final String DM_MANAGE = "DM_MANAGE";

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        try (PreparedStatement query = connection.prepareStatement("""
                select id, res_auth_label
                from dm_auth_res
                where res_auth_label like '%"DM_OBJ"%'
                   or res_auth_label like '%"DM_DCL"%'
                """);
                ResultSet resultSet = query.executeQuery();
                PreparedStatement update = connection.prepareStatement("""
                        update dm_auth_res
                        set res_auth_label = ?, gmt_modified = now()
                        where id = ?
                        """)) {
            while (resultSet.next()) {
                String oldLabels = resultSet.getString("res_auth_label");
                String newLabels = migrateAuthLabels(oldLabels);
                if (oldLabels.equals(newLabels)) {
                    continue;
                }

                update.setString(1, newLabels);
                update.setLong(2, resultSet.getLong("id"));
                update.executeUpdate();
            }
        }
    }

    static String migrateAuthLabels(String authLabels) {
        List<String> oldLabels = JsonUtils.toListUseType(authLabels, String.class);
        List<String> newLabels = new ArrayList<>(oldLabels.size());
        Set<String> migratedTargets = new HashSet<>();
        boolean changed = false;

        for (String oldLabel : oldLabels) {
            String newLabel = switch (oldLabel) {
                case DM_OBJ -> DM_DDL;
                case DM_DCL -> DM_MANAGE;
                default -> oldLabel;
            };
            changed |= !newLabel.equals(oldLabel);

            if ((DM_DDL.equals(newLabel) || DM_MANAGE.equals(newLabel)) && !migratedTargets.add(newLabel)) {
                changed = true;
                continue;
            }
            newLabels.add(newLabel);
        }

        return changed ? JsonUtils.toJson(newLabels) : authLabels;
    }

    @Override
    public List<String> collectScript() {
        return List.of();
    }
}
