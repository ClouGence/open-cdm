package com.clougence.detectrule.parser.ast.expr;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

/**
 * basic identifier expression has subscript
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public class IdentifySubExpression extends AstFragment implements Expression {

    private final Expression expression;

    public IdentifySubExpression(Expression expression){
        this.expression = Objects.requireNonNull(expression, "expression is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitIdentifySubExpression(new VisitorContext<>(this, v -> {
                this.expression.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("[");
        this.expression.doFormat(writer);
        writer.write("]");
    }
}
