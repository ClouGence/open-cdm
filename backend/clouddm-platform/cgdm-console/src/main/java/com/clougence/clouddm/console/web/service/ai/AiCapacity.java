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

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.model.ai.AiError;

import jakarta.annotation.Resource;

@Component
public class AiCapacity {
    @Resource
    private AiProperties               limits;
    private final Map<String, Integer> users   = new HashMap<>();
    private final Map<String, Integer> engines = new HashMap<>();
    private int                        active;
    private int                        probes;

    public synchronized AiPermit acquire(String uid, String engineId, boolean probe) {
        int userCount = users.getOrDefault(uid, 0);
        int engineCount = engines.getOrDefault(engineId, 0);
        if (active >= limits.getMaxActive() || userCount >= 1 || engineCount >= limits.getMaxPerEngine() || (probe && probes >= limits.getMaxProbes())) {
            throw AiError.AI_BUSY.exception();
        }
        active++;
        if (probe) {
            probes++;
        }
        users.put(uid, userCount + 1);
        engines.put(engineId, engineCount + 1);
        return new AiPermit(() -> release(uid, engineId, probe));
    }

    private synchronized void release(String uid, String engineId, boolean probe) {
        active--;
        if (probe) {
            probes--;
        }
        users.computeIfPresent(uid, (key, count) -> {
            if (count == 1) {
                return null;
            }
            return count - 1;
        });
        engines.computeIfPresent(engineId, (key, count) -> {
            if (count == 1) {
                return null;
            }
            return count - 1;
        });
    }
}
