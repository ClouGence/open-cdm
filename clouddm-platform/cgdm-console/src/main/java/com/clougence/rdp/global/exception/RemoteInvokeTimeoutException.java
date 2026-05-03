package com.clougence.rdp.global.exception;

import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.rdp.service.enumeration.RSocketSendType;
import com.clougence.rdp.util.RdpI18nUtils;

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
            return RdpI18nUtils.getMessage(getErrorCode(), String.valueOf(this.passTime), String.valueOf(this.clusterId), this.getMessage());
        } else {
            return RdpI18nUtils.getMessage(getErrorCode(), String.valueOf(this.passTime), String.valueOf(this.clusterId), this.workerIp, this.getMessage());
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
