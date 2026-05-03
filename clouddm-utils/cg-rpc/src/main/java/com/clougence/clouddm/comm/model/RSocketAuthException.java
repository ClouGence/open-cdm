package com.clougence.clouddm.comm.model;

/**
 * @author bucketli 2021/1/18 13:05
 */
public class RSocketAuthException extends RuntimeException {

    public RSocketAuthException(){
    }

    public RSocketAuthException(String message){
        super(message);
    }

    public RSocketAuthException(String message, Throwable cause){
        super(message, cause);
    }

    public RSocketAuthException(Throwable cause){
        super(cause);
    }

    public RSocketAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
