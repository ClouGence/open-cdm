package com.clougence.detectrule.parser.ast.primary;

import java.io.IOException;

import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * float,integer,decimal value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class NumberValue extends StringToken implements Value {

    private boolean    symbol;
    private boolean    negative;
    private NumberType type;

    public NumberValue(NumberType type, String value){
        super("", value);
        this.type = type;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitNumberValue(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.symbol) {
            if (this.negative) {
                writer.write("-");
            } else {
                writer.write("+");
            }
        }
        super.doFormat(writer);
    }
}
