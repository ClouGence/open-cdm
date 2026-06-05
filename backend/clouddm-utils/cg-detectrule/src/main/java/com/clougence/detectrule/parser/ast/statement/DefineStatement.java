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
package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.RuleStatement;
import com.clougence.detectrule.parser.format.DetectRuleFmtOptions;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * define statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class DefineStatement extends AstFragment implements RuleStatement, VisitorTree {

    private StringToken       name;
    private StringToken       type;
    private StringToken       defaultValue;
    private List<StringToken> enums;
    private StringToken       hint;

    public DefineStatement(StringToken name){
        this.name = Objects.requireNonNull(name, "name is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitDefineStatement(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("#define ");
        this.name.doFormat(writer);

        if (this.type != null) {
            if (!writer.lastIsBlank()) {
                writer.write(" ");
            }
            writer.write("as ");
            this.type.doFormat(writer);
        }

        if (this.defaultValue != null) {
            if (!writer.lastIsBlank()) {
                writer.write(" ");
            }
            writer.write("default ");
            this.defaultValue.doFormat(writer);
        }

        if (this.enums != null) {
            if (!writer.lastIsBlank()) {
                writer.write(" ");
            }
            writer.write("enum ");
            this.printEnum(writer, this.enums);
        }

        if (this.hint != null) {
            if (!writer.lastIsBlank()) {
                writer.write(" ");
            }
            writer.write("hint ");
            this.hint.doFormat(writer);
        }
    }

    private void printEnum(FmtWriter writer, List<StringToken> enums) throws IOException {
        String newLineMode = writer.getStringOption(DetectRuleFmtOptions.WRAPPING_DEFINE_ENUMS_ELEMENT_AS_NEW_LINE);

        writer.write("[");
        if (enums.isEmpty()) {
            if (writer.getBooleanOption(DetectRuleFmtOptions.NEW_LINE_DEFINE_ENUMS_ELEMENT_EMPTY)) {
                writer.newLineAndIndents();
            } else if (writer.getBooleanOption(DetectRuleFmtOptions.SPACES_DEFINE_ENUMS_ELEMENT_EMPTY)) {
                writer.write(" ");
            }
        } else {
            writer.incrDepth();
            for (int i = 0; i < this.enums.size(); i++) {
                StringToken token = this.enums.get(i);

                // before element
                if (i > 0) {
                    if (StringUtils.equalsIgnoreCase(newLineMode, "Inline")) {
                        writer.write(", ");
                    } else {
                        writer.write(",");
                        writer.newLineAndIndents();
                    }
                } else {
                    if (!StringUtils.equalsIgnoreCase(newLineMode, "Inline")) {
                        writer.newLineAndIndents();
                    }
                }

                // do element
                token.doFormat(writer);
            }
            writer.decrDepth();

            // after element
            if (!StringUtils.equalsIgnoreCase(newLineMode, "Inline")) {
                writer.newLineAndIndents();
            }
        }
        writer.write("]");
    }
}
