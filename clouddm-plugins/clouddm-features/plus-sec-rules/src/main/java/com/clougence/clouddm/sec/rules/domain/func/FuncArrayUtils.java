package com.clougence.clouddm.sec.rules.domain.func;

import java.lang.reflect.Array;
import java.util.List;

import com.clougence.detectrule.lang.CollectionAccess;
import com.clougence.detectrule.lang.reflect.RuleFunction;

public class FuncArrayUtils {

    @RuleFunction("size")
    public int size(Object array) {
        if (array == null) {
            return 0;
        } else if (array.getClass().isArray()) {
            return Array.getLength(array);
        } else if (array instanceof List) {
            return ((List<?>) array).size();
        } else if (array instanceof CollectionAccess) {
            return ((CollectionAccess) array).getSize();
        } else {
            throw new UnsupportedOperationException("@func.array.size type '" + array.getClass() + "' Unsupported");
        }
    }

    @RuleFunction("containAny")
    public boolean containAny(List<?> a, List<?> b) {
        for (Object o : a) {
            for (Object object : b) {
                if (o.toString().equals(object.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
