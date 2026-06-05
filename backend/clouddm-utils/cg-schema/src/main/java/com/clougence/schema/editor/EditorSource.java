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
package com.clougence.schema.editor;

import java.util.Map;

import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.utils.CollectionUtils;

public interface EditorSource<T> {

    T getSource();

    String getAttr(String attrKey);

    void removeAttr(String attrKey);

    void addAttr(String attrKey, String attrValue);

    default void fillAttr(AttributeUmiData umiAttributes) {
        if (umiAttributes == null) {
            return;
        }
        Map<String, String> attrs = umiAttributes.getAttributes();
        if (attrs != null) {
            attrs.forEach(this::addAttr);
        }
    }

    default void fillAttr(Map<String, String> attrs) {
        if (CollectionUtils.isEmpty(attrs)) {
            return;
        }
        attrs.forEach(this::addAttr);
    }

}
