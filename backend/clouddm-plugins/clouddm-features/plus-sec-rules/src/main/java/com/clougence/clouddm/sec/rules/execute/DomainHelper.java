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
package com.clougence.clouddm.sec.rules.execute;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.objectweb.asm.*;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sec.rules.domain.CheckerDomain;
import com.clougence.clouddm.sec.rules.domain.internal.DynamicClass;

public class DomainHelper implements Opcodes {

    private static final Map<Class<?>, Class<?>>                   classCache  = new ConcurrentHashMap<>();
    private static final Map<ClassLoader, DomainMergedClassLoader> loaderCache = new ConcurrentHashMap<>();

    public static <T extends RuleDomain> List<CheckerDomain> create(List<T> domainData) {
        return domainData.stream().map(DomainHelper::create).collect(Collectors.toList());
    }

    public static <T extends RuleDomain> CheckerDomain create(T domainData) {
        try {
            Class<?> checkerType = genCheckerDomainClass(domainData);
            Constructor<?> c = checkerType.getConstructor(RuleDomain.class);
            return (CheckerDomain) c.newInstance(domainData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> genCheckerDomainClass(RuleDomain ruleDomain) {
        Class<?> domainType = ruleDomain.getClass();

        if (classCache.containsKey(domainType)) {
            return classCache.get(domainType);
        }

        ClassLoader domainLoader = ruleDomain.getClass().getClassLoader();
        DomainMergedClassLoader mergedLoader = loaderCache.computeIfAbsent(domainLoader, DomainMergedClassLoader::new);

        synchronized (classCache) {
            if (classCache.containsKey(domainType)) {
                return classCache.get(domainType);
            }

            byte[] checkerDomainBytes = domainCheckerGenBytes(ruleDomain);
            String checkerDomainName = newCheckTypeName(domainType);
            Class<?> checkerDomainType = mergedLoader.loadClass(checkerDomainName, checkerDomainBytes);
            classCache.put(domainType, checkerDomainType);
            return checkerDomainType;
        }
    }

    private static class DomainMergedClassLoader extends ClassLoader {

        private final ClassLoader domainLoader;

        public DomainMergedClassLoader(ClassLoader domainLoader){
            super(CheckerDomain.class.getClassLoader());
            this.domainLoader = domainLoader;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            try {
                return super.loadClass(name, resolve);
            } catch (Exception e1) {
                return this.domainLoader.loadClass(name);
            }
        }

        public Class<?> loadClass(String name, byte[] domainBytes) {
            return super.defineClass(name, domainBytes, 0, domainBytes.length);
        }
    }

    private static String newCheckTypeName(Class<?> domainType) {
        return CheckerDomain.class.getName() + ".internal." + domainType.getSimpleName() + "CheckerDomain";
    }

    private static byte[] domainCheckerGenBytes(RuleDomain ruleDomain) {
        String domainTypeAsmName = Type.getInternalName(ruleDomain.getClass());
        String domainTypeAsmDesc = "L" + domainTypeAsmName + ";";
        String domainCheckAsmName = Type.getInternalName(CheckerDomain.class);
        String domainCheckAsmDesc = "L" + domainCheckAsmName + ";";
        String baseDomainAsmName = Type.getInternalName(RuleDomain.class);
        String baseDomainAsmDesc = "L" + baseDomainAsmName + ";";

        //
        String newCheckName = newCheckTypeName(ruleDomain.getClass()).replace('.', '/');

        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, newCheckName, null, domainCheckAsmName, new String[] { Type.getInternalName(DynamicClass.class) });
        {
            FieldVisitor fv;
            fv = cw.visitField(ACC_PRIVATE, "domain", domainTypeAsmDesc, null, null);
            fv.visitEnd();
        }
        {
            MethodVisitor mv;
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + baseDomainAsmDesc + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, domainCheckAsmName, "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, domainTypeAsmName);
            mv.visitFieldInsn(PUTFIELD, newCheckName, "domain", domainTypeAsmDesc);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            MethodVisitor mv;
            mv = cw.visitMethod(ACC_PUBLIC, "getDomain", "()" + domainTypeAsmDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, newCheckName, "domain", domainTypeAsmDesc);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            MethodVisitor mv;
            mv = cw.visitMethod(ACC_PUBLIC, "setDomain", "(" + domainTypeAsmDesc + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, newCheckName, "domain", domainTypeAsmDesc);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

}
