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
package com.clougence.clouddm.ds;

import java.util.Set;

import com.clougence.detectrule.lang.reflect.RuleIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class TestUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                return m.hasAnnotation(RuleIgnore.class);
            }
        });
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static Set<String> readToSet(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    public static boolean equalsSet(Set<String> l1, Set<String> l2) {
        return l1.equals(l2);
    }
}
