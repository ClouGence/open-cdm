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
package com.clougence.clouddm.console.web.component.approval.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalExecutionStateMO {
    public static final String TYPE_PREPARATION = "EXECUTION_PREPARATION";
    public static final String TYPE_DISPATCH    = "EXECUTION_DISPATCH";
    public static final String TYPE_RUNNING     = "EXECUTION_RUNNING";

    public static final String STATUS_INIT     = "INIT";
    public static final String STATUS_RUNNING  = "RUNNING";
    public static final String STATUS_FINISHED = "FINISHED";
    public static final String STATUS_FAILED   = "FAILED";
    public static final String STATUS_CANCELED = "CANCELED";

    private String             executionType;
    private Integer            displayOrder;
    private String             executionStatus;
    private Long               startTimeUtc;
    private Long               finishTimeUtc;
    private Long               processedCount;
    private Long               totalCount;
    private String             errorMessage;

    public ApprovalExecutionStateMO(){
    }

    public ApprovalExecutionStateMO(String executionType, int displayOrder){
        this.executionType = executionType;
        this.displayOrder = displayOrder;
        this.executionStatus = STATUS_INIT;
    }

    public static boolean isExecutionType(String type) {
        return TYPE_PREPARATION.equals(type) || TYPE_DISPATCH.equals(type) || TYPE_RUNNING.equals(type);
    }
}
