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

package com.clougence.sql.greptimedb;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

final class GreptimeQueryResourceResolver implements QueryResourceResolver {

    private static final I18nUtils I18N = I18nUtils.initI18n(GreptimeSqlI18nKeys.class);

    @Override
    public void resolve(QueryRequest request, Function<UmiTypes, List<String>> objectNames, Predicate<BehaviorRelation> permitted, QueryResourceContext context) {
        String database = (String) context.levels().get(UmiTypes.Schema);
        boolean hasDatabase = StringUtils.isNotBlank(database);
        if ((hasDatabase && !isPathSegment(database)) || request.getRelations() == null || request.getRelations().isEmpty()) {
            throw unresolved();
        }

        Map<String, Value> objects = new HashMap<>();
        Set<String> tables = new LinkedHashSet<>();
        for (BehaviorRelation relation : request.getRelations()) {
            resolveObject(relation.getSubject(), relation.getAction(), context, objects, tables);
            BehaviorAction targetAction = BehaviorAction.READ;
            if (relation.getAction() == BehaviorAction.RENAME) {
                targetAction = BehaviorAction.CREATE;
            }
            for (BehaviorObject target : relation.getTarget()) {
                resolveObject(target, targetAction, context, objects, tables);
            }
        }

        Map<TargetType, List<String>> scope = new EnumMap<>(TargetType.class);
        if (hasDatabase) {
            scope.put(TargetType.Schema, List.of(database));
        }
        scope.put(TargetType.Table, List.copyOf(tables));
        request.setResolvedResources(Map.copyOf(scope));
    }

    private void resolveObject(BehaviorObject resource, BehaviorAction action, QueryResourceContext context, Map<String, Value> objects, Set<String> tables) {
        TargetType type = resource.getObjectType();
        ObjectName name = resource.getObjectName();
        if (type == TargetType.Table || type == TargetType.View) {
            if (name == null || name.getCatalog() != null || !isPathSegment(name.getObjectName())) {
                throw unresolved();
            }
            // The owner has datasource-wide access even when the database resolves the unqualified name.
            if (name.getSchema() == null && context.dataSourceOwner()) {
                return;
            }
            if (!isPathSegment(name.getSchema())) {
                throw unresolved();
            }
            if (action == BehaviorAction.CREATE) {
                return;
            }

            String path = "/" + name.getSchema() + "/" + name.getObjectName() + "/";
            Value value = objects.computeIfAbsent(path, ignored -> context.selectObject().apply(Map.of(UmiTypes.Schema, name.getSchema()), name.getObjectName()));
            if (value instanceof RdbTable actual && name.getSchema().equals(actual.getSchema()) && name.getObjectName().equals(actual.getName())) {
                if (actual.getUmiType() == UmiTypes.View) {
                    resource.setObjectType(TargetType.View);
                } else if (actual.getUmiType() == UmiTypes.Table && "BASE TABLE".equals(actual.getTableType()) && type == TargetType.Table) {
                    if (!context.dataSourceOwner()) {
                        tables.add(path);
                    }
                    return;
                }
            }
            if (!context.dataSourceOwner()) {
                if (resource.getObjectType() == TargetType.View) {
                    throw ThirdPartyApiException.as().with(I18N.getMessage(GreptimeSqlI18nKeys.GREPTIMEDB_TEAM_VIEW_UNSUPPORTED));
                }
                throw unresolved();
            }
        } else if (type == TargetType.Schema || type == TargetType.Query) {
            if (name == null || (name.getSchema() != null && !isPathSegment(name.getSchema()))) {
                throw unresolved();
            }
            if (!context.dataSourceOwner() && type == TargetType.Schema && (name.getSchema() == null || action == BehaviorAction.READ)) {
                throw unresolved();
            }
        } else if (type != TargetType.Instance || !context.dataSourceOwner()) {
            throw unresolved();
        }
    }

    private static boolean isPathSegment(String value) {
        return value != null && !value.isBlank() && value.indexOf('/') < 0 && value.indexOf('\\') < 0;
    }

    private static ThirdPartyApiException unresolved() {
        return ThirdPartyApiException.as().with(I18N.getMessage(GreptimeSqlI18nKeys.GREPTIMEDB_SQL_SCOPE_UNRESOLVED));
    }
}
