package com.clougence.utils.format;

import java.util.HashSet;
import java.util.Map;

import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

@Deprecated // use CollectionUtils.decorateCaseSensitive
public class MapCaseSensitiveUtil {

    @Deprecated // use CollectionUtils.decorateCaseSensitive
    public static Map<String, ?> decorateCaseSensitive(Map<String, ?> listMap) {
        Map<String, Object> inSensitiveMap = new LinkedCaseInsensitiveMap<>();
        HashSet<String> set = new HashSet<>(listMap.size());
        for (Map.Entry<String, ?> entry : listMap.entrySet()) {
            set.add(entry.getKey().toUpperCase());
            inSensitiveMap.put(entry.getKey(), entry.getValue());
        }
        if (set.size() != listMap.size()) {
            return listMap;
        }
        return inSensitiveMap;
    }
}
