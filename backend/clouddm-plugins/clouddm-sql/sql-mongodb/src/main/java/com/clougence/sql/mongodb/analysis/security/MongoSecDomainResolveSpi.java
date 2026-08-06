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
package com.clougence.sql.mongodb.analysis.security;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.mongodb.analysis.security.domain.MongoCmdDomain;
import com.clougence.sql.mongodb.analysis.security.domain.MongoCollectionDomain;
import com.clougence.sql.mongodb.parser.MongoDslProvider;
import com.clougence.sql.mongodb.parser.MongoSplitAnalysisSpi;
import com.clougence.sql.mongodb.parser.ast.MongoFuncType;
import com.clougence.sql.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.sql.mongodb.parser.ast.commands.collection.CollectionFunc;

public class MongoSecDomainResolveSpi implements SecDomainResolveSpi {

    public MongoSecDomainResolveSpi(MetaService metaService){
    }

    protected DslProvider dslProvider() {
        return MongoDslProvider.INSTANCE;
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader queryReader, int baseLine, int baseColumn, ContextInfo ctxInfo) {
        var scripts = new MongoSplitAnalysisSpi().splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return resolveStatement(dsType, reader, codeLine, codeColumn, ctxInfo).stream();
        }).onClose(scripts::close);
    }

    private List<RuleDomain> resolveStatement(DataSourceType dsType, Reader queryReader, int baseLine, int baseColumn, ContextInfo ctxInfo) {
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

            StatementSet statementSet;
            try (StringReader reader = new StringReader(s.getScript())) {
                statementSet = DslHelper.parserDsl(dslProvider(), reader);
            }
            for (Statement statement : statementSet.getStatements()) {
                AbstractMongoFunc mongoFunc = (AbstractMongoFunc) statement;
                MongoCmdDomain mongoCmdDomain;
                if (statement instanceof CollectionFunc && ((CollectionFunc) statement).getFuncType() != MongoFuncType.AGGREGATE) {
                    MongoCollectionDomain mongoCollectionDomain = new MongoCollectionDomain();
                    mongoCollectionDomain.setCollection(((CollectionFunc) statement).getCollectionName());
                    mongoCmdDomain = mongoCollectionDomain;
                } else {
                    mongoCmdDomain = new MongoCmdDomain();
                }
                MongoFuncType funcType = mongoFunc.getFuncType();
                String funcStr = funcType.getFuncStr();

                RuleQueryType convert = MongoAnalysisHelper.convert(funcType);
                mongoCmdDomain.setSqlType(convert);
                mongoCmdDomain.setAuditKind(convert == RuleQueryType.READ ? SecQueryKind.QUERY : convert.getAuditKind());
                mongoCmdDomain.setFunc(funcStr);
                mongoCmdDomain.setDsType(dsType);
                mongoCmdDomain.setSplitScript(ss);
                domainList.add(mongoCmdDomain);
            }
        }
        return domainList;
    }
}
