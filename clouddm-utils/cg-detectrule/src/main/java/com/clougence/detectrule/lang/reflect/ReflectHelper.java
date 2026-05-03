package com.clougence.detectrule.lang.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import com.clougence.detectrule.lang.CollectionAccess;
import com.clougence.detectrule.lang.EnumAccess;
import com.clougence.detectrule.lang.KeyPairAccess;
import com.clougence.utils.BeanUtils;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;

// domain 根元素不能是：annotation、interface、anonymousClass、array、enum、primitive、set
// domain 子元素不能是：annotation、interface、anonymousClass、set
// enum 类型可以通过 EnumOfCode 接口来增强
//
public final class ReflectHelper {

    private static Map<TypeType, Type> atomicTypeMap = new HashMap<>();
    private static Set<String>         ignoreFields  = new HashSet<>();

    public static void addIgnoreField(String fieldName) {
        ignoreFields.add(fieldName);
    }

    public static Type atomicType(TypeType typeType, Class<?> localType) {
        if (!typeType.isAtomic()) {
            return null;
        }
        return atomicTypeMap.computeIfAbsent(typeType, tt -> {
            switch (tt) {
                case Boolean:
                    return new Type(null, Boolean.TYPE, TypeType.Boolean);
                case Float:
                    return new Type(null, Double.TYPE, TypeType.Float);
                case Integer:
                    return new Type(null, Long.TYPE, TypeType.Integer);
                case Decimal:
                    return new Type(null, BigDecimal.class, TypeType.Decimal);
                case String:
                    return new Type(null, String.class, TypeType.String);
                case Date:
                    if (localType == null) {
                        return new Type(null, LocalDate.class, TypeType.Date);
                    } else {
                        return new Type(null, localType, TypeType.Date);
                    }
                case Time:
                    if (localType == null) {
                        return new Type(null, LocalTime.class, TypeType.Time);
                    } else {
                        return new Type(null, localType, TypeType.Time);
                    }
                case Datetime:
                    if (localType == null) {
                        return new Type(null, LocalDateTime.class, TypeType.Datetime);
                    } else {
                        return new Type(null, localType, TypeType.Datetime);
                    }
                case Null:
                    return new Type(null, Void.class, TypeType.Null);
                default:
                    return null;
            }
        });
    }

    public static Type arrayType(Class<?> aClass, Class<?> elementType) {
        Type type = new Type(null, aClass, TypeType.Collection);
        type.setArray(true);
        if (elementType != null) {
            type.setComponentType(recursionResolve(null, elementType, null));
        } else {
            type.setComponentType(recursionResolve(null, Object.class, null));
        }
        return type;
    }

    public static Type keyPairType(Class<?> aClass, Class<?> pairType) {
        Type type = new Type(null, aClass, TypeType.KeyPair);
        type.setKeyPair(true);
        if (pairType != null) {
            type.setComponentType(recursionResolve(null, pairType, null));
        } else {
            type.setComponentType(recursionResolve(null, Object.class, null));
        }
        return type;
    }

    private static boolean allowedInterface(Class<?> javaType) {
        if (javaType.isInterface()) {
            if (List.class.isAssignableFrom(javaType)) {
                return true;
            } else if (Map.class.isAssignableFrom(javaType)) {
                return true;
            } else if (CollectionAccess.class.isAssignableFrom(javaType)) {
                return true;
            } else if (KeyPairAccess.class.isAssignableFrom(javaType)) {
                return true;
            } else if (EnumAccess.class.isAssignableFrom(javaType)) {
                return true;
            }
        }
        return false;
    }

    public static Type resolveDomain(Class<?> domainType) {
        TypeType typeType = TypeType.valueOfType(domainType);
        if (domainType.isAnnotation() || domainType.isArray() || domainType.isEnum() || (domainType.isInterface() && !allowedInterface(domainType)) || domainType.isPrimitive()
            || domainType.isAnonymousClass() || Set.class.isAssignableFrom(domainType)) {
            throw new IllegalArgumentException("domainType can not be [annotation, array, enum, interface, primitive, string, anonymous, set]");
        }

        if (typeType.isSelfType()) {
            throw new IllegalArgumentException("domainType " + domainType + " is declared as Atomic.");
        }

        return recursionResolve(null, domainType, null);
    }

    private static Type recursionResolve(Accessible parent, Class<?> javaType, java.lang.reflect.Field onJavaField) {
        if (javaType.isAnnotation() || (javaType.isInterface() && !allowedInterface(javaType)) || javaType.isAnonymousClass() || Set.class.isAssignableFrom(javaType)) {
            return null;
        }

        // special for Array
        if (javaType.isArray()) {
            Type type = new Type(parent, javaType, TypeType.Collection);
            type.setArray(true);
            type.setComponentType(recursionResolve(type, javaType.getComponentType(), null));
            return type;
        }

        // special for List
        if (List.class.isAssignableFrom(javaType)) {
            Type type = new Type(parent, javaType, TypeType.Collection);
            type.setArray(true);
            if (onJavaField != null) {
                Class<?> elementType = ClassUtils.getSuperClassGenricType(onJavaField, 0);
                type.setComponentType(recursionResolve(type, elementType, null));
            } else {
                type.setComponentType(recursionResolve(type, Object.class, null));
            }
            return type;
        }

        // special for Map
        if (Map.class.isAssignableFrom(javaType)) {
            Type type = new Type(parent, javaType, TypeType.KeyPair);
            type.setKeyPair(true);
            if (onJavaField != null) {
                Class<?> elementType = ClassUtils.getSuperClassGenricType(onJavaField, 1);
                type.setComponentType(recursionResolve(type, elementType, null));
            } else {
                type.setComponentType(recursionResolve(type, Object.class, null));
            }
            return type;
        }

        // special for Enum
        if (javaType.isEnum()) {
            Type type = new Type(parent, javaType, TypeType.String);
            type.setEnumString(true);
            type.setEnumOfCode(new InnerEnumOfCode<>(javaType));
            return type;
        }

        TypeType typeType = TypeType.valueOfType(javaType);
        Type domainType = new Type(parent, javaType, typeType);
        if (typeType.isSelfType()) {
            return domainType;
        }

        // fields
        if (!javaType.isAnnotationPresent(RuleIgnore.class)) {
            for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(javaType)) {
                Field field = resolveField(domainType, property);
                if (field != null) {
                    domainType.addField(field.getName(), field);
                }
            }
        }

        // function
        for (Method method : javaType.getMethods()) {
            if (!method.isAnnotationPresent(RuleFunction.class) || method.isAnnotationPresent(RuleIgnore.class)) {
                continue;
            }

            Func func = resolveMethod(domainType, method);
            if (func != null) {
                domainType.addMethod(func.getName(), func);
            }
        }

        return domainType;
    }

    static Field resolveField(Type domainType, PropertyDescriptor property) {
        Class<?> domainJavaType = domainType.getLocalType();
        java.lang.reflect.Field javaField = BeanUtils.findALLFields(domainJavaType).stream().filter(field -> {
            return field.getName().equals(property.getName());
        }).findFirst().orElse(null);

        if (javaField != null && javaField.isAnnotationPresent(RuleIgnore.class)) {
            return null;
        }

        Class<?> propertyType = property.getPropertyType();
        Type fieldType = recursionResolve(domainType, propertyType, javaField);
        if (fieldType == null) {
            return null;
        }

        AccessMode accessMode;
        Method writeMethod = property.getWriteMethod();
        Method readMethod = property.getReadMethod();
        if (writeMethod.isAnnotationPresent(RuleIgnore.class)) {
            writeMethod = null;
        }
        if (readMethod.isAnnotationPresent(RuleIgnore.class)) {
            readMethod = null;
        }

        if (writeMethod == null && readMethod == null) {
            return null;
        } else if (writeMethod != null && readMethod != null) {
            accessMode = AccessMode.ReadWrite;
        } else if (writeMethod == null && readMethod != null) {
            accessMode = AccessMode.ReadOnly;
        } else if (writeMethod != null && readMethod == null) {
            accessMode = AccessMode.WriteOnly;
        } else {
            return null;
        }

        return new Field(domainType, property.getName(), fieldType, accessMode, writeMethod, readMethod);
    }

    private static Func resolveMethod(Type domainType, Method function) {
        RuleFunction ruleFunction = function.getAnnotation(RuleFunction.class);
        String funcName = StringUtils.isBlank(ruleFunction.value()) ? function.getName() : ruleFunction.value();
        Func func = new Func(domainType, funcName, function);

        Parameter[] paramTypes = function.getParameters();
        FuncArg[] funcArgs = new FuncArg[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Parameter param = paramTypes[i];

            String argName = param.getName();
            String argDesc = null;//param.getName();
            Type argType = recursionResolve(func, param.getType(), null);
            funcArgs[i] = new FuncArg(func, i, argName, argDesc, argType);
        }

        Class<?> returnType = function.getReturnType();
        Type funcReturn = recursionResolve(func, returnType, null);

        func.setFuncArgs(Arrays.asList(funcArgs));
        func.setFuncReturn(funcReturn);
        return func;
    }
}
