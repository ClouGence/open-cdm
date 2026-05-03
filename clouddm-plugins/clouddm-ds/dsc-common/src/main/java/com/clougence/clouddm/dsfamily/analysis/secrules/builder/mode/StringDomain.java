package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import lombok.Getter;

@Getter
public class StringDomain implements ModeDomain {

    private String val;

    public StringDomain(String val){
        this.val = val;
    }
}
