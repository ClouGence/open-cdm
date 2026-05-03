package com.clougence.detectrule.parser.ast.token;

import java.io.IOException;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.dslpaser.ast.token.SymbolToken;
import com.clougence.dslpaser.foramt.FmtOptions;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * double symbol
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class DoubleSymbolToken extends SymbolToken implements Fragment {

    private SymbolSide side;
    private String     lesser;

    public DoubleSymbolToken(SymbolToken primary, String lesser, SymbolSide lesserSide){
        super(primary.getValue());
        this.lesser = lesser;
        this.side = lesserSide;
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        String firstSymbol;
        String secondSymbol;
        if (this.side == SymbolSide.Left) {
            firstSymbol = this.lesser;
            secondSymbol = this.getValue();
        } else {
            firstSymbol = this.getValue();
            secondSymbol = this.lesser;
        }

        this.writeSymbol(writer, firstSymbol, secondSymbol);
    }

    private void writeSymbol(FmtWriter writer, String first, String second) throws IOException {
        String mode = writer.getStringOption(FmtOptions.WRAPPING_WRAP_SYMBOL);

        if (StringUtils.equalsIgnoreCase(mode, "No")) {
            writer.writeSymbol(first);
            writer.write(" ");
            writer.writeSymbol(second);
            return;
        }

        if (StringUtils.equalsIgnoreCase(mode, "Before") || StringUtils.equalsIgnoreCase(mode, "After")) {
            writer.writeSymbol(first);
            writer.writeSymbol(second);
        }

        if (StringUtils.equalsIgnoreCase(mode, "Around")) {
            writer.write(" ");
            writer.write(first);
            writer.write(" ");
            writer.write(second);
            writer.write(" ");
        }
    }
}
