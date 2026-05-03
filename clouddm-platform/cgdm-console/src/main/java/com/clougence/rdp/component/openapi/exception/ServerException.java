package com.clougence.rdp.component.openapi.exception;

public class ServerException extends Exception {

    private String    requestId;

    private String    errCode;

    private String    errMsg;

    private ErrorType errorType;

    private String    errorDescription;

    public ServerException(String errorCode, String errorMessage, String requestId, String errorDescription){
        this(errorCode, errorMessage);
        this.errorDescription = errorDescription;
        this.requestId = requestId;
    }

    public ServerException(String errCode, String errMsg, String requestId){
        this(errCode, errMsg);
        this.requestId = requestId;
        this.errorType = ErrorType.SERVER;
    }

    public ServerException(String errCode, String errMsg, Throwable cause){
        super(errCode + " : " + errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errorType = ErrorType.SERVER;
    }

    public ServerException(String errCode, String errMsg){
        super(errCode + " : " + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errorType = ErrorType.SERVER;
    }

    public ServerException(String message){
        super(message);
        this.errorType = ErrorType.SERVER;
    }

    public ServerException(Throwable cause){
        super(cause);
        this.errorType = ErrorType.SERVER;
    }
}
