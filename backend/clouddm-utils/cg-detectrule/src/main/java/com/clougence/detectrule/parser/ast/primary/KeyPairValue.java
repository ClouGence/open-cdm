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

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.ast.expr.ParamExpression;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
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
public class KeyPairValue extends AstFragment implements Fragment, VisitorTree {

    private final StringToken key;
    private final Expression  value;

    public KeyPairValue(StringToken key, Expression value){
        this.key = key;
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitKeyPairValue(new VisitorContext<>(this, v -> {
                this.value.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        String newLineMode = writer.getStringOption(FmtOptions.WRAPPING_PAIR_ELEMENT_AS_NEW_LINE);
        String spaceMode = writer.getStringOption(FmtOptions.WRAPPING_PAIR_ELEMENT_AS_SPACE);

        this.key.doFormat(writer);
        if (StringUtils.equalsIgnoreCase(spaceMode, "No")) {
            writer.write(":");
        } else if (StringUtils.equalsIgnoreCase(spaceMode, "Before")) {
            writer.write(" :");
        } else if (StringUtils.equalsIgnoreCase(spaceMode, "Around")) {
            writer.write(" : ");
        } else if (StringUtils.equalsIgnoreCase(spaceMode, "After")) {
            writer.write(": ");
        } else {
            writer.write(": ");
        }
        this.value.doFormat(writer);

        // after element
        if (StringUtils.equalsIgnoreCase(newLineMode, "Any")) {
            writer.newLineAndIndents();
        } else if (StringUtils.equalsIgnoreCase(newLineMode, "Expression")) {
            if (isExpression(this.value)) {
                writer.newLineAndIndents();
            }
        }
    }

    private boolean isExpression(Expression expr) {
        if (expr instanceof Value || expr instanceof ParamExpression) {
            return false;
        } else {
            return true;
        }
    }
}
