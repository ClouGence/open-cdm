package com.clougence.rdp.global.exception;

import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.utils.ExceptionUtils;

/**
 * @author bucketli 2021/1/6 10:20
 */
@Deprecated // use throw new ErrorMessageException(RdpI18nUtils.getMessage(xxxx));
public class ConsoleRuntimeException extends RuntimeException {

    private final Object[]         params;
    private final ConsoleErrorCode errorCode;

    public ConsoleRuntimeException(ConsoleErrorCode errorCode, Object... params){
        super(errorCode.getMessage(params));
        this.params = params;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else {
            Throwable e = ExceptionUtils.getRootCause(this);
            return e.getMessage();
        }
    }

    @Override
    public String toString() {
        return getLocalizedMessage();
    }

    public Object[] getParams() { return params; }

    public ConsoleErrorCode getErrorCode() { return errorCode; }
}
