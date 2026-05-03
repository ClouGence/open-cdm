package com.clougence.clouddm.dsfamily.postgres.analysis;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.analysis.column.AbstractSelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.dsfamily.postgres.parser.PgDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class PgSelectColumnAnalysisSpi extends AbstractSelectColumnAnalysisSpi {

    protected PgSecDomainResolveSpi resolveSpi;

    public PgSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new PgSecDomainResolveSpi(metaService);
    }

    protected DslProvider dslProvider() {
        return PgDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory domainBuilder, Parser parser) {
        return new PgSQLParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        PgBuilderFactory builder = new PgBuilderFactory(this.metaService);
        DslHelper.doVisitor(dslProvider(), script, (lexer, parser) -> this.parserVisitor(builder, parser));

        List<SelectItem> selectItems = analyzeRealColumn(contextInfo.getCuid(), contextInfo.getDsId(), contextInfo.getLevelsParam(), builder.buildKeepOrigin());
        for (SelectItem selectItem : selectItems) {
            for (RealColumn column : selectItem.getColumns()) {
                if (StringUtils.isEmpty(column.getSchema())) {
                    column.setSchema(contextInfo.getLevelsParam().get(UmiTypes.Schema).toString());
                }
                if (StringUtils.isEmpty(column.getCatalog())) {
                    column.setCatalog(contextInfo.getLevelsParam().get(UmiTypes.Catalog).toString());
                }
            }
        }
        return selectItems;
    }

    @Override
    public boolean supportParseSelectColumn() {
        return true;
    }

}
