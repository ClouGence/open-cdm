package com.clougence.clouddm.console.web.component.detectrule.domain;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecRangeVerify;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;
import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecRange {

    private long               rangeId;
    private long               refId;
    private SecMatchMode       matchMode;
    private SecRangeType       rangeType;
    private SecRangeVerify     verify;
    private List<String>       verifyMessage;

    // levelPrefix for RDB
    private SecRangeItem       environment;
    private SecRangeItem       instance;
    private SecRangeItem       catalog;
    private SecRangeItem       schema;
    private SecRangeItem       table;

    private TargetType         tableLevelType; // Table or View or Materialized
    private DataSourceType     dsType;

    // levelNodes
    private List<SecRangeItem> nodes;

    private boolean            chooseAll;
}
