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
package com.clougence.sql.iotdb;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.Trees;

import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.sql.iotdb.antlr.IotdbParser;
import com.clougence.utils.i18n.I18nUtils;

final class IotdbQueryResourceResolver implements QueryResourceResolver {

    private static final I18nUtils   I18N           = I18nUtils.initI18n(IotdbSqlI18nKeys.class);
    private static final Set<String> SAFE_FUNCTIONS = Set
        .of("NOW", "COUNT", "SUM", "AVG", "MIN", "MAX", "MIN_VALUE", "MAX_VALUE", "FIRST_VALUE", "LAST_VALUE", "MIN_TIME", "MAX_TIME");

    @Override
    public void resolve(QueryRequest request, Function<UmiTypes, List<String>> objectNames, Predicate<BehaviorRelation> permitted, QueryResourceContext context) {
        String currentModel = (String) context.levels().get(UmiTypes.Catalog);
        IotdbDslProvider provider = IotdbSqlEngineSpi.provider(currentModel);
        boolean tree = provider == IotdbDslProvider.TREE;
        String model = tree ? "TREE" : "TABLE";

        validateFunctions(request, provider);
        List<BehaviorRelation> relations = request.getRelations();
        if (relations == null || (relations.isEmpty() && (tree || !request.hasQueryType(SplitQueryType.SELECT)))) {
            throw unresolved();
        }

        Map<String, Value> objects = new HashMap<>();
        List<String> databases = null;
        for (BehaviorRelation relation : relations) {
            BehaviorObject resource = relation.getSubject();
            ObjectName name = resource.getObjectName();
            if (!model.equals(name.getCatalog())) {
                throw unresolved();
            }
            TargetType type = resource.getObjectType();
            if (tree && type == TargetType.Timeseries) {
                String path = name.getObjectName();
                if (!safeSegment(path) || path.indexOf('*') >= 0) {
                    throw unresolved();
                }
                if (databases == null) {
                    databases = context.listLevels().apply(List.of(UmiTypes.Catalog), Map.of(UmiTypes.Catalog, model)).stream().map(DsElement::getObjName).toList();
                }
                List<String> owners = databases.stream().filter(db -> path.startsWith(db + ".")).toList();
                if (owners.size() != 1) {
                    throw unresolved();
                }
                resolveTree(relation, owners.get(0), path, context, objects);
            } else if (!tree && (type == TargetType.Table || type == TargetType.View)) {
                resolveTable(relation, context, objects);
            } else {
                resolveNamespace(relation, context.dataSourceOwner());
            }
        }

        String database = (String) context.levels().get(UmiTypes.Schema);
        Map<TargetType, List<String>> scope = new HashMap<>();
        scope.put(TargetType.Catalog, List.of(model));
        if (!tree && database != null) {
            scope.put(TargetType.Schema, List.of(database));
        }
        if (!context.dataSourceOwner()) {
            for (BehaviorRelation relation : relations) {
                TargetType type = relation.getSubject().getObjectType();
                if ((type == TargetType.Table || type == TargetType.Timeseries) && relation.getAction() != BehaviorAction.CREATE) {
                    scope.computeIfAbsent(type, ignored -> new ArrayList<>()).add(relation.getSubject().getObjectPath());
                }
            }
        }
        scope.replaceAll((type, names) -> names.stream().distinct().toList());
        request.setResolvedResources(Map.copyOf(scope));
    }

    private void resolveTree(BehaviorRelation relation, String database, String path, QueryResourceContext context, Map<String, Value> objects) {
        if (!safeSegment(database) || !path.startsWith(database + ".")) {
            throw unresolved();
        }
        if (relation.getAction() != BehaviorAction.CREATE && !(context.dataSourceOwner() && relation.getAction() == BehaviorAction.INSERT)) {
            Value metadata = lookup(context, objects, "TREE", database, path);
            // A native alias may resolve to a different path and can be retargeted by schema changes.
            if (metadata == null || metadata.getUmiType() != UmiTypes.Timeseries || !path.equals(metadata.asValue())) {
                throw unresolved();
            }
            if (!context.dataSourceOwner() && !"BASE".equals(metadata.getAttributes().get("viewtype"))) {
                throw viewDenied();
            }
        }
        BehaviorObject resource = relation.getSubject();
        resource.setObjectName(new ObjectName("TREE", database, path));
        resource.setObjectPath("/TREE/" + database + "/" + path + "/");
    }

    private void resolveTable(BehaviorRelation relation, QueryResourceContext context, Map<String, Value> objects) {
        BehaviorObject resource = relation.getSubject();
        ObjectName name = resource.getObjectName();
        String database = name.getSchema();
        String table = name.getObjectName();
        if (!safeSegment(database) || !safeSegment(table) || !resource.getObjectPath().equals("/TABLE/" + database + "/" + table + "/")) {
            throw unresolved();
        }
        if (context.dataSourceOwner() || relation.getAction() == BehaviorAction.CREATE) {
            return;
        }
        if (resource.getObjectType() == TargetType.View) {
            throw viewDenied();
        }
        Value metadata = lookup(context, objects, "TABLE", database, table);
        if (metadata == null) {
            throw unresolved();
        }
        if (!(metadata instanceof RdbTable actual) || !table.equals(actual.getName()) || !database.equals(actual.getSchema()) || !"TABLE".equals(actual.getCatalog())) {
            throw unresolved();
        }
        if (actual.getUmiType() != UmiTypes.Table || !"BASE TABLE".equals(actual.getTableType())) {
            throw viewDenied();
        }
    }

    private void resolveNamespace(BehaviorRelation relation, boolean owner) {
        BehaviorObject resource = relation.getSubject();
        ObjectName name = resource.getObjectName();
        String database = name.getSchema();
        if (resource.getObjectType() == TargetType.Schema && relation.getAction() != BehaviorAction.READ) {
            if (!safeSegment(database) || ("TREE".equals(name.getCatalog()) && database.indexOf('*') >= 0)
                || !resource.getObjectPath().equals("/" + name.getCatalog() + "/" + database + "/")) {
                throw unresolved();
            }
        } else if (!owner) {
            throw unresolved();
        }
    }

    private Value lookup(QueryResourceContext context, Map<String, Value> objects, String model, String database, String name) {
        String key = database + "/" + name;
        return objects.computeIfAbsent(key, ignored -> context.selectObject().apply(Map.of(UmiTypes.Catalog, model, UmiTypes.Schema, database), name));
    }

    private void validateFunctions(QueryRequest request, IotdbDslProvider provider) {
        var lexer = provider.createLexer(CharStreams.fromString(request.getQueryBody()));
        var parser = (IotdbParser) provider.createParser(lexer);
        var statements = provider.statements(parser);
        if (statements.size() != 1) {
            throw unresolved();
        }
        boolean tree = provider == IotdbDslProvider.TREE;
        if (tree && Trees.findAllRuleNodes(statements.get(0), IotdbParser.RULE_treeNode)
            .stream()
            .map(node -> (IotdbParser.TreeNodeContext) node)
            .anyMatch(node -> !node.treeWildcard().isEmpty())) {
            throw unresolved();
        }
        int rule = tree ? IotdbParser.RULE_treeAtom : IotdbParser.RULE_tableAtom;
        for (var node : Trees.findAllRuleNodes(statements.get(0), rule)) {
            var atom = (ParserRuleContext) node;
            if (atom.getChildCount() > 1 && "(".equals(atom.getChild(1).getText()) && !SAFE_FUNCTIONS.contains(atom.getStart().getText().toUpperCase(Locale.ROOT))
                && !(atom instanceof IotdbParser.TableAtomContext tableAtom && tableAtom.EXISTS() != null)) {
                throw unresolved();
            }
        }
    }

    private boolean safeSegment(String value) {
        return value != null && !value.isBlank() && value.indexOf('/') < 0 && value.indexOf('\\') < 0;
    }

    private static ThirdPartyApiException unresolved() {
        return ThirdPartyApiException.as().with(I18N.getMessage(IotdbSqlI18nKeys.IOTDB_SQL_SCOPE_UNRESOLVED));
    }

    private static ThirdPartyApiException viewDenied() {
        return ThirdPartyApiException.as().with(I18N.getMessage(IotdbSqlI18nKeys.IOTDB_TEAM_VIEW_UNSUPPORTED));
    }
}
