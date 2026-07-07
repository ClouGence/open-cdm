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

import java.util.HashSet;
import java.util.Set;

import com.clougence.utils.loader.providers.ClassPathResourceLoader;

/**
 * ClassFinder interface for ResourcesLoader
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public class CgClassScanner extends CgResourceScanner {

    private final CgClassLoader classLoader;

    /** Scanner using ClassPathResourceLoader scan,load class using new ClassLoader */
    public CgClassScanner(ClassLoader parent, String resourcePrefix){
        super(new ClassPathResourceLoader(resourcePrefix));
        this.classLoader = this.loader.toClassLoader(parent);
    }

    /** Scanner using ClassPathResourceLoader scan,load class using new ClassLoader */
    public CgClassScanner(ClassPathResourceLoader loader){
        super(loader);
        this.classLoader = this.loader.toClassLoader(loader.getClassLoader());
    }

    /** Scanner using ResourceLoader scan,load class using new ClassLoader */
    public CgClassScanner(ClassLoader parent, ResourceLoader loader){
        super(loader);
        this.classLoader = this.loader.toClassLoader(parent);
    }

    /** Scanner using loader scan,load class using loader */
    public CgClassScanner(CgClassLoader loader){
        super(loader.getResourceLoader());
        this.classLoader = loader;
    }

    /**
     * Returns all classes in the scanned jar package that match the compareType parameter. (Depends on implementation results.)
     * @param matcherType Matching type, which can be the class description, implemented interfaces, or inherited parent class.
     * @return Returns the results of the scan.
     */
    public Set<Class<?>> getClassSet(String[] loadPackages, Class<?> matcherType) {
        return this.getClassSet(loadPackages, context -> {
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
    }

    /**
     * All classes in the scanning jar package that match the compareType parameters are returned. (Incumbent on implementation results)
     * @param matcher Match the rules.
     * @return Returns the results of the scan.
     */
    public Set<Class<?>> getClassSet(ClassMatcher matcher) {
        return this.getClassSet(new String[0], matcher);
    }

    /**
     * All classes in the scanning jar package that match the compareType parameters are returned. (Incumbent on implementation results)
     * @param matcher Match the rules.
     * @return Returns the results of the scan.
     */
    public Set<Class<?>> getClassSet(String[] loadPackages, ClassMatcher matcher) {
        Set<String> classNamesSet = this.getClassNamesSet(loadPackages, matcher);
        Set<Class<?>> classes = new HashSet<>();
        try {
            for (String className : classNamesSet) {
                classes.add(this.classLoader.loadClass(className));
            }
            return classes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
