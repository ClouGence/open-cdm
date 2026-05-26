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
package com.clougence.clouddm.team.provider.feishu.constants.approval;

import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.utils.StringUtils;

public enum FeishuTaskStatus {

    PENDING,
    APPROVED,
    REJECTED,
    TRANSFERRED,
    DONE;

    public static FeishuTaskStatus getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (FeishuTaskStatus status : FeishuTaskStatus.values()) {
            if (status.name().equals(result)) {
                return status;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }

    public ApprovalActivityStatus convertStatus() {
        switch (this) {
            case PENDING: {
                return ApprovalActivityStatus.RUNNING;
            }
            case APPROVED: {
                return ApprovalActivityStatus.COMPLETED;
            }
            case REJECTED: {
                return ApprovalActivityStatus.REFUSE;
            }
            case TRANSFERRED: {
                return ApprovalActivityStatus.CLOSE;
            }
            case DONE: {
                return ApprovalActivityStatus.CANCELED;
            }

        }
        return null;
    }
}
