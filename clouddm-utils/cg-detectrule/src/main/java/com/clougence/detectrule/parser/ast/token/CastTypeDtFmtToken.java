package com.clougence.detectrule.parser.ast.token;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * dataTime format
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class CastTypeDtFmtToken extends AstFragment implements Fragment {

    private StringToken format;

    public CastTypeDtFmtToken(StringToken format){
        this.format = Objects.requireNonNull(format, "format is null.");
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write("T(");
        this.format.doFormat(writer);
        writer.write(")");
    }
}
