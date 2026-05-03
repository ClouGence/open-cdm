package com.clougence.detectrule.parser.ast.token;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.foramt.FmtOptions;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * number format
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class CastTypeNumFmtToken extends AstFragment implements Fragment {

    private StringToken   digit;
    private RoundingToken rounding;

    public CastTypeNumFmtToken(StringToken digit, RoundingToken rounding){
        this.digit = Objects.requireNonNull(digit, "digit is null.");
        this.rounding = rounding;
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("(");
        writer.write(this.digit.getValue());
        if (this.rounding != null) {
            if (writer.getBooleanOption(FmtOptions.SPACES_BEFORE_KEYWORDS_TYPE_CAST)) {
                writer.write(", ");
            } else {
                writer.write(",");
            }

            this.rounding.doFormat(writer);
        }
        writer.write(")");
    }
}
