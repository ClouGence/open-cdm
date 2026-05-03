package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectItemDomain implements ModeDomain {

    private List<RuleDomain> domainList = new ArrayList<>();
    private String           alias;
    private String           name;

}
