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
package com.clougence.clouddm.comm.util;

import com.clougence.clouddm.comm.model.RSocketDirectionType;
import com.clougence.clouddm.comm.model.RSocketReceiverConstans;
import com.clougence.clouddm.comm.model.RSocketRespCode;
import com.clougence.clouddm.comm.model.RSocketRespDTO;

/**
 * @author wanshao create time is 2021/1/11
 **/
public class RSocketRespUtil {

    public static <T> RSocketRespDTO<T> buildSuccess(T data) {
        return new RSocketRespDTO<T>(RSocketRespCode.SUCCESS, data);
    }

    /**
     * Return success immediately in rsocket communication. Real results will return async.
     */
    public static <T> RSocketRespDTO<T> buildSyncSuccess(T data, RSocketDirectionType rSocketDirectionType) {
        RSocketRespDTO<T> respDTO = new RSocketRespDTO<>(RSocketRespCode.SUCCESS, data);
        respDTO.setApiFullMethodName(RSocketReceiverConstans.RECEIVE_METHOD_NAME);
        respDTO.setRSocketDirectionType(rSocketDirectionType);
        return respDTO;
    }

    public static RSocketRespDTO<String> buildSuccess() {
        return new RSocketRespDTO<>(RSocketRespCode.SUCCESS);
    }

    public static RSocketRespDTO<String> buildSuccess(String code, String msg) {
        return new RSocketRespDTO<>(code, msg);
    }

    /**
     * [Attention] Do not use String object data when you are sending request
     */
    public static <T> RSocketRespDTO<T> buildSuccess(String code, String msg, T data) {
        return new RSocketRespDTO<>(code, msg, data);
    }

    public static RSocketRespDTO<String> buildSuccess(RSocketRespCode resultEnum) {
        return new RSocketRespDTO<>(resultEnum);
    }

    public static RSocketRespDTO<String> buildError() {
        return new RSocketRespDTO<>(RSocketRespCode.ERROR);
    }

    public static RSocketRespDTO<String> buildError(String msg) {
        return new RSocketRespDTO<>(RSocketRespCode.ERROR.getCode(), msg);
    }

    public static RSocketRespDTO<String> buildError(String code, String msg) {
        return new RSocketRespDTO<>(code, msg);
    }

    public static <T> RSocketRespDTO<T> buildError(String code, String msg, T data) {
        return new RSocketRespDTO<>(code, msg, data);
    }

    public static RSocketRespDTO<String> buildError(RSocketRespCode resultEnum) {
        return new RSocketRespDTO<>(resultEnum);
    }

    public static <T> boolean isSuccess(RSocketRespDTO<T> responseData) {
        return responseData != null && responseData.getCode().equals(RSocketRespCode.SUCCESS.getCode());
    }

    public static <T> boolean isFailed(RSocketRespDTO<T> responseData) {
        return responseData == null || !responseData.getCode().equals(RSocketRespCode.SUCCESS.getCode());
    }

}
