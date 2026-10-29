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

import java.util.concurrent.*;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.Getter;

@Component
@Getter
public class AiExecutors {
    @Resource
    @Getter(AccessLevel.NONE)
    private AiProperties                limits;
    private ExecutorService             provider;
    private ExecutorService             metadata;
    private ExecutorService             events;
    private ExecutorService             control;
    private ScheduledThreadPoolExecutor timer;

    @PostConstruct
    public void initialize() {
        provider = pool("ai-provider", limits.getMaxActive());
        metadata = pool("ai-metadata", limits.getMaxActive());
        events = pool("ai-events", limits.getMaxActive() * 4);
        control = pool("ai-control", 2);
        timer = new ScheduledThreadPoolExecutor(2, ThreadUtils.daemonThreadFactory(AiExecutors.class.getClassLoader(), "ai-timer-%s"));
        timer.setRemoveOnCancelPolicy(true);
    }

    private static ExecutorService pool(String name, int maximum) {
        return new ThreadPoolExecutor(0,
            maximum,
            30,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            ThreadUtils.daemonThreadFactory(AiExecutors.class.getClassLoader(), name + "-%s"),
            new ThreadPoolExecutor.AbortPolicy());
    }

    @PreDestroy
    public void shutdown() {
        timer.shutdownNow();
        provider.shutdownNow();
        metadata.shutdownNow();
        events.shutdownNow();
        control.shutdownNow();
    }
}
