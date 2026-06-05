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

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.foramt.FmtOptions;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * number format
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class CastTypeNumFmtToken extends AstFragment implements Fragment {

    private StringToken   digit;
    private RoundingToken rounding;

    public CastTypeNumFmtToken(StringToken digit, RoundingToken rounding){
        this.digit = Objects.requireNonNull(digit, "digit is null.");
        this.rounding = rounding;
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("(");
        writer.write(this.digit.getValue());
        if (this.rounding != null) {
            if (writer.getBooleanOption(FmtOptions.SPACES_BEFORE_KEYWORDS_TYPE_CAST)) {
                writer.write(", ");
            } else {
                writer.write(",");
            }

            this.rounding.doFormat(writer);
        }
        writer.write(")");
    }
}
