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
package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.token.ArgToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloatToken extends ArgToken {

    private Double floatValue;
    private String floatValueStr;

    FloatToken(long argIndex){
        super(argIndex);
    }

    FloatToken(Double value, String string){
        super(-1);
        this.floatValue = value;
        this.floatValueStr = string;
    }

    public static FloatToken ofArg(long index) {
        return new FloatToken(index);
    }

    public static FloatToken of(Double value, String string) {
        return new FloatToken(value, string);
    }

    @Override
    public Object getValue() { return this.floatValue; }
}
