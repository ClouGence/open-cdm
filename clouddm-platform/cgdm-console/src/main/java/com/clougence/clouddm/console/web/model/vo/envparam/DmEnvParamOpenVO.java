package com.clougence.clouddm.console.web.model.vo.envparam;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-04 17:49
 */
@Getter
@Setter
public class DmEnvParamOpenVO {

    private long                  envId;
    private String                envName;
    private String                envDesc;
    private DmEnvParamSecDesVO    secDesVO;
    private DmEnvParamTicketDesVO sqlTicketInfo;
    private DmEnvParamTicketDesVO changeTicketInfo;
    private DmEnvParamTicketDesVO authTicketInfo;
    private boolean               allowAllStatements;
}
