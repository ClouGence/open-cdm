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

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * if or switch else statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class SwitchElseStatement extends StatementList implements Fragment, VisitorTree {

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitSwitchElseStatement(new VisitorContext<>(this, v -> {
                super.accept(visitor);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        this.printKeywords("else", writer);
        writer.newLineAndIndents();

        writer.incrDepth();
        writer.fillSpace();
        super.doFormat(writer);
        writer.decrDepth();
    }

    private void printKeywords(String keywords, FmtWriter writer) throws IOException {
        if (writer.lastIsBlank() || writer.lastIsNewLine()) {
            writer.write(keywords);
        } else {
            writer.write(" ");
            writer.write(keywords);
        }
    }
}
