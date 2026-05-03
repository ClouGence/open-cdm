package com.clougence.schema.editor.builder.mappings;

import java.util.Map;

/**
 * @author mode 2021/5/21 19:56
 */
public interface MappingHandler {

    boolean hasColumnMapping(String srcType);

    String columnMapping(String srcType, Map<String, String> attrMap);

    boolean hasFunctionMapping(String srcFunction);

    String functionMapping(String srcFunction, Map<String, String> attrMap);
}
