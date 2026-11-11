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
package com.clougence.sql.postgres.analysis.security;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaIndexedObject;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.analysis.security.builder.PgBuilderFactory;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.utils.StringUtils;

public class PgSecDomainResolveSpi implements SecDomainResolveSpi, PgSecDomainOptionKeys {

    private final MetaService        metaService;
    private final PgDslProvider      provider;
    private final PgSplitAnalysisSpi splitter;

    public PgSecDomainResolveSpi(MetaService metaService, PostgresVersion version){
        this(metaService, new PgDslProvider(version), new PgSplitAnalysisSpi(version));
    }

    protected PgSecDomainResolveSpi(MetaService metaService, PgDslProvider provider, PgSplitAnalysisSpi splitter){
        this.metaService = metaService;
        this.provider = provider;
        this.splitter = splitter;
    }

    public PostgresVersion version() {
        return provider.version();
    }

    protected DslProvider dslProvider() {
        return provider;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory domainBuilder, Parser parser) {
        return new PgSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader queryReader, int baseLine, int baseColumn, ContextInfo ctxInfo) {
        var scripts = this.splitter.splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return resolveStatement(dsType, reader, codeLine, codeColumn, ctxInfo).stream();
        }).onClose(scripts::close);
    }

    protected List<RuleDomain> resolveStatement(DataSourceType dsType, Reader queryReader, int baseLine, int baseColumn, ContextInfo ctxInfo) {
        CodeLocation dslBase = new CodeLocation(baseLine, baseColumn);
        List<RuleDomain> domainList = new ArrayList<>();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), queryReader, dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            PgBuilderFactory builder = new PgBuilderFactory(this.metaService);
            try (StringReader reader = new StringReader(s.getScript())) {
                DslHelper.doVisitor(dslProvider(), reader, (lexer, parser) -> this.parserVisitor(builder, parser));
            }
            List<RuleDomain> build;
            if (ctxInfo == null) {
                build = builder.build();
            } else {
                build = builder.build(ctxInfo.getCuid(), ctxInfo.getDsId(), ctxInfo.getLevelsParam());
            }
            this.resolveDropIndexOwner(build, ctxInfo);
            for (RuleDomain domain : build) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }

        return domainList;
    }

    private void resolveDropIndexOwner(List<RuleDomain> domains, ContextInfo ctxInfo) {
        for (RuleDomain domain : domains) {
            if (!(domain instanceof RdbIndexDomain index) || index.getSqlType() != RuleQueryType.DROP_INDEX) {
                continue;
            }
            this.fillIndexOwner(index, ctxInfo);
        }
    }

    private void fillIndexOwner(RdbIndexDomain index, ContextInfo ctxInfo) {
        if (this.metaService == null || ctxInfo == null || ctxInfo.getLevelsParam() == null) {
            throw new UnsupportedOperationException("unsupported SQL: cannot resolve owner table of index '" + index.getName() + "'.");
        }

        Map<UmiTypes, Object> levels = new HashMap<>(ctxInfo.getLevelsParam());
        if (StringUtils.isNotBlank(index.getSchema())) {
            levels.put(UmiTypes.Schema, index.getSchema());
        }
        MetaIndexedObject owner = this.metaService.fetchIndexedObject(ctxInfo.getCuid(), ctxInfo.getDsId(), levels, index.getName());
        if (owner == null || StringUtils.isBlank(owner.getSchema()) || StringUtils.isBlank(owner.getName())) {
            throw new UnsupportedOperationException("unsupported SQL: cannot resolve owner table of index '" + index.getName() + "'.");
        }

        index.setCatalog(owner.getCatalog());
        index.setSchema(owner.getSchema());
        index.setTableCatalog(owner.getCatalog());
        index.setTableSchema(owner.getSchema());
        index.setTableName(owner.getName());
    }
}
