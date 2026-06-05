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
package com.clougence.utils.loader;

import java.util.function.Predicate;

/**
 * 扫描过程中的匹配器
 * @version : 2021-10-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class ClassMatchers {

    private ClassMatchers(){
    }

    /** 任意类型 */
    public static Predicate<?> any() {
        return t -> true;
    }

    /** 类型 或 类上有 xx 注解 */
    public static ClassMatcher featureWith(final Class<?> annotationType) {
        String featureType = annotationType.getName();
        return context -> {
            ClassMatcher.ClassInfo classInfo = context.getClassInfo();
            for (String castType : classInfo.castType) {
                if (castType.equals(featureType)) {
                    return true;
                }
            }
            for (String face : classInfo.annos) {
                if (face.equals(featureType)) {
                    return true;
                }
            }
            return false;
        };
    }

    /** 类上需要有 xx 注解 */
    public static ClassMatcher annotatedWith(final Class<?> annotationType) {
        String featureType = annotationType.getName();
        return context -> {
            ClassMatcher.ClassInfo classInfo = context.getClassInfo();
            for (String face : classInfo.annos) {
                if (face.equals(featureType)) {
                    return true;
                }
            }
            return false;
        };
    }

    /** 子类 */
    public static ClassMatcher subClassesOf(final Class<?> superclass) {
        String testType = superclass.getName();
        return context -> {
            ClassMatcher.ClassInfo classInfo = context.getClassInfo();
            if (classInfo.className.equals(testType)) {
                return false;
            }
            for (String castType : classInfo.castType) {
                if (castType.equals(testType)) {
                    return true;
                }
            }
            return false;
        };
    }
}
