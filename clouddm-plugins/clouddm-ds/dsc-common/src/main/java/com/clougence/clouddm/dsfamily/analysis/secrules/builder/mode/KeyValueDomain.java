package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import com.clougence.clouddm.sdk.service.secrules.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyValueDomain implements Domain {

    private String key;
    private String value;

    public KeyValueDomain(String key, String value){
        this.key = key;
        this.value = value;
    }
}
