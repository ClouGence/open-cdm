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
package com.clougence.sql.mysql.analysis.behavior;

import java.util.*;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.analysis.reference.MySqlObjectReference;
import com.clougence.utils.StringUtils;

final class MyBehaviorRelationAssembler {

    private final String                     sql;
    private final List<MySqlObjectReference> references;
    private final Map<UmiTypes, Object>      levels;
    private final boolean[]                  consumed;
    private final List<BehaviorRelation>     relations = new ArrayList<>();

    MyBehaviorRelationAssembler(String sql, List<MySqlObjectReference> references, Map<UmiTypes, Object> levels){
        this.sql = sql == null ? "" : sql;
        this.references = references;
        this.levels = levels;
        this.consumed = new boolean[references.size()];
    }

    List<BehaviorRelation> assemble() {
        assembleRename();
        assembleGrantOrRevoke();
        assembleDefaultRoles();
        assembleImport();
        assembleExport();
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

    private void assembleRename() {
        for (int i = 0; i < references.size(); i++) {
            MySqlObjectReference source = references.get(i);
            if (consumed[i] || action(source.sqlType()) != BehaviorAction.RENAME || !source.require()) {
                continue;
            }
            for (int j = i + 1; j < references.size(); j++) {
                MySqlObjectReference target = references.get(j);
                if (!consumed[j] && target.sqlType() == source.sqlType() && !target.require() && target.targetType() == source.targetType()) {
                    addRelation(i, BehaviorAction.RENAME, List.of(j));
                    break;
                }
            }
        }
    }

    private void assembleGrantOrRevoke() {
        BehaviorAction relationAction = null;
        for (MySqlObjectReference reference : references) {
            BehaviorAction current = action(reference.sqlType());
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
            if (action(references.get(i).sqlType()) != relationAction) {
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
        if ((MyBehaviorText.afterStartingWords(normalized, "GRANT", "PROXY") >= 0 || MyBehaviorText.afterStartingWords(normalized, "REVOKE", "PROXY") >= 0)
            && principals.size() > 1) {
            addRelation(principals.get(0), relationAction, distinctByLocation(principals.subList(1, principals.size())));
        }
    }

    private void assembleDefaultRoles() {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        int alterUserEnd = MyBehaviorText.afterStartingWords(normalized, "ALTER", "USER");
        if (!(normalized.startsWith("SET DEFAULT ROLE") || alterUserEnd >= 0 && MyBehaviorText.containsWords(normalized.substring(alterUserEnd), "DEFAULT", "ROLE"))) {
            return;
        }
        List<Integer> users = all(reference -> reference.targetType() == TargetType.User && action(reference.sqlType()) == BehaviorAction.ALTER);
        List<Integer> roles = all(reference -> reference.targetType() == TargetType.Role && action(reference.sqlType()) == BehaviorAction.ALTER);
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
            MySqlObjectReference reference = references.get(index);
            String key = reference.targetType() + "|" + reference.startLine() + ":" + reference.startColumn() + "~" + reference.endLine() + ":" + reference.endColumn();
            distinct.putIfAbsent(key, index);
        }
        return new ArrayList<>(distinct.values());
    }

    private void assembleImport() {
        int table = first(reference -> action(reference.sqlType()) == BehaviorAction.IMPORT && reference.targetType() == TargetType.Table);
        List<Integer> files = all(reference -> action(reference.sqlType()) == BehaviorAction.IMPORT && reference.targetType() == TargetType.File);
        if (table >= 0 && !files.isEmpty()) {
            addRelation(table, BehaviorAction.IMPORT, files);
        }
    }

    private void assembleExport() {
        int file = first(reference -> action(reference.sqlType()) == BehaviorAction.EXPORT && reference.targetType() == TargetType.File);
        if (file < 0) {
            return;
        }
        List<Integer> sources = all(reference -> action(reference.sqlType()) == BehaviorAction.READ && isDataObject(reference.targetType()));
        if (!sources.isEmpty()) {
            addRelation(file, BehaviorAction.EXPORT, sources);
        }
    }

    private void assembleIndexRelation() {
        int table = -1;
        for (int i = 0; i < references.size(); i++) {
            BehaviorAction tableAction = action(references.get(i).sqlType());
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
            BehaviorAction indexAction = action(references.get(i).sqlType());
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
            if (!consumed[i] && action(references.get(i).sqlType()) == BehaviorAction.READ && isDataObject(references.get(i).targetType())) {
                hasDataDependency = true;
                break;
            }
        }
        if (related && !hasDataDependency) {
            consumed[table] = true;
        }
    }

    private void assembleTriggerRelation() {
        int trigger = first(reference -> reference.targetType() == TargetType.Trigger && action(reference.sqlType()) == BehaviorAction.CREATE);
        if (trigger < 0) {
            return;
        }
        int table = first(reference -> reference.targetType() == TargetType.Table && action(reference.sqlType()) == BehaviorAction.ALTER);
        if (table >= 0) {
            addRelation(trigger, BehaviorAction.CREATE, List.of(table));
        }
    }

    private void assembleDataDependencies() {
        int subject = first(reference -> isDependencySubject(reference.sqlType()));
        if (subject < 0) {
            return;
        }
        List<Integer> sources = all(reference -> action(reference.sqlType()) == BehaviorAction.READ && isDataObject(reference.targetType()));
        if (sources.isEmpty()) {
            return;
        }
        BehaviorAction behaviorAction = action(references.get(subject).sqlType());
        if ((behaviorAction == BehaviorAction.CREATE || behaviorAction == BehaviorAction.ALTER) && MyBehaviorText.containsWords(sql, "OR", "REPLACE")) {
            behaviorAction = BehaviorAction.REPLACE;
        }
        addRelation(subject, behaviorAction, sources);
    }

    private boolean isDependencySubject(SecQueryType type) {
        return switch (type) {
            case CREATE_TABLE, ALTER_TABLE, CREATE_VIEW, ALTER_VIEW, INSERT, UPDATE, DELETE, MERGE -> true;
            default -> false;
        };
    }

    private boolean isDataObject(TargetType type) {
        return type == TargetType.Table || type == TargetType.View || type == TargetType.Materialized;
    }

    private void addUnary(int index) {
        MySqlObjectReference reference = references.get(index);
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(toObject(reference));
        relation.setAction(action(reference.sqlType()));
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

    private BehaviorObject toObject(MySqlObjectReference reference) {
        BehaviorObject object = new BehaviorObject();
        object.setTargetType(reference.targetType());
        object.setResourcePath(resourcePath(reference.nodes()));
        object.setStartLine(reference.startLine());
        object.setStartColumn(reference.startColumn());
        object.setEndLine(reference.endLine());
        object.setEndColumn(reference.endColumn());
        return object;
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
        String key = object.getTargetType() + "|" + object.getResourcePath();
        if (object.getResourcePath().equals(currentSchemaPath()) && object.getTargetType() != TargetType.Catalog && object.getTargetType() != TargetType.Schema) {
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

    static BehaviorAction action(SecQueryType type) {
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
        if (name.startsWith("ADMIN_") || name.startsWith("MAINTAIN_") || type == SecQueryType.ADMIN || type == SecQueryType.UNSAFE) {
            return BehaviorAction.ADMIN;
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
            case DATA_EXPORT -> BehaviorAction.EXPORT;
            case QUERY_LOCK, SESSION_LOCK -> BehaviorAction.LOCK;
            case SESSION_SETTING_WRITE, SYSTEM_SETTING_WRITE -> BehaviorAction.CONFIGURE;
            default -> BehaviorAction.OTHER;
        };
    }

    @FunctionalInterface
    private interface ReferencePredicate {

        boolean test(MySqlObjectReference reference);
    }
}
