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
package com.clougence.schema.umi.struts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 属性Keys。
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public abstract class AttributeUmiData implements UmiData {

    private final Map<String, String> attributes = new HashMap<>();

    public String getAttribute(UmiAttributeNames name) {
        return this.getAttribute(name.getCodeKey());
    }

    public String getAttribute(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return this.attributes.get(key);
    }

    public void setAttribute(UmiAttributeNames name, String value) {
        this.setAttribute(name.getCodeKey(), value);
    }

    public void setAttribute(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return;
        }

        this.attributes.put(key, value);
    }

    public void removeAttribute(UmiAttributeNames name) {
        this.removeAttribute(name.getCodeKey());
    }

    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    public void merge(AttributeUmiData umiAttribute) {
        this.attributes.putAll(umiAttribute.getAttributes());
    }

    public void merge(Map<String, String> map) {
        this.attributes.putAll(map);
    }

    public Map<String, String> getAttributes() { return Collections.unmodifiableMap(this.attributes); }

    public Set<String> keys() {
        return this.attributes.keySet();
    }

    @Override
    public Object unwrap() {
        return this.attributes;
    }
}
