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
package com.clougence.sql.kafka;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class KafkaCommandSpec {
    private KafkaCommandSpec(){
    }

    public static Map<String, KafkaOption> options(KafkaCommandType type) {
        Map<String, KafkaOption> options = new LinkedHashMap<>();
        options.put("--help", new KafkaOption(false, false));
        for (String action : type.getActions()) {
            options.put(action, new KafkaOption(false, false));
        }
        List<String> values = new ArrayList<>();
        List<String> flags = new ArrayList<>();
        switch (type) {
            case TOPICS -> {
                values.addAll(List.of("--topic", "--partitions", "--replication-factor", "--config"));
                flags.addAll(List.of("--if-exists", "--if-not-exists"));
            }
            case CONSUMER -> {
                values.addAll(List.of("--topic", "--partition", "--offset", "--max-messages", "--timeout-ms"));
                flags.add("--from-beginning");
            }
        }
        for (String name : values) {
            options.put(name, new KafkaOption(true, name.equals("--config")));
        }
        for (String name : flags) {
            options.put(name, new KafkaOption(false, false));
        }
        return options;
    }

    public static List<String> allowed(KafkaCommandType type, String action) {
        if (type == KafkaCommandType.CONSUMER || action == null) {
            return List.copyOf(options(type).keySet());
        }
        List<String> allowed = new ArrayList<>(List.of("--help", action));
        if (type == KafkaCommandType.TOPICS) {
            allowed.add("--topic");
            if (action.equals("--create")) {
                allowed.addAll(List.of("--partitions", "--replication-factor", "--config", "--if-not-exists"));
            }
            if (action.equals("--delete")) {
                allowed.add("--if-exists");
            }
        }
        return allowed;
    }
}
