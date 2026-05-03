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
import lombok.Setter;

/**
 * function expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class FunctionExpression extends AstFragment implements Expression {

    private DetectExpression   identify;
    private FunctionParameters parameters;

    public FunctionExpression(DetectExpression identify, FunctionParameters parameters){
        this.identify = Objects.requireNonNull(identify, "identify is null.");
        this.parameters = Objects.requireNonNull(parameters, "parameters is null.");;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitFunctionExpression(new VisitorContext<>(this, v -> {
                this.identify.accept(v);
                this.parameters.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        this.identify.doFormat(writer);
        this.parameters.doFormat(writer);
    }
}
