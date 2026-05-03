package com.clougence.clouddm.sdk.execute.resultset.echo;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    private String                        batchId;
    private String                        resultId;

    // Request
    private String                        sessionId;
    private String                        queryId;
    private String                        querySql;
    private List<Map<TargetType, String>> resource;
    private Map<String, String>           variables;
    // for status
    private boolean                       success;
    private String                        message;
    // for cost
    private long                          costTimeMs;
    private ResultType                    resultType;
    // for rewrite
    private boolean                       hasRewrite;
    private String                        originalScript;
    private List<String>                  rewriteTag;
}
