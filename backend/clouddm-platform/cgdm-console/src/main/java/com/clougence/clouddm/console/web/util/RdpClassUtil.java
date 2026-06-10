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
package com.clougence.clouddm.console.web.util;

import java.util.HashSet;
import java.util.Set;

import com.clougence.utils.loader.CgResourceScanner;
import com.clougence.utils.loader.ClassMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public class RdpClassUtil {

    /**
     * Returns all classes in the scanned jar package that match the compareType parameter. (Depends on implementation results.)
     * @param matcherType Matching type, which can be the class description, implemented interfaces, or inherited parent class.
     * @return Returns the results of the scan.
     */
    public static Set<Class<?>> getClassSet(ClassLoader classLoader, CgResourceScanner scanner, String[] loadPackages, Class<?> matcherType) throws ClassNotFoundException {
        Set<String> namesSet = scanner.getClassNamesSet(loadPackages, context -> {
            ClassMatcher.ClassInfo classInfo = context.getClassInfo();
            String matcherTypeName = matcherType.getName();

            if (classInfo != null) {
                if (matcherType.isAnnotation()) {
                    for (String anno : classInfo.annos) {
                        if (anno.equals(matcherTypeName)) {
                            return true;
                        }
                    }
                } else if (matcherType.isInterface()) {
                    for (String anno : classInfo.interFaces) {
                        if (anno.equals(matcherTypeName)) {
                            return true;
                        }
                    }
                } else {
                    for (String castType : classInfo.castType) {
                        if (castType.equals(matcherTypeName)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        });

        Set<Class<?>> classSet = new HashSet<>();
        for (String name : namesSet) {
            classSet.add(classLoader.loadClass(name));
        }
        return classSet;
    }
}
