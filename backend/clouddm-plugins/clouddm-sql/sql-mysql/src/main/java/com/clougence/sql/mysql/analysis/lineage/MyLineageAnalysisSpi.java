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
package com.clougence.sql.mysql.analysis.lineage;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.model.LineageQuery;
import com.clougence.sql.common.analysis.lineage.resolve.LineageMetadataResolver;
import com.clougence.sql.common.analysis.lineage.resolve.LineageResolver;
import com.clougence.sql.common.analysis.lineage.resolve.LineageTableName;
import com.clougence.sql.mysql.analysis.lineage.antlr.MyLineageCstVisitor;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

public class MyLineageAnalysisSpi implements LineageAnalysisSpi {

    private final MetaService metaService;
    private final DslProvider provider;

    public MyLineageAnalysisSpi(MetaService metaService, MySqlParserConfig config){
        this.metaService = metaService;
        this.provider = new MyDslProvider(config);
    }

    @Override
    public List<LineageColumn> analyze(Reader sql, LineageContext lineageContext) {
        AtomicReference<MyLineageCstVisitor> visitorRef = new AtomicReference<>();
        DslHelper.doVisitor(provider, sql, (lexer, parser) -> {
            MyLineageCstVisitor visitor = new MyLineageCstVisitor(parser);
            visitorRef.set(visitor);
            return visitor;
        });
        LineageQuery query = visitorRef.get().query();

        Map<UmiTypes, Object> defaultLevels = lineageContext.getLevelsParam();
        if (defaultLevels == null) {
            defaultLevels = Map.of();
        }
        Map<UmiTypes, Object> contextLevels = defaultLevels;
        LineageMetadataResolver metadataResolver = tableName -> resolveColumns(lineageContext, contextLevels, tableName);
        return new LineageResolver(metadataResolver).resolve(query);
    }

    private List<SourceName> resolveColumns(LineageContext context, Map<UmiTypes, Object> defaultLevels, LineageTableName tableName) {
        Map<UmiTypes, Object> levels = new HashMap<>(defaultLevels);
        if (tableName.catalog() != null) {
            levels.put(UmiTypes.Catalog, tableName.catalog());
        }
        if (tableName.schema() != null) {
            levels.put(UmiTypes.Schema, tableName.schema());
        }
        List<MetaCol> columns = metaService.fetchTableColumns(context.getUserUID(), context.getDsId(), levels, tableName.table());
        return columns.stream().map(column -> {
            String catalog = column.getCatalog();
            if (catalog == null || catalog.isBlank()) {
                catalog = Objects.toString(levels.get(UmiTypes.Catalog), null);
            }
            String schema = column.getSchema();
            if (schema == null || schema.isBlank()) {
                schema = Objects.toString(levels.get(UmiTypes.Schema), null);
            }
            String table = column.getTable();
            if (table == null || table.isBlank()) {
                table = tableName.table();
            }
            return new SourceName(catalog, schema, table, column.getColumn());
        }).toList();
    }
}
