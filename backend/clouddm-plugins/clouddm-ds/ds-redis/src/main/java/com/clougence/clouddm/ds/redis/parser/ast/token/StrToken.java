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
public class StrToken extends ArgToken {

    private String  value;
    private boolean quotes;

    private StrToken(long argIndex){
        super(argIndex);
    }

    private StrToken(String value, boolean quotes){
        super(-1);
        this.value = value;
        this.quotes = quotes;
    }

    public static StrToken ofArg(long index) {
        return new StrToken(index);
    }

    public static StrToken of(String value, boolean quotes) {
        return new StrToken(value, quotes);
    }
}
