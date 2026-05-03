package com.clougence.clouddm.console.web.component.whitelist.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Range {

    private long min;
    private long max;

    public Range(long min, long max){
        this.min = min;
        this.max = max;
    }
}
