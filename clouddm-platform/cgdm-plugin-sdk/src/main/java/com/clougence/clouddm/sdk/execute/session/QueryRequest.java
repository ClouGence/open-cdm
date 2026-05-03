package com.clougence.clouddm.sdk.execute.session;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryRequest implements Cloneable {

    // Request
    private String                        batchId;
    private String                        queryId;
    private String                        queryBody;
    private List<QueryArg>                queryArgs;
    private SecQueryType                  queryType;
    private DataSourceType                queryDsType;
    private List<Map<TargetType, String>> resource;
    private Requester                     requester;
    private Date                          requestTime;

    // for desensitization
    private boolean                       usingValueProcess;
    private Map<String, List<RealColumn>> columnList;

    // for env, see  ExecuteVariables
    private Map<String, String>           variables;

    // for execute config
    private boolean                       useCallable = false;
    private boolean                       useExplain  = false;
    private boolean                       useCompile  = false;

    // for rewrite
    private boolean                       hasRewrite  = false;
    private List<String>                  rewriteTag;
    private String                        originalBody;

    // Response
    private QueryResultConf               resultConf;

    @Override
    public QueryRequest clone() {
        QueryRequest req = new QueryRequest();
        req.queryId = this.queryId;
        req.queryBody = this.queryBody;
        if (this.queryArgs != null) {
            req.queryArgs = this.queryArgs.stream().map(QueryArg::clone).collect(Collectors.toList());
        }
        req.queryType = this.queryType;
        req.queryDsType = this.queryDsType;
        if (this.resource != null) {
            req.resource = this.resource.stream().map((Function<Map<TargetType, String>, Map<TargetType, String>>) HashMap::new).collect(Collectors.toList());
        }
        req.requester = this.requester;
        req.requestTime = this.requestTime;
        req.usingValueProcess = this.usingValueProcess;

        if (this.variables != null) {
            req.setVariables(new HashMap<>(this.variables));
        }

        req.useCallable = this.useCallable;
        req.resultConf = this.resultConf.clone();
        return req;
    }
}
