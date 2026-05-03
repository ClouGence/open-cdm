package com.clougence.clouddm.sdk.messenger;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/12 9:36 下午
 **/
@Getter
@Setter
public class MsgContent {

    private String       messageId;
    private List<String> atTarget;
    private MsgSendType  type;
    private String       body;

}
