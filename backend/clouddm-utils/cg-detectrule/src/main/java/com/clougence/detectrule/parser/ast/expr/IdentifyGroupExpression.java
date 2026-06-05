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
package com.clougence.detectrule.parser.ast.expr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

/**
 * multiple basic identifier expressions group consisting expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public class IdentifyGroupExpression extends AstFragment implements Expression {

    private final List<Expression> continuous = new ArrayList<>();

    public void appendExpr(Expression item) {
        this.continuous.add(item);
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitIdentifyGroupExpression(new VisitorContext<>(this, v -> {
                for (Expression expr : this.continuous) {
                    expr.accept(v);
                }
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.continuous.isEmpty()) {
            throw new IllegalStateException("identify is empty.");
        }

        for (int i = 0; i < this.continuous.size(); i++) {
            Expression identify = this.continuous.get(i);
            boolean excludeDot = identify instanceof IdentifySubExpression;
            if (i > 0 && !excludeDot) {
                writer.write(".");
            }
            identify.doFormat(writer);
        }
    }
}
