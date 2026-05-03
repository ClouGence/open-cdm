package com.clougence.clouddm.console.web.model.vo.openapi;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsInfoResMsg;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsRuleEntity;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmApiQueryResultVO {

    // basic info
    private String             script;
    private String             original;
    private List<String>       rewriteTags;

    // rule check info
    private WarnLevel          ruleCheckLevel;
    private List<WsRuleEntity> ruleCheckList;

    // result info
    private String             resultId;
    private List<String>       columnList;
    private List<String>       columnType;
    private ReceiveMode        receiveMode;
    private String             cacheFile;

    // cost and data
    private String             receiveCost;
    private int                fetchCount;
    private List<ResultSetRow> rows;

    // message or error
    private List<WsInfoResMsg> msgList;

}
