package com.clougence.clouddm.ds.doris.analysis;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.doris.analysis.builder.DrBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.column.AbstractSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.doris.parser.DrDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class DrSelectColumnAnalysisSpi extends AbstractSelectColumnAnalysisSpi {

    public DrSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return DrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(DrBuilderFactory domainBuilder, Parser parser) {
        return new DrSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        DrBuilderFactory builder = new DrBuilderFactory(this.metaService);
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
