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
package com.clougence.utils.estimate;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author bucketli 2025/1/28 20:51:02
 *
 * copy from jdk.nashorn.internal.ir.debug.ObjectSizeCalculator ,as upper jdk8,jdk default remove this class.
 */
public final class CcObjectSizeCalculator {

    private final int                                                 arrayHeaderSize;
    private final int                                                 objectHeaderSize;
    private final int                                                 objectPadding;
    private final int                                                 referenceSize;
    private final int                                                 superclassFieldPadding;
    private final Map<Class<?>, CcObjectSizeCalculator.ClassSizeInfo> classSizeInfos       = new IdentityHashMap();
    private final Map<Object, Object>                                 alreadyVisited       = new IdentityHashMap();
    private final Map<Class<?>, ClassHistogramElement>                histogram            = new IdentityHashMap();
    private final Deque<Object>                                       pending              = new ArrayDeque(16384);
    private long                                                      size;
    static Class<?>                                                   managementFactory    = null;
    static Class<?>                                                   memoryPoolMXBean     = null;
    static Class<?>                                                   memoryUsage          = null;
    static Method                                                     getMemoryPoolMXBeans = null;
    static Method                                                     getUsage             = null;
    static Method                                                     getMax               = null;

    public static long getObjectSize(Object obj) throws UnsupportedOperationException {
        return obj == null ? 0L : (new CcObjectSizeCalculator(CcObjectSizeCalculator.CurrentLayout.SPEC)).calculateObjectSize(obj);
    }

    public CcObjectSizeCalculator(CcObjectSizeCalculator.MemoryLayoutSpecification memoryLayoutSpecification){
        Objects.requireNonNull(memoryLayoutSpecification);
        this.arrayHeaderSize = memoryLayoutSpecification.getArrayHeaderSize();
        this.objectHeaderSize = memoryLayoutSpecification.getObjectHeaderSize();
        this.objectPadding = memoryLayoutSpecification.getObjectPadding();
        this.referenceSize = memoryLayoutSpecification.getReferenceSize();
        this.superclassFieldPadding = memoryLayoutSpecification.getSuperclassFieldPadding();
    }

    public synchronized long calculateObjectSize(Object obj) {
        this.histogram.clear();

        try {
            Object o = obj;

            while (true) {
                this.visit(o);
                if (this.pending.isEmpty()) {
                    long var3 = this.size;
                    return var3;
                }

                o = this.pending.removeFirst();
            }
        } finally {
            this.alreadyVisited.clear();
            this.pending.clear();
            this.size = 0L;
        }
    }

    public List<ClassHistogramElement> getClassHistogram() { return new ArrayList(this.histogram.values()); }

    private CcObjectSizeCalculator.ClassSizeInfo getClassSizeInfo(Class<?> clazz) {
        CcObjectSizeCalculator.ClassSizeInfo csi = (CcObjectSizeCalculator.ClassSizeInfo) this.classSizeInfos.get(clazz);
        if (csi == null) {
            csi = new CcObjectSizeCalculator.ClassSizeInfo(clazz);
            this.classSizeInfos.put(clazz, csi);
        }

        return csi;
    }

    private void visit(Object obj) {
        if (!this.alreadyVisited.containsKey(obj)) {
            Class<?> clazz = obj.getClass();
            if (clazz == CcObjectSizeCalculator.ArrayElementsVisitor.class) {
                ((CcObjectSizeCalculator.ArrayElementsVisitor) obj).visit(this);
            } else {
                this.alreadyVisited.put(obj, obj);
                if (clazz.isArray()) {
                    this.visitArray(obj);
                } else {
                    this.getClassSizeInfo(clazz).visit(obj, this);
                }
            }

        }
    }

    private void visitArray(Object array) {
        Class<?> arrayClass = array.getClass();
        Class<?> componentType = arrayClass.getComponentType();
        int length = Array.getLength(array);
        if (componentType.isPrimitive()) {
            this.increaseByArraySize(arrayClass, length, getPrimitiveFieldSize(componentType));
        } else {
            this.increaseByArraySize(arrayClass, length, (long) this.referenceSize);
            switch (length) {
                case 0:
                    break;
                case 1:
                    this.enqueue(Array.get(array, 0));
                    break;
                default:
                    this.enqueue(new CcObjectSizeCalculator.ArrayElementsVisitor((Object[]) ((Object[]) array)));
            }
        }

    }

    private void increaseByArraySize(Class<?> clazz, int length, long elementSize) {
        this.increaseSize(clazz, roundTo((long) this.arrayHeaderSize + (long) length * elementSize, this.objectPadding));
    }

    void enqueue(Object obj) {
        if (obj != null) {
            this.pending.addLast(obj);
        }

    }

    void increaseSize(Class<?> clazz, long objectSize) {
        ClassHistogramElement he = (ClassHistogramElement) this.histogram.get(clazz);
        if (he == null) {
            he = new ClassHistogramElement(clazz);
            this.histogram.put(clazz, he);
        }

        he.addInstance(objectSize);
        this.size += objectSize;
    }

    static long roundTo(long x, int multiple) {
        return (x + (long) multiple - 1L) / (long) multiple * (long) multiple;
    }

    private static long getPrimitiveFieldSize(Class<?> type) {
        if (type != Boolean.TYPE && type != Byte.TYPE) {
            if (type != Character.TYPE && type != Short.TYPE) {
                if (type != Integer.TYPE && type != Float.TYPE) {
                    if (type != Long.TYPE && type != Double.TYPE) {
                        throw new AssertionError("Encountered unexpected primitive type " + type.getName());
                    } else {
                        return 8L;
                    }
                } else {
                    return 4L;
                }
            } else {
                return 2L;
            }
        } else {
            return 1L;
        }
    }

    public static CcObjectSizeCalculator.MemoryLayoutSpecification getEffectiveMemoryLayoutSpecification() {
        String vmName = System.getProperty("java.vm.name");
        if (vmName != null && (vmName.startsWith("OpenJDK ") || vmName.startsWith("Java HotSpot(TM) "))) {
            String dataModel = System.getProperty("sun.arch.data.model");
            if ("32".equals(dataModel)) {
                return new CcObjectSizeCalculator.MemoryLayoutSpecification() {

                    public int getArrayHeaderSize() { return 12; }

                    public int getObjectHeaderSize() { return 8; }

                    public int getObjectPadding() { return 8; }

                    public int getReferenceSize() { return 4; }

                    public int getSuperclassFieldPadding() { return 4; }
                };
            } else if (!"64".equals(dataModel)) {
                throw new UnsupportedOperationException("Unrecognized value '" + dataModel + "' of sun.arch.data.model system property");
            } else {
                String strVmVersion = System.getProperty("java.vm.version");
                int vmVersion = Integer.parseInt(strVmVersion.substring(0, strVmVersion.indexOf(46)));
                if (vmVersion >= 17) {
                    long maxMemory = 0L;
                    if (getMemoryPoolMXBeans == null) {
                        throw new AssertionError("java.lang.management not available in compact 1");
                    }

                    try {
                        List<?> memoryPoolMXBeans = (List) getMemoryPoolMXBeans.invoke(managementFactory);

                        Object max;
                        for (Iterator var7 = memoryPoolMXBeans.iterator(); var7.hasNext(); maxMemory += (Long) max) {
                            Object mp = var7.next();
                            Object usage = getUsage.invoke(mp);
                            max = getMax.invoke(usage);
                        }
                    } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var11) {
                        throw new AssertionError("java.lang.management not available in compact 1");
                    }

                    if (maxMemory < 32212254720L) {
                        return new CcObjectSizeCalculator.MemoryLayoutSpecification() {

                            public int getArrayHeaderSize() { return 16; }

                            public int getObjectHeaderSize() { return 12; }

                            public int getObjectPadding() { return 8; }

                            public int getReferenceSize() { return 4; }

                            public int getSuperclassFieldPadding() { return 4; }
                        };
                    }
                }

                return new CcObjectSizeCalculator.MemoryLayoutSpecification() {

                    public int getArrayHeaderSize() { return 24; }

                    public int getObjectHeaderSize() { return 16; }

                    public int getObjectPadding() { return 8; }

                    public int getReferenceSize() { return 8; }

                    public int getSuperclassFieldPadding() { return 8; }
                };
            }
        } else {
            throw new UnsupportedOperationException("ObjectSizeCalculator only supported on HotSpot VM");
        }
    }

    static {
        try {
            managementFactory = Class.forName("java.lang.management.ManagementFactory");
            memoryPoolMXBean = Class.forName("java.lang.management.MemoryPoolMXBean");
            memoryUsage = Class.forName("java.lang.management.MemoryUsage");
            getMemoryPoolMXBeans = managementFactory.getMethod("getMemoryPoolMXBeans");
            getUsage = memoryPoolMXBean.getMethod("getUsage");
            getMax = memoryUsage.getMethod("getMax");
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException var1) {
        }

    }

    private class ClassSizeInfo {

        private final long    objectSize;
        private final long    fieldsSize;
        private final Field[] referenceFields;

        public ClassSizeInfo(Class<?> clazz){
            long newFieldsSize = 0L;
            List<Field> newReferenceFields = new LinkedList();
            Field[] var6 = clazz.getDeclaredFields();
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                Field f = var6[var8];
                if (!Modifier.isStatic(f.getModifiers())) {
                    Class<?> type = f.getType();
                    if (type.isPrimitive()) {
                        newFieldsSize += CcObjectSizeCalculator.getPrimitiveFieldSize(type);
                    } else {
                        f.setAccessible(true);
                        newReferenceFields.add(f);
                        newFieldsSize += (long) CcObjectSizeCalculator.this.referenceSize;
                    }
                }
            }

            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                CcObjectSizeCalculator.ClassSizeInfo superClassInfo = CcObjectSizeCalculator.this.getClassSizeInfo(superClass);
                newFieldsSize += CcObjectSizeCalculator.roundTo(superClassInfo.fieldsSize, CcObjectSizeCalculator.this.superclassFieldPadding);
                newReferenceFields.addAll(Arrays.asList(superClassInfo.referenceFields));
            }

            this.fieldsSize = newFieldsSize;
            this.objectSize = CcObjectSizeCalculator.roundTo((long) CcObjectSizeCalculator.this.objectHeaderSize + newFieldsSize, CcObjectSizeCalculator.this.objectPadding);
            this.referenceFields = (Field[]) newReferenceFields.toArray(new Field[newReferenceFields.size()]);
        }

        void visit(Object obj, CcObjectSizeCalculator calc) {
            calc.increaseSize(obj.getClass(), this.objectSize);
            this.enqueueReferencedObjects(obj, calc);
        }

        public void enqueueReferencedObjects(Object obj, CcObjectSizeCalculator calc) {
            Field[] var3 = this.referenceFields;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field f = var3[var5];

                try {
                    calc.enqueue(f.get(obj));
                } catch (IllegalAccessException var9) {
                    AssertionError ae = new AssertionError("Unexpected denial of access to " + f);
                    ae.initCause(var9);
                    throw ae;
                }
            }

        }
    }

    private static class ArrayElementsVisitor {

        private final Object[] array;

        ArrayElementsVisitor(Object[] array){
            this.array = array;
        }

        public void visit(CcObjectSizeCalculator calc) {
            Object[] var2 = this.array;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Object elem = var2[var4];
                if (elem != null) {
                    calc.visit(elem);
                }
            }

        }
    }

    private static class CurrentLayout {

        private static final CcObjectSizeCalculator.MemoryLayoutSpecification SPEC = CcObjectSizeCalculator.getEffectiveMemoryLayoutSpecification();

        private CurrentLayout(){
        }
    }

    public interface MemoryLayoutSpecification {

        int getArrayHeaderSize();

        int getObjectHeaderSize();

        int getObjectPadding();

        int getReferenceSize();

        int getSuperclassFieldPadding();
    }
}
