package com.clougence.clouddm.sdk.execute.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultLimit implements Cloneable {

    // some limit
    private long fetchRecordCountLimit;
    private long fetchResultSetBytesLimit;
    private long fetchColumnBytesLimit;
    private long fetchElementBytesLimit;
    private long fetchPageSize;
    private int  queryTimeoutSec;

    @Override
    public ResultLimit clone() {
        ResultLimit conf = new ResultLimit();

        conf.fetchRecordCountLimit = this.fetchRecordCountLimit;
        conf.fetchResultSetBytesLimit = this.fetchResultSetBytesLimit;
        conf.fetchColumnBytesLimit = this.fetchColumnBytesLimit;
        conf.fetchElementBytesLimit = this.fetchElementBytesLimit;
        conf.fetchPageSize = this.fetchPageSize;
        conf.queryTimeoutSec = this.queryTimeoutSec;
        return conf;
    }
}
