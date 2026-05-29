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

import java.util.Arrays;

import lombok.Getter;

/**
 * @author bucketli 2021/1/6 10:18
 */
@Deprecated // use throw new ErrorMessageException(DmI18nUtils.getMessage(xxxx));
public enum ConsoleErrorCode {

    UNKNOWN(ErrorType.SYSTEM, "1001"),
    EMPTY_VERIFY_CODE(ErrorType.SYSTEM, "1002"),
    GET_A_VERIFY_CODE_FIRST(ErrorType.SYSTEM, "1003"),
    VERIFY_CODE_IS_ERROR(ErrorType.SYSTEM, "1004"),
    VERIFY_CODE_IS_EXPIRED(ErrorType.SYSTEM, "1005"),
    VERIFY_CODE_FREQUENCY_TOO_FAST(ErrorType.SYSTEM, "1006"),
    ALREADY_REGISTER(ErrorType.SYSTEM, "1007"),
    PUNISH_NOT_FINISH_YET(ErrorType.SYSTEM, "1008"),
    NEED_REGISTER_FIRST(ErrorType.SYSTEM, "1009"),
    VERIFY_EMAIL_FREQUENCY_TOO_FAST(ErrorType.SYSTEM, "1011"),
    VERIFY_IM_FREQUENCY_TOO_FAST(ErrorType.SYSTEM, "1012"),
    STILL_HAVE_BIZ_USE_IT_WHEN_DELETE_DATASOURCE(ErrorType.DATASOURCE, "3011"),
    VERIFY_PHONE_DISAGREE_FIRST(ErrorType.USER, "8009");

    @Getter
    private final String    code;
    private final ErrorType type;

    ConsoleErrorCode(ErrorType type, String code){
        this.code = code;
        this.type = type;
    }

    public String getType() { return type.name(); }

    public String getMessage(Object... params) {
        if (params == null || params.length == 0) {
            return this.name();
        }
        return this.name() + Arrays.toString(params);
    }
}

enum ErrorType {
    SYSTEM,
    USER,
    DATASOURCE,
    WORKER,
    DATA_OP,
    RSOCKET,
    TICKET,
    SQL_EDITOR,
    DS_ENV
}
