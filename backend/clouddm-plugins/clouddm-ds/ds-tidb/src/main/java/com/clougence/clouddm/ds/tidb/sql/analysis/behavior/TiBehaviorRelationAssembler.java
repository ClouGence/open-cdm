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
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.util.*;

import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBObjectReference;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

final class TiBehaviorRelationAssembler {

    private final String                    sql;
    private final SplitQueryType            statementType;
    private final BehaviorAction            statementAction;
    private final List<TiDBObjectReference> references;
    private final Map<UmiTypes, Object>     levels;
    private final boolean[]                 consumed;
    private final List<BehaviorRelation>    relations = new ArrayList<>();

    TiBehaviorRelationAssembler(String sql, SplitQueryType statementType, List<TiDBObjectReference> references, Map<UmiTypes, Object> levels){
        this.sql = sql == null ? "" : sql;
        this.statementType = statementType;
        this.statementAction = statementAction(this.sql, statementType);
        this.references = references;
        this.levels = levels;
        this.consumed = new boolean[references.size()];
    }

    List<BehaviorRelation> assemble() {
        assembleQueryWatch();
        assembleRename();
        assembleGrantOrRevoke();
        assembleDefaultRoles();
        assembleImport();
        assembleExport();
        assembleTableDefinition();
        assembleIndexRelation();
        assembleTriggerRelation();
        assembleDataDependencies();
        for (int i = 0; i < references.size(); i++) {
            if (!consumed[i]) {
                addUnary(i);
            }
        }

        Map<String, BehaviorRelation> distinct = new LinkedHashMap<>();
        for (BehaviorRelation relation : relations) {
            StringBuilder key = new StringBuilder(relation.getAction().name()).append('|').append(objectKey(relation.getSubject()));
            for (BehaviorObject target : relation.getTarget()) {
                key.append('|').append(objectKey(target));
            }
            distinct.putIfAbsent(key.toString(), relation);
        }
        return new ArrayList<>(distinct.values());
    }

    private void assembleTableDefinition() {
        if (statementType != SplitQueryType.CREATE_TABLE && statementType != SplitQueryType.ALTER_TABLE) {
            return;
        }
        int table = first(reference -> reference.targetType() == TargetType.Table && (action(reference) == BehaviorAction.CREATE || action(reference) == BehaviorAction.ALTER));
        if (table < 0) {
            return;
        }
        List<Integer> children = all(reference -> reference.targetType() == TargetType.Column || reference.targetType() == TargetType.Constraint);
        if (children.isEmpty()) {
            return;
        }

        BehaviorObject carrier = toObject(references.get(table));
        addUnary(table);
        for (Integer child : children) {
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(toObject(references.get(child)));
            relation.setAction(action(references.get(child)));
            relation.getTarget().add(carrier);
            relations.add(relation);
            consumed[child] = true;
        }
    }

    private void assembleQueryWatch() {
        int policy = first(reference -> reference.targetType() == TargetType.Policy && reference.sqlType() == SplitQueryType.ADMIN_RESOURCE_GROUP);
        if (policy < 0) {
            return;
        }
        List<Integer> resourceGroups = all(reference -> reference.targetType() == TargetType.ResourceGroup && action(reference) == BehaviorAction.READ);
        if (!resourceGroups.isEmpty()) {
            addRelation(policy, action(references.get(policy)), resourceGroups);
        }
    }

    private void assembleRename() {
        for (int i = 0; i < references.size(); i++) {
            TiDBObjectReference source = references.get(i);
            if (consumed[i] || action(source) != BehaviorAction.RENAME || !source.require()) {
                continue;
            }
            for (int j = i + 1; j < references.size(); j++) {
                TiDBObjectReference target = references.get(j);
                if (!consumed[j] && target.sqlType() == source.sqlType() && !target.require() && target.targetType() == source.targetType()) {
                    addRelation(i, BehaviorAction.RENAME, List.of(j));
                    break;
                }
            }
        }
    }

    private void assembleGrantOrRevoke() {
        BehaviorAction relationAction = null;
        for (TiDBObjectReference reference : references) {
            BehaviorAction current = action(reference);
            if (current == BehaviorAction.GRANT || current == BehaviorAction.REVOKE) {
                relationAction = current;
                break;
            }
        }
        if (relationAction == null) {
            return;
        }

        List<Integer> resources = new ArrayList<>();
        List<Integer> principals = new ArrayList<>();
        for (int i = 0; i < references.size(); i++) {
            if (action(references.get(i)) != relationAction) {
                continue;
            }
            TargetType type = references.get(i).targetType();
            if (type == TargetType.UserOrRole || type == TargetType.User || type == TargetType.Role) {
                principals.add(i);
            } else {
                resources.add(i);
            }
        }

        if (!resources.isEmpty() && !principals.isEmpty()) {
            addRelation(resources.get(0), relationAction, distinctByLocation(principals));
            return;
        }

        List<Integer> roles = principals.stream().filter(index -> references.get(index).targetType() == TargetType.Role).toList();
        List<Integer> accounts = principals.stream().filter(index -> references.get(index).targetType() != TargetType.Role).toList();
        if (!roles.isEmpty() && !accounts.isEmpty()) {
            List<Integer> targets = distinctByLocation(accounts);
            for (Integer role : distinctByLocation(roles)) {
                BehaviorRelation relation = new BehaviorRelation();
                relation.setSubject(toObject(references.get(role)));
                relation.setAction(relationAction);
                for (Integer target : targets) {
                    relation.getTarget().add(toObject(references.get(target)));
                }
                relations.add(relation);
                consumed[role] = true;
            }
            targets.forEach(index -> consumed[index] = true);
            return;
        }

        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if ((TiBehaviorText.afterStartingWords(normalized, "GRANT", "PROXY") >= 0 || TiBehaviorText.afterStartingWords(normalized, "REVOKE", "PROXY") >= 0)
            && principals.size() > 1) {
            addRelation(principals.get(0), relationAction, distinctByLocation(principals.subList(1, principals.size())));
        }
    }

    private void assembleDefaultRoles() {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        int alterUserEnd = TiBehaviorText.afterStartingWords(normalized, "ALTER", "USER");
        if (!(normalized.startsWith("SET DEFAULT ROLE") || alterUserEnd >= 0 && TiBehaviorText.containsWords(normalized.substring(alterUserEnd), "DEFAULT", "ROLE"))) {
            return;
        }
        List<Integer> users = all(reference -> reference.targetType() == TargetType.User && action(reference) == BehaviorAction.ALTER);
        List<Integer> roles = all(reference -> reference.targetType() == TargetType.Role && action(reference) == BehaviorAction.ALTER);
        if (users.isEmpty() || roles.isEmpty()) {
            return;
        }
        List<Integer> targets = distinctByLocation(roles);
        for (Integer user : distinctByLocation(users)) {
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(toObject(references.get(user)));
            relation.setAction(BehaviorAction.ALTER);
            for (Integer role : targets) {
                relation.getTarget().add(toObject(references.get(role)));
            }
            relations.add(relation);
            consumed[user] = true;
        }
        targets.forEach(index -> consumed[index] = true);
    }

    private List<Integer> distinctByLocation(List<Integer> indexes) {
        Map<String, Integer> distinct = new LinkedHashMap<>();
        for (Integer index : indexes) {
            TiDBObjectReference reference = references.get(index);
            String key = reference.targetType() + "|" + reference.startLine() + ":" + reference.startColumn() + "~" + reference.endLine() + ":" + reference.endColumn();
            distinct.putIfAbsent(key, index);
        }
        return new ArrayList<>(distinct.values());
    }

    private void assembleImport() {
        int table = first(reference -> action(reference) == BehaviorAction.IMPORT && reference.targetType() == TargetType.Table);
        List<Integer> files = all(reference -> action(reference) == BehaviorAction.IMPORT && reference.targetType() == TargetType.File);
        if (table >= 0 && !files.isEmpty()) {
            addRelation(table, BehaviorAction.IMPORT, files);
        }
    }

    private void assembleExport() {
        int file = first(reference -> action(reference) == BehaviorAction.EXPORT && reference.targetType() == TargetType.File);
        if (file < 0) {
            return;
        }
        List<Integer> sources = all(reference -> action(reference) == BehaviorAction.READ && isDataObject(reference.targetType()));
        if (!sources.isEmpty()) {
            addRelation(file, BehaviorAction.EXPORT, sources);
        }
    }

    private void assembleIndexRelation() {
        int table = -1;
        for (int i = 0; i < references.size(); i++) {
            BehaviorAction tableAction = action(references.get(i));
            if (references.get(i).targetType() == TargetType.Table && (tableAction == BehaviorAction.CREATE || tableAction == BehaviorAction.ALTER)) {
                table = i;
                break;
            }
        }
        if (table < 0) {
            return;
        }

        BehaviorObject carrier = toObject(references.get(table));
        boolean related = false;
        for (int i = 0; i < references.size(); i++) {
            BehaviorAction indexAction = action(references.get(i));
            if (consumed[i] || references.get(i).targetType() != TargetType.Index
                || (indexAction != BehaviorAction.CREATE && indexAction != BehaviorAction.ALTER && indexAction != BehaviorAction.DROP && indexAction != BehaviorAction.RENAME)) {
                continue;
            }
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(toObject(references.get(i)));
            relation.setAction(indexAction);
            relation.getTarget().add(carrier);
            relations.add(relation);
            consumed[i] = true;
            related = true;
        }
        boolean hasDataDependency = false;
        for (int i = 0; i < references.size(); i++) {
            if (!consumed[i] && action(references.get(i)) == BehaviorAction.READ && isDataObject(references.get(i).targetType())) {
                hasDataDependency = true;
                break;
            }
        }
        if (related && !hasDataDependency) {
            consumed[table] = true;
        }
    }

    private void assembleTriggerRelation() {
        int trigger = first(reference -> reference.targetType() == TargetType.Trigger && action(reference) == BehaviorAction.CREATE);
        if (trigger < 0) {
            return;
        }
        int table = first(reference -> reference.targetType() == TargetType.Table && action(reference) == BehaviorAction.ALTER);
        if (table >= 0) {
            addRelation(trigger, BehaviorAction.CREATE, List.of(table));
        }
    }

    private void assembleDataDependencies() {
        int subject = first(reference -> isDependencySubject(reference.sqlType()));
        if (subject < 0) {
            return;
        }
        List<Integer> sources = all(reference -> action(reference) == BehaviorAction.READ && isDataObject(reference.targetType())
                                                 && !sameDataObject(references.get(subject), reference));
        if (sources.isEmpty()) {
            return;
        }
        BehaviorAction behaviorAction = action(references.get(subject));
        if ((behaviorAction == BehaviorAction.CREATE || behaviorAction == BehaviorAction.ALTER) && TiBehaviorText.containsWords(sql, "OR", "REPLACE")) {
            behaviorAction = BehaviorAction.REPLACE;
        }
        addRelation(subject, behaviorAction, sources);
        for (int index = 0; index < references.size(); index++) {
            if (!consumed[index] && action(references.get(index)) == BehaviorAction.READ && sameDataObject(references.get(subject), references.get(index))) {
                consumed[index] = true;
            }
        }
    }

    private boolean isDependencySubject(SplitQueryType type) {
        return switch (type) {
            case CREATE_TABLE, ALTER_TABLE, CREATE_VIEW, ALTER_VIEW, INSERT, UPDATE, DELETE, MERGE -> true;
            default -> false;
        };
    }

    private boolean isDataObject(TargetType type) {
        return type == TargetType.Table || type == TargetType.View || type == TargetType.Materialized;
    }

    private boolean sameDataObject(TiDBObjectReference left, TiDBObjectReference right) {
        if (left.targetType() != right.targetType() || left.nodes().size() != right.nodes().size()) {
            return false;
        }
        for (int index = 0; index < left.nodes().size(); index++) {
            if (!StringUtils.equalsIgnoreCase(left.nodes().get(index), right.nodes().get(index))) {
                return false;
            }
        }
        return true;
    }

    private void addUnary(int index) {
        TiDBObjectReference reference = references.get(index);
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(toObject(reference));
        relation.setAction(action(reference));
        relations.add(relation);
        consumed[index] = true;
    }

    private void addRelation(int subjectIndex, BehaviorAction behaviorAction, List<Integer> targetIndexes) {
        BehaviorRelation relation = new BehaviorRelation();
        BehaviorObject subject = toObject(references.get(subjectIndex));
        relation.setSubject(subject);
        relation.setAction(behaviorAction);
        Map<String, BehaviorObject> distinctTargets = new LinkedHashMap<>();
        for (Integer targetIndex : targetIndexes) {
            BehaviorObject target = toObject(references.get(targetIndex));
            distinctTargets.putIfAbsent(objectKey(target), target);
            consumed[targetIndex] = true;
        }
        relation.getTarget().addAll(distinctTargets.values());
        consumed[subjectIndex] = true;
        relations.add(relation);
    }

    private BehaviorObject toObject(TiDBObjectReference reference) {
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(reference.targetType());
        List<String> nodes = reference.nodes();
        List<String> displayNodes = nodes;
        if (nodes.size() == 1 && (reference.targetType() == TargetType.Function || reference.targetType() == TargetType.Table)) {
            displayNodes = new ArrayList<>();
            addLevel(displayNodes, UmiTypes.Catalog);
            addLevel(displayNodes, UmiTypes.Schema);
            displayNodes.add(nodes.get(0));
        }
        object.setObjectPath(resourcePath(displayNodes));
        if (!nodes.isEmpty() && reference.explicitName()) {
            object.setObjectName(objectName(reference.targetType(), nodes));
        }
        object.setStartLine(reference.startLine());
        object.setStartColumn(reference.startColumn());
        object.setEndLine(reference.endLine());
        object.setEndColumn(reference.endColumn());
        return object;
    }

    private ObjectName objectName(TargetType targetType, List<String> nodes) {
        if (targetType == TargetType.File) {
            return new ObjectName(null, null, nodes.get(0));
        }
        if (nodes.size() >= 3) {
            return new ObjectName(nodes.get(nodes.size() - 3), nodes.get(nodes.size() - 2), nodes.get(nodes.size() - 1));
        }
        if (nodes.size() == 2) {
            return new ObjectName(level(UmiTypes.Catalog), nodes.get(0), nodes.get(1));
        }
        return new ObjectName(null, null, nodes.get(0));
    }

    private void addLevel(List<String> nodes, UmiTypes type) {
        String value = level(type);
        if (StringUtils.isNotBlank(value)) {
            nodes.add(value);
        }
    }

    private String level(UmiTypes type) {
        return levels == null || levels.get(type) == null ? null : StringUtils.toString(levels.get(type));
    }

    private String resourcePath(List<String> nodes) {
        List<String> path = new ArrayList<>();
        if (levels != null && levels.get(UmiTypes.Instance) != null) {
            String instancePath = StringUtils.toString(levels.get(UmiTypes.Instance));
            int start = 0;
            for (int end = 0; end <= instancePath.length(); end++) {
                if (end < instancePath.length() && instancePath.charAt(end) != '/') {
                    continue;
                }
                String part = instancePath.substring(start, end);
                if (StringUtils.isNotBlank(part)) {
                    path.add(part);
                }
                start = end + 1;
            }
        }
        for (String node : nodes) {
            if (StringUtils.isNotBlank(node)) {
                path.add(node);
            }
        }
        return path.isEmpty() ? "/" : "/" + String.join("/", path) + "/";
    }

    private int first(ReferencePredicate predicate) {
        for (int i = 0; i < references.size(); i++) {
            if (!consumed[i] && predicate.test(references.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private List<Integer> all(ReferencePredicate predicate) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < references.size(); i++) {
            if (!consumed[i] && predicate.test(references.get(i))) {
                result.add(i);
            }
        }
        return result;
    }

    private String objectKey(BehaviorObject object) {
        ObjectName name = object.getObjectName();
        String key = object.getObjectType() + "|" + object.getObjectPath() + "|" + (name == null ? "" : name.getCatalog() + "|" + name.getSchema() + "|" + name.getObjectName());
        if (object.getObjectPath().equals(currentSchemaPath()) && object.getObjectType() != TargetType.Catalog && object.getObjectType() != TargetType.Schema) {
            key += "|" + object.getStartLine() + ":" + object.getStartColumn() + "~" + object.getEndLine() + ":" + object.getEndColumn();
        }
        return key;
    }

    private String currentSchemaPath() {
        List<String> nodes = new ArrayList<>();
        if (levels != null && levels.get(UmiTypes.Catalog) != null) {
            nodes.add(StringUtils.toString(levels.get(UmiTypes.Catalog)));
        }
        if (levels != null && levels.get(UmiTypes.Schema) != null) {
            nodes.add(StringUtils.toString(levels.get(UmiTypes.Schema)));
        }
        return resourcePath(nodes);
    }

    private BehaviorAction action(TiDBObjectReference reference) {
        BehaviorAction action = reference.action() == null ? defaultAction(reference.sqlType()) : reference.action();
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (statementType == SplitQueryType.ALTER_REPLICATION && reference.targetType() == TargetType.ConfigKey) {
            return statementAction;
        }
        if (statementAction == BehaviorAction.UNSAFE && isUnsafeReference(reference, action)) {
            return BehaviorAction.UNSAFE;
        }
        if (statementAction == BehaviorAction.IMPORT && normalized.startsWith("CLONE")
            && (action == BehaviorAction.CREATE || action == BehaviorAction.ALTER || action == BehaviorAction.UNKNOWN)) {
            return BehaviorAction.IMPORT;
        }
        if (reference.action() == null && normalized.startsWith("ALTER INSTANCE") && statementAction == BehaviorAction.ALTER) {
            return statementAction;
        }
        if (reference.action() == null && isOperationalType(reference.sqlType())) {
            return statementAction;
        }
        if (reference.action() == null && reference.targetType() == TargetType.Transaction) {
            return statementAction;
        }
        return action;
    }

    private static boolean isOperationalType(SplitQueryType type) {
        return switch (type) {
            case ADMIN, ADMIN_TABLE, ADMIN_PERFORMANCE, ADMIN_LOG, MAINTAIN_LOG, ADMIN_REPLICATION, ADMIN_RESOURCE_GROUP, ALTER_REPLICATION, PROGRAM_CONTROL -> true;
            default -> false;
        };
    }

    private static BehaviorAction statementAction(String sql, SplitQueryType type) {
        if (isUnsafeStatement(sql)) {
            return BehaviorAction.UNSAFE;
        }
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (type == SplitQueryType.PROGRAM_CONTROL && (normalized.startsWith("SIGNAL ") || normalized.startsWith("RESIGNAL"))) {
            return BehaviorAction.RAISE;
        }
        if (type == SplitQueryType.TRANSACTION) {
            if (normalized.startsWith("BEGIN") || normalized.startsWith("START TRANSACTION")) {
                return BehaviorAction.START;
            }
            if (normalized.startsWith("COMMIT")) {
                return BehaviorAction.STOP;
            }
            return BehaviorAction.RESET;
        }
        if (normalized.startsWith("QUERY WATCH ADD")) {
            return BehaviorAction.CREATE;
        }
        if (normalized.startsWith("QUERY WATCH REMOVE")) {
            return BehaviorAction.DROP;
        }
        if (normalized.startsWith("START REPLICA") || normalized.startsWith("START SLAVE") || normalized.startsWith("START GROUP_REPLICATION")) {
            return BehaviorAction.START;
        }
        if (normalized.startsWith("STOP REPLICA") || normalized.startsWith("STOP SLAVE") || normalized.startsWith("STOP GROUP_REPLICATION")) {
            return BehaviorAction.STOP;
        }
        if (normalized.startsWith("RESET REPLICA") || normalized.startsWith("RESET SLAVE") || normalized.startsWith("RESET BINARY LOGS") || normalized.startsWith("RESET MASTER")
            || normalized.startsWith("RESET QUERY CACHE")) {
            return BehaviorAction.RESET;
        }
        if (normalized.startsWith("CHANGE REPLICATION") || normalized.startsWith("CHANGE MASTER") || normalized.startsWith("ALTER INSTANCE") && type == SplitQueryType.ADMIN_LOG) {
            return BehaviorAction.ALTER;
        }
        if (normalized.startsWith("BINLOG ")) {
            return BehaviorAction.APPLY;
        }
        if (normalized.contains("FLUSH")) {
            return BehaviorAction.FLUSH;
        }
        if (normalized.startsWith("KILL ")) {
            return BehaviorAction.TERMINATE;
        }
        if (normalized.startsWith("PURGE BINARY LOGS") || normalized.startsWith("PURGE MASTER LOGS")) {
            return BehaviorAction.PURGE;
        }
        if (normalized.contains("CACHE INDEX") || normalized.contains("LOAD INDEX INTO CACHE")) {
            return BehaviorAction.LOAD;
        }
        if (normalized.contains("CHECK TABLE")) {
            return BehaviorAction.VALIDATE;
        }
        if (normalized.contains("CHECKSUM TABLE")) {
            return BehaviorAction.CHECKSUM;
        }
        if (normalized.contains("ANALYZE TABLE") || normalized.contains("ANALYZE NO_WRITE_TO_BINLOG TABLE") || normalized.contains("ANALYZE LOCAL TABLE")) {
            return BehaviorAction.ANALYZE;
        }
        if (normalized.contains("OPTIMIZE TABLE") || normalized.contains("OPTIMIZE NO_WRITE_TO_BINLOG TABLE") || normalized.contains("OPTIMIZE LOCAL TABLE")) {
            return BehaviorAction.OPTIMIZE;
        }
        if (normalized.contains("REPAIR TABLE") || normalized.contains("REPAIR NO_WRITE_TO_BINLOG TABLE") || normalized.contains("REPAIR LOCAL TABLE")) {
            return BehaviorAction.REPAIR;
        }
        if (normalized.contains("GTID_NEXT") || normalized.contains("PSEUDO_SLAVE_MODE")) {
            return BehaviorAction.CONFIGURE;
        }
        if (normalized.contains("SET RESOURCE GROUP") || type == SplitQueryType.ADMIN_RESOURCE_GROUP) {
            return BehaviorAction.SWITCH;
        }
        return defaultAction(type);
    }

    private boolean isUnsafeReference(TiDBObjectReference reference, BehaviorAction action) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("INSTALL PLUGIN") || normalized.startsWith("UNINSTALL PLUGIN") || normalized.startsWith("INSTALL COMPONENT")
            || normalized.startsWith("UNINSTALL COMPONENT")) {
            return reference.targetType() == TargetType.Library || reference.targetType() == TargetType.File;
        }
        if (normalized.startsWith("CREATE") && normalized.contains("FUNCTION") && normalized.contains("SONAME")) {
            return reference.targetType() == TargetType.Function || reference.targetType() == TargetType.Library || reference.targetType() == TargetType.File;
        }
        return action != BehaviorAction.READ && action != BehaviorAction.CALL;
    }

    private static BehaviorAction defaultAction(SplitQueryType type) {
        if (type == SplitQueryType.ADMIN || type == SplitQueryType.MAINTAIN_LOG) {
            return BehaviorAction.UNKNOWN;
        }
        String name = type.name();
        if (name.startsWith("CREATE_") || name.startsWith("ADD_")) {
            return BehaviorAction.CREATE;
        }
        if (name.startsWith("ALTER_") || name.startsWith("COMMENT_") || name.startsWith("TRUNCATE_")) {
            return BehaviorAction.ALTER;
        }
        if (name.startsWith("DROP_")) {
            return BehaviorAction.DROP;
        }
        if (name.startsWith("RENAME_")) {
            return BehaviorAction.RENAME;
        }
        if (name.startsWith("ADMIN_")) {
            return BehaviorAction.UNKNOWN;
        }
        if (type == SplitQueryType.UNSAFE) {
            return BehaviorAction.UNSAFE;
        }
        if (name.startsWith("SWITCH_")) {
            return BehaviorAction.SWITCH;
        }
        return switch (type) {
            case SELECT, METADATA, PERFORMANCE, LOG_READ, SESSION_VARIABLE_RW -> BehaviorAction.READ;
            case INSERT -> BehaviorAction.INSERT;
            case UPDATE -> BehaviorAction.UPDATE;
            case DELETE -> BehaviorAction.DELETE;
            case MERGE -> BehaviorAction.MERGE;
            case CALL_PROG_OBJ -> BehaviorAction.CALL;
            case GRANT -> BehaviorAction.GRANT;
            case REVOKE -> BehaviorAction.REVOKE;
            case TRANSFER_PRIVILEGE -> BehaviorAction.TRANSFER;
            case DATA_IMPORT -> BehaviorAction.IMPORT;
            case DATA_EXPORT, BACKUP -> BehaviorAction.EXPORT;
            case RESTORE -> BehaviorAction.RESTORE;
            case RECOVER -> BehaviorAction.RECOVER;
            case MAINTAIN_BACKUP -> BehaviorAction.UNKNOWN;
            case QUERY_LOCK, SESSION_LOCK -> BehaviorAction.LOCK;
            case SESSION_SETTING_WRITE, SYSTEM_SETTING_WRITE -> BehaviorAction.CONFIGURE;
            default -> BehaviorAction.UNKNOWN;
        };
    }

    private static boolean isUnsafeStatement(String sql) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        return normalized.startsWith("EXECUTE") || normalized.startsWith("PREPARE") || normalized.startsWith("DEALLOCATE PREPARE") || normalized.startsWith("RESTART")
               || normalized.startsWith("SHUTDOWN") || normalized.startsWith("BINLOG ") || normalized.startsWith("RESET MASTER") || normalized.startsWith("RESET BINARY LOGS")
               || normalized.startsWith("INSTALL PLUGIN") || normalized.startsWith("UNINSTALL PLUGIN") || normalized.startsWith("INSTALL COMPONENT")
               || normalized.startsWith("UNINSTALL COMPONENT") || normalized.startsWith("ALTER INSTANCE") && normalized.contains("DISABLE") && normalized.contains("REDO_LOG")
               || normalized.startsWith("CREATE") && normalized.contains("FUNCTION") && normalized.contains("SONAME") || normalized.contains("SQL_SLAVE_SKIP_COUNTER")
               || normalized.contains("GTID_PURGED") || normalized.contains("DEBUG") && normalized.contains("FORCE_FAKE_UUID");
    }

    @FunctionalInterface
    private interface ReferencePredicate {

        boolean test(TiDBObjectReference reference);
    }
}
