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
package com.clougence.clouddm.console.web.component.cicd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChangeFlowWebhookPolicy {

    /**
     * In-process fixed-minute rate counters keyed by {@code ownerUid:flowId}. Each value stores the epoch minute
     * and the number of authenticated Webhook requests accepted in that minute.
     */
    private static final Map<String, Map.Entry<Long, Integer>> RATE_WINDOWS            = new ConcurrentHashMap<>();

    private ChangeFlowWebhookPolicy(){
    }

    public static boolean isCommitShaValid(String commitSha) {
        return commitSha != null && ChangeFlowConstants.WEBHOOK_COMMIT_SHA_PATTERN.matcher(commitSha).matches();
    }

    public static boolean isDeliveryIdValid(String deliveryId) {
        return deliveryId == null || deliveryId.length() <= ChangeFlowConstants.MAX_WEBHOOK_DELIVERY_ID_LENGTH;
    }

    public static synchronized boolean allowRequest(String owner, long flowId) {
        long minute = System.currentTimeMillis() / 60_000;
        String key = owner + ":" + flowId;
        if (RATE_WINDOWS.size() >= ChangeFlowConstants.MAX_TRACKED_WEBHOOK_RATE_WINDOWS) {
            RATE_WINDOWS.entrySet().removeIf(entry -> entry.getValue().getKey() < minute);
            if (!RATE_WINDOWS.containsKey(key) && RATE_WINDOWS.size() >= ChangeFlowConstants.MAX_TRACKED_WEBHOOK_RATE_WINDOWS) {
                return false;
            }
        }
        return RATE_WINDOWS.compute(key, (ignored, current) -> {
            if (current == null || current.getKey() != minute) {
                return Map.entry(minute, 1);
            }
            return Map.entry(minute, current.getValue() + 1);
        }).getValue() <= ChangeFlowConstants.MAX_WEBHOOK_REQUESTS_PER_MINUTE;
    }
}
