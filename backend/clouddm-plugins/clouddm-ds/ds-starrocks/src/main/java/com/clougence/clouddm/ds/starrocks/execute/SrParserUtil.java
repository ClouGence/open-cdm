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
package com.clougence.clouddm.ds.starrocks.execute;

import java.util.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

import com.clougence.adapter.starrocks.StarRocksAttributeNames;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.clouddm.ds.starrocks.definition.ui.editor.table.SrTableEditorFields;
import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.SrCreateTableBaseVisitor;
import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.SrCreateTableLexer;
import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.SrCreateTableParser;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.utils.JsonUtils;

public class SrParserUtil extends SrCreateTableBaseVisitor<Void> implements SrTableEditorFields {

    private final RdbTable rdbTable = new RdbTable();
    private Parser         parser;

    private SrParserUtil(){

    }

    public static RdbTable parser(String createTableSql) {
        SrParserUtil srParserUtil = new SrParserUtil();
        SrCreateTableLexer lexer = new SrCreateTableLexer(new UpperCaseCharStream(CharStreams.fromString(createTableSql)));
        SrCreateTableParser parser = new SrCreateTableParser(new CommonTokenStream(lexer));
        srParserUtil.parser = parser;
        srParserUtil.visit(parser.sqlStatements());
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
    public Void visitCreateTableStatement(SrCreateTableParser.CreateTableStatementContext ctx) {
        rdbTable.setName(getName(getText(ctx.qualifiedName())));

        rdbTable.setColumns(new LinkedHashMap<>());
        rdbTable.setIndices(new ArrayList<>());

        SrCreateTableParser.EngineDescContext engine = null;
        SrCreateTableParser.KeyDescContext key = null;
        SrCreateTableParser.CommentContext comment = null;
        SrCreateTableParser.DistributionDescContext distribution = null;
        for (SrCreateTableParser.TablePropertyContext property : ctx.tableProperty()) {
            if (property.engineDesc() != null) {
                engine = property.engineDesc();
            }
            if (property.keyDesc() != null) {
                key = property.keyDesc();
            }
            if (property.comment() != null) {
                comment = property.comment();
            }
            if (property.distributionDesc() != null) {
                distribution = property.distributionDesc();
            }
        }

        if (comment != null) {
            rdbTable.setComment(getString(getText(comment.string())));
        }

        if (engine != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.ENGINE, getString(getText(engine.identifier())));
        }

        if (ctx.EXTERNAL() != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.EXTERNAL, "true");
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.EXTERNAL, "false");
        }

        if (key != null) {
            key.accept(this);
        }
        if (distribution != null) {
            distribution.accept(this);
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_TYPE, null);
        }
        for (SrCreateTableParser.TableElementContext tableElement : ctx.tableElement()) {
            if (tableElement.columnDesc() != null) {
                tableElement.columnDesc().accept(this);
            } else if (tableElement.indexDesc() != null) {
                tableElement.indexDesc().accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitKeyDesc(SrCreateTableParser.KeyDescContext ctx) {
        RdbPrimaryKey rdbPrimaryKey = new RdbPrimaryKey();

        rdbPrimaryKey.setName(ctx.getChild(0).getText() + " KEY");

        for (SrCreateTableParser.IdentifierContext identifierContext : ctx.identifierList().identifier()) {
            rdbPrimaryKey.addColumn(getName(getText(identifierContext)));
        }
        rdbTable.setAttribute(StarRocksAttributeNames.KEY_TYPE, ctx.getChild(0).getText());

        rdbTable.setPrimaryKey(rdbPrimaryKey);
        return null;
    }

    @Override
    public Void visitDistributionDesc(SrCreateTableParser.DistributionDescContext ctx) {
        if (ctx.HASH() != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_TYPE, "HASH");
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_TYPE, "RANDOM");
        }

        if (ctx.identifierList() != null) {
            List<Map<String, String>> columns = new ArrayList<>();
            for (SrCreateTableParser.IdentifierContext identifierContext : ctx.identifierList().identifier()) {
                Map<String, String> map = new HashMap<>();
                map.put(SPI_TABLE_DISTRIBUTED_COLUMNS_NAME, getName(getText(identifierContext)));
                columns.add(map);
            }
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS, JsonUtils.toJson(columns));
        } else {
            rdbTable.setAttribute(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS, null);
        }

        if (ctx.bucketNumber != null) {
            rdbTable.setAttribute(StarRocksAttributeNames.BUCKET_NUMBER, ctx.bucketNumber.getText());
        }

        return null;
    }

    @Override
    public Void visitColumnDesc(SrCreateTableParser.ColumnDescContext ctx) {
        RdbColumn rdbColumn = new RdbColumn();
        rdbColumn.setIndex(rdbTable.getColumns().size());
        rdbColumn.setName(getName(getText(ctx.identifier())));
        if (ctx.type() != null) {
            visitColumnType(ctx.type(), rdbColumn);
        }

        SrCreateTableParser.AggDescContext aggDesc = null;
        SrCreateTableParser.CommentContext comment = null;
        SrCreateTableParser.DefaultDescContext defaultDesc = null;
        boolean autoIncrement = false;
        for (SrCreateTableParser.ColumnOptionContext option : ctx.columnOption()) {
            if (option.aggDesc() != null) {
                aggDesc = option.aggDesc();
            }
            if (option.comment() != null) {
                comment = option.comment();
            }
            if (option.defaultDesc() != null) {
                defaultDesc = option.defaultDesc();
            }
            if (option.AUTO_INCREMENT() != null) {
                autoIncrement = true;
            }
        }
        if (aggDesc != null) {
            rdbColumn.setAttribute(StarRocksAttributeNames.AGG_TYPE, getText(aggDesc));
        }

        if (comment != null) {
            rdbColumn.setComment(getString(getText(comment.string())));
        }

        if (autoIncrement) {
            rdbColumn.setAttribute(StarRocksAttributeNames.AUTO_INCREMENT, "true");
        } else {
            rdbColumn.setAttribute(StarRocksAttributeNames.AUTO_INCREMENT, "false");
        }

        if (defaultDesc != null) {
            if (defaultDesc.string() != null) {
                rdbColumn.setDefaultValue(getString(getText(defaultDesc.string())));
            } else if (defaultDesc.NULL() != null) {
                String text = getText(defaultDesc);
                rdbColumn.setDefaultValue(text.substring(text.indexOf(defaultDesc.DEFAULT().getText()) + 7));
            }
        }

        rdbTable.addColumn(rdbColumn);
        return null;
    }

    @Override
    public Void visitIndexDesc(SrCreateTableParser.IndexDescContext ctx) {
        RdbIndex rdbIndex = new RdbIndex();

        String name = getName(getText(ctx.indexName));
        List<String> indexColumn = new ArrayList<>();
        for (SrCreateTableParser.IdentifierContext identifierContext : ctx.identifierList().identifier()) {
            indexColumn.add(getName(getText(identifierContext)));
        }

        rdbIndex.setName(name);
        rdbIndex.setColumnList(indexColumn);

        if (ctx.indexType() != null) {
            rdbIndex.setAttribute(StarRocksAttributeNames.INDEX_TYPE, ctx.indexType().identifier().getText());
        }
        rdbIndex.setType(RdbIndexType.Normal);
        if (Objects.nonNull(ctx.comment())) {
            String comment = getString(getText(ctx.comment().string()));
            if (comment.equals("")) {
                rdbIndex.setComment("");
            } else {
                rdbIndex.setComment(comment);
            }
        }
        rdbTable.addIndex(rdbIndex);
        return null;
    }

    private void visitColumnType(SrCreateTableParser.TypeContext context, RdbColumn rdbColumn) {
        StarRocksTypes starRocksTypes = StarRocksTypes.valueOfCode(context.typeName().getText());
        rdbColumn.setSqlType(starRocksTypes);

        SrCreateTableParser.TypeParameterContext typeParameter = context.typeParameter();
        if (typeParameter != null) {
            String length = typeParameter.precision.getText();
            if (starRocksTypes.isNumber() && !starRocksTypes.isAccurateDecimal()) {
                rdbColumn.setNumericPrecision(Integer.valueOf(length));
            } else if (starRocksTypes.isDataOrTime()) {
                rdbColumn.setDatetimePrecision(Integer.valueOf(length));
            } else if (starRocksTypes.isString() || starRocksTypes.isBinary()) {
                rdbColumn.setCharLength(Long.valueOf(length));
            } else if (starRocksTypes.isAccurateDecimal()) {
                rdbColumn.setNumericPrecision(Integer.valueOf(length));
                if (typeParameter.scale != null) {
                    rdbColumn.setNumericScale(Integer.valueOf(typeParameter.scale.getText()));
                }
            } else {
                rdbColumn.setNumericPrecision(Integer.valueOf(length));
            }
        }

    }

    private static final class UpperCaseCharStream implements CharStream {

        private final CharStream stream;

        private UpperCaseCharStream(CharStream stream){
            this.stream = stream;
        }

        @Override
        public String getText(Interval interval) { return stream.getText(interval); }

        @Override
        public void consume() { stream.consume(); }

        @Override
        public int LA(int i) {
            int c = stream.LA(i);
            if (c <= 0) {
                return c;
            }
            return Character.toUpperCase(c);
        }

        @Override
        public int mark() { return stream.mark(); }

        @Override
        public void release(int marker) { stream.release(marker); }

        @Override
        public int index() { return stream.index(); }

        @Override
        public void seek(int index) { stream.seek(index); }

        @Override
        public int size() { return stream.size(); }

        @Override
        public String getSourceName() { return stream.getSourceName(); }
    }

}
