package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhereDomain implements ModeDomain {

    private List<Domain> domains    = new ArrayList<>();
    private boolean      validWhere = false;

}
