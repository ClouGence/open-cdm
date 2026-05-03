package com.clougence.clouddm.sdk.execute.session;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2022/2/17 17:02:59
 */
@Getter
@Setter
public class ResultColMeta {

    private final ColMetaData  meta;
    private final ValueFetcher fetcher;

    public ResultColMeta(ColMetaData meta, ValueFetcher fetcher){
        this.meta = meta;
        this.fetcher = fetcher;
    }
}
