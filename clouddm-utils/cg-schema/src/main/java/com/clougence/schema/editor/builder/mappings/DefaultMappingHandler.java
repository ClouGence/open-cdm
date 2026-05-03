package com.clougence.schema.editor.builder.mappings;

import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 * @author mode 2021/5/21 19:56
 */
public class DefaultMappingHandler implements MappingHandler {

    private final DsType              srcDsType;
    private final DsType              tarDsType;
    private final Map<String, String> typeMapping;
    private final MainVersion         targetMainVersion;

    public DefaultMappingHandler(DsType srcDsType, DsType tarDsType, MainVersion targetMainVersion){
        this.srcDsType = srcDsType;
        this.tarDsType = tarDsType;
        this.targetMainVersion = targetMainVersion;
        this.typeMapping = new LinkedCaseInsensitiveMap<>();
        Map<FieldType, FieldType> typeMap = SchemaFramework.getTypeMappingService(srcDsType).getMapping(tarDsType, targetMainVersion);
        typeMap.forEach((srcType, tarType) -> typeMapping.put(srcType.getCodeKey(), tarType.getCodeKey()));
    }

    @Override
    public boolean hasColumnMapping(String srcType) {
        return this.typeMapping.containsKey(srcType);
    }

    @Override
    public String columnMapping(String srcType, Map<String, String> attrMap) {
        FieldType realSrcFieldType = SchemaFramework.getFieldType(this.srcDsType, srcType);
        if (realSrcFieldType == null) {
            throw new IllegalArgumentException("source Type '" + srcType + "' is not exist.");
        }

        String targetType = this.typeMapping.get(realSrcFieldType.getCodeKey());
        if (StringUtils.isBlank(targetType)) {
            throw new IllegalArgumentException("source Type '" + srcType + "' has no mapping.");
        }
        return targetType;
    }

    @Override
    public boolean hasFunctionMapping(String srcFunction) {
        String targetType = SchemaFramework.getFunctionMappingService(this.srcDsType).findMapping(srcFunction, this.tarDsType, this.targetMainVersion);
        return StringUtils.isNotBlank(targetType);
    }

    @Override
    public String functionMapping(String srcFunction, Map<String, String> attrMap) {
        String targetType = SchemaFramework.getFunctionMappingService(this.srcDsType).findMapping(srcFunction, this.tarDsType, this.targetMainVersion);
        if (StringUtils.isBlank(targetType)) {
            throw new IllegalArgumentException("source Function '" + srcFunction + "' has no mapping.");
        }
        return targetType;
    }

}
