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
package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingCallbackEvent {

    BPMS_TASK_CHANGE("bpms_task_change"),
    BPMS_INSTANCE_CHANGE("bpms_instance_change");

    String eventType;

    DingCallbackEvent(String eventType){
        this.eventType = eventType;
    }

    public static DingCallbackEvent getByName(String eventType) {
        if (StringUtils.isBlank(eventType)) {
            return null;
        }

        for (DingCallbackEvent dingApproResult : DingCallbackEvent.values()) {
            if (dingApproResult.eventType.equals(eventType)) {
                return dingApproResult;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + eventType);
    }
}
