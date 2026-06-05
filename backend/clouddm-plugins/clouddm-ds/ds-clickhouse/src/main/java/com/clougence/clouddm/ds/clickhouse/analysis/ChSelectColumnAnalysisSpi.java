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
package com.clougence.clouddm.ds.clickhouse.analysis;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.clickhouse.analysis.builder.ChBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.column.AbstractSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.clickhouse.parser.ChSqlDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class ChSelectColumnAnalysisSpi extends AbstractSelectColumnAnalysisSpi {

    public ChSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return ChSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(ChBuilderFactory domainBuilder, Parser parser) {
        return new ChSQLParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        ChBuilderFactory builder = new ChBuilderFactory(this.metaService);
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
