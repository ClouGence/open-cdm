package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WithSelectDomain implements ModeDomain {
    private String          name;
    private RdbSelectDomain selectDomain;
}
