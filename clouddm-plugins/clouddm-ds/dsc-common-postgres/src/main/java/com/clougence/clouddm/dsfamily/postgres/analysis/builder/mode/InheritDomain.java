package com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode;

import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InheritDomain implements ModeDomain {

    private List<String> tableNames = new ArrayList<>();

}
