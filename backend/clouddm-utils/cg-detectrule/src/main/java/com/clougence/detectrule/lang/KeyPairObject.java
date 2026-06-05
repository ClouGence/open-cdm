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
package com.clougence.detectrule.lang;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.clougence.detectrule.lang.reflect.Accessible;
import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.lang.reflect.TypeType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyPairObject implements KeyPairAccess {

    private final Type                    pairType;
    private final Map<String, LangObject> data;

    public KeyPairObject(){
        this.pairType = null;
        this.data = new LinkedHashMap<>();
    }

    public KeyPairObject(Type elementType){
        this.pairType = Objects.requireNonNull(elementType, "pairType is null.");
        if (!this.pairType.getTypeType().isAtomic()) {
            throw new UnsupportedOperationException("pairType '" + elementType.getTypeType() + "' is Unsupported.");
        }
        this.data = new LinkedHashMap<>();
    }

    @Override
    public TypeType getType() { return TypeType.KeyPair; }

    @Override
    public Object unwrap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        this.data.forEach((key, val) -> result.put(key, val.unwrap()));
        return result;
    }

    @Override
    public Accessible getPairReflect(String key) {
        return this.pairType;
    }

    public LangObject getPair(String key) {
        return this.data.get(key);
    }

    @Override
    public boolean containPair(String key) {
        return this.data.containsKey(key);
    }

    @Override
    public String[] getKeySet() { return this.data.keySet().toArray(new String[0]); }

    @Override
    public void putKeyPair(String key, LangObject value) {
        if (value.getType() == TypeType.Null) {
            this.data.put(key, value);
            return;
        }

        if (this.pairType != null && value.getType() != this.pairType.getTypeType()) {
            throw new ClassCastException("the value type '" + value.getType() + "' can not be as '" + this.pairType.getTypeType() + "'");
        }

        this.data.put(key, value);
    }
}
