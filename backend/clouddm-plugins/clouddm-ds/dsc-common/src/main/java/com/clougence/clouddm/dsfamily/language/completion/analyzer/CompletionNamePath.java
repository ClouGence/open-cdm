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
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletionNamePath {

    @Builder.Default
    private final List<String> qualifiers = Collections.emptyList();
    private final String       prefix;

    public String catalog() {
        return qualifiers.size() >= 3 ? qualifiers.get(qualifiers.size() - 3) : null;
    }

    public String schema() {
        return qualifiers.size() >= 2 ? qualifiers.get(qualifiers.size() - 2) : null;
    }

    public String table() {
        return qualifiers.isEmpty() ? null : qualifiers.get(qualifiers.size() - 1);
    }

    public String currentName() {
        return prefix;
    }
}
