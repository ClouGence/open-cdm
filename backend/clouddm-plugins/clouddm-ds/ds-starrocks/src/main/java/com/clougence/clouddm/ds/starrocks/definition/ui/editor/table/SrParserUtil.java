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
package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;

import com.clougence.adapter.starrocks.StarRocksAttributeNames;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.clouddm.ds.starrocks.parser.SrDslProvider;
import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksBaseVisitor;
import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.utils.JsonUtils;

public class SrParserUtil extends StarRocksBaseVisitor<Void> implements SrTableEditorFields {

    private RdbTable rdbTable = new RdbTable();
    private Parser   parser;

    private SrParserUtil(){

    }

    public static RdbTable parser(String createTableSql) {
        SrParserUtil srParserUtil = new SrParserUtil();
        DslHelper.doVisitor(SrDslProvider.INSTANCE, createTableSql, (lexer, parser) -> {
            srParserUtil.parser = parser;
            return srParserUtil;
        });
        return srParserUtil.rdbTable;
    }

    private String getName(String name) {
        if (name.startsWith("`")) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    private String getText(RuleContext ruleContext) {
        return parser.getTokenStream().getText(ruleContext);
    }

    private String getText(Token start, Token end) {
        return parser.getTokenStream().getText(start, end);
    }

    private String getString(String text) {
        if (text.startsWith("'") || text.startsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitCreateTableStatement(StarRocksParser.CreateTableStatementContext ctx) {
        rdbTable.setName(getName(getText(ctx.qualifiedName())));

        rdbTable.setColumns(new LinkedHashMap<>());
        rdbTable.setIndices(new ArrayList<>());

        if (ctx.comment() != null) {
            rdbTable.setComment(getString(getText(ctx.comment().string())));
        }

        if (ctx.engineDesc() != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.ENGINE, getString(getText(ctx.engineDesc().identifier())));
        }

        if (ctx.EXTERNAL() != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.EXTERNAL, "true");
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.EXTERNAL, "false");
        }

        if (ctx.keyDesc() != null) {
            ctx.keyDesc().accept(this);
        }
        if (ctx.distributionDesc() != null) {
            ctx.distributionDesc().accept(this);
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_TYPE, null);
        }
        for (StarRocksParser.ColumnDescContext columnDescContext : ctx.columnDesc()) {
            columnDescContext.accept(this);
        }

        for (StarRocksParser.IndexDescContext indexDescContext : ctx.indexDesc()) {
            indexDescContext.accept(this);
        }
        return null;
    }

    @Override
    public Void visitKeyDesc(StarRocksParser.KeyDescContext ctx) {
        RdbPrimaryKey rdbPrimaryKey = new RdbPrimaryKey();

        rdbPrimaryKey.setName(ctx.getChild(0).getText() + " KEY");

        for (StarRocksParser.IdentifierContext identifierContext : ctx.identifierList().identifier()) {
            rdbPrimaryKey.addColumn(getName(getText(identifierContext)));
        }
        rdbTable.setAttribute(StarRocksAttributeNames.KEY_TYPE, ctx.getChild(0).getText());

        rdbTable.setPrimaryKey(rdbPrimaryKey);
        return null;
    }

    @Override
    public Void visitDistributionDesc(StarRocksParser.DistributionDescContext ctx) {
        if (ctx.HASH() != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_TYPE, "HASH");
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_TYPE, "RANDOM");
        }

        if (ctx.identifierList() != null) {
            List<Map<String, String>> columns = new ArrayList<>();
            for (StarRocksParser.IdentifierContext identifierContext : ctx.identifierList().identifier()) {
                Map<String, String> map = new HashMap<>();
                map.put(SPI_TABLE_DISTRIBUTED_COLUMNS_NAME, getName(getText(identifierContext)));
                columns.add(map);
            }
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS, JsonUtils.toJson(columns));
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS, null);
        }

        if (ctx.BUCKETS() != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.BUCKET_NUMBER, ctx.INTEGER_VALUE().getText());
        }

        return null;
    }

    @Override
    public Void visitColumnDesc(StarRocksParser.ColumnDescContext ctx) {
        RdbColumn rdbColumn = new RdbColumn();
        rdbColumn.setIndex(rdbTable.getColumns().size());
        rdbColumn.setName(getName(getText(ctx.identifier())));
        if (ctx.type() != null) {
            visitColumnType(ctx.type(), rdbColumn);
        }

        if (ctx.aggDesc() != null) {
            rdbColumn.setAttribute(StarRocksAttributeNames.AGG_TYPE, getText(ctx.aggDesc()));
        }

        if (ctx.comment() != null) {
            rdbColumn.setComment(getString(getText(ctx.comment().string())));
        }

        if (ctx.AUTO_INCREMENT() != null) {
            rdbColumn.setAttribute(StarRocksAttributeNames.AUTO_INCREMENT, "true");
        } else {
            rdbColumn.setAttribute(StarRocksAttributeNames.AUTO_INCREMENT, "false");
        }

        if (ctx.defaultDesc() != null) {
            if (ctx.defaultDesc().string() != null) {
                rdbColumn.setDefaultValue(getString(getText(ctx.defaultDesc().string())));
            } else if (ctx.defaultDesc().NULL() != null) {
                String text = getText(ctx.defaultDesc());
                rdbColumn.setDefaultValue(text.substring(text.indexOf(ctx.defaultDesc().DEFAULT().getText()) + 7));
            }
        }

        rdbTable.addColumn(rdbColumn);
        return null;
    }

    @Override
    public Void visitIndexDesc(StarRocksParser.IndexDescContext ctx) {
        RdbIndex rdbIndex = new RdbIndex();

        String name = getName(getText(ctx.indexName));
        List<String> indexColumn = new ArrayList<>();
        for (StarRocksParser.IdentifierContext identifierContext : ctx.identifierList().identifier()) {
            indexColumn.add(getName(getText(identifierContext)));
        }

        rdbIndex.setName(name);
        rdbIndex.setColumnList(indexColumn);

        if (ctx.indexType() != null) {
            rdbIndex.setAttribute(StarRocksAttributeNames.INDEX_TYPE, ctx.indexType().getChild(1).getText());
        }
        rdbIndex.setType(RdbIndexType.Normal);
        if (Objects.nonNull(ctx.comment())) {
            String comment = getString(getText(ctx.comment().string()));
            if (comment.equals("")) {
                rdbIndex.setComment("");
            } else {
                rdbIndex.setComment(comment.substring(1, comment.length() - 1));
            }
        }
        rdbTable.addIndex(rdbIndex);
        return null;
    }

    private void visitColumnType(StarRocksParser.TypeContext context, RdbColumn rdbColumn) {
        if (context.baseType() != null) {
            StarRocksTypes starRocksTypes = StarRocksTypes.valueOfCode(context.baseType().getChild(0).getText());
            rdbColumn.setSqlType(starRocksTypes);

            if (context.baseType().typeParameter() != null) {
                String length = context.baseType().typeParameter().INTEGER_VALUE().getText();
                if (starRocksTypes.isNumber() && !starRocksTypes.isAccurateDecimal()) {
                    rdbColumn.setNumericPrecision(Integer.valueOf(length));
                } else if (starRocksTypes.isDataOrTime()) {
                    rdbColumn.setDatetimePrecision(Integer.valueOf(length));
                } else if (starRocksTypes.isString() || starRocksTypes.isBinary()) {
                    rdbColumn.setCharLength(Long.valueOf(length));
                } else {
                    rdbColumn.setNumericPrecision(Integer.valueOf(length));
                }
            }
        } else if (context.decimalType() != null) {
            StarRocksTypes starRocksTypes = StarRocksTypes.valueOfCode(context.decimalType().getChild(0).getText());
            rdbColumn.setSqlType(starRocksTypes);
            if (context.decimalType().precision != null) {
                rdbColumn.setNumericPrecision(Integer.valueOf(context.decimalType().precision.getText()));
            }
            if (context.decimalType().scale != null) {
                rdbColumn.setNumericScale(Integer.valueOf(context.decimalType().scale.getText()));
            }
        } else {
            StarRocksTypes starRocksTypes = StarRocksTypes.valueOfCode(context.getChild(0).getChild(0).getText());
            rdbColumn.setSqlType(starRocksTypes);
        }

    }

}
