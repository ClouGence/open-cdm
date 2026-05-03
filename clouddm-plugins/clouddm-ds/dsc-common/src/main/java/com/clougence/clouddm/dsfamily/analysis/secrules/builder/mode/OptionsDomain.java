package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import java.util.Map;

import com.clougence.clouddm.sdk.service.secrules.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionsDomain implements Domain {

    private Map<String, String> options;

    public OptionsDomain(Map<String, String> options){
        this.options = options;
    }
}
