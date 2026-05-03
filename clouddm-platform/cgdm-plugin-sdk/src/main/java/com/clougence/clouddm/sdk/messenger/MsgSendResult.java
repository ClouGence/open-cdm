package com.clougence.clouddm.sdk.messenger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/12 9:36 下午
 **/
@Getter
@Setter
public class MsgSendResult {

    private boolean success;
    private String  messageId;
    private String  message;

    private MsgSendResult(String messageId, String message){
        this.success = true;
        this.messageId = messageId;
        this.message = message;
    }

    public static MsgSendResult success(String messageId, String message) {
        MsgSendResult result = new MsgSendResult(messageId, message);
        result.success = true;
        return result;
    }

    public static MsgSendResult failed(String messageId, String message) {
        MsgSendResult result = new MsgSendResult(messageId, message);
        result.success = false;
        return result;
    }
}
