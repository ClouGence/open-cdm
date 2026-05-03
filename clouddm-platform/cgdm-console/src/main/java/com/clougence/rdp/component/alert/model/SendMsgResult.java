package com.clougence.rdp.component.alert.model;

import java.util.List;

import com.clougence.rdp.service.enumeration.AlertMediaType;

import lombok.Getter;

/**
 * @author bucketli 2023/12/26 11:39:08
 */
@Getter
public class SendMsgResult {

    private final boolean        success;

    private final String         errMsg;

    private final String         content;

    private final AlertMediaType mediaType;

    private final List<String>   sendUids;

    public SendMsgResult(boolean success, String errMsg, String content, AlertMediaType mediaType, List<String> sendUids){
        this.success = success;
        this.errMsg = errMsg;
        this.content = content;
        this.mediaType = mediaType;
        this.sendUids = sendUids;
    }
}
