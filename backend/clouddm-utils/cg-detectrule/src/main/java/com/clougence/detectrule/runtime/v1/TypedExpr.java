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
package com.clougence.detectrule.runtime.v1;

import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.reflect.Accessible;
import com.clougence.dslpaser.ast.location.Location;

import lombok.Getter;

@Getter
class TypedExpr {

    private final Location   location;
    private final LangObject data;
    private final Accessible type;

    public TypedExpr(Location location, LangObject data, Accessible type){
        this.location = location;
        this.data = data;
        this.type = type;
    }
}
