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
package com.clougence.clouddm.console.web.service.ai;

import java.util.Locale;
import java.util.concurrent.*;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.model.ai.AiError;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AiMetadataExecutor {
    @Resource
    private AiExecutors  executors;
    @Resource
    private AiProperties limits;

    public <T> CompletableFuture<T> submit(AiRunHandle handle, AiPermit owner, Callable<T> operation) {
        CompletableFuture<T> response = new CompletableFuture<>();
        Locale locale = LocaleContextHolder.getLocale();
        AiPermit physical = owner.retain();
        ScheduledFuture<?> timeout;
        try {
            timeout = executors.getTimer().schedule(() -> {
                response.completeExceptionally(AiError.METADATA_TIMEOUT.exception());
                handle.fail(AiError.METADATA_TIMEOUT);
            }, Math.min(handle.remainingMillis(), limits.getMetadataSeconds() * 1000L), TimeUnit.MILLISECONDS);
        } catch (RuntimeException e) {
            physical.close();
            throw e;
        }
        response.whenComplete((value, error) -> timeout.cancel(false));
        try {
            executors.getMetadata().execute(() -> {
                try (physical) {
                    LocaleContextHolder.setLocale(locale);
                    handle.checkActive();
                    T value = operation.call();
                    handle.checkActive();
                    response.complete(value);
                } catch (ErrorMessageException | CancellationException e) {
                    response.completeExceptionally(e);
                } catch (Exception e) {
                    log.error("AI metadata failed", e);
                    response.completeExceptionally(AiError.METADATA_FAILED.exception());
                } finally {
                    LocaleContextHolder.resetLocaleContext();
                }
            });
        } catch (RejectedExecutionException e) {
            physical.close();
            response.completeExceptionally(AiError.AI_BUSY.exception());
        }
        return response;
    }
}
