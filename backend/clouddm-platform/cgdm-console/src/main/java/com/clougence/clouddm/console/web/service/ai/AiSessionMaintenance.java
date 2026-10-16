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

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.platform.dal.model.ai.AiTurnStatus;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AiSessionMaintenance {
    @Resource
    private AiExecutors         executors;
    @Resource
    private AiTurnStore         store;
    @Resource
    private AiTurnService       turns;
    @Resource
    private AiStreamService     streams;
    private long                nextExpiryCheck;
    private final AtomicBoolean running = new AtomicBoolean();
    private ScheduledFuture<?>  schedule;

    @PostConstruct
    public void start() {
        nextExpiryCheck = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
        schedule = executors.getTimer().scheduleWithFixedDelay(this::dispatch, 1, 1, TimeUnit.SECONDS);
    }

    private void dispatch() {
        streams.tick();
        if (!running.compareAndSet(false, true)) {
            return;
        }
        try {
            executors.getControl().execute(() -> {
                try {
                    turns.checkActiveRuns();
                    streams.followPersistedRuns();
                    long now = System.nanoTime();
                    if (now - nextExpiryCheck >= 0) {
                        nextExpiryCheck = now + TimeUnit.SECONDS.toNanos(2);
                        sweep();
                    }
                } catch (Exception e) {
                    log.error("AI session maintenance failed", e);
                } finally {
                    running.set(false);
                }
            });
        } catch (RejectedExecutionException e) {
            running.set(false);
        }
    }

    private void sweep() {
        for (AiTurnRecordVO turn : store.expired(100)) {
            // Another Console may already have finished or cleaned this turn.
            try {
                store.finish(turn.getSessionId(), turn.getId(), AiTurnStatus.EXPIRED, null, null, AiError.TURN_EXPIRED.name(), AiError.TURN_EXPIRED.message(), null);
            } catch (ErrorMessageException e) {
                if (!AiError.NOT_FOUND.name().equals(e.getErrorCode())) {
                    throw e;
                }
            }
        }

    }

    @PreDestroy
    public void stop() {
        if (schedule != null) {
            schedule.cancel(false);
        }
    }
}
