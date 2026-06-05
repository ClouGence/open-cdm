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
package com.clougence.clouddm.comm.model;

import lombok.Getter;

/**
 * @author wanshao create time is 2020/11/9
 **/
public enum RSocketDirectionType {

    /**
     * console to sidecar direction
     */
    SERVER_TO_CLIENT("[server->client]"),
    /**
     * sidecar to console direction
     */
    CLIENT_TO_SERVER("[client->server]");

    RSocketDirectionType(String logPrefix){
        this.logPrefix = logPrefix;
    }

    /**
     * when print async process log will use this log prefix to specify the request direction
     */
    @Getter
    private final String logPrefix;
}
