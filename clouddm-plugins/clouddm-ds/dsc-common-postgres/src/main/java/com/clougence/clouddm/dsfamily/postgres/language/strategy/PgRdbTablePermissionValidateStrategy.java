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
package com.clougence.clouddm.dsfamily.postgres.language.strategy;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.language.validate.ValidateContext;
import com.clougence.clouddm.dsfamily.language.validate.ValidateStatementState;
import com.clougence.clouddm.dsfamily.language.validate.rdb.RdbTablePermissionValidateStrategy;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSqlParserVisitor;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.dslpaser.antlr.DslHelper;

public class PgRdbTablePermissionValidateStrategy extends RdbTablePermissionValidateStrategy {

    @Override
    protected List<RuleDomain> resolveDomains(ValidateContext context, MetaService metaService) {
        List<RuleDomain> domains = new ArrayList<>();
        for (ValidateStatementState state : context.getStatementStates()) {
            PgBuilderFactory builder = new PgBuilderFactory(metaService);
            try {
                DslHelper.doVisitor(context.getDslProvider(), state.getSqlText(), (lexer, parser) -> new PgSqlParserVisitor(builder, parser));
            } catch (UnsupportedOperationException e) {
                continue;
            }

            SplitScript splitScript = toSplitScript(state);
            for (RuleDomain domain : builder.build()) {
                domain.setSplitScript(splitScript);
                domains.add(domain);
            }
        }
        return domains;
    }

    private static SplitScript toSplitScript(ValidateStatementState state) {
        SplitScript splitScript = new SplitScript();
        splitScript.setScript(state.getSqlText());
        splitScript.setBodyStartCodeLine(state.getRange().getStartPosition().getLineNumber());
        splitScript.setBodyEndCodeLine(state.getRange().getEndPosition().getLineNumber());
        splitScript.setBodyStartCodeColumn(state.getRange().getStartPosition().getColumnNumber());
        splitScript.setBodyEndCodeColumn(state.getRange().getEndPosition().getColumnNumber());
        return splitScript;
    }
}
