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
package com.clougence.detectrule.lang.reflect;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Field implements Accessible {

    private final Accessible parent;
    private final String     name;
    private final Type       type;
    private final TypeType   typeType;

    private final AccessMode accessMode;
    private final Method     readMethod;
    private final Method     writeMethod;

    Field(Accessible parent, String name, Type type, AccessMode accessMode, Method writeMethod, Method readMethod){
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.typeType = TypeType.Field;

        this.accessMode = accessMode;
        this.writeMethod = writeMethod;
        this.readMethod = readMethod;
    }

    @Override
    public String toString() {
        return "Field{" + //
               "name='" + name + "', " +//
               "type=" + type + ", " +//
               "typeType=" + typeType + ", " +//
               "accessMode=" + accessMode + '}';
    }
}
