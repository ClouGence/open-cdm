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
import java.lang.reflect.Modifier;
import java.util.List;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Func implements Accessible {

    private final Accessible parent;
    private String           name;
    private List<FuncArg>    funcArgs;
    private Type             funcReturn;

    private Method           localMethod;
    private boolean          staticMethod;

    Func(Accessible parent, String name, Method localMethod){
        this.parent = parent;
        this.name = name;
        this.localMethod = localMethod;
        this.staticMethod = Modifier.isStatic(localMethod.getModifiers());
    }

    @Override
    public TypeType getTypeType() { return TypeType.Func; }

    @Override
    public String toString() {
        return "Func{" + //
               "name='" + name + "', " +//
               "funcArgs=(" + StringUtils.join(funcArgs.toArray(), ", ") + "), " +//
               "funcReturn=" + this.funcReturn + ", " +//
               "typeType=" + getTypeType() + '}';
    }
}
