package com.clougence.detectrule.parser.ast.token;

import java.math.RoundingMode;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.dslpaser.ast.token.StringToken;

import lombok.Getter;
import lombok.Setter;

/**
 * number format rounding
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class RoundingToken extends StringToken implements Fragment {

    private RoundingMode rounding;

    public RoundingToken(String symbol, RoundingMode rounding){
        super("", symbol);
        Objects.requireNonNull(symbol, "symbol is null.");
        this.rounding = Objects.requireNonNull(rounding, "rounding is null.");
    }
}
