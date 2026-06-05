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
package com.clougence.utils.token;

import java.util.Map;

import lombok.Getter;

public class MapVariableTokenHandler implements TokenHandler {

    private final Map<String, Object> variables;
    @Getter
    public final String               openToken;
    @Getter
    public final String               closeToken;

    public MapVariableTokenHandler(String openToken, String closeToke, Map<String, Object> variables){
        this.variables = variables;
        this.openToken = openToken;
        this.closeToken = closeToke;
    }

    @Override
    public String handleToken(String content) {
        if (variables != null && variables.containsKey(content)) {
            return variables.get(content).toString();
        }
        return openToken + content + closeToken;
    }
}
