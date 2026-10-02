/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis.lineage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.antlr.v4.runtime.CharStreams;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.*;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.resolve.LineageResolver;
import com.clougence.sql.common.analysis.lineage.resolve.LineageTableName;
import com.clougence.sql.hana.i18n.HanaSqlI18nKeys;
import com.clougence.sql.hana.parser.HanaDslProvider;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class HanaLineageAnalysisSpi implements LineageAnalysisSpi {
    private final MetaService metaService;

    public HanaLineageAnalysisSpi(MetaService metaService) {
        this.metaService = metaService;
    }

    @Override
    public List<LineageColumn> analyze(String sql, LineageContext context) {
        try {
            var lexer = HanaDslProvider.INSTANCE.createLexer(CharStreams.fromString(sql));
            var parser = (HanaParser) HanaDslProvider.INSTANCE.createParser(lexer);
            var statement = parser.statementRoot().statement();
            if (statement.query() == null) throw new IllegalArgumentException("Result lineage requires a SELECT query");
            var query = new HanaLineageVisitor().query(statement.query());
            Map<LineageTableName, List<SourceName>> columns = new HashMap<>();
            var result = new LineageResolver(table -> columns.computeIfAbsent(table, name -> resolveColumns(context, name)), true, false).resolve(query);
            if (result.stream().anyMatch(column -> column.column().isEmpty())) {
                throw new IllegalArgumentException("A result expression with source columns requires an explicit alias");
            }
            return result;
        } catch (AntlerSyntaxException | IllegalArgumentException e) {
            String msg = "Cannot resolve HANA result lineage";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, HanaSqlI18nKeys.HANA_SQL_LINEAGE_UNSUPPORTED, e.getMessage());
        }
    }

    private List<SourceName> resolveColumns(LineageContext context, LineageTableName table) {
        Map<UmiTypes, Object> levels = new HashMap<>();
        if (context.getLevelsParam() != null) levels.putAll(context.getLevelsParam());
        if (table.schema() != null) levels.put(UmiTypes.Schema, table.schema());
        var columns = metaService.fetchTableColumns(context.getUserUID(), context.getDsId(), levels, table.table());
        if (columns.isEmpty()) throw new IllegalArgumentException("No column metadata for " + table.table());
        return columns.stream().map(column -> {
            String catalog = column.getCatalog();
            if (catalog == null || catalog.isBlank()) catalog = Objects.toString(levels.get(UmiTypes.Catalog), null);
            String schema = column.getSchema();
            if (schema == null || schema.isBlank()) schema = Objects.toString(levels.get(UmiTypes.Schema), null);
            String sourceTable = column.getTable();
            if (sourceTable == null || sourceTable.isBlank()) sourceTable = table.table();
            // Platform authorization uses slash-delimited resource paths.
            if (List.of(Objects.toString(catalog, ""), Objects.toString(schema, ""), sourceTable, column.getColumn()).stream().anyMatch(n -> n.contains("/"))) {
                throw new IllegalArgumentException("Column source contains a resource path separator");
            }
            return new SourceName(catalog, schema, sourceTable, column.getColumn());
        }).toList();
    }
}
