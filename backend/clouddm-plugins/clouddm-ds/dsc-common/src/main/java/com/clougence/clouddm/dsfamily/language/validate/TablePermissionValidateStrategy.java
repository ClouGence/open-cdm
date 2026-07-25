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
package com.clougence.clouddm.dsfamily.language.validate;

import java.util.*;

import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class TablePermissionValidateStrategy implements ValidateStrategy {

    private static final String ALLOWED_TABLES_OPTION = "allowedTables";

    @Override
    public boolean match(ValidateContext context) {
        return !context.getStatementStates().isEmpty();
    }

    @Override
    public List<Diagnostic> validate(ValidateContext context, MetaService metaService) {
        Set<String> allowedTables = allowedTables(context);
        if (allowedTables.isEmpty() && metaService == null) {
            return List.of();
        }

        List<TableResource> tableResources = resolveTableResources(context, metaService);
        Set<String> knownTables = knownTables(context, metaService, tableResources);
        List<Diagnostic> diagnostics = new ArrayList<>();
        for (TableResource tableResource : tableResources) {
            String table = tableResource.table();
            if (StringUtils.isBlank(table)) {
                continue;
            }

            String tableKey = table.toLowerCase(Locale.ROOT);
            if (!knownTables.isEmpty() && !knownTables.contains(tableKey)) {
                diagnostics.add(ValidateDiagnostics.error("Unknown or inaccessible table: " + table, //
                        ValidateDiagnostics.tokenRange(context.getSqlText(), table, tableResource.range())));
                continue;
            }

            if (allowedTables.isEmpty() || allowedTables.contains(tableKey)) {
                continue;
            }

            diagnostics.add(ValidateDiagnostics.error("No permission to access table: " + table, //
                    ValidateDiagnostics.tokenRange(context.getSqlText(), table, tableResource.range())));
        }
        return diagnostics;
    }

    protected List<TableResource> resolveTableResources(ValidateContext context, MetaService metaService) {
        SqlParserParameters parameters = new SqlParserParameters(context.getRequest().getSqlParameters());
        BehaviorAnalysisSpi behaviorAnalysisSpi = context.getRequest().getSqlEngine().behaviorAnalysisSpi(parameters);
        if (behaviorAnalysisSpi == null) {
            return List.of();
        }

        List<TableResource> tableResources = new ArrayList<>();
        for (ValidateStatementState state : context.getStatementStates()) {
            Map<UmiTypes, Object> levelsParam = context.getRequest().getLevelsParam();
            int lineNumber = state.getRange().getStartPosition().getLineNumber();
            int columnNumber = state.getRange().getStartPosition().getColumnNumber();
            List<StatementBehavior> behaviors = behaviorAnalysisSpi.analysisBehavior(state.getSqlText(), levelsParam, lineNumber, columnNumber);
            if (behaviors == null) {
                continue;
            }
            for (StatementBehavior behavior : behaviors) {
                if (behavior == null || behavior.getRelations() == null) {
                    continue;
                }
                for (BehaviorRelation relation : behavior.getRelations()) {
                    if (relation == null) {
                        continue;
                    }
                    addTableResource(tableResources, relation.getSubject());
                    if (relation.getTarget() != null) {
                        for (BehaviorObject target : relation.getTarget()) {
                            addTableResource(tableResources, target);
                        }
                    }
                }
            }
        }

        return tableResources;
    }

    private static void addTableResource(List<TableResource> resources, BehaviorObject object) {
        if (object == null || !isTableResource(object.getTargetType()) || StringUtils.isBlank(object.getResourcePath())) {
            return;
        }

        String[] nodes = object.getResourcePath().split("/");
        for (int i = nodes.length - 1; i >= 0; i--) {
            if (StringUtils.isNotBlank(nodes[i])) {
                BlockLocation objectRange = new BlockLocation();
                objectRange.setStartPosition(new CodeLocation(object.getStartLine(), object.getStartColumn()));
                objectRange.setEndPosition(new CodeLocation(object.getEndLine(), object.getEndColumn()));
                resources.add(new TableResource(nodes[i], objectRange));
                return;
            }
        }
    }

    private static Set<String> allowedTables(ValidateContext context) {
        Object value = context.getRequest().getOptions().get(ALLOWED_TABLES_OPTION);
        Set<String> allowedTables = new HashSet<>();
        if (value instanceof Collection<?>) {
            for (Object item : (Collection<?>) value) {
                addTable(allowedTables, item);
            }
        } else if (value instanceof String) {
            for (String item : ((String) value).split(",")) {
                addTable(allowedTables, item);
            }
        }
        return allowedTables;
    }

    private static void addTable(Set<String> allowedTables, Object value) {
        String table = StringUtils.toString(value).trim();
        if (StringUtils.isNotBlank(table)) {
            allowedTables.add(table.toLowerCase(Locale.ROOT));
        }
    }

    private static Set<String> knownTables(ValidateContext context, MetaService metaService, List<TableResource> tableResources) {
        if (metaService == null || tableResources.isEmpty()) {
            return Set.of();
        }

        try {
            List<MetaObj> metaObjs = metaService.cachedObjectNames(//
                    context.getRequest().getPrimaryUserId(), //
                    context.getRequest().getCurrentUserId(), //
                    context.getRequest().getDataSourceId(),  //
                    context.getRequest().getLevels(),        //
                    context.getRequest().getLevelsParam());
            if (metaObjs == null || metaObjs.isEmpty()) {
                return Set.of();
            }

            Set<String> result = new HashSet<>();
            for (MetaObj metaObj : metaObjs) {
                if (metaObj == null || !isTableLike(metaObj.getType()) || StringUtils.isBlank(metaObj.getName())) {
                    continue;
                }
                result.add(metaObj.getName().toLowerCase(Locale.ROOT));
            }
            return result;
        } catch (RuntimeException e) {
            return Set.of();
        }
    }

    private static boolean isTableLike(UmiTypes type) {
        return type == UmiTypes.Table || type == UmiTypes.View || type == UmiTypes.ExternalTable || type == UmiTypes.Materialized;
    }

    private static boolean isTableResource(TargetType type) {
        return type == TargetType.Table || type == TargetType.View || type == TargetType.Materialized;
    }

    protected record TableResource(String table, BlockLocation range) {
    }
}
