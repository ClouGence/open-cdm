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
package com.clougence.utils.ref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.utils.StringUtils;

/**
 * Tuple
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2016-07-17
 */
public class Tuple {

    private final List<Object> values;

    public Tuple(Object... values){
        this.values = new ArrayList<>(values.length);
        Collections.addAll(this.values, values);
    }

    public static Tuple of(Object... values) {
        return new Tuple(values);
    }

    public <T> T get0() {
        return this.getByIndex(0);
    }

    public <T> T get1() {
        return this.getByIndex(1);
    }

    public <T> T get2() {
        return this.getByIndex(2);
    }

    public <T> T get3() {
        return this.getByIndex(3);
    }

    public <T> T get4() {
        return this.getByIndex(4);
    }

    public <T> T get5() {
        return this.getByIndex(5);
    }

    public <T> T get6() {
        return this.getByIndex(6);
    }

    public <T> T get7() {
        return this.getByIndex(7);
    }

    public <T> T get8() {
        return this.getByIndex(8);
    }

    public <T> T get9() {
        return this.getByIndex(9);
    }

    private <T> T getByIndex(int index) {
        if (this.values.size() > index) {
            return (T) this.values.get(index);
        } else {
            return null;
        }
    }

    public <T> List<T> toList() {
        return (List<T>) this.values;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tuple)) {
            return false;
        }
        Tuple other = (Tuple) obj;
        return Arrays.deepEquals(this.values.toArray(), other.values.toArray());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.values.toArray());
    }

    @Override
    public String toString() {
        return "Tuple[" + StringUtils.join(this.values, ", ") + "]";
    }
}
