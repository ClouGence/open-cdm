package com.clougence.clouddm.api.common.rpc;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResultEnum;

public class ResSoDataUtils {

    public static ResSoData<String> buildSuccess() {
        return new ResSoData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, null);
    }

    public static <T> ResSoData<T> buildSuccess(T data) {
        return new ResSoData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, data);
    }

    public static <T> ResSoData<T> buildSuccess(String requestId, T data) {
        return new ResSoData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), requestId, data);
    }

    public static ResSoData<String> buildError(String msg) {
        return new ResSoData<>(ResultEnum.ERROR.getCode(), msg, null, null);
    }

    public static ResSoData<String> buildError(String code, String msg) {
        return new ResSoData<>(code, msg, null, null);
    }

    public static ResSoData<String> buildError(String code, String msg, String requestId) {
        return new ResSoData<>(code, msg, requestId, null);
    }

    public static <T> ResSoData<T> buildError(String code, String msg, String requestId, T data) {
        return new ResSoData<>(code, msg, requestId, data);
    }

    public static <T> boolean isSuccess(ResSoData<T> responseData) {
        return responseData != null && responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }

    public static <T> boolean isFailed(ResSoData<T> responseData) {
        return responseData == null || !responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }
}
