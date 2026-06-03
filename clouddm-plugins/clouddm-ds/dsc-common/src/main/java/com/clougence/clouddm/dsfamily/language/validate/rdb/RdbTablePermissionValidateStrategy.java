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
package com.clougence.clouddm.dsfamily.language.validate.rdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.dsfamily.language.validate.ValidateContext;
import com.clougence.clouddm.dsfamily.language.validate.ValidateDiagnostics;
import com.clougence.clouddm.dsfamily.language.validate.ValidateStrategy;
import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.StringUtils;

public abstract class RdbTablePermissionValidateStrategy implements ValidateStrategy {

    private static final String ALLOWED_TABLES_OPTION = "allowedTables";

    @Override
    public boolean match(ValidateContext context) {
        return !allowedTables(context).isEmpty();
    }

    @Override
    public List<Diagnostic> validate(ValidateContext context, MetaService metaService) {
        Set<String> allowedTables = allowedTables(context);
        if (allowedTables.isEmpty()) {
            return List.of();
        }

        List<Diagnostic> diagnostics = new ArrayList<>();
        for (RuleDomain domain : resolveDomains(context, metaService)) {
            for (Map<TargetType, String> resource : domain.resolveResource()) {
                String table = resource.get(TargetType.Table);
                if (StringUtils.isBlank(table) || allowedTables.contains(table.toLowerCase(Locale.ROOT))) {
                    continue;
                }

                diagnostics.add(ValidateDiagnostics.error(//
                    "No permission to access table: " + table, //
                    ValidateDiagnostics.tokenRange(context.getSqlText(), table, range(domain))));
            }
        }
        return diagnostics;
    }

    protected abstract List<RuleDomain> resolveDomains(ValidateContext context, MetaService metaService);

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

    private static BlockLocation range(RuleDomain domain) {
        if (domain.getSplitScript() == null) {
            return null;
        }

        BlockLocation range = new BlockLocation();
        range.setStartPosition(new CodeLocation(domain.getSplitScript().getBodyStartCodeLine(), domain.getSplitScript().getBodyStartCodeColumn()));
        range.setEndPosition(new CodeLocation(domain.getSplitScript().getBodyEndCodeLine(), domain.getSplitScript().getBodyEndCodeColumn()));
        return range;
    }
}
