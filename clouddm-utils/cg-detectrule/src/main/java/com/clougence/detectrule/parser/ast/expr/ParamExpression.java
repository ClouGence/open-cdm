package com.clougence.detectrule.parser.ast.expr;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * reading environment parameters
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class ParamExpression extends AstFragment implements Expression {

    private StringToken parameter;

    public ParamExpression(StringToken parameter){
        this.parameter = Objects.requireNonNull(parameter, "parameter is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitParamExpression(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("#{");
        this.parameter.doFormat(writer);
        writer.write("}");
    }
}
