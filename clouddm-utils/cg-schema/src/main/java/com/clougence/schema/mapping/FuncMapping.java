package com.clougence.schema.mapping;

import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaService.FunctionMappingService;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

/**
 * SqlType Mapping Registry
 *
 * @author mode create time is 2020-05-07
 */
public final class FuncMapping extends AbstractMapping<String> implements FunctionMappingService {

    @Override
    public String findMapping(String srcFunction, DsType targetDsType, MainVersion targetVersion) {
        Map<String, String> functionMapping = getMapping(targetDsType, targetVersion);
        if (functionMapping.containsKey("*")) {
            String mappingTo = functionMapping.get("*");
            if ("*".equals(mappingTo)) {
                return srcFunction;
            } else if (StringUtils.isNotBlank(mappingTo)) {
                return mappingTo;
            }
        }
        return functionMapping.get(srcFunction.toUpperCase());
    }
}
