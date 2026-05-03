package com.clougence.detectrule.parser.ast.expr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class FunctionParameters extends AstFragment implements Expression {

    private List<Expression> args;

    public FunctionParameters(){
        this.args = new ArrayList<>();
    }

    public void addArg(Expression expr) {
        this.args.add(Objects.requireNonNull(expr, "expr is null."));
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitFunctionParameters(new VisitorContext<>(this, v -> {
                for (Expression expr : this.args) {
                    expr.accept(v);
                }
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("(");
        for (int i = 0; i < this.args.size(); i++) {
            Expression argExpr = this.args.get(i);

            if (i > 0) {
                writer.write(", ");
            }

            argExpr.doFormat(writer);
        }
        writer.write(")");
    }
}
