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
package com.clougence.clouddm.ds.kafka.execute;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.common.KafkaFuture;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import lombok.Getter;

public class KafkaAdminContext {
    @Getter
    private final Admin             admin;
    private final KafkaSession      session;
    private final QueryRequest      query;
    private final ResultBuilder     builder;
    private final KafkaResultWriter writer;
    private final long              deadline;
    private final int               apiTimeoutMs;
    private final long              start = System.currentTimeMillis();

    public KafkaAdminContext(KafkaSession session, Admin admin, QueryRequest query, ResultBuilder builder){
        this.session = session;
        this.admin = admin;
        this.query = query;
        this.builder = builder;
        this.writer = new KafkaResultWriter(query, builder);
        this.apiTimeoutMs = session.getDsConfig().getApiTimeoutMs();
        long timeout = apiTimeoutMs;
        if (query.getResultConf().getQueryTimeoutSec() > 0) {
            timeout = TimeUnit.SECONDS.toMillis(query.getResultConf().getQueryTimeoutSec());
        }
        this.deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeout);
    }

    public void checkActive() {
        session.checkCancelled();
        if (System.nanoTime() >= deadline) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_QUERY_TIMEOUT);
        }
    }

    public <T> T await(IntFunction<KafkaFuture<T>> request) throws Exception {
        checkActive();
        long remaining = Math.max(1, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()));
        int timeout = (int) Math.min(apiTimeoutMs, remaining);
        return session.await(() -> request.apply(timeout), timeout);
    }

    public void table(List<KafkaResultColumn> columns, List<Object[]> rows) throws Exception {
        writer.table(columns, rows, this::checkActive);
    }

    public void managementReport(List<KafkaResultColumn> columns, List<Object[]> rows) throws Exception {
        // A completed/failed batch still needs its per-resource outcomes after timeout or interruption.
        writer.table(columns, rows, () -> {
        });
    }

    public void affected(long count) {
        var result = builder.newResultCount(query);
        result.receiveUpdateCount(count);
        result.collectCost(System.currentTimeMillis() - start);
        result.finishRecord(true);
    }
}
