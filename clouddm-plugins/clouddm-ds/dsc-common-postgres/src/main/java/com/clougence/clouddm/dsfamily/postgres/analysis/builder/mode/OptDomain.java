package com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode;

import com.clougence.clouddm.sdk.service.secrules.ModeDomain;

import lombok.Getter;

@Getter
public class OptDomain implements ModeDomain {
    private String key;
    private String value;

    public OptDomain(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
