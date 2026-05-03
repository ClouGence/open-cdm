/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clougence.utils.loader.jar;

import java.io.File;
import java.util.Objects;
import java.util.jar.JarEntry;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.jar.JarFile;
import org.junit.jupiter.api.Test;


/**
 * use jar -cvf0 xxx.jar * to package jar.
 * Tests for {@link JarFile}.
 */
public class NestedJarTests {
    @Test
    public void nestedJar_1() throws Exception {
        String file = ResourcesUtils.getResource("/nested-jars/nested-package-all.jar").getFile();
        JarFile jarFile = new JarFile(new File(file));

        JarEntry jarEntry1 = jarFile.getJarEntry("basic.jar");
        JarEntry jarEntry2 = jarFile.getJarEntry("app-1.jar");
        JarEntry jarEntry3 = jarFile.getJarEntry("app-2.zip");

        JarFile nestedJar1 = jarFile.getNestedJarFile(jarEntry1);
        JarFile nestedJar2 = jarFile.getNestedJarFile(jarEntry2);
        JarFile nestedJar3 = jarFile.getNestedJarFile(jarEntry3);

        assert nestedJar1.getJarEntry("app/lib/Foo.class") != null;
        assert nestedJar2.getJarEntry("app/onlyjar/FooOnlyJar.class") != null;
        assert nestedJar3.getJarEntry("app/onlyzip/FooOnlyZip.class") != null;
    }

    @Test
    public void nestedJar_2() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/basic.jar/app/lib/Foo.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/onlyjar/FooOnlyJar.class"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/onlyzip/FooOnlyZip.class"));

        String file = ResourcesUtils.getResource("/nested-jars/nested-package-all.jar").getFile();
        JarFile jarFile = new JarFile(new File(file));

        JarEntry jarEntry1 = jarFile.getJarEntry("basic.jar");
        JarEntry jarEntry2 = jarFile.getJarEntry("app-1.jar");
        JarEntry jarEntry3 = jarFile.getJarEntry("app-2.zip");

        JarFile nestedJar1 = jarFile.getNestedJarFile(jarEntry1);
        JarFile nestedJar2 = jarFile.getNestedJarFile(jarEntry2);
        JarFile nestedJar3 = jarFile.getNestedJarFile(jarEntry3);

        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(nestedJar1.getInputStream(nestedJar1.getJarEntry("app/lib/Foo.class"))));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(nestedJar2.getInputStream(nestedJar2.getJarEntry("app/onlyjar/FooOnlyJar.class"))));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(nestedJar3.getInputStream(nestedJar3.getJarEntry("app/onlyzip/FooOnlyZip.class"))));
    }
}
