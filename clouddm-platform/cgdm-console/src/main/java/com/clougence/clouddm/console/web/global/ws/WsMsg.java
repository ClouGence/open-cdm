package com.clougence.clouddm.console.web.global.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class WsMsg {

    @JsonIgnore
    private String channelKey;
    private String object;
    private WsType type;
}
