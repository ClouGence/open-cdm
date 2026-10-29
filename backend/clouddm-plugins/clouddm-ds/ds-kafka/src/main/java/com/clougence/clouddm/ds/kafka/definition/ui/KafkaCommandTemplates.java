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
package com.clougence.clouddm.ds.kafka.definition.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.struts.UmiTypes;

public class KafkaCommandTemplates {
    public static String quickQuery(UmiTypes type, String name) {
        if (type == UmiTypes.Topic) {
            return "kafka-console-consumer --topic " + quote(name) + " --from-beginning --max-messages 10";
        }
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    public static List<String> commands(UmiTypes type, String name) {
        List<String> commands = new ArrayList<>();
        if (type == UmiTypes.Topic) {
            String topicPattern = quote(Pattern.quote(name));
            commands.add("kafka-topics --list");
            commands.add("kafka-topics --describe --topic " + topicPattern);
            commands.add(quickQuery(type, name));
            commands.add("kafka-console-consumer --topic " + quote(name) + " --partition 0 --offset latest --max-messages 10");
            commands.add("# kafka-topics --create --topic 'new_topic' --partitions 1 --replication-factor 1");
            commands.add("# kafka-topics --delete --topic " + topicPattern);
        } else {
            throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
        }
        return commands;
    }

    private static String quote(String value) {
        return "'" + value.replace("'", "'\\''") + "'";
    }
}
