package com.clougence.clouddm.console.web.dal.model;

import com.clougence.clouddm.console.web.dal.enumeration.DmChangeApproveStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeCheckStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeExecStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020/11/9 13:23
 */
@Getter
@Setter
public class DmProjectChangeFlowWalked {

    private DmChangeCheckStrategy   flowCheck;
    private DmChangeApproveStrategy flowApprove;
    private DmChangeExecStrategy    flowExecute;
}
