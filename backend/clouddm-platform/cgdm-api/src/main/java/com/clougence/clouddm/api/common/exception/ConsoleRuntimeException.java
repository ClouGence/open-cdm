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

import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.utils.ExceptionUtils;

/**
 * @author bucketli 2021/1/6 10:20
 */
@Deprecated // use throw new ErrorMessageException(DmI18nUtils.getMessage(xxxx));
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
