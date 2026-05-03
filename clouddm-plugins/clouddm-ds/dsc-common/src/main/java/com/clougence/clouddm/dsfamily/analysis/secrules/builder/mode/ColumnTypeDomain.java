package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import lombok.Getter;

@Getter
public class ColumnTypeDomain implements ModeDomain {

    private String type;
    private String fullName;
    private String length;

    public ColumnTypeDomain(String type, String fullName, String length){
        this.type = type;
        this.fullName = fullName;
        this.length = length;
    }
}
