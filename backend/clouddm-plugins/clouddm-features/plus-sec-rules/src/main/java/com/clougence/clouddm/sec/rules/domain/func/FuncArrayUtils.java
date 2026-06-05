/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
