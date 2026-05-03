package com.clougence.clouddm.api.sidecar.session.execute;

import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusDTO {

    // it not going to change.
    private Integer          maxIdleTimeSec;
    private long             createTime;

    // will change.
    private String           curCatalog;
    private String           curSchema;
    private boolean          autoCommit;
    private boolean          readOnly;
    private RdbIsolation     isolation;
    private boolean          hasUnCommitted;

    // instantaneous

    private volatile boolean executing;
    private volatile String  curBatchId;
    private volatile String  curQueryId;
    private volatile long    lastRequestTime;

    private volatile int     waitBatchSize;
    private volatile int     waitQuerySize;
    private volatile int     eventGoods;
}
