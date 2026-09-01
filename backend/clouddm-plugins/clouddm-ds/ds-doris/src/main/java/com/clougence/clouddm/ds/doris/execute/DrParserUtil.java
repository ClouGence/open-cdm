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
import com.clougence.adapter.doris.DorisTableType;
import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableBaseVisitor;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableLexer;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableParser;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.utils.JsonUtils;

/**
 * Restores Doris table metadata from a CREATE TABLE statement.
 *
 * <p>This parser is intentionally independent from a live Doris connection. Syntax errors are
 * rejected instead of returning a partially populated table.</p>
 */
public class DrParserUtil extends DrCreateTableBaseVisitor<Void> {

    private static final String FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME = "name";

    private final RdbTable      rdbTable                                = new RdbTable();
    private Parser              parser;

    private DrParserUtil(){
        this.rdbTable.setColumns(new LinkedHashMap<>());
        this.rdbTable.setIndices(new ArrayList<>());
        this.rdbTable.setTableType(DorisTableType.Table.getTypeName());
    }

    public static RdbTable parseTable(String createTableSql) {
        if (createTableSql == null || createTableSql.trim().isEmpty()) {
            throw new IllegalArgumentException("CREATE TABLE SQL must not be blank.");
        }

        DrParserUtil parserUtil = new DrParserUtil();
        SyntaxErrorCollector errors = new SyntaxErrorCollector();
        DrCreateTableLexer lexer = new DrCreateTableLexer(new UpperCaseCharStream(CharStreams.fromString(createTableSql)));
        lexer.removeErrorListeners();
        lexer.addErrorListener(errors);

        DrCreateTableParser parser = new DrCreateTableParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errors);
        parserUtil.parser = parser;
        DrCreateTableParser.SingleStatementContext statement = parser.singleStatement();
        if (!errors.messages.isEmpty() || parser.getNumberOfSyntaxErrors() > 0) {
            throw new IllegalArgumentException("Invalid Doris CREATE TABLE: " + String.join("; ", errors.messages));
        }

        parserUtil.visit(statement);
        return parserUtil.rdbTable;
    }

    private String getText(RuleContext ruleContext) {
        return this.parser.getTokenStream().getText(ruleContext);
    }

    private String identifier(DrCreateTableParser.IdentifierContext ctx) {
        String text = getText(ctx);
        if (text.length() >= 2 && text.charAt(0) == '`' && text.charAt(text.length() - 1) == '`') {
            return text.substring(1, text.length() - 1).replace("``", "`");
        }
        return text;
    }

    private List<String> qualifiedName(DrCreateTableParser.QualifiedNameContext ctx) {
        List<String> parts = new ArrayList<>();
        for (DrCreateTableParser.IdentifierContext part : ctx.parts) {
            parts.add(identifier(part));
        }
        return parts;
    }

    private String qualifiedNameText(DrCreateTableParser.QualifiedNameContext ctx) {
        return String.join(".", qualifiedName(ctx));
    }

    private String stringLiteral(DrCreateTableParser.StringLiteralContext ctx) {
        String text = getText(ctx);
        if (text.length() < 2) {
            return text;
        }
        char quote = text.charAt(0);
        String value = text.substring(1, text.length() - 1);
        value = value.replace(String.valueOf(quote) + quote, String.valueOf(quote));
        StringBuilder result = new StringBuilder(value.length());
        boolean escaped = false;
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (!escaped && current == '\\') {
                escaped = true;
                continue;
            }
            if (escaped) {
                switch (current) {
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case '0':
                        result.append('\0');
                        break;
                    default:
                        result.append(current);
                        break;
                }
                escaped = false;
            } else {
                result.append(current);
            }
        }
        if (escaped) {
            result.append('\\');
        }
        return result.toString();
    }

    @Override
    public Void visitCreateTable(DrCreateTableParser.CreateTableContext ctx) {
        List<String> tableName = qualifiedName(ctx.name);
        if (tableName.size() > 3) {
            throw new IllegalArgumentException("Doris table name has more than three parts: " + getText(ctx.name));
        }
        rdbTable.setName(tableName.get(tableName.size() - 1));
        if (tableName.size() >= 2) {
            rdbTable.setSchema(tableName.get(tableName.size() - 2));
        }
        if (tableName.size() == 3) {
            rdbTable.setCatalog(tableName.get(0));
        }

        if (ctx.tableModifier() != null) {
            String modifier = getText(ctx.tableModifier()).toUpperCase(Locale.ROOT);
            rdbTable.setAttribute(DorisAttributeNames.TABLE_MODIFIER, modifier);
            rdbTable.setAttribute(DorisAttributeNames.TEMPORARY, Boolean.toString("TEMPORARY".equals(modifier)));
            if ("EXTERNAL".equals(modifier)) {
                rdbTable.setTableType("EXTERNAL TABLE");
            }
        } else {
            rdbTable.setAttribute(DorisAttributeNames.TEMPORARY, "false");
        }

        if (ctx.likeTable != null) {
            rdbTable.setAttribute(DorisAttributeNames.LIKE_TABLE, qualifiedNameText(ctx.likeTable));
            if (ctx.ROLLUP() != null) {
                String rollup = "WITH ROLLUP";
                if (ctx.identifierList() != null) {
                    rollup += getText(ctx.identifierList());
                }
                rdbTable.setAttribute(DorisAttributeNames.ROLLUP, rollup);
            }
            return null;
        }

        if (ctx.tableDefinition() != null) {
            for (DrCreateTableParser.TableElementContext tableElement : ctx.tableDefinition().tableElement()) {
                tableElement.accept(this);
            }
        }

        for (DrCreateTableParser.TablePropertyContext property : ctx.tableProperty()) {
            applyTableProperty(property);
        }
        if (ctx.queryClause() != null) {
            rdbTable.setAttribute(DorisAttributeNames.CREATE_QUERY, getText(ctx.queryClause()));
        }
        return null;
    }

    private void applyTableProperty(DrCreateTableParser.TablePropertyContext property) {
        if (property.engineDesc() != null) {
            rdbTable.setAttribute(DorisAttributeNames.ENGINE, identifier(property.engineDesc().engine));
        } else if (property.keyDesc() != null) {
            applyKey(property.keyDesc());
        } else if (property.orderByDesc() != null) {
            rdbTable.setAttribute(DorisAttributeNames.ORDER_BY, getText(property.orderByDesc()));
        } else if (property.commentClause() != null) {
            rdbTable.setComment(stringLiteral(property.commentClause().comment));
        } else if (property.partitionDesc() != null) {
            rdbTable.setAttribute(DorisAttributeNames.PARTITION_EXPR, getText(property.partitionDesc()));
        } else if (property.distributionDesc() != null) {
            applyDistribution(property.distributionDesc());
        } else if (property.rollupDesc() != null) {
            rdbTable.setAttribute(DorisAttributeNames.ROLLUP, getText(property.rollupDesc()));
        } else if (property.propertiesDesc() != null) {
            rdbTable.setAttribute(DorisAttributeNames.PROPERTIES_CONFIG, getText(property.propertiesDesc()));
        } else if (property.brokerPropertiesDesc() != null) {
            rdbTable.setAttribute(DorisAttributeNames.BROKER_PROPERTIES, getText(property.brokerPropertiesDesc()));
        }
    }

    private void applyKey(DrCreateTableParser.KeyDescContext key) {
        String keyType;
        if (key.AGGREGATE() != null) {
            keyType = "AGGREGATE KEY";
        } else if (key.UNIQUE() != null) {
            keyType = "UNIQUE KEY";
        } else if (key.PRIMARY() != null) {
            keyType = "PRIMARY KEY";
        } else {
            keyType = "DUPLICATE KEY";
        }

        RdbPrimaryKey rdbPrimaryKey = new RdbPrimaryKey();
        rdbPrimaryKey.setName(keyType);
        for (DrCreateTableParser.IdentifierContext column : key.keys.identifierSeq().ident) {
            rdbPrimaryKey.addColumn(identifier(column));
        }
        rdbTable.setAttribute(DorisAttributeNames.KEY_TYPE, keyType);
        rdbTable.setPrimaryKey(rdbPrimaryKey);
    }

    private void applyDistribution(DrCreateTableParser.DistributionDescContext distribution) {
        if (distribution.HASH() != null) {
            List<Map<String, String>> columns = new ArrayList<>();
            for (DrCreateTableParser.IdentifierContext column : distribution.hashKeys.identifierSeq().ident) {
                Map<String, String> value = new LinkedHashMap<>();
                value.put(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME, identifier(column));
                columns.add(value);
            }
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_TYPE, "HASH");
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS, JsonUtils.toJson(columns));
        } else {
            rdbTable.setAttribute(DorisAttributeNames.DISTRIBUTED_BY_TYPE, "RANDOM");
        }
        if (distribution.bucketNumber != null) {
            rdbTable.setAttribute(DorisAttributeNames.BUCKET_NUMBER, distribution.bucketNumber.getText());
        } else if (distribution.autoBucket != null) {
            rdbTable.setAttribute(DorisAttributeNames.BUCKET_NUMBER, "AUTO");
        }
    }

    @Override
    public Void visitIndexDef(DrCreateTableParser.IndexDefContext ctx) {
        RdbIndex index = new RdbIndex();
        index.setName(identifier(ctx.indexName));
        index.setType(RdbIndexType.Normal);
        for (DrCreateTableParser.IdentifierContext column : ctx.identifierList().identifierSeq().ident) {
            index.addColumn(identifier(column));
        }
        if (ctx.indexType != null) {
            index.setAttribute(DorisAttributeNames.INDEX_TYPE, ctx.indexType.getText());
        } else if (ctx.indexTypeIdentifier != null) {
            index.setAttribute(DorisAttributeNames.INDEX_TYPE, identifier(ctx.indexTypeIdentifier));
        }
        for (DrCreateTableParser.IndexOptionContext option : ctx.indexOption()) {
            if (option.commentClause() != null) {
                index.setComment(stringLiteral(option.commentClause().comment));
            } else if (option.propertiesDesc() != null) {
                index.setAttribute(DorisAttributeNames.INDEX_PROPERTIES, getText(option.propertiesDesc()));
            }
        }
        rdbTable.addIndex(index);
        return null;
    }

    @Override
    public Void visitColumnDef(DrCreateTableParser.ColumnDefContext ctx) {
        RdbColumn column = new RdbColumn();
        column.setIndex(rdbTable.getColumns().size());
        column.setName(qualifiedNameText(ctx.name));
        column.setCatalog(rdbTable.getCatalog());
        column.setSchema(rdbTable.getSchema());
        column.setTable(rdbTable.getName());
        applyColumnType(ctx.type(), column);

        boolean nullable = true;
        boolean autoIncrement = false;
        for (DrCreateTableParser.ColumnOptionContext option : ctx.columnOption()) {
            if (option.aggDesc() != null) {
                column.setAttribute(DorisAttributeNames.AGG_TYPE, option.aggDesc().aggType.getText());
            } else if (option.NULL() != null) {
                nullable = option.NOT() == null;
            } else if (option.AUTO_INCREMENT() != null) {
                autoIncrement = true;
                if (option.INTEGER_VALUE() != null) {
                    column.setAttribute(DorisAttributeNames.AUTO_INCREMENT_INIT, option.INTEGER_VALUE().getText());
                }
            } else if (option.DEFAULT() != null) {
                applyDefault(option.defaultValue(), column);
            } else if (option.ON() != null) {
                column.setAttribute(DorisAttributeNames.CURRENT_UPDATE_TYPE, getText(option).substring(3).trim());
            } else if (option.commentClause() != null) {
                column.setComment(stringLiteral(option.commentClause().comment));
            } else if (option.AS() != null) {
                String expression = getText(option);
                int open = expression.indexOf('(');
                int close = expression.lastIndexOf(')');
                if (open >= 0 && close > open) {
                    expression = expression.substring(open + 1, close);
                }
                column.setAttribute(DorisAttributeNames.GENERATED, expression);
            }
        }
        column.setAttribute(DorisAttributeNames.NULLABLE, Boolean.toString(nullable));
        column.setAttribute(DorisAttributeNames.AUTO_INCREMENT, Boolean.toString(autoIncrement));
        rdbTable.addColumn(column);
        return null;
    }

    private void applyDefault(DrCreateTableParser.DefaultValueContext defaultValue, RdbColumn column) {
        String raw = getText(defaultValue);
        column.setAttribute(DorisAttributeNames.DEFAULT, raw);
        if (defaultValue.stringLiteral() != null) {
            column.setDefaultValue(stringLiteral(defaultValue.stringLiteral()));
            column.setDefaultValueIsFunc(false);
        } else if (defaultValue.NULL() != null) {
            column.setDefaultValue(null);
            column.setDefaultValueIsFunc(false);
        } else {
            column.setDefaultValue(raw);
            column.setDefaultValueIsFunc(defaultValue.CURRENT_TIMESTAMP() != null || defaultValue.CURRENT_DATE() != null || defaultValue.LEFT_PAREN() != null);
        }
    }

    private void applyColumnType(DrCreateTableParser.TypeContext type, RdbColumn column) {
        String columnType = getText(type);
        String dataType;
        if (type.ARRAY() != null) {
            dataType = "ARRAY";
            column.setArrayDimension(arrayDimensions(type));
        } else if (type.MAP() != null) {
            dataType = "MAP";
        } else if (type.STRUCT() != null) {
            dataType = "STRUCT";
        } else if (type.AGG_STATE() != null) {
            dataType = "AGG_STATE";
        } else if (type.VARIANT() != null) {
            dataType = "VARIANT";
        } else {
            dataType = type.typeName().getText();
        }

        DorisTypes dorisType = DorisTypes.valueOfCode(dataType);
        column.setSqlType(dorisType);
        column.setAttribute(DorisAttributeNames.DATA_TYPE, dataType);
        column.setAttribute(DorisAttributeNames.COLUMN_TYPE, columnType);

        DrCreateTableParser.TypeParameterContext parameters = type.typeParameter();
        if (parameters == null || parameters.params.isEmpty() || parameters.params.get(0).ASTERISK() != null) {
            return;
        }
        int precision = Integer.parseInt(parameters.params.get(0).INTEGER_VALUE().getText());
        if (dorisType.isDataOrTime()) {
            column.setDatetimePrecision(precision);
        } else if (dorisType.isString() || dorisType.isBinary()) {
            column.setCharLength((long) precision);
        } else {
            column.setNumericPrecision(precision);
        }
        if (parameters.params.size() > 1 && parameters.params.get(1).INTEGER_VALUE() != null) {
            column.setNumericScale(Integer.parseInt(parameters.params.get(1).INTEGER_VALUE().getText()));
        }
    }

    private int arrayDimensions(DrCreateTableParser.TypeContext type) {
        if (type.ARRAY() == null) {
            return 0;
        }
        return 1 + arrayDimensions(type.elementType);
    }

    private static final class SyntaxErrorCollector extends BaseErrorListener {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            messages.add("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }

    private record UpperCaseCharStream(CharStream stream) implements CharStream {

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
            return c <= 0 ? c : Character.toUpperCase(c);
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
