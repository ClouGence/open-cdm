package com.clougence.clouddm.sdk.execute.resultset.echo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSet extends Result {

    private int                fetchCount;
    private String             receiveCost;
    private List<ResultSetRow> rowSet;

    public ResultSet cloneEmpty() {
        ResultSet empty = new ResultSet();
        empty.setResultId(this.getResultId());
        empty.setSessionId(this.getSessionId());
        empty.setQueryId(this.getQueryId());
        empty.setQuerySql(this.getQuerySql());
        empty.setResource(this.getResource());
        empty.setVariables(this.getVariables());
        empty.setSuccess(this.isSuccess());
        empty.setMessage(this.getMessage());
        empty.setCostTimeMs(this.getCostTimeMs());
        empty.setResultType(this.getResultType());
        empty.setHasRewrite(this.isHasRewrite());
        empty.setOriginalScript(this.getOriginalScript());
        empty.setRewriteTag(this.getRewriteTag());
        empty.setFetchCount(this.fetchCount);
        empty.setRowSet(new ArrayList<>());
        return empty;
    }
}
