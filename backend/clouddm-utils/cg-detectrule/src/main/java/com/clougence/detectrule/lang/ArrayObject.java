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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.lang.reflect.TypeType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrayObject implements CollectionAccess {

    private final Type             elementType;
    private final List<LangObject> data;

    public ArrayObject(){
        this.elementType = null;
        this.data = new ArrayList<>();
    }

    public ArrayObject(Type elementType){
        this.elementType = Objects.requireNonNull(elementType, "elementType is null.");
        if (!this.elementType.getTypeType().isAtomic()) {
            throw new UnsupportedOperationException("elementType '" + elementType.getTypeType() + "' is Unsupported.");
        }
        this.data = new ArrayList<>();
    }

    @Override
    public TypeType getType() { return TypeType.Collection; }

    @Override
    public List<Object> unwrap() {
        List<Object> result = new ArrayList<>(this.data.size());
        for (LangObject obj : this.data) {
            result.add(obj.unwrap());
        }
        return result;
    }

    @Override
    public Type getElementType() { return this.elementType; }

    @Override
    public void putElement(LangObject value) {
        if (value.getType() == TypeType.Null) {
            this.data.add(value);
            return;
        }

        if (this.elementType != null && value.getType() != this.elementType.getTypeType()) {
            throw new ClassCastException("the value type '" + value.getType() + "' can not be as '" + this.elementType.getTypeType() + "'");
        }

        this.data.add(value);
    }

    @Override
    public LangObject getElement(int index) {
        return this.data.get(index);
    }

    @Override
    public boolean contains(LangObject element) {
        if (!element.getType().isAtomic()) {
            return false;
        }

        boolean isNull = element.getType() == TypeType.Null;
        boolean isString = element.getType() == TypeType.String;
        Object testObj = element.unwrap();

        for (LangObject object : this.data) {
            if (testObj == null) {
                if (object.unwrap() == null) {
                    return true;
                } else {
                    continue;
                }
            }

            if (isNull && object.getType() == TypeType.Null) {
                return true;
            }

            if (!element.getType().isAtomic()) {
                continue;
            }

            if (testObj.equals(object.unwrap())) {
                return true;
            }

            if (this.elementType != null && this.elementType.isEnumString()) {
                if (testObj.toString().equals(object.unwrap().toString())) {
                    return true;
                }
            }

            if (isString && object.getType() == TypeType.String) {
                if (testObj.toString().equals(object.unwrap().toString())) {
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    public int getSize() { return this.data.size(); }

    @Override
    public Iterator<LangObject> toIterator() {
        return this.data.iterator();
    }
}
