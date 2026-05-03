package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryResultVO {

    private List<WsInfoResMsg>      wsInfoResMsgList    = new ArrayList<>();

    private List<WsResultSetResMsg> resultSetResMsgList = new ArrayList<>();

    private List<WsStatusResMsg>    statusResMsgList    = new ArrayList<>();

    private List<WsRuleResMsg>      wsRuleResMsgList    = new ArrayList<>();
}
