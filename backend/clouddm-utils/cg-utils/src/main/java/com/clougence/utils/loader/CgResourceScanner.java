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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.clougence.utils.loader.ClassMatcher.ClassInfo;
import com.clougence.utils.loader.ClassMatcher.ClassMatcherContext;

/**
 * ClassFinder interface for ResourcesLoader
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public class CgResourceScanner {

    protected final ResourceLoader       loader;
    private final Map<String, ClassInfo> classInfoMap = new ConcurrentHashMap<>();

    /** Scanner using ClassPathResourceLoader scan,load class using new ClassLoader */
    public CgResourceScanner(ResourceLoader loader){
        this.loader = Objects.requireNonNull(loader, "loader is null.");
    }

    /**
     * Returns all classes in the scanned jar package that match the compareType parameter. (Depends on implementation results.)
     * @param matcher Matching rules.
     * @return Returns the results of the scan.
     */
    public Set<String> getClassNamesSet(String[] loadPackages, ClassMatcher matcher) {
        String[] packages = new String[loadPackages.length];
        for (int i = 0; i < loadPackages.length; i++) {
            packages[i] = loadPackages[i].replace(".", "/");
        }
        try {
            List<String> classes = this.loader.scanResources(ResourceLoader.MatchType.Prefix, event -> {
                String className = event.getName();
                if (!className.endsWith(".class")) {
                    return null;
                } else {
                    className = className.substring(0, className.length() - 6);
                    className = className.replace("/", ".");
                    try {
                        return testAndLoad(className, matcher);
                    } catch (ClassNotFoundException e) {
                        throw new IOException(e);
                    }
                }
            }, packages);
            return new HashSet<>(classes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String testAndLoad(String className, ClassMatcher matcher) throws IOException, ClassNotFoundException {
        ClassInfo classInfo = null;
        if (this.classInfoMap.containsKey(className)) {
            classInfo = this.classInfoMap.get(className);
        }
        if (classInfo == null) {
            classInfo = loadClassInfo(className);
            if (classInfo == null) {
                return null;
            }
            this.classInfoMap.put(className, classInfo);
        }

        ClassMatcherContext context = new ClassMatcherContext(classInfo) {

            @Override
            public ClassInfo loadClass(String className) throws IOException {
                return loadClassInfo(className);
            }
        };
        if (matcher.matcher(context)) {
            return className;
        } else {
            return null;
        }
    }

    /** Parses class bytecode, then reads the parent class and implemented interfaces. */
    public ClassInfo loadClassInfo(String className) throws IOException {
        try (InputStream classStream = this.loader.getResourceAsStream(className.replace('.', '/') + ".class")) {
            if (classStream == null) {
                return null;
            } else {
                return loadClassInfo(className, classStream);
            }
        }
    }

    /** Parses class bytecode, then reads the parent class and implemented interfaces. */
    private ClassInfo loadClassInfo(String className, final InputStream inStream) throws IOException {
        /* use ClassReader read basic info. */
        ClassReader classReader = new ClassReader(inStream);

        /* read className, parentType, interfaces、annos */
        final ClassInfo info = new ClassInfo();
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {

            @Override
            public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
                info.className = name.replace('/', '.');
                if (superName != null) {
                    info.superName = superName.replace('/', '.');
                }
                info.interFaces = interfaces;
                for (int i = 0; i < info.interFaces.length; i++) {
                    info.interFaces[i] = info.interFaces[i].replace('/', '.');
                }
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
                /* convert "Ljava/lang/Object;" to "java/lang/Object" */
                String[] annoArrays = info.annos == null ? new String[0] : info.annos;
                String[] newAnnoArrays = new String[annoArrays.length + 1];
                System.arraycopy(annoArrays, 0, newAnnoArrays, 0, annoArrays.length);

                String annnoType = desc.substring(1, desc.length() - 1);
                newAnnoArrays[newAnnoArrays.length - 1] = annnoType.replace('/', '.');
                info.annos = newAnnoArrays;
                return super.visitAnnotation(desc, visible);
            }
        }, ClassReader.SKIP_CODE);

        /* parents */
        if (info.superName != null) {
            try (InputStream superStream = this.loader.getResourceAsStream(info.superName.replace('.', '/') + ".class")) {
                if (superStream != null) {
                    this.loadClassInfo(info.superName, superStream);//Load parent
                }
            }
        }

        /* interfaces */
        for (String faces : info.interFaces) {
            try (InputStream superStream = this.loader.getResourceAsStream(faces.replace('.', '/') + ".class")) {
                if (superStream != null) {
                    this.loadClassInfo(faces, superStream);//load parent
                }
            }
        }
        /* Type chains */
        Set<String> castTypeList = new TreeSet<>();/* Convertible Type */
        String superName = info.superName;
        addCastTypeList(info, castTypeList);//this

        if (superName != null) {
            while (superName != null && this.classInfoMap.containsKey(superName)) {
                ClassInfo superInfo = this.classInfoMap.get(superName);
                addCastTypeList(superInfo, castTypeList);//super
                superName = superInfo.superName;
            }
        }
        info.castType = castTypeList.toArray(new String[0]);

        this.classInfoMap.put(info.className, info);
        return info;
    }

    private void addCastTypeList(final ClassInfo info, final Set<String> addTo) {
        if (info == null) {
            return;
        }
        addTo.add(info.className);
        if (info.superName != null) {
            addTo.add(info.superName);
        }
        if (info.interFaces != null) {
            for (String atFaces : info.interFaces) {
                addTo.add(atFaces);
                this.addCastTypeList(this.classInfoMap.get(atFaces), addTo);
            }
        }
    }
}
