package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensitiveConfig {

    private long              envId;
    private String            envName;
    private long              dsId;
    private String            dsName;
    private DataSourceType    dsType;

    private String            dsUseSpecName;

    private List<CheckerRule> senRuleList;
}
