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
package com.clougence.clouddm.api;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.utils.loader.CgResourceScanner;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

public class RSocketApiMethodNameTest {

    /** RSocket routes by interface name plus method name, so an overloaded api method shadows its sibling on the console side. */
    @Test
    public void rsocketApiMethodNamesAreUnique() throws Exception {
        String packageScope = "com.clougence.clouddm.api";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        CgResourceScanner scanner = new CgResourceScanner(new ClassPathResourceLoader(classLoader, ""));
        Set<String> classNames = scanner.getClassNamesSet(new String[] { packageScope }, context -> {
            if (context.getClassInfo() != null) {
                for (String anno : context.getClassInfo().annos) {
                    if (anno.equals(RSocketApiClass.class.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });

        List<String> duplicated = new ArrayList<>();
        int apiCount = 0;
        for (String className : classNames) {
            Class<?> clz = classLoader.loadClass(className);
            if (!clz.isInterface()) {
                continue;
            }
            apiCount++;

            Map<String, Integer> nameCount = new TreeMap<>();
            for (Method method : clz.getDeclaredMethods()) {
                nameCount.merge(method.getName(), 1, Integer::sum);
            }
            nameCount.forEach((name, count) -> {
                if (count > 1) {
                    duplicated.add(className + "." + name + " declared " + count + " times");
                }
            });
        }

        Assert.assertTrue("no @RSocketApiClass interface found under " + packageScope, apiCount > 0);
        Assert.assertTrue("RSocket api method names must be unique per interface: " + duplicated, duplicated.isEmpty());
    }
}
