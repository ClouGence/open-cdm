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
 * boolean value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
public class BooleanValue extends StringToken implements Value {

    public BooleanValue(boolean value){
        this(value ? "true" : "false");
    }

    public BooleanValue(String value){
        super("", value);
        if (!(StringUtils.equalsIgnoreCase(value, "true") || StringUtils.equalsIgnoreCase(value, "false"))) {
            throw new IllegalStateException("must be 'true' or 'false'");
        }
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitBooleanValue(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write(this.getValue());
    }
}
