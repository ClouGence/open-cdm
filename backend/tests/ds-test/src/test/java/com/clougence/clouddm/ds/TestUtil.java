package com.clougence.clouddm.ds;

import java.util.Set;

import com.clougence.detectrule.engine.lang.reflect.RuleIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class TestUtil {

    public static final String        LONG_SPLIT   = "------------------------------------------------------------------------------------------";
    public static final String        SHORT_SPLIT  = "----------";

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
        return objectMapper.readValue(json, new TypeReference<Set<String>>() {
        });
    }

    public static boolean equalsSet(Set<String> l1, Set<String> l2) {
        return l1.equals(l2);
    }
}
