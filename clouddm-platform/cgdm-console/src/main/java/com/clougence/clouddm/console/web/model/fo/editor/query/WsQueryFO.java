package com.clougence.clouddm.console.web.model.fo.editor.query;

import java.util.List;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WsQueryFO {

    @JsonIgnore
    private String         channelKey;
    private String         sessionId;
    private String         primaryUserId;
    private String         currentUserId;
    private WsQueryType    queryType;
    private long           requestTime;
    private String         clientIp;

    private List<String>   levels;
    private String         queryString;
    private List<QueryArg> queryArgs;
    private int            basicCodeLine;
    private int            basicCodeColumn;
    private boolean        force;
    private ReceiveMode    receiveMode;

    private boolean        rdbAutoCommit;
    private boolean        rdbReadOnly;
    private RdbIsolation   rdbIsolation;

    private boolean        viewOriginData;
}
