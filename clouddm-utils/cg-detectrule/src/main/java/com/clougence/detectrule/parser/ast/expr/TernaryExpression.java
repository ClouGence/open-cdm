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
 * ternary operation expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class TernaryExpression extends AstFragment implements Expression {

    private Expression testExpr;
    private Expression thenExpr;
    private Expression elseExpr;

    public TernaryExpression(Expression testExp, Expression thenExp, Expression elseExp){
        this.testExpr = Objects.requireNonNull(testExp, "test expr is null.");
        this.thenExpr = Objects.requireNonNull(thenExp, "then expr is null.");
        this.elseExpr = Objects.requireNonNull(elseExp, "else expr is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitTernaryExpression(new VisitorContext<>(this, v -> {
                this.testExpr.accept(v);
                this.thenExpr.accept(v);
                this.elseExpr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        this.testExpr.doFormat(writer);
        writer.write(" ? ");
        this.thenExpr.doFormat(writer);
        writer.write(" : ");
        this.elseExpr.doFormat(writer);
    }
}
