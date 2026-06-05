/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.api.common.exception;

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
