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
 * basic identifier expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public class IdentifyNameExpression extends AstFragment implements Expression {

    private final boolean usingQualifier;
    private final String  identify;

    public IdentifyNameExpression(boolean usingQualifier, String identify){
        this.usingQualifier = usingQualifier;
        this.identify = Objects.requireNonNull(identify, "identify is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitIdentifyNameExpression(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.usingQualifier) {
            writer.write("`");
            writer.write(this.identify);
            writer.write("`");
        } else {
            writer.write(this.identify);
        }
    }
}
