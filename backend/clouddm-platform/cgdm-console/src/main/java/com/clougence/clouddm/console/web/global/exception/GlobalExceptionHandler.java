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
package com.clougence.clouddm.console.web.global.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clougence.clouddm.api.common.exception.ConsoleRuntimeException;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/29 11:20
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ThirdPartyApiException.class)
    @ResponseBody
    public ResWebData<?> handleNormalException(ThirdPartyApiException e) {
        logAndSaveExc(e);

        if (StringUtils.equals(RdpFeatureIDs.PRODUCT_CLOUD_RDP, e.getProductType())) {
            String i18nMsg = DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs());
            return ResWebDataUtils.buildError(DmErrorCode.COMM_SYSTEM_ERROR.code(), i18nMsg);
        } else if (StringUtils.equals(RdpFeatureIDs.PRODUCT_CLOUD_DM, e.getProductType())) {
            String i18nMsg = DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs());
            return ResWebDataUtils.buildError(DmErrorCode.COMM_SYSTEM_ERROR.code(), i18nMsg);
        } else {
            return ResWebDataUtils.buildError(DmErrorCode.COMM_SYSTEM_ERROR.code(), e.getMessageKey());
        }
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResWebData<?> handleNormalException(Exception e) {
        logAndSaveExc(e);
        List<String> errorMsgs = new ArrayList<>();
        errorMsgs.add(e.getMessage());
        String msgs = JsonUtils.toJson(errorMsgs);
        return ResWebDataUtils.buildError(DmErrorCode.COMM_SYSTEM_ERROR.code(), msgs);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResWebData<?> handleMethodArgumentException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        List<String> errorMsgs = errors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        String msgs = JsonUtils.toJson(errorMsgs);
        return ResWebDataUtils.buildError(msgs);
    }

    @ExceptionHandler(value = ErrorMessageException.class)
    @ResponseBody
    public ResWebData<?> handleNormalException(ErrorMessageException e) {
        logAndSaveExc(e);
        return ResWebDataUtils.buildError(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(value = ConsoleRuntimeException.class)
    @ResponseBody
    public ResWebData<?> handleNormalException(ConsoleRuntimeException e) {
        logAndSaveExc(e);
        return ResWebDataUtils.buildError(e.getErrorCode().getCode(), DmI18nUtils.getMessage(e.getErrorCode(), e.getParams()));
    }

    protected void logAndSaveExc(Throwable e) {
        String errMsg = "msg:" + ExceptionUtils.getRootCauseMessage(e);
        log.error(errMsg, e);
        try {
            Sentry.captureException(e);
        } catch (Exception exception) {
            String msg = "save exception to sentry failed, msg:" + ExceptionUtils.getRootCauseMessage(exception);
            log.error(msg, exception);
        }
    }
}
