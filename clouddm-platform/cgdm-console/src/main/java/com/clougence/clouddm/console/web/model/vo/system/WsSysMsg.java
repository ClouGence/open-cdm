package com.clougence.clouddm.console.web.model.vo.system;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/6 11:25
 */
@Getter
@Setter
public class WsSysMsg {

    @JsonIgnore
    private String  curUserId;
    @JsonIgnore
    private String  channelKey;

    private boolean serviceReady;
}
