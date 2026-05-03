package com.clougence.dslpaser.ast.token;

import java.io.IOException;

import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.dslpaser.foramt.Format;

import lombok.Getter;

/**
 * Basic ast token
 * @author zyc@hasor.net
 * @version : 2020-06-11
 */
@Getter
public class IntegerToken extends StringToken implements Format {

    public IntegerToken(String value){
        super("", value);
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.write(this.getValue());
    }
}
