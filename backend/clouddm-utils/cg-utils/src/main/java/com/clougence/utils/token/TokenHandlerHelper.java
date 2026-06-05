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
package com.clougence.utils.token;

import java.util.function.Function;

import lombok.Getter;

@Getter
public class TokenHandlerHelper implements TokenHandler {

    private final String                   openToken;
    private final String                   closeToken;
    private final Function<String, String> applyContext;

    public TokenHandlerHelper(String openToken, String closeToken, Function<String, String> applyContext){
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.applyContext = applyContext;
    }

    @Override
    public String handleToken(String var1) {
        return this.applyContext.apply(var1);
    }
}
