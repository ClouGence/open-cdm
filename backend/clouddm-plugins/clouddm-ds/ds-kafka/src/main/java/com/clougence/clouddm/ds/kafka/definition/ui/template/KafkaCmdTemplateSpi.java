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
package com.clougence.clouddm.ds.kafka.definition.ui.template;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.ui.template.CmdExample;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class KafkaCmdTemplateSpi implements CmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "kafka-topics --list";
    }

    @Override
    public List<CmdExample> getExamples() {
        List<CmdExample> examples = new ArrayList<>();
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_LIST_TITLE, "kafka-topics --list"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_DESCRIBE_TITLE, "kafka-topics --describe --topic '^orders$'"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_PATTERN_TITLE, "kafka-topics --describe --topic 'orders[.].*'"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_CREATE_TITLE,
            "kafka-topics --create --topic orders --partitions 3 --replication-factor 1 --config retention.ms=86400000 --if-not-exists"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_DELETE_TITLE, "kafka-topics --delete --topic '^orders$' --if-exists"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_BEGINNING_TITLE, "kafka-console-consumer --topic orders --from-beginning --max-messages 10 --timeout-ms 10000"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_LATEST_TITLE, "kafka-console-consumer --topic orders --max-messages 10 --timeout-ms 10000"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_PARTITION_TITLE,
            "kafka-console-consumer --topic orders --partition 0 --offset latest --max-messages 10 --timeout-ms 10000"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_TOPICS_HELP_TITLE, "kafka-topics --help"));
        examples.add(new CmdExample(KafkaDsI18nKeys.KAFKA_EXAMPLE_CONSUMER_HELP_TITLE, "kafka-console-consumer.sh --help"));
        return examples;
    }
}
