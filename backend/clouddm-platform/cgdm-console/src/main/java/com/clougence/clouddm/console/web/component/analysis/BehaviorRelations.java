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

/**
 * Interprets behavior relations for console-side authorization, audit, and execution backfill.
 */
public final class BehaviorRelations {

    private static final Map<TargetType, SecDataAuthKind>                           AUTH_KIND_OVERRIDES    = buildAuthKindOverrides();
    private static final Map<BehaviorAction, Function<TargetType, SecDataAuthKind>> AUTH_KIND_RESOLVERS    = Map.ofEntries( //
            Map.entry(BehaviorAction.CREATE, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.ALTER, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.DROP, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.RENAME, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.DDL)), //
            Map.entry(BehaviorAction.READ, targetType -> backupAuthKind(targetType, SecDataAuthKind.READ)), //
            Map.entry(BehaviorAction.INSERT, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.UPDATE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.DELETE, targetType -> backupAuthKind(targetType, SecDataAuthKind.WRITE)), //
            Map.entry(BehaviorAction.MERGE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.REPLACE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.COPY, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.MOVE, targetType -> SecDataAuthKind.WRITE), //
            Map.entry(BehaviorAction.CALL, targetType -> SecDataAuthKind.PROGRAM), //
            Map.entry(BehaviorAction.IMPORT, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.EXPORT, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.GRANT, targetType -> SecDataAuthKind.MANAGE), //
            Map.entry(BehaviorAction.REVOKE, targetType -> SecDataAuthKind.MANAGE), //
            Map.entry(BehaviorAction.TRANSFER, targetType -> SecDataAuthKind.MANAGE), //
            Map.entry(BehaviorAction.LOCK, targetType -> null), //
            Map.entry(BehaviorAction.UNLOCK, targetType -> null), //
            Map.entry(BehaviorAction.CONFIGURE, targetType -> SecDataAuthKind.MANAGE), //
            Map.entry(BehaviorAction.SWITCH, targetType -> null), //
            Map.entry(BehaviorAction.ANALYZE, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.APPLY, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.CHECKPOINT, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.CHECKSUM, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.FLUSH, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.LOAD, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.OPTIMIZE, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.PURGE, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.REFRESH, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.REPAIR, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.RECOVER, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.RESET, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.RESTORE, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.START, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.STOP, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.TERMINATE, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.VALIDATE, targetType -> SecDataAuthKind.MAINTAIN), //
            Map.entry(BehaviorAction.UNSAFE, targetType -> SecDataAuthKind.UNSAFE), //
            Map.entry(BehaviorAction.UNKNOWN, targetType -> AUTH_KIND_OVERRIDES.getOrDefault(targetType, SecDataAuthKind.UNSAFE)));

    private static final Set<TargetType>                                            LEVELS_BASED_TARGETS   = EnumSet.of( //
            TargetType.Environment, TargetType.Instance, TargetType.Machine, //
            TargetType.UserOrRole, TargetType.User, TargetType.Role, TargetType.ConfigKey, TargetType.File, //
            TargetType.Query, TargetType.Update, TargetType.Delete, TargetType.Insert, TargetType.Call, //
            TargetType.Tablespace, TargetType.Log, TargetType.Library, TargetType.ResourceGroup, TargetType.Replication, //
            TargetType.PublicationSubscription, TargetType.Publication, TargetType.Subscription, TargetType.PrepareStatement, TargetType.Backup);
    /** Analysis-only objects which must never become independent authorization resources. */
    private static final Set<TargetType>                                            NON_PERMISSION_TARGETS = EnumSet.of(//
            TargetType.Column, TargetType.Constraint, TargetType.Query, TargetType.Insert, //
            TargetType.Update, TargetType.Delete, TargetType.Call);
    /** Child objects whose structural lifecycle mutates one explicit parent table. */
    private static final Set<TargetType>                                            TABLE_CHILD_TARGETS    = EnumSet.of(//
            TargetType.Column, TargetType.Index, TargetType.Constraint, TargetType.Trigger, TargetType.Partition);
    private static final Set<BehaviorAction>                                        STRUCTURAL_ACTIONS     = EnumSet.of(//
            BehaviorAction.CREATE, BehaviorAction.ALTER, BehaviorAction.DROP, BehaviorAction.RENAME, BehaviorAction.MOVE);

    private BehaviorRelations(){
    }

    private static Map<TargetType, SecDataAuthKind> buildAuthKindOverrides() {
        EnumMap<TargetType, SecDataAuthKind> overrides = new EnumMap<>(TargetType.class);
        registerSpaceAuthKinds(overrides);
        registerDdlAuthKinds(overrides);
        registerManageAuthKinds(overrides);
        registerMaintainAuthKinds(overrides);
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
                TargetType.Sequence, TargetType.Synonym, TargetType.Type, //
                TargetType.ProgramObject, TargetType.Function, TargetType.Procedure, //
                TargetType.Trigger, TargetType.Package, TargetType.Operator);
    }

    private static void registerManageAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.MANAGE, //
                TargetType.UserOrRole, TargetType.User, TargetType.Role, TargetType.Object, //
                TargetType.Event, TargetType.Job, TargetType.Link, //
                TargetType.Profile, TargetType.Context, TargetType.Queue, TargetType.QueueSubscriber, //
                TargetType.Pipe, TargetType.SchedulerObject, TargetType.SchemaObject, TargetType.Library, //
                TargetType.Replication, TargetType.PublicationSubscription, TargetType.Publication, TargetType.Subscription, //
                TargetType.Log, TargetType.ConfigKey, //
                TargetType.Policy, TargetType.RowAccessPolicy, TargetType.MaskingPolicy, TargetType.RedactionPolicy, TargetType.PartitionGroup);
    }

    private static void registerMaintainAuthKinds(Map<TargetType, SecDataAuthKind> overrides) {
        putAuthKinds(overrides, SecDataAuthKind.MAINTAIN, //
                TargetType.Environment, TargetType.Instance, TargetType.Machine, //
                TargetType.ResourceGroup, TargetType.Backup);
    }

    private static SecDataAuthKind backupAuthKind(TargetType targetType, SecDataAuthKind defaultKind) {
        return targetType == TargetType.Backup ? SecDataAuthKind.MAINTAIN : defaultKind;
    }

    private static void putAuthKinds(Map<TargetType, SecDataAuthKind> overrides, SecDataAuthKind authKind, TargetType... targetTypes) {
        Arrays.stream(targetTypes).forEach(targetType -> overrides.put(targetType, authKind));
    }

    public static List<BehaviorRequest> flattenResourceIgnoringPermission(Collection<BehaviorRelation> relations) {
        return flattenResource(null, null, relations, false);
    }

    public static List<BehaviorRequest> flattenResource(SysObjectRegistrySpi registry, String dbVersion,//
                                                        Collection<BehaviorRelation> relations) {
        return flattenResource(registry, dbVersion, relations, true);
    }

    private static List<BehaviorRequest> flattenResource(SysObjectRegistrySpi registry, String dbVersion,//
                                                         Collection<BehaviorRelation> relations, boolean permissionProjection) {
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
            if (permissionProjection) {
                validatePermissionObjects(relation.getAction(), subject, targets);
                if ((subject.getObjectType() == TargetType.Insert || subject.getObjectType() == TargetType.Update || subject.getObjectType() == TargetType.Delete)
                    && (relation.getAction() == BehaviorAction.INSERT || relation.getAction() == BehaviorAction.UPDATE || relation.getAction() == BehaviorAction.DELETE
                        || relation.getAction() == BehaviorAction.MERGE || relation.getAction() == BehaviorAction.REPLACE)) {
                    throw new IllegalArgumentException("DML permission relation requires a physical Table subject: " + subject.getObjectType());
                }
                if (requiresTableParentProjection(subject, relation.getAction())) {
                    projectTableChildRelation(requests, relation.getAction(), subject, targets, registry, dbVersion);
                    continue;
                }
            }
            switch (relation.getAction()) {
                case RENAME, MOVE -> {
                    addRequest(requests, BehaviorAction.DROP, subject, registry, dbVersion, permissionProjection);
                    BehaviorObject relatedSubject = subject;
                    targets.forEach(target -> {
                        addRequest(requests, relatedObjectAction(relatedSubject, target, BehaviorAction.CREATE), target, registry, dbVersion, permissionProjection);
                    });
                }
                case COPY -> {
                    addRequest(requests, BehaviorAction.READ, subject, registry, dbVersion, permissionProjection);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.COPY, target, registry, dbVersion, permissionProjection);
                    });
                }
                case IMPORT -> {
                    addRequest(requests, BehaviorAction.IMPORT, subject, registry, dbVersion, permissionProjection);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.READ, target, registry, dbVersion, permissionProjection);
                    });
                }
                case EXPORT -> {
                    addRequest(requests, BehaviorAction.EXPORT, subject, registry, dbVersion, permissionProjection);
                    targets.forEach(target -> {
                        addRequest(requests, BehaviorAction.READ, target, registry, dbVersion, permissionProjection);
                    });
                }
                case GRANT, REVOKE, TRANSFER -> {
                    addRequest(requests, relation.getAction(), subject, registry, dbVersion, permissionProjection);
                    targets.forEach(target -> {
                        addRequest(requests, relation.getAction(), target, registry, dbVersion, permissionProjection);
                    });
                }
                case CREATE, ALTER -> {
                    addRequest(requests, relation.getAction(), subject, registry, dbVersion, permissionProjection);
                    BehaviorObject relatedSubject = subject;
                    targets.forEach(target -> {
                        addRequest(requests, relatedObjectAction(relatedSubject, target, BehaviorAction.READ), target, registry, dbVersion, permissionProjection);
                    });
                }
                case DROP -> {
                    addRequest(requests, BehaviorAction.DROP, subject, registry, dbVersion, permissionProjection);
                    BehaviorObject relatedSubject = subject;
                    targets.forEach(target -> {
                        addRequest(requests, relatedObjectAction(relatedSubject, target, BehaviorAction.DROP), target, registry, dbVersion, permissionProjection);
                    });
                }
                case INSERT, UPDATE, DELETE, MERGE, REPLACE -> {
                    addRequest(requests, relation.getAction(), subject, registry, dbVersion, permissionProjection);
                    BehaviorObject relatedSubject = subject;
                    targets.forEach(target -> {
                        BehaviorAction targetAction = relatedSubject.getObjectType() == TargetType.Column && target.getObjectType() == TargetType.Table ? relation
                            .getAction() : BehaviorAction.READ;
                        addRequest(requests, targetAction, target, registry, dbVersion, permissionProjection);
                    });
                }
                default -> {
                    addRequest(requests, relation.getAction(), subject, registry, dbVersion, permissionProjection);
                    targets.forEach(target -> {
                        addRequest(requests, relation.getAction(), target, registry, dbVersion, permissionProjection);
                    });
                }
            }
        }
        return List.copyOf(requests.values());
    }

    private static boolean requiresTableParentProjection(BehaviorObject subject, BehaviorAction action) {
        if (subject == null || subject.getObjectType() == null) {
            return false;
        }
        return subject.getObjectType() == TargetType.Column || TABLE_CHILD_TARGETS.contains(subject.getObjectType()) && STRUCTURAL_ACTIONS.contains(action);
    }

    private static void projectTableChildRelation(Map<RequestKey, BehaviorRequest> requests, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets,
                                                  SysObjectRegistrySpi registry, String databaseVersion) {
        BehaviorObject parent = targets.stream().filter(Objects::nonNull).filter(target -> target.getObjectType() == TargetType.Table).findFirst().orElseThrow();
        String parentPath = DmDsUtils.normalizeResourcePath(parent.getObjectPath());
        BehaviorAction parentAction = STRUCTURAL_ACTIONS.contains(action) ? BehaviorAction.ALTER : action;
        addRequest(requests, parentAction, parent, registry, databaseVersion, true);

        for (BehaviorObject target : targets) {
            boolean sameParent = target != null && target.getObjectType() == TargetType.Table
                                 && Objects.equals(parentPath, DmDsUtils.normalizeResourcePath(target.getObjectPath()));
            if (target == null || sameParent || TABLE_CHILD_TARGETS.contains(target.getObjectType())) {
                continue;
            }
            // A child lifecycle changes only its parent table. Any additional object is a dependency,
            // never another structural mutation target.
            addRequest(requests, BehaviorAction.READ, target, registry, databaseVersion, true);
        }
    }

    private static void addRequest(Map<RequestKey, BehaviorRequest> requests, BehaviorAction action, BehaviorObject resource,//
                                   SysObjectRegistrySpi registry, String databaseVersion, boolean permissionProjection) {
        if (resource == null) {
            return;
        }
        TargetType targetType = Objects.requireNonNullElse(resource.getObjectType(), TargetType.Unknown);
        if (permissionProjection && NON_PERMISSION_TARGETS.contains(targetType)) {
            return;
        }
        String resourcePath = DmDsUtils.normalizeResourcePath(resource.getObjectPath());
        RequestKey key = new RequestKey(action, targetType, resourcePath);
        SecDataAuthKind authKind = requiredAuthKind(action, targetType);
        if (isPermissionExempt(registry, action, resource, databaseVersion)) {
            authKind = null;
        }
        requests.putIfAbsent(key, new BehaviorRequest(resource, action, authKind));
    }

    private static void validatePermissionObjects(BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        List<BehaviorObject> resources = new ArrayList<>(targets.size() + 1);
        resources.add(subject);
        resources.addAll(targets);
        if (resources.stream().anyMatch(resource -> resource != null && resource.getObjectType() == TargetType.Unknown)) {
            throw new IllegalArgumentException("Unknown/parameter behavior object cannot become a permission resource");
        }
        if (subject.getObjectType() == TargetType.Column || TABLE_CHILD_TARGETS.contains(subject.getObjectType()) && STRUCTURAL_ACTIONS.contains(action)) {
            Map<String, BehaviorObject> parents = new LinkedHashMap<>();
            targets.stream().filter(Objects::nonNull).filter(target -> target.getObjectType() == TargetType.Table).forEach(target -> {
                parents.putIfAbsent(DmDsUtils.normalizeResourcePath(target.getObjectPath()), target);
            });
            if (parents.size() != 1) {
                throw new IllegalArgumentException(
                    subject.getObjectType() + " permission relation requires exactly one explicit parent Table target for " + action + ", found " + parents.size());
            }
        }
    }

    private static boolean isPermissionExempt(SysObjectRegistrySpi registry, BehaviorAction action, BehaviorObject resource, String databaseVersion) {
        ObjectName name = resource.getObjectName();
        return registry != null && //
               name != null && //
               registry.isPermissionExempt(action, resource.getObjectType(), name.getCatalog(), name.getSchema(), name.getObjectName(), databaseVersion);
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

    private static BehaviorAction relatedObjectAction(BehaviorObject subject, BehaviorObject target, BehaviorAction defaultAction) {
        TargetType subjectType = subject == null ? null : subject.getObjectType();
        if (target != null && //
            target.getObjectType() == TargetType.Table && //
            TABLE_CHILD_TARGETS.contains(subjectType)) {
            return BehaviorAction.ALTER;
        }
        return defaultAction;
    }

    private record RequestKey(BehaviorAction action, TargetType targetType, String resourcePath) {
    }

}
