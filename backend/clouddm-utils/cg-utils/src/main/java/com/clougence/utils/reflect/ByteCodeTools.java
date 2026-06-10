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
package com.clougence.utils.reflect;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.*;
import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * The tool class that you use to generate bytes.
 * @author 赵永春 (zyc@hasor.net)
 * @version 2009-10-16
 */
public class ByteCodeTools implements Opcodes {

    /** Convert a particular type to an asm, int to I, String to Ljava/lang/ String */
    public static String toAsmType(final Class<?> classType) {
        if (classType == int.class) {
            return "I";
        } else if (classType == byte.class) {
            return "B";
        } else if (classType == char.class) {
            return "C";
        } else if (classType == double.class) {
            return "D";
        } else if (classType == float.class) {
            return "F";
        } else if (classType == long.class) {
            return "J";
        } else if (classType == short.class) {
            return "S";
        } else if (classType == boolean.class) {
            return "Z";
        } else if (classType == void.class) {
            return "V";
        } else if (classType.isArray()) {
            return "[" + toAsmType(classType.getComponentType());
        } else {
            return "L" + Type.getInternalName(classType) + ";";
        }
    }

    /** Converting a particular type to an asm presentation, int, int to II, String, int to Ljava/lang/String; I */
    public static String toAsmType(final Class<?>[] classType) {
        String returnString = "";
        for (Class<?> c : classType) {
            returnString += toAsmType(c);
        }
        return returnString;
    }

    public static String toJavaType(final String asmType) {
        if (asmType.charAt(0) == 'L') {
            return asmType.substring(1, asmType.length() - 1).replace("/", ".");
        } else {
            return asmType.replace("/", ".");
        }
    }

    /** Convert a method to its ASM signature. */
    public static String toAsmSignature(Method targetMethod) {
        class MoreType {

            String          name       = null;
            Class<?>        paramClass = null;
            TypeVariable<?> paramType  = null;
        }
        //
        StringBuffer signature = new StringBuffer();
        {
            //Step:1
            Class<?>[] pTypeArray = targetMethod.getParameterTypes();
            MoreType[] mTypeList = new MoreType[pTypeArray.length];
            for (int i = 0; i < pTypeArray.length; i++) {
                Class<?> pType = pTypeArray[i];
                MoreType mtype = new MoreType();
                mtype.paramClass = pType;
                mTypeList[i] = mtype;
            }
            //Step:2
            java.lang.reflect.Type[] gTypeArray = targetMethod.getGenericParameterTypes();
            for (int i = 0; i < gTypeArray.length; i++) {
                java.lang.reflect.Type gType = gTypeArray[i];
                if (gType instanceof TypeVariable) {
                    mTypeList[i].name = ((TypeVariable<?>) gType).getName();
                    mTypeList[i].paramType = (TypeVariable<?>) gType;
                }
            }
            //Step:3
            java.lang.reflect.Type[] tTypeArray = targetMethod.getTypeParameters();
            for (int i = 0; i < tTypeArray.length; i++) {
                if (i == 0) {
                    signature.append("<");
                }
                //
                TypeVariable<?> tType = (TypeVariable<?>) tTypeArray[i];
                String tName = tType.getName();
                for (int j = 0; j < mTypeList.length; j++) {
                    if (tName.equals(mTypeList[j].name)) {
                        signature.append(tName);
                        if (mTypeList[j].paramClass.isInterface()) {
                            signature.append("::");
                        } else {
                            signature.append(":");
                        }
                        //
                        for (java.lang.reflect.Type atType : mTypeList[j].paramType.getBounds()) {
                            getTypeVarStr(signature, atType);
                        }
                    }
                }
                //
                if (i == tTypeArray.length - 1) {
                    signature.append(">");
                }
            }
            //Step:4
            signature.append("(");
            for (int i = 0; i < mTypeList.length; i++) {
                MoreType mType = mTypeList[i];
                if (mType.name != null) {
                    signature.append(String.format("T%s;", mType.name));
                } else {
                    signature.append(toAsmType(mType.paramClass));
                }
            }
            signature.append(")");
            //Step:5
            signature.append(toAsmType(targetMethod.getReturnType()));
        }
        //
        if (signature.length() == 0) {
            return null;
        }
        return signature.toString();
    }

    private static StringBuffer getTypeVarStr(StringBuffer atString, java.lang.reflect.Type type) {
        //
        if (type instanceof TypeVariable) {
            TypeVariable<?> paramType = (TypeVariable<?>) type;
            atString.append("T" + paramType.getName() + ";");
        } else if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            atString.append("L" + replaceClassName((Class<?>) paramType.getRawType()));
            atString.append("<");
            for (java.lang.reflect.Type atType : paramType.getActualTypeArguments()) {
                getTypeVarStr(atString, atType);
            }
            atString.append(">");
            atString.append(";");
        } else if (type instanceof Class<?>) {
            Class<?> paramType = (Class<?>) type;
            atString.append(toAsmType(paramType));
        } else if (type instanceof WildcardType) {
            WildcardType paramType = (WildcardType) type;
            java.lang.reflect.Type[] upperType = paramType.getUpperBounds();//Upper Border
            java.lang.reflect.Type[] lowerType = paramType.getLowerBounds();//Lower Border
            if (lowerType.length == 0 && upperType.length != 0) {
                atString.append("+");
                for (java.lang.reflect.Type atType : upperType) {
                    getTypeVarStr(atString, atType);
                }
            } else if (lowerType.length != 0 && upperType.length != 0) {
                atString.append("-");
                for (java.lang.reflect.Type atType : lowerType) {
                    getTypeVarStr(atString, atType);
                }
            } else {
                throw new RuntimeException("Generic format error.");
            }
        } else if (type instanceof GenericArrayType) {
            //System.out.println();
        }
        //
        return atString;
    }

    /** Get method ASM description information */
    public static String toAsmFullDesc(Method method) {
        StringBuffer str = new StringBuffer();
        str.append(method.getName());
        str.append("(");
        str.append(toAsmType(method.getParameterTypes()));
        str.append(")");
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class) {
            str.append("V");
        } else {
            str.append(toAsmType(returnType));
        }
        return str.toString();
    }

    /** Get method ASM description information */
    public static String toAsmDesc(Method method) {
        StringBuffer str = new StringBuffer();
        str.append("(");
        str.append(toAsmType(method.getParameterTypes()));
        str.append(")");
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class) {
            str.append("V");
        } else {
            str.append(toAsmType(returnType));
        }
        return str.toString();
    }

    /**
     * Disaggregation of IIIILjava/lang/Integer; ASM-type presentation in F.
     * Test string IIIILjava/lang/Integer; F[[[ILjava/lang.Boolean; ->]
     * ["I", "I", "I", "I", "Ljava/lang/Integer;", "F", "[[[I", "Ljava/lang.Boolean"]
     */
    public static String[] splitAsmType(final String asmTypes) {
        class AsmTypeRead {

            StringReader sread = null;

            public AsmTypeRead(final String sr){
                this.sread = new StringReader(sr);
            }

            /** Reads until the next semicolon or end. */
            private String readToSemicolon() throws IOException {
                String res = "";
                while (true) {
                    int strInt = this.sread.read();
                    if (strInt == -1) {
                        return res;
                    } else if ((char) strInt == ';') {
                        return res + ';';
                    } else {
                        res += (char) strInt;
                    }
                }
            }

            /** Read a type */
            private String readType() throws IOException {
                int strInt = this.sread.read();
                if (strInt == -1) {
                    return "";
                }
                switch ((char) strInt) {
                    case '['://array
                        return '[' + this.readType();
                    case 'L'://Object
                        return 'L' + this.readToSemicolon();
                    default:
                        return String.valueOf((char) strInt);
                }
            }

            /** Read All Types */
            public String[] readTypes() throws IOException {
                ArrayList<String> ss = new ArrayList<String>(0);
                while (true) {
                    String s = this.readType();
                    if ("".equals(s)) {
                        break;
                    } else {
                        ss.add(s);
                    }
                }
                String[] res = new String[ss.size()];
                ss.toArray(res);
                return res;
            }
        }
        try {
            return new AsmTypeRead(asmTypes).readTypes();//     IIIILjava/lang/Integer;F[[[Ljava/util/Date;
        } catch (IOException e) {
            throw new RuntimeException("Invalid ASM type desc.");
        }
    }

    /** Convert class names to asm class names */
    public static String replaceClassName(final Class<?> targetClass) {
        return targetClass.getName().replace(".", "/");
    }

    public static String replaceClassName(final String targetClass) {
        return targetClass.replace(".", "/");
    }

    public static String[] replaceClassName(Class<?>[] exceptionTypes) {
        String[] typeStr = new String[exceptionTypes.length];
        for (int i = 0; i < exceptionTypes.length; i++) {
            typeStr[i] = replaceClassName(exceptionTypes[i]);
        }
        return typeStr;
    }

    /** Convert a Ljava/lang/Object; in form to java/lang/Object */
    public static String asmTypeToType(final String asmType) {
        if (asmType.charAt(0) == 'L') {
            return asmType.substring(1, asmType.length() - 1);
        } else {
            return asmType;
        }
    }
}
