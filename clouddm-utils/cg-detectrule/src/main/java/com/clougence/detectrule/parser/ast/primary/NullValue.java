package com.clougence.detectrule.parser.ast.primary;

import java.io.IOException;

import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * null value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public class NullValue extends StringToken implements Value {

    public NullValue(){
        this("null");
    }

    public NullValue(String value){
        super("", value);
        if (!StringUtils.equalsIgnoreCase(value, "null")) {
            throw new IllegalStateException("must be 'null'");
        }
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitNullValue(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        super.doFormat(writer);
    }
}
