package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecRangeVerify;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;
import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RangeVO {

    private long              rangeId;
    private long              refId;
    private SecMatchMode      matchMode;
    private SecRangeType      rangeType;
    private SecRangeVerify    verify;
    private List<String>      verifyMessage;

    private String            desc;

    // levelPrefix for RDP
    private RangeItemVO       env;
    private RangeItemVO       ds;
    private RangeItemVO       catalog;
    private RangeItemVO       schema;
    private RangeItemVO       table;
    private DataSourceType    dsType;
    private TargetType        tableLevelType; // Table or View or Materialized

    // levelNodes
    private List<RangeItemVO> nodes;

    private boolean           chooseAll;
}
