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
package com.clougence.clouddm.console.web.component.approval;

import java.util.concurrent.TimeUnit;

public final class CustomApprovalConstants {
    public static final String SYSTEM_EXECUTOR_NAME                 = "CloudDM";
    public static final int    MAX_REQUEST_ID_LENGTH                = 64;
    public static final int    MAX_ENGINE_NAME_LENGTH               = 128;
    public static final int    MAX_TEAM_NAME_LENGTH                 = 128;
    public static final int    MAX_TEAM_DESCRIPTION_LENGTH          = 200;
    public static final String AUTOMATIC_REQUEST_ID_PREFIX          = "automatic_";
    public static final long   EXECUTION_PREPARATION_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(5);

    public static final String AUTHORIZATION_ENGINE                 = "ENGINE";
    public static final String APPROVAL_MODE_CUSTOM                 = "CUSTOM";
    public static final String TEMPLATE_ID_PREFIX                   = "CUSTOM-";

    public static final String FLOW_CONTEXT_TYPE                    = "CUSTOM_APPROVAL_FLOW";
    public static final int    FLOW_SCHEMA_VERSION                  = 2;
    public static final int    APPROVAL_SNAPSHOT_VERSION            = 2;
    public static final String NODE_CONTEXT_TYPE                    = "CUSTOM_APPROVAL_NODE";
    public static final int    NODE_SNAPSHOT_VERSION                = 1;
    public static final int    EXECUTION_SNAPSHOT_VERSION           = 1;
    public static final String ENGINE_CONFIG_CONTEXT_TYPE           = "CUSTOM_APPROVAL_ENGINES";
    public static final int    ENGINE_CONFIG_SCHEMA_VERSION         = 1;
    public static final String TEAM_CONFIG_CONTEXT_TYPE             = "APPROVAL_TEAMS";
    public static final int    TEAM_CONFIG_SCHEMA_VERSION           = 1;

    public static final int    MAX_CONTEXT_BYTES                    = 60_000;
    public static final int    MAX_COMMENT_BYTES                    = 60_000;

    private CustomApprovalConstants(){
    }
}
