package com.clougence.clouddm.comm.util;

public class ResponseUtils {

    public static ResponseData<String> buildSuccess() {
        return new ResponseData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, null);
    }

    public static <T> ResponseData<T> buildSuccess(T data) {
        return new ResponseData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, data);
    }

    public static <T> ResponseData<T> buildSuccess(String requestId, T data) {
        return new ResponseData<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), requestId, data);
    }

    public static ResponseData<String> buildError(String msg) {
        return new ResponseData<>(ResultEnum.ERROR.getCode(), msg, null, null);
    }

    public static ResponseData<String> buildError(String code, String msg) {
        return new ResponseData<>(code, msg, null, null);
    }

    public static ResponseData<String> buildError(String code, String msg, String requestId) {
        return new ResponseData<>(code, msg, requestId, null);
    }

    public static <T> ResponseData<T> buildError(String code, String msg, String requestId, T data) {
        return new ResponseData<>(code, msg, requestId, data);
    }

    public static <T> boolean isSuccess(ResponseData<T> responseData) {
        return responseData != null && responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }

    public static <T> boolean isFailed(ResponseData<T> responseData) {
        return responseData == null || !responseData.getCode().equals(ResultEnum.SUCCESS.getCode());
    }
}
