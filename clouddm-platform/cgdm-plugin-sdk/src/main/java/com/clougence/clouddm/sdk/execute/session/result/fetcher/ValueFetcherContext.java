package com.clougence.clouddm.sdk.execute.session.result.fetcher;

import java.io.File;

import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.result.ReaderOptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueFetcherContext {

    private final ResultColMeta     meta;
    private final ReaderOptions     options;
    private String                  vfcId;
    private File                    tmpFile;
    private ValueFetcherContextData context;
    private boolean                 errStatus = false;
    private Exception               errObject = null;

    public ValueFetcherContext(String vfcId, ResultColMeta meta, ReaderOptions options){
        this.vfcId = vfcId;
        this.meta = meta;
        this.options = options;
    }

    public void free() {
        if (this.context != null) {
            this.context.free();
        }
        if (this.tmpFile != null) {
            if (this.tmpFile.exists()) {
                this.tmpFile.delete();
            }
            this.tmpFile = null;
        }
        this.context = null;
        this.errStatus = false;
        this.errObject = null;
    }

    public void markError(Exception e) {
        this.errStatus = true;
        this.errObject = e;
    }
}
