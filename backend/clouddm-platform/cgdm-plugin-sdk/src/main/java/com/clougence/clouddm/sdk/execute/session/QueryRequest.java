/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.sdk.execute.session;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.execute.session.result.ColumnConfig;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryRequest implements Cloneable {

    // Request
    private long                      index;
    private String                    batchId;
    private String                    queryId;
    private String                    queryBody;
    private List<QueryArg>            queryArgs;
    private int                       bodyStartCodeLine;
    //
    private Set<StatementType>        queryTypes;
    private Long                      dsId;
    private DataSourceType            dsType;
    private List<BehaviorRelation>    relations;
    private Requester                 requester;
    private Date                      requestTime;

    // for masking
    private boolean                   usingValueProcess;
    private Map<String, ColumnConfig> columnList;

    // for execute config
    private boolean                   useCallable = false;
    private boolean                   useExplain  = false;
    private boolean                   useCompile  = false;

    // for rewrite
    private boolean                   hasRewrite  = false;
    private List<String>              rewriteTag;
    private String                    originalBody;

    // Response
    private QueryResultConf           resultConf;

    @Override
    public QueryRequest clone() {
        QueryRequest req = new QueryRequest();
        req.index = this.index;
        req.batchId = this.batchId;
        req.queryId = this.queryId;
        req.queryBody = this.queryBody;
        req.bodyStartCodeLine = this.bodyStartCodeLine;
        if (this.queryArgs != null) {
            req.queryArgs = this.queryArgs.stream().map(QueryArg::clone).collect(Collectors.toList());
        }
        if (this.queryTypes != null) {
            req.queryTypes = new LinkedHashSet<>(this.queryTypes);
        }
        if (this.relations != null) {
            req.relations = List.copyOf(this.relations);
        }
        req.dsId = this.dsId;
        req.dsType = this.dsType;
        req.requester = this.requester;
        req.requestTime = this.requestTime;
        req.usingValueProcess = this.usingValueProcess;
        if (this.columnList != null) {
            req.columnList = new LinkedHashMap<>(this.columnList);
        }

        req.useCallable = this.useCallable;
        req.useExplain = this.useExplain;
        req.useCompile = this.useCompile;
        req.hasRewrite = this.hasRewrite;
        if (this.rewriteTag != null) {
            req.rewriteTag = new ArrayList<>(this.rewriteTag);
        }
        req.originalBody = this.originalBody;
        req.resultConf = this.resultConf == null ? null : this.resultConf.clone();
        return req;
    }

    public boolean hasQueryType(StatementType queryType) {
        return this.queryTypes != null && this.queryTypes.contains(queryType);
    }

    public void addRewriteTag(String tag) {
        if (this.rewriteTag != null && this.rewriteTag.contains(tag)) {
            return;
        }
        List<String> tags = this.rewriteTag == null ? new ArrayList<>() : new ArrayList<>(this.rewriteTag);
        tags.add(tag);
        this.rewriteTag = tags;
    }
}
