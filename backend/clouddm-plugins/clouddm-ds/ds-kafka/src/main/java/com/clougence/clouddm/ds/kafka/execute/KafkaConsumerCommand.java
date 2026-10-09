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

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.sql.kafka.KafkaCommand;
import com.clougence.utils.i18n.I18nUtils;

public class KafkaConsumerCommand {
    private final KafkaSession  session;
    private final KafkaClients  clients;
    private final QueryRequest  query;
    private final ResultBuilder builder;

    public KafkaConsumerCommand(KafkaSession session, KafkaClients clients, QueryRequest query, ResultBuilder builder){
        this.session = session;
        this.clients = clients;
        this.query = query;
        this.builder = builder;
    }

    public void execute(KafkaCommand command) throws Exception {
        KafkaCommand effective = normalize(command);
        long timeoutMs = Long.parseLong(effective.value("--timeout-ms"));
        session.checkCancelled();
        try (KafkaRecordIterator records = new KafkaRecordIterator(session, clients.createConsumer(), timeoutMs)) {
            records.assign(effective);
            new KafkaResultWriter(query, builder).table(List
                .of(KafkaResultColumn.TOPIC, KafkaResultColumn.PARTITION, KafkaResultColumn.OFFSET, KafkaResultColumn.TIMESTAMP, KafkaResultColumn.TIMESTAMP_TYPE, KafkaResultColumn.KEY, KafkaResultColumn.VALUE), records, session::checkCancelled);
            if (records.isTimedOut()) {
                var message = builder.newMessage(query);
                String text = I18nUtils.initI18n(KafkaDsI18nKeys.class).getMessage(KafkaDsI18nKeys.KAFKA_CONSUMER_IDLE_TIMEOUT, new Object[] { timeoutMs });
                message.receiveMessage(MessageLevel.Info, text, false);
                message.finishRecord(true);
            }
        }
    }

    private KafkaCommand normalize(KafkaCommand command) throws Exception {
        long maxMessages = query.getResultConf().getFetchRecordCountLimit();
        if (command.has("--max-messages")) {
            long requested = Long.parseLong(command.value("--max-messages"));
            if (maxMessages <= 0 || requested < maxMessages) {
                maxMessages = requested;
            }
        }
        if (maxMessages <= 0) {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_CONSUMER_LIMIT_REQUIRED);
        }
        maxMessages = Math.min(maxMessages, Integer.MAX_VALUE);
        long timeoutMs = TimeUnit.SECONDS.toMillis(query.getResultConf().getQueryTimeoutSec());
        if (command.has("--timeout-ms")) {
            timeoutMs = Long.parseLong(command.value("--timeout-ms"));
        } else if (timeoutMs <= 0) {
            timeoutMs = session.getDsConfig().getApiTimeoutMs();
        }
        var options = new LinkedHashMap<>(command.getOptions());
        options.put("--max-messages", List.of(Long.toString(maxMessages)));
        options.put("--timeout-ms", List.of(Long.toString(timeoutMs)));
        KafkaCommand effective = new KafkaCommand(command.getType(), options);
        if (!options.equals(command.getOptions())) {
            StringWriter text = new StringWriter();
            effective.doFormat(new FmtWriter(text));
            if (!query.isHasRewrite()) {
                query.setOriginalBody(query.getQueryBody());
            }
            query.setHasRewrite(true);
            query.addRewriteTag(KafkaDsI18nKeys.KAFKA_CONSUMER_BOUNDS_REWRITE);
            query.setQueryBody(text.toString());
        }
        query.getResultConf().setFetchRecordCountLimit(maxMessages);
        return effective;
    }
}
