package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.ModeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnListDomain implements ModeDomain {

    private List<String> columns;

    public ColumnListDomain(List<String> columns){
        this.columns = columns;
    }

}
