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
package com.clougence.clouddm.console.web.service.cicd.domain;

import com.clougence.clouddm.sdk.scm.ScmEventType;

import lombok.Getter;

@Getter
public final class ChangeTriggerContext {

    /**
     * Prefix persisted in {@code trigger_type} for SCM Webhook triggers; the concrete SCM event type is appended.
     */
    private static final String WEBHOOK_PREFIX = "Webhook";

    private final String        commitId;
    private final String        deliveryId;
    private final String        triggerType;

    private ChangeTriggerContext(String commitId, String deliveryId, String triggerType){
        this.commitId = commitId;
        this.deliveryId = deliveryId;
        this.triggerType = triggerType;
    }

    public static ChangeTriggerContext webhook(String commitId, String deliveryId, ScmEventType eventType) {
        return new ChangeTriggerContext(commitId, deliveryId, WEBHOOK_PREFIX + eventType.name());
    }

    public static ChangeTriggerContext manual(String commitId) {
        return new ChangeTriggerContext(commitId, null, "Manual");
    }

    public static ChangeTriggerContext remote(String commitId) {
        return new ChangeTriggerContext(commitId, null, "Remote");
    }
}
