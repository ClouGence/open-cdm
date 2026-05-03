package com.clougence.clouddm.api.common.rpc;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResultEnum;

public class ResWebDataUtils {

    public static <T> ResWebData<T> buildSuccess() {
        return new ResWebData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, null);
    }

    public static <T> ResWebData<T> buildSuccess(T data) {
        return new ResWebData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, data);
    }

    public static <T> ResWebData<T> buildSuccess(String requestId, T data) {
        return new ResWebData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), requestId, data);
    }

    public static <T> ResWebData<T> buildError(String msg) {
        return new ResWebData<>(ResultEnum.ERROR.getCode(), msg, null, null);
    }

    public static <T> ResWebData<T> buildError(String code, String msg) {
        return new ResWebData<>(code, msg, null, null);
    }

    public static <T> ResWebData<T> buildError(String code, String msg, String requestId) {
        return new ResWebData<>(code, msg, requestId, null);
    }

    public static <T> ResWebData<T> buildError(String code, String msg, String requestId, T data) {
        return new ResWebData<>(code, msg, requestId, data);
    }

    public static <T> boolean isSuccess(ResWebData<T> responseData) {
        return responseData != null && responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }

    public static <T> boolean isFailed(ResWebData<T> responseData) {
        return responseData == null || !responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }
}
