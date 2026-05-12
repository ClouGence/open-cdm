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
package com.clougence.rdp.global.exception;

import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.rdp.service.enumeration.RSocketSendType;
import com.clougence.clouddm.console.web.util.DmI18nUtils;

public class RemoteInvokeTimeoutException extends RuntimeException {

    private final RSocketSendType sendType;

    private final Long            clusterId;

    private final String          workerIp;

    private final long            passTime;

    private final String          requestId;

    public RemoteInvokeTimeoutException(RSocketSendType sendType, Long clusterId, String workerIp, long passTime, String message, String requestId){
        super(message);
        this.sendType = sendType;
        this.clusterId = clusterId;
        this.workerIp = workerIp;
        this.passTime = passTime;
        this.requestId = requestId;
    }

    public String getI18N() {
        if (sendType == RSocketSendType.CLUSTER) {
            return DmI18nUtils.getMessage(getErrorCode(), String.valueOf(this.passTime), String.valueOf(this.clusterId), this.getMessage());
        } else {
            return DmI18nUtils.getMessage(getErrorCode(), String.valueOf(this.passTime), String.valueOf(this.clusterId), this.workerIp, this.getMessage());
        }
    }

    public ConsoleErrorCode getErrorCode() {
        if (sendType == RSocketSendType.CLUSTER) {
            return ConsoleErrorCode.RPC_INVOKER_CLUSTER_ERROR;
        } else {
            return ConsoleErrorCode.RPC_INVOKER_SPECIFIED_ERROR;
        }
    }
}
