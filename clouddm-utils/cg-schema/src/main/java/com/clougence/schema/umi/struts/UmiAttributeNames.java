package com.clougence.schema.umi.struts;

import java.util.Map;

/**
 * 属性Keys。
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public interface UmiAttributeNames {

    String getCodeKey();

    default String getValue(Map<String, String> attrMap) {
        return attrMap.get(getCodeKey());
    }

    default void setValue(Map<String, String> attrMap, String value) {
        attrMap.put(getCodeKey(), value);
    }
}
