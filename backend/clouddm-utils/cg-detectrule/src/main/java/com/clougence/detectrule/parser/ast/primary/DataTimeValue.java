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
package com.clougence.detectrule.parser.ast.primary;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.ast.token.CastTypeDtFmtToken;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * dataTime value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class DataTimeValue extends AstFragment implements Value {

    private StringToken        value;
    private CastTypeDtFmtToken format;

    public DataTimeValue(StringToken value, CastTypeDtFmtToken format){
        this.value = Objects.requireNonNull(value, "value is null.");
        this.format = format;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitDataTimeValue(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.format == null) {
            writer.write("T");
            this.value.doFormat(writer);
        } else {
            writer.write("T(");
            this.format.getFormat().doFormat(writer);
            writer.write(")");
            this.value.doFormat(writer);
        }
    }
}
