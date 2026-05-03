package com.clougence.clouddm.ds.maxcompute.analysis;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.maxcompute.analysis.builder.McBuilderFactory;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.maxcompute.parser.McSqlDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class McSecDomainResolveSpi implements SecDomainResolveSpi, McSecDomainOptionKeys {

    private final MetaService metaService;

    public McSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected DslProvider dslProvider() {
        return McSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(McBuilderFactory domainBuilder, Parser parser) {
        return new McSQLParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        CodeLocation dslBase = new CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> domainList = new ArrayList<>();
        McConfig mcConfig = (McConfig) ctxInfo.getDataSourceConfig();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), codeInfo.getQuery(), dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            McBuilderFactory builder = new McBuilderFactory(this.metaService, mcConfig.getSchemaStyle());
            DslHelper.doVisitor(dslProvider(), s.getScript(), (lexer, parser) -> this.parserVisitor(builder, parser));
            List<RuleDomain> build;
//            if (ctxInfo.isDeepParser()) {
//                build = builder.build(ctxInfo.getCuid(), ctxInfo.getDsId(), ctxInfo.getLevelsParam());
//            } else {
                build = builder.build();
//            }
            for (RuleDomain domain : build) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }

        return domainList;
    }
}
