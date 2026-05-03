package com.clougence.clouddm.sdk.execute.resultset.echo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSetValue {

    private boolean complete;  // in cache file is complete
    private boolean mask;
    private boolean error;
    private long    moreSize;  // totalSize - value.size
    private long    totalSize; // dataSize
    private String  value;     // data for display

    public static ResultSetValue of(boolean complete, boolean mask, String value, long moreSize, long totalSize) {
        ResultSetValue v = new ResultSetValue();
        v.setError(false);
        v.setComplete(complete);
        v.setMask(mask);
        v.setTotalSize(totalSize);
        v.setMoreSize(moreSize);
        v.setValue(value);
        return v;
    }

    public static ResultSetValue ofError(boolean complete, boolean mask, String message) {
        ResultSetValue v = new ResultSetValue();
        v.setError(true);
        v.setComplete(complete);
        v.setMask(mask);
        v.setTotalSize(0);
        v.setMoreSize(0);
        v.setValue(message);
        return v;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
