package com.clougence.schema.mapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaService;
import com.clougence.schema.metadata.MainVersion;

/**
 * Mapping Registry
 *
 * @author mode create time is 2020-05-07
 */
public abstract class AbstractMapping<T> implements SchemaService.BasicMappingService<T> {

    // tarDsType <SrcType -> TarType >
    private final Map<DsType, Map<T, MappingInfo<T>>> mappingMap = new HashMap<>();

    @Override
    public Map<T, T> getMapping(DsType targetDsType, MainVersion targetVersion) {
        final String version = targetVersion != null ? targetVersion.getMainVersion() : null;

        Map<T, MappingInfo<T>> typeMappingInfoMap = mappingMap.get(targetDsType);
        if (typeMappingInfoMap == null) {
            return Collections.emptyMap();
        } else {

            Map<T, T> map = new LinkedHashMap<>();
            for (Map.Entry<T, MappingInfo<T>> entry : typeMappingInfoMap.entrySet()) {
                MappingInfo<T> tarType = entry.getValue();
                T target = tarType.getTarget(version);
                if (target != null) {
                    map.put(entry.getKey(), target);
                }
            }
            return map;
        }
    }

    @Override
    public T findMapping(T srcType, DsType targetDsType, MainVersion targetVersion) {
        return getMapping(targetDsType, targetVersion).get(srcType);
    }

    @Override
    public void addMapping(DsType targetDsType, T sourceType, T targetType, MainVersion targetVersion, Map<String, String> attr) {
        // srcType -> tarType
        Map<T, MappingInfo<T>> typeMapping = mappingMap.computeIfAbsent(targetDsType, dsType -> new HashMap<>());

        // try find or init default
        MappingInfo<T> info = typeMapping.computeIfAbsent(sourceType, sourceTypeCopy -> new MappingInfo<>(sourceTypeCopy, targetType));

        // add version
        final String version = targetVersion != null ? targetVersion.getMainVersion() : null;
        info.addVersion(version, targetType);
        info.addAttr(version, attr);
    }
}
