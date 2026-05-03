package com.clougence.clouddm.console.web.component.detectrule.impl;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.console.web.dal.model.DmSecRangeDO;
import com.clougence.clouddm.console.web.dal.model.DmSecRefererDO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class SecRefererWrap {

    private final DmSecRefererDO     referer;
    private final List<DmSecRangeDO> rangeList;

    public SecRefererWrap(DmSecRefererDO referer){
        this.referer = referer;
        this.rangeList = new ArrayList<>();
    }
}
