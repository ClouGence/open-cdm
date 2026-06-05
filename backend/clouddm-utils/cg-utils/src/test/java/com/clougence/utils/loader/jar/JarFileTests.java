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
package com.clougence.utils.loader.jar;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.jar.JarFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;


/**
 * Tests for {@link JarFile}.
 */
public class JarFileTests {
    @Test
    public void jarFile_1_1() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        java.util.jar.JarFile jarFile = new java.util.jar.JarFile(new File(file));
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<String> names = new HashSet<>();
        while (entries.hasMoreElements()) {
            names.add(entries.nextElement().getName());
        }
        assert !names.isEmpty();
        assert names.contains("app/FooImpl.class");
        assert names.contains("app/FooImpl.java");
        assert names.contains("META-INF/jar.mf");
        assert names.contains("META-INF/ReadMe.txt");
    }

    @Test
    public void jarFile_1_2() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarFile jarFile = new JarFile(new File(file));
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<String> names = new HashSet<>();
        while (entries.hasMoreElements()) {
            names.add(entries.nextElement().getName());
        }
        assert !names.isEmpty();
        assert names.contains("app/FooImpl.class");
        assert names.contains("app/FooImpl.java");
        assert names.contains("META-INF/jar.mf");
        assert names.contains("META-INF/ReadMe.txt");
    }

    @Test
    public void jarFile_2_1() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        java.util.jar.JarFile jarFile = new java.util.jar.JarFile(new File(file));
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<String> names = new HashSet<>();
        while (entries.hasMoreElements()) {
            names.add(entries.nextElement().getName());
        }
        assert !names.isEmpty();
        assert names.contains("app/FooImpl.class");
        assert names.contains("app/FooImpl.java");
        assert names.contains("META-INF/ReadMe.txt");
    }

    @Test
    public void jarFile_2_2() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarFile jarFile = new JarFile(new File(file));
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<String> names = new HashSet<>();
        while (entries.hasMoreElements()) {
            names.add(entries.nextElement().getName());
        }
        assert !names.isEmpty();
        assert names.contains("app/FooImpl.class");
        assert names.contains("app/FooImpl.java");
        assert names.contains("META-INF/ReadMe.txt");
    }

    @Test
    public void getManifest() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarFile jarFile = new JarFile(new File(file));

        assert jarFile.getManifest().getMainAttributes().getValue("Created-By").length() > 0;
    }

    @Test
    public void getManifestEntry() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarFile jarFile = new JarFile(new File(file));

        ZipEntry entry = jarFile.getJarEntry("META-INF/jar.mf");
        Manifest manifest = new Manifest(jarFile.getInputStream(entry));
        assert manifest.getMainAttributes().getValue("Created-By").equals("1.8.0_221 (Oracle Corporation)");
    }
}
