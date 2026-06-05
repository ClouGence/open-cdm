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

import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.detectrule.lang.EnumAccess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Type implements Accessible {

    private final Accessible         parent;
    private final TypeType           typeType;
    private final Class<?>           localType;

    private final Map<String, Field> fields;
    private final Map<String, Func>  methods;

    // -- for array or list
    private boolean                  array;
    private boolean                  keyPair;
    private Type                     componentType;

    // -- for enum
    private boolean                  enumString;
    private EnumAccess<?>            enumOfCode;

    Type(Accessible parent, Class<?> localType, TypeType typeType){
        this.parent = parent;
        this.localType = localType;
        this.typeType = typeType;
        this.fields = new LinkedHashMap<>();
        this.methods = new LinkedHashMap<>();
    }

    void addField(String name, Field field) {
        this.fields.put(name, field);
    }

    void addMethod(String name, Func func) {
        this.methods.put(name, func);
    }

    public Field getField(String name) {
        return this.fields.get(name);
    }

    public Func getMethod(String name) {
        return this.methods.get(name);
    }

    @Override
    public String getName() { return this.localType.getName(); }

    @Override
    public String toString() {
        return "Type{" + localType + '}';
    }

}
