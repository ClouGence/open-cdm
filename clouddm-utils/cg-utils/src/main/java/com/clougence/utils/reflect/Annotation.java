/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.utils.reflect;

import java.util.*;
import java.util.function.Function;

import com.clougence.utils.ClassUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;

/**
 * @author 赵永春 (zyc@hasor.net)
 */
public class Annotation {

    private static final Annotation         EMPTY = new Annotation();
    private final Map<String, List<Object>> confData;

    Annotation(){
        this.confData = new HashMap<>();
    }

    public void putData(String name, boolean value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, byte value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, short value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, int value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, long value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, float value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, double value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, char value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, String value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public void putData(String name, Enum<?> value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value.name());
    }

    public void putData(String name, Class<?> value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value.getName());
    }

    void putData(String name, List<?> value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).addAll(value);
    }

    public void putData(String name, Annotation value) {
        this.confData.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
    }

    public boolean getBoolean(String name) {
        return this.castValue(name, Boolean.TYPE, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return this.castValue(name, Boolean.TYPE, defaultValue);
    }

    public List<Boolean> getBooleanArray(String name) {
        return this.castValues(name, Boolean.TYPE);
    }

    public byte getByte(String name) {
        return this.castValue(name, Byte.TYPE, (byte) 0);
    }

    public byte getByte(String name, byte defaultValue) {
        return this.castValue(name, Byte.TYPE, defaultValue);
    }

    public List<Byte> getByteArray(String name) {
        return this.castValues(name, Byte.TYPE);
    }

    public short getShort(String name) {
        return this.castValue(name, Short.TYPE, (short) 0);
    }

    public short getShort(String name, short defaultValue) {
        return this.castValue(name, Short.TYPE, defaultValue);
    }

    public List<Short> getShortArray(String name) {
        return this.castValues(name, Short.TYPE);
    }

    public int getInt(String name) {
        return this.castValue(name, Integer.TYPE, 0);
    }

    public int getInt(String name, int defaultValue) {
        return this.castValue(name, Integer.TYPE, defaultValue);
    }

    public List<Integer> getIntArray(String name) {
        return this.castValues(name, Integer.TYPE);
    }

    public long getLong(String name) {
        return this.castValue(name, Long.TYPE, 0L);
    }

    public long getLong(String name, long defaultValue) {
        return this.castValue(name, Long.TYPE, defaultValue);
    }

    public List<Long> getLongArray(String name) {
        return this.castValues(name, Long.TYPE);
    }

    public float getFloat(String name) {
        return this.castValue(name, Float.TYPE, 0.0f);
    }

    public float getFloat(String name, float defaultValue) {
        return this.castValue(name, Float.TYPE, defaultValue);
    }

    public List<Float> getFloatArray(String name) {
        return this.castValues(name, Float.TYPE);
    }

    public double getDouble(String name) {
        return this.castValue(name, Double.TYPE, 0.0d);
    }

    public double getDouble(String name, double defaultValue) {
        return this.castValue(name, Double.TYPE, defaultValue);
    }

    public List<Double> getDoubleArray(String name) {
        return this.castValues(name, Double.TYPE);
    }

    public char getChar(String name) {
        return this.castValue(name, Character.class, '\0');
    }

    public char getChar(String name, char defaultValue) {
        return this.castValue(name, Character.class, defaultValue);
    }

    public List<Character> getCharArray(String name) {
        return this.castValues(name, Character.class);
    }

    public String getString(String name) {
        return this.castValue(name, String.class, null);
    }

    public String getString(String name, String defaultValue) {
        return this.castValue(name, String.class, defaultValue);
    }

    public List<String> getStringArray(String name) {
        return this.castValues(name, String.class);
    }

    public Class<?> getClass(String name) throws ClassNotFoundException {
        return this.castClass(name, null, false);
    }

    public Class<?> getClass(String name, boolean initialize) throws ClassNotFoundException {
        return this.castClass(name, null, initialize);
    }

    public Class<?> getClass(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return this.castClass(name, classLoader, false);
    }

    public Class<?> getClass(String name, ClassLoader classLoader, boolean initialize) throws ClassNotFoundException {
        return this.castClass(name, classLoader, initialize);
    }

    public List<Class<?>> getClassArray(String name) throws ClassNotFoundException {
        return this.castClasses(name, null, false);
    }

    public List<Class<?>> getClassArray(String name, boolean initialize) throws ClassNotFoundException {
        return this.castClasses(name, null, initialize);
    }

    public List<Class<?>> getClassArray(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return this.castClasses(name, classLoader, false);
    }

    public List<Class<?>> getClassArray(String name, ClassLoader classLoader, boolean initialize) throws ClassNotFoundException {
        return this.castClasses(name, classLoader, initialize);
    }

    public Enum<?> getEnum(String name, Enum<?>[] values) {
        return this.castEnum(name, values, null, null);
    }

    public Enum<?> getEnum(String name, Enum<?>[] values, Enum<?> defaultValue) {
        return this.castEnum(name, values, defaultValue, null);
    }

    public List<Enum<?>> getEnumArray(String name, Enum<?>[] values) {
        return this.castEnums(name, values, null);
    }

    public Enum<?> getEnum(String name, Enum<?>[] values, Function<String, Enum<?>> passer) {
        return this.castEnum(name, values, null, passer);
    }

    public Enum<?> getEnum(String name, Enum<?>[] values, Enum<?> defaultValue, Function<String, Enum<?>> passer) {
        return this.castEnum(name, values, defaultValue, passer);
    }

    public List<Enum<?>> getEnumArray(String name, Enum<?>[] values, Function<String, Enum<?>> passer) {
        return this.castEnums(name, values, passer);
    }

    public Annotation getAnnotation(String name) {
        return this.castAnnotation(name);
    }

    public Annotation getAnnotation(Class<?> name) {
        return this.castAnnotation(name.getName());
    }

    public List<Annotation> getAnnotationArray(String name) {
        return this.castAnnotations(name);
    }

    public List<Annotation> getAnnotationArray(Class<?> name) {
        return this.castAnnotations(name.getName());
    }

    protected <T> T castValue(String name, Class<?> toType, T defaultValue) {
        Object data = this.confData.get(name);
        if (data == null || ((List) data).isEmpty()) {
            return defaultValue;
        }

        return (T) ConverterUtils.convert(toType, ((List<?>) data).get(0));
    }

    protected <T> List<T> castValues(String name, Class<?> toType) {
        Object data = this.confData.getOrDefault(name, null);
        if (data == null || ((List) data).isEmpty()) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>();
        for (Object o : ((List) data)) {
            if (o != null) {
                result.add((T) ConverterUtils.convert(toType, o));
            }
        }
        return Collections.unmodifiableList(result);
    }

    private Class<?> castClass(String name, ClassLoader classLoader, boolean initialize) throws ClassNotFoundException {
        String string = getString(name);
        if (StringUtils.isBlank(string)) {
            return null;
        } else {
            return ClassUtils.getClass(classLoader, string, initialize);
        }
    }

    private List<Class<?>> castClasses(String name, ClassLoader classLoader, boolean initialize) throws ClassNotFoundException {
        List<String> string = getStringArray(name);
        if (CollectionUtils.isEmpty(string)) {
            return null;
        } else {
            List<Class<?>> result = new ArrayList<>();
            for (String s : string) {
                result.add(ClassUtils.getClass(classLoader, s, initialize));
            }
            return Collections.unmodifiableList(result);
        }
    }

    private Annotation castAnnotation(String name) {
        Object data = this.confData.getOrDefault(name, null);
        if (data == null || ((List) data).isEmpty()) {
            return null;
        }

        Object obj = ((List<?>) data).get(0);
        if (obj instanceof Annotation) {
            return (Annotation) obj;
        } else {
            return null;
        }
    }

    private List<Annotation> castAnnotations(String name) {
        Object data = this.confData.getOrDefault(name, null);
        if (data == null || ((List) data).isEmpty()) {
            return Collections.emptyList();
        }

        List<Annotation> result = new ArrayList<>();
        for (Object o : ((List) data)) {
            if (o instanceof Annotation) {
                result.add((Annotation) o);
            }
        }
        return Collections.unmodifiableList(result);
    }

    private Enum<?> castEnum(String name, Enum<?>[] values, Enum<?> defaultValue, Function<String, Enum<?>> passer) {
        if (values == null || values.length == 0) {
            return null;
        }

        String value = this.castValue(name, String.class, defaultValue != null ? defaultValue.name() : null);
        if (passer != null) {
            Enum<?> test = passer.apply(value);
            if (test != null) {
                return test;
            } else {
                return defaultValue;
            }
        }

        if (!StringUtils.isBlank(value)) {
            for (Enum<?> test : values) {
                if (StringUtils.equalsIgnoreCase(test.name(), value)) {
                    return test;
                }
            }
        }
        return defaultValue;
    }

    private List<Enum<?>> castEnums(String name, Enum<?>[] values, Function<String, Enum<?>> passer) {
        if (values == null || values.length == 0) {
            return Collections.emptyList();
        }

        List<String> value = this.castValues(name, String.class);
        if (CollectionUtils.isEmpty(value)) {
            return Collections.emptyList();
        }

        List<Enum<?>> result = new ArrayList<>();
        for (String s : value) {
            if (passer != null) {
                Enum<?> test = passer.apply(s);
                if (test != null) {
                    result.add(test);
                }
            } else {
                for (Enum<?> test : values) {
                    if (StringUtils.equalsIgnoreCase(test.name(), s)) {
                        result.add(test);
                    }
                }
            }
        }
        return result;
    }

    public static Annotation empty() {
        return EMPTY;
    }

    public static Annotation create() {
        return new Annotation();
    }

    boolean isEmpty() { return this.confData.isEmpty(); }
}
