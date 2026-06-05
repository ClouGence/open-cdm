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
package com.clougence.clouddm.api.common.rpc;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResultEnum;

public class ResApiDataUtils {

    public static ResApiData<String> buildSuccess() {
        return new ResApiData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, null);
    }

    public static <T> ResApiData<T> buildSuccess(T data) {
        return new ResApiData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, data);
    }

    public static <T> ResApiData<T> buildSuccess(String requestId, T data) {
        return new ResApiData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), requestId, data);
    }

    public static ResApiData<String> buildError(String msg) {
        return new ResApiData<>(ResultEnum.ERROR.getCode(), msg, null, null);
    }

    public static ResApiData<String> buildError(String code, String msg) {
        return new ResApiData<>(code, msg, null, null);
    }

    public static ResApiData<String> buildError(String code, String msg, String requestId) {
        return new ResApiData<>(code, msg, requestId, null);
    }

    public static <T> ResApiData<T> buildError(String code, String msg, String requestId, T data) {
        return new ResApiData<>(code, msg, requestId, data);
    }

    public static <T> boolean isSuccess(ResApiData<T> responseData) {
        return responseData != null && responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }

    public static <T> boolean isFailed(ResApiData<T> responseData) {
        return responseData == null || !responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }
}
