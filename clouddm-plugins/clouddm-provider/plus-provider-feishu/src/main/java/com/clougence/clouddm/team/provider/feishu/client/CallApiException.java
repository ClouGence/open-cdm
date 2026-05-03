package com.clougence.clouddm.team.provider.feishu.client;

import lombok.Getter;

@Getter
public class CallApiException extends RuntimeException {

    private final int code;

    public CallApiException(int code, String message){
        super(message);
        this.code = code;
    }

}
