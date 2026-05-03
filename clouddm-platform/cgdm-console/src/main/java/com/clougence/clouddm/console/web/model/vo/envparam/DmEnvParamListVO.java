package com.clougence.clouddm.console.web.model.vo.envparam;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-03 17:27
 */
@Getter
@Setter
public class DmEnvParamListVO {

    private long               envId;
    private List<DmSecSpecVO>  ruleMessageVOList;
    private DmEnvApprovalTemVO approvalTemVOS;
}
