package com.clougence.schema.editor;

import java.util.Map;

import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.utils.CollectionUtils;

public interface EditorSource<T> {

    T getSource();

    String getAttr(String attrKey);

    void removeAttr(String attrKey);

    void addAttr(String attrKey, String attrValue);

    default void fillAttr(AttributeUmiData umiAttributes) {
        if (umiAttributes == null) {
            return;
        }
        Map<String, String> attrs = umiAttributes.getAttributes();
        if (attrs != null) {
            attrs.forEach(this::addAttr);
        }
    }

    default void fillAttr(Map<String, String> attrs) {
        if (CollectionUtils.isEmpty(attrs)) {
            return;
        }
        attrs.forEach(this::addAttr);
    }

}
