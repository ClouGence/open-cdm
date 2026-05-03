package com.clougence.detectrule.parser.ast.primary;

import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;

import lombok.Getter;

/**
 * string value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public class StringValue extends StringToken implements Value {

    public StringValue(String wrap, String value){
        super(wrap, value);
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitStringValue(new VisitorContext<>(this));
        }
    }
}
