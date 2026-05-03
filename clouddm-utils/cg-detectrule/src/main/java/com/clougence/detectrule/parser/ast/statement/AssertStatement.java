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
 * assert statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class AssertStatement extends AstFragment implements RuleStatement, VisitorTree {

    private Expression testExpr;
    private Expression dataExpr;

    public AssertStatement(Expression testExpr, Expression dataExpr){
        this.testExpr = Objects.requireNonNull(testExpr, "test expr is null.");
        this.dataExpr = dataExpr;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitAssertStatement(new VisitorContext<>(this, v -> {
                testExpr.accept(v);
                dataExpr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("assert ");
        this.testExpr.doFormat(writer);

        if (this.dataExpr != null) {
            writer.write(", ");
            this.dataExpr.doFormat(writer);
        }
    }
}
