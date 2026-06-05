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
package com.clougence.detectrule.parser.ast.token;

import java.math.RoundingMode;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.dslpaser.ast.token.StringToken;

import lombok.Getter;
import lombok.Setter;

/**
 * number format rounding
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class RoundingToken extends StringToken implements Fragment {

    private RoundingMode rounding;

    public RoundingToken(String symbol, RoundingMode rounding){
        super("", symbol);
        Objects.requireNonNull(symbol, "symbol is null.");
        this.rounding = Objects.requireNonNull(rounding, "rounding is null.");
    }
}
