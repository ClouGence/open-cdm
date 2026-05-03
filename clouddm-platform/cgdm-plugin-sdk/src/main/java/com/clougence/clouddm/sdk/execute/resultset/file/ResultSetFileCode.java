package com.clougence.clouddm.sdk.execute.resultset.file;

import lombok.Getter;

@Getter
public enum ResultSetFileCode {

    Unsupported(1);

    private final int code;

    ResultSetFileCode(int code){
        this.code = code;
    }

    public static ResultSetFileCode of(int code) {
        for (ResultSetFileCode value : ResultSetFileCode.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
