package com.clougence.clouddm.sdk.messenger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/12 9:36 下午
 **/
@Getter
@Setter
public class MsgSendConfig {

    private String webhookUrl;
    private String secret;
}
