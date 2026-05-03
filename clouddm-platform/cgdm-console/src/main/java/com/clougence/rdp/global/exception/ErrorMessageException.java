package com.clougence.rdp.global.exception;

import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.utils.ExceptionUtils;

public class ErrorMessageException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public ErrorMessageException(String errorMessage){
        this(ConsoleErrorCode.UNKNOWN.getCode(), errorMessage);
    }

    public ErrorMessageException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorMessageException(String errorCode, String errorMessage, Throwable e){
        super(errorMessage, e);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorMessageException(Throwable e){
        super(e.getMessage(), e);
        this.errorCode = ConsoleErrorCode.UNKNOWN.getCode();
        this.errorMessage = e.getMessage();
    }

    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else {
            Throwable e = ExceptionUtils.getRootCause(this);
            if (e == this) {
                return super.getMessage();
            } else {
                return e.getMessage();
            }
        }
    }

    public String toString() {
        return this.getLocalizedMessage();
    }

    public String getErrorCode() { return this.errorCode; }

    public String getErrorMessage() { return this.errorMessage; }
}
