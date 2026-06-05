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
import java.util.ArrayList;
import java.util.List;

import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtOptions;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * object value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class ObjectValue extends AstFragment implements Value {

    private final List<KeyPairValue> keyPairValues;

    public ObjectValue(){
        this.keyPairValues = new ArrayList<>();
    }

    public void addField(KeyPairValue pairValue) {
        this.keyPairValues.add(pairValue);
    }

    public void clear() {
        this.keyPairValues.clear();
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitObjectValue(new VisitorContext<>(this, v -> {
                for (KeyPairValue pairValue : this.keyPairValues) {
                    pairValue.accept(v);
                }
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        String newLineMode = writer.getStringOption(FmtOptions.WRAPPING_PAIR_ELEMENT_AS_NEW_LINE);

        writer.write("{");
        if (this.keyPairValues.isEmpty()) {
            if (writer.getBooleanOption(FmtOptions.NEW_LINE_EMPTY_OBJECT)) {
                writer.newLineAndIndents();
            } else if (writer.getBooleanOption(FmtOptions.SPACES_EMPTY_OBJECT)) {
                writer.write(" ");
            }
        } else {
            writer.incrDepth();
            for (int i = 0; i < this.keyPairValues.size(); i++) {
                KeyPairValue pairValue = this.keyPairValues.get(i);

                // before element
                if (i > 0) {
                    if (StringUtils.equalsIgnoreCase(newLineMode, "Pair")) {
                        writer.write(",");
                        writer.newLineAndIndents();
                    } else {
                        writer.write(", ");
                    }
                } else {
                    if (StringUtils.equalsIgnoreCase(newLineMode, "Pair")) {
                        writer.newLineAndIndents();
                    }
                }

                // do element
                pairValue.doFormat(writer);
            }
            writer.decrDepth();

            // after element
            if (StringUtils.equalsIgnoreCase(newLineMode, "Pair")) {
                writer.newLineAndIndents();
            }
        }
        writer.write("}");
    }
}
