package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.RuleStatement;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
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
public class AssignStatement extends AstFragment implements RuleStatement, VisitorTree {

    private StringToken varName;
    private Expression  varExpr;

    public AssignStatement(StringToken varName, Expression varExpr){
        this.varName = Objects.requireNonNull(varName, "varName is null.");
        this.varExpr = Objects.requireNonNull(varExpr, "varExpr is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitAssignStatement(new VisitorContext<>(this, v -> {
                varExpr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        this.varName.doFormat(writer);
        writer.write(" = ");
        this.varExpr.doFormat(writer);
    }
}
