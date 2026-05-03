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
 * type conversion expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class CastExpression extends AstFragment implements Expression {

    private Expression           expr;
    private CastExpressionAsType castType;

    public CastExpression(Expression expr, CastExpressionAsType castType){
        this.expr = Objects.requireNonNull(expr, "cast expr is null.");
        this.castType = Objects.requireNonNull(castType, "cast as type is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitCastExpression(new VisitorContext<>(this, v -> {
                expr.accept(v);
                castType.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("cast(");
        this.expr.doFormat(writer);
        writer.write(" as ");
        this.castType.doFormat(writer);
        writer.write(")");
    }
}
