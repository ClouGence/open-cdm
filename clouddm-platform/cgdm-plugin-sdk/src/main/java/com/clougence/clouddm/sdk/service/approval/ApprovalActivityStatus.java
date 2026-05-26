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
package com.clougence.clouddm.sdk.service.approval;

public enum ApprovalActivityStatus {

    NEW,
    RUNNING,
    CANCELED,
    COMPLETED,
    REFUSE,
    CLOSE; // or approval if one task agree the activity another task will close, will delete task from context

    public static ApprovalActivityStatus valueOfIgnoreCase(String tarFormat) {
        if (tarFormat == null || tarFormat.trim().equals("")) {
            return null;
        }

        for (ApprovalActivityStatus format : ApprovalActivityStatus.values()) {
            if (format.name().equalsIgnoreCase(tarFormat.trim())) {
                return format;
            }
        }
        return null;
    }

    public static boolean canUpdate(ApprovalActivityStatus oldStatus, ApprovalActivityStatus newStatus) {
        if (oldStatus == NEW) {
            return true;
        }
        return oldStatus == RUNNING && newStatus != NEW;
    }
}
