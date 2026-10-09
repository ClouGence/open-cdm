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

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaCommandType {
    TOPICS("kafka-topics", List.of("--list", "--describe", "--create", "--delete")),
    GROUPS("kafka-consumer-groups", List.of("--list", "--describe", "--delete")),
    CONSUMER("kafka-console-consumer", List.of());

    private final String       command;
    private final List<String> actions;

    public static KafkaCommandType find(String name) {
        if (name.endsWith(".sh")) {
            name = name.substring(0, name.length() - 3);
        }
        for (KafkaCommandType type : values()) {
            if (type.command.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
