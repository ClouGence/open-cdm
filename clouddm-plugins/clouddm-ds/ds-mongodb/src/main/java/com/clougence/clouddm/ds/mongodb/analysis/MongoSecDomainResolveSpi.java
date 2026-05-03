package com.clougence.clouddm.ds.mongodb.analysis;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.mongodb.analysis.secrules.MongoCmdDomain;
import com.clougence.clouddm.ds.mongodb.analysis.secrules.MongoCollectionDomain;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.mongodb.parser.MongoDslProvider;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MongoSecDomainResolveSpi implements SecDomainResolveSpi {

    public MongoSecDomainResolveSpi(MetaService metaService){
        ;
    }

    protected DslProvider dslProvider() {
        return MongoDslProvider.INSTANCE;
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase = //
                new com.clougence.dslpaser.ast.location.CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> domainList = new ArrayList<>();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), codeInfo.getQuery(), dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            StatementSet statementSet = DslHelper.parserDsl(dslProvider(), s.getScript());
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

                SecQueryType convert = MongoAnalysisHelper.convert(funcType);
                mongoCmdDomain.setSqlType(convert);
                mongoCmdDomain.setAuditKind(convert.getAuditKind());
                mongoCmdDomain.setFunc(funcStr);
                mongoCmdDomain.setDsType(dsType);
                mongoCmdDomain.setSplitScript(ss);
                domainList.add(mongoCmdDomain);
            }
        }
        return domainList;
    }
}
