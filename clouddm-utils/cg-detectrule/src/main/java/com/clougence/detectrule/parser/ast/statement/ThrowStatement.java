package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.RuleStatement;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * throw statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class ThrowStatement extends AstFragment implements RuleStatement, VisitorTree {

    private Expression expr;

    public ThrowStatement(Expression expr){
        this.expr = Objects.requireNonNull(expr, "expr is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitThrowStatement(new VisitorContext<>(this, v -> {
                expr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("throw ");
        this.expr.doFormat(writer);
    }
}
