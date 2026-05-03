package com.clougence.rdp.component.openapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientException extends Exception {

    private String    requestId;

    private String    errCode;

    private String    errMsg;

    private ErrorType errorType;

    private String    errorDescription;

    public ClientException(String errorCode, String errorMessage, String requestId, String errorDescription){
        this(errorCode, errorMessage);
        this.errorDescription = errorDescription;
        this.requestId = requestId;
    }

    public ClientException(String errCode, String errMsg, String requestId){
        this(errCode, errMsg);
        this.requestId = requestId;
        this.errorType = ErrorType.CLIENT;
    }

    public ClientException(String errCode, String errMsg, Throwable cause){
        super(errCode + " : " + errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errorType = ErrorType.CLIENT;
    }

    public ClientException(String errCode, String errMsg){
        super(errCode + " : " + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errorType = ErrorType.CLIENT;
    }

    public ClientException(String message){
        super(message);
        this.errorType = ErrorType.CLIENT;
    }

    public ClientException(Throwable cause){
        super(cause);
        this.errorType = ErrorType.CLIENT;
    }
}
