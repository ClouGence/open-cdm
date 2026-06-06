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
package com.clougence.clouddm.sdk.model.exception;

import com.clougence.utils.ArrayUtils;

import lombok.Getter;

@Getter
public class ThirdPartyApiException extends RuntimeException {

    private final ThirdPartyApiErrorType errorType;
    private final String                 messageKey;
    private final Object[]               messageArgs;

    public ThirdPartyApiException(Throwable e, String errorMessage, ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs){
        super(errorMessage, e);
        this.errorType = errorType;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public static ThirdPartyApiExceptionBuilder as() {
        return new ThirdPartyApiExceptionBuilderImpl();
    }

    public interface ThirdPartyApiExceptionBuilder {

        ThirdPartyApiException with(String messageKey, Object... messageArgs);

        ThirdPartyApiException with(ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs);

        ThirdPartyApiException with(Throwable e);

        ThirdPartyApiException with(Throwable e, String messageKey, Object... messageArgs);

        ThirdPartyApiException with(Throwable e, ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs);
    }

    private static final class ThirdPartyApiExceptionBuilderImpl implements ThirdPartyApiExceptionBuilder {

        public ThirdPartyApiException with(String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(null, messageKey, ThirdPartyApiErrorType.OTHER, messageKey, messageArgs);
        }

        public ThirdPartyApiException with(ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(null, messageKey, errorType, messageKey, messageArgs);
        }

        public ThirdPartyApiException with(Throwable e) {
            return new ThirdPartyApiException(e, e.getMessage(), ThirdPartyApiErrorType.OTHER, e.getMessage(), ArrayUtils.EMPTY_OBJECT_ARRAY);
        }

        @Override
        public ThirdPartyApiException with(Throwable e, String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(e, e.getMessage(), ThirdPartyApiErrorType.OTHER, messageKey, messageArgs);
        }

        @Override
        public ThirdPartyApiException with(Throwable e, ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(e, e.getMessage(), errorType, messageKey, messageArgs);
        }
    }
}
