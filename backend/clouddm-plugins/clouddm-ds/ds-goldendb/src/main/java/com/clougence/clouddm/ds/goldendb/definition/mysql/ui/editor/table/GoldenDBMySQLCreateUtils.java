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
package com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table;

import static com.clougence.adapter.goldendb.GoldenDBAttributeNames.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.goldendb.dialect.mysql.GoldenDBMySQLDialect;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyCreateUtils;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class GoldenDBMySQLCreateUtils extends MyCreateUtils {

    private static final Pattern SIMPLE_GROUPS = Pattern.compile("(?i)g\\d+(\\s*,\\s*g\\d+)*");
    private static final Pattern SAFE_CLAUSE   = Pattern.compile("[A-Za-z0-9_$`.,'\\\"\\s()\\[\\]+\\-*/]+", Pattern.DOTALL);

    @Override
    public Dialect getDialect() { return GoldenDBMySQLDialect.INSTANCE; }

    @Override
    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        List<String> statements = super.buildCreate(buildContext, catalog, schema, table, eTable);
        String distributionType = DISTRIBUTION_TYPE.getValue(eTable.getAttribute());
        if (StringUtils.isBlank(distributionType)) {
            return statements;
        }

        String baseStatement = statements.get(0).trim();
        if (baseStatement.endsWith(";")) {
            baseStatement = baseStatement.substring(0, baseStatement.length() - 1);
        }
        String distribution = buildDistribution(buildContext, eTable.getAttribute(), distributionType);
        return List.of(baseStatement + "\n" + distribution + ";");
    }

    private String buildDistribution(TriggerContext buildContext, Map<String, String> attributes, String distributionType) {
        String type = distributionType.toUpperCase(Locale.ROOT);
        String groups = requireSafeClause(DISTRIBUTION_GROUPS.getValue(attributes), "GoldenDB distribution groups are required.");

        return switch (type) {
            case "HASH" -> "DISTRIBUTED BY HASH (" + hashColumns(buildContext, attributes) + ") (" + requireSimpleGroups(groups) + ")";
            case "RANGE", "LIST" -> "DISTRIBUTED BY " + type + " (" + distributionExpression(attributes) + ") (" + groups + ")";
            case "DUPLICATE" -> "DISTRIBUTED BY DUPLICATE (" + requireSimpleGroups(groups) + ")";
            default -> throw new IllegalArgumentException("Unsupported GoldenDB distribution type: " + type);
        };
    }

    private String hashColumns(TriggerContext buildContext, Map<String, String> attributes) {
        String columnsJson = DISTRIBUTION_COLUMNS.getValue(attributes);
        if (StringUtils.isBlank(columnsJson)) {
            throw new IllegalArgumentException("GoldenDB hash distribution requires at least one column.");
        }
        List<String> columns = JsonUtils.toListUseType(columnsJson, String.class);
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("GoldenDB hash distribution requires at least one column.");
        }
        return columns.stream().map(column -> getDialect().fmtName(buildContext.isUseDelimited(), column)).reduce((left, right) -> left + ", " + right).orElseThrow();
    }

    private String distributionExpression(Map<String, String> attributes) {
        String expression = DISTRIBUTION_EXPRESSION.getValue(attributes);
        return requireSafeClause(expression, "GoldenDB distribution expression is required.");
    }

    private String requireSimpleGroups(String groups) {
        if (!SIMPLE_GROUPS.matcher(groups).matches()) {
            throw new IllegalArgumentException("GoldenDB HASH and DUPLICATE groups must use g<number> names separated by commas.");
        }
        return groups;
    }

    private String requireSafeClause(String value, String missingMessage) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(missingMessage);
        }
        String clause = value.trim();
        if (clause.contains(";") || clause.contains("--") || clause.contains("/*") || clause.contains("*/") || !SAFE_CLAUSE.matcher(clause).matches()) {
            throw new IllegalArgumentException("GoldenDB distribution clause contains unsupported characters.");
        }
        return clause;
    }
}
