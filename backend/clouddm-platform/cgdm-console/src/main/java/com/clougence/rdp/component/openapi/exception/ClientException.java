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
