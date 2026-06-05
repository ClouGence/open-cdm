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
package com.clougence.clouddm.ds.mongodb.parser.ast;

import java.util.LinkedHashMap;

public class CommandBuilder extends LinkedHashMap<String, String> {

    public String toString() {
        StringBuilder command = new StringBuilder();
        command.append("{");
        this.forEach((k, v) -> {
            command.append("\"").append(k).append("\":").append(v).append(",");
        });
        command.deleteCharAt(command.length() - 1);
        command.append("}");
        return command.toString();
    }

    public void addOption(String option, String value) {
        if (value == null) {
            return;
        }
        this.put(option, value);
    }
}
