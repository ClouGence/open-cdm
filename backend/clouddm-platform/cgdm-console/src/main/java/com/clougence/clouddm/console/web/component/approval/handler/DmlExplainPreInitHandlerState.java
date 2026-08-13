/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import com.clougence.clouddm.console.web.component.analysis.AnalysisQueryOptions;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
final class DmlExplainPreInitHandlerState {

    private final PreInitContext                context;
    private final DmlExplainStatistics          statistics;
    private final AnalysisQueryOptions          options;
    private final ExplainPlanSpi                explainSpi;
    private final RewriteSpi                    rewriteSpi;
    private final SqlParserParameters           parameters;
    private final BufferedWriter                writer;
    private final int                           maxStatements;
    private final long                          maxStatementBytes;

    @Builder.Default
    private final AtomicLong                    selectedCount = new AtomicLong();
    @Builder.Default
    private final Semaphore                     parserSlots   = new Semaphore(Math.max(1, Runtime.getRuntime().availableProcessors()));
    @Builder.Default
    private final List<CompletableFuture<Void>> parsingTasks  = new ArrayList<>();

    boolean select() {
        return this.selectedCount.getAndIncrement() < this.maxStatements;
    }

    void submit(Runnable task) {
        this.parserSlots.acquireUninterruptibly();
        this.parsingTasks.add(CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } finally {
                this.parserSlots.release();
            }
        }));
    }

    void await() {
        this.parsingTasks.forEach(CompletableFuture::join);
    }
}
