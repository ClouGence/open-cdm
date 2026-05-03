package com.clougence.clouddm.console.web.model.vo.editor.query;

import com.clougence.clouddm.console.web.dal.enumeration.DataSourceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/6 11:25
 */
@Getter
@Setter
public class WsResMsg {

    @JsonIgnore
    private String           curUserId;
    @JsonIgnore
    private String           channelKey;

    private String           original;
    private String           sessionId;
    private WsResultType     resultType;
    private DataSourceStatus dsStatus;
    private String           dsStatusMessage;
    private int              codeLine;
}
