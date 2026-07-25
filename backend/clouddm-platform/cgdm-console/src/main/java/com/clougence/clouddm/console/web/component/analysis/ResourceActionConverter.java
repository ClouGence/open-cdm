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
package com.clougence.clouddm.console.web.component.analysis;

import java.util.*;

import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

final class ResourceActionConverter {

    private static final Set<TargetType> LEVELS_BASED_TARGETS = EnumSet.of( //
        TargetType.Environment, TargetType.Instance, TargetType.Machine, //
        TargetType.UserOrRole, TargetType.User, TargetType.Role, TargetType.ConfigKey, TargetType.File, //
        TargetType.Query, TargetType.Update, TargetType.Delete, TargetType.Insert, TargetType.Call, //
        TargetType.Tablespace, TargetType.Log, TargetType.Library, TargetType.ResourceGroup, TargetType.Replication, //
        TargetType.PublicationSubscription, TargetType.Publication, TargetType.Subscription, TargetType.PrepareStatement);

    List<ResourceAction> convert(List<StatementBehavior> behaviors, String currentResourcePath, String instanceResourcePath) {
        if (behaviors == null || behaviors.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, ResourceAction> distinct = new LinkedHashMap<>();
        for (int statementIndex = 0; statementIndex < behaviors.size(); statementIndex++) {
            StatementBehavior behavior = behaviors.get(statementIndex);
            if (behavior == null || behavior.getRelations() == null || behavior.getRelations().isEmpty()) {
                continue;
            }
            for (BehaviorRelation relation : behavior.getRelations()) {
                append(distinct, statementIndex, behavior.getStatementType(), relation, currentResourcePath, instanceResourcePath);
            }
        }
        return new ArrayList<>(distinct.values());
    }

    private void append(Map<String, ResourceAction> result, int statementIndex, SplitQueryType statementType, BehaviorRelation relation, String currentResourcePath,
                        String instanceResourcePath) {
        if (relation == null || relation.getSubject() == null || relation.getAction() == null) {
            return;
        }

        BehaviorObject subject = relation.getSubject();
        List<BehaviorObject> targets = relation.getTarget();
        if (targets == null) {
            targets = Collections.emptyList();
        }

        switch (relation.getAction()) {
            case RENAME, MOVE -> {
                addSubject(result, statementIndex, statementType, BehaviorAction.DROP, subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addSubject(result, statementIndex, statementType, BehaviorAction.CREATE, target, currentResourcePath, instanceResourcePath));
            }
            case COPY -> {
                addDerived(result, statementIndex, statementType, BehaviorAction.READ, subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addDerived(result, statementIndex, statementType, BehaviorAction.COPY, target, currentResourcePath, instanceResourcePath));
            }
            case IMPORT -> {
                addSubject(result, statementIndex, statementType, BehaviorAction.IMPORT, subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addDerived(result, statementIndex, statementType, BehaviorAction.READ, target, currentResourcePath, instanceResourcePath));
            }
            case EXPORT -> {
                addSubject(result, statementIndex, statementType, BehaviorAction.EXPORT, subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addDerived(result, statementIndex, statementType, BehaviorAction.READ, target, currentResourcePath, instanceResourcePath));
            }
            case GRANT, REVOKE, TRANSFER -> {
                addSubject(result, statementIndex, statementType, relation.getAction(), subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addSubject(result, statementIndex, statementType, relation.getAction(), target, currentResourcePath, instanceResourcePath));
            }
            case CREATE, ALTER -> {
                addSubject(result, statementIndex, statementType, relation.getAction(), subject, currentResourcePath, instanceResourcePath);
                for (BehaviorObject target : targets) {
                    addDerived(result, statementIndex, statementType, relatedObjectAction(subject, target), target, currentResourcePath, instanceResourcePath);
                }
            }
            case INSERT, UPDATE, DELETE, MERGE, REPLACE -> {
                addSubject(result, statementIndex, statementType, relation.getAction(), subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addDerived(result, statementIndex, statementType, BehaviorAction.READ, target, currentResourcePath, instanceResourcePath));
            }
            default -> {
                addSubject(result, statementIndex, statementType, relation.getAction(), subject, currentResourcePath, instanceResourcePath);
                targets.forEach(target -> addSubject(result, statementIndex, statementType, relation.getAction(), target, currentResourcePath, instanceResourcePath));
            }
        }
    }

    private BehaviorAction relatedObjectAction(BehaviorObject subject, BehaviorObject target) {
        if (target != null && target.getTargetType() == TargetType.Table && subject != null && isTableOwnedObject(subject.getTargetType())) {
            return BehaviorAction.ALTER;
        }
        return BehaviorAction.READ;
    }

    private boolean isTableOwnedObject(TargetType type) {
        return type == TargetType.Index || type == TargetType.Constraint || type == TargetType.Trigger;
    }

    private void addSubject(Map<String, ResourceAction> result, int statementIndex, SplitQueryType statementType, BehaviorAction action, BehaviorObject object,
                            String currentResourcePath, String instanceResourcePath) {
        SecDataAuthKind authKind = action.getAuthKind();
        if ((action == BehaviorAction.CREATE || action == BehaviorAction.ALTER || action == BehaviorAction.DROP || action == BehaviorAction.OTHER) //
            && statementType != null && statementType != SplitQueryType.UNKNOWN) {
            authKind = statementType.getAuthKind();
        }
        add(result, statementIndex, statementType, action, authKind, object, currentResourcePath, instanceResourcePath);
    }

    private void addDerived(Map<String, ResourceAction> result, int statementIndex, SplitQueryType statementType, BehaviorAction action, BehaviorObject object,
                            String currentResourcePath, String instanceResourcePath) {
        add(result, statementIndex, statementType, action, action.getAuthKind(), object, currentResourcePath, instanceResourcePath);
    }

    private void add(Map<String, ResourceAction> result, int statementIndex, SplitQueryType statementType, BehaviorAction action, SecDataAuthKind authKind, BehaviorObject object,
                     String currentResourcePath, String instanceResourcePath) {
        if (object == null || action == null) {
            return;
        }

        ResourceAction resourceAction = new ResourceAction();
        resourceAction.setStatementIndex(statementIndex);
        resourceAction.setStatementType(statementType == null ? SplitQueryType.UNKNOWN : statementType);
        resourceAction.setAction(action);
        resourceAction.setAuthKind(authKind);
        resourceAction.setTargetType(object.getTargetType() == null ? TargetType.Unknown : object.getTargetType());
        String sourceResourcePath = normalizedPath(object.getResourcePath());
        String resourcePath = levelsBasedPath(resourceAction.getTargetType(), sourceResourcePath, currentResourcePath, instanceResourcePath);
        resourceAction.setSourceResourcePath(sourceResourcePath);
        resourceAction.setResourcePath(resourcePath);
        resourceAction.setLevelsBased(!Objects.equals(sourceResourcePath, resourcePath));
        resourceAction.setStartLine(object.getStartLine());
        resourceAction.setStartColumn(object.getStartColumn());
        resourceAction.setEndLine(object.getEndLine());
        resourceAction.setEndColumn(object.getEndColumn());
        boolean sessionScopedDrop = statementType == SplitQueryType.DROP_TABLE && action == BehaviorAction.DROP //
                                    && resourceAction.getTargetType() == TargetType.Table //
                                    && resourceAction.toDsResPath().getResPath().equals(currentResourcePath);
        resourceAction.setSkipPermission(sessionScopedDrop);

        String key = statementIndex + "|" + resourceAction.getAction() + "|" + resourceAction.getAuthKind() + "|" //
                     + resourceAction.getTargetType() + "|" + resourceAction.toDsResPath().getResPath();
        result.putIfAbsent(key, resourceAction);
    }

    private String levelsBasedPath(TargetType targetType, String sourceResourcePath, String currentResourcePath, String instanceResourcePath) {
        String currentPath = normalizedPath(currentResourcePath);
        String instancePath = normalizedPath(instanceResourcePath);
        if (Objects.equals(sourceResourcePath, currentPath) || !sourceResourcePath.startsWith(instancePath)) {
            return sourceResourcePath;
        }
        if (Objects.equals(sourceResourcePath, instancePath)) {
            return currentPath;
        }
        if (!LEVELS_BASED_TARGETS.contains(targetType) || sourceResourcePath.startsWith(currentPath)) {
            return sourceResourcePath;
        }
        return normalizedPath(currentPath + sourceResourcePath.substring(instancePath.length()));
    }

    private String normalizedPath(String path) {
        if (path == null || path.isBlank() || "/".equals(path)) {
            return "/";
        }
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.endsWith("/") ? normalized : normalized + "/";
    }
}
