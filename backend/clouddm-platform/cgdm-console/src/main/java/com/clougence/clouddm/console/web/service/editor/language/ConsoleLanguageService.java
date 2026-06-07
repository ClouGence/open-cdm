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
package com.clougence.clouddm.console.web.service.editor.language;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.language.DsLanguageService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.editor.WsRequestFO;
import com.clougence.clouddm.console.web.model.fo.editor.language.WsLanguageFO;
import com.clougence.clouddm.console.web.model.fo.editor.language.WsLanguageType;
import com.clougence.clouddm.console.web.model.vo.editor.WsResult;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.schema.umi.struts.UmiTypes;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleLanguageService implements UnifiedPostConstruct, ConsoleLanguageApi {

    private final AtomicInteger              currentRequests       = new AtomicInteger();
    private final Map<String, AtomicInteger> currentRequestsByUser = new ConcurrentHashMap<>();

    @Resource
    private ApplicationContext               appContext;
    @Resource
    private DmAuthServiceForBiz              authCheckService;
    @Resource
    private UserConfigService                userConfigService;
    @Resource
    private DsLanguageService                dsLanguageService;
    @Resource
    private DmDsConfigService                dmDsConfigService;
    private ConsoleLanguageTaskExecutor      languageExecutor;

    @Override
    public void init() throws Exception {
        this.languageExecutor = new ConsoleLanguageTaskExecutor(this.appContext.getClassLoader(), 10);
    }

    @Override
    public void stop() {
        this.languageExecutor.close();
    }

    @Override
    public void offerLanguageRequest(WsLanguageFO fo, Consumer<WsResult> consumer) {
        if (!this.authCheckService.checkRoleAuthWithoutError(fo.getPrimaryUserId(), fo.getCurrentUserId(), SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            String message = RdpAuthUtils.missRoleAuthMsg(SecRoleAuthLabel.DM_QUERY_CONSOLE);
            consumer.accept(DmConvertUtils.convertToWsLanguageErrorResult(fo, null, DmErrorCode.DS_LANGUAGE_ERROR.code(), message));
            return;
        }

        JSONObject request = fo.getRequest();
        if (request == null) {
            request = new JSONObject();
        }

        LanguageCtx ctx = createLanguageCtx(fo);
        AbstractRequest parsed = parseRequest(fo, ctx, request);

        try {
            switch (fo.getLanguageType()) {
                case COMPLETE:
                    acquireOrThrow(fo);
                    submitComplete(fo, (CompletionRequest) parsed, releaseRequestCounter(fo, consumer));
                    break;
                case VALIDATE:
                    acquireOrThrow(fo);
                    submitValidate(fo, (ValidateRequest) parsed, releaseRequestCounter(fo, consumer));
                    break;
                case SPLIT:
                    submitSplit(fo, (SplitRequest) parsed, consumer);
                    break;
                default:
                    throw new UnsupportedOperationException("Language WsType '" + fo.getLanguageType() + "' Unsupported.");
            }
        } catch (ErrorMessageException e) {
            consumer.accept(DmConvertUtils.convertToWsLanguageErrorResult(fo, parsed, e.getErrorCode(), e.getErrorMessage()));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            String errCode = DmErrorCode.DS_LANGUAGE_ERROR.code();
            String errMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.DS_LANGUAGE_SERVICE_ERROR.name());
            consumer.accept(DmConvertUtils.convertToWsLanguageErrorResult(fo, parsed, errCode, errMsg));
        }
    }

    private LanguageCtx createLanguageCtx(WsLanguageFO fo) {
        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        if (levels == null || levels.dsDO() == null) {
            return new LanguageCtx(levels, null, null, null);
        }

        DmDsDO dsDO = levels.dsDO();
        Map<String, Object> params = new HashMap<>();
        SessionContextDTO ctxDTO = new SessionContextDTO();
        levels.levelsParam().forEach((umiType, value) -> {
            if (umiType == UmiTypes.Catalog) {
                params.put(SessionSpi.PARAMS_DEFAULT_DB, value);
                ctxDTO.setRdbCatalog((String) value);
            } else if (umiType == UmiTypes.Schema) {
                params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, value);
                ctxDTO.setRdbSchema((String) value);
            }
        });

        return new LanguageCtx(levels, this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType()), ctxDTO, params);
    }

    private static AbstractRequest parseRequest(WsLanguageFO fo, LanguageCtx ctx, JSONObject json) {
        WsLanguageType languageType = fo.getLanguageType();
        if (languageType == null) {
            throw new UnsupportedOperationException("Language WsType is empty.");
        }

        AbstractRequest request = json.toJavaObject(languageType.getRequestType());
        request.setRequestId(fo.getRequestId());
        request.setPrimaryUserId(fo.getPrimaryUserId());
        request.setCurrentUserId(fo.getCurrentUserId());
        if (ctx.getLevels() != null && ctx.getLevels().dsDO() != null) {
            request.setDataSourceId(ctx.getLevels().dsDO().getId());
            request.setDsType(ctx.getLevels().dsDO().getDataSourceType().getTypeName());
            request.setLevels(ctx.getLevels().levelsDef());
            request.setLevelsParam(ctx.getLevels().levelsParam());
        }

        if (ctx.getCtxDTO() != null) {
            request.setCatalog(ctx.getCtxDTO().getRdbCatalog());
            request.setSchema(ctx.getCtxDTO().getRdbSchema());
        }

        request.setBasicCodeLine(fo.getBasicCodeLine());
        request.setBasicCodeColumn(fo.getBasicCodeColumn());
        return request;
    }

    private void acquireOrThrow(WsRequestFO fo) {
        if (this.currentRequests.incrementAndGet() > this.userConfigService.languageMaxRequests()) {
            this.currentRequests.decrementAndGet();
            throw new ErrorMessageException(DmErrorCode.DS_LANGUAGE_ERROR.code(), DmI18nUtils.getMessage(I18nDmMsgKeys.DS_LANGUAGE_BUSY.name()));
        }

        String userId = fo.getCurrentUserId();
        AtomicInteger userCounter = this.currentRequestsByUser.computeIfAbsent(userId, key -> new AtomicInteger());
        if (userCounter.incrementAndGet() > this.userConfigService.languageMaxRequests(userId)) {
            userCounter.decrementAndGet();
            if (userCounter.get() <= 0) {
                this.currentRequestsByUser.remove(userId, userCounter);
            }

            this.currentRequests.decrementAndGet();
            throw new ErrorMessageException(DmErrorCode.DS_LANGUAGE_ERROR.code(), DmI18nUtils.getMessage(I18nDmMsgKeys.DS_LANGUAGE_USER_BUSY.name()));
        }
    }

    private Consumer<WsResult> releaseRequestCounter(WsRequestFO fo, Consumer<WsResult> consumer) {
        AtomicBoolean released = new AtomicBoolean();
        return result -> {
            try {
                consumer.accept(result);
            } finally {
                if (released.compareAndSet(false, true)) {
                    this.currentRequests.decrementAndGet();
                    String userId = fo.getCurrentUserId();
                    AtomicInteger userCounter = this.currentRequestsByUser.get(userId);
                    if (userCounter != null && userCounter.decrementAndGet() <= 0) {
                        this.currentRequestsByUser.remove(userId, userCounter);
                    }
                }
            }
        };
    }

    private void submitTask(WsLanguageFO fo, AbstractRequest request, Consumer<WsResult> consumer, Runnable task) {
        this.languageExecutor.submitTask(() -> {
            try {
                task.run();
            } catch (ErrorMessageException e) {
                consumer.accept(DmConvertUtils.convertToWsLanguageErrorResult(fo, request, e.getErrorCode(), e.getErrorMessage()));
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                String errCode = DmErrorCode.DS_LANGUAGE_ERROR.code();
                String errMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.DS_LANGUAGE_SERVICE_ERROR.name());
                consumer.accept(DmConvertUtils.convertToWsLanguageErrorResult(fo, request, errCode, errMsg));
            }
        });
    }
    // ------------------------------------------------------------------------
    //                                                             for Complete
    // ------------------------------------------------------------------------

    private void submitComplete(WsLanguageFO fo, CompletionRequest request, Consumer<WsResult> consumer) {
        CompletionResult degraded = new CompletionResult();
        degraded.setRequestId(request.getRequestId());
        degraded.setRequestVersion(request.getRequestVersion());

        submitTask(fo, request, consumer, () -> {
            CompletionResult result = this.dsLanguageService.complete(fo.getPrimaryUserId(), request);
            consumer.accept(DmConvertUtils.convertToWsLanguageResult(fo, result == null ? degraded : result));
        });
    }

    // ------------------------------------------------------------------------
    //                                                             for Validate
    // ------------------------------------------------------------------------

    private void submitValidate(WsLanguageFO fo, ValidateRequest request, Consumer<WsResult> consumer) {
        ValidateResult degraded = new ValidateResult();
        degraded.setRequestId(request.getRequestId());
        degraded.setRequestVersion(request.getRequestVersion());

        submitTask(fo, request, consumer, () -> {
            ValidateResult result = this.dsLanguageService.validate(fo.getPrimaryUserId(), request);
            consumer.accept(DmConvertUtils.convertToWsLanguageResult(fo, result == null ? degraded : result));
        });
    }

    // ------------------------------------------------------------------------
    //                                                                for Split
    // ------------------------------------------------------------------------

    private void submitSplit(WsLanguageFO fo, SplitRequest request, Consumer<WsResult> consumer) {
        SplitResult degraded = new SplitResult();
        degraded.setRequestId(request.getRequestId());
        degraded.setRequestVersion(request.getRequestVersion());

        submitTask(fo, request, consumer, () -> {
            SplitResult result = this.dsLanguageService.split(fo.getPrimaryUserId(), request);
            consumer.accept(DmConvertUtils.convertToWsLanguageResult(fo, result == null ? degraded : result));
        });
    }
}
