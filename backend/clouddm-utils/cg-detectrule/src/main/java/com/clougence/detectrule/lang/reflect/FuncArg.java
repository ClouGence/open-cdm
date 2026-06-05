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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class FuncArg implements Accessible {

    private final Accessible parent;
    private final int        index;
    private final String     name;
    private final String     desc;
    private final Type       type;

    public FuncArg(Accessible parent, int index, String name, String desc, Type type){
        this.parent = parent;
        this.index = index;
        this.name = name;
        this.desc = desc;
        this.type = type;
    }

    @Override
    public TypeType getTypeType() { return TypeType.Arg; }

    @Override
    public String toString() {
        return "FuncArg{" + //
               "index='" + index + "', " +//
               "type=" + type + ", " +//
               "typeType=" + getTypeType() + '}';
    }
}
