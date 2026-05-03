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
 * boosts expression evaluation priority
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class PrivilegeExpression extends AstFragment implements Expression {

    private Expression expr;

    public PrivilegeExpression(Expression expr){
        this.expr = Objects.requireNonNull(expr, "expr is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitPrivilegeExpression(new VisitorContext<>(this, v -> {
                this.expr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        //        if (this.expr instanceof PrivilegeExpression) {
        //            this.expr.doFormat(depth, formatOption, writer); // clear code
        //        } else {
        writer.write("(");
        this.expr.doFormat(writer);
        writer.write(")");
        //        }
    }
}
