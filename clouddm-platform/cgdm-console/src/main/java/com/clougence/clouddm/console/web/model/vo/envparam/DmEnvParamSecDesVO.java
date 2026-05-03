package com.clougence.clouddm.console.web.model.vo.envparam;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-14 14:02
 */
@Getter
@Setter
@Builder
public class DmEnvParamSecDesVO {

    private boolean openSec;
    private long    id;
    private String  name;
    private String  desc;
}
