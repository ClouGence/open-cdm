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
package com.clougence.schema.editor.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author barry
 * @date 2022/7/27 上午11:11:16
 * @description
 */
@Getter
@Setter
public class CharsetsAndCollations {

    private Map<String, String> data = new HashMap<>();

    public void add(String collationInfo, String charsetInfo) {
        data.put(collationInfo, charsetInfo);
    }

    public Set<String> collations() {
        return data.keySet();
    }

    public Collection<String> charsets() {
        return data.values();
    }

    public boolean containsCollation(String collation) {
        return this.collations().contains(collation);
    }

    public boolean containsCharset(String charset) {
        return this.charsets().contains(charset);
    }

    public List<String> findCollationsFromCharset(String charset) {
        List<String> re = new ArrayList<>();
        for (Map.Entry<String, String> e : data.entrySet()) {
            if (e.getValue().equals(charset)) {
                re.add(e.getKey());
            }
        }

        return re;
    }
}
