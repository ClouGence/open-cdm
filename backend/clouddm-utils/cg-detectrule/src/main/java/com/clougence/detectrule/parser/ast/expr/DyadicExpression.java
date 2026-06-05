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
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.primary.NumberValue;
import com.clougence.detectrule.parser.ast.token.SymbolSide;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.SymbolToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtOptions;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * dyadic operation expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class DyadicExpression extends AstFragment implements Expression {

    private Expression  firstExpr;
    private SymbolToken symbol;
    private Expression  secondExpr;

    public DyadicExpression(Expression firstExpr, SymbolToken symbol, Expression secondExpr){
        this.firstExpr = Objects.requireNonNull(firstExpr, "first expr is null.");
        this.symbol = Objects.requireNonNull(symbol, "symbol is null.");
        this.secondExpr = Objects.requireNonNull(secondExpr, "second expr is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitDyadicExpression(new VisitorContext<>(this, v -> {
                this.firstExpr.accept(v);
                this.secondExpr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.firstExpr instanceof UnaryExpression && this.secondExpr instanceof NumberValue && ((UnaryExpression) this.firstExpr).getSide() == SymbolSide.Right) {
            this.firstExpr.doFormat(writer);

            writer.stashOption();
            writer.updateOption(FmtOptions.WRAPPING_WRAP_SYMBOL, "Before");
            this.symbol.doFormat(writer);
            writer.unStashOption();

            this.secondExpr.doFormat(writer);
            return;
        }

        this.firstExpr.doFormat(writer);
        this.symbol.doFormat(writer);
        this.secondExpr.doFormat(writer);
    }
}
