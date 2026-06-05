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
package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;

import com.clougence.adapter.doris.DorisAttributeNames;
import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.ds.doris.parser.DrDslProvider;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParser;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.utils.JsonUtils;

public class DrParserUtil extends DorisParserBaseVisitor<Void> implements DrTableEditorFields {

    private RdbTable rdbTable = new RdbTable();
    private Parser   parser;

    private DrParserUtil(){

    }

    public static RdbTable parseTable(String createTableSql) {
        DrParserUtil srParserUtil = new DrParserUtil();
        DslHelper.doVisitor(DrDslProvider.INSTANCE, createTableSql, (lexer, parser) -> {
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
    public Void visitCreateTable(DorisParser.CreateTableContext ctx) {
        rdbTable.setName(getName(getText(ctx.name)));
        rdbTable.setColumns(new LinkedHashMap<>());

        if (ctx.COMMENT() != null) {
            rdbTable.setComment(getString(ctx.STRING_LITERAL().getText()));
        }
        if (ctx.ENGINE() != null) {
            rdbTable.setAttribute(DorisAttributeNames.ENGINE, getName(getText(ctx.engine)));
        }

        if (ctx.HASH() != null) {
            List<Map<String, String>> columns = new ArrayList<>();

            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_TYPE, "HASH");
            for (DorisParser.ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.hashKeys.identifierSeq().ident) {
                Map<String, String> map = new HashMap<>();
                map.put(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME, getName(getText(errorCapturingIdentifierContext.identifier())));
                columns.add(map);
            }
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS, JsonUtils.toJson(columns));
        } else {
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_TYPE, "RANDOM");
        }
        if (ctx.BUCKETS() != null) {
            rdbTable.setAttribute(DorisAttributeNames.BUCKET_NUMBER, ctx.INTEGER_VALUE().getText());
        }

        if (ctx.KEY() != null) {
            RdbPrimaryKey rdbPrimaryKey = new RdbPrimaryKey();
            if (ctx.AGGREGATE() != null) {
                rdbPrimaryKey.setName("AGGREGATE KEY");
                rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, "AGGREGATE KEY");
            } else if (ctx.UNIQUE() != null) {
                rdbPrimaryKey.setName("UNIQUE KEY");
                rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, "UNIQUE KEY");
            } else {
                rdbPrimaryKey.setName("DUPLICATE KEY");
                rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, "DUPLICATE KEY");
            }
            for (DorisParser.ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.keys.identifierSeq().ident) {
                rdbPrimaryKey.addColumn(getName(getText(errorCapturingIdentifierContext.identifier())));
            }
            rdbTable.setPrimaryKey(rdbPrimaryKey);
        }

        for (DorisParser.ColumnDefContext columnDefContext : ctx.columnDefs().columnDef()) {
            columnDefContext.accept(this);
        }
        if (ctx.indexDefs() != null) {
            for (DorisParser.IndexDefContext index : ctx.indexDefs().indexes) {
                index.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitIndexDef(DorisParser.IndexDefContext ctx) {
        RdbIndex rdbIndex = new RdbIndex();

        String name = getName(getText(ctx.indexName));
        List<String> indexColumn = new ArrayList<>();
        for (DorisParser.ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.identifierList().identifierSeq().errorCapturingIdentifier()) {
            indexColumn.add(getName(getText(errorCapturingIdentifierContext.identifier())));
        }
        rdbIndex.setName(name);
        rdbIndex.setColumnList(indexColumn);

        if (ctx.indexType != null) {
            rdbIndex.setAttribute(DorisAttributeNames.INDEX_TYPE, ctx.indexType.getText());
        }
        rdbIndex.setType(RdbIndexType.Normal);
        if (Objects.nonNull(ctx.comment)) {
            String comment = getString(ctx.comment.getText());
            if (comment.equals("")) {
                rdbIndex.setComment("");
            } else {
                rdbIndex.setComment(comment.substring(1, comment.length() - 1));
            }
        }
        rdbTable.addIndex(rdbIndex);
        return null;
    }

    @Override
    public Void visitColumnDef(DorisParser.ColumnDefContext ctx) {
        RdbColumn rdbColumn = new RdbColumn();
        rdbColumn.setIndex(rdbTable.getColumns().size());
        rdbColumn.setName(getName(getText(ctx.identifier())));
        if (ctx.type != null) {
            visitColumnType(ctx.type, rdbColumn);
        }

        if (ctx.aggType != null) {
            rdbColumn.setAttribute(DorisAttributeNames.AGG_TYPE, getText(ctx.aggTypeDef()));
        }

        if (ctx.comment != null) {
            rdbColumn.setComment(getString(ctx.comment.getText()));
        }

        if (ctx.AUTO_INCREMENT() != null) {
            rdbColumn.setAttribute(DorisAttributeNames.AUTO_INCREMENT, "true");
        } else {
            rdbColumn.setAttribute(DorisAttributeNames.AUTO_INCREMENT, "false");
        }

        rdbTable.addColumn(rdbColumn);
        return null;
    }

    private void visitColumnType(DorisParser.DataTypeContext context, RdbColumn rdbColumn) {
        if (context instanceof DorisParser.PrimitiveDataTypeContext) {
            DorisParser.PrimitiveDataTypeContext primitiveDataTypeContext = (DorisParser.PrimitiveDataTypeContext) context;
            DorisTypes starRocksTypes = DorisTypes.valueOfCode(primitiveDataTypeContext.primitiveColType().type.getText());
            rdbColumn.setSqlType(starRocksTypes);

            if (!primitiveDataTypeContext.INTEGER_VALUE().isEmpty()) {
                String length = primitiveDataTypeContext.INTEGER_VALUE(0).getText();
                if (starRocksTypes.isNumber() && !starRocksTypes.isAccurateDecimal()) {
                    rdbColumn.setNumericPrecision(Integer.valueOf(length));
                } else if (starRocksTypes.isDataOrTime()) {
                    rdbColumn.setDatetimePrecision(Integer.valueOf(length));
                } else if (starRocksTypes.isString() || starRocksTypes.isBinary()) {
                    rdbColumn.setCharLength(Long.valueOf(length));
                } else if (starRocksTypes.hasApproximate() || starRocksTypes.isAccurateDecimal()) {
                    rdbColumn.setNumericPrecision(Integer.valueOf(length));
                    if (primitiveDataTypeContext.INTEGER_VALUE().size() > 1) {
                        rdbColumn.setNumericScale(Integer.valueOf(primitiveDataTypeContext.INTEGER_VALUE(1).getText()));
                    }
                } else {
                    rdbColumn.setNumericPrecision(Integer.valueOf(length));
                }
            }
        } else {
            DorisTypes starRocksTypes = DorisTypes.valueOfCode(context.getChild(0).getText());
            rdbColumn.setSqlType(starRocksTypes);
        }

    }

}
