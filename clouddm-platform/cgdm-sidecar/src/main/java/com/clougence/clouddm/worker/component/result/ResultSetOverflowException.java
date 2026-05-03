package com.clougence.clouddm.worker.component.result;

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
