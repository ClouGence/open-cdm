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
package com.clougence.schema.editor.builder.utils;

import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.domain.EForeignKey;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ForeignKeyChangeUtil {

    public static void genChangeSql(List<EForeignKey> sourceForeignKeys, List<EForeignKey> targetForeignKeys, SqlBuilder provider, TriggerContext triggerContext,
                                    String sourceCatalog, String sourceSchema, String sourceName, List<Action> actions) {
        Map<String, EForeignKey> sourceMap = sourceForeignKeys.stream().collect(Collectors.toMap(EForeignKey::getName, EForeignKey -> EForeignKey));
        Map<String, EForeignKey> targetMap = targetForeignKeys.stream()
            .collect(Collectors.toMap(foreignKey -> foreignKey.getName() != null ? foreignKey.getName() : UUID.randomUUID().toString(), index -> index));

        for (EForeignKey eForeignKey : sourceForeignKeys) {
            EForeignKey targetEForeign = targetMap.get(eForeignKey.getName());
            if (!targetMap.containsKey(eForeignKey.getName())) {
                // drop
                List<String> sqlDrops = provider.dropForeignKey(triggerContext, sourceCatalog, sourceSchema, sourceName, eForeignKey);
                actions.add(new Action(sqlDrops, sourceCatalog, sourceSchema, sourceName));
            } else if (eForeignKey.testChanged(targetEForeign)) {
                // rebuild
                List<String> sqlDrops = provider.dropForeignKey(triggerContext, sourceCatalog, sourceSchema, sourceName, eForeignKey);
                actions.add(new Action(sqlDrops, sourceCatalog, sourceSchema, sourceName));

                List<String> sqlCreates = provider.createForeignKey(triggerContext, sourceCatalog, sourceSchema, sourceName, targetEForeign);
                actions.add(new Action(sqlCreates, sourceCatalog, sourceSchema, sourceName));
            }
        }
        for (EForeignKey eForeignKey : targetForeignKeys) {
            if (!sourceMap.containsKey(eForeignKey.getName())) {
                // add target
                List<String> sqlAddStrings = provider.createForeignKey(triggerContext, sourceCatalog, sourceSchema, sourceName, eForeignKey);
                actions.add(new Action(sqlAddStrings, sourceCatalog, sourceSchema, sourceName));
            }
        }
    }
}
