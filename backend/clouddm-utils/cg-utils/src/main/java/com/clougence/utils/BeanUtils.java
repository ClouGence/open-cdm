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
package com.clougence.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import com.clougence.utils.convert.ConverterUtils;

/**
 *
 * @version : 2011-6-3
 * @author 赵永春 (zyc@hasor.net)
 */
public class BeanUtils {

    /**Gets the default value for the specified type. */
    public static Object getDefaultValue(final Class<?> returnType) {
        if (returnType == null || !returnType.isPrimitive()) {
            return null;
        }
        //
        if (returnType == int.class) {
            return 0;
        } else if (returnType == byte.class) {
            return (byte) 0;
        } else if (returnType == char.class) {
            return '\0';
        } else if (returnType == double.class) {
            return 0d;
        } else if (returnType == float.class) {
            return 0f;
        } else if (returnType == long.class) {
            return 0L;
        } else if (returnType == short.class) {
            return (short) 0;
        } else if (returnType == boolean.class) {
            return false;
        } else if (returnType == void.class) {
            return null;
        } else if (returnType.isArray()) {
            return null;
        }
        return null;
    }

    public static Object[] getDefaultValue(Class<?>[] paramArray) {
        if (paramArray == null) {
            return null;
        }
        Object[] objs = new Object[paramArray.length];
        for (int i = 0; i < paramArray.length; i++) {
            objs[i] = getDefaultValue(paramArray[i]);
        }
        return objs;
    }

    /**
     * Invokes a method on the target object by reflection.
     * @param target Callee.
     * @param methodName Method name to call.
     * @param objects Parameter list.
     */
    public static Object invokeMethod(final Object target, final String methodName, final Object... objects) throws IllegalArgumentException, IllegalAccessException,
                                                                                                             InvocationTargetException {
        if (target == null) {
            return null;
        }
        Class<?> targetType = target.getClass();
        Method invokeMethod = null;
        //Reflection Call Method
        Method[] ms = targetType.getMethods();
        for (Method m : ms) {
            //1. Skip methods with a different name.
            if (!m.getName().equals(methodName)) {
                continue;
            }
            //2. Skip methods whose parameter count does not match.
            Class<?>[] paramTypes = m.getParameterTypes();
            if (paramTypes.length != objects.length) {
                continue;
            }
            //3. Skip methods whose parameter types do not match.
            boolean isFind = true;
            for (int i = 0; i < paramTypes.length; i++) {
                Object param_object = objects[i];
                if (param_object == null) {
                    continue;
                }
                //
                if (!paramTypes[i].isAssignableFrom(param_object.getClass())) {
                    isFind = false;
                    break;
                }
            }
            //5. Skip unmatched parameter types.
            if (!isFind) {
                continue;
            }
            //Execute the matching method.
            invokeMethod = m;
        }
        if (invokeMethod == null) {
            throw new NullPointerException(methodName + " invokeMethod is null.");
        } else {
            return invokeMethod.invoke(target, objects);
        }
    }
    /*----------------------------------------------------------------------------------------*/

    /** Gets fields declared by the class and inherited from parent classes; fields redefined in subclasses are also included. */
    public static List<Field> findALLFields(final Class<?> target) {
        if (target == null) {
            return null;
        }
        ArrayList<Field> fList = new ArrayList<>();
        BeanUtils.findALLFields(target, fList);
        return fList;
    }

    private static void findALLFields(final Class<?> target, final ArrayList<Field> fList) {
        if (target == null) {
            return;
        }
        for (Field field : target.getDeclaredFields()) {
            if (!fList.contains(field)) {
                fList.add(field);
            }
        }
        for (Field field : target.getFields()) {
            if (!fList.contains(field)) {
                fList.add(field);
            }
        }
        Class<?> superType = target.getSuperclass();
        if (superType == null || superType == target) {
            return;
        }
        BeanUtils.findALLFields(superType, fList);
    }

    /** Gets methods declared by the class and inherited from parent classes; overridden subclass methods are also returned. */
    public static List<Method> findALLMethods(final Class<?> target) {
        if (target == null) {
            return null;
        }
        ArrayList<Method> mList = new ArrayList<>();
        BeanUtils.findALLMethods(target, mList);
        return mList;
    }

    private static void findALLMethods(final Class<?> target, final ArrayList<Method> mList) {
        if (target == null) {
            return;
        }
        for (Method method : target.getDeclaredMethods()) {
            if (!mList.contains(method)) {
                mList.add(method);
            }
        }
        for (Method method : target.getMethods()) {
            if (!mList.contains(method)) {
                mList.add(method);
            }
        }
        Class<?> superType = target.getSuperclass();
        if (superType == null || superType == target) {
            return;
        }
        BeanUtils.findALLMethods(superType, mList);
    }
    /*----------------------------------------------------------------------------------------*/

    /** Finds a list of accessible fields. */
    public static List<Field> getFields(final Class<?> type) {
        return Arrays.asList(type.getFields());
    }

    /** Finds a list of accessible methods. */
    public static List<Method> getMethods(final Class<?> type) {
        return Arrays.asList(type.getMethods());
    }

    /** Finds an accessible field. */
    public static Field getField(final String fieldName, final Class<?> type) {
        if (fieldName == null || type == null) {
            return null;
        }
        for (Field f : type.getFields()) {
            if (f.getName().equals(fieldName)) {
                return f;
            }
        }
        for (Field f : type.getDeclaredFields()) {
            if (f.getName().equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    /** Finds an accessible method. */
    public static Method getMethod(final Class<?> atClass, final String name, final Class<?>[] paramType) {
        try {
            return atClass.getMethod(name, paramType);
        } catch (Exception e) {
            try {
                return atClass.getDeclaredMethod(name, paramType);
            } catch (Exception e1) {
                return null;
            }
        }
    }

    /** Gets property names and also exposes accessible fields as properties. */
    public static List<String> getPropertiesAndFields(final Class<?> target) {
        List<String> mnames = BeanUtils.getProperties(target);
        List<Field> fnames = BeanUtils.getFields(target);
        for (Field f : fnames) {
            String fName = f.getName();
            if (!mnames.contains(fName)) {
                mnames.add(fName);
            }
        }
        return mnames;
    }

    /** Gets property names, including read-only, write-only, and read-write properties. */
    public static List<String> getProperties(final Class<?> target) {
        List<String> mnames = new ArrayList<>();
        List<Method> ms = BeanUtils.getMethods(target);
        for (Method m : ms) {
            if (m.getDeclaringClass() == Object.class) {
                continue;
            }
            String name = m.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                name = name.substring(3);
            } else if (name.startsWith("is")) {
                name = name.substring(2);
            } else {
                continue;
            }
            if (!name.equals("")) {
                name = StringUtils.firstCharToLowerCase(name);
                if (!mnames.contains(name)) {
                    mnames.add(name);
                }
            }
        }
        return mnames;
    }

    /** Gets property descriptors, including read-only, write-only, and read-write properties. */
    public static PropertyDescriptor[] getPropertyDescriptors(final Class<?> defineType) {
        List<PropertyDescriptor> mnames = new ArrayList<>();
        List<String> ms = BeanUtils.getProperties(defineType);
        for (String m : ms) {
            try {
                mnames.add(new PropertyDescriptor(m, defineType));
            } catch (Exception e) {
            }
        }
        return mnames.toArray(new PropertyDescriptor[0]);
    }

    /** Gets the read method for a property. */
    public static Method getReadMethod(final String property, final Class<?> target) {
        if (property == null || target == null) {
            return null;
        }
        String methodName_1 = "get" + StringUtils.firstCharToUpperCase(property);
        String methodName_2 = "is" + StringUtils.firstCharToUpperCase(property);
        //
        for (Method m : target.getMethods()) {
            if (m.getParameterTypes().length == 0) {
                String methodName = m.getName();
                if (methodName.equals(methodName_1)) {
                    return m;
                }
                /* Boolean getter. */
                if (methodName.equals(methodName_2)) {
                    Class<?> t = m.getReturnType();
                    if (t == Boolean.class || t == boolean.class) {
                        return m;
                    }
                }
            }
        }
        return null;
    }

    /** Gets the write method for a property. */
    public static Method getWriteMethod(final String property, final Class<?> target) {
        if (property == null || target == null) {
            return null;
        }
        String methodName = "set" + StringUtils.firstCharToUpperCase(property);
        for (Method m : target.getMethods()) {
            if (m.getName().equals(methodName)) {
                if (m.getParameterTypes().length == 1) {
                    return m;
                }
            }
        }
        return null;
    }

    /** Tests whether the target has the named property through either a read or write method. */
    public static boolean hasProperty(final String propertyName, final Class<?> target) {
        //Get, set method
        if (BeanUtils.getReadMethod(propertyName, target) == null) {
            return BeanUtils.getWriteMethod(propertyName, target) != null;
        }
        return true;
    }

    /** Tests whether the target has the named field. */
    public static boolean hasField(final String propertyName, final Class<?> target) {
        return BeanUtils.getField(propertyName, target) != null;
    }

    /** Tests whether the target has the named property or field. */
    public static boolean hasPropertyOrField(final String name, final Class<?> target) {
        if (!BeanUtils.hasProperty(name, target)) {
            return BeanUtils.hasField(name, target);
        }
        return true;
    }

    /** Tests whether {@link #readProperty(Object, String)} is supported. */
    public static boolean canReadProperty(final String propertyName, final Class<?> target) {
        Method readMethod = BeanUtils.getReadMethod(propertyName, target);
        return readMethod != null;
    }

    /** Tests whether {@link #readPropertyOrField(Object, String)} is supported. */
    public static boolean canReadPropertyOrField(final String propertyName, final Class<?> target) {
        if (!BeanUtils.canReadProperty(propertyName, target)) {
            return BeanUtils.hasField(propertyName, target);
        }
        return true;
    }

    /** Tests whether {@link #writeProperty(Object, String, Object)} is supported. */
    public static boolean canWriteProperty(final String propertyName, final Class<?> target) {
        Method writeMethod = BeanUtils.getWriteMethod(propertyName, target);
        return writeMethod != null;
    }

    /** Tests whether field writing is supported. */
    public static boolean canWriteField(final String propertyName, final Class<?> target) {
        Field field = getField(propertyName, target);
        return field != null && !Modifier.isFinal(field.getModifiers());
    }

    /** Tests whether {@link #writePropertyOrField(Object, String, Object)} is supported. */
    public static boolean canWritePropertyOrField(final String propertyName, final Class<?> target) {
        if (!BeanUtils.canWriteProperty(propertyName, target)) {
            return BeanUtils.canWriteField(propertyName, target);
        }
        return true;
    }
    /*----------------------------------------------------------------------------------------*/

    /** Writes a property value after converting the input to the property's type. Returns whether the write succeeded. */
    public static boolean writeProperty(final Object object, final String attName, final Object value) {
        if (object == null || attName == null) {
            return false;
        }
        //1. Find the write method.
        Class<?> defineType = object.getClass();
        Method writeMethod = BeanUtils.getWriteMethod(attName, defineType);
        if (writeMethod == null) {
            return false;
        }
        //2. Convert the property value.
        Class<?> toType = writeMethod.getParameterTypes()[0];
        Object attValueObject = ConverterUtils.convert(toType, value);
        //3. Write the property value.
        try {
            writeMethod.invoke(object, attValueObject);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Writes a field value after converting the input to the field's type. Returns whether the write succeeded. */
    public static boolean writeField(final Object object, final String fieldName, final Object value) {
        if (object == null || fieldName == null) {
            return false;
        }
        //1. Find the field.
        Class<?> defineType = object.getClass();
        Field writeField = BeanUtils.getField(fieldName, defineType);
        if (writeField == null) {
            return false;
        }
        //2. Convert the field value.
        Class<?> toType = writeField.getType();
        Object attValueObject = ConverterUtils.convert(toType, value);
        //3. Write the field value.
        try {
            writeField.setAccessible(true);
            writeField.set(object, attValueObject);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Writes a property first; if that is not supported, writes the matching field. */
    public static boolean writePropertyOrField(final Object object, final String attName, final Object value) {
        Class<?> defineType = object.getClass();
        if (BeanUtils.canWriteProperty(attName, defineType)) {
            return BeanUtils.writeProperty(object, attName, value);//Support method writing
        }
        if (BeanUtils.hasField(attName, defineType)) {
            return BeanUtils.writeField(object, attName, value);//Support field writing
        }
        return false;
    }

    /** Reads a property value. */
    public static Object readProperty(final Object object, final String attName) {
        if (object == null || attName == null) {
            return false;
        }
        //1. Find the read method.
        Class<?> defineType = object.getClass();
        Method readMethod = BeanUtils.getReadMethod(attName, defineType);
        if (readMethod == null) {
            return null;
        }
        //Read the property.
        try {
            return readMethod.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }

    /** Reads a field value. */
    public static Object readField(final Object object, final String fieldName) {
        if (object == null || fieldName == null) {
            return null;
        }
        //1. Find the field.
        Class<?> defineType = object.getClass();
        Field readField = BeanUtils.getField(fieldName, defineType);
        if (readField == null) {
            return null;
        }
        //2. Execute field reading
        try {
            readField.setAccessible(true);
            return readField.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    /** Reads a property first; if that is not supported, reads the matching field. */
    public static Object readPropertyOrField(final Object object, final String attName) {
        Class<?> defineType = object.getClass();
        if (BeanUtils.canReadProperty(attName, defineType)) {
            return BeanUtils.readProperty(object, attName);//Support method reading
        }
        if (BeanUtils.hasField(attName, defineType)) {
            return BeanUtils.readField(object, attName);//Support field reading
        }
        return null;
    }

    /***/
    public static Class<?> getPropertyType(final Class<?> defineType, final String attName) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(attName, defineType);
            return pd.getPropertyType();
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, Class<?>> getPropertyType(Class<?> target) {
        Map<String, Class<?>> propertyMap = new LinkedHashMap<>();
        for (Method m : target.getMethods()) {
            if (m.getDeclaringClass() == Object.class) {
                continue;
            }

            String name = m.getName();
            if (name.startsWith("get")) {
                name = StringUtils.firstCharToLowerCase(name.substring(3));
                propertyMap.put(name, m.getReturnType());
            } else if (name.startsWith("is")) {
                name = StringUtils.firstCharToLowerCase(name.substring(2));
                propertyMap.put(name, m.getReturnType());
            } else if (name.startsWith("set")) {
                name = StringUtils.firstCharToLowerCase(name.substring(3));
                propertyMap.put(name, m.getParameterTypes()[0]);
            }
        }

        return propertyMap;
    }

    /***/
    public static Class<?> getFieldType(final Class<?> defineType, final String attName) {
        Field readField = BeanUtils.getField(attName, defineType);
        if (readField != null) {
            return readField.getType();
        }
        return null;
    }

    /***/
    public static Class<?> getPropertyOrFieldType(final Class<?> defineType, final String attName) {
        Class<?> propType = null;
        //
        propType = BeanUtils.getPropertyType(defineType, attName);
        if (propType != null) {
            return propType;
        }
        propType = BeanUtils.getFieldType(defineType, attName);
        return propType;
    }

    /***/
    public static void copyProperties(final Object dest, final Object orig) {
        if (dest == null) {
            throw new IllegalArgumentException("dest is null");
        }
        if (orig == null) {
            throw new IllegalArgumentException("orig is null");
        }
        //
        List<String> propNames = new ArrayList<>();
        if (orig instanceof Map) {
            for (Object key : ((Map) orig).keySet()) {
                propNames.add(key.toString());
            }
        } else {
            propNames = BeanUtils.getProperties(orig.getClass());
        }
        for (String prop : propNames) {
            BeanUtils.copyProperty(dest, orig, prop);
        }
    }

    /***/
    public static void copyProperty(final Object dest, final Object orig, final String propertyName) {
        if (dest == null) {
            throw new IllegalArgumentException("dest is null");
        }
        if (orig == null) {
            throw new IllegalArgumentException("orig is null");
        }
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("propertyName is null");
        }
        //
        if (!(orig instanceof Map)) {
            if (!BeanUtils.canReadPropertyOrField(propertyName, orig.getClass())) {
                return;
            }
        }
        if (!(dest instanceof Map)) {
            if (!BeanUtils.canWritePropertyOrField(propertyName, dest.getClass())) {
                return;
            }
        }
        //
        Object val = null;
        if (!(orig instanceof Map)) {
            val = BeanUtils.readPropertyOrField(orig, propertyName);
        } else {
            val = ((Map) orig).get(propertyName);
        }
        //
        if (!(dest instanceof Map)) {
            BeanUtils.writePropertyOrField(dest, propertyName, val);
        } else {
            ((Map) dest).put(propertyName, val);
        }
    }
}
