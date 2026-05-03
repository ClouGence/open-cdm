package com.clougence.utils.io.result;

import java.io.IOException;

import lombok.Getter;

@Getter
public class ResultSetOverflowException extends IOException {

    private final long overflowSize;

    public ResultSetOverflowException(String s, long overflowSize){
        super(s);
        this.overflowSize = overflowSize;
    }
}
