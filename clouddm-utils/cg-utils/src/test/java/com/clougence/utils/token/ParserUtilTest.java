package com.clougence.utils.token;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ParserUtilTest {

    @Test
    public void testParse() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("cmd", "echo hello");
        MapVariableTokenHandler handler = new MapVariableTokenHandler("#{", "}", variables);
        GenericTokenParser parser = new GenericTokenParser(handler);
        String json = "cmd=\"#{cmd}\"";
        String res = parser.parse(json);
        System.out.println(res);
    }
}
