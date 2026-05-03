package com.clougence.clouddm.console.web.global.ws;

import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public enum WsType {

    WS_REQ_ECHO("WS_REQ_ECHO"),
    WS_RES_ECHO("WS_RES_ECHO"),

    WS_REQ_QUERY("WS_REQ_QUERY"),
    WS_RES_QUERY("WS_RES_QUERY"),

    WS_RES_ERROR("WS_RES_ERROR"),
    WS_SYS_STATUS("WS_SYS_STATUS"),
    WS_RES_ASYNC_EVENT("WS_RES_ASYNC_EVENT"),
    WS_RES_DRIVER_DOWNLOAD_EVENT("WS_RES_DRIVER_DOWNLOAD_EVENT"),
    WS_RES_EXPORT_EVENT("WS_RES_EXPORT_EVENT"),

    WS_RES_LICENSE_ERROR("WS_RES_LICENSE_ERROR"),;

    private final String wsType;

    WsType(String wsType){
        this.wsType = wsType;
    }

    public static WsType valueOfCode(String code) {
        for (WsType wsType : WsType.values()) {
            if (StringUtils.equalsIgnoreCase(wsType.wsType, code)) {
                return wsType;
            }
        }

        throw new UnsupportedOperationException(code + " not support.");
    }

    public String getCode() { return this.wsType; }
}
