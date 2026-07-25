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
package com.clougence.clouddm.ds.doris.execute;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;

import com.clougence.adapter.doris.DorisAttributeNames;
import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableBaseVisitor;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableLexer;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableParser;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.utils.JsonUtils;

public class DrParserUtil extends DrCreateTableBaseVisitor<Void> {

    private static final String FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME = "name";
    private final RdbTable      rdbTable                                = new RdbTable();
    private Parser              parser;

    private DrParserUtil(){

    }

    public static RdbTable parseTable(String createTableSql) {
        DrParserUtil parserUtil = new DrParserUtil();
        DrCreateTableLexer lexer = new DrCreateTableLexer(new UpperCaseCharStream(CharStreams.fromString(createTableSql)));
        DrCreateTableParser parser = new DrCreateTableParser(new CommonTokenStream(lexer));
        parserUtil.parser = parser;
        parserUtil.visit(parser.singleStatement());
        return parserUtil.rdbTable;
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
    public Void visitCreateTable(DrCreateTableParser.CreateTableContext ctx) {
        rdbTable.setName(getName(getText(ctx.name)));
        rdbTable.setColumns(new LinkedHashMap<>());

        DrCreateTableParser.EngineDescContext engine = null;
        DrCreateTableParser.KeyDescContext key = null;
        DrCreateTableParser.CommentClauseContext comment = null;
        DrCreateTableParser.DistributionDescContext distribution = null;
        for (DrCreateTableParser.TablePropertyContext property : ctx.tableProperty()) {
            if (property.engineDesc() != null) {
                engine = property.engineDesc();
            }
            if (property.keyDesc() != null) {
                key = property.keyDesc();
            }
            if (property.commentClause() != null) {
                comment = property.commentClause();
            }
            if (property.distributionDesc() != null) {
                distribution = property.distributionDesc();
            }
        }

        if (comment != null) {
            rdbTable.setComment(getString(comment.comment.getText()));
        }
        if (engine != null) {
            rdbTable.setAttribute(DorisAttributeNames.ENGINE, getName(getText(engine.engine)));
        }

        if (distribution != null && distribution.HASH() != null) {
            List<Map<String, String>> columns = new ArrayList<>();

            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_TYPE, "HASH");
            for (DrCreateTableParser.IdentifierContext identifierContext : distribution.hashKeys.identifierSeq().ident) {
                Map<String, String> map = new HashMap<>();
                map.put(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME, getName(getText(identifierContext)));
                columns.add(map);
            }
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS, JsonUtils.toJson(columns));
        } else {
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_TYPE, "RANDOM");
        }
        if (distribution != null && distribution.bucketNumber != null) {
            rdbTable.setAttribute(DorisAttributeNames.BUCKET_NUMBER, distribution.bucketNumber.getText());
        }

        if (key != null) {
            RdbPrimaryKey rdbPrimaryKey = new RdbPrimaryKey();
            if (key.AGGREGATE() != null) {
                rdbPrimaryKey.setName("AGGREGATE KEY");
                rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, "AGGREGATE KEY");
            } else if (key.UNIQUE() != null) {
                rdbPrimaryKey.setName("UNIQUE KEY");
                rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, "UNIQUE KEY");
            } else {
                rdbPrimaryKey.setName("DUPLICATE KEY");
                rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, "DUPLICATE KEY");
            }
            for (DrCreateTableParser.IdentifierContext identifierContext : key.keys.identifierSeq().ident) {
                rdbPrimaryKey.addColumn(getName(getText(identifierContext)));
            }
            rdbTable.setPrimaryKey(rdbPrimaryKey);
        }

        for (DrCreateTableParser.TableElementContext tableElement : ctx.tableElement()) {
            if (tableElement.columnDef() != null) {
                tableElement.columnDef().accept(this);
            } else if (tableElement.indexDef() != null) {
                tableElement.indexDef().accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitIndexDef(DrCreateTableParser.IndexDefContext ctx) {
        RdbIndex rdbIndex = new RdbIndex();

        String name = getName(getText(ctx.indexName));
        List<String> indexColumn = new ArrayList<>();
        for (DrCreateTableParser.IdentifierContext identifierContext : ctx.identifierList().identifierSeq().ident) {
            indexColumn.add(getName(getText(identifierContext)));
        }
        rdbIndex.setName(name);
        rdbIndex.setColumnList(indexColumn);

        if (ctx.indexType != null) {
            rdbIndex.setAttribute(DorisAttributeNames.INDEX_TYPE, ctx.indexType.getText());
        }
        rdbIndex.setType(RdbIndexType.Normal);
        DrCreateTableParser.CommentClauseContext comment = null;
        for (DrCreateTableParser.IndexOptionContext option : ctx.indexOption()) {
            if (option.commentClause() != null) {
                comment = option.commentClause();
            }
        }
        if (comment != null) {
            String commentText = getString(comment.comment.getText());
            if (commentText.equals("")) {
                rdbIndex.setComment("");
            } else {
                rdbIndex.setComment(commentText);
            }
        }
        rdbTable.addIndex(rdbIndex);
        return null;
    }

    @Override
    public Void visitColumnDef(DrCreateTableParser.ColumnDefContext ctx) {
        RdbColumn rdbColumn = new RdbColumn();
        rdbColumn.setIndex(rdbTable.getColumns().size());
        rdbColumn.setName(getName(getText(ctx.name)));
        if (ctx.type() != null) {
            visitColumnType(ctx.type(), rdbColumn);
        }

        DrCreateTableParser.AggDescContext aggDesc = null;
        DrCreateTableParser.CommentClauseContext commentClause = null;
        for (DrCreateTableParser.ColumnOptionContext option : ctx.columnOption()) {
            if (option.aggDesc() != null) {
                aggDesc = option.aggDesc();
            }
            if (option.commentClause() != null) {
                commentClause = option.commentClause();
            }
        }
        if (aggDesc != null) {
            rdbColumn.setAttribute(DorisAttributeNames.AGG_TYPE, getText(aggDesc));
        }

        if (commentClause != null) {
            rdbColumn.setComment(getString(commentClause.comment.getText()));
        }

        if (ctx.columnOption().stream().anyMatch(option -> option.AUTO_INCREMENT() != null)) {
            rdbColumn.setAttribute(DorisAttributeNames.AUTO_INCREMENT, "true");
        } else {
            rdbColumn.setAttribute(DorisAttributeNames.AUTO_INCREMENT, "false");
        }

        rdbTable.addColumn(rdbColumn);
        return null;
    }

    private void visitColumnType(DrCreateTableParser.TypeContext context, RdbColumn rdbColumn) {
        DorisTypes dorisTypes = DorisTypes.valueOfCode(context.typeName().getText());
        rdbColumn.setSqlType(dorisTypes);

        DrCreateTableParser.TypeParameterContext typeParameter = context.typeParameter();
        if (typeParameter != null) {
            String length = typeParameter.precision.getText();
            if (dorisTypes.isNumber() && !dorisTypes.isAccurateDecimal()) {
                rdbColumn.setNumericPrecision(Integer.valueOf(length));
            } else if (dorisTypes.isDataOrTime()) {
                rdbColumn.setDatetimePrecision(Integer.valueOf(length));
            } else if (dorisTypes.isString() || dorisTypes.isBinary()) {
                rdbColumn.setCharLength(Long.valueOf(length));
            } else if (dorisTypes.hasApproximate() || dorisTypes.isAccurateDecimal()) {
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
        public String getText(Interval interval) {
            return stream.getText(interval);
        }

        @Override
        public void consume() {
            stream.consume();
        }

        @Override
        public int LA(int i) {
            int c = stream.LA(i);
            if (c <= 0) {
                return c;
            }
            return Character.toUpperCase(c);
        }

        @Override
        public int mark() {
            return stream.mark();
        }

        @Override
        public void release(int marker) {
            stream.release(marker);
        }

        @Override
        public int index() {
            return stream.index();
        }

        @Override
        public void seek(int index) {
            stream.seek(index);
        }

        @Override
        public int size() {
            return stream.size();
        }

        @Override
        public String getSourceName() { return stream.getSourceName(); }
    }

}
