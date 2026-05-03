package com.clougence.schema.metadata;

import com.clougence.utils.StringUtils;

/**
 * @author chunlin create time is 2025/4/15
 */
public enum CgTableType {

    BASE_TABLE,
    VIEW,;

    public static CgTableType of(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (CgTableType value : CgTableType.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }
}
