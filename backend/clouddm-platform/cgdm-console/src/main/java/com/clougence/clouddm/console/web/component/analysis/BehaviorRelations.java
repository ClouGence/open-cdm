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
import java.util.function.Function;

import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

/**
 * Interprets behavior relations for console-side authorization, audit, and execution backfill.
 */
public final class BehaviorRelations {

    private static final Map<TargetType, SecDataAuthKind>                           AUTH_KIND_OVERRIDES           = buildAuthKindOverrides();
    private static final Map<BehaviorAction, Function<TargetType, SecDataAuthKind>> AUTH_KIND_RESOLVERS           = Map.ofEntries( //
            Map.entry(BehaviorAction.CREATE, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.ALTER, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.DROP, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.RENAME, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.READ, targetType -> SecDataAuthKind.READ), //
            Map.entry(BehaviorAction.INSERT, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.UPDATE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.DELETE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.MERGE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.REPLACE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.IMPORT, targetType -> SecDataAuthKind.ADMIN), //
            Map.entry(BehaviorAction.EXPORT, targetType -> SecDataAuthKind.ADMIN), //
            Map.entry(BehaviorAction.CALL, targetType -> SecDataAuthKind.CALL), //
            Map.entry(BehaviorAction.GRANT, targetType -> SecDataAuthKind.AUTH), //
            Map.entry(BehaviorAction.REVOKE, targetType -> SecDataAuthKind.AUTH), //
            Map.entry(BehaviorAction.TRANSFER, targetType -> SecDataAuthKind.AUTH), //
            Map.entry(BehaviorAction.COPY, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.MOVE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.LOCK, targetType -> null), //
            Map.entry(BehaviorAction.CONFIGURE, targetType -> SecDataAuthKind.ADMIN), //
            Map.entry(BehaviorAction.SWITCH, targetType -> null), //
            Map.entry(BehaviorAction.ADMIN, targetType -> targetType == TargetType.UserOrRole || targetType == TargetType.User || targetType == TargetType.Role //
                ? SecDataAuthKind.AUTH : SecDataAuthKind.ADMIN), //
            Map.entry(BehaviorAction.UNSAFE, targetType -> SecDataAuthKind.UNSAFE), //
            Map.entry(BehaviorAction.OTHER, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.OTHER)));

    private static final Set<TargetType>                                            LEVELS_BASED_TARGETS          = EnumSet.of( //
            TargetType.Environment, TargetType.Instance, TargetType.Machine, //
            TargetType.UserOrRole, TargetType.User, TargetType.Role, TargetType.ConfigKey, TargetType.File, //
            TargetType.Query, TargetType.Update, TargetType.Delete, TargetType.Insert, TargetType.Call, //
            TargetType.Tablespace, TargetType.Log, TargetType.Library, TargetType.ResourceGroup, TargetType.Replication, //
            TargetType.PublicationSubscription, TargetType.Publication, TargetType.Subscription, TargetType.PrepareStatement);
    private static final Set<SplitQueryType>                                        PERMISSION_EXEMPT_QUERY_TYPES = EnumSet.of( //
            SplitQueryType.BLOCK, SplitQueryType.PROGRAM_CONTROL, SplitQueryType.TRANSACTION, //
            SplitQueryType.QUERY_LOCK, SplitQueryType.SESSION_LOCK);

    private BehaviorRelations(){
    }

    private static Map<TargetType, SecDataAuthKind> buildAuthKindOverrides() {
        EnumMap<TargetType, SecDataAuthKind> overrides = new EnumMap<>(TargetType.class);
        registerSpaceAuthKinds(overrides);
        registerDdlAuthKinds(overrides);
        registerProgrammableObjectAuthKinds(overrides);
        registerAuthKinds(overrides);
        registerAdminAuthKinds(overrides);
        return Collections.unmodifiableMap(overrides);
    }

    private static void registerSpaceAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.SPACE, //
                TargetType.Catalog, TargetType.Schema, TargetType.Tablespace);
    }

    private static void registerDdlAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.DDL,//
                TargetType.Table, TargetType.Column, TargetType.Constraint, TargetType.Index, //
                TargetType.Partition, TargetType.View, TargetType.Materialized, //
                TargetType.Sequence, TargetType.Synonym, TargetType.Type);
    }

    private static void registerProgrammableObjectAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.PROGRAM,//
                TargetType.ProgramObject, TargetType.Function, TargetType.Procedure, //
                TargetType.Trigger, TargetType.Event, TargetType.Job, TargetType.Operator, TargetType.Package);
    }

    private static void registerAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.AUTH, //
                TargetType.UserOrRole, TargetType.User, TargetType.Role, TargetType.Object);
    }

    private static void registerAdminAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.ADMIN, //
                TargetType.Environment, TargetType.Instance, TargetType.Machine, //
                TargetType.ResourceGroup, TargetType.Library, //
                TargetType.Replication, TargetType.PublicationSubscription, TargetType.Publication, TargetType.Subscription, //
                TargetType.Log, TargetType.ConfigKey, //
                TargetType.Policy, TargetType.RowAccessPolicy, TargetType.MaskingPolicy, TargetType.RedactionPolicy);
    }

    private static void putAuthKinds(Map<TargetType, SecDataAuthKind> overrides, SecDataAuthKind authKind, TargetType... targetTypes) {
        Arrays.stream(targetTypes).forEach(targetType -> overrides.put(targetType, authKind));
    }

    public static List<BehaviorRequest> flattenResourceIgnoringPermission(Collection<BehaviorRelation> relations) {
        return flattenResource(null, null, relations, null);
    }

    public static List<BehaviorRequest> flattenResource(SysObjectRegistrySpi registry, String dbVersion,//
                                                        Collection<BehaviorRelation> relations, Collection<SplitQueryType> queryTypes) {
        if (relations == null || relations.isEmpty()) {
            return Collections.emptyList();
        }
        Map<RequestKey, BehaviorRequest> requests = new LinkedHashMap<>();
        for (BehaviorRelation relation : relations) {
            if (relation == null || relation.getSubject() == null || relation.getAction() == null) {
                continue;
            }
            BehaviorObject subject = relation.getSubject();
            List<BehaviorObject> targets = relation.getTarget() == null ? List.of() : relation.getTarget();
            switch (relation.getAction()) {
                case RENAME, MOVE -> {
                    addRequest(requests, BehaviorAction.DROP, subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.CREATE, target, registry, queryTypes, dbVersion);
                    });
                }
                case COPY -> {
                    addRequest(requests, BehaviorAction.READ, subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.COPY, target, registry, queryTypes, dbVersion);
                    });
                }
                case IMPORT -> {
                    addRequest(requests, BehaviorAction.IMPORT, subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.READ, target, registry, queryTypes, dbVersion);
                    });
                }
                case EXPORT -> {
                    addRequest(requests, BehaviorAction.EXPORT, subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.READ, target, registry, queryTypes, dbVersion);
                    });
                }
                case GRANT, REVOKE, TRANSFER -> {
                    addRequest(requests, relation.getAction(), subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, relation.getAction(), target, registry, queryTypes, dbVersion);
                    });
                }
                case CREATE, ALTER -> {
                    addRequest(requests, relation.getAction(), subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, relatedObjectAction(subject, target), target, registry, queryTypes, dbVersion);
                    });
                }
                case INSERT, UPDATE, DELETE, MERGE, REPLACE -> {
                    addRequest(requests, relation.getAction(), subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.READ, target, registry, queryTypes, dbVersion);
                    });
                }
                default -> {
                    addRequest(requests, relation.getAction(), subject, registry, queryTypes, dbVersion);
                    targets.forEach(target -> {
                        addRequest(requests, relation.getAction(), target, registry, queryTypes, dbVersion);
                    });
                }
            }
        }
        return List.copyOf(requests.values());
    }

    private static void addRequest(Map<RequestKey, BehaviorRequest> requests, BehaviorAction action, BehaviorObject resource,//
                                   SysObjectRegistrySpi registry, Collection<SplitQueryType> queryTypes, String databaseVersion) {
        if (resource == null) {
            return;
        }
        TargetType targetType = Objects.requireNonNullElse(resource.getObjectType(), TargetType.Unknown);
        String resourcePath = DmDsUtils.normalizeResourcePath(resource.getObjectPath());
        RequestKey key = new RequestKey(action, targetType, resourcePath);
        SecDataAuthKind authKind = requiredAuthKind(action, targetType);
        if (isPermissionExempt(registry, queryTypes, action, resource, databaseVersion)) {
            authKind = null;
        }
        requests.putIfAbsent(key, new BehaviorRequest(resource, action, authKind));
    }

    private static boolean isPermissionExempt(SysObjectRegistrySpi registry, Collection<SplitQueryType> queryTypes,//
                                              BehaviorAction action, BehaviorObject resource, String databaseVersion) {
        TargetType targetType = resource.getObjectType();
        if (isCommonVirtualResource(queryTypes, action, targetType)) {
            return true;
        }
        ObjectName name = resource.getObjectName();
        return registry != null && //
               name != null && //
               registry.isPermissionExempt(action, targetType, name.getCatalog(), name.getSchema(), name.getObjectName(), databaseVersion);
    }

    private static boolean isCommonVirtualResource(Collection<SplitQueryType> queryTypes, BehaviorAction action, TargetType targetType) {
        if (queryTypes == null || action == null || targetType == null) {
            return false;
        }
        if ((action == BehaviorAction.OTHER || action == BehaviorAction.LOCK) && !queryTypes.isEmpty() && queryTypes.stream().allMatch(PERMISSION_EXEMPT_QUERY_TYPES::contains)) {
            return true;
        }
        return switch (action) {
            case READ -> targetType == TargetType.Log && queryTypes.contains(SplitQueryType.LOG_READ);
            case ADMIN -> targetType == TargetType.Instance && queryTypes.contains(SplitQueryType.ADMIN);
            case LOCK -> true;
            default -> false;
        };
    }

    private static SecDataAuthKind requiredAuthKind(BehaviorAction action, TargetType targetType) {
        Function<TargetType, SecDataAuthKind> resolver = Objects.requireNonNull(AUTH_KIND_RESOLVERS.get(action), "Unsupported behavior action: " + action);
        return resolver.apply(targetType);
    }

    public static String resourcePath(BehaviorObject object, String currentResourcePath, String instanceResourcePath) {
        if (object == null) {
            return "/";
        }

        String sourcePath = DmDsUtils.normalizeResourcePath(object.getObjectPath());
        String currentPath = DmDsUtils.normalizeResourcePath(currentResourcePath);
        String instancePath = DmDsUtils.normalizeResourcePath(instanceResourcePath);
        TargetType targetType = Objects.requireNonNullElse(object.getObjectType(), TargetType.Unknown);
        if (Objects.equals(sourcePath, currentPath) || !sourcePath.startsWith(instancePath)) {
            return sourcePath;
        }
        if (Objects.equals(sourcePath, instancePath)) {
            return currentPath;
        }
        if (!LEVELS_BASED_TARGETS.contains(targetType) || sourcePath.startsWith(currentPath)) {
            return sourcePath;
        }
        return DmDsUtils.normalizeResourcePath(currentPath + sourcePath.substring(instancePath.length()));
    }

    private static BehaviorAction relatedObjectAction(BehaviorObject subject, BehaviorObject target) {
        TargetType subjectType = subject == null ? null : subject.getObjectType();
        if (target != null && //
            target.getObjectType() == TargetType.Table && //
            (subjectType == TargetType.Index || subjectType == TargetType.Constraint || subjectType == TargetType.Trigger)) {
            return BehaviorAction.ALTER;
        }
        return BehaviorAction.READ;
    }

    private record RequestKey(BehaviorAction action, TargetType targetType, String resourcePath) {
    }

}
