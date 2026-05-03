package com.clougence.clouddm.ds.oracle.analysis;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.oracle.analysis.builder.OraBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.column.AbstractSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.oracle.parser.OraDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class OraSelectColumnAnalysisSpi extends AbstractSelectColumnAnalysisSpi {

    public OraSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return OraDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(OraBuilderFactory domainBuilder, Parser parser) {
        return new OraSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        OraBuilderFactory builder = new OraBuilderFactory(this.metaService);
        DslHelper.doVisitor(dslProvider(), script, (lexer, parser) -> this.parserVisitor(builder, parser));

        List<SelectItem> selectItems = analyzeRealColumn(contextInfo.getCuid(), contextInfo.getDsId(), contextInfo.getLevelsParam(), builder.buildKeepOrigin());
        for (SelectItem selectItem : selectItems) {
            for (RealColumn column : selectItem.getColumns()) {
                if (StringUtils.isEmpty(column.getSchema())) {
                    column.setSchema(contextInfo.getLevelsParam().get(UmiTypes.Schema).toString());
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
