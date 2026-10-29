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
package com.clougence.sql.es;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

final class EsJsonAccess {
    private EsJsonAccess(){
    }

    static Map<String, EsJsonValue> object(EsJsonValue value) {
        if (!(value instanceof EsJsonObject object)) {
            throw new EsParseException(value.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
        }
        Map<String, EsJsonValue> fields = new LinkedHashMap<>();
        for (EsJsonMember member : object.members()) {
            if (fields.putIfAbsent(member.name().value(), member.value()) != null) {
                throw new EsParseException(member.name().range(), EsSqlI18nKeys.ES_AMBIGUOUS_REQUEST);
            }
        }
        return fields;
    }

    static Map<String, EsJsonValue> fields(EsJsonValue value, Set<String> allowed) {
        Map<String, EsJsonValue> fields = object(value);
        for (var field : fields.entrySet()) {
            if (!allowed.contains(field.getKey())) {
                throw new EsParseException(field.getValue().range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
        }
        return fields;
    }

    static String string(EsJsonValue value) {
        if (!(value instanceof EsJsonString string)) {
            throw new EsParseException(value.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
        }
        return string.value();
    }
}
